package com.worldware.mail.smtp.direct;

import java.util.*;
import java.io.*;
import com.worldware.mail.*;
import com.worldware.mail.smtp.client.*;

/** Takes responsibility for delivering a message to a list of addresses that
 * are all on one host.
 */
class DeliveryAgent {

    /** The list of addresses, and the message
	 */
    DeliveryRecord m_dr;

    /** The number of times an exception occurred while trying to deliver
	 * the message.
	 */
    int m_exceptionCount = 0;

    /** Total number of times we have called deliver()
	 * @see #deliver
	 */
    int m_totalRetries = 0;

    /** Creates a new delivery log
	 * @param dr A Delivery Record
	 * @see #m_log
	 */
    DeliveryAgent(DeliveryRecord dr) {
        m_dr = dr;
    }

    /** Tries to deliver the message to the host specified in the DeliveryRecord
	 */
    boolean deliver() throws IOException {
        m_totalRetries++;
        MsgManager mgr = m_dr.getMgr();
        Vector mav = m_dr.getAddresses();
        Vector v = new Vector(mav.size());
        for (Enumeration e = mav.elements(); e.hasMoreElements(); ) {
            v.addElement(((MailAddress) e.nextElement()).toString());
        }
        System.out.println("Creating conection to " + m_dr.getDestHost() + " from " + m_dr.getSourceHost());
        SMTPClientTCP sc = new SMTPClientTCP(m_dr.getDestHost(), m_dr.getSourceHost(), true);
        sc.setDebugPrintStream(System.out);
        sc.setRetryCount(1);
        MailMessage mm = mgr.getMessage();
        String envSender = mgr.getEnvelope().getSender();
        Vector bounceList;
        try {
            bounceList = sc.sendMessage(v, envSender, mm);
        } catch (IOException ioe) {
            m_exceptionCount++;
            return false;
        }
        Vector permanentlyRejected = new Vector();
        for (Enumeration e = mav.elements(); e.hasMoreElements(); ) {
            MailAddress addr = null;
            addr = (MailAddress) e.nextElement();
            boolean bounced = false;
            for (Enumeration b = bounceList.elements(); b.hasMoreElements(); ) {
                DeliveryStatus ds = (DeliveryStatus) b.nextElement();
                if (ds.getAddress().equals(addr)) {
                    bounced = true;
                    permanentlyRejected.addElement(ds);
                    break;
                }
            }
            if (!bounced) {
                System.out.println("Delivered msg " + mgr + " to " + addr);
                mgr.logMsgDelivered(addr, DeliveryLog.RC_SENT);
            }
        }
        if (permanentlyRejected.size() == 0) {
            return true;
        }
        return giveUp(permanentlyRejected, mm, envSender);
    }

    /** Give up on the following addresses, and return the message to the
	 * sender, along with appropriate notification
	 */
    boolean giveUp(Vector permanentlyRejected, MailMessage mm, String envSender) throws IOException {
        MsgManager mgr = m_dr.getMgr();
        mgr.bounce(mm, permanentlyRejected, envSender);
        for (Enumeration b = permanentlyRejected.elements(); b.hasMoreElements(); ) {
            DeliveryStatus ds = (DeliveryStatus) b.nextElement();
            mgr.logMsgDelivered(ds.getAddress(), DeliveryLog.RC_REFUSED);
        }
        return true;
    }
}
