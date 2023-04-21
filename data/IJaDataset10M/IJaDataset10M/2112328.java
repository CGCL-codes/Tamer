package ca.compsci.opent.compiler;

import ca.compsci.opent.compiler.lexer.LexerException;
import ca.compsci.opent.compiler.parser.ParserException;

public class Utils {

    public static enum ErrorType {

        SemanticError, SyntaxError
    }

    public static void reportError(ErrorType type, String msg, String filename, int line, int col) {
        switch(type) {
            case SemanticError:
                System.err.printf("Semantic error: %s\n", msg);
                break;
            case SyntaxError:
                System.err.printf("  Syntax error: %s\n", msg);
                break;
        }
        if (filename != null) System.err.printf("          file: %s\n", filename);
        if (line >= 0) System.err.printf("          line: %d\n", line);
        if (col >= 0) System.err.printf("        column: %d\n", col);
        System.err.println();
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void reportSyntaxError(LexerException le, String filename) {
        String raw_msg = le.getMessage();
        String[] line_col = raw_msg.substring(1, raw_msg.indexOf(']')).split(",");
        int line = Integer.valueOf(line_col[0]);
        int col = Integer.valueOf(line_col[1]);
        String msg = raw_msg.substring(raw_msg.indexOf(':') + 2);
        reportError(ErrorType.SyntaxError, "invalid symbol '" + msg + "'", filename, line, col);
    }

    public static void reportSyntaxError(ParserException le, String filename) {
        String raw_msg = le.getMessage();
        String[] line_col = raw_msg.substring(1, raw_msg.indexOf(']')).split(",");
        int line = Integer.valueOf(line_col[0]);
        int col = Integer.valueOf(line_col[1]);
        String msg = raw_msg.substring(raw_msg.indexOf(':') + 2);
        reportError(ErrorType.SyntaxError, "expecting " + msg, filename, line, col);
    }
}
