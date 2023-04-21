package net.sourceforge.ondex.event;

/**
 * Implementation of a ParserListener, does nothing.
 * 
 * @author taubertj
 * 
 */
public class ParserAdapter implements ParserListener {

    /**
	 * Method implemented from ParserListener, does nothing.
	 * 
	 */
    public void eventOccurred(ParserEvent e) {
    }

    /**
	 * Method implemented from ONDEXListener, does nothing.
	 * 
	 */
    public void eventOccurred(ONDEXEvent e) {
    }
}
