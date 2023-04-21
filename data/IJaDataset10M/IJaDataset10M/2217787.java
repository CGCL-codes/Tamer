package de.maramuse.soundcomp.parser;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import de.maramuse.soundcomp.parser.SCParser.ParserVal;

public class Parser {

    SCParser parser = null;

    private boolean debug = false;

    private boolean optimize = true;

    public SCParser.ParserVal parse(InputStream stream, Collection<String> paths) throws Exception {
        Preprocessor.setDebug(debug);
        Preprocessor preprocessor = new Preprocessor(stream);
        preprocessor.appendPath(paths);
        parser = new SCParser(preprocessor);
        parser.yydebug = debug;
        parser.optimize = optimize;
        parser.yyparse();
        return parser.getResult();
    }

    public SCParser.ParserVal parse(File file, Collection<String> paths) throws Exception {
        Preprocessor.setDebug(debug);
        Preprocessor preprocessor = new Preprocessor(file);
        preprocessor.appendPath(paths);
        parser = new SCParser(preprocessor);
        parser.yydebug = debug;
        parser.optimize = optimize;
        parser.yyparse();
        return parser.getResult();
    }

    /**
   * Returns the error text resulting from the last parse() call
   * 
   * @return the error text resulting from the last parse() call
   */
    public String getError() {
        if (parser == null) return null;
        return parser.error;
    }

    /**
   * Prints the current value stack of the parser
   */
    public void dumpStack() {
        if (parser == null) return;
        parser.dumpStacks();
    }

    /**
   * Returns the last read symbol (important in case of parse errors)
   * 
   * @return the last read symbol
   */
    public ParserVal getLastSymbol() {
        if (parser == null) return null;
        return parser.getLastValidReadSymbol();
    }

    public String getSymbol(int symbol) {
        if (parser == null) return "" + symbol;
        return parser.getSymbol(symbol);
    }

    /**
   * sets the debug mode for the next parse() call
   * @param debug the debug flag
   */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
   * sets the optimization mode for the next parse() call
   * @param debug the optimization flag
   */
    public void setOptimize(boolean optmize) {
        this.optimize = optmize;
    }
}
