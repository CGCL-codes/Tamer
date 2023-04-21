package hudson.zipscript.parser.template.element.lang.variable.special.string;

import hudson.zipscript.parser.context.ExtendedContext;
import hudson.zipscript.parser.template.element.lang.variable.adapter.RetrievalContext;
import hudson.zipscript.parser.template.element.lang.variable.special.SpecialMethod;
import hudson.zipscript.parser.util.StringUtil;

public class LowerFirstSpecialMethod implements SpecialMethod {

    public static final LowerFirstSpecialMethod INSTANCE = new LowerFirstSpecialMethod();

    public Object execute(Object source, RetrievalContext retrievalContext, String contextHint, ExtendedContext context) throws Exception {
        return StringUtil.firstLetterLowerCase((String) source);
    }

    public RetrievalContext getExpectedType() {
        return RetrievalContext.TEXT;
    }
}
