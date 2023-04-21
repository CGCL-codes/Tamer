package org.epo.jbps.heart.xmlparser;

import org.xml.sax.*;

/**
 * Class describing the state Initialized
 *
 * @author Infotel Conseil
 */
public class ParserStateInitialized extends ParserState {

    /**
 * ParserStateInitialized constructor
 * @param int theEventId, Attribute theAtts
 */
    public ParserStateInitialized(int theEventId, Attributes theAtts) {
        super();
    }

    /**
 * Next states of the current state depending on incoming event
 * @return ParserState
 * @param SaxHandler theXmlHandler,  int eventId , Attributes theAtts
 */
    public ParserState nextState(SaxHandler theXmlHandler, int theEventId, Attributes theAtts) {
        ParserState myNextState = null;
        switch(theEventId) {
            case XmlTagsTable.REQUEST:
                myNextState = new ParserStateProcessingRequest(theEventId, theAtts);
                break;
            case XmlTagsTable.OUTPUT:
                myNextState = new ParserStateProcessingOutput(theEventId, theAtts);
                break;
            default:
                myNextState = this;
        }
        return (myNextState);
    }

    /**
 * This method is triggered when entering in the current state.
 * @param theXmlHandler SaxHandler
 * @exception BpsProcessException
 */
    public void onEntry(SaxHandler theXmlHandler) {
    }

    /**
 * This method is triggered when exiting the current state.
 * @param SaxHandler theXmlHandler
 * @exception BpsProcessException
 */
    public void onExit(SaxHandler theXmlHandler) {
    }

    /**
 * Method triggered while runnning in the current state
 * @param SaxHandler theXmlHandler, int theEventId, AttributeList theAtts
 * @exception BpsProcessException
 */
    public void run(SaxHandler theXmlHandler, int theEventId, Attributes theAtts) {
    }
}
