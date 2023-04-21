package org.goobs.internet;

import java.util.Properties;

public class MailServer {

    public static final int POP = 1;

    public static final int IMAP = 2;

    public static final int OMIT_PROTOCOL = 3;

    private Properties props = new Properties();

    private class StandardMailbox extends Mailbox {

        @Override
        protected Properties getProperties() {
            return props;
        }
    }

    public MailServer(String outgoing, String incoming, int type) {
        props.put("mail.smtp.host", outgoing);
        if (type == POP) {
            props.put("mail.pop.host", incoming);
        } else if (type == IMAP) {
            props.put("mail.imap.host", incoming);
        } else if (type == OMIT_PROTOCOL) {
        } else {
            throw new IllegalArgumentException("Could not find incoming server type: " + type);
        }
    }

    public Mailbox openMailbox() {
        return new StandardMailbox();
    }

    public static void main(String[] args) {
        MailServer serv = new MailServer("gaasanalytical.com", "gaasanalytical.com", IMAP);
        Mailbox m = serv.openMailbox();
        Email msg = new Email("webmaster@gaasanalytical.com");
        msg.setFrom("webmaster@gaasanalytial.com");
        msg.setSubject("hello");
        msg.setText("message");
        msg.attach("/home/gabor/data.dat");
        m.sendMail(msg);
    }
}
