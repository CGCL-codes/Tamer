package net.ajaxforms.compiler.xforms;

import net.ajaxforms.compiler.Creator;
import net.ajaxforms.compiler.DefaultCreator;
import net.ajaxforms.compiler.ElementAnnotation;

@ElementAnnotation(requiredId = true, childs = "xf:label?, xf:help?, xf:hint?, xf:alert?, [xf:item | xf:itemset]+, $actions", attributes = "[ref | bind], model?, selection?, appearance?, incremental?")
public class Select extends XFormsElement {

    public Select(String localName) {
        super(localName);
    }

    public String toScript() {
        StringBuffer sb = new StringBuffer("var ");
        sb.append(getVarId());
        sb.append("=new XFSelect(");
        sb.append(toScriptParam(getId())).append(",");
        sb.append(getNodeName().equals("select")).append(",");
        sb.append(getAttribute("appearance").getNodeValue().equals("full")).append(",");
        sb.append(toStriptBinding("ref")).append(",");
        sb.append(toScriptParam(getAttribute("incremental")));
        sb.append(");");
        return sb.toString();
    }

    public static final Creator CREATOR = new DefaultCreator(XFormsLibrary.NAMESPACE, Select.class);
}
