package com.bitgate.util.services.engine.tags.u;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.w3c.dom.Node;
import com.bitgate.util.base64.Base64;
import com.bitgate.util.cache.FileCache;
import com.bitgate.util.debug.Debug;
import com.bitgate.util.services.engine.DocumentTag;
import com.bitgate.util.services.engine.RenderEngine;
import com.bitgate.util.services.engine.TagInspector;
import com.bitgate.util.services.engine.tags.ElementDescriber;

/**
 * This element allows you to send an E-Mail using a remote mail server to a recipient, or list of recipients.
 *
 * @author Kenji Hollis &lt;kenji@nuklees.com&gt;
 * @version $Id: //depot/nuklees/util/services/engine/tags/u/Smtp.java#35 $
 */
public class Smtp extends DocumentTag implements ElementDescriber {

    private String host, authuser, authpass, subject, from, to, cc, bcc, contenttype, callProc;

    private HashMap tags;

    public Smtp() {
    }

    public ArrayList getSubElements() {
        ArrayList el = new ArrayList();
        el.add("block");
        el.add("attachment");
        return el;
    }

    public void prepareTag(Node n) {
        super.prepareTag(n, DocumentTag.USE_NO_RECURSION);
        tags = TagInspector.getNodes(n);
        host = TagInspector.get(n, "host");
        authuser = TagInspector.get(n, "authuser");
        authpass = TagInspector.get(n, "authpass");
        subject = TagInspector.get(n, "subject");
        from = TagInspector.get(n, "from");
        to = TagInspector.get(n, "to");
        cc = TagInspector.get(n, "cc");
        bcc = TagInspector.get(n, "bcc");
        callProc = TagInspector.get(n, "call");
        contenttype = TagInspector.get(n, "contenttype");
    }

    class MyAuthenticator extends javax.mail.Authenticator {

        private String au, ap;

        public MyAuthenticator(String au, String ap) {
            this.au = au;
            this.ap = ap;
        }

        public javax.mail.PasswordAuthentication getPasswordAuthentication() {
            return new javax.mail.PasswordAuthentication(au, ap);
        }
    }

    public StringBuffer render(RenderEngine c) {
        if (c.isBreakState() || !c.canRender("u")) {
            return new StringBuffer();
        }
        String logTime = null;
        if (c.getWorkerContext() != null) {
            logTime = c.getWorkerContext().getWorkerStart();
        }
        host = TagInspector.processElement(host, c);
        authuser = TagInspector.processElement(authuser, c);
        authpass = TagInspector.processElement(authpass, c);
        subject = TagInspector.processElement(subject, c);
        from = TagInspector.processElement(from, c);
        to = TagInspector.processElement(to, c);
        cc = TagInspector.processElement(cc, c);
        bcc = TagInspector.processElement(bcc, c);
        callProc = TagInspector.processElement(callProc, c);
        contenttype = TagInspector.processElement(contenttype, c);
        if (subject.equals("") || to.equals("") || tags.get("block") == null || from.equals("")) {
            c.setExceptionState(true, "&lt;Smtp&gt; requires subject, to, block, and from to be present.");
            return new StringBuffer();
        }
        StringBuffer bodyBuffer = null;
        c.getDocumentEngine().newThis();
        if (((bodyBuffer = ((DocumentTag) tags.get("block")).render(c)) == null)) {
            c.setExceptionState(true, "&lt;Smtp&gt; requires &lt;block&gt; to contain data.");
            return new StringBuffer();
        }
        c.getDocumentEngine().deleteThis();
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "10000");
        if (contenttype == null || contenttype.equals("")) {
            contenttype = "text/plain";
        }
        Debug.debug("Called host='" + host + "' authuser='" + authuser + "' authpass='" + authpass + "' subject='" + subject + "' from='" + from + "' to='" + to + "' cc='" + cc + "' bcc='" + bcc + "' contenttype='" + contenttype + "'");
        try {
            javax.mail.Authenticator auth = null;
            if ((authuser != null && !authuser.equals("")) || (authpass != null && !authpass.equals(""))) {
                auth = new MyAuthenticator(authuser, authpass);
                props.put("mail.smtp.auth", "true");
            }
            javax.mail.Session session = null;
            if (auth == null) {
                session = javax.mail.Session.getInstance(props);
                Debug.inform("Using an auth-less default mail session.");
            } else {
                session = javax.mail.Session.getInstance(props, auth);
                Debug.inform("Using an auth default mail session.");
            }
            javax.mail.internet.MimeMessage msg = new javax.mail.internet.MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setHeader("X-Mailer", "uQML-Mail-Server");
            java.util.Calendar calendar = new GregorianCalendar();
            java.util.Date date = calendar.getTime();
            msg.setSentDate(date);
            msg.setContent(bodyBuffer.toString(), contenttype);
            msg.addHeader("Cc", cc);
            msg.addHeader("Bcc", bcc);
            Iterator tagNames = tags.keySet().iterator();
            Multipart multipart = new MimeMultipart();
            boolean hasAttachments = false;
            boolean givenBody = false;
            while (tagNames.hasNext()) {
                String key = (String) tagNames.next();
                String originalKey = new String(key);
                key = TagInspector.processElement(key, c);
                if (key.startsWith("attachment")) {
                    if (key.indexOf("[") != -1) {
                        String keyData = key.substring(key.indexOf("[") + 1, key.indexOf("]"));
                        String[] keyEntries = keyData.split(",");
                        String attachmentFilename = null;
                        String attachmentType = null;
                        String attachmentRootless = null;
                        for (int i = 0; i < keyEntries.length; i++) {
                            String[] keyPair = keyEntries[i].split("=");
                            String currentDocroot = null;
                            if (keyPair[0].equals("filename")) {
                                attachmentFilename = keyPair[1].substring(1, keyPair[1].length() - 1);
                                Debug.inform("Attachment filename '" + attachmentFilename + "'");
                            } else if (keyPair[0].equals("type")) {
                                attachmentType = keyPair[1].substring(1, keyPair[1].length() - 1);
                                Debug.inform("Attachment type '" + attachmentType + "'");
                            } else if (keyPair[0].equals("rootless")) {
                                attachmentRootless = keyPair[1].substring(1, keyPair[1].length() - 1);
                                Debug.inform("Attachment rootless '" + attachmentRootless + "'");
                            }
                        }
                        if (attachmentFilename == null || attachmentFilename.equals("")) {
                            continue;
                        }
                        StringBuffer attachmentBuffer = new StringBuffer();
                        String currentDocroot = null;
                        if (c.getWorkerContext() == null) {
                            if (c.getRenderContext().getCurrentDocroot() == null) {
                                currentDocroot = ".";
                            } else {
                                currentDocroot = c.getRenderContext().getCurrentDocroot();
                            }
                        } else {
                            currentDocroot = c.getWorkerContext().getDocRoot();
                        }
                        if (attachmentRootless != null && attachmentRootless.equalsIgnoreCase("true")) {
                            if (c.getVendContext().getVend().getIgnorableDocroot(c.getClientContext().getMatchedHost())) {
                                currentDocroot = "";
                            }
                        }
                        if (!currentDocroot.endsWith("/")) {
                            if (!currentDocroot.equals("") && currentDocroot.length() > 0) {
                                currentDocroot += "/";
                            }
                        }
                        String attachment = null;
                        byte[] attachmentBytes = null;
                        if (attachmentType.equalsIgnoreCase("file")) {
                            attachmentBytes = FileCache.getDefault().read(c.getWorkerContext(), currentDocroot + attachmentFilename, c.getVendContext().getVend().getRenderExtension(c.getClientContext().getMatchedHost()), c.getVendContext().getVend().getServerpageExtensions(), false);
                        } else if (attachmentType.equalsIgnoreCase("data")) {
                            attachmentBuffer = ((DocumentTag) tags.get(originalKey)).render(c);
                        }
                        if (attachmentBytes == null) {
                            attachmentBytes = Base64.decode(attachmentBuffer.toString().getBytes());
                        }
                        if (attachmentFilename.indexOf("/") != -1) {
                            attachmentFilename = attachmentFilename.substring(attachmentFilename.lastIndexOf("/") + 1);
                        }
                        MimeBodyPart att = new MimeBodyPart();
                        DataSource dSource = new ByteDataSource(attachmentBytes, "binary/binary", attachmentFilename);
                        att.setDataHandler(new DataHandler(dSource));
                        att.setHeader("Content-Type", "binary/binary");
                        att.setHeader("Content-Transfer-Encoding", "base64");
                        att.setHeader("Content-Disposition", "attachment");
                        att.setFileName(attachmentFilename);
                        if (!givenBody) {
                            MimeBodyPart bodyAtt = new MimeBodyPart();
                            bodyAtt.setText(bodyBuffer.toString());
                            givenBody = true;
                            multipart.addBodyPart(bodyAtt);
                        }
                        multipart.addBodyPart(att);
                        hasAttachments = true;
                    } else {
                        continue;
                    }
                }
            }
            if (hasAttachments) {
                msg.setContent(multipart);
            }
            Transport.send(msg);
            Debug.user(logTime, "Message sent successfully to '" + to + "'");
        } catch (javax.mail.AuthenticationFailedException e) {
            c.setExceptionState(true, "Failed to send E-Mail: SMTP Authentication failed: " + e.getMessage());
        } catch (Exception e) {
            c.setExceptionState(true, "&lt;Smtp&gt; failed to send message: " + e);
        }
        if (callProc != null && !callProc.equals("")) {
            Call call = new Call();
            call.callProcedure(c, null, null, callProc, null);
        }
        return new StringBuffer();
    }
}
