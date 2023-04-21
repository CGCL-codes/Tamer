package de.uniwue.dltk.textmarker.internal.core.parsers;

import java.util.ArrayList;
import java.util.List;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.FailedPredicateException;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.eclipse.dltk.ast.DLTKToken;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import de.uniwue.dltk.textmarker.internal.core.parser.TextMarkerParseUtils;

/**
 * 
 * @see org.eclipse.dltk.p y thon.internal.core.parsers.DLTKP y thonErrorReporter
 * 
 */
public class DLTKTextMarkerErrorReporter {

    IProblemReporter reporter;

    DLTKTokenConverter converter;

    TextMarkerParser parser;

    List problems = new ArrayList();

    public DLTKTextMarkerErrorReporter(DLTKTokenConverter converter, IProblemReporter reporter, TextMarkerParser parser) {
        this.converter = converter;
        this.reporter = reporter;
        this.parser = parser;
    }

    public void reportError(RecognitionException re) {
        if (reporter == null) {
            return;
        }
        if (re.token == null) {
            System.out.println("Token is null in ErrorReporter");
            return;
        }
        Token token = re.token;
        int line = re.token.getLine();
        int index = token.getTokenIndex();
        if (index < 0) {
            index = re.index;
            TokenStream tokenStream = parser.getTokenStream();
            try {
                token = tokenStream.get(index - 1);
                line = token.getLine();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        String message = re.getMessage();
        String m = "";
        if (message != null) {
            m = message;
        }
        String tokenText = token.getText() == null ? "" : token.getText();
        int bounds[] = TextMarkerParseUtils.getBounds(token);
        int st = bounds[0];
        int et = bounds[1];
        String errorPrefix = "";
        if (re instanceof NoViableAltException) {
            NoViableAltException nvae = (NoViableAltException) re;
            errorPrefix = "Syntax error: ";
            m = errorPrefix + nvae.grammarDecisionDescription;
            reportProblem(line, m, st, et);
        } else if (re instanceof MismatchedTokenException) {
            errorPrefix = "Mismatched Input: ";
            MismatchedTokenException mte = (MismatchedTokenException) re;
            int expecting = mte.expecting;
            String expectedToken = "";
            if (expecting > 0) {
                expectedToken = parser.getTokenNames()[expecting];
                errorPrefix = errorPrefix + "Expecting \"" + expectedToken;
                String msg = errorPrefix + "\" but found \"" + tokenText + "\".";
                reportProblem(line, msg, st, et);
            } else {
                reportDefaultProblem(line, m, tokenText, st, et, errorPrefix);
            }
        } else if (re instanceof FailedPredicateException) {
            errorPrefix = "Failed predicate: ";
            reportDefaultProblem(line, m, tokenText, st, et, errorPrefix);
        } else {
            String[] messages = { "Syntax Error:" + message, message };
            if (message == null) {
                messages[0] = re.toString();
            }
            DLTKToken convert = this.converter.convert(re.token);
            DefaultProblem defaultProblem = new DefaultProblem("", messages[0], 0, new String[] {}, ProblemSeverities.Error, st, et, re.token.getLine());
            if (!problems.contains(defaultProblem)) {
                reporter.reportProblem(defaultProblem);
                problems.add(defaultProblem);
                System.out.println(messages[0] + " ### line " + re.token.getLine());
            }
        }
    }

    /**
   * @param line
   * @param m
   * @param tokenText
   * @param st
   * @param et
   * @param errorPrefix
   */
    private void reportDefaultProblem(int line, String m, String tokenText, int st, int et, String errorPrefix) {
        String msg = errorPrefix + m + " : " + tokenText;
        reportProblem(line, msg, st, et);
    }

    private void reportProblem(int line, String msg, int st, int et) {
        DefaultProblem defaultProblem = createDefaultProblem(msg, st, et, line);
        if (!problems.contains(defaultProblem)) {
            reporter.reportProblem(defaultProblem);
            problems.add(defaultProblem);
        }
    }

    private DefaultProblem createDefaultProblem(String m, int st, int et, int line) {
        return new DefaultProblem("", m, 0, new String[] {}, ProblemSeverities.Error, st, et, line);
    }

    public void reportThrowable(Throwable extre) {
        extre.printStackTrace();
    }

    public void reportMessage(String msg) {
    }

    public void reportErrorOld(RecognitionException re) {
        if (reporter == null) {
            return;
        }
        if (re.token == null) {
            System.out.println("Token is null in ErrorReporter");
            return;
        }
        Token token = re.token;
        int line = re.token.getLine();
        int index = token.getTokenIndex();
        if (index < 0) {
            index = re.index;
            TokenStream tokenStream = parser.getTokenStream();
            try {
                token = tokenStream.get(index - 1);
                line = token.getLine();
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        String message = re.getMessage();
        String m = "unknown error";
        if (message != null) {
            m = message;
        }
        if (re instanceof NoViableAltException) {
            NoViableAltException ec = (NoViableAltException) re;
            if (message == null || ec.token.getText() == null) {
                m = ec.toString();
            } else {
                m = "Syntax Error:" + message + " : " + ec.token.getText();
            }
            String[] messages = { m };
            int st = converter.convert(ec.token.getLine(), ec.token.getCharPositionInLine());
            String sm = ec.token.getText();
            int et = st + ec.token.getText().length();
            if (st == -1) return;
            DefaultProblem defaultProblem = new DefaultProblem("", messages[0], 0, new String[] {}, ProblemSeverities.Error, st, et, ec.token.getLine());
            if (!problems.contains(defaultProblem)) {
                reporter.reportProblem(defaultProblem);
                problems.add(defaultProblem);
                System.out.println(messages[0] + " ### line " + ec.token.getLine());
            }
        } else if (re instanceof MismatchedTokenException) {
            MismatchedTokenException ec = (MismatchedTokenException) re;
            if (message == null || ec.token.getText() == null) {
                m = ec.toString();
            } else {
                m = "mismatched input: " + message + " : " + ec.token.getText();
            }
            String[] messages = { m };
            int st = converter.convert(ec.token.getLine(), ec.token.getCharPositionInLine());
            int et = st;
            if (ec.token.getText() != null) {
                et = ec.token.getText().length() + st;
                if (et >= this.converter.length()) {
                    et = this.converter.length() - 1;
                    st -= 2;
                }
            } else {
                st = ((CommonToken) token).getStartIndex();
                et = ((CommonToken) token).getStopIndex();
            }
            DefaultProblem defaultProblem = new DefaultProblem("", messages[0], 0, new String[] {}, ProblemSeverities.Error, st, et, ec.line);
            if (!problems.contains(defaultProblem)) {
                reporter.reportProblem(defaultProblem);
                problems.add(defaultProblem);
                System.out.println(messages[0] + " ### line " + ec.line);
            }
        } else if (re instanceof FailedPredicateException) {
            String[] messages = { "Syntax Error:" + message, message };
            if (message == null) {
                messages[0] = re.toString();
            }
            DLTKToken convert = this.converter.convert(re.token);
            int st = convert.getColumn();
            int et = convert.getColumn() + convert.getText().length();
            DefaultProblem defaultProblem = new DefaultProblem("", "Type not defined in this script: " + convert.getText(), 0, new String[] {}, ProblemSeverities.Warning, st, et, re.token.getLine());
            if (!problems.contains(defaultProblem)) {
                reporter.reportProblem(defaultProblem);
                problems.add(defaultProblem);
                System.out.println(messages[0] + " ### line " + re.token.getLine());
            }
        } else {
            String[] messages = { "Syntax Error:" + message, message };
            if (message == null) {
                messages[0] = re.toString();
            }
            DLTKToken convert = this.converter.convert(re.token);
            int st = convert.getColumn();
            int et = convert.getColumn() + convert.getText().length();
            DefaultProblem defaultProblem = new DefaultProblem("", messages[0], 0, new String[] {}, ProblemSeverities.Error, st, et, re.token.getLine());
            if (!problems.contains(defaultProblem)) {
                reporter.reportProblem(defaultProblem);
                problems.add(defaultProblem);
                System.out.println(messages[0] + " ### line " + re.token.getLine());
            }
        }
    }
}
