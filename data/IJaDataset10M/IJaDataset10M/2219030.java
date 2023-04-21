package com.ivis.xprocess.core.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AlertMsg {

    private static final String BUNDLE_NAME = "com.ivis.xprocess.core.properties.alerts";

    public static String must_have_name;

    public static String needs_uptodate_diagnostics;

    public static String needs_uptodate_diagnostics_fix1;

    public static String ar_must_have_name;

    public static String fix_distributing_equally;

    public static String fix_favoring_one_project;

    public static String availability_exceeded1;

    public static String availability_exceeded2;

    public static String constraint_with_null_package;

    public static String constraint_with_null_package_fix;

    public static String task_has_cyclic_constraints;

    public static String is_constrained_by;

    public static String remove_constraints;

    public static String person_has_duplicate_roles;

    public static String person_has_duplicate_roles_fix1;

    public static String cyclical_dependency;

    public static String cyclical_dependency_fix1;

    public static String unique_roletype_name_message;

    public static String unique_roletype_name_message_fix1;

    public static String project_has_duplicate_resources;

    public static String project_has_duplicate_resources_fix1;

    public static String should_have_forecast;

    public static String should_have_forecast2;

    public static String schedule_start_before_schedule_end;

    public static String set_schedule_end_after_six_months_fix;

    public static String set_schedule_end_after_one_year_fix;

    public static String project_needs_resources;

    public static String task_name_not_unique;

    public static String task_name_not_unique_fix1;

    public static String role_in_short_supply;

    public static String hours_needed;

    public static String invalid_rule;

    public static String same_rank;

    public static String children_have_been_closed;

    public static String children_have_been_closed_fix1;

    public static String task_specifies;

    public static String assignments_to;

    public static String role_but_only;

    public static String available;

    public static String auto_assigned_max_not_possible;

    public static String estimate_message;

    public static String estimate_message_fix1;

    public static String estimate_message_fix2;

    public static String estimate_message_fix3;

    public static String size_message;

    public static String size_message_fix1;

    public static String size_message_fix2;

    public static String closed_task_open_child;

    public static String closed_task_open_task_fix1;

    public static String constraint_with_never_ending_task;

    public static String unanswered_gateway;

    public static String approve_fix;

    public static String reopen_fix;

    public static String task_has_duplicate_assignments;

    public static String task_has_duplicate_assignments_fix;

    public static String duplicate_required_resource;

    public static String more_time_booked_than_best_case;

    public static String more_time_booked_than_most_likely;

    public static String more_time_booked_than_worst_case;

    public static String more_time_booked_than_estimate;

    public static String more_time_booked_than_estimate_parent_task;

    public static String more_time_booked_than_bestcase_parent_task;

    public static String more_time_booked_than_most_parent_task;

    public static String more_time_booked_than_worst_parent_task;

    public static String task_estimation_is_zero;

    public static String parent_task_estimation_is_zero;

    public static String task_estimation_pre_fix_message;

    public static String task_estimation_post_fix_message;

    public static String task_size_is_zero;

    public static String task_size_fix_message;

    public static String no_required_resource;

    public static String orphaned_child;

    public static String delete_task;

    public static String reparent_task;

    public static String parents_target_should_be_same_or_later;

    public static String fix_change_subtask_target;

    public static String fix_change_parent_target;

    public static String overhead_earliest_end_should_be_before_parents_target_end;

    public static String target_date_message;

    public static String target_date_message_fix1;

    public static String target_date_message_fix2;

    public static String target_date_message_oht;

    public static String target_date_message_oht_fix1;

    public static String target_date_message_oht_fix2;

    static {
        final Logger logger = Logger.getLogger(AlertMsg.class.getName());
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
        for (Field field : AlertMsg.class.getDeclaredFields()) {
            if (!Modifier.isFinal(field.getModifiers())) {
                if (bundle != null) {
                    try {
                        field.set(null, bundle.getString(field.getName()));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (MissingResourceException e) {
                        try {
                            String missingValueMessage = "Missing message for " + field.getName() + " in " + BUNDLE_NAME;
                            logger.log(Level.SEVERE, missingValueMessage);
                            field.set(null, missingValueMessage);
                        } catch (IllegalArgumentException e1) {
                            e1.printStackTrace();
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
