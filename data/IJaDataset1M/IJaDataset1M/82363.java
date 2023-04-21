package jam4j.lang;

import java.io.IOException;
import java.util.List;

/**
 * A script in the Jam4J language.
 * 
 * @author Luke Maurer
 */
public final class JamFile {

    /**
     * The special filename representing the built-in {@code Jambase} file.
     */
    public static final String JAMBASE = "+";

    private static final String JAMBASE_RESOURCE = "/jam4j/Jambase";

    private final String filename;

    private final Statement statements;

    JamFile(String filename, Statement... statements) {
        this.filename = filename;
        this.statements = Statement.block(statements);
    }

    JamFile(String filename, List<Statement> statements) {
        this.filename = filename;
        this.statements = Statement.block(statements);
    }

    /**
     * Parse the file with the given filename, returning a compiled JamFile
     * object.
     * 
     * @param filename The name of the file to parse. If it is equal to
     *            {@link #JAMBASE}, the built-in {@code Jambase} file will be
     *            parsed.
     * @return The compiled JamFile object.
     * @throws IOException If an I/O error occurs.
     * @throws ParseException If there is a syntax error.
     */
    public static JamFile parse(String filename) throws IOException, ParseException {
        return JAMBASE.equals(filename) ? ParserRunner.parseResource(JAMBASE_RESOURCE, "<Jambase>") : ParserRunner.parse(filename);
    }

    /**
     * @return The filename, as given to {@link #parse(String)} at
     * construction.
     */
    public String getFilename() {
        return filename;
    }

    @Override
    public String toString() {
        return "JamFile(" + filename + ')';
    }

    /**
     * Run the script, in the given context.
     * 
     * @param cxt The context to run the script in.
     */
    public void run(Context cxt) {
        statements.runRuleBody(cxt);
    }
}
