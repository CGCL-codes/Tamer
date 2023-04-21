package hudson.zipscript.parser.template.element.lang.variable;

import hudson.zipscript.ext.data.DefaultElementContainer;
import hudson.zipscript.parser.exception.ParseException;
import hudson.zipscript.parser.template.data.ParsingSession;
import hudson.zipscript.parser.template.element.DefaultElementFactory;
import hudson.zipscript.parser.template.element.Element;

public class SpecialVariableDefaultEelementFactory implements DefaultElementFactory {

    public static final SpecialVariableDefaultEelementFactory INSTANCE = new SpecialVariableDefaultEelementFactory();

    public DefaultElementContainer createDefaultElement(Element nextElement, String text, ParsingSession session, int contentPosition) throws ParseException {
        SpecialVariableElementImpl element = new SpecialVariableElementImpl(text, session, contentPosition);
        element.setShouldEvaluateSeparators(false);
        element.setElementPosition(contentPosition);
        element.setElementLength(text.length());
        return new DefaultElementContainer(element, nextElement);
    }

    public boolean doAppend(char c) {
        return true;
    }
}
