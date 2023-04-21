package edu.napier.soc.xfdm.parser;

import edu.napier.soc.xfdm.model.Database;
import java.io.StringReader;

/**
 * @author Thomas Ford
 * @version 0.1, 16-Jan-2009
 */
public class XFDMParser {

    private Parser p;

    private Database d;

    private volatile Interpreter i;

    private volatile int numOfStatements;

    public XFDMParser(Database d) {
        this.d = d;
    }

    public void setDatabase(Database d) {
        this.d = d;
    }

    public void parseStatements(String text, QueryResultSet set) throws SyntaxException, InterpreterException {
        if (text == null) {
            throw new NullPointerException("text must not be null");
        }
        if (set == null) {
            throw new NullPointerException("set must not be null");
        }
        if (p == null) {
            p = new Parser(new StringReader(text));
        } else {
            p.ReInit(new StringReader(text));
        }
        try {
            SimpleNode sn = p.start();
            String[] commands = modifyCommands(text);
            numOfStatements = commands.length;
            i = new Interpreter(d, set, modifyCommands(text));
            i.start(sn);
        } catch (ParseException ex) {
            throw new SyntaxException(ex);
        }
    }

    private String[] modifyCommands(String commands) {
        return commands.replaceAll("//.*?\n", "").split(";");
    }

    public int getExecuted() {
        if (i == null) {
            return 0;
        } else {
            return i.getNumberExecuted();
        }
    }

    public void stopRequest() {
        i.stopRequest();
    }

    public int getNumOfStatements() {
        return numOfStatements;
    }
}
