package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.dom.Attribute;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.Text;
import com.volantis.mcs.protocols.DescendantStylesMerger;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.Styles;
import java.util.HashSet;

/**
 * Holds the name and optional prototype sibling for use during an element
 * re-mapping exercise. It also indicates whether the element can be
 * "optimized away" if none of the 'important' style properties are set on
 * the element's Styles. The sibling is intended to be used as a prototype
 * pattern source. If the element is to always be optimized away, the name
 * should be set to null.
 */
public class BasicMapperElement implements TransMapper {

    /**
     * The name to which an element should be mapped.
     */
    private String name;

    /**
     * A prototypical Node instance that describes an optional new sibling
     * for the element whose name is being re-mapped to the associated name in
     * this mapper element. May be null if no new sibling is required.
     */
    private Node sibling;

    /**
     * If set true the element can be optimized away (instead of being renamed)
     * when none of the 'important' property values are set on the element's
     * Styles. Even when set to false optimization may occur if the element
     * contains a single table or div element.
     */
    private boolean optimizeIfNoStyles;

    /**
     * The display keyword to use if creating or changing an element.
     */
    private final StyleKeyword display;

    /**
     * The set of attributes that can be retained if the element is retained
     * and re-mapped. Can be null if no attributes may be retained.
     */
    private HashSet validAttributes = null;

    /**
     * The {@link TransformationConfiguration} which should be used when
     * remapping elements. This may be a default configuration if there is no
     * protocol specific configuration required for transformation.
     */
    private TransformationConfiguration transformationConfiguration;

    /**
     * Initialize the new instance using the attributes given. This version
     * doesn't allow any attributes to be retained.
     *
     * @param name      the new element name to be used if, when re-mapping is
     *                  applied, the element is to be retained
     * @param sibling   a prototypical instance of a sibling to be inserted
     *                  after the element being updated, or null
     * @param optimizeIfNoStyles if <code>true</code> the element can be
     *                  optimized away if its Styles doesn't have values set
     *                  for any of the properties that have been deemed
     *                  significant.
     */
    public BasicMapperElement(String name, Node sibling, boolean optimizeIfNoStyles) {
        this.name = name;
        this.sibling = sibling;
        this.optimizeIfNoStyles = optimizeIfNoStyles;
        this.display = null;
    }

    /**
     * Initialize the new instance using the attributes given.
     *
     * @param name      the new element name to be used if, when re-mapping is
     *                  applied, the element is to be retained
     * @param validAttributes the names of attributes that can be retained
     *                  after re-mapping
     * @param sibling   a prototypical instance of a sibling to be inserted
     *                  after the element being updated, or null
     * @param optimizeIfNoStyles if <code>true</code> the element can be
     *                  optimized away if its Styles doesn't have values set
     *                  for any of the properties that have been deemed
     *                  significant.
     * @param configuration used when remapping elements to determine if the
     *                  element to be remapped has any style information that
     * @param display   the display for the element.
     */
    public BasicMapperElement(String name, String[] validAttributes, Node sibling, boolean optimizeIfNoStyles, TransformationConfiguration configuration, StyleKeyword display) {
        this.name = name;
        this.sibling = sibling;
        this.optimizeIfNoStyles = optimizeIfNoStyles;
        this.transformationConfiguration = configuration;
        this.display = display;
        if (validAttributes != null) {
            int i;
            int size = validAttributes.length;
            this.validAttributes = new HashSet(size);
            for (i = 0; i < size; i++) {
                this.validAttributes.add(validAttributes[i]);
            }
        }
    }

    /**
     * Return the new element name to be used if, when re-mapping is applied,
     * the element is to be retained.
     *
     * @return the new element name to be used if, when re-mapping is applied,
     * the element is to be retained
     */
    public String getName() {
        return name;
    }

    /**
     * Set the new element name to be used if, when re-mapping is applied, the
     * element is to be retained.
     *
     * @param name      the new element name to be used if, when re-mapping is
     *                  applied, the element is to be retained
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The returned value should be used as a prototype instance from which
     * a copy should be made. The alternative method, getSiblingInstance,
     * is provided for this purpose.
     */
    public Node getSibling() {
        return sibling;
    }

    /**
     * Returns a new sibling instance based on the prototype sibling member
     * (or null if no sibling is defined). Note that, in the case of an
     * Element sibling, the "clone" is made via a shallow copy. If the
     * sibling is non-null and is not a Text or Element node null is always
     * returned.
     */
    public Node getSiblingInstance() {
        Node result = null;
        if (sibling != null) {
            DOMFactory factory = sibling.getDOMFactory();
            if (sibling instanceof Element) {
                Element element;
                Element prototype = (Element) sibling;
                element = factory.createElement();
                element.copy(prototype);
                result = element;
            } else if (sibling instanceof Text) {
                Text text;
                Text prototype = (Text) sibling;
                text = factory.createText();
                text.append(prototype.getContents(), 0, prototype.getLength());
                text.setEncoded(prototype.isEncoded());
                result = text;
            }
        }
        return result;
    }

    /**
     * Allows a sibling node prototype to be set. Can be null.
     *
     * @param sibling the prototype instance to be used
     */
    public void setSibling(Node sibling) {
        this.sibling = sibling;
    }

    /**
     * Returns true if the element should always be optimized away if it has no
     * values set for any of the 'important' style properties.
     *
     * @return true if the element should always be optimized away if it has no
     * values set for any of the 'important' style properties
     */
    public boolean isOptimizeIfNoStyles() {
        return optimizeIfNoStyles;
    }

    /**
     * Allows the optimization status to be set.
     *
     * @param optimizeIfNoStyles    indicates whether the element should always
     *                              be optimized away if it has no values set
     *                              for any of the 'important' style properties.
     */
    public void setOptimizeIfNoStyles(boolean optimizeIfNoStyles) {
        this.optimizeIfNoStyles = optimizeIfNoStyles;
    }

    /**
     * Specifically re-maps the given element using the values held in the
     * instance attributes.
     *
     * @param element the element that needs to be remapped
     * @param helper the element helper in use
     */
    public void remap(Element element, ElementHelper helper) {
        boolean optimizeAway = (isOptimizeIfNoStyles() && !transformationConfiguration.optimisationWouldLoseImportantStyles(element));
        if (!optimizeAway && (element.getHead() == element.getTail())) {
            optimizeAway = helper.isTable(element.getHead());
            String name = element.getName();
            if (!optimizeAway && (name != null) && (element.getHead() instanceof Element)) {
                Element subElement = (Element) element.getHead();
                optimizeAway = name.equals(subElement.getName());
            }
        }
        if (optimizeAway) {
            element.setName(null);
            if (element.getStyles() != null) {
                DescendantStylesMerger merger = new DescendantStylesMerger();
                merger.pushStylesDown(element);
            }
            element.clearStyles();
        } else {
            element.setName(getName());
            Styles styles = element.getStyles();
            if (styles != null) {
                styles.getPropertyValues().setComputedValue(StylePropertyDetails.DISPLAY, display);
            }
            if (validAttributes == null) {
                element.clearAttributes();
            } else {
                Attribute attribute;
                Attribute nextAttribute;
                String attrName;
                for (attribute = element.getAttributes(); attribute != null; attribute = nextAttribute) {
                    nextAttribute = attribute.getNext();
                    attrName = attribute.getName();
                    if (!validAttributes.contains(attrName)) {
                        element.removeAttribute(attrName);
                    }
                }
            }
        }
        if (getSibling() != null) {
            getSiblingInstance().insertAfter(element);
        }
    }
}
