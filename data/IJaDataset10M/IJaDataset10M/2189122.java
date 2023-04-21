package emailer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.*;
import org.apache.log4j.Logger;

public class Pop3Receiver {

    /**
	 * fetch messages and process them.
	 */
    public static void receive(String storeType, String server, String user, String password, String outputDir, String connection) {
        long start = System.currentTimeMillis();
        boolean deleteMessage = false;
        Store store = null;
        Folder folder = null;
        try {
            Properties props = System.getProperties();
            if ("SSL".equals(connection)) {
                if (log.isDebugEnabled()) {
                    log.debug("using SSL");
                }
                props.setProperty("mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.pop3.host", "pop.gmail.com");
                props.put("mail.pop3.port", "995");
                props.put("mail.pop3.starttls.enable", "true");
                props.setProperty("mail.pop3.socketFactory.fallback", "false");
            }
            Session session = Session.getDefaultInstance(props, null);
            store = session.getStore(storeType);
            store.connect(server, user, password);
            folder = store.getDefaultFolder();
            if (folder == null) {
                throw new Exception("No default folder");
            }
            folder = folder.getFolder("INBOX");
            if (folder == null) {
                throw new Exception("No POP3 INBOX");
            }
            if (deleteMessage) {
                folder.open(Folder.READ_WRITE);
            } else {
                folder.open(Folder.READ_ONLY);
            }
            Message[] messages = folder.getMessages();
            if (log.isDebugEnabled()) {
                log.debug(messages.length + " messages");
            }
            for (int i = 0; i < messages.length; i++) {
                if (messages[i].isSet(Flags.Flag.ANSWERED)) {
                    if (log.isDebugEnabled()) {
                        log.debug("message has already been answered");
                    }
                } else {
                    try {
                        parse(messages[i], outputDir);
                        if (deleteMessage) {
                            if (log.isDebugEnabled()) {
                                log.debug("deleting the message");
                            }
                            messages[i].setFlag(Flags.Flag.DELETED, true);
                        } else {
                            if (log.isDebugEnabled()) {
                                log.debug("marking the message as answered");
                            }
                            messages[i].setFlag(Flags.Flag.ANSWERED, true);
                        }
                    } catch (EmailerException e) {
                        log.error("unable to download file from server");
                    }
                }
            }
        } catch (AuthenticationFailedException ex) {
            log.error("authentication failed, you sure that the details are correct?", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (folder != null) {
                    if (deleteMessage) {
                        folder.close(true);
                    } else {
                        folder.close(false);
                    }
                }
                if (store != null) {
                    store.close();
                }
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
            if (log.isDebugEnabled()) {
                long end = System.currentTimeMillis();
                long took = end - start;
                log.debug("processing took " + took / 1000 + " seconds ");
            }
        }
    }

    public static void parse(Message message, String outputDir) {
        try {
            String subject = message.getSubject();
            if (log.isDebugEnabled()) {
                log.debug("subject = " + subject);
            }
            if (subject != null && subject.indexOf("AEL") >= 0) {
                if (log.isDebugEnabled()) {
                    log.debug("found a data message");
                }
                String contentType = message.getContentType();
                if (log.isDebugEnabled()) {
                    log.debug("contentType = " + contentType);
                }
                if (contentType.startsWith("text/plain") || contentType.startsWith("text/html") || contentType.startsWith("multipart/alternative")) {
                    InputStream is = message.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String thisLine = reader.readLine();
                    while (thisLine != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("line = " + thisLine);
                        }
                        if (thisLine.indexOf("http") > 0) {
                            String urlName = thisLine.substring(thisLine.indexOf("http"), thisLine.length());
                            log.debug("filename = " + urlName);
                            URL url = new URL(urlName.trim());
                            String dataname = subject.substring(subject.indexOf("AEL"), subject.indexOf("_AEL"));
                            File file = new File(outputDir + dataname + ".zip");
                            InputStream downloadInputStream = url.openStream();
                            if (log.isDebugEnabled()) {
                                log.debug("downloading " + url.getPath());
                            }
                            toFile(downloadInputStream, file);
                        }
                        thisLine = reader.readLine();
                    }
                } else {
                    log.error("I don't recognise the contentType '" + contentType + "'");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.error("this isn't a data message");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void toFile(InputStream is, File file) throws EmailerException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            if (log.isDebugEnabled()) {
                log.debug("written " + file.length() + " bytes to " + file.getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            log.error(e);
            throw new EmailerException();
        } catch (IOException e) {
            log.error(e);
            throw new EmailerException();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error(e);
            }
        }
    }

    static {
        log = Logger.getLogger(Pop3Receiver.class);
    }

    public static void main(String[] args) throws IOException {
        if (null != args && args.length > 0) {
            propsLoc = args[0];
        }
        Properties props = new Properties();
        FileInputStream fis = null;
        try {
            if (log.isDebugEnabled()) {
                log.debug("getting properties from " + propsLoc);
            }
            fis = new FileInputStream(propsLoc);
        } catch (FileNotFoundException ex) {
            fis = new FileInputStream("F://email-file-downloader/classes/" + propsLoc);
        }
        props.load(fis);
        props.list(System.out);
        String storeType = props.getProperty("store");
        String server = props.getProperty("server");
        String user = props.getProperty("user");
        String password = props.getProperty("password");
        String outputDir = props.getProperty("outputDir");
        String connection = props.getProperty("connection");
        Pop3Receiver.receive(storeType, server, user, password, outputDir, connection);
    }

    private static String propsLoc = "downloader_argyll.properties";

    public static transient Logger log;
}
