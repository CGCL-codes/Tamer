package hudson.zipscript.parser.template.element.directive.importdir;

import hudson.zipscript.parser.context.ExtendedContext;
import hudson.zipscript.parser.context.MapContextWrapper;
import hudson.zipscript.parser.exception.ExecutionException;
import hudson.zipscript.parser.exception.ParseException;
import hudson.zipscript.parser.template.data.ElementIndex;
import hudson.zipscript.parser.template.data.ParsingSession;
import hudson.zipscript.parser.template.element.directive.AbstractDirective;
import hudson.zipscript.parser.template.element.lang.variable.VariableElement;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

public class ImportDirective extends AbstractDirective {

    private String namespace;

    private VariableElement importElement;

    private String contents;

    private int contentStartPosition;

    public ImportDirective(String contents, ParsingSession session, int contentStartPosition) throws ParseException {
        this.contents = contents;
        this.contentStartPosition = contentStartPosition;
    }

    public void validate(ParsingSession session) throws ParseException {
        int index = contents.indexOf(" as ");
        if (index == -1) {
            throw new ParseException(this, "Invalid formed import.  Should be [#import pathElement as reference/]");
        }
        String s1 = contents.substring(0, index).trim();
        this.namespace = contents.substring(index + 4, contents.length()).trim();
        importElement = new VariableElement(false, true, s1.trim(), session, contentStartPosition);
        if (this.importElement.isStatic()) {
            session.addMacroImport(namespace, (String) this.importElement.objectValue(new MapContextWrapper(new HashMap())));
        } else {
            session.addDynamicMacroImport(namespace);
        }
    }

    public void merge(ExtendedContext context, Writer sw) throws ExecutionException {
        if (!this.importElement.isStatic()) {
            Object path = importElement.objectValue(context);
            if (null == path) throw new ExecutionException("Null import path evaluated for '" + this + "'", this);
            context.addMacroImport(namespace, path.toString());
        }
    }

    public ElementIndex normalize(int index, List elementList, ParsingSession session) throws ParseException {
        return null;
    }

    public List getChildren() {
        return null;
    }

    public String toString() {
        return "[#import contents/]";
    }
}
