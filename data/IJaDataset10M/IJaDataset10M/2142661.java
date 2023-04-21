package mecca.messenger;

import java.util.Enumeration;
import java.util.Vector;
import javax.servlet.http.HttpSession;
import mecca.messenger.object.Message;
import mecca.object.User;
import mecca.portal.velocity.VTemplate;
import mecca.util.DateUtil;
import mecca.util.Logger;
import mecca.util.StringChecker;
import org.apache.velocity.Template;

/**
 * This servlet is the Messenger Module controller.
 * 
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class MessengerModule extends VTemplate {

    private MessengerProcessor processor;

    private String className = "infusion.messenger.MessengerModule";

    private boolean logger = false;

    private Logger log;

    private String targetPage;

    public Template doTemplate() throws Exception {
        if (logger) log = new Logger(className);
        HttpSession session = request.getSession();
        processor = new MessengerProcessor();
        String user = (String) session.getAttribute("_portal_login");
        String action = request.getParameter("form_action");
        if ((action == null) || (action.equals(""))) action = "none";
        if (logger) log.setMessage("action = " + action);
        if (action.equals("none")) {
            DateUtil du = new DateUtil();
            context.put("dateUtil", du);
            String view = request.getParameter("view");
            if (view == null) view = "receive";
            Vector list = new Vector();
            if (view.equals("sent")) {
                list = processor.getSentMessages(user);
            } else {
                list = processor.getReceivedMessages(user);
            }
            context.put("messageList", list);
            context.put("view", view);
            targetPage = "messenger/messenger_home.vm";
        } else if (action.equals("message_compose")) {
            Vector userList = processor.getUsers();
            context.put("sender", user);
            context.put("userList", userList);
            targetPage = "messenger/messenger_compose.vm";
        } else if (action.equals("message_sent")) {
            String receiver = request.getParameter("receiver");
            if ((receiver.equals("All")) || (receiver.equals("all"))) {
                Vector userList = processor.getUsers();
                User userObj = null;
                receiver = "";
                for (Enumeration e = userList.elements(); e.hasMoreElements(); ) {
                    userObj = new User();
                    userObj = (User) e.nextElement();
                    if (!userObj.getLogin().equals(user)) {
                        if (receiver.equals("")) {
                            receiver = userObj.getLogin();
                        } else {
                            receiver = receiver + ", " + userObj.getLogin();
                        }
                    }
                }
            }
            String subject = request.getParameter("subject");
            String content = request.getParameter("content");
            processor.setMessage(user, receiver, subject, content);
            DateUtil du = new DateUtil();
            context.put("dateUtil", du);
            String view = request.getParameter("view");
            if (view == null) view = "receive";
            Vector list = new Vector();
            if (view.equals("sent")) {
                list = processor.getSentMessages(user);
            } else {
                list = processor.getReceivedMessages(user);
            }
            context.put("messageList", list);
            context.put("view", view);
            targetPage = "messenger/messenger_home.vm";
        } else if (action.equals("message_read")) {
            String id = request.getParameter("message_id");
            String view = request.getParameter("view");
            Message msg = processor.getMessage(id, view);
            if (view.equals("receive")) {
                if (!msg.isRead()) processor.setMessageStatus(id, true);
            }
            String content = msg.getContent();
            msg.setContent(StringChecker.putLineBreak(content));
            context.put("message", msg);
            context.put("view", view);
            targetPage = "messenger/messenger_read.vm";
        } else if (action.equals("message_get_reply_form")) {
            Vector userList = processor.getUsers();
            String id = request.getParameter("message_id");
            Message msg = processor.getMessage(id, "receive");
            context.put("sender", user);
            context.put("message", msg);
            targetPage = "messenger/messenger_reply.vm";
        } else if (action.equals("message_delete")) {
            String[] msgIds = request.getParameterValues("msgid");
            String view = request.getParameter("view");
            if (view == null) view = "receive";
            for (int i = 0; i < msgIds.length; i++) {
                processor.deleteMessage(msgIds[i], view);
            }
            DateUtil du = new DateUtil();
            context.put("dateUtil", du);
            Vector list = new Vector();
            if (view.equals("sent")) {
                list = processor.getSentMessages(user);
            } else {
                list = processor.getReceivedMessages(user);
            }
            context.put("messageList", list);
            context.put("view", view);
            targetPage = "messenger/messenger_home.vm";
        } else if (action.equals("single_message_delete")) {
            String msgId = request.getParameter("message_id");
            String view = request.getParameter("view");
            if (view == null) view = "receive";
            processor.deleteMessage(msgId, view);
            DateUtil du = new DateUtil();
            context.put("dateUtil", du);
            Vector list = new Vector();
            if (view.equals("sent")) {
                list = processor.getSentMessages(user);
            } else {
                list = processor.getReceivedMessages(user);
            }
            context.put("messageList", list);
            context.put("view", view);
            targetPage = "messenger/messenger_home.vm";
        }
        Template template = engine.getTemplate(targetPage);
        return template;
    }
}
