package net.lukemurphey.nsia.eventlog;

import java.util.Vector;
import net.lukemurphey.nsia.Application;
import net.lukemurphey.nsia.eventlog.EventLogMessage.EventType;
import net.lukemurphey.nsia.response.Action;
import net.lukemurphey.nsia.response.ActionFailedException;

/**
 * The site-group event log hook initiates incident response actions within the scope of a site-group (at a site-group level).
 * @author Luke Murphey
 *
 */
public class SiteGroupStatusEventLogHook extends EventLogHook {

    private static final long serialVersionUID = 1L;

    private Action action;

    private String siteGroupID;

    private int siteGroupIDInt;

    private EventLogSeverity minimumSeverity;

    public SiteGroupStatusEventLogHook(Action action, int siteGroupID, EventLogSeverity minimumSeverity) {
        if (action == null) {
            throw new IllegalArgumentException("The action to perform cannot be null");
        }
        if (minimumSeverity == null) {
            throw new IllegalArgumentException("The minimum severity level cannot be null");
        }
        if (siteGroupID < 0) {
            throw new IllegalArgumentException("The identifier of the site group must be greater than zero");
        }
        this.action = action;
        this.minimumSeverity = minimumSeverity;
        this.siteGroupID = Long.toString(siteGroupID);
        this.siteGroupIDInt = siteGroupID;
    }

    /**
	 * Get the site group ID
	 * @return
	 */
    public int getSiteGroupID() {
        return siteGroupIDInt;
    }

    @Override
    public void processEvent(EventLogMessage message) throws EventLogHookException, ActionFailedException {
        if (message == null) {
            return;
        }
        if (message.getSeverity().toInt() < minimumSeverity.toInt()) {
            return;
        }
        if (message.getEventType() == EventType.RULE_COMPLETE_REJECTED || message.getEventType() == EventType.RULE_COMPLETE_FAILED) {
            EventLogField field = message.getField(EventLogField.FieldName.SITE_GROUP_ID);
            if (field != null && field.getValue().equals(siteGroupID)) {
                action.execute(message);
                logActionCompletion(action, field);
            }
        }
    }

    @Override
    public Action getAction() {
        return action;
    }

    /**
	 * Get all of the event hooks for the given site group.
	 * @param app
	 * @param siteGroupID
	 * @return
	 */
    public static SiteGroupStatusEventLogHook[] getSiteGroupEventLogHooks(Application app, long siteGroupID) {
        EventLogHook[] allHooks = app.getEventLog().getHooks();
        Vector<SiteGroupStatusEventLogHook> siteGroupHooks = new Vector<SiteGroupStatusEventLogHook>();
        for (int c = 0; c < allHooks.length; c++) {
            if (allHooks[c] instanceof SiteGroupStatusEventLogHook && ((SiteGroupStatusEventLogHook) allHooks[c]).getSiteGroupID() == siteGroupID) {
                siteGroupHooks.add((SiteGroupStatusEventLogHook) allHooks[c]);
            }
        }
        SiteGroupStatusEventLogHook[] hooksArray = new SiteGroupStatusEventLogHook[siteGroupHooks.size()];
        siteGroupHooks.toArray(hooksArray);
        return hooksArray;
    }
}
