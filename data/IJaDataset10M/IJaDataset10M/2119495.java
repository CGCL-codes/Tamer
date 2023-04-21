package net.ajaxforms.compiler.xforms;

import net.ajaxforms.compiler.XElement;
import net.ajaxforms.compiler.XNode;

public class XFormsElement extends XElement {

    private String varId;

    public XFormsElement(String localName) {
        super(XFormsLibrary.NAMESPACE, localName);
        this.varId = getId();
    }

    public String getVarId() {
        return varId;
    }

    public String toScriptParam(String value) {
        return value == null ? "null" : "'" + value + "'";
    }

    public String toScriptParam(String value, String defaultValue) {
        return "'" + (value == null ? defaultValue : value) + "'";
    }

    public String toScriptParam(XNode node) {
        return node == null ? "null" : node.toScript();
    }

    public String toScriptParam(XNode node, String defaultValue) {
        return node == null ? defaultValue : node.toScript();
    }

    public String toStriptBinding(String name) {
        XNode bind = getAttribute("bind");
        if (bind != null) {
            return "new Binding(null, null, " + bind.toScript() + ")";
        }
        XNode xpath = getAttribute(name);
        XNode model = getAttribute("model");
        if (xpath == null) {
            xpath = getAttribute("value");
        }
        return xpath == null ? "null" : "new Binding(" + xpath.toScript() + (model != null ? "," + model.toScript() : "") + ")";
    }
}
