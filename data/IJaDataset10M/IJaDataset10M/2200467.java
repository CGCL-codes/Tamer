package hudson.zipscript.parser.template.element.directive.macrodir;

import hudson.zipscript.parser.exception.ParseException;
import hudson.zipscript.parser.template.data.ElementIndex;
import hudson.zipscript.parser.template.data.ParsingSession;
import java.util.List;

public class MacroFooterElement extends MacroHeaderElement {

    public MacroFooterElement(String contents, ParsingSession session, int position) throws ParseException {
        super(contents, session, position);
    }

    public ElementIndex normalize(int index, List elementList, ParsingSession session) throws ParseException {
        ((MacroInstanceDirective) session.getNestingStack().get(session.getNestingStack().size() - 1)).setFooter(this);
        return new ElementIndex(null, -1);
    }
}
