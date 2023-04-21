package net.ajaxforms.compiler.xforms;

import net.ajaxforms.compiler.XNode;
import net.ajaxforms.compiler.Context;
import net.ajaxforms.compiler.Creator;
import net.ajaxforms.compiler.DefaultCreator;
import net.ajaxforms.compiler.ElementAnnotation;
import net.ajaxforms.compiler.Writer;
import net.ajaxforms.compiler.xforms.actions.AbstractAction;

@ElementAnnotation(requiredId = true, parent = "xf:model", attributes = "src?")
public class Instance extends XFormsElement {

    private String srcXML;

    public Instance(String localName) {
        super(localName);
    }

    public void process() {
        super.process();
        int cont = 0;
        for (XNode child : getChildNodes()) {
            if (!(child instanceof AbstractAction)) {
                cont++;
            }
        }
        Context ctx = Context.getInstance();
        if (cont > 1) {
            ctx.addError(this, "Has more that one child: " + cont);
        } else if (cont == 1) {
            this.srcXML = Writer.toString(getChildNodes().get(0));
        } else if (getAttribute("src") == null) {
            ctx.addError(this, "It must has src attribute or XML document inner");
        }
    }

    public String toScript() {
        StringBuffer sb = new StringBuffer("new XFInstance(");
        sb.append(toScriptParam(getId())).append(",");
        sb.append(((XFormsElement) this.parentNode).getVarId()).append(",");
        sb.append(toScriptParam(getAttribute("src"))).append(",");
        sb.append(toScriptParam(this.srcXML));
        sb.append(");");
        return sb.toString();
    }

    public static final Creator CREATOR = new DefaultCreator(XFormsLibrary.NAMESPACE, Instance.class);
}
