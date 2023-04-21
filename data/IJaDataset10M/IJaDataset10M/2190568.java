package hudson.zipscript.parser.template.element.group;

import hudson.zipscript.parser.context.ExtendedContext;
import hudson.zipscript.parser.exception.ExecutionException;
import hudson.zipscript.parser.template.element.directive.NestableDirective;
import hudson.zipscript.parser.util.StringUtil;
import java.io.Writer;

public class EndGroupElement extends NestableDirective {

    public void merge(ExtendedContext context, Writer sw) {
        StringUtil.append(')', sw);
    }

    public boolean booleanValue(ExtendedContext context) throws ExecutionException {
        throw new ExecutionException("groups can not be evaluated as booleans", this);
    }

    public Object objectValue(ExtendedContext context) throws ExecutionException {
        throw new ExecutionException("groups can not be evaluated as objects", this);
    }

    public String toString() {
        return ")";
    }
}
