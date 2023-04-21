package hudson.zipscript.parser.template.element.directive.ifdir;

import hudson.zipscript.parser.context.ExtendedContext;
import hudson.zipscript.parser.exception.ExecutionException;
import hudson.zipscript.parser.template.element.directive.NestableDirective;
import java.io.Writer;

public class ElseIfDirective extends NestableDirective {

    private String contents;

    public ElseIfDirective(String contents) {
        this.contents = contents;
    }

    public String toString() {
        return "[#elseif " + contents + "]";
    }

    public void merge(ExtendedContext context, Writer sw) throws ExecutionException {
        throw new ExecutionException("Invalid if directive", this);
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
