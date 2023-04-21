package ui;

import gvME.Logger;
import gvME.gvME;
import gvME.parseMsgs;
import gvME.settings;
import gvME.textConvo;
import gvME.tools;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStoreException;

/**
 *
 * @author Matt Defenthaler
 */
public class Inbox extends MailBox {

    private static Hashtable inboxHash;

    private Command replyCmd, refreshCmd, callCmd, markUnreadCmd;

    public Inbox() throws IOException, RecordStoreException {
        super("Inbox", "inboxStore");
        addCommand(getReplyCmd());
        addCommand(getRefreshCmd());
        addCommand(getCallCmd());
        addCommand(getMarkUnreadCmd());
        setSelectCommand(getReadCmd());
        initInboxHash();
    }

    /**
     *creates a hashtable of textConvos. Key = textConvo.msgID; Value = textConvo
     *this is done to more quickly check to see if a textConvo with a given msgID already exists
     */
    private void initInboxHash() {
        Enumeration listEnum = list.elements();
        while (listEnum.hasMoreElements()) {
            textConvo crnt = (textConvo) listEnum.nextElement();
            if (!crnt.getIsRead()) numUnread++;
            getInboxHash().put(crnt.getMsgID(), crnt);
        }
        updateUnread();
    }

    /**
     * Updates the main menu's Inbox item to reflect the number of unread messages the Inbox contains
     */
    private void updateUnread() {
        String[] inboxUnread = { "Inbox (", String.valueOf(numUnread), ")" };
        gvME.setMenu(1, tools.combineStrings(inboxUnread));
    }

    /**
     * Adds new messages to the Inbox's list vector, GUI List, and inboxHash
     * @param newMsgs Vector of messages to add to the Inbox
     * @throws IOException
     * @throws RecordStoreException
     */
    public void updateInbox(Vector newMsgs) throws IOException, RecordStoreException {
        int numNewMsgs = 0;
        if (newMsgs != null && (numNewMsgs = newMsgs.size()) > 0) {
            for (int i = numNewMsgs - 1; i >= 0; i--) {
                textConvo newConvo = (textConvo) newMsgs.elementAt(i);
                int index = -1;
                if (getInboxHash().containsKey(newConvo.getMsgID())) {
                    textConvo crnt = (textConvo) getInboxHash().get(newConvo.getMsgID());
                    if (crnt.getIsRead()) {
                        numUnread++;
                        crnt.setIsRead(false);
                    }
                    index = list.indexOf(crnt);
                    getInboxHash().put(crnt.getMsgID(), crnt);
                    list.setElementAt(crnt, index);
                    addItem(crnt, index);
                } else {
                    numUnread++;
                    getInboxHash().put(newConvo.getMsgID(), newConvo);
                    addItem(newConvo);
                }
            }
            updateUnread();
        }
    }

    /**
     * Sets specified textConvo to read.
     * @param crnt textConvo to mark read.
     * @param selIndex index of textConvo in GUI List
     */
    private void markConvoRead(textConvo crnt, int selIndex) {
        if (!crnt.getIsRead()) {
            crnt.setIsRead(true);
            numUnread--;
            set(selIndex, getString(selIndex), super.msgIsRead);
        } else {
            numUnread++;
            crnt.setIsRead(false);
            set(selIndex, getString(selIndex), super.msgIsUnread);
        }
        updateUnread();
        list.setElementAt(crnt, selIndex);
        getInboxHash().put(crnt.getMsgID(), crnt);
        updateRS();
    }

    /**
     * Method to manually check for new messages.
     */
    private void refreshInbox() {
        Thread refreshThread = new Thread() {

            public void run() {
                try {
                    gvME.getInbox().updateInbox(parseMsgs.readMsgs());
                } catch (IOException ex) {
                    Logger.add(getClass().getName(), ex.getMessage());
                    ex.printStackTrace();
                } catch (Exception ex) {
                    Logger.add(getClass().getName(), ex.getMessage());
                    ex.printStackTrace();
                }
            }
        };
        refreshThread.start();
    }

    public static Hashtable getInboxHash() {
        if (inboxHash == null) {
            inboxHash = new Hashtable();
        }
        return inboxHash;
    }

    private Command getReplyCmd() {
        if (replyCmd == null) {
            replyCmd = new Command("Reply", Command.ITEM, 2);
        }
        return replyCmd;
    }

    private Command getCallCmd() {
        if (callCmd == null) {
            callCmd = new Command("Call", Command.ITEM, 3);
        }
        return callCmd;
    }

    private Command getMarkUnreadCmd() {
        if (markUnreadCmd == null) {
            markUnreadCmd = new Command("Mark (Un)read", Command.ITEM, 4);
        }
        return markUnreadCmd;
    }

    private Command getRefreshCmd() {
        if (refreshCmd == null) {
            refreshCmd = new Command("Refresh", Command.ITEM, 8);
        }
        return refreshCmd;
    }

    public void commandAction(Command command, Displayable displayable) {
        if (command == backCmd) super.commandAction(command, displayable); else if (!list.isEmpty() && (command == delItemCmd || command == delAllCmd)) {
            if (command == delItemCmd) {
                textConvo crnt = (textConvo) list.elementAt(getSelectedIndex());
                String hashkey = (String) crnt.getMsgID();
                getInboxHash().remove(hashkey);
                if (!crnt.getIsRead()) numUnread--;
                updateUnread();
            }
            if (command == delAllCmd) {
                getInboxHash().clear();
                numUnread = 0;
                updateUnread();
            }
            super.commandAction(command, displayable);
        } else if (command == refreshCmd) {
            refreshInbox();
        } else if (!list.isEmpty()) {
            int selIndex = this.getSelectedIndex();
            textConvo original = (textConvo) list.elementAt(selIndex);
            if (command == readCmd) {
                textConvo crnt = (textConvo) list.elementAt(selIndex);
                if (!crnt.getIsRead()) markConvoRead(crnt, selIndex);
                gvME.dispMan.switchDisplayable(null, new MsgList(crnt));
            } else if (command == replyCmd) {
                WriteMsg wm = new WriteMsg("Reply", original);
                gvME.dispMan.switchDisplayable(null, wm);
            } else if (command == callCmd) {
                try {
                    MakeCall mc = new MakeCall(original.getReplyNum(), original.getSender());
                    gvME.dispMan.switchDisplayable(null, mc);
                } catch (Exception ex) {
                    if (ex.toString().indexOf("no call from") >= 0) {
                        gvME.dispMan.switchDisplayable(gvME.getNoCallFromAlert(), settings.getChangeSettingsMenu());
                    }
                }
            } else if (command == markUnreadCmd) {
                textConvo crnt = (textConvo) list.elementAt(selIndex);
                markConvoRead(crnt, selIndex);
            }
        }
    }
}
