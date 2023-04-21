package hudson.zipscript.parser.template.element.lang.variable.special.string;

import hudson.zipscript.parser.context.ExtendedContext;
import hudson.zipscript.parser.template.element.Element;
import hudson.zipscript.parser.template.element.lang.variable.adapter.RetrievalContext;
import hudson.zipscript.parser.template.element.lang.variable.special.SpecialMethod;

public class RPadSpecialMethod implements SpecialMethod {

    private Element padding;

    public RPadSpecialMethod(Element[] vars) {
        if (null != vars && vars.length > 0) {
            padding = vars[0];
        }
    }

    public Object execute(Object source, RetrievalContext retrievalContext, String contextHint, ExtendedContext context) throws Exception {
        if (null == padding) return source;
        int paddingAmt = ((Number) padding.objectValue(context)).intValue();
        StringBuffer sb = new StringBuffer();
        sb.append(source);
        for (int i = 0; i < paddingAmt; i++) sb.append(' ');
        return sb.toString();
    }

    public RetrievalContext getExpectedType() {
        return RetrievalContext.TEXT;
    }
}
