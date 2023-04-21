package ru.yep.forum.core;

import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import ru.yep.forum.events.Event;
import ru.yep.forum.utils.Emailer;
import ru.yep.forum.utils.ForumUtil;
import ru.yep.forum.utils.ForumUtil.ConfigKeys;
import ru.yep.forum.utils.ForumUtil.MessageCommands;

/**
 * @author Oleg Orlov
 */
public class EventSystem {

    private static final int SKIPPED_SECONDS = 1200;

    private static final int POSTPONED_SECONDS = 3600;

    private static final Logger logger = Logger.getLogger(EventSystem.class.getName());

    private static final EventSystem instance = new EventSystem();

    private EventSystem() {
    }

    public static EventSystem getDefault() {
        return instance;
    }

    private Scheduler scheduler = new Scheduler();

    private Emailer emailer;

    private Map eventsForUser = new ConcurrentHashMap();

    private Map sentEvents = new ConcurrentHashMap();

    public void init() {
        try {
            Properties props = Engine.getDefault().getForumProperties();
            String smtpHost = props.getProperty(ConfigKeys.MAIL_SMTP_HOST, null);
            int smtpPort = Integer.parseInt(props.getProperty(ConfigKeys.MAIL_SMTP_PORT, "25"));
            String smtpLogin = props.getProperty(ConfigKeys.MAIL_SMTP_LOGIN, null);
            String smtpPasswd = props.getProperty(ConfigKeys.MAIL_SMTP_PASSWD, null);
            if (smtpHost == null || smtpLogin == null || smtpPasswd == null) {
                logger.warning("E-mail notification is not configured");
                emailer = new Emailer("") {

                    public void sendMail(String from, String to, String subject, String text) throws Exception {
                    }
                };
            } else emailer = new Emailer(smtpHost, smtpPort, smtpLogin, smtpPasswd);
        } catch (Exception e) {
            logger.severe("EventSystem isn't initialized well.\n" + e.getMessage());
        }
    }

    /**
     * used by NotifierServlet
     */
    public NotifierEvent getNextEventToSentFor(User user) {
        logger.info("Preparing message to send for user: " + user.getLogin());
        LinkedList eventList = (LinkedList) eventsForUser.get(user);
        if (eventList == null || eventList.isEmpty()) return null;
        Event sourceEvent = (Event) eventList.removeFirst();
        NotifierEvent result = new NotifierEvent(sourceEvent, user);
        sentEvents.put(result.messageId, result);
        return result;
    }

    public void markMessage(String messageId, String updateCommand) {
        NotifierEvent notifierEvent = (NotifierEvent) sentEvents.remove(messageId);
        if (notifierEvent == null) {
            logger.severe("No message found for messageId: " + messageId);
            return;
        }
        if (updateCommand.equals(MessageCommands.POSTPONED)) {
            scheduler.scheduleEvent(notifierEvent.sourceEvent, POSTPONED_SECONDS, notifierEvent.user);
            logger.finest("Message postponed. messageId: " + messageId + ", user: " + notifierEvent.user.getLogin() + ", messagetext: \n" + notifierEvent.getAsPlainText());
        } else if (updateCommand.equals(MessageCommands.SKIPPED)) {
            scheduler.scheduleEvent(notifierEvent.sourceEvent, SKIPPED_SECONDS, notifierEvent.user);
            logger.finest("Message skipped. messageId: " + messageId + ", user: " + notifierEvent.user.getLogin() + ", messagetext: \n" + notifierEvent.getAsPlainText());
        }
    }

    /**
	 * publish event to all users
	 * @deprecated use publishEvent(Event,User[]) instead
	 */
    public void publishEvent(Event event) {
        publishEvent(event, Users.getDefault().getUsers());
    }

    /**
     * publish event to specified users
     * TODO: check concurrent
     */
    public void publishEvent(Event event, User[] users) {
        logger.info("Publish new event: \n" + event);
        for (int i = 0; i < users.length; i++) {
            LinkedList eventsListForUser = (LinkedList) eventsForUser.get(users[i]);
            if (eventsListForUser == null) {
                eventsListForUser = new LinkedList();
                eventsForUser.put(users[i], eventsListForUser);
            }
            eventsListForUser.add(event);
        }
        String forumName = Engine.getDefault().getProperty(ConfigKeys.MAIL_FORUM_NAME);
        for (int i = 0; i < users.length; i++) {
            try {
                emailer.sendMail(forumName, users[i].getEmail(), "Notification", event.getAsPlainText());
            } catch (Exception e) {
                logger.severe(".publishEvent(). E-mail couldn't be sent\n" + ForumUtil.toString(e));
            }
        }
    }

    /**
	 * event wrapper
	 */
    public static class NotifierEvent extends Event {

        Event sourceEvent;

        String messageId;

        User user;

        private static volatile int nextId;

        private static String generateId() {
            int currentNextId = nextId++;
            return String.valueOf(currentNextId);
        }

        NotifierEvent(Event sourceEvent, User user) {
            this.sourceEvent = sourceEvent;
            this.messageId = generateId();
            this.user = user;
        }

        public String getMessageId() {
            return messageId;
        }

        public String getAsHtml() {
            return sourceEvent.getAsHtml();
        }

        public String getAsNotifierHtml(HttpServletRequest request) {
            return sourceEvent.getAsNotifierHtml(request);
        }

        public String getAsPlainText() {
            return sourceEvent.getAsPlainText();
        }
    }

    /**
	 * Just sends any email from forum account 
	 */
    public void sendEmail(String toAddr, String subject, String message) {
        try {
            emailer.sendMail(Engine.getDefault().getProperty(ConfigKeys.MAIL_FORUM_NAME), toAddr, subject, message);
        } catch (Exception e) {
            logger.severe(".sendEmail(): Could't sent an email\n" + ForumUtil.toString(e));
        }
    }

    /**
	 * TODO: sort sheduling by ascending time and shedule by timer 1st of them. after first sheduling - next 
	 */
    public static class Scheduler {

        public void scheduleEvent(Event event, int afterSeconds, User toUser) {
        }
    }
}
