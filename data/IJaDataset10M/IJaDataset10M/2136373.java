package org.ztemplates.actions.urlhandler.tree.term;

import org.ztemplates.actions.expression.ZParserException;
import org.ztemplates.actions.util.ZFormatUtil;

public class ZTreeNestedEnd extends ZTreeTerm {

    private final String name;

    private final Class nestedClass;

    public ZTreeNestedEnd(Class clazz, String name, Class nestedClass) throws ZParserException {
        super(clazz);
        this.name = name;
        this.nestedClass = nestedClass;
    }

    public String getName() {
        return name;
    }

    public void toXml(StringBuilder sb, int depth) {
        ZFormatUtil.indent(sb, depth);
        sb.append("<nested-end name=\"" + name + "\" nestedClass=\"" + nestedClass.getSimpleName() + "\"/>");
    }

    public String toDefinition() {
        return " }";
    }
}
