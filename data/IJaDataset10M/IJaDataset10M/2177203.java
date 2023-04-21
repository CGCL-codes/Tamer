package com.evaserver.rof.script;

/**
 * @author Max Antoni
 * @version $Revision: 36 $
 */
class THtmlScriptElement extends TElement {

    static final TObject SCRIPT_PROTOTYPE;

    static {
        SCRIPT_PROTOTYPE = new TElement(null, new TString("script"));
        SCRIPT_PROTOTYPE.putOwnProperty(new AttrProperty("charset", Property.DONT_DELETE));
        SCRIPT_PROTOTYPE.putOwnProperty(new AttrProperty("defer", Property.DONT_DELETE));
        SCRIPT_PROTOTYPE.putOwnProperty(new AttrProperty("src", Property.DONT_DELETE));
        SCRIPT_PROTOTYPE.putOwnProperty(new AttrProperty("type", Property.DONT_DELETE));
        SCRIPT_PROTOTYPE.putOwnProperty(new AttrProperty("htmlFor", "for", Property.DONT_DELETE));
        SCRIPT_PROTOTYPE.putOwnProperty(new PropertyNative("text", Property.DONT_DELETE) {

            TType getValue(TObject inThis) throws ScriptException {
                return new TString(((THtmlScriptElement) inThis).getText().toString());
            }

            void setValue(TObject inThis, TType inValue) throws ScriptException {
                ExecutionContext ec = new ExecutionContextRoot(inThis, 1000, 10);
                ((THtmlScriptElement) inThis).setText(inValue.toJSString(ec));
            }
        });
    }

    /**
     * @param inDocument the document.
     * @param inNodeName the name of the node.
     */
    public THtmlScriptElement(TDocument inDocument, TString inNodeName) {
        super(SCRIPT_PROTOTYPE, inDocument, inNodeName);
    }

    TNode createClone() {
        return new THtmlScriptElement(getOwnerDocument(), getNodeName());
    }

    StringBuffer getText() {
        StringBuffer buffer = new StringBuffer();
        TNodeList l = getChildNodes();
        for (int i = 0; i < l.getLength(); i++) {
            TText text = (TText) l.item(i);
            if (i > 0) {
                buffer.append('\n');
            }
            buffer.append(text.getNodeValue());
        }
        return buffer;
    }

    void setText(TString inText) throws ScriptException {
        while (hasChildNodes()) {
            removeChild((TNode) getLastChild());
        }
        appendChild(getOwnerDocument().createTextNode(inText));
    }

    void toHtml(StringBuffer inoutBuffer, String inIndent, ToHtmlConfig inToHtmlConfig) {
        boolean isInline = isInlineTag(this);
        if (!isInline) {
            int bufferLength = inoutBuffer.length();
            if (bufferLength > 0 && inoutBuffer.charAt(bufferLength - 1) != '\n') {
                inoutBuffer.append("\r\n");
            }
            inoutBuffer.append(inIndent);
        }
        inoutBuffer.append("<script");
        attributesToHtml(inoutBuffer);
        inoutBuffer.append('>');
        StringBuffer buffer = getText();
        if (buffer.length() > 0) {
            inoutBuffer.append("\r\n");
            inoutBuffer.append(inIndent);
            inoutBuffer.append("// <![CDATA[\r\n");
            inoutBuffer.append(inIndent);
            inoutBuffer.append("\t");
            inoutBuffer.append(buffer);
            inoutBuffer.append("\r\n");
            inoutBuffer.append(inIndent);
            inoutBuffer.append("// ]]>\r\n");
            inoutBuffer.append(inIndent);
        }
        inoutBuffer.append("</script>");
        if (!isInline) {
            inoutBuffer.append("\r\n");
        }
    }
}
