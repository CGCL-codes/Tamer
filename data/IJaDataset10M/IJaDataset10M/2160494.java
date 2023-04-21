package com.quikj.application.web.oamp.logviewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.ScrollPane;
import java.net.UnknownHostException;
import com.quikj.application.web.oamp.messaging.LogRequestMessage;
import com.quikj.application.web.oamp.messaging.OAMPFrameworkMessageParser;
import com.quikj.application.web.oamp.messaging.OAMPMessageInterface;
import com.quikj.application.web.oamp.messaging.OAMPSystemMessageParser;
import com.quikj.application.web.oamp.messaging.RegistrationRequestMessage;
import com.quikj.application.web.oamp.messaging.RegistrationResponseMessage;
import com.quikj.client.beans.RichTextPanel;
import com.quikj.client.framework.HTTPCommunicationErrorInterface;
import com.quikj.client.framework.HTTPMessageListenerInterface;
import com.quikj.client.framework.HTTPRspMessage;
import com.quikj.client.framework.ServerCommunications;

/**
 * 
 * @author amit
 */
public class LogViewerApplet extends java.applet.Applet implements HTTPCommunicationErrorInterface {

    class LoginResponseListener implements HTTPMessageListenerInterface {

        public void messageReceived(int req_id, int status, String content_type, int http_status, String reason, String message) {
            if (status == HTTPMessageListenerInterface.TIMEOUT) {
                disconnect();
                statusBar.setText("Time-out receiving response from server");
            } else if (status == HTTPMessageListenerInterface.RECEIVED) {
                if (http_status == HTTPRspMessage.OK) {
                    OAMPFrameworkMessageParser.setParserType(OAMPFrameworkMessageParser.NANOXML_PARSER);
                    OAMPFrameworkMessageParser parser = null;
                    try {
                        parser = new OAMPFrameworkMessageParser();
                    } catch (Exception ex) {
                        disconnect();
                        statusBar.setText("Error obtaining XML parser");
                        return;
                    }
                    if (parser.parse(message, false) == false) {
                        disconnect();
                        statusBar.setText("Error decoding server response");
                        return;
                    }
                    if (parser.isSystemMessage() == false) {
                        disconnect();
                        statusBar.setText("Bad server response");
                        return;
                    }
                    OAMPSystemMessageParser sysparser = new OAMPSystemMessageParser();
                    if (sysparser.parse(parser.getFirstChild(), false) == false) {
                        disconnect();
                        statusBar.setText("Error decoding server system message response");
                        return;
                    }
                    OAMPMessageInterface parsed_message = sysparser.getMessage();
                    if ((parsed_message instanceof com.quikj.application.web.oamp.messaging.RegistrationResponseMessage) == true) {
                        RegistrationResponseMessage response_msg = (RegistrationResponseMessage) parsed_message;
                        statusBar.setText("Logged in");
                        com.setRequestListener(new RequestListener());
                    } else {
                        disconnect();
                        statusBar.setText("Bad server response");
                        return;
                    }
                } else {
                    disconnect();
                    if (reason != null) {
                        statusBar.setText(reason + " (" + http_status + ")");
                    } else {
                        statusBar.setText("Server returned error: " + http_status);
                    }
                }
            } else {
                disconnect();
                statusBar.setText("Unknown status: " + status + " received from the server");
            }
        }
    }

    class RequestListener implements HTTPMessageListenerInterface {

        public void messageReceived(int req_id, int status, String content_type, int http_status, String reason, String message) {
            OAMPFrameworkMessageParser parser = null;
            try {
                parser = new OAMPFrameworkMessageParser();
            } catch (Exception ex) {
                statusBar.setText("Error obtaining XML parser: " + ex.getClass().getName());
                return;
            }
            if (parser.parse(message, true) == false) {
                statusBar.setText("Received request message could not be parsed: " + parser.getErrorMessage());
                return;
            }
            if (parser.isSystemMessage() == false) {
                statusBar.setText("Received a non system message");
                return;
            }
            OAMPSystemMessageParser sparser = new OAMPSystemMessageParser();
            if (sparser.parse(parser.getFirstChild(), true) == false) {
                statusBar.setText("Received request message could not be parsed: " + parser.getErrorMessage());
                return;
            }
            OAMPMessageInterface parsed_message = sparser.getMessage();
            if ((parsed_message instanceof LogRequestMessage) == true) {
                LogRequestMessage log_message = (LogRequestMessage) parsed_message;
                String log = log_message.getMessage();
                int severity = log_message.getSeverity();
                Color color = Color.black;
                int style = Font.PLAIN;
                switch(severity) {
                    case TRACE:
                        color = Color.black;
                        break;
                    case INFORMATIONAL:
                        color = Color.blue;
                        break;
                    case WARNING:
                        color = Color.yellow;
                        break;
                    case ERROR:
                        color = Color.red;
                        break;
                    case FATAL:
                        color = Color.red;
                        style = Font.BOLD;
                        break;
                }
                logPanel.getTextArea().addText(log, color, style);
            }
        }
    }

    public static final int TRACE = 0;

    public static final int INFORMATIONAL = 1;

    public static final int WARNING = 2;

    public static final int ERROR = 3;

    public static final int FATAL = 4;

    private RichTextPanel logPanel;

    private ServerCommunications com = null;

    private String authCode = "";

    private int port = 8087;

    private int plugin = 3;

    private java.awt.TextField statusBar;

    private java.awt.Panel buttonPanel;

    private java.awt.ScrollPane logPane;

    private boolean connect() {
        if (com != null) {
            disconnect();
        }
        try {
            com = new ServerCommunications(getCodeBase().getHost(), port, plugin, false);
        } catch (UnknownHostException ex) {
            return false;
        }
        com.setClosedListener(this);
        statusBar.setText("Connecting to " + getCodeBase().getHost() + ':' + port + " .....");
        if (com.connect() == false) {
            statusBar.setText("Connection to server " + getCodeBase().getHost() + ':' + port + " failed");
            return false;
        }
        statusBar.setText("Connected to " + getCodeBase().getHost() + ':' + port);
        return true;
    }

    public void communicationError(String host, int port, int appl_id, int cause) {
        disconnect();
        statusBar.setText("Lost connection with the server");
    }

    private void disconnect() {
        if (com != null) {
            if (com.isConnected() == true) {
                com.disconnect();
            }
        }
        statusBar.setText("Disconnected");
        logPanel.getTextArea().clearText();
    }

    private void doLogin() {
        if (connect() == false) {
            statusBar.setText("Connection to " + port + " failed");
            return;
        }
        if (login() == false) {
            statusBar.setText("Could not login to the server");
        }
    }

    /** Initializes the applet LogViewerApplet */
    public void init() {
        OAMPFrameworkMessageParser.setParserType(OAMPFrameworkMessageParser.NANOXML_PARSER);
        initParam();
        initComponents();
    }

    /**
	 * This method is called from within the init() method to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonPanel = new java.awt.Panel();
        logPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
        logPanel = new RichTextPanel();
        logPanel.getTextArea().setBackground(Color.lightGray);
        logPane.add(logPanel);
        logPane.setSize(400, 200);
        statusBar = new java.awt.TextField();
        setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 100.0;
        add(buttonPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 100.0;
        gridBagConstraints.weighty = 100.0;
        add(logPane, gridBagConstraints);
        statusBar.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 100.0;
        add(statusBar, gridBagConstraints);
    }

    private void initParam() {
        String parm = getParameter("auth-code");
        if (parm != null) {
            authCode = parm;
        }
        parm = getParameter("port");
        if (parm != null) {
            try {
                port = Integer.parseInt(parm);
            } catch (NumberFormatException ex) {
                return;
            }
        }
        parm = getParameter("plugin");
        if (parm != null) {
            try {
                plugin = Integer.parseInt(parm);
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }

    private boolean login() {
        RegistrationRequestMessage message = new RegistrationRequestMessage();
        message.setAuthCode(authCode);
        int req_id = com.sendRequestMessage("text/xml", OAMPFrameworkMessageParser.format(OAMPSystemMessageParser.OAMP_SYSTEM_FEATURE_NAME, message), new LoginResponseListener(), 10000);
        if (req_id == -1) {
            return false;
        }
        return true;
    }

    public void start() {
        doLogin();
    }

    public void stop() {
        disconnect();
    }
}
