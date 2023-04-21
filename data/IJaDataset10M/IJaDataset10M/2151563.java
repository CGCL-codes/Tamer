package net.sf.traser.propagation.propagators;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import net.sf.traser.configuration.AbstractConfigurable;
import net.sf.traser.configuration.ConfigManager;
import net.sf.traser.configuration.ConfigurationException;
import net.sf.traser.configuration.composite.ManagerService;
import net.sf.traser.configuration.parameterized.ParameterizedService;
import net.sf.traser.databinding.base.CreateEvent;
import net.sf.traser.databinding.base.CreateEvent.ItemProperty;
import net.sf.traser.databinding.base.GetPropertyValues;
import net.sf.traser.databinding.base.PropertyValueResponse.Value;
import net.sf.traser.databinding.base.PropertyValuesReport;
import net.sf.traser.facade.BasicFacade;
import net.sf.traser.propagation.GuardExpression;
import net.sf.traser.propagation.Propagator;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.util.XMLUtils;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Marcell Szathmári
 */
public class EmailNotifier extends AbstractConfigurable implements Propagator {

    private static final Logger LOG = Logger.getLogger(EmailNotifier.class.getName());

    public static final String SOURCEPROPERTY = "source-property";

    public static final String EMAILPROPERTY = "email-property";

    public static final String SMTPHOST = "smtp-host";

    public static final String SMTPPORT = "smtp-port";

    public static final String SMTPUSER = "smtp-user";

    public static final String SMTPPASS = "smtp-pass";

    public static final String RESOLUTION = "resolution";

    public static final String SENDER = "sender";

    /** Mail configuration properties */
    private Properties mailProp = new Properties();

    /** The manager service that manages the triggers. */
    private final ManagerService<String, GuardExpression> guardManager;

    /** The manager service that manages the triggers. */
    private final ParameterizedService parameters;

    /**
     * Creates a composite updater instance.
     */
    public EmailNotifier() {
        guardManager = new ManagerService<String, GuardExpression>(GUARD, GuardExpression.class);
        parameters = new ParameterizedService(SOURCEPROPERTY, EMAILPROPERTY, SMTPHOST, SMTPPORT, SMTPUSER, SMTPPASS, RESOLUTION, SENDER);
    }

    @Override
    public void init(OMElement configuration, ConfigManager manager) {
        super.init(configuration, manager);
        guardManager.init(configuration, manager);
        parameters.init(configuration, manager);
    }

    /** The polling thread. */
    private Thread process;

    /** The runnable task the process executes. */
    private Runnable task;

    /** Flag for exiting the poll cycle. */
    private Boolean finish = false;

    /** The interval, the propagator sends the emails. */
    private int pollFreq;

    /** The default interval, the propagator sends the emails. */
    private int DEF_POLLFREQ = 1000 * 60 * 60 * 24;

    @Override
    public void finish() {
        super.finish();
        finish = true;
        if (process != null) {
            try {
                process.interrupt();
                process.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(EmailNotifier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void configure() throws ConfigurationException {
        guardManager.configure();
        parameters.configure();
        updatePollFreq();
        mailProp.put("mail.smtp.protocol", "smtp");
        mailProp.put("mail.smtp.auth", "true");
        if (get(SMTPHOST) != null) {
            mailProp.put("mail.smtp.host", get(SMTPHOST));
        }
        if (get(SMTPPORT) != null) {
            mailProp.put("mail.smtp.port", get(SMTPPORT));
        }
        task = new Runnable() {

            public void run() {
                Thread.currentThread().setPriority(1);
                while (!finish) {
                    try {
                        Session session = Session.getInstance(mailProp, new Authenticator() {

                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(get(SMTPUSER), get(SMTPPASS));
                            }
                        });
                        Message msg = new MimeMessage(session);
                        try {
                            msg.setFrom(new InternetAddress(get(SENDER)));
                            msg.setSubject("TraSer status change notification digest");
                        } catch (AddressException e) {
                            e.printStackTrace();
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                        synchronized (messages) {
                            for (Map.Entry<String, LinkedList<String>> e : messages.entrySet()) {
                                if (e.getValue().size() > 0) {
                                    String body = StringUtils.join(e.getValue().iterator(), "\n");
                                    InternetAddress address = new InternetAddress();
                                    address.setAddress(e.getKey());
                                    LOG.info("Trying to send notification to: " + address);
                                    try {
                                        msg.setRecipient(Message.RecipientType.TO, address);
                                        msg.setText(body);
                                        Transport.send(msg);
                                        e.getValue().clear();
                                        LOG.info("Have sent notification to: " + address + " ('" + body + "')");
                                    } catch (MessagingException ex) {
                                        Logger.getLogger(EmailNotifier.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                        Thread.sleep(pollFreq);
                    } catch (InterruptedException ex) {
                        System.out.println("Process: " + this.toString() + " interrupted");
                    }
                }
                System.out.println("Email notifier process finished.");
            }
        };
        process = new Thread(task);
        process.start();
    }

    public void remove(Class<GuardExpression> c, String i) {
        guardManager.remove(c, i);
    }

    public GuardExpression get(Class<GuardExpression> c, String i) {
        return guardManager.get(c, i);
    }

    public Collection<GuardExpression> list(Class<GuardExpression> c) {
        return guardManager.list(c);
    }

    public void add(GuardExpression t) {
        guardManager.add(t);
    }

    public void set(String property, String value) {
        parameters.set(property, value);
        updatePollFreq();
        if (get(SMTPHOST) != null) {
            mailProp.put("mail.smtp.host", get(SMTPHOST));
        }
        if (get(SMTPPORT) != null) {
            mailProp.put("mail.smtp.port", get(SMTPPORT));
        }
    }

    /** Updates the polling frequency according to the value of the RESOLUTION parameter. */
    private void updatePollFreq() {
        String pFreq = get(RESOLUTION);
        if (pFreq != null && !"".equals(pFreq)) {
            pollFreq = 1000 * Integer.parseInt(pFreq);
        } else {
            pollFreq = DEF_POLLFREQ;
        }
    }

    public Collection<String> getPropertyNames() {
        return parameters.getPropertyNames();
    }

    public String get(String property) {
        return parameters.get(property);
    }

    public Collection<String> getAffectedProperties() {
        return Collections.singleton(get(SOURCEPROPERTY));
    }

    public String getId() {
        return this.toString();
    }

    /** The list of messages grouped by the address to send them.
     * The notifier thread reads and empties this list. */
    private final HashMap<String, LinkedList<String>> messages = new HashMap<String, LinkedList<String>>();

    public Collection<CreateEvent> getEvents(CreateEvent event) {
        ItemProperty iProp = null;
        for (ItemProperty ip : event.getItemProperties()) {
            if (ip.getName().equals(get(SOURCEPROPERTY))) {
                iProp = ip;
                break;
            }
        }
        if (iProp != null) {
            LinkedList<String> messageRows = new LinkedList<String>();
            messageRows.add("Property '" + get(SOURCEPROPERTY) + "' of item '" + event.getItem().getItemId() + "' was updated.");
            if (iProp.getClear() != null && iProp.getClear()) {
                messageRows.add("\t The existing values has been cleared.");
            }
            for (ItemProperty.Value val : iProp.getValues()) {
                try {
                    messageRows.add("\t The value '" + XMLUtils.toOM(val.getAny()).toString() + "' has been " + (val.getAction() == val.getAction().ADD ? "added" : "removed") + ".");
                } catch (Exception ex) {
                    Logger.getLogger(EmailNotifier.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            String message = StringUtils.join(messageRows.iterator(), "\n");
            try {
                GetPropertyValues gpv = new GetPropertyValues();
                gpv.setItem(event.getItem());
                gpv.addProperty(get(EMAILPROPERTY));
                PropertyValuesReport pvr = manager.get(BasicFacade.class).getPropertyValues(gpv);
                synchronized (messages) {
                    for (Value value : pvr.getProperty(0).getValues()) {
                        try {
                            String address = XMLUtils.toOM(value.getAny()).getText();
                            LinkedList mQueue = messages.get(address);
                            if (mQueue == null) {
                                mQueue = new LinkedList();
                                messages.put(address, mQueue);
                            }
                            mQueue.add(message);
                        } catch (Exception ex) {
                            Logger.getLogger(EmailNotifier.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(EmailNotifier.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Collections.emptyList();
    }
}
