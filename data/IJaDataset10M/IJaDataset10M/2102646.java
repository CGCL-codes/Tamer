package com.ilog.translator.java2cs.configuration.target;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.ilog.translator.java2cs.configuration.ChangeModifierDescriptor;
import com.ilog.translator.java2cs.configuration.info.Constants;
import com.ilog.translator.java2cs.configuration.info.ElementInfo;
import com.ilog.translator.java2cs.translation.noderewriter.FieldRewriter;

/**
 * 
 * @author afau
 * 
 * Represents a 'target field' i.e. the description (package & name) of a field
 * in a C# classes.
 * 
 * options :
 *    - format
 *    
 */
public class TargetField extends TargetMemberElement {

    /**
	 * 
	 */
    public TargetField() {
        super(null, null);
        rewriter = new FieldRewriter();
    }

    /**
	 * Create a new target field with given name.
	 * 
	 * @param name
	 *            The name of this target field.
	 */
    public TargetField(String fname) {
        super(null, fname);
        setRewriter(new FieldRewriter(fname));
        this.name.parse(fname);
    }

    /**
	 * 
	 * @param dummy
	 * @param pattern
	 */
    public TargetField(int dummy, String pattern) {
        super(null, null);
        this.pattern.setValue(pattern);
        setRewriter(new FieldRewriter(dummy, pattern));
    }

    /**
	 * 
	 * @param mod
	 */
    public TargetField(ChangeModifierDescriptor mod) {
        setChangeModifierDescriptor(mod);
        rewriter = new FieldRewriter(mod);
    }

    @Override
    public FieldRewriter getRewriter() {
        FieldRewriter rew = (FieldRewriter) super.getRewriter();
        rew.setChangeModifier(changeModifiersDescriptor);
        rew.setName(name.getValue());
        rew.setFormat(pattern.getValue());
        if (!type.isDefaultValue()) rew.setReturnType(type.getValue());
        return rew;
    }

    @Override
    public String toString() {
        String descr = "";
        descr += ((packageName == null) ? "" : packageName + ".") + name;
        return descr;
    }

    public void toXML(StringBuilder descr, String tabValue) {
        descr.append(">\n");
        descr.append(Constants.FOURTAB + "<target");
        xmlAttributeInTargetPart(descr);
        if (needTargetPart()) {
            descr.append(">\n");
            xmlElementsInTargetPart(descr);
            ;
            descr.append(Constants.FOURTAB + "</target>\n");
        } else {
            descr.append("/>\n");
        }
    }

    public void xmlAttributeInTargetPart(StringBuilder descr) {
        ElementInfo.toXML(descr, name, " ", "", "");
        ElementInfo.toXML(descr, translated, " ", "", "");
        ElementInfo.toXML(descr, renamed, " ", "", "");
        ElementInfo.toXML(descr, type, " ", "", "");
        ElementInfo.toXML(descr, dotnetFramework, "", "", "");
    }

    public void xmlElementsInTargetPart(StringBuilder descr) {
        ElementInfo.toXML(descr, pattern, "", "", Constants.FIVETAB);
        ElementInfo.toXML(descr, comments, "", "", Constants.FIVETAB);
        ElementInfo.toXML(descr, getChangeModifierDescriptor(), Constants.FIVETAB);
    }

    private boolean needTargetPart() {
        return !pattern.isDefaultValue() || getChangeModifierDescriptor() != null || !type.isDefaultValue() || !comments.isDefaultValue();
    }

    public void fromXML(Element pckElement) {
        name.fromXML(pckElement);
        translated.fromXML(pckElement);
        renamed.fromXML(pckElement);
        pattern.fromXML(pckElement);
        type.fromXML(pckElement);
        dotnetFramework.fromXML(pckElement);
        if (!type.isDefaultValue()) {
            ((FieldRewriter) getRewriter()).setReturnType(type.getValue());
        }
        comments.fromXML(pckElement);
        NodeList lNodes = pckElement.getChildNodes();
        if (lNodes != null) {
            for (int i = 0; i < lNodes.getLength(); i++) {
                Node child = lNodes.item(i);
                if (child.getNodeName().equals("modifiers")) {
                    if (changeModifiersDescriptor == null) changeModifiersDescriptor = new ChangeModifierDescriptor();
                    changeModifiersDescriptor.fromXML((Element) child);
                }
            }
        }
    }
}

;
