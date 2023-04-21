package com.worldware.ichabod.node;

import java.io.File;
import java.util.Enumeration;
import com.worldware.ichabod.webui.WebUI;
import com.worldware.ichabod.webui.WebUITargetConfirm;
import com.worldware.mail.InvalidMailAddressException;
import com.worldware.mail.MailAddress;
import com.worldware.mail.MailMessage;
import com.worldware.mail.smtp.server.SMTPReceiveInfo;
import com.worldware.misc.Log;
import com.worldware.misc.PropertyFile;

/** This class handles mailing list subscription confirmations for the whole host
  * probably it would work with one TargetConfirm for all hosts.
  */
public class TargetConfirm extends TargetLocalBase {

    /** Messages received this session */
    int m_curMsgCount = 0;

    /** Successfull confirm messages received this session */
    int m_curOkCount = 0;

    /** Class that tracks outstanding confirmation requests */
    private ConfirmationHandler m_ch;

    /** String that is added to the end of a list's name to make the lists bounce alias.
	  */
    public static final String bounceSuffix = "-bounce";

    /** Reads an existing targetconfirm object from the disk 
	  * @param userName the name of this object (for now, it better be 'confirm')
	  * @param targetDir the directory this object will be read from
	  * @param parent The parent object of this object (a TargetHost)
	  */
    public TargetConfirm(File targetDir, String userName, Target parent) throws DataNodeException {
        super(targetDir, userName, parent);
        m_ch = new ConfirmationHandler(targetDir);
    }

    /** Reads an existing targetconfirm object from the disk 
	  * @param userName the name of this object (for now, it better be 'confirm')
	  * @param targetDir the directory this object will be read from
	  * @param parent The parent object of this object (a TargetHost)
	  */
    TargetConfirm(String userName, File targetDir, Target parent) throws DataNodeException {
        super(userName, targetDir, parent);
        m_ch = new ConfirmationHandler(targetDir);
    }

    /** Creates the object that will provide the web user interface for this object.<P>
	 * This should only be called once per object.
	 */
    protected WebUI createWebUI() {
        return new WebUITargetConfirm(this);
    }

    /** Accepts a confirmation message, and acts on it. 
	  * @param depth This is used to prevent infinite recursion (e.g. two objects forwarding mail to each other)
	  * @param destinationAddress The address this message is going to
	  */
    boolean deliverMessage(int depth, MailAddress destinationAddress, MailMessage message, MailAddress senderAddress, boolean replyAllowed, SMTPReceiveInfo ri) {
        boolean confirm = true;
        if (destinationAddress.extractUsername().endsWith(bounceSuffix)) {
            Log.error("TargetConfirm:  Got a message to the bounce address from '" + senderAddress + ". Will remove token if found");
            confirm = false;
        }
        Log.error("TargetConfirm:  Got a bounce message.  from '" + senderAddress + "' " + senderAddress);
        if (senderAddress == null) {
            Log.error("TargetConfirm:  Got msg from null sender. Will remove token");
            confirm = false;
        }
        depth++;
        m_curMsgCount++;
        String keyToken;
        try {
            keyToken = ConfirmationHandler.findTokenKey(message);
        } catch (DataNodeException dne) {
            Log.error("TargetConfirm: could not find token in message " + message);
            return true;
        }
        ConfirmationToken cfm;
        try {
            cfm = m_ch.verifyTokenAndRemove(keyToken);
        } catch (DataNodeException dne) {
            Log.error("TargetConfirm: Found valid token, but it's not in the hash. (from " + destinationAddress + ") " + message + "\r\n" + getList());
            return true;
        }
        if (!confirm) {
            Log.log(Log.MASK_ALWAYS, "Got a bounce, removed token '" + cfm + "'");
            return true;
        } else Log.log(Log.MASK_ALWAYS, "Not a bounce. token '" + cfm + "'");
        String[] listAddresses;
        MailAddress listAddress;
        String userAddress;
        listAddresses = cfm.getListAddresses();
        userAddress = cfm.getUserAddress();
        MailAddress userMA;
        try {
            userMA = new MailAddress(userAddress, true);
        } catch (InvalidMailAddressException ima) {
            Log.error("TargetConfirm: bad address '" + userAddress + "' in token: " + ima);
            return true;
        }
        String userPart = userMA.extractUsername();
        if (userPart.equalsIgnoreCase("confirm") || userPart.equalsIgnoreCase("confirm" + bounceSuffix)) {
            Log.error("Some joker is trying to subscribe the confirmation handler to a list");
            return true;
        }
        TargetHost th = (TargetHost) getParent();
        for (int i = 0; i < listAddresses.length; i++) {
            listAddress = new MailAddress(listAddresses[i]);
            String listName = listAddress.extractUsername();
            DataNode list1 = th.getChildByName(listName);
            if (!(list1 instanceof TargetList)) {
                Log.error("TargetConfirm: Subscription target '" + listName + "' is not a list. " + cfm);
                return true;
            }
            TargetList list = (TargetList) list1;
            int rc = list.addAddress(userAddress.toString());
            if (rc == TargetList.TL_OK) {
                Log.log(Log.MASK_ALWAYS, "TargetConfirm: Added '" + userAddress + "' to '" + listAddress + "'.");
            } else if (rc == TargetList.TL_DUPLICATE) {
                Log.log(Log.MASK_ALWAYS, "TargetConfirm: '" + userAddress + "' was aready on '" + listAddress + "'.");
            } else {
                Log.error("TargetConfirm: Unable to confirm subscrption of '" + userAddress + "' to '" + listAddress + "' rc = " + rc);
            }
            m_curOkCount++;
        }
        return true;
    }

    /** Does necessary creation for a new instance of this object. */
    public static TargetConfirm create(String userName, File targetDir, Target parent) throws DataNodeException {
        if (!userName.equals("confirm")) throw new DataNodeException("ERROR: The name of a confirmation account must be 'confirm'");
        File newFile = getInfoFile(targetDir, false);
        if (!PropertyFile.createFile(newFile)) return (null);
        PropertyFile tempFile = new PropertyFile(newFile);
        tempFile.put(targetClassKey, TargetConfirm.class.getName());
        tempFile.put(targetNameKey, userName);
        tempFile.put(passwordKey, defaultPassword);
        tempFile.close();
        return new TargetConfirm(userName, targetDir, parent);
    }

    /** Deletes this user, listeners are notified.
	  * returns DEL_OK, DEL_IN_USE or DEL_NOT_EMPTY, or what its parent returns
	  * pass parm force as true to force delete of a box with mail still in the box
	  */
    public int delete(boolean force) {
        if (null != Router.getRouter().usesAddress(getOurAddress(), true)) return DEL_IN_USE;
        return super.delete(force);
    }

    public String toString() {
        return ("TargetConfirm (dir:" + getBaseDir() + ")" + "parent: " + super.toString());
    }

    /** Gets the number of messages recieved by this user during this run.
	  */
    public int getActivityCount() {
        return m_curMsgCount;
    }

    int getMessageCount() {
        return 0;
    }

    public Enumeration listConfirmations() {
        return m_ch.listConfirmations();
    }

    /** Generates a unique ID for new subscription requests, so we can later
	  * implement sending out a message to verify new subscribers.
	  * @param emailAddress The email address of the subscriber
	  * @param listAddress The address of the list being subscribed to
	  */
    String newConfirmation(String emailAddress, MailAddress listAddress) {
        String s = m_ch.newConfirmation(emailAddress, listAddress);
        return s;
    }

    /** Generates a unique ID for new subscription requests, so we can later
	  * implement sending out a message to verify new subscribers.
	  * @param emailAddress The email address of the subscriber
	  * @param listAddresses A String array of the lists being subscribed to. (full list name, like list@host.com)
	  */
    public String newConfirmation(String emailAddress, String[] listAddresses) {
        String s = m_ch.newConfirmation(emailAddress, listAddresses);
        return s;
    }

    /** Gets a configuration token that matches the given key. Like listConfirmations(),
	  * this is a quasi debugging function, that is only used to display the
	  * confirmation queue in the web ui
	  * @see #listConfirmations
	  */
    public String getTokenString(String keyToken) throws DataNodeException {
        return m_ch.getTokenString(keyToken);
    }

    /** Checks to see if this user already has confirmations outstanding.
	 * @param sa Subscriber's email address
	 * @return The confirmation key, if the address already has a pending confirmation, else null
	 */
    public String hasPending(MailAddress sa) {
        return m_ch.hasPending(sa);
    }

    public String removeConfirmation(String key) {
        return m_ch.removeConfirmation(key);
    }

    String getList() {
        return m_ch.getList();
    }

    /** Gets the address to use at the bounce address for confirmation mailings */
    public String getBounceAddress() {
        String ourName = new MailAddress(getParent().getName(), getName() + bounceSuffix).toString();
        return ourName;
    }

    /** Handles the bounce address, too 
	  * @returns true if this object responds to the specified name
	  */
    boolean hasNameOf(String name) {
        String alias = getName() + bounceSuffix;
        if (name.equalsIgnoreCase(alias)) return true;
        return matches(name);
    }
}
