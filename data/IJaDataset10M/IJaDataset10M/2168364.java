package com.worldware.ichabod.node;

import java.io.*;
import java.util.*;
import com.worldware.mail.pop3.*;
import com.worldware.mail.smtp.*;
import com.worldware.mail.*;
import com.worldware.mail.smtp.server.SMTPReceiveInfo;
import com.worldware.misc.*;
import com.worldware.ichabod.webui.*;

/** This class sends an update to the specified address, at the specified interval
  */
public class TargetPing extends TargetAuto {

    /** The last time we emailed a status report */
    Date lastTime = null;

    private static final String pingIntervalKey = "interval";

    private static final String destinationKey = "destination";

    /** The number of hours between updates until an article in the archive expires
	 */
    PropertyInteger m_interval = new PropertyInteger(this, "PingInterval", pingIntervalKey, new Integer(24), "interval", "Ping Interval (hours)", "The Ping Interval is the number of hours between \"I'm Alive\" messages sent by the server. (168 = 1 week, 0 = disabled)", Property.MODE_USER, Property.GROUP_DEFAULT);

    PropertyAddress m_destination = new PropertyAddress(this, "DestinationAddress", destinationKey, null, "dest", "Destination Address", "The Destination Address is the address that will receive the periodic messages.", Property.MODE_USER, Property.GROUP_DEFAULT);

    /**
	  * @param userName The name of this object
	  * @param targetDir The directory this object persists into
	  * @param parent The parent of this object (e.g. the host this list is on)
	  */
    public TargetPing(File targetDir, String userName, Target parent) throws DataNodeException {
        super(targetDir, userName, parent);
        m_externalProperties.addElement(m_interval);
        m_externalProperties.addElement(m_destination);
    }

    /**
	  * @param userName The name of this object
	  * @param targetDir The directory this object persists into
	  * @param parent The parent of this object (e.g. the host this account is on)
	  */
    TargetPing(String userName, File targetDir, Target parent) throws DataNodeException {
        super(userName, targetDir, parent);
        m_externalProperties.addElement(m_interval);
        m_externalProperties.addElement(m_destination);
    }

    /** Creates the object that will provide the web user interface for this object.<P>
	 * This should only be called once per object.
	 */
    protected WebUI createWebUI() {
        return new WebUITargetPing(this);
    }

    /** We just throw messages away, but we reset our counter when we get them.
	 * <P>
	 * This means that you can set up one ping account to suppress another ping account. When 
	 * the first one stops sending, the seconds one's timer stops getting reset, and it's 
	 * warning message goes out.
	 * */
    public boolean deliverMessage(int depth, MailAddress destinationAddress, MailMessage incomingMessage, MailAddress senderAddress, boolean replyAllowed, SMTPReceiveInfo ri) {
        if (lastTime != null) {
            lastTime = new Date();
            Log.log(Log.MASK_ALWAYS, "Resetting ping timer on " + getOurAddress() + " due to message from " + senderAddress);
        }
        return true;
    }

    /** Creates the property file for a new instance of this class 
	 * @return An newly created object of type TargetPing, returned as TargetAuto, since we can't override and have a different return type.
	 * */
    public static TargetAuto create(String userName, File targetDir, Target parent) throws DataNodeException {
        staticIsNameOK(userName);
        File newFile = getInfoFile(targetDir, false);
        if (!PropertyFile.createFile(newFile)) return (null);
        PropertyFile tempFile = new PropertyFile(newFile);
        tempFile.put(passwordKey, defaultPassword);
        tempFile.put(targetClassKey, TargetPing.class.getName());
        tempFile.put(targetNameKey, userName);
        tempFile.close();
        Vector signature = new Vector();
        TargetPing tl = new TargetPing(userName, targetDir, parent);
        signature.addElement("Periodic Ping from %reply%\r\n");
        signature.addElement("These messages sent after every %interval% hours of operation\r\n");
        tl.putSignature(signature.elements());
        return tl;
    }

    public String toString() {
        return ("[TargetPing (dir:" + getBaseDir() + ")" + "parent: " + super.toString() + "]");
    }

    /** Default subject for repsonses from this account. */
    public String getReplySubject() {
        String s = this.getOneLineDescription();
        if ((s == null) || (s.length() == 0)) return "Automatic status notification from " + getOurAddress();
        String subject = this.replaceText(s);
        return subject;
    }

    /** This is called periodically by the system, to allow nodes to do whatever backups,
	  * etc, that they need to do on a regular basis
	  */
    public void timerTick(Date d) {
        if (lastTime == null) {
            lastTime = d;
            return;
        }
        long tickInterval = getInterval();
        if (tickInterval <= 0) return;
        long interval = d.getTime() - lastTime.getTime();
        if (interval < tickInterval) return;
        if ((interval - tickInterval) < (tickInterval / 2)) lastTime = new Date(lastTime.getTime() + tickInterval); else lastTime = d;
        String subject = getReplySubject();
        doIt(subject);
    }

    /** Sends the message. Logs an error on failure
	 * @return true if successfull
	 */
    public boolean doIt(String subject) {
        String backUpAddress;
        backUpAddress = m_destination.get();
        if ((backUpAddress == null) || (backUpAddress.length() == 0)) {
            Log.error("TargetPing.timerTick: destination address for " + getOurAddress() + " not set, can't ping");
            return false;
        }
        return sendReply(0, backUpAddress, this.getErrorsAddress(), subject, "");
    }

    /** This method does some simple text substitutions on the string passed in.
	  */
    public String replaceText(String s) {
        int i = s.indexOf("%");
        if (i == -1) return s;
        s = substitute(s, "%interval%", m_interval.get());
        s = substitute(s, "%reply%", getOurAddress());
        return super.replaceText(s);
    }

    /** Gets the last time the ticker went off
	 */
    public Date getLastTime() {
        return lastTime;
    }

    /** Gets the interval for this object
	 * @return interval in milliseconds.
	 */
    public long getInterval() {
        int i = m_interval.getInteger().intValue();
        return i * 60L * 60L * 1000L;
    }
}
