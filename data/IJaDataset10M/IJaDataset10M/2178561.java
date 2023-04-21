package net.teqlo.queue;

import java.io.StringReader;
import java.util.Collection;
import javax.mail.internet.InternetAddress;
import net.teqlo.ActivityType;
import net.teqlo.TeqloException;
import net.teqlo.bus.messages.mail.ActionQueueEntry;
import net.teqlo.bus.messages.mail.NewAlias;
import net.teqlo.components.standard.actionQueueV0_01.QueueExecutor;
import net.teqlo.db.ActivityLookup;
import net.teqlo.db.Attributes;
import net.teqlo.db.ComponentLookup;
import net.teqlo.db.ExecutorLookup;
import net.teqlo.db.PrefLookup;
import net.teqlo.db.ServiceLookup;
import net.teqlo.db.User;
import net.teqlo.db.UserAlias;
import net.teqlo.db.XmlDatabase;
import net.teqlo.runtime.XmlRuntime;
import net.teqlo.util.Loggers;
import net.teqlo.util.MailSender;
import net.teqlo.util.References;
import net.teqlo.util.RuntimeFiles;
import net.teqlo.util.SecurityUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * This class deals with accepting queued action items from the Teqlo action queue, locating the target user,
 * creating accounts and sending out invitations as required, and then invoking the receiving services with
 * the action data. It is called from Incoming.consumeMessage().
 */
public class ActionQueue {

    private static final String INVITATION_TEMPLATE = "quickwork_invitation.html";

    private static ActionQueue theInstance = null;

    private ActionQueue() {
    }

    public void handleMessage(ActionQueueEntry action) throws TeqloException {
        User to = XmlDatabase.getInstance().getUserByAlias(action.getToAlias());
        UserAlias alias = null;
        if (to != null) alias = to.getAlias(action.getToAlias());
        if (to == null || alias == null || !alias.isReady()) parkEntry(action); else invokeServices(to, action, to.getUserApps());
    }

    public void handleMessage(NewAlias newAlias) throws TeqloException {
        Loggers.QUEUES.info("Checking stored actions for " + newAlias.getAlias());
        User user = XmlDatabase.getInstance().getUserByAlias(newAlias.getAlias());
        String[] keys = new String[] { "alias", "user", "queue", "logger" };
        Object[] vals = new Object[] { newAlias.getAlias(), user, this, Loggers.XML_DATABASE };
        XmlDatabase.getInstance().executeSystemScript(XmlDatabase.NEW_ALIAS_SCRIPT, keys, vals);
        ActionDatabase.getInstance().pullActions(newAlias.getAlias(), this);
    }

    private static String getTaskSubject(String data) {
        String subject = null;
        try {
            SAXBuilder builder = new SAXBuilder();
            Document listDoc = builder.build(new StringReader(data));
            Element element = listDoc.getRootElement();
            subject = element.getAttributeValue("label");
        } catch (Exception e) {
            Loggers.QUEUES.warn("Problem extracting the subject from a task", e);
        }
        if (subject == null || subject.length() == 0) subject = "New Task";
        return subject;
    }

    private void invokeServices(User user, ActionQueueEntry data, String[] serviceList) {
        boolean invokedAny = false;
        for (String serviceFqn : serviceList) {
            try {
                ServiceLookup sl = user.getServiceLookup(serviceFqn);
                Collection<ActivityLookup> activityLookups = sl.getActivityLookups();
                for (ActivityLookup al : activityLookups) {
                    try {
                        String activityFqn = al.getActivityFqn();
                        ExecutorLookup el = al.getExecutorLookup();
                        ComponentLookup cl = el.getComponentLookup();
                        String uri = cl.getComponentUri();
                        if ("teqlo://ActionQueue/v0.1".equals(uri) && al.getType() == ActivityType.TRANSIENT_PRODUCER) {
                            Attributes attributes = al.getAttributes();
                            String action = attributes.getAttributeValue(QueueExecutor.ACTION_ATTRIBUTE);
                            if (QueueExecutor.RECEIVE_ACTION.equals(action)) {
                                PrefLookup pl = el.getPrefLookup(QueueExecutor.TOPIC_PREF);
                                String prefValue = user.getPrefValue(pl);
                                if (prefValue != null && prefValue.equals(data.getTopic())) try {
                                    XmlRuntime.getInstance().invokeServiceSync(user, serviceFqn, activityFqn, data);
                                    invokedAny = true;
                                } catch (TeqloException e) {
                                    Loggers.QUEUES.error("Failed to invoke queue receive action in application " + sl.getServiceFqn(), e);
                                }
                                Loggers.QUEUES.info("Invoked service " + serviceFqn + " for user " + user.getHandle());
                            }
                        }
                    } catch (TeqloException e) {
                        Loggers.QUEUES.warn("Internal error while checking activities at " + serviceFqn, e);
                    }
                }
            } catch (TeqloException e) {
                Loggers.QUEUES.warn("Internal error while checking services for user " + user.getUserFqn(), e);
            }
        }
        if (!invokedAny) Loggers.QUEUES.warn("Received action for user " + user.getHandle() + " with topic " + data.getTopic() + ", but could not located any matching services to invoke.");
    }

    private void parkEntry(ActionQueueEntry action) throws TeqloException {
        ActionDatabase db = ActionDatabase.getInstance();
        String to = action.getToAlias().toLowerCase();
        boolean first = !db.hasActions(to);
        db.store(to, action);
        Loggers.QUEUES.info("Stored task for " + to);
        XmlDatabase.getInstance().addStat("/", "storeAction", to);
        if (first) {
            String template = RuntimeFiles.getContent("templates", INVITATION_TEMPLATE);
            int index = template.indexOf("\n");
            String subjectTemplate = template.substring(0, index);
            String contentTemplate = template.substring(index + 1);
            String teqloUrl = References.getTeqloURL();
            String taskSubject = getTaskSubject(action.getData());
            String password = SecurityUtil.getVerificationCode(to);
            String subject = String.format(subjectTemplate, teqloUrl, action.getFromAlias(), to, password, taskSubject);
            String content = String.format(contentTemplate, teqloUrl, action.getFromAlias(), to, password, taskSubject);
            try {
                MailSender.sendMessage(new InternetAddress(to), subject, content);
            } catch (Exception e) {
                Loggers.QUEUES.error("Could not send invitation to user " + to, e);
            }
            Loggers.QUEUES.info("Invited " + to);
        }
    }

    public static synchronized ActionQueue getInstance() {
        if (theInstance == null) theInstance = new ActionQueue();
        return theInstance;
    }
}
