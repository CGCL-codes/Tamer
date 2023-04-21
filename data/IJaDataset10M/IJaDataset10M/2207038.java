package de.searchworkorange.searchserver.server.statemachine;

import de.searchworkorange.lib.communication.NoXSDFileFoundException;
import java.util.logging.Logger;
import org.apache.log4j.Level;
import de.searchworkorange.searchserver.server.exceptions.NetworkException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
class SendSearchResult extends ServerSSDState {

    private static final boolean CLASSDEBUG = false;

    public SendSearchResult(StateMachine stateMachine) {
        super(stateMachine);
    }

    /**
     * @return String
     */
    @Override
    public String toString() {
        return "State: SendSearchResult";
    }

    @Override
    public void workInState() {
        try {
            StringBuffer infix = new StringBuffer();
            infix.append("<RESULT SEARCHTIME=\"");
            infix.append("" + stateMachine.getSearcher().getSearchTime());
            infix.append("\">");
            StringWriter sw = stateMachine.getSo().getSearchResultStringWriter();
            if (sw != null) {
                BufferedReader br = new BufferedReader(new StringReader(sw.toString()));
                String line;
                while ((line = br.readLine()) != null) {
                    infix.append(line + "\n");
                }
                br.close();
                sw.close();
            }
            infix.append("</RESULT>");
            StringBuffer msgBuffer = msgGen.generateMsgStringBuffer(infix);
            if (stateMachine.getComInterpr().validateXML(msgBuffer.toString())) {
                stateMachine.getSocketAction().send(msgBuffer, true);
                stateMachine.setSearchResultSent();
            } else {
                stateMachine.getLoggerCol().logDebug(CLASSDEBUG, this.getClass().getName(), Level.DEBUG, "!!!!  RESULT MSG (XML) is NOT VALID  !!!!");
                stateMachine.setSystemError();
            }
        } catch (IOException ex) {
            Logger.getLogger(SendSearchResult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SAXParseException ex) {
            Logger.getLogger(SendSearchResult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(SendSearchResult.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (NoXSDFileFoundException ex) {
            stateMachine.getLoggerCol().logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
            stateMachine.setSystemError();
        } catch (NetworkException ex) {
            stateMachine.getLoggerCol().logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
            stateMachine.setSystemError();
        }
    }
}
