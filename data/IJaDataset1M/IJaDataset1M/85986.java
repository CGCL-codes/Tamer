package ru.aslanov.schedule.server.services;

import ru.aslanov.schedule.model.DataItem;
import ru.aslanov.schedule.model.Group;
import ru.aslanov.schedule.model.Operation;
import ru.aslanov.schedule.server.AccessDeniedException;
import ru.aslanov.schedule.server.AccessManager;
import ru.aslanov.schedule.server.gcalendar.GCalendarSyncService;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.PersistenceAware;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * Created: Feb 2, 2010 10:33:28 AM
 *
 * @author Sergey Aslanov
 */
@PersistenceAware
public class GroupService extends ScheduleChildService<Group> {

    public GroupService() {
        super(null);
    }

    @Override
    protected Class<Group> getEntityClass() {
        return Group.class;
    }

    @Override
    protected void makeObjectPersistent(PersistenceManager pm, Group object, HttpServletRequest request) {
        getParent(pm, request).getGroups().add(object);
    }

    @Override
    protected void setParameter(Group object, String paramName, String paramValue, PersistenceManager pm) throws Exception {
        if (!"days".equals(paramName)) {
            super.setParameter(object, paramName, paramValue, pm);
        }
    }

    @Override
    protected void fillDataItem(DataItem dataItem, Group object, HttpServletRequest request, PersistenceManager pm) throws Exception {
        super.fillDataItem(dataItem, object, request, pm);
        setAttr(dataItem, "schedule", object.getSchedule());
        setAttr(dataItem, "teacher1Key", object.getTeacher1Key());
        setAttr(dataItem, "teacher2Key", object.getTeacher2Key());
        setAttr(dataItem, "locationKey", object.getLocationKey());
        setAttr(dataItem, "danceKey", object.getDanceKey());
        setAttr(dataItem, "levelKey", object.getLevelKey());
        setAttr(dataItem, "comment", object.getComment());
        setAttr(dataItem, "welcome", object.getWelcome());
        setAttr(dataItem, "started", object.getStarted());
        setAttr(dataItem, "until", object.getUntil());
        setAttr(dataItem, "title", object.getTitle());
        setAttr(dataItem, "open", object.isOpen());
        setAttr(dataItem, "privates", object.isPrivates());
        setAttr(dataItem, "hidden", object.isHidden());
        setAttr(dataItem, "days", object.getDays());
        setAttr(dataItem, "calendarKey", object.getCalendarKey());
    }

    @Override
    protected boolean checkPermission(Group existingEntity, HttpServletRequest request, PersistenceManager pm, Operation operation) throws AccessDeniedException {
        final AccessManager am = AccessManager.getInstance();
        if (existingEntity != null) {
            if (!am.hasAccessToGroup(existingEntity.getEncodedKey(), operation)) {
                return false;
            }
        }
        if (operation == Operation.CREATE || operation == Operation.UPDATE) {
            final String scheduleKey = getParentKey(request);
            if (am.isScheduleAdmin(scheduleKey)) {
                return true;
            } else {
                String curTeacher = am.getScheduleTeacherKey(scheduleKey);
                if (curTeacher == null) {
                    return false;
                }
                String t1 = request.getParameter("teacher1Key");
                String t2 = request.getParameter("teacher2Key");
                if (t1 == null && existingEntity != null) t1 = existingEntity.getTeacher1Key();
                if (t2 == null && existingEntity != null) t2 = existingEntity.getTeacher2Key();
                return curTeacher.equals(t1) || curTeacher.equals(t2);
            }
        }
        return true;
    }

    @Override
    protected void onChange(PersistenceManager pm, Group group, Operation operation, final HttpServletRequest request) {
        final GCalendarSyncService syncService = new GCalendarSyncService();
        if (operation == Operation.UPDATE) {
            String newCalendarKey = request.getParameter("calendarKey");
            if ("null".equals(newCalendarKey)) newCalendarKey = "";
            Boolean hide = Boolean.valueOf(request.getParameter("hidden"));
            if (newCalendarKey != null) {
                final String existingCalendarKey = group.getCalendarKey();
                if ((existingCalendarKey != null && !newCalendarKey.equals(existingCalendarKey)) || hide) {
                    syncService.removeGroupSchedules(pm, group);
                }
                if (!newCalendarKey.equals("") && !hide) {
                    syncService.updateGroupSchedules(pm, group, newCalendarKey, true);
                }
            } else {
                if (!hide) {
                    syncService.updateGroupSchedules(pm, group, null, true);
                } else {
                    syncService.removeGroupSchedules(pm, group);
                }
            }
        } else if (operation == Operation.REMOVE) {
            syncService.removeGroupSchedules(pm, group);
        } else if (operation == Operation.CREATE) {
        }
        executeAfterCommit(new Runnable() {

            @Override
            public void run() {
                syncService.sendEvents(getParentKey(request));
            }
        });
    }
}
