package org.itracker.services.util;

import java.text.SimpleDateFormat;
import java.util.Locale;
import org.itracker.core.resources.ITrackerResources;

/**
  * This interface defines the tags used in the export XML.
  */
public interface ImportExportTags {

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static final String EXPORT_LOCALE_STRING = ITrackerResources.BASE_LOCALE;

    public static final Locale EXPORT_LOCALE = ITrackerResources.getLocale(EXPORT_LOCALE_STRING);

    public static final String ATTR_CREATOR_ID = "creator-id";

    public static final String ATTR_BIT = "bit";

    public static final String ATTR_DATE = "date";

    public static final String ATTR_ID = "id";

    public static final String ATTR_NAME = "name";

    public static final String ATTR_ORDER = "order";

    public static final String ATTR_STATUS = "status";

    public static final String ATTR_SYSTEMID = "systemid";

    public static final String ATTR_VALUE = "value";

    public static final String TAG_COMPONENT = "component";

    public static final String TAG_COMPONENTS = "components";

    public static final String TAG_COMPONENT_ID = "component-id";

    public static final String TAG_COMPONENT_DESCRIPTION = "component-description";

    public static final String TAG_COMPONENT_NAME = "component-name";

    public static final String TAG_CONFIGURATION = "configuration";

    public static final String TAG_CONFIGURATION_VERSION = "configuration-version";

    public static final String TAG_CUSTOM_FIELD = "custom-field";

    public static final String TAG_CUSTOM_FIELDS = "custom-fields";

    public static final String TAG_CUSTOM_FIELD_DATEFORMAT = "custom-field-dateformat";

    public static final String TAG_CUSTOM_FIELD_LABEL = "custom-field-label";

    public static final String TAG_CUSTOM_FIELD_OPTION = "custom-field-option";

    public static final String TAG_CUSTOM_FIELD_REQUIRED = "custom-field-required";

    public static final String TAG_CUSTOM_FIELD_SORTOPTIONS = "custom-field-sortoptions";

    public static final String TAG_CUSTOM_FIELD_TYPE = "custom-field-type";

    public static final String TAG_CREATE_DATE = "create-date";

    public static final String TAG_CREATOR = "creator";

    public static final String TAG_EMAIL = "email";

    public static final String TAG_FIRST_NAME = "first-name";

    public static final String TAG_HISTORY_ENTRY = "history-entry";

    public static final String TAG_ISSUE = "issue";

    public static final String TAG_ISSUES = "issues";

    public static final String TAG_ISSUE_ATTACHMENT = "issue-attachment";

    public static final String TAG_ISSUE_ATTACHMENTS = "issue-attachments";

    public static final String TAG_ISSUE_ATTACHMENT_CREATOR = "issue-attachment-creator";

    public static final String TAG_ISSUE_ATTACHMENT_DESCRIPTION = "issue-attachment-description";

    public static final String TAG_ISSUE_ATTACHMENT_FILENAME = "issue-attachment-filename";

    public static final String TAG_ISSUE_ATTACHMENT_ORIGFILE = "issue-attachment-origfile";

    public static final String TAG_ISSUE_ATTACHMENT_SIZE = "issue-attachment-size";

    public static final String TAG_ISSUE_ATTACHMENT_TYPE = "issue-attachment-type";

    public static final String TAG_ISSUE_COMPONENTS = "issue-components";

    public static final String TAG_ISSUE_DESCRIPTION = "issue-description";

    public static final String TAG_ISSUE_FIELD = "issue-field";

    public static final String TAG_ISSUE_FIELDS = "issue-fields";

    public static final String TAG_ISSUE_HISTORY = "issue-history";

    public static final String TAG_ISSUE_PROJECT = "issue-project";

    public static final String TAG_ISSUE_RESOLUTION = "issue-resolution";

    public static final String TAG_ISSUE_SEVERITY = "issue-severity";

    public static final String TAG_ISSUE_STATUS = "issue-status";

    public static final String TAG_ISSUE_VERSIONS = "issue-versions";

    public static final String TAG_LAST_MODIFIED = "last-modified";

    public static final String TAG_LAST_NAME = "last-name";

    public static final String TAG_LOGIN = "login";

    public static final String TAG_OWNER = "owner";

    public static final String TAG_PROJECT = "project";

    public static final String TAG_PROJECTS = "projects";

    public static final String TAG_PROJECT_DESCRIPTION = "project-description";

    public static final String TAG_PROJECT_FIELDS = "project-custom-fields";

    public static final String TAG_PROJECT_FIELD_ID = "project-custom-field";

    public static final String TAG_PROJECT_NAME = "project-name";

    public static final String TAG_PROJECT_OPTIONS = "project-options";

    public static final String TAG_PROJECT_OWNERS = "project-owners";

    public static final String TAG_PROJECT_OWNER_ID = "project-owner";

    public static final String TAG_PROJECT_STATUS = "project-status";

    public static final String TAG_RESOLUTION = "resolution";

    public static final String TAG_RESOLUTIONS = "resolutions";

    public static final String TAG_ROOT = "itracker";

    public static final String TAG_SEVERITIES = "severities";

    public static final String TAG_SEVERITY = "severity";

    public static final String TAG_STATUS = "status";

    public static final String TAG_STATUSES = "statuses";

    public static final String TAG_SUPER_USER = "super-user";

    public static final String TAG_TARGET_VERSION_ID = "target-version-id";

    public static final String TAG_USER = "user";

    public static final String TAG_USERS = "users";

    public static final String TAG_USER_STATUS = "user-status";

    public static final String TAG_VERSION = "version";

    public static final String TAG_VERSIONS = "versions";

    public static final String TAG_VERSION_DESCRIPTION = "version-description";

    public static final String TAG_VERSION_ID = "version-id";

    public static final String TAG_VERSION_NUMBER = "version-number";
}
