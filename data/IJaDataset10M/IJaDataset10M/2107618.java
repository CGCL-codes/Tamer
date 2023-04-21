package net.sourceforge.djindent.parser;

/**
 * This is used to represent the output of the parser.  Anything that wants to talk to the parser 
 * should talk to it through the parser pipe.  The idea being that the parser pipe provides symbols 
 * in an abstract way to whatever the next piece of the architecture is.  This way the parser pipe
 * can be backed by anything, but this allows for a way for something produce symbols and something
 * to get symbols.
 */
public interface ParserPipe {

    /**
     * This is used to add a symbol into the pipe.  This should be used by the parser to provide
     * a new symbol to whatever needs it at the other end of the pipe.
     * @param a_newSymbol This is the new Symbol that is going to be added to the end of the pipe
     */
    public void addSymbol(Symbol a_newSymbol);

    /**
     * This is the method that is used to retrieve the next symbol from the pipe.  If no symbols
     * are available it will return null;
     * @return Symbol This is the next symbol in the pipe line, null if there is nothing else in the pipe
     */
    public Symbol getNextSymbol();

    /**
     * This method tells whether or not there is a symbol waiting in the pipe or not
     * @return boolean true if there is a symbol waiting, false if there isn't
     */
    public boolean symbolAvailable();
}
