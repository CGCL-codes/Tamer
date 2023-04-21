package examples;

import org.apache.oro.text.regex.*;

/**
 * This is a test program demonstrating an application of the matchesPrefix()
 * methods introduced in OROMatcher v1.0.6.  This example program shows how
 * you might tokenize a stream of input using whitespace as a token
 * separator.  Don't forget to use quotes around the input on the command
 * line, e.g.
 *    java prefixExample "Test to see if 1.0 is real and 2 is an integer"
 *
 * If you don't need the power of a full blown lexer generator, you can
 * easily use regular expressions to create your own tokenization and
 * simple parsing classes using similar approaches.
 *
 * @version @version@
 */
public final class prefixExample {

    public static final int REAL = 0;

    public static final int INTEGER = 1;

    public static final int STRING = 2;

    public static final String[] types = { "Real", "Integer", "String" };

    public static final String whitespace = "\\s+";

    public static final String[] tokens = { "-?\\d*\\.\\d+(?:[eE][-+]-?\\d+)?(?=\\s|$)", "-?\\d+(?=\\s|$)", "\\S+" };

    public static final String tokens2 = "(-?\\d*\\.\\d+(?:[eE][-+]-?\\d+)?(?=\\s|$))|(-?\\d+(?=\\s|$))|(\\S+)";

    public static final void main(String args[]) {
        int token;
        PatternMatcherInput input;
        PatternMatcher matcher;
        PatternCompiler compiler;
        Pattern[] patterns;
        Pattern tokenSeparator = null, patterns2 = null;
        if (args.length < 1) {
            System.err.println("Usage: prefixExample <sample input>");
            System.exit(1);
        }
        input = new PatternMatcherInput(args[0]);
        compiler = new Perl5Compiler();
        patterns = new Pattern[tokens.length];
        try {
            tokenSeparator = compiler.compile(whitespace);
            patterns2 = compiler.compile(tokens2);
            for (token = 0; token < tokens.length; token++) patterns[token] = compiler.compile(tokens[token]);
        } catch (MalformedPatternException e) {
            System.err.println("Bad pattern.");
            e.printStackTrace();
            System.exit(1);
        }
        matcher = new Perl5Matcher();
        System.out.println("\nOne approach.\n");
        do {
            for (token = 0; token < tokens.length; token++) if (matcher.matchesPrefix(input, patterns[token])) {
                System.out.println(types[token] + ": " + matcher.getMatch());
                break;
            }
        } while (matcher.contains(input, tokenSeparator));
        System.out.println("\nAn equivalent alternative.\n");
        input.setCurrentOffset(input.getBeginOffset());
        do {
            if (matcher.matchesPrefix(input, patterns2)) {
                MatchResult result = matcher.getMatch();
                for (token = 1; token <= tokens.length; token++) {
                    if (result.group(token) != null) {
                        System.out.println(types[token - 1] + ": " + result);
                        break;
                    }
                }
            }
        } while (matcher.contains(input, tokenSeparator));
    }
}
