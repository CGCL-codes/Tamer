package gui;

import main.GameData;
import main.Player;
import network.Packet;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * 
 */
public final class ChatPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** TODO_DOCUMENT_ME */
    private static ChatPanel instance = null;

    /** TODO_DOCUMENT_ME */
    private JScrollPane chatOutScrollPane = null;

    /** TODO_DOCUMENT_ME */
    private JEditorPane chatOutEditorPane = null;

    /** TODO_DOCUMENT_ME */
    private JTextField commandTextField = null;

    /** TODO_DOCUMENT_ME */
    private CButton sendButton = null;

    /**
    * Creates a new ChatPanel object.
    */
    private ChatPanel() {
        initialize();
    }

    /**
    * TODO_DOCUMENT_ME
    * 
    * @return DOCUMENT_ME
    */
    public static ChatPanel getInstance() {
        if (instance == null) {
            instance = new ChatPanel();
        }
        return instance;
    }

    /**
    * TODO_DOCUMENT_ME
    */
    private void initialize() {
        setSize(470, 110);
        setLayout(null);
        setOpaque(false);
        add(getChatOutScrollPane());
        add(getCommandTextField());
        add(getSendButton());
    }

    /**
    * TODO_DOCUMENT_ME
    * 
    * @return TODO_DOCUMENT_ME
    */
    private JScrollPane getChatOutScrollPane() {
        if (chatOutScrollPane == null) {
            chatOutScrollPane = new JScrollPane();
            chatOutScrollPane.setSize(420, 67);
            chatOutScrollPane.setLocation(0, 0);
            chatOutScrollPane.setOpaque(false);
            chatOutScrollPane.getViewport().setOpaque(false);
            chatOutScrollPane.setBorder(null);
            chatOutScrollPane.setViewportView(getChatOutEditorPane());
            chatOutScrollPane.setAutoscrolls(true);
        }
        return chatOutScrollPane;
    }

    /**
    * TODO_DOCUMENT_ME
    * 
    * @return TODO_DOCUMENT_ME
    */
    private JEditorPane getChatOutEditorPane() {
        if (chatOutEditorPane == null) {
            chatOutEditorPane = new JEditorPane();
            chatOutEditorPane.setContentType("text/html");
            chatOutEditorPane.setDocument(new HTMLDocument());
            chatOutEditorPane.setOpaque(false);
            chatOutEditorPane.setEditable(false);
            chatOutEditorPane.setSelectionColor(GUIConstants.GRAY);
            chatOutEditorPane.setForeground(GUIConstants.WHITE);
            chatOutEditorPane.setCaretColor(GUIConstants.ORANGE);
            chatOutEditorPane.setAutoscrolls(true);
            chatOutEditorPane.setFont(GUIConstants.INFO_FONT);
        }
        return chatOutEditorPane;
    }

    /**
    * TODO_DOCUMENT_ME
    * 
    * @return TODO_DOCUMENT_ME
    */
    private JTextField getCommandTextField() {
        if (commandTextField == null) {
            commandTextField = new JTextField();
            commandTextField.setSize(350, 20);
            commandTextField.setLocation(0, 80);
            commandTextField.setOpaque(false);
            commandTextField.setBorder(null);
            commandTextField.setForeground(GUIConstants.BLACK);
            commandTextField.setCaretColor(GUIConstants.BLACK);
            commandTextField.setMargin(new Insets(0, 5, 0, 5));
            commandTextField.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    if (e.isControlDown()) {
                        GameFrame.getInstance().dispatchEvent(e);
                    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        GameData.getInstance().getParser().processString(commandTextField.getText());
                        commandTextField.setText("");
                        commandTextField.moveCaretPosition(0);
                    }
                }
            });
        }
        return commandTextField;
    }

    /**
    * TODO_DOCUMENT_ME
    * 
    * @return TODO_DOCUMENT_ME
    */
    private CButton getSendButton() {
        if (sendButton == null) {
            sendButton = new CButton() {

                /**
				 * 
				 */
                private static final long serialVersionUID = 1L;

                public void doActionPerformed() {
                    sendMessage();
                }
            };
            sendButton.setSize(50, 20);
            sendButton.setLocation(360, 80);
            sendButton.setText("Send");
        }
        return sendButton;
    }

    /**
    * TODO_DOCUMENT_ME
    * 
    * @param msg TODO_DOCUMENT_ME
    */
    public void appendMessage(String msg) {
        String[] tokens = msg.split("\n");
        if (tokens.length > 2) {
            String message = "<font color=\"#" + tokens[1] + "\"><b>" + tokens[0] + "</b>: ";
            if (tokens.length == 3) {
                message += tokens[2] + "</font>";
            } else {
                message += "</font>";
            }
            HTMLDocument doc = (HTMLDocument) getChatOutEditorPane().getDocument();
            HTMLEditorKit toolkit = ((HTMLEditorKit) getChatOutEditorPane().getEditorKit());
            try {
                toolkit.insertHTML(doc, doc.getLength(), message, 0, 0, null);
                getChatOutEditorPane().setCaretPosition(doc.getLength());
            } catch (BadLocationException e) {
                if (GUIConstants.DEBUG) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                if (GUIConstants.DEBUG) {
                    e.printStackTrace();
                }
            }
            if (!tokens[0].equals(GameData.getInstance().getLocalPlayer().getName())) {
                GUIConstants.getChatInSound().play();
            }
        } else {
            if (GUIConstants.DEBUG) {
                System.err.println("ERROR: invalid chat message: " + msg);
            }
        }
    }

    /**
    * TODO_DOCUMENT_ME
    * 
    * @param msg TODO_DOCUMENT_ME
    */
    public void println(String msg) {
        String message = "<font color=\"#00FF00\"><b>";
        message += msg + "</b></font>";
        HTMLDocument doc = (HTMLDocument) getChatOutEditorPane().getDocument();
        HTMLEditorKit toolkit = ((HTMLEditorKit) getChatOutEditorPane().getEditorKit());
        try {
            toolkit.insertHTML(doc, doc.getLength(), message, 0, 0, null);
            getChatOutEditorPane().setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            if (GUIConstants.DEBUG) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (GUIConstants.DEBUG) {
                e.printStackTrace();
            }
        }
    }

    /**
    * TODO_DOCUMENT_ME
    */
    public void sendMessage() {
        Packet packet = null;
        String message = commandTextField.getText();
        commandTextField.setText("");
        commandTextField.moveCaretPosition(0);
        if (message.equalsIgnoreCase("/playerinfo")) {
            message = new String();
            ChatPanel.getInstance().println("Player Stats: " + GameData.getInstance().getPlayerList().size());
            for (int i = 0; i < GameData.getInstance().getPlayerList().size(); i++) {
                Player temp = (Player) GameData.getInstance().getPlayerList().get(i);
                ChatPanel.getInstance().println(temp.isReady() + " ,Name= " + temp.getName() + ", Id= " + temp.getIdTag() + ", Color= " + temp.getColor());
            }
        } else {
            String formattedMessage = GameData.getInstance().getLocalPlayer().getName() + "\n" + Integer.toHexString(GameData.getInstance().getLocalPlayer().getColor().getRGB()).substring(2) + "\n" + message;
            packet = new Packet(Packet.TX_MESSAGE, formattedMessage);
            if (GameData.getInstance().getLocalKernel().isServer()) {
                appendMessage(formattedMessage);
            }
            GameData.getInstance().getLocalKernel().send(packet);
        }
        GUIConstants.getChatOutSound().play();
    }

    /**
    * fd
    * 
    * @param message fd
    */
    public void sendMessage(String message) {
        String formattedMessage = GameData.getInstance().getLocalPlayer().getName() + "\n" + GameData.getInstance().getLocalPlayer().getColor() + "\n" + message;
    }
}
