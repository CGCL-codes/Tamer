package lazyj.notifications;

import java.util.Set;
import java.util.TreeSet;
import lazyj.ExtProperties;
import lazyj.mail.Mail;
import lazyj.mail.Sendmail;

/**
 * Send a message as email. This implementation doesn't require any keys but you can specify:<br><ul>
 * <li>[prefix].from=<i>default "from" field to be used if the "from" in {@link Message} is <code>null</code></i></li>
 * <li>[prefix].to=<i>list of comma separated email addresses, complementary to what is defined in the {@link Message}</i></li>
 * <li>[prefix].subject=<i>default subject</i></li>
 * <li>[prefix].message=<i>default message</i></li>
 * <li>[prefix].smtp_ip=<i>IP/name of the SMTP server, default <code>127.0.0.1</code></li>
 * <li>[prefix].smtp_port=<i>TCP port of the SMTP server, default <code>25</code></li>
 * </ul> 
 * 
 * @author costing
 * @since Nov 16, 2007 (1.0.3)
 */
public class EmailSender extends Sender {

    /**
	 * Default mail sender
	 */
    private String sDefaultFrom = null;

    /**
	 * Default list of recipients
	 */
    private Set<String> sDefaultTo = new TreeSet<String>();

    /**
	 * Default subject line
	 */
    private String sDefaultSubject = null;

    /**
	 * Default mail body
	 */
    private String sDefaultMessage = null;

    /**
	 * SMTP server to use
	 */
    private String sServerHost = "127.0.0.1";

    /**
	 * Port on which the SMTP server listens on
	 */
    private int iServerPort = 25;

    @SuppressWarnings("nls")
    @Override
    public boolean init(final ExtProperties prop, final String keyPrefix) {
        this.sDefaultFrom = prop.gets(keyPrefix + "from", this.sDefaultFrom);
        this.sDefaultSubject = prop.gets(keyPrefix + "subject", this.sDefaultSubject);
        this.sDefaultMessage = prop.gets(keyPrefix + "message", this.sDefaultMessage);
        this.sDefaultTo = Message.listToSet(prop.gets(keyPrefix + "to"));
        this.sServerHost = prop.gets(keyPrefix + "smtp_ip", this.sServerHost);
        this.iServerPort = prop.geti(keyPrefix + "smtp_port", this.iServerPort);
        return true;
    }

    @Override
    public boolean send(Message m) {
        final String sFrom = m.sFrom != null ? m.sFrom : this.sDefaultFrom;
        if (sFrom == null) return false;
        final Sendmail sm = new Sendmail(sFrom, this.sServerHost, this.iServerPort);
        final TreeSet<String> ts = new TreeSet<String>(this.sDefaultTo);
        ts.addAll(m.sTo);
        if (ts.size() == 0) return false;
        final Mail mail = new Mail();
        mail.sFrom = sFrom;
        mail.sTo = Message.setToList(ts);
        mail.sBody = m.sMessage != null ? m.sMessage : this.sDefaultMessage;
        mail.sSubject = m.sSubject != null ? m.sSubject : this.sDefaultSubject;
        if (mail.sBody == null || mail.sSubject == null) return false;
        return sm.send(mail);
    }
}
