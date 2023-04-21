package com.simpleftp.ftp.server;

import java.io.File;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.simpleftp.ftp.server.command.AbstractCommand;
import com.simpleftp.ftp.server.command.FtpCONNECTCommand;
import com.simpleftp.ftp.server.command.FtpCommandFactory;
import com.simpleftp.ftp.server.command.FtpPASSCommand;
import com.simpleftp.ftp.server.command.FtpUSERCommand;
import com.simpleftp.ftp.server.command.ICommand;
import com.simpleftp.ftp.server.utils.FtpLogger;
import com.simpleftp.ftp.server.utils.TextUtil;

public class FtpStateMachine {

    private static Document configDoc;

    private static FtpStateMachine stateMachine = null;

    private static String FTP_STATE_TAG = "ftpState";

    private static String STATE_TAG = "state";

    private static String COMMAND_TAG = "command";

    private static String COMMAND_NAME_TAG = "name";

    private static String SUCCESS_STATE_TAG = "successState";

    private static String FAILURE_STATE_TAG = "failureState";

    private static Logger logger = FtpLogger.getLogger();

    public static final IState WAITING_TO_CONNECT = new FtpState("WAITING_TO_CONNECT");

    private FtpStateMachine() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        configDoc = db.parse(this.getClass().getClassLoader().getResourceAsStream("com/simpleftp/ftp/server/properties/state_machine.xml"));
    }

    public static synchronized FtpStateMachine getInstance() throws Exception {
        if (stateMachine == null) {
            stateMachine = new FtpStateMachine();
        }
        return stateMachine;
    }

    /**
	 * Take the state and a command name to get the next success and failure states of the session for the command.
	 * Can also be used to check if a command is in good sequence. If the IState is null the command
	 * is not in a sequence.
	 * @return IState Array with IState[0]-The next success state, IState[1]-The next failure state. Will return
	 * null if there is no next state for the current state on the command.
	 * */
    public synchronized IState[] getNextStates(IState currentState, AbstractCommand command) throws Exception {
        if (currentState == null) {
            logger.error("getNextStates: State cannot be null");
            throw new IllegalArgumentException("State cannot be null");
        }
        if (command == null) {
            logger.error("getNextStates: Command cannot be null");
            throw new IllegalArgumentException("Command name cannot be null");
        }
        Element commandElem = null;
        IState[] nextStates = new IState[2];
        Element stateElement = getStateElement(currentState.getState());
        if (FtpLogger.debug) {
            if (stateElement == null) {
                logger.debug("No State Transition for State " + currentState.getState());
            } else {
                logger.debug("State Transition for the current state found");
            }
        }
        NodeList nl = stateElement.getElementsByTagName(COMMAND_TAG);
        if (nl != null && nl.getLength() > 0) {
            int i = 0;
            for (i = 0; i < nl.getLength(); i++) {
                commandElem = (Element) nl.item(i);
                String value = getTextValue(commandElem, COMMAND_NAME_TAG);
                if (command.getCommandName().equalsIgnoreCase(value.trim())) {
                    String successState = getTextValue(commandElem, SUCCESS_STATE_TAG);
                    String failureState = getTextValue(commandElem, FAILURE_STATE_TAG);
                    nextStates[0] = new FtpState(successState);
                    nextStates[1] = new FtpState(failureState);
                    if (FtpLogger.debug) {
                        logger.debug("Next successState is " + successState);
                        logger.debug("Next failureState is " + failureState);
                    }
                    break;
                }
            }
            if (i == nl.getLength()) {
                if (FtpLogger.debug) {
                    logger.debug("Command not in sequence");
                }
            }
        }
        return nextStates;
    }

    private Element getStateElement(String state) throws Exception {
        if (TextUtil.isEmpty(state)) {
            throw new IllegalArgumentException("State cannot be null");
        }
        Element stateElem = null;
        Element docEle = configDoc.getDocumentElement();
        NodeList nl = docEle.getElementsByTagName(FTP_STATE_TAG);
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                stateElem = (Element) nl.item(i);
                String value = getTextValue(stateElem, STATE_TAG);
                if (state.equalsIgnoreCase(value.trim())) {
                    break;
                }
            }
        }
        return stateElem;
    }

    /**
	 * Take a xml element and the tag name, look for the tag and get
	 * the text content 
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is name I will return John  
	 * @param ele
	 * @param tagName
	 * @return
	 */
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        NodeList nl = ele.getElementsByTagName(tagName);
        if (nl != null && nl.getLength() > 0) {
            Element el = (Element) nl.item(0);
            textVal = el.getFirstChild().getNodeValue();
        }
        return textVal;
    }
}
