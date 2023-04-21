package ru.yep.forum.utils;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Oleg Orlov
 */
public class Emailer {

    public static void main(String[] args) {
        try {
            Options opt = new Options();
            opt.addOption("h", "help", false, "Print help for this application");
            opt.addOption("f", "from", true, "Address to be used for field FROM");
            opt.addOption("t", "to", true, "Address to send");
            opt.addOption("m", "message", true, "Message text");
            opt.addOption("s", "subject", true, "Message subject");
            opt.addOption("p", "smtp", true, "SMTP host[:port = 25]");
            BasicParser parser = new BasicParser();
            CommandLine cl = parser.parse(opt, args);
            if (cl.hasOption("h")) {
                HelpFormatter f = new HelpFormatter();
                f.printHelp("OptionsTip", opt);
                return;
            }
            int errors = 0;
            if (!cl.hasOption("f")) {
                System.err.println("-f 'From' isn't specified");
                errors++;
            }
            if (!cl.hasOption("t")) {
                System.err.println("-t 'To' isn't specified");
                errors++;
            }
            if (!cl.hasOption("m")) {
                System.err.println("-m Text of message isn't specified");
                errors++;
            }
            if (!cl.hasOption("s")) {
                System.err.println("-s Subject isn't specified");
                errors++;
            }
            if (errors > 0) return;
            Emailer mailer = new Emailer("smtp.mail.ru", 587, "forum.client@mail.ru", "forumclient");
            mailer.sendMail("forum.client@mail.ru", "oleg.v.orlov@gmail.com", "About it", "Hi. It's nice to read this message");
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String host = "mail2.mercator.ru";

    private int port = 25;

    private String login = "forumservice@mail2.mercator.ru";

    private String password = "a0mailforum";

    public Emailer(String host, int port, String login, String password) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public Emailer(String host) {
        this.host = host;
    }

    public void sendMail(String fromAddr, String toAddr, String subject, String text) throws Exception {
        Properties props = new SpyingProprs();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.localhost", "127.0.0.1");
        Authenticator auth = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(login, password);
            }
        };
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromAddr));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
        message.setSubject(subject);
        message.setText(text);
        Transport.send(message);
    }

    public static final class SpyingProprs extends Properties {

        private static final long serialVersionUID = 1L;

        public String getProperty(String key, String defaultValue) {
            System.out.println("asked: [" + key + "]=[" + defaultValue + "]");
            return super.getProperty(key, defaultValue);
        }

        public String getProperty(String key) {
            System.out.println("asked: [" + key + "]");
            return super.getProperty(key);
        }

        public synchronized Object get(Object key) {
            return super.get(key);
        }
    }
}
