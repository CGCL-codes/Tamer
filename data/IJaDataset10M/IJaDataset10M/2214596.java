package net.ajaxforms.compiler.xforms.actions;

import net.ajaxforms.compiler.Creator;
import net.ajaxforms.compiler.DefaultCreator;
import net.ajaxforms.compiler.ElementAnnotation;
import net.ajaxforms.compiler.xforms.XFormsLibrary;

@ElementAnnotation(attributes = "model?, $events", childs = "")
public class ModelAction extends AbstractAction {

    public ModelAction(String localName) {
        super(localName);
    }

    @Override
    public String buildAction() {
        StringBuffer sb = new StringBuffer("new XFDispatch('xforms-");
        sb.append(getNodeName()).append("',");
        sb.append(toScriptParam(getAttribute("model"), "xforms.defaultModel")).append(",");
        sb.append(toScriptParam(getAttribute("if"))).append(",");
        sb.append(toScriptParam(getAttribute("while")));
        sb.append(")");
        return sb.toString();
    }

    public static final Creator CREATOR = new DefaultCreator(XFormsLibrary.NAMESPACE, ModelAction.class);
}
