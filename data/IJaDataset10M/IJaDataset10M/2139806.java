package org.jato.tags;

import org.jato.*;
import org.jdom.*;

public abstract class TextScriptTag extends DefaultScriptTag {

    public static final String TEXT = "text";

    public TextScriptTag() {
        super(TEXT);
    }

    public TextScriptTag(String format) throws JatoException {
        super(TEXT);
        setFormatText(format);
    }

    protected StringBuffer describeAttributes(StringBuffer buf) {
        buf = super.describeAttributes(buf);
        attr(buf, "iterate", this.getIterate());
        attr(buf, "skip-null", String.valueOf(this.getSkipNulls()));
        return buf;
    }

    public void process(Interpreter jato, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException {
        Object obj = null;
        try {
            obj = getTextValue(jato, thisClass, thisObj, xmlIn, xmlOut);
            if ((obj == null) && (getSkipNulls() == true)) {
                return;
            } else {
                processChildrenWith(obj, jato, thisClass, thisObj, xmlIn, xmlOut);
                String text = "";
                if (getFormat() != null) {
                    text = getFormat().format(obj, jato, this, thisClass, thisObj, xmlIn, xmlOut);
                } else if (obj != null) {
                    text = obj.toString();
                }
                xmlOut.addContent(text);
            }
        } catch (Exception ex) {
            throw new JatoException("Could not get text value for element " + xmlOut, ex);
        }
    }

    public abstract Object getTextValue(Interpreter jato, Class thisClass, Object thisObj, Element xmlIn, Element xmlOut) throws JatoException;

    public ScriptTag[] getTextTemplate() throws JatoException {
        ScriptTag[] init = new ScriptTag[3];
        init[0] = getFormatTemplate();
        init[1] = getIterateTemplate();
        init[2] = getSkipNullsTemplate();
        return init;
    }
}
