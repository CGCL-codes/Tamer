package com.fddtool.pd.common;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import com.fddtool.exception.InvalidStateException;
import com.fddtool.pd.account.Person;
import com.fddtool.pd.bug.Issue;
import com.fddtool.pd.fddproject.Activity;
import com.fddtool.pd.fddproject.Feature;
import com.fddtool.pd.fddproject.Project;
import com.fddtool.pd.fddproject.ProjectAspect;
import com.fddtool.pd.fddproject.ProjectGroup;
import com.fddtool.pd.fddproject.SourceCodeUnit;
import com.fddtool.pd.fddproject.SubjectArea;
import com.fddtool.pd.fddproject.Workpackage;
import com.fddtool.pd.fddproject.WorkpackageContainer;
import com.fddtool.pd.task.Task;
import com.fddtool.resource.MessageKey;
import com.fddtool.resource.MessageProviderImpl;
import com.fddtool.ui.view.tree.entries.AccountSettings;
import com.fddtool.ui.view.tree.entries.AdminEmails;
import com.fddtool.ui.view.tree.entries.AdminProjects;
import com.fddtool.ui.view.tree.entries.ArchivedProjectsSettings;
import com.fddtool.ui.view.tree.entries.LocaleSettings;
import com.fddtool.ui.view.tree.entries.MilestoneSettings;
import com.fddtool.ui.view.tree.entries.NotificationSettings;
import com.fddtool.ui.view.tree.entries.ProjectTypeSettings;
import com.fddtool.ui.view.tree.entries.RootAdmin;
import com.fddtool.ui.view.tree.entries.RootSettings;

/**
 * This enumeration defines the types of entries that may be present in FDDPMA
 * project hierarchy.
 * 
 * @author Serguei Khramtchenko
 */
public class AppEntryType implements Comparable<AppEntryType> {

    /**
     * Identifier of the entry type. Once established, it should not be changed, 
     * as it is often stored in the persistent storage. 
     */
    private String id;

    /**
     * A key to the name of the entry to display in UI - language - dependent.
     */
    private MessageKey nameKey;

    /**
     * A natural sort order for project entries.
     */
    private Integer sortOrder;

    /**
     * The class that implements this project entry type.
     */
    private Class<? extends AppEntry> entryClass;

    /**
     * Stores all the existing entry types.
     */
    private static final List<AppEntryType> ALL_ENTRY_TYPES = new ArrayList<AppEntryType>();

    /**
     * Creates a new instance of this class. This is declared private to avoid
     * external instantiation.
     * 
     * @param name
     *            String that identifies entry type.
     * @param nameKey
     *            MessageKey that is a key to a language-depen3ant name of the
     *            type.
     */
    private AppEntryType(String id, MessageKey nameKey, int sortOrder, Class<? extends AppEntry> entryClass) {
        this.id = id;
        this.nameKey = nameKey;
        this.sortOrder = new Integer(sortOrder);
        this.entryClass = entryClass;
        ALL_ENTRY_TYPES.add(this);
    }

    /**
     * Denotes the project entry type for project groups.
     */
    public static final AppEntryType PROJECT_GROUP = new AppEntryType("ProjectGroup", MessageKey.LBL_PROJECT_GROUP, 1, ProjectGroup.class);

    /**
     * Denotes the project entry type for projects.
     */
    public static final AppEntryType PROJECT = new AppEntryType("Project", MessageKey.LBL_PROJECT, 2, Project.class);

    /**
     * Denotes the project entry type for project aspects.
     */
    public static final AppEntryType PROJECT_ASPECT = new AppEntryType("ProjectAspect", MessageKey.LBL_PROJECT_ASPECT, 3, ProjectAspect.class);

    /**
     * Denotes the project entry type for workpackage container.
     */
    public static final AppEntryType WORKPACKAGE_CONTAINER = new AppEntryType("WorkpackageContainer", MessageKey.LBL_WORKPACKAGES, 4, WorkpackageContainer.class);

    /**
     * Denotes the project entry type for workpackages.
     */
    public static final AppEntryType WORKPACKAGE = new AppEntryType("Workpackage", MessageKey.LBL_WORKPACKAGE, 5, Workpackage.class);

    /**
     * Denotes the project entry type for subject areas.
     */
    public static final AppEntryType SUBJECT_AREA = new AppEntryType("SubjectArea", MessageKey.LBL_SUBJECT_AREA, 6, SubjectArea.class);

    /**
     * Denotes the project entry type for activities.
     */
    public static final AppEntryType ACTIVITY = new AppEntryType("Activity", MessageKey.LBL_ACTIVITY, 7, Activity.class);

    /**
     * Denotes the project entry type for features.
     */
    public static final AppEntryType FEATURE = new AppEntryType("Feature", MessageKey.LBL_FEATURE, 8, Feature.class);

    public static final AppEntryType TASK = new AppEntryType("Task", MessageKey.LBL_TASK, 9, Task.class);

    public static final AppEntryType ISSUE = new AppEntryType("Issue", MessageKey.LBL_ISSUE, 10, Issue.class);

    public static final AppEntryType CODE_UNIT = new AppEntryType("CodeUnit", MessageKey.LBL_CODE_UNIT, 100, SourceCodeUnit.class);

    public static final AppEntryType SETTING_ROOT = new AppEntryType("SettingRoot", MessageKey.LBL_SETTINGS, 200, RootSettings.class);

    public static final AppEntryType SETTING_ACCOUNT = new AppEntryType("SettingAccount", MessageKey.LBL_ACCOUNT, 201, AccountSettings.class);

    public static final AppEntryType SETTING_PROJECT_TYPES = new AppEntryType("SettingProjectType", MessageKey.LBL_PROJECT_TYPES, 202, ProjectTypeSettings.class);

    public static final AppEntryType SETTING_MILESTONES = new AppEntryType("SettingMilestone", MessageKey.LBL_MILESTONES, 203, MilestoneSettings.class);

    public static final AppEntryType SETTING_NOTIFICATIONS = new AppEntryType("SettingNotification", MessageKey.LBL_NOTIFICATIONS, 204, NotificationSettings.class);

    public static final AppEntryType SETTING_ARCHIVED_PROJECTS = new AppEntryType("SettingArchivedProjects", MessageKey.LBL_ARCHIVED_PROJECTS, 205, ArchivedProjectsSettings.class);

    public static final AppEntryType SETTING_LOCALE = new AppEntryType("SettingLocale", MessageKey.LBL_LOCALE, 206, LocaleSettings.class);

    public static final AppEntryType USER = new AppEntryType("User", MessageKey.LBL_ACCOUNT, 300, Person.class);

    public static final AppEntryType ADMIN_ROOT = new AppEntryType("AdminRoot", MessageKey.LBL_ADMINISTRATION, 400, RootAdmin.class);

    public static final AppEntryType ADMIN_PROJECTS = new AppEntryType("AdminProjects", MessageKey.LBL_PROJECTS, 401, AdminProjects.class);

    public static final AppEntryType ADMIN_EMAILS = new AppEntryType("AdminEmails", MessageKey.LBL_EMAILS, 401, AdminEmails.class);

    /**
     * Finds project entry type by its unique identifier.
     * 
     * @param id
     *            String identifier of project entry type to find.
     * 
     * @return AppEntryType that was found, or <code>null</code> if nothing is
     *         found.
     */
    public static final AppEntryType findById(String id) {
        for (Iterator<AppEntryType> iter = ALL_ENTRY_TYPES.iterator(); iter.hasNext(); ) {
            AppEntryType type = iter.next();
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return null;
    }

    /**
     * Returns unique identifier of this project entry type.
     * 
     * @return String containing id.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns of this project entry type. This name is language dependant.
     * 
     * @return String containing project entry type name, as it may be displayed
     *         in UI.
     */
    public String getName() {
        return MessageProviderImpl.getProvider().getMessage(nameKey);
    }

    /**
     * Lists all known project entry types.
     * 
     * @return List of <code>AppEntryType</code> objects.
     */
    public static List<AppEntryType> listAll() {
        return Collections.unmodifiableList(ALL_ENTRY_TYPES);
    }

    /**
     * {@inheritDoc}
     */
    public int compareTo(AppEntryType o) {
        if (o == this) {
            return 0;
        } else if (o instanceof AppEntryType) {
            AppEntryType other = (AppEntryType) o;
            return this.sortOrder.compareTo(other.sortOrder);
        } else {
            return -2;
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof AppEntryType) {
            AppEntryType other = (AppEntryType) o;
            return this.getId().equals(other.getId());
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return getId().hashCode();
    }

    /**
     * Finds the project entry by id. This method takes into account the type of
     * the entry.
     * 
     * @param id
     *            String with identifier of an entry to find.
     * 
     * @return AppEntry that was found, or <code>null</code> if nothing is
     *         found.
     * @throws InvalidStateException
     *             if the destination entry cannot be created via reflection.
     */
    public AppEntry findObjectById(String id) {
        try {
            Method m = entryClass.getMethod("findById", new Class[] { String.class });
            Object result = null;
            result = (AppEntry) m.invoke(null, new Object[] { id });
            return (AppEntry) result;
        } catch (Exception ex) {
            ex.printStackTrace();
            String[] params = new String[] { entryClass.getName(), id };
            throw new InvalidStateException(MessageKey.ERROR_CANNOT_FIND_OBJECT, params);
        }
    }
}
