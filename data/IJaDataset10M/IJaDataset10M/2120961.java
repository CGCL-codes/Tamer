package net.suberic.pooka.messaging;

import java.net.*;
import java.nio.channels.*;
import java.io.*;
import java.util.logging.*;
import javax.mail.*;
import javax.mail.internet.MimeMessage;
import net.suberic.pooka.*;
import net.suberic.pooka.gui.NewMessageProxy;
import net.suberic.pooka.gui.NewMessageFrame;
import net.suberic.pooka.gui.MessageUI;

/** 
 * This handles an already-made connection between 
 */
public class PookaMessageHandler extends Thread {

    private static int sCounter = 1;

    Socket mSocket = null;

    boolean mStopped = false;

    PookaMessageListener mParent = null;

    BufferedWriter mWriter = null;

    BufferedReader mReader = null;

    /**
   * Creates a new PookaMessageHandler.
   */
    public PookaMessageHandler(PookaMessageListener pParent, Socket pSocket) {
        super("PookaMessageHandler-" + sCounter++);
        getLogger().log(Level.FINE, "creating new PookaMessageHandler");
        mSocket = pSocket;
        mParent = pParent;
    }

    /**
   * Opens the socket and listens to it.
   */
    public void run() {
        try {
            while (!mStopped && !mSocket.isClosed()) {
                getLogger().log(Level.FINE, "handling messages.");
                mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
                handleMessage(mReader.readLine());
            }
        } catch (Exception e) {
            System.out.println("error in MessageHandler -- closing down.");
            e.printStackTrace();
        }
        cleanup();
    }

    /**
   * Handles the received message.
   */
    public void handleMessage(String pMessage) throws java.io.IOException {
        getLogger().log(Level.FINE, "handling message:  '" + pMessage + "'.");
        if (pMessage != null) {
            if (pMessage.startsWith(PookaMessagingConstants.S_NEW_MESSAGE)) {
                handleNewEmailMessage(pMessage);
            } else if (pMessage.startsWith(PookaMessagingConstants.S_CHECK_VERSION)) {
                handleCheckVersionMessage();
            } else if (pMessage.startsWith(PookaMessagingConstants.S_BYE)) {
                handleByeMessage();
            } else if (pMessage.startsWith(PookaMessagingConstants.S_START_POOKA)) {
                handleStartPookaMessage();
            }
        } else {
            handleByeMessage();
        }
    }

    /**
   * Handles a newEmail message.
   */
    protected void handleNewEmailMessage(String pMessage) {
        getLogger().log(Level.FINE, "it's a new message command.");
        String address = null;
        UserProfile profile = null;
        if (pMessage.length() > PookaMessagingConstants.S_NEW_MESSAGE.length()) {
            int toAddressEnd = pMessage.indexOf(' ', PookaMessagingConstants.S_NEW_MESSAGE.length() + 1);
            if (toAddressEnd == -1) toAddressEnd = pMessage.length();
            address = pMessage.substring(PookaMessagingConstants.S_NEW_MESSAGE.length() + 1, toAddressEnd);
            if (toAddressEnd != pMessage.length() && toAddressEnd != pMessage.length() + 1) {
                String profileString = pMessage.substring(toAddressEnd + 1);
                profile = Pooka.getPookaManager().getUserProfileManager().getProfile(profileString);
            }
        }
        sendNewEmail(address, profile);
    }

    /**
   * Sends a new email message.
   */
    public void sendNewEmail(String pAddress, UserProfile pProfile) {
        final String fAddress = pAddress;
        final UserProfile fProfile = pProfile;
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    getLogger().log(Level.FINE, "creating new message.");
                    NewMessageFrame template = new NewMessageFrame(new NewMessageProxy(new NewMessageInfo(new MimeMessage(Pooka.getDefaultSession()))));
                    MimeMessage mm = new MimeMessage(Pooka.getDefaultSession());
                    if (fAddress != null) mm.setRecipients(Message.RecipientType.TO, fAddress);
                    NewMessageInfo info = new NewMessageInfo(mm);
                    if (fProfile != null) info.setDefaultProfile(fProfile);
                    NewMessageProxy proxy = new NewMessageProxy(info);
                    MessageUI nmu = Pooka.getUIFactory().createMessageUI(proxy, template);
                    nmu.openMessageUI();
                } catch (MessagingException me) {
                    Pooka.getUIFactory().showError(Pooka.getProperty("error.NewMessage.errorLoadingMessage", "Error creating new message:  ") + "\n" + me.getMessage(), Pooka.getProperty("error.NewMessage.errorLoadingMessage.title", "Error creating new message."), me);
                }
            }
        });
    }

    /**
   * Handles a checkVersionMessage.
   */
    public void handleCheckVersionMessage() throws java.io.IOException {
        sendResponse(Pooka.getPookaManager().getLocalrc());
    }

    /**
   * Handles a start Pooka message.
   */
    protected void handleStartPookaMessage() {
        getLogger().log(Level.FINE, "handing start pooka message.");
        if (Pooka.getUIFactory() instanceof net.suberic.pooka.gui.PookaMinimalUIFactory) {
            ((net.suberic.pooka.gui.PookaMinimalUIFactory) Pooka.getUIFactory()).unregisterListeners();
            Pooka.sStartupManager.startupMainPookaWindow(null);
        } else {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    net.suberic.pooka.gui.MainPanel mainPanel = Pooka.getMainPanel();
                    if (mainPanel != null) {
                        getLogger().log(Level.FINE, "calling toFront() on " + javax.swing.SwingUtilities.getWindowAncestor(mainPanel));
                        javax.swing.SwingUtilities.getWindowAncestor(mainPanel).toFront();
                    }
                }
            });
        }
    }

    /**
   * Handles a bye message.
   */
    public void handleByeMessage() throws java.io.IOException {
        closeSocket();
    }

    /**
   * Sends a response.
   */
    public void sendResponse(String pMessage) throws java.io.IOException {
        BufferedWriter writer = getWriter();
        getLogger().log(Level.FINE, "sending response '" + pMessage);
        writer.write(pMessage);
        writer.newLine();
        writer.flush();
    }

    /**
   * Closes the socket.
   */
    public void closeSocket() throws java.io.IOException {
        mSocket.close();
    }

    /**
   * Stops this handler.
   */
    void stopHandler() {
        mStopped = true;
        try {
            closeSocket();
        } catch (Exception e) {
        }
    }

    /**
   * Gets the writer for this handler.
   */
    public BufferedWriter getWriter() throws java.io.IOException {
        if (mWriter == null) {
            synchronized (this) {
                if (mWriter == null) {
                    mWriter = new BufferedWriter(new OutputStreamWriter(mSocket.getOutputStream()));
                }
            }
        }
        return mWriter;
    }

    /** 
   * Cleans up this handler.
   */
    void cleanup() {
        mParent.removeHandler(this);
    }

    /**
   * Gets the logger for this class.
   */
    public Logger getLogger() {
        return Logger.getLogger("Pooka.debug.messaging");
    }
}
