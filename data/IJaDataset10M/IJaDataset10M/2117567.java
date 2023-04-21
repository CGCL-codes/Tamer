package cfg.parser;

/**
 * Recursive descent parser for the following grammar:
  
           A -> A a B
           B -> C b B
             -> C
           C -> c C
             -> d
 * @author ddaniels
 *
 */
public class OriginalGrammarParser {

    final String originalInput;

    String input;

    String lastRead;

    boolean parseSuccessful;

    boolean parseFinished = false;

    final String a = "a";

    final String b = "b";

    final String c = "c";

    final String d = "d";

    final String EOF = "$";

    public OriginalGrammarParser(String userInput) {
        originalInput = userInput;
        input = originalInput;
        lastRead = "";
    }

    public boolean parse() {
        A();
        return parseSuccessful;
    }

    private void A() {
        B();
        write("A->B");
        while (isNext(a)) {
            consumeInput();
            B();
            if (parseFinished) return;
            write("A->AaB");
        }
        if (input.length() > 1) {
            parseError("Production A parsing failure expected an '" + a + "'");
        } else {
            parseSuccess("Production A parse success");
        }
    }

    private void write(String production) {
        System.out.println(production);
    }

    private void B() {
        C();
        if (parseFinished) return;
        if (isNext(b)) {
            consumeInput();
            B();
            if (parseFinished) return;
            write("B->CbB");
        } else {
            write("B->C");
        }
    }

    private void C() {
        if (parseFinished) return;
        if (isNext(c)) {
            consumeInput();
            C();
            write("C->cC");
        } else if (isNext(d)) {
            consumeInput();
            write("C->d");
        } else {
            parseError("Parsing C production expected a '" + c + "' or '" + d + "' terminal");
        }
    }

    /**
     * @return true if the next token matches the expected value, false
     *         otherwise.
     */
    private boolean isNext(String expected) {
        return input.startsWith(expected);
    }

    /**
     * 
     * @param expected
     * @return true if the top of the input matches the string
     */
    private void consumeInput() {
        lastRead = input.substring(0, 1);
        input = input.substring(1);
    }

    private void parseError(String errorMessage) {
        int inputErrorIndex = originalInput.length() - input.length();
        System.out.println("[ParseError]: input[" + originalInput + "] Error occurred at input string index[" + inputErrorIndex + "]:");
        System.out.println(originalInput.substring(0, inputErrorIndex) + "[" + input.substring(0, 1) + "]" + input.substring(1));
        System.out.println(errorMessage);
        parseSuccessful = false;
        parseFinished = true;
    }

    private void parseSuccess(String successMessage) {
        System.out.println("[ParseSuccess]: input[" + originalInput + "] is a valid production of the LL(1) CFG.");
        System.out.println(successMessage);
        parseSuccessful = true;
        parseFinished = true;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        String testInput = "daccdbdbd$";
        OriginalGrammarParser r = new OriginalGrammarParser(testInput);
        boolean parseSuccess = r.parse();
        System.out.println("Parse of [" + testInput + "] was succesful? " + parseSuccess + "\n\n");
        testInput = "ddaccdbdbd$";
        r = new OriginalGrammarParser(testInput);
        parseSuccess = r.parse();
        System.out.println("Parse of [" + testInput + "] was succesful? " + parseSuccess + "\n\n");
        testInput = "daccdbbdbd$";
        r = new OriginalGrammarParser(testInput);
        parseSuccess = r.parse();
        System.out.println("Parse of [" + testInput + "] was succesful? " + parseSuccess + "\n\n");
    }
}
