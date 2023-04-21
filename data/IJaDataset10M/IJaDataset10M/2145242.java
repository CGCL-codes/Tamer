package org.xaware.server.engine.instruction;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.XAwareException;
import org.xaware.shared.util.logging.XAwareLogger;
import java.util.*;

/**
 * 
 * <p>
 * <code>XAOptional([parameters])</code> is an XA-Script Instruction that treats the enclosing element as
 * optional in the BizDocument result, removing it from the BizDocument results if certain conditions hold
 * relating to the element and its children.  The default if no value is supplied is:
 * 
 * With no parameter, the default behavior is to remove the element if these conditions hold:
 * <ol>
 * <li>The text value of the element must either be blank or have an unresolved reference (indicating a failed
 * substitution).
 * <li>Each attribute that is present must either be blank or have an unresolved reference (indicating a failed
 * substitution).
 * <li>The element has no children.
 * </ol>
 * 
 * The 0 to all of the following parameters can be passed as the value of the attribute as a comma separated list:
 * <ul>
 * <li>ignoreBlank -Do not consider a blank value (0-length string or all whitespace characters) as cause to remove an
 * optional element. Set this parameter if you only want to remove elements with failed substitutions. If present,
 * ignoreSubstitutionFailure must NOT be present.
 * <li>ignoreSubstitutionFailure -Do not consider a failed substitution as cause to remove an optional element. If
 * present, ignoreElementValue must NOT be present.
 * <li>ignoreAttributes -Attribute values will not be considered in determining whether to remove the element.
 * <li>ignoreElementValue -Element values will not be considered in determining whether to remove the element.
 * <li>ignoreChildren -The presence of children elements does not affect whether this element is removed (in contrast to
 * the default case).
 * <li>checkChildren -When other conditions are met for removing an element, these same conditions are applied
 * recursively on all children elements. The element (and its descendants) is removed only if every descendant element
 * meets these same conditions.
 * <li>applyOnChildren -The conditions applied to check for removal of this element are applied individually on each
 * descendant element. This is useful to remove all "unused" elements from an entire subtree.
 * <li>applyOnAttributes -The conditions applied to check for removal are applied individually on each attribute.  This is useful to remove "unused" attributes.
 * <li>treatWhitespaceAsData -Treat whitespace characters (space, non-breaking space, tab, etc.) in element values as
 * significant data. If white space characters are detected, the value is considered to be non-blank. 
 * <li>required -During the processing of parameter applyOnChildren, this parameter can be applied on any descendant to
 * override normal behavior. If an element is a descendant of an element with xa:optional=applyOnChildren,
 * and the element has xa:optional=required, then the element will not be removed, regardless of its value, attribute,
 * or presence of children.  Note that this value is valid only on an element that is a child of an element with
 * xa:optional=applyOnChildren, but no validation is performed.
 * </ul>
 * <p/>
 * 
 * This instruction command should always be used as a post process command.
 * The xa:optional command has three aspects to its operation:
 * <ol>
 * <li>What condition do we check? Are we looking for a blank in an particular XML construct, or for substitution
 * errors, or both? By default we look for both, and allow overriding this behavior with ignoreBlank or
 * ignoreSubstitutionFailure.  Note that it is illegal to ignore both of these, since we would then not be checking
 * a condition.
 * <li>What XML constructs are we checking? Are we looking at an element value, an attribute, or the presence of
 * children?
 * 
 * By default we are checking for all three, and this can be overridden with parameters ignoreElementValue ,
 * ignoreAttributes,
 * 
 * and ignoreChildren.
 * 
 * <li>Do we extend the checking into an elements descendants? This behavior is controlled by children processing
 * parameters.
 * 
 * ignoreChildren means we don�t care if there are children or not, and the checking performed on the parent element
 * solely
 * 
 * determines whether the element is removed. checkChildren means we apply the same checking on all descendant
 * elements, and only remove the element if all descendants also meet the criteria for removal. applyOnChildren
 * is a shortcut to propagate the checking automatically to all descendant elements. This is a quick way to remove all elements that are
 * blank or had a substitution failure.
 * </ol>
 * 
 * <p>
 * Usage:
 * 
 * <xmp>
 * <elem xa:optional="applyOnChildren,ignoreBlank,ignoreSubstitutionFailure,ignoreAttributes,ignoreElementValue,ignoreChildren,checkChildren,applyOnChildren,applyOnAttributes,treatWhitespaceAsData,required" xmlns:xa="http://xaware.org/xas/ns1" />
 * </xmp>
 * 
 * 
 * @author Tim Ferguson based on the extension created by Kirstan Vandersluis and P Richards
 * 
 * @version 1.0
 */
public class XAOptionalInst extends Instruction {

    protected static XAwareLogger lf = XAwareLogger.getXAwareLogger("XAOptionalBaseInst");

    public static final String xawareNamespacePrefix = XAwareConstants.XAWARE_NAMESPACE.substring(0, 17);

    protected String value = null;

    /**
     * Return false, because all the work is done in init().
     *
     * @see org.xaware.server.engine.IInstruction#hasMoreExecutions()
     */
    @Override
    public boolean hasMoreExecutions() {
        return false;
    }

    /**
     * All the work has been done in init().
     *
     * @see org.xaware.server.engine.IInstruction#execute()
     */
    @Override
    public void execute() throws XAwareException {
        throw new XAwareException("Method not implemented");
    }

    @Override
    protected void config() throws XAwareException {
        this.value = this.instAttribute.getValue();
    }

    @Override
    protected void init() throws XAwareException {
        try {
            if (value.equals("required")) {
                scriptNode.getElement().setAttribute(instAttribute);
            } else {
                process();
            }
            this.completed = true;
        } catch (JDOMException e) {
            throw new XAwareException(e);
        }
    }

    /**
     * Get a String representing the name of this Instruction
     * 
     * @see org.xaware.server.engine.IInstruction#getName()
     */
    @Override
    public String getName() {
        return "Second pass " + super.getName();
    }

    /**
     * <p>
     * This method is called to process the instruction attribute.
     * </p>
     * 
     */
    protected void process() throws JDOMException {
        Element elem = scriptNode.getElement();
        lf.fine("XA-Script instruction: optional on element " + elem.getName() + ", parameters:" + value);
        boolean ignoreBlank = false;
        boolean ignoreSubstitutionFailure = false;
        boolean ignoreAttributes = false;
        boolean ignoreElementValue = false;
        boolean ignoreChildren = false;
        boolean checkChildren = false;
        boolean applyOnChildren = false;
        boolean applyOnAttributes = false;
        boolean treatWhitespaceAsData = false;
        boolean required = false;
        if (value.length() > 0) {
            if (value.indexOf("ignore") >= 0) {
                ignoreBlank = value.indexOf("ignoreBlank") >= 0;
                ignoreSubstitutionFailure = value.indexOf("ignoreSubstitutionFailure") >= 0;
                ignoreAttributes = value.indexOf("ignoreAttributes") >= 0;
                ignoreElementValue = value.indexOf("ignoreElementValue") >= 0;
                ignoreChildren = value.indexOf("ignoreChildren") >= 0;
            }
            checkChildren = value.indexOf("checkChildren") >= 0;
            applyOnChildren = value.indexOf("applyOnChildren") >= 0;
            applyOnAttributes = value.indexOf("applyOnAttributes") >= 0;
            treatWhitespaceAsData = value.indexOf("treatWhitespaceAsData") >= 0;
            required = value.indexOf("required") >= 0;
        }
        if (checkAndRemove(elem, ignoreBlank, ignoreSubstitutionFailure, ignoreAttributes, ignoreElementValue, ignoreChildren, checkChildren, applyOnChildren, applyOnAttributes, treatWhitespaceAsData, required)) {
            elem.getParentElement().removeContent(elem);
        }
    }

    /**
     * 
     * checkAndRemove - check if element can be removed
     */
    protected boolean checkAndRemove(Element elem, boolean ignoreBlank, boolean ignoreSubstitutionFailure, boolean ignoreAttributes, boolean ignoreElementValue, boolean ignoreChildren, boolean checkChildren, boolean applyOnChildren, boolean applyOnAttributes, boolean treatWhitespaceAsData, boolean required) {
        if (isRemovable(elem, ignoreBlank, ignoreSubstitutionFailure, ignoreAttributes, ignoreElementValue, ignoreChildren, checkChildren, applyOnChildren, applyOnAttributes, treatWhitespaceAsData, required)) {
            lf.fine("XAOptional:removing element due to lack of qualifying content:" + elem.getName());
            return true;
        } else {
            if (applyOnAttributes) {
                Iterator<?> iter = elem.getAttributes().iterator();
                List<Attribute> attrsToRemove = new ArrayList<Attribute>();
                while (iter.hasNext()) {
                    Attribute attr = (Attribute) iter.next();
                    if (isValueRemovable(attr.getValue(), ignoreBlank, ignoreSubstitutionFailure, treatWhitespaceAsData)) {
                        attrsToRemove.add(attr);
                    }
                }
                for (Attribute attr : attrsToRemove) {
                    elem.removeAttribute(attr);
                }
            }
        }
        return false;
    }

    /**
     * 
     * isRemovable - check if element can be removed
     */
    protected boolean isRemovable(Element elem, boolean ignoreBlank, boolean ignoreSubstitutionFailure, boolean ignoreAttributes, boolean ignoreElementValue, boolean ignoreChildren, boolean checkChildren, boolean applyOnChildren, boolean applyOnAttributes, boolean treatWhitespaceAsData, boolean required) {
        boolean removeElem = true;
        if (applyOnChildren) {
            String optional = elem.getAttributeValue(this.instAttribute.getName(), this.instAttribute.getNamespace());
            if (optional != null && optional.equals("required")) {
                elem.removeAttribute(this.instAttribute.getName(), this.instAttribute.getNamespace());
                removeElem = false;
            }
        }
        if (applyOnChildren && elem.getChildren().size() > 0) {
            Iterator<?> iter = elem.getChildren().iterator();
            List<Element> elemsToRemove = new ArrayList<Element>();
            while (iter.hasNext()) {
                Element child = (Element) iter.next();
                if (checkAndRemove(child, ignoreBlank, ignoreSubstitutionFailure, ignoreAttributes, ignoreElementValue, ignoreChildren, checkChildren, applyOnChildren, applyOnAttributes, treatWhitespaceAsData, required)) {
                    elemsToRemove.add(child);
                }
            }
            for (Element child : elemsToRemove) {
                elem.removeContent(child);
            }
        }
        if (!ignoreElementValue) {
            String value = elem.getText();
            if (!isValueRemovable(value, ignoreBlank, ignoreSubstitutionFailure, treatWhitespaceAsData)) {
                removeElem = false;
            }
        }
        if (!ignoreAttributes) {
            Iterator<?> iter = elem.getAttributes().iterator();
            while (iter.hasNext()) {
                Attribute attr = (Attribute) iter.next();
                if (attr.getNamespaceURI().startsWith(xawareNamespacePrefix)) continue;
                if (!isValueRemovable(attr.getValue(), ignoreBlank, ignoreSubstitutionFailure, treatWhitespaceAsData)) {
                    removeElem = false;
                }
            }
        }
        if (!ignoreChildren && !checkChildren) {
            if (elem.getChildren().size() > 0) {
                removeElem = false;
            }
        }
        if (checkChildren) {
            if (elem.getChildren().size() > 0) {
                Iterator<?> iter = elem.getChildren().iterator();
                while (iter.hasNext()) {
                    Element child = (Element) iter.next();
                    if (!isRemovable(child, ignoreBlank, ignoreSubstitutionFailure, ignoreAttributes, ignoreElementValue, ignoreChildren, checkChildren, applyOnChildren, applyOnAttributes, treatWhitespaceAsData, required)) {
                        removeElem = false;
                    }
                }
            }
        }
        return removeElem;
    }

    /**
     * 
     * checkValue - check a text value against current settings
     */
    protected boolean isValueRemovable(String value, boolean ignoreBlank, boolean ignoreSubstitutionFailure, boolean treatWhitespaceAsData) {
        if (!treatWhitespaceAsData) value = value.trim();
        if (!ignoreBlank && value.length() > 0) {
            if (ignoreSubstitutionFailure) {
                return false;
            } else {
                int iLoc;
                if ((iLoc = value.indexOf('%')) >= 0 && value.indexOf('%', iLoc + 1) >= 0) {
                    ;
                } else {
                    return false;
                }
            }
        } else if (ignoreBlank) {
            int iLoc;
            if ((iLoc = value.indexOf('%')) >= 0 && value.indexOf('%', iLoc + 1) >= 0) {
                ;
            } else {
                return false;
            }
        }
        return true;
    }
}
