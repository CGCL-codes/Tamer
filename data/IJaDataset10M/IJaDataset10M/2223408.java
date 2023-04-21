package br.ufpe.cin.stp.mass.view.wireless;

import java.io.IOException;
import java.util.Vector;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParserException;
import br.ufpe.cin.stp.mass.view.wireless.handler.Session;
import br.ufpe.cin.stp.mass.view.wireless.handler.SessionResults;
import br.ufpe.cin.stp.mass.view.wireless.handler.VoteItem;

/**
 * @created 08/08/2004 01:06:41
 * @author Marcello Sales Jr. <a href='masj2@cin.ufpe.br'>masj2@cin.ufpe.br</a>
 * @version 1.0
 */
public abstract class SessionResultsXMLHandler {

    /**
	 * @created 08/08/2004 21:36:34
	 * @param currentSession
	 * @param parser
	 * @return gets the SessionResults representation of a given 
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
    public static SessionResults parseSessionResults(Session currentSession, KXmlParser parser) throws XmlPullParserException, IOException {
        boolean opened = true;
        SessionResults sr = new SessionResults(currentSession);
        int eventType = parser.getEventType();
        while ((eventType = parser.next()) != KXmlParser.END_DOCUMENT) {
            if (eventType == KXmlParser.START_TAG) {
                if (parser.getName().equals("results")) opened = parser.getAttributeValue("", "opened").equals("true"); else if (parser.getName().equals("question-item")) sr.addVoteItem(new VoteItem(currentSession.getQuestions()[0].getQuestionItemString(parser.getAttributeValue("", "id")), parser.getAttributeValue("", "number"), parser.getAttributeValue("", "percentage")));
            }
        }
        return sr;
    }
}
