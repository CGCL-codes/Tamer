package hudson.zipscript.parser.template.element.lang.variable.special.object;

import hudson.zipscript.parser.context.ExtendedContext;
import hudson.zipscript.parser.exception.ExecutionException;
import hudson.zipscript.parser.template.element.lang.variable.adapter.RetrievalContext;
import hudson.zipscript.parser.template.element.lang.variable.special.SpecialMethod;

public class ToIntSpecialMethod implements SpecialMethod {

    public static ToIntSpecialMethod INSTANCE = new ToIntSpecialMethod();

    public Object execute(Object source, RetrievalContext retrievalContext, String contextHint, ExtendedContext context) throws Exception {
        if (source instanceof Number) return new Integer(((Number) source).intValue()); else if (source instanceof String) return new Integer((String) source); else throw new ExecutionException("Invalid type for int conversion: " + source.getClass().getName(), null);
    }

    public RetrievalContext getExpectedType() {
        return RetrievalContext.SCALAR;
    }
}
