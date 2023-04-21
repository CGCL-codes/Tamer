package server;

import objects.Race;
import objects.util.GamerAddress;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * Author: serhiy
 * Created on Dec 11, 2006, 2:54:04 AM
 */
public final class Answer extends PrintWriter {

    public String subject = null;

    private final Set<javax.mail.internet.InternetAddress> addresses = new HashSet<javax.mail.internet.InternetAddress>();

    private final StringWriter buffer;

    private Answer(StringWriter out) {
        super(new PrintWriter(out));
        buffer = out;
    }

    public Answer() {
        this(new StringWriter());
    }

    public Answer(OutputStream out) {
        super(new PrintWriter(out));
        buffer = null;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void addRecipients(Race race) {
        addresses.addAll(Arrays.asList(race.getAddress(GamerAddress.Mode.ANSWER)));
    }

    public Set<javax.mail.internet.InternetAddress> getRecipients() {
        return addresses;
    }

    public String toString() {
        return buffer.toString();
    }

    public void send(Session session) {
        flush();
        if (session.message == null) return;
        try {
            javax.mail.internet.MimeMessage reply = MailFactory.replyMessage(session.message, subject);
            if (session.isGM) MailFactory.setGameMasterAddress(reply);
            Set<javax.mail.internet.InternetAddress> addresses = new HashSet<javax.mail.internet.InternetAddress>();
            javax.mail.internet.InternetAddress[] addresses1 = (javax.mail.internet.InternetAddress[]) reply.getRecipients(javax.mail.Message.RecipientType.TO);
            if (addresses1 != null) addresses.addAll(Arrays.asList(addresses1));
            addresses.addAll(getRecipients());
            if (!addresses.isEmpty()) {
                reply.setRecipients(javax.mail.Message.RecipientType.TO, addresses.toArray(new javax.mail.internet.InternetAddress[addresses.size()]));
                MailFactory.setText(reply, toString());
                javax.mail.Transport.send(reply);
            }
        } catch (javax.mail.MessagingException err) {
            MailFactory.getLogger().log(Level.SEVERE, "Can't send answer", err);
        }
    }
}
