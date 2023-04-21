package james;

import javax.mail.internet.*;
import javax.mail.*;
import java.io.*;
import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

/**
 *
 * @author Dr. Zhou Wu
 */
public class Testing {

    public static void main(String s[]) throws Exception {
        org.apache.avalon.framework.logger.Logger log = new org.apache.avalon.excalibur.logger.ConsoleLoggerManager().getDefaultLogger();
        log = new org.apache.avalon.framework.logger.NullLogger();
        org.apache.james.mailrepository.MBoxMailRepository m = new org.apache.james.mailrepository.MBoxMailRepository();
        org.apache.avalon.framework.configuration.DefaultConfiguration con = new org.apache.avalon.framework.configuration.DefaultConfiguration("testing");
        m.enableLogging(log);
        File f = new File("/tmp/mbox3");
        f.createNewFile();
        con.setAttribute("destinationURL", "mbox:///tmp/mbox3");
        con.setAttribute("type", "MAIL");
        m.configure(con);
        class MyMessage extends MimeMessage {

            private String messageId;

            MyMessage() {
                super((Session) null);
            }

            public void setMessageId(String id) {
                messageId = id;
            }

            protected void updateMessageID() throws MessagingException {
                setHeader("Message-ID", messageId);
            }
        }
        MyMessage mime = new MyMessage();
        java.util.List<String> list = new java.util.ArrayList<String>();
        list.add("Received:  from ONLPEXCBEN01.OLS.Phoenix.edu ([10.17.29.11]) by priv2.OLS.Phoenix.edu with Microsoft SMTPSVC(5.0.2195.6713); Mon, 14 Jan 2008 20:24:4-0700");
        list.add("MIME-Version: 1.0");
        list.add("Content-Type:multipart/mixed");
        list.add("content-class: urn:content-classes:message");
        list.add("Return-Path: <dvannghe@email.phoenix.edu>");
        list.add("X-MimeOLE: Produced By Microsoft Exchange V6.0.6603.0");
        list.add("X-OriginalArrivalTime: 15 Jan 2008 03:24:40.0255 (UTC) FILETIME=[29D974F0:01C85726]");
        list.add("Subject: FW: Denise VanNghe's week 2 individual assignment");
        list.add("Date: Mon, 14 Jan 2008 20:22:02 -0700");
        list.add("Message-ID: <A6BDA80651261141AF6691ECCF9D36B501945933@ONLPEXCBEN01.OLS.Phoenix.edu>");
        list.add("X-MS-Has-Attach: yes");
        list.add("X-MS-TNEF-Correlator:");
        list.add("Thread-Topic: Denise VanNghe's week 2 individual assignment");
        list.add("Thread-Index: AchXEJvu4+8NrpsZT9uvFlm5r7EehgAFS+Uf");
        list.add("References: <BAY143-W45CF0F94C65A74FA0A75ADD6470@phx.gbl>");
        list.add("From: \"Denise VanNghe\" <dvannghe@email.phoenix.edu>");
        list.add("To: \"ZHOU WU\" <zhouwu@email.phoenix.edu>");
        list.add("Cc: \"Denise VanNghe\" <dvannghe@email.phoenix.edu>");
        for (int i = 0; i < list.size(); i++) {
            mime.addHeaderLine(list.get(i));
        }
        mime.setMessageId(mime.getHeader("Message-ID")[0]);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Here's the file");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        String filename = "c:\\tmp\\nbproject551.zip";
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
        mime.setContent(multipart);
        org.apache.james.core.MailImpl mail2 = new org.apache.james.core.MailImpl(mime);
        m.store(mail2);
    }
}
