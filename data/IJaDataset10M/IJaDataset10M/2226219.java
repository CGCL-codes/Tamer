package org.suli.kozosprojekt.brt.mail;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Mail message.
 */
public class Message {

    public String Headers;

    public String Body;

    private final String From;

    private final String To;

    private static final String CRLF = "\r\n";

    public Message(final String from, final String to, final String subject, final String text) {
        this.From = from.trim();
        this.To = to.trim();
        this.Headers = "From: " + this.From + Message.CRLF;
        this.Headers += "To: " + this.To + Message.CRLF;
        this.Headers += "Subject: " + subject.trim() + Message.CRLF;
        final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
        final String dateString = format.format(new Date());
        this.Headers += "Date: " + dateString + Message.CRLF;
        this.Body = text;
    }

    public String getFrom() {
        return this.From;
    }

    public String getTo() {
        return this.To;
    }

    public boolean isValid() {
        final int fromat = this.From.indexOf('@');
        final int toat = this.To.indexOf('@');
        if ((fromat < 1) || ((this.From.length() - fromat) <= 1)) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if ((toat < 1) || ((this.To.length() - toat) <= 1)) {
            System.out.println("Recipient address is invalid");
            return false;
        }
        if (fromat != this.From.lastIndexOf('@')) {
            System.out.println("Sender address is invalid");
            return false;
        }
        if (toat != this.To.lastIndexOf('@')) {
            System.out.println("Recipient address is invalid");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String res;
        res = this.Headers + Message.CRLF;
        res += this.Body;
        return res;
    }
}
