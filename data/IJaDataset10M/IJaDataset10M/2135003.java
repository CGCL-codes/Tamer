package org.lcx.taskvision.ui.wizzard;

import org.eclipse.osgi.util.NLS;

/**
 * @author Laurent Carbonnaux
 */
public class Messages extends NLS {

    private static final String BUNDLE_NAME = "org.lcx.taskvision.ui.wizzard.messages";

    static {
        reloadMessages();
    }

    public static void reloadMessages() {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }

    public static String ScrumVisionQueryWizzard_page_title;

    public static String ScrumVisionQueryWizzard_page_description;

    public static String ScrumVisionQueryWizzard_update_attribute;

    public static String ScrumVisionQueryWizzard_repository_needed;

    public static String ScrumVisionQueryWizzard_specify_repository;

    public static String ScrumVisionQueryWizzard_reset_selection;

    public static String ScrumVisionQueryWizzard_query_title;

    public static String ScrumVisionQueryWizzard_query_type;

    public static String ScrumVisionQueryWizzard_summary;

    public static String ScrumVisionQueryWizzard_description;

    public static String ScrumVisionQueryWizzard_owner;

    public static String ScrumVisionQueryWizzard_priority;

    public static String ScrumVisionQueryWizzard_completed;

    public static String ScrumVisionQueryWizzard_completed_true;

    public static String ScrumVisionQueryWizzard_completed_false;

    public static String ScrumVisionQueryWizzard_completed_all;

    public static String ScrumVisionQueryWizzard_feature;

    public static String ScrumVisionQueryWizzard_userstory;

    public static String ScrumVisionQueryWizzard_sprint;

    public static String ScrumVisionQueryWizzard_updating_repository;

    public static String ScrumVisionQueryWizzard_updating_title_Error;

    public static String ScrumVisionQueryWizzard_updating_message_Error;

    public static String ScrumVisionQueryWizzard_query_type_Error;

    public static String ScrumVisionQueryWizzard_title_Error;

    public static String ScrumVisionRepositorySettingsPage_title;

    public static String ScrumVisionRepositorySettingsPage_finish_the_settings;

    public static String ScrumVisionRepositorySettingsPage_backlog_choosen;

    public static String ScrumVisionRepositorySettingsPage_backlog_not_choosen;

    public static String ScrumVisionRepositorySettingsPage_sprint_not_choosen;

    public static String ScrumVisionRepositorySettingsPage_feature_not_choosen;

    public static String ScrumVisionRepositorySettingsPage_userstory_not_choosen;

    public static String ScrumVisionRepositorySettingsPage_valid_credential;

    public static String ScrumVisionRepositorySettingsPage_create_backlog;

    public static String ScrumVisionRepositorySettingsPage_choose_backlog;

    public static String ScrumVisionRepositorySettingsPage_backlog_name;

    public static String ScrumVisionRepositorySettingsPage_backlog_default_name;

    public static String ScrumVisionRepositorySettingsPage_sprint_duration;

    public static String ScrumVisionRepositorySettingsPage_sprint_duration_help;

    public static String ScrumVisionRepositorySettingsPage_sprint_start_date;

    public static String ScrumVisionRepositorySettingsPage_create_button;

    public static String ScrumVisionRepositorySettingsPage_create_button_tooltip;

    public static String ScrumVisionRepositorySettingsPage_refresh_button;

    public static String ScrumVisionRepositorySettingsPage_refresh_button_tooltip;

    public static String ScrumVisionRepositorySettingsPage_refresh_folder_button;

    public static String ScrumVisionRepositorySettingsPage_refresh_folder_button_tooltip;

    public static String ScrumVisionRepositorySettingsPage_refresh_folder_label;

    public static String ScrumVisionRepositorySettingsPage_backlog_information;

    public static String ScrumVisionRepositorySettingsPage_update_button;

    public static String ScrumVisionRepositorySettingsPage_update_button_tooltip;

    public static String ScrumVisionRepositorySettingsPage_backlog_spreadsheet;

    public static String ScrumVisionRepositorySettingsPage_task_worksheet;

    public static String ScrumVisionRepositorySettingsPage_sprint_worksheet;

    public static String ScrumVisionRepositorySettingsPage_feature_worksheet;

    public static String ScrumVisionRepositorySettingsPage_userstory_worksheet;

    public static String ScrumVisionRepositorySettingsPage_creating_backlog;

    public static String ScrumVisionRepositorySettingsPage_url_invalid_Error;

    public static String ScrumVisionRepositorySettingsPage_Error;

    public static String NewScrumVisionTaskWizard_title;

    public static String NewScrumVisionTaskPage_title;

    public static String NewScrumVisionTaskPage_description;

    public static String NewScrumVisionTaskPage_task_type;

    public static String NewScrumVisionTaskPage_task_type_Error;
}
