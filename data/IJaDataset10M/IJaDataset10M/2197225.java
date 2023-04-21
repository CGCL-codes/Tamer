package org.epo.jbpa.xmlparsing;

import org.xml.sax.*;
import org.epo.jbpa.dl.*;

/**
 * This abstract class defines a generic state of the control 
 * files parser.
 *
 * @author INFOTEL Conseil
 */
public abstract class ParserState {

    public boolean isSubTag;

    public boolean isEndTag;

    /**
 * ParserState constructor.
 */
    public ParserState() {
        super();
        isSubTag = false;
        isEndTag = false;
    }

    /**
 * Describing next state depending on input event
 * @return ParserState
 * @param SaxHandler theXmlHandler,  int eventId , Attributes theAtts
 */
    public abstract ParserState nextState(SaxHandler theXmlHandler, int theEventId, Attributes theAtts);

    /**
 * This method is triggered when entering in the current state.
 * @param SaxHandler theXmlHandler
 * @exception JBpaException
 */
    public abstract void onEntry(SaxHandler theXmlHandler) throws JBpaException;

    /**
 * This method is triggered when exiting the current state.
 * @param SaxHandler theXmlHandler
 * @exception JBpaException
 */
    public abstract void onExit(SaxHandler theXmlHandler) throws JBpaException;

    /**
 * Method running during the current state
 * @param SaxHandler theXmlHandler, int theEventId, Attributes theAtts
 * @exception JBpaException
 */
    public abstract void run(SaxHandler theXmlHandler, int theEventId, Attributes theAtts) throws JBpaException;
}
