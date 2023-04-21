package com.xmultra.processor.email;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.StringTokenizer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import com.xmultra.XmultraConfig;
import com.xmultra.log.EmailLog;
import com.xmultra.log.ErrorLogEntry;
import com.xmultra.log.MessageLogEntry;
import com.xmultra.processor.Processor;
import com.xmultra.util.InitMapHolder;
import com.xmultra.util.ListHolder;
import com.xmultra.util.SyncFlag;

/**
 * EmailProcessor sends an email to an SMTP server.
 *
 * @author     Wayne W. Weber
 * @version        $Revision: #2 $
 * @since      1.2
 */
public class EmailProcessor extends Processor {

    /**
   * Updated automatically by source control management.
   */
    public static final String VERSION = "@version $Revision: #2 $";

    private File srcDoneLocFile = null;

    private String srcDoneLocation = null;

    private SyncFlag retrySyncFlag = null;

    private boolean notifyAndStartWaitingFlag = false;

    private long timeBetweenRetries = 0;

    private String smtpServerAddress = null;

    private int noRetries = 0;

    private int noRetriesSoFar = 0;

    private static String EMAIL_TYPE_ATTR = "EmailType";

    private static String EMAIL_TO_ATTR = "EmailTo";

    private static String EMAIL_FROM_ATTR = "EmailFrom";

    private static String EMAIL_SUBJECT_ATTR = "EmailSubject";

    private boolean emailXml = true;

    private String textEmailToStr = "";

    private String textEmailFromStr = "";

    private String textEmailSubjectStr = "";

    /**
    * Initializes the Processor object after it has been created.
    *
    * @param n         This Processor's corresponding element in Xmultra's
    *                  main configuration file. A child of the Processor
    *                  element. Has setup info.
    *
    * @param imh       Holds references to utility and log objects.
    *
    * @param sf        Used to communicate between threads.
    *
    * @param stopFlag  Goes false when the thread running in this
    *                  "run()" method should end.
    *
    * @return          True if init was successful.
    */
    public boolean init(Node n, InitMapHolder imh, SyncFlag sf, SyncFlag stopFlag) {
        if (!super.init(n, imh, sf, stopFlag)) return false;
        retrySyncFlag = new SyncFlag(false);
        msgEntry = new MessageLogEntry(this, VERSION);
        errEntry = new ErrorLogEntry(this, VERSION);
        srcDoneLocFile = getLocationDirectory(XmultraConfig.SRC_DONE_LOCATION_ELEMENT);
        if (srcDoneLocFile == null) return false;
        srcDoneLocation = srcDoneLocFile.toString();
        smtpServerAddress = xmlParseUtils.getAttributeValueFromNode(aProcessorNode, XmultraConfig.SMTP_SERVER);
        String noRetriesStr = xmlParseUtils.getAttributeValueFromNode(aProcessorNode, XmultraConfig.NUMBER_RETRIES);
        String secsBetweenRetriesStr = xmlParseUtils.getAttributeValueFromNode(aProcessorNode, XmultraConfig.SECS_BETWEEN_RETRIES);
        try {
            noRetries = Integer.parseInt(noRetriesStr);
            timeBetweenRetries = Long.parseLong(secsBetweenRetriesStr) * XmultraConfig.MS_PER_SECOND;
        } catch (NumberFormatException e) {
            errEntry.setThrowable(e);
            errEntry.setAppContext("init()");
            errEntry.setAppMessage("Bad number format in NumberRetries/" + "SecsBetweenRetries in an EmailProcessor.");
            logger.logError(errEntry);
            return false;
        }
        String emailTypeStr = xmlParseUtils.getAttributeValueFromNode(aProcessorNode, EMAIL_TYPE_ATTR);
        if (emailTypeStr.equals("Text")) {
            emailXml = false;
        }
        textEmailToStr = xmlParseUtils.getAttributeValueFromNode(aProcessorNode, EMAIL_TO_ATTR);
        textEmailFromStr = xmlParseUtils.getAttributeValueFromNode(aProcessorNode, EMAIL_FROM_ATTR);
        textEmailSubjectStr = xmlParseUtils.getAttributeValueFromNode(aProcessorNode, EMAIL_SUBJECT_ATTR);
        return true;
    }

    /**
    * Makes this class runnable. This method is called when the Thread.start()
    * method is invoked. Has the main loop of this thread.
    *
    */
    public void run() {
        if (!processorStopSyncFlag.getFlag()) super.notifyAndStartWaiting();
        noRetriesSoFar = 0;
        notifyAndStartWaitingFlag = true;
        while (!processorStopSyncFlag.getFlag()) {
            sendEmail();
            if (processorStopSyncFlag.getFlag()) break;
            if (notifyAndStartWaitingFlag) {
                super.notifyAndStartWaiting();
                noRetriesSoFar = 0;
                notifyAndStartWaitingFlag = true;
            }
        }
        msgEntry.setAppContext("run()");
        msgEntry.setMessageText("Exiting EmailProcessor.");
        logger.logProcess(msgEntry);
        processorSyncFlag.setFlag(false);
        processorStopSyncFlag.setFlag(false);
    }

    /**
    * Sends the documents.
    */
    private void sendEmail() {
        List fileList = listHolder.getList();
        int listIndex = listHolder.getIndex();
        EmailMessage emailMsg = null;
        File currentFile = null;
        SMTPClient client = new SMTPClient();
        try {
            String host = InetAddress.getLocalHost().getHostName();
            for (; listIndex < fileList.size(); listIndex++) {
                currentFile = (File) fileList.get(listIndex);
                super.currentObjBeingProcessed = currentFile;
                if (currentFile == null || currentFile.length() == 0 || !currentFile.isFile()) {
                    continue;
                }
                try {
                    if (emailXml) {
                        emailMsg = parseXmlEmailDocument(currentFile, new EmailMessage());
                    } else {
                        emailMsg = parseTextEmailDocument(currentFile, new EmailMessage());
                    }
                    client.connect(smtpServerAddress);
                    if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                        client.disconnect();
                        throw new IOException("SMTP server refused connection:" + smtpServerAddress);
                    }
                    client.login(host);
                    boolean result;
                    result = client.setSender(emailMsg.from);
                    if (result == false) {
                        throw new IOException("Email 'From' invalid format: " + emailMsg.from);
                    }
                    StringTokenizer st = new StringTokenizer(emailMsg.to, ",");
                    while (st.hasMoreTokens()) {
                        String to = st.nextToken();
                        result = client.addRecipient(to);
                        if (result == false) {
                            throw new IOException("Email 'To' invalid format: " + emailMsg.to);
                        }
                    }
                    String data = "Subject: " + emailMsg.subject + "\nTo: " + emailMsg.to + "\n\n" + emailMsg.body;
                    result = client.sendShortMessageData(data);
                    if (result == false) {
                        throw new IOException("Email 'Body' invalid format: " + data);
                    }
                    client.logout();
                    if (client.isConnected()) {
                        client.disconnect();
                    }
                } catch (UnknownHostException e) {
                    msgEntry.setDocInfo(currentFile.toString());
                    msgEntry.setMessageText("Unknown host: " + smtpServerAddress);
                    msgEntry.setError(e.getMessage());
                    logger.logWarning(msgEntry);
                } catch (Exception e) {
                    msgEntry.setDocInfo(currentFile.toString());
                    msgEntry.setMessageText("Error in email element(s)");
                    msgEntry.setError(e.getMessage());
                    logger.logWarning(msgEntry);
                    fileUtils.moveFileToDoneLocation(currentFile, srcDoneLocation);
                    fileList.set(listIndex, null);
                    currentFile = null;
                    continue;
                }
                msgEntry.setDocInfo(currentFile.toString());
                msgEntry.setMessageText("Email successfully sent to '" + emailMsg.to + "'");
                logger.logProcess(msgEntry);
                if (processorStopSyncFlag.getFlag()) break;
            }
        } catch (IOException e) {
            if (noRetriesSoFar++ < noRetries) {
                waitBetweenRetry();
                notifyAndStartWaitingFlag = false;
            } else {
                notifyAndStartWaitingFlag = true;
                errEntry.setThrowable(e);
                errEntry.setAppContext("sendEmail()");
                errEntry.setAppMessage("Unable to send file after " + (noRetriesSoFar - 1) + " retries. Max Retries.");
                logger.logError(errEntry);
            }
            return;
        }
        moveFilesToDone(listHolder);
        notifyAndStartWaitingFlag = true;
    }

    /**
    * Moves the files to the done location and advances the index.
    *
    * @param lh    The list of files (emails) to move to done.
    */
    private void moveFilesToDone(ListHolder lh) {
        List fileList = lh.getList();
        int listIndex = lh.getIndex();
        for (; listIndex < fileList.size(); listIndex++) {
            if (fileList.get(listIndex) == null) continue;
            fileUtils.moveFileToDoneLocation((File) fileList.get(listIndex), srcDoneLocation);
        }
        listHolder.setIndex(listIndex);
    }

    /**
    * Wait for a period of time.
    */
    private void waitBetweenRetry() {
        try {
            retrySyncFlag.waitUntilTrue(timeBetweenRetries);
        } catch (InterruptedException e) {
            errEntry.setThrowable(e);
            errEntry.setAppContext("waitBetweenRetry()");
            logger.logError(errEntry);
        }
    }

    /**
    * Parses an XML email document into its components.
    *
    * @param file      The email message is in this file.
    *
    * @param email     An empty object which will contain the email message.
    *
    * @return          An object which contains the email message.
    *
    * @exception SAXException  Thrown if there is an error in the structure of
    *                          the Xml document (in the file).
    */
    private EmailMessage parseXmlEmailDocument(File file, EmailMessage email) throws SAXException {
        String emailDocumentStr = fileUtils.readFile(file);
        Document emailDocument = xmlParseUtils.convertStringToXmlDocument(emailDocumentStr, false);
        email.to = xmlParseUtils.getTextFromDocument(emailDocument, EmailLog.TO);
        email.from = xmlParseUtils.getTextFromDocument(emailDocument, EmailLog.FROM);
        email.subject = xmlParseUtils.getTextFromDocument(emailDocument, EmailLog.SUBJECT);
        email.body = xmlParseUtils.getTextFromDocument(emailDocument, EmailLog.BODY);
        return email;
    }

    /**
    * if email type is text
    * email message reading from file
    * the file doesn't contain email information (To, From, Subject)
    * Get information from system configuration
    *
    * @param file      The email message is in this file.
    *
    * @param email     An empty object which will contain the email message.
    *
    * @return          An object which contains the email message.
    */
    private EmailMessage parseTextEmailDocument(File file, EmailMessage email) {
        String emailDocumentStr = fileUtils.readFile(file);
        if ((textEmailToStr != null) && (textEmailToStr.length() > 1)) {
            email.to = textEmailToStr;
        }
        if ((textEmailFromStr != null) && (textEmailFromStr.length() > 1)) {
            email.from = textEmailFromStr;
        }
        if ((textEmailSubjectStr != null) && (textEmailSubjectStr.length() > 1)) {
            email.subject = textEmailSubjectStr;
        }
        email.body = emailDocumentStr;
        return email;
    }

    /**
    * An inner class to hold the contents of an email message.
    */
    private class EmailMessage {

        private String to = "";

        private String from = "";

        private String subject = "";

        private String body = "";
    }
}
