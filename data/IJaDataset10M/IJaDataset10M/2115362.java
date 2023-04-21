package org.eclipse.xtext.example.formatting;

import org.eclipse.xtext.example.services.FJGrammarAccess.ClassElements;
import org.eclipse.xtext.example.services.FJGrammarAccess.FieldElements;
import org.eclipse.xtext.example.services.FJGrammarAccess.MethodCallElements;
import org.eclipse.xtext.example.services.FJGrammarAccess.MethodElements;
import org.eclipse.xtext.example.services.FJGrammarAccess.NewElements;
import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

/**
 * This class contains custom formatting description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#formatting
 * on how and when to use it
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an
 * example
 */
public class FJFormatter extends AbstractDeclarativeFormatter {

    @Override
    protected void configureFormatting(FormattingConfig c) {
        org.eclipse.xtext.example.services.FJGrammarAccess f = (org.eclipse.xtext.example.services.FJGrammarAccess) getGrammarAccess();
        c.setAutoLinewrap(80);
        c.setSpace("    ");
        FieldElements fieldAccess = f.getFieldAccess();
        c.setLinewrap(1).after(fieldAccess.getSemicolonKeyword_2());
        c.setNoSpace().before(fieldAccess.getSemicolonKeyword_2());
        MethodElements methodAccess = f.getMethodAccess();
        c.setLinewrap(1).after(methodAccess.getRightCurlyBracketKeyword_7());
        c.setNoSpace().around(methodAccess.getLeftParenthesisKeyword_2());
        c.setNoSpace().before(methodAccess.getRightParenthesisKeyword_4());
        c.setNoSpace().before(methodAccess.getCommaKeyword_3_1_0());
        c.setNoSpace().around(f.getExpressionAccess().getFullStopKeyword_1_1());
        c.setNoSpace().before(f.getMethodBodyAccess().getSemicolonKeyword_2());
        NewElements newAccess = f.getNewAccess();
        c.setNoSpace().before(newAccess.getCommaKeyword_3_1_0());
        c.setNoSpace().around(newAccess.getLeftParenthesisKeyword_2());
        c.setNoSpace().before(newAccess.getRightParenthesisKeyword_4());
        MethodCallElements methodCallAccess = f.getMethodCallAccess();
        c.setNoSpace().before(methodCallAccess.getCommaKeyword_2_1_0());
        c.setNoSpace().around(methodCallAccess.getLeftParenthesisKeyword_1());
        c.setNoSpace().before(methodCallAccess.getRightParenthesisKeyword_3());
        ClassElements classAccess = f.getClassAccess();
        c.setIndentation(classAccess.getLeftCurlyBracketKeyword_3(), classAccess.getRightCurlyBracketKeyword_6());
        c.setLinewrap().after(classAccess.getLeftCurlyBracketKeyword_3());
        c.setLinewrap(2).after(classAccess.getRightCurlyBracketKeyword_6());
    }
}
