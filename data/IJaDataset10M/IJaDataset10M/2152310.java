package net.sourceforge.freecol.client;

import net.sourceforge.freecol.common.FreeColException;
import net.sourceforge.freecol.networking.Message;
import net.sourceforge.freecol.networking.MessageHandler;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.util.logging.Logger;

/**
* The handler for messages that have
* been received before the game has started.
*/
public final class PreGameMessageHandler implements MessageHandler {

    private static final Logger logger = Logger.getLogger(PreGameMessageHandler.class.getName());

    private final PreGameNetworkListener listener;

    /**
    * The constructor to use.
    * @param l The listener for this handler.
    */
    public PreGameMessageHandler(PreGameNetworkListener l) {
        listener = l;
    }

    /**
    * Deals with incoming messages that have just been received.
    * @param message The message to deal with.
    * @throws FreeColException
    */
    public void handle(Message message) throws FreeColException {
        if (message != null) {
            logger.info("Received a " + message.getType() + " message.");
            if (message.getType().equals("AddPlayer")) {
                handleAddPlayer(message.getDocument().getDocumentElement());
            } else if (message.getType().equals("RemovePlayer")) {
                handleRemovePlayer(message.getDocument().getDocumentElement());
            } else if (message.getType().equals("ServerChat")) {
                handleServerChat(message.getDocument().getDocumentElement());
            } else if (message.getType().equals("Version")) {
            } else if (message.getType().equals("State")) {
                handleState(message.getDocument().getDocumentElement());
            } else if (message.getType().equals("PlayerReady")) {
                handlePlayerReady(message.getDocument().getDocumentElement());
            } else if (message.getType().equals("UpdateNation")) {
                handleUpdateNation(message.getDocument().getDocumentElement());
            } else if (message.getType().equals("UpdateColor")) {
                handleUpdateColor(message.getDocument().getDocumentElement());
            } else if (message.getType().equals("Error")) {
                handleError(message.getDocument().getDocumentElement());
            } else {
                throw new FreeColException("Message is of unsupported type \"" + message.getType() + "\".");
            }
        }
    }

    /**
    * Handles an AddPlayer message.
    * @param element The element (root element in a DOM-parsed XML tree) that
    * holds all the information.
    * @throws FreeColException
    */
    private void handleAddPlayer(Element element) throws FreeColException {
        String att = element.getAttribute("admin");
        boolean admin;
        if ((att != null) && att.equals("true")) {
            admin = true;
        } else {
            admin = false;
        }
        Node node = element.getElementsByTagName("AddPlayerName").item(0);
        if ((node != null) && (node.getNodeName().equals("AddPlayerName"))) {
            node = node.getFirstChild();
        } else {
            throw new FreeColException("AddPlayer hasn't got an element named \"AddPlayerName\".");
        }
        if (node == null) {
            throw new FreeColException("Invalid AddPlayerName tag.");
        } else {
            logger.info("Player joined: " + node.getNodeValue());
            listener.addPlayer(node.getNodeValue(), admin);
        }
    }

    /**
    * Handles a RemovePlayer message.
    * @param element The element (root element in a DOM-parsed XML tree) that
    * holds all the information.
    * @throws FreeColException
    */
    private void handleRemovePlayer(Element element) throws FreeColException {
    }

    /**
    * Handles a ServerChat message.
    * @param element The element (root element in a DOM-parsed XML tree) that
    * holds all the information.
    * @throws FreeColException
    */
    private void handleServerChat(Element element) throws FreeColException {
        String serverChatName = null, serverChatMessage = null;
        boolean privateChat = false;
        String att = element.getAttribute("private");
        if (att.equals("true")) {
            privateChat = true;
        }
        Node node = element.getElementsByTagName("ServerChatName").item(0);
        if ((node != null) && (node.getNodeName().equals("ServerChatName"))) {
            node = node.getFirstChild();
            if (node != null) {
                serverChatName = node.getNodeValue();
            }
        }
        if (serverChatName == null) {
            throw new FreeColException("'ServerChatName' tag in ServerChat message has an invalid value.");
        }
        node = element.getElementsByTagName("ServerChatMessage").item(0);
        if ((node != null) && (node.getNodeName().equals("ServerChatMessage"))) {
            node = node.getFirstChild();
            if (node != null) {
                serverChatMessage = node.getNodeValue();
            }
        }
        if (serverChatMessage == null) {
            throw new FreeColException("'ServerChatMessage' tag in ServerChat message has an invalid value.");
        }
        listener.chatReceived(serverChatName, serverChatMessage, privateChat);
    }

    /**
    * Handles a State message.
    * @param element The element (root element in a DOM-parsed XML tree) that
    * holds all the information.
    * @throws FreeColException
    */
    private void handleState(Element element) throws FreeColException {
        String att = element.getAttribute("type");
        if (!att.equals("inGame")) {
            throw new FreeColException("'type' attribute in State message is not allowed to be \"" + att + "\" at this point.");
        }
        String firstPlayer = null;
        Node node = element.getElementsByTagName("CurrentPlayerName").item(0);
        if ((node != null) && (node.getNodeName().equals("CurrentPlayerName"))) {
            node = node.getFirstChild();
            if (node != null) {
                firstPlayer = node.getNodeValue();
                if (firstPlayer == null) {
                    throw new FreeColException("'State' message has an invalid value for the 'CurrentPlayerName' tag.");
                }
            }
        }
        node = element.getElementsByTagName("StateMessage").item(0);
        if ((node != null) && (node.getNodeName().equals("StateMessage"))) {
            node = node.getFirstChild();
            if (node != null) {
                logger.info("StateMessage: " + node.getNodeValue());
            }
        }
        listener.startGame(firstPlayer);
    }

    /**
    * Handles a PlayerReady message.
    * @param element The element (root element in a DOM-parsed XML tree) that
    * holds all the information.
    * @throws FreeColException
    */
    private void handlePlayerReady(Element element) throws FreeColException {
        Node node = element.getElementsByTagName("PlayerReadyName").item(0);
        if ((node != null) && (node.getNodeName().equals("PlayerReadyName"))) {
            node = node.getFirstChild();
        } else {
            throw new FreeColException("PlayerReady hasn't got an element named \"PlayerReadyName\".");
        }
        if (node != null) {
            listener.playerReady(node.getNodeValue());
        } else {
            throw new FreeColException("Invalid PlayerReadyName tag.");
        }
    }

    /**
    * Handles an UpdateNation message.
    * @param element The element (root element in a DOM-parsed XML tree) that
    * holds all the information.
    * @throws FreeColException
    */
    private void handleUpdateNation(Element element) throws FreeColException {
        Node node = element.getElementsByTagName("UpdateNationName").item(0);
        if ((node != null) && (node.getNodeName().equals("UpdateNationName"))) {
            node = node.getFirstChild();
        } else {
            throw new FreeColException("UpdateNation hasn't got an element named \"UpdateNationName\".");
        }
        if (node != null) {
            String att = element.getAttribute("nation");
            if (att != null) {
                listener.setNation(node.getNodeValue(), att);
            } else {
                throw new FreeColException("Invalid UpdateNation tag: invalid nation attribute.");
            }
        } else {
            throw new FreeColException("Invalid UpdateNationName tag.");
        }
    }

    /**
    * Handles an UpdateColor message.
    * @param element The element (root element in a DOM-parsed XML tree) that
    * holds all the information.
    * @throws FreeColException
    */
    private void handleUpdateColor(Element element) throws FreeColException {
        Node node = element.getElementsByTagName("UpdateColorName").item(0);
        if ((node != null) && (node.getNodeName().equals("UpdateColorName"))) {
            node = node.getFirstChild();
        } else {
            throw new FreeColException("UpdateColor hasn't got an element named \"UpdateColorName\".");
        }
        if (node != null) {
            String att = element.getAttribute("color");
            if (att != null) {
                listener.setColor(node.getNodeValue(), att);
            } else {
                throw new FreeColException("Invalid UpdateColor tag: invalid color attribute.");
            }
        } else {
            throw new FreeColException("Invalid UpdateColorName tag.");
        }
    }

    /**
    * Handles an Error message.
    * @param element The element (root element in a DOM-parsed XML tree) that
    * holds all the information.
    * @throws FreeColException
    */
    private void handleError(Element element) throws FreeColException {
        Node node = element.getElementsByTagName("ErrorMessage").item(0);
        if ((node != null) && (node.getNodeName().equals("ErrorMessage"))) {
            node = node.getFirstChild();
        } else {
            throw new FreeColException("Error hasn't got an element named \"ErrorMessage\".");
        }
        if (node != null) {
            listener.errorMessage(node.getNodeValue());
        } else {
            throw new FreeColException("Invalid Error tag.");
        }
    }
}
