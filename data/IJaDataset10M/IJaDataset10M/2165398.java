package org.ztemplates.actions.urlhandler.tree.term;

import org.ztemplates.actions.expression.ZParserException;
import org.ztemplates.actions.util.ZFormatUtil;

public class ZTreeVariable extends ZTreeTerm {

    private final String name;

    public ZTreeVariable(Class clazz, String name) throws ZParserException {
        super(clazz);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void toXml(StringBuilder sb, int depth) {
        ZFormatUtil.indent(sb, depth);
        sb.append("<variable name=\"" + name + "\"/>");
    }

    public String toDefinition() {
        return "${" + name + "}";
    }
}
