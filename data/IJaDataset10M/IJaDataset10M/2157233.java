package net.ajaxforms.compiler.xforms;

import net.ajaxforms.compiler.Creator;
import net.ajaxforms.compiler.DefaultCreator;
import net.ajaxforms.compiler.ElementAnnotation;

@ElementAnnotation(requiredId = true, parent = "[xf:model | xf:bind]", childs = "xf:bind*", attributes = "nodeset, readonly?, required?, relevant?, calculate?, constraint?, type?")
public class Bind extends XFormsElement {

    public Bind(String localName) {
        super(localName);
    }

    public String toScript() {
        StringBuffer sb = new StringBuffer("var ");
        sb.append(getVarId()).append(" = new XFBind(");
        sb.append(toScriptParam(getId())).append(",");
        sb.append(((XFormsElement) this.parentNode).getVarId()).append(",");
        sb.append(toScriptParam(getAttribute("nodeset"))).append(",");
        sb.append(toScriptParam(getAttribute("type"))).append(",");
        sb.append(toScriptParam(getAttribute("readonly"))).append(",");
        sb.append(toScriptParam(getAttribute("required"))).append(",");
        sb.append(toScriptParam(getAttribute("relevant"))).append(",");
        sb.append(toScriptParam(getAttribute("calculate"))).append(",");
        sb.append(toScriptParam(getAttribute("constraint")));
        sb.append(");");
        return sb.toString();
    }

    public static final Creator CREATOR = new DefaultCreator(XFormsLibrary.NAMESPACE, Bind.class);
}
