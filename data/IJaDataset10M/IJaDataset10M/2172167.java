package com.sitescape.team.task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.portlet.PortletSession;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import com.sitescape.team.ObjectKeys;
import com.sitescape.team.context.request.RequestContextHolder;
import com.sitescape.team.domain.CustomAttribute;
import com.sitescape.team.domain.Definition;
import com.sitescape.team.domain.Entry;
import com.sitescape.team.domain.FolderEntry;
import com.sitescape.team.module.definition.DefinitionUtils;
import com.sitescape.team.search.filter.SearchFilter;
import com.sitescape.team.web.WebKeys;

public class TaskHelper {

    public static final String PRIORITY_TASK_ENTRY_ATTRIBUTE_NAME = "priority";

    public static final String STATUS_TASK_ENTRY_ATTRIBUTE_NAME = "status";

    public static final String COMPLETED_TASK_ENTRY_ATTRIBUTE_NAME = "completed";

    public static final String ASSIGNMENT_TASK_ENTRY_ATTRIBUTE_NAME = "assignment";

    public static final String ASSIGNMENT_GROUPS_TASK_ENTRY_ATTRIBUTE_NAME = "assignment_groups";

    public static final String ASSIGNMENT_TEAMS_TASK_ENTRY_ATTRIBUTE_NAME = "assignment_teams";

    public static final String TIME_PERIOD_TASK_ENTRY_ATTRIBUTE_NAME = "start_end";

    public enum FilterType {

        CLOSED, DAY, WEEK, MONTH, ACTIVE, ALL
    }

    public static final FilterType FILTER_TYPE_DEFAULT = FilterType.ALL;

    public static String getTaskCompletedValue(Entry entry) {
        CustomAttribute customAttribute = entry.getCustomAttribute(TaskHelper.COMPLETED_TASK_ENTRY_ATTRIBUTE_NAME);
        if (customAttribute == null) {
            return null;
        }
        Set value = (Set) customAttribute.getValueSet();
        if (value == null) {
            return null;
        }
        Iterator it = value.iterator();
        if (it.hasNext()) {
            return (String) it.next();
        }
        return null;
    }

    public static String getTaskStatusValue(Entry entry) {
        CustomAttribute customAttribute = entry.getCustomAttribute(TaskHelper.STATUS_TASK_ENTRY_ATTRIBUTE_NAME);
        if (customAttribute == null) {
            return null;
        }
        Set value = (Set) customAttribute.getValueSet();
        if (value == null) {
            return null;
        }
        Iterator it = value.iterator();
        if (it.hasNext()) {
            return (String) it.next();
        }
        return null;
    }

    private static String getTaskSingleValue(Map formData, String key) {
        Object values = formData.get(key);
        if (values == null) return null; else if (values instanceof String) return (String) values; else if (values instanceof String[]) return ((String[]) values)[0];
        return null;
    }

    public static String getTaskStatusValue(Map formData) {
        return getTaskSingleValue(formData, TaskHelper.STATUS_TASK_ENTRY_ATTRIBUTE_NAME);
    }

    public static String getTaskPriorityValue(Map formData) {
        return getTaskSingleValue(formData, TaskHelper.PRIORITY_TASK_ENTRY_ATTRIBUTE_NAME);
    }

    public static String getTaskCompletedValue(Map formData) {
        return getTaskSingleValue(formData, TaskHelper.COMPLETED_TASK_ENTRY_ATTRIBUTE_NAME);
    }

    public static FilterType getTaskFilterType(PortletSession portletSession) {
        return (FilterType) portletSession.getAttribute(WebKeys.TASK_CURRENT_FILTER_TYPE);
    }

    /**
	 * Saves given type in session or default type if given is <code>null</code> or unknown.
	 * 
	 * @param portletSession
	 * @param filterType
	 * @return
	 */
    public static FilterType setTaskFilterType(PortletSession portletSession, FilterType filterType) {
        if (filterType == null) {
            filterType = getTaskFilterType(portletSession);
            if (filterType == null) {
                filterType = FILTER_TYPE_DEFAULT;
            }
        }
        portletSession.setAttribute(WebKeys.TASK_CURRENT_FILTER_TYPE, filterType);
        return filterType;
    }

    public static SearchFilter buildSearchFilter(FilterType filterType) {
        SearchFilter searchFilter = new SearchFilter(true);
        DateTimeZone userTimeZone = DateTimeZone.forTimeZone(RequestContextHolder.getRequestContext().getUser().getTimeZone());
        if (filterType.equals(FilterType.CLOSED)) {
            searchFilter.addTaskStatuses(new String[] { "s3", "s4" });
        } else if (filterType.equals(FilterType.ACTIVE)) {
            searchFilter.addTaskStatuses(new String[] { "s1", "s2" });
        } else if (filterType.equals(FilterType.DAY)) {
            DateTime dateTime = (new DateTime(userTimeZone)).plusDays(1).toDateMidnight().toDateTime().withZone(DateTimeZone.forID("GMT"));
            searchFilter.addTaskEndDate(dateTime.toString("yyyy-MM-dd HH:mm"));
            searchFilter.addTaskStatuses(new String[] { "s1", "s2" });
        } else if (filterType.equals(FilterType.WEEK)) {
            DateTime dateTime = new DateTime(userTimeZone);
            dateTime = dateTime.plusWeeks(1).toDateMidnight().toDateTime().withZone(DateTimeZone.forID("GMT"));
            searchFilter.addTaskEndDate(dateTime.toString("yyyy-MM-dd HH:mm"));
            searchFilter.addTaskStatuses(new String[] { "s1", "s2" });
        } else if (filterType.equals(FilterType.MONTH)) {
            DateTime dateTime = new DateTime(userTimeZone);
            dateTime = dateTime.plusMonths(1).toDateMidnight().toDateTime().withZone(DateTimeZone.forID("GMT"));
            searchFilter.addTaskEndDate(dateTime.toString("yyyy-MM-dd HH:mm"));
            searchFilter.addTaskStatuses(new String[] { "s1", "s2" });
        }
        return searchFilter;
    }

    public static Map adjustTaskAttributesDependencies(FolderEntry entry, Map formData) {
        Map result = new HashMap(formData);
        if (!TaskHelper.isTaskEntryType(entry)) {
            return formData;
        }
        String newPriority = TaskHelper.getTaskPriorityValue(formData);
        String newStatus = TaskHelper.getTaskStatusValue(formData);
        String newCompleted = TaskHelper.getTaskCompletedValue(formData);
        TaskHelper.adjustTaskAttributesDependencies(entry, result, newPriority, newStatus, newCompleted);
        return result;
    }

    private static boolean isTaskEntryType(FolderEntry entry) {
        Definition entryDef = entry.getEntryDef();
        String family = DefinitionUtils.getFamily(entryDef.getDefinition());
        return isTaskEntryType(family);
    }

    private static boolean isTaskEntryType(String family) {
        return ObjectKeys.FAMILY_TASK.equals(family);
    }

    public static void adjustTaskAttributesDependencies(FolderEntry entry, Map formData, String newPriority, String newStatus, String newCompleted) {
        Definition entryDef = entry.getEntryDef();
        String family = DefinitionUtils.getFamily(entryDef.getDefinition());
        TaskHelper.adjustTaskAttributesDependencies(entry, family, formData, newPriority, newStatus, newCompleted);
    }

    public static void adjustTaskAttributesDependencies(FolderEntry entry, String typeOfEntry, Map formData, String newPriority, String newStatus, String newCompleted) {
        if ((typeOfEntry != null && !TaskHelper.isTaskEntryType(typeOfEntry)) || (entry != null && !TaskHelper.isTaskEntryType(entry))) {
            return;
        }
        if (!newPriority.equals("")) {
            formData.put(TaskHelper.PRIORITY_TASK_ENTRY_ATTRIBUTE_NAME, new String[] { newPriority });
        }
        if (!newStatus.equals("")) {
            formData.put(TaskHelper.STATUS_TASK_ENTRY_ATTRIBUTE_NAME, new String[] { newStatus });
            if (newStatus.equals("s3")) {
                formData.put(TaskHelper.COMPLETED_TASK_ENTRY_ATTRIBUTE_NAME, new String[] { "c100" });
            }
            if (entry != null) {
                String statusCurrent = TaskHelper.getTaskStatusValue(entry);
                String completedCurrent = TaskHelper.getTaskCompletedValue(entry);
                if ((newStatus.equals("s1") || newStatus.equals("s2")) && "s3".equals(statusCurrent) && "c100".equals(completedCurrent)) {
                    formData.put(TaskHelper.COMPLETED_TASK_ENTRY_ATTRIBUTE_NAME, new String[] { "c090" });
                }
            }
        }
        if (!newCompleted.equals("")) {
            formData.put(TaskHelper.COMPLETED_TASK_ENTRY_ATTRIBUTE_NAME, new String[] { newCompleted });
            if (newCompleted.equals("c000")) {
                formData.put(TaskHelper.STATUS_TASK_ENTRY_ATTRIBUTE_NAME, new String[] { "s1" });
            }
            if (newCompleted.equals("c010") || newCompleted.equals("c020") || newCompleted.equals("c030") || newCompleted.equals("c040") || newCompleted.equals("c050") || newCompleted.equals("c060") || newCompleted.equals("c070") || newCompleted.equals("c080") || newCompleted.equals("c090")) {
                formData.put(TaskHelper.STATUS_TASK_ENTRY_ATTRIBUTE_NAME, new String[] { "s2" });
            }
            if (newCompleted.equals("c100")) {
                formData.put(TaskHelper.STATUS_TASK_ENTRY_ATTRIBUTE_NAME, new String[] { "s3" });
            }
        }
    }
}
