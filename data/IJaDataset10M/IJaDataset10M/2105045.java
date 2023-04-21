package evolaris.platform.smssvc.web.action;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.hibernate.Session;
import evolaris.framework.async.business.InteractionLogManager;
import evolaris.framework.async.datamodel.InteractionLog;
import evolaris.framework.smsservices.business.SmsServiceDbManager;
import evolaris.framework.smsservices.business.commands.EventVariables;
import evolaris.framework.smsservices.business.commands.MacroCommand;
import evolaris.framework.smsservices.business.commands.Result;
import evolaris.framework.smsservices.business.commands.TimerEventParameters;
import evolaris.framework.smsservices.datamodel.CommandEntry;
import evolaris.framework.smsservices.datamodel.SmsCommandEntry;
import evolaris.framework.smsservices.datamodel.TimerEvent;
import evolaris.framework.sys.business.exception.BugException;
import evolaris.framework.sys.business.exception.InputException;
import evolaris.framework.sys.web.action.LocalizedAction;
import evolaris.framework.um.business.UserManager;
import evolaris.framework.um.datamodel.User;
import evolaris.platform.smssvc.web.form.TimerEventExecutionForm;

/**
 * This action processes a manually triggered timer event.
 * 
 * @author thomas.ebner
 * @date 29.08.2007
 */
public class TimerEventExecutionAction extends LocalizedAction {

    private static final Logger LOGGER = Logger.getLogger(TimerEventExecutionAction.class);

    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        return null;
    }

    @Override
    protected ActionForward defaultMethod(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) {
        TimerEventExecutionForm teForm = (TimerEventExecutionForm) form;
        SmsServiceDbManager smsServiceDbManager = new SmsServiceDbManager(locale, session);
        TimerEvent timerEvent = smsServiceDbManager.getTimerEvent(teForm.getId());
        if (timerEvent == null) {
            throw new InputException(getResources(req).getMessage(locale, "admin.entryNotAvailable"));
        }
        MessageResources resources = getResources(req);
        TimerEventExecutionAction.processEvent(req, timerEvent, locale, session, resources);
        req.setAttribute("serviceType", InteractionLogManager.EVENT_TIMER_SIMULATED);
        return mapping.findForward("executed");
    }

    protected static final void processEvent(HttpServletRequest req, TimerEvent timerEvent, Locale locale, Session session, MessageResources resources) {
        LOGGER.info("processing timer event #" + timerEvent.getId() + "; scheduled for " + timerEvent.getScheduledTime() + " (group: " + timerEvent.getGroup().getGroupname() + ")");
        Set<User> requestingUsers;
        if (timerEvent.getRequestingUser() != null) {
            requestingUsers = new HashSet<User>(1);
            requestingUsers.add(timerEvent.getRequestingUser());
        } else if (timerEvent.getRequestingUserSet() != null) {
            requestingUsers = timerEvent.getRequestingUserSet().getUsers();
            LOGGER.info("timer event configured for user set `" + timerEvent.getRequestingUserSet().getName() + "` with " + requestingUsers.size() + " users");
        } else {
            LOGGER.error("configured event #" + timerEvent.getId() + " without requesting user/user set/group => ignoring");
            throw new BugException("There is no requesting user for the timer event " + timerEvent.getId());
        }
        for (User requestingUser : requestingUsers) {
            if (timerEvent == null) {
                break;
            }
            Set<CommandEntry> commandEntries = timerEvent.getCommandEntries();
            if (commandEntries.size() == 0) {
                LOGGER.warn("empty command list for timer event #" + timerEvent.getId() + " => ignoring the event for all requesting users");
                break;
            }
            if (timerEvent.getRequestingUserSet() != null) {
                for (CommandEntry entry : commandEntries) {
                    if (entry instanceof SmsCommandEntry) {
                        SmsCommandEntry smsCommandEntry = (SmsCommandEntry) entry;
                        if (smsCommandEntry.getDestinationUserSet() != null || smsCommandEntry.getDestinationGroup() != null) {
                            ResourceBundle messages = ResourceBundle.getBundle("SmsServices", locale);
                            throw new InputException(messages.getString("smssvc.SendingToMoreThanOneUserNotAllowedInTimerEventAppliedToMoreThanOneRequestingUser"));
                        }
                    }
                }
            }
            UserManager userManager = new UserManager(locale, session);
            long requestingUserId = requestingUser.getId();
            requestingUser = userManager.getUserDetails(requestingUserId);
            if (requestingUser == null) {
                LOGGER.warn("requesting user #" + requestingUserId + " deleted => ignoring");
                continue;
            }
            InteractionLogManager interactionLogManager = new InteractionLogManager(locale, session);
            TimerEventParameters timerEventParameters = new TimerEventParameters(Locale.GERMAN, session, requestingUser, timerEvent);
            InteractionLog log = new InteractionLog();
            timerEventParameters.updateInteractionLog(log);
            log.setOperation(InteractionLogManager.EVENT_TIMER_SIMULATED);
            interactionLogManager.writeInteractionLog(log);
            interactionLogManager.writeInteractionLog(log);
            EventVariables eventVariables = new EventVariables(timerEventParameters);
            MacroCommand macroCommand = new MacroCommand(timerEventParameters, eventVariables, commandEntries);
            Result result = macroCommand.execute();
            if (result.getValue() != null) {
                req.setAttribute("result", result.getValue());
            }
            LOGGER.debug("command interpretation result: " + result.getValue());
        }
        if (requestingUsers.size() > 1) {
            String message = resources.getMessage(locale, "smssvc.ForNUsersExecuted", requestingUsers.size() + "");
            req.setAttribute("result", message);
        }
    }

    @Override
    protected Map getKeyMethodMap() {
        return null;
    }
}
