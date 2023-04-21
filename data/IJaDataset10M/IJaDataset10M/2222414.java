package com.myopa.ui.core;

import java.awt.Component;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import com.myopa.util.LoggerUtil;

/**
 * This class holds all error handling dialog boxes displayed to the user.
 * This class should also log these errors to help in debugging.
 *
 * @author Paul Campbell <myopa@users.sourceforge.net>
 * @author Clint Burns <c1burns@users.sourceforge.net>
 */
public class ErrorHandler {

    private static Logger logger = LoggerUtil.getInstance().getLogger();

    public static void unexpectedSQLError(String error, Component comp) {
        logger.error(error);
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("An_unknown_SQL_error_has_occured_-_please_try_again_later."), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("SQL_Error"), JOptionPane.ERROR_MESSAGE);
    }

    public static void loginError(String error, Component comp) {
        logger.debug(error);
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("You_have_entered_an_incorrect_username_or_password.") + java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("_Please_try_again."), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Login_Error"), JOptionPane.ERROR_MESSAGE);
    }

    public static void blankUsernamePassword(Component comp) {
        logger.debug("Blank username or password");
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("BlankUsernamePassword"), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Login_Error"), JOptionPane.ERROR_MESSAGE);
    }

    public static void jdbcError(String error, Component comp) {
        logger.error(error);
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("The_postgresql_driver_could_not_be_found._Application_is") + java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("_unable_to_continue"), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Fatal_Error"), JOptionPane.ERROR_MESSAGE);
    }

    public static void notLoggedInError(Component comp) {
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("You_must_be_logged_in_to_access_the_options_screen."), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Authentication_Required"), JOptionPane.ERROR_MESSAGE);
    }

    public static void iniFileError(String error, Component comp) {
        logger.error(error);
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Unable_to_read_configuration_file._Please_visit_the_") + java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("website_for_instructions_on_how_to_fix_this."), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Configuration_Error"), JOptionPane.ERROR_MESSAGE);
    }

    public static void feedbackError(String url, Component comp) {
        logger.warn(java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Unable_to_launch_web_browser_on_your_system._") + url);
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Unable_to_launch_web_browser_on_your_system.__To_") + java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("provide_feedback_please_go_to_") + url, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Browser_Error"), JOptionPane.ERROR_MESSAGE);
    }

    public static void recordNotFoundError(String error, Component comp) {
        logger.debug(error);
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Unable_to_find_record_in_database._Perhaps_") + java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("it_has_been_deleted."), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Record_Not_Found"), JOptionPane.ERROR_MESSAGE);
    }

    public static void propertyNotFoundError(String error, Component comp) {
        logger.error(error);
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Unable_to_find_associated_property."), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Property_Not_Found"), JOptionPane.ERROR_MESSAGE);
    }

    public static void invalidDate(String error, Component comp) {
        logger.debug(error);
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Invalid_date_entered._Please_correct_and_retry."), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("Invalid_Date"), JOptionPane.ERROR_MESSAGE);
    }

    public static void updaterNotFound(String error, Component comp) {
        logger.debug(error);
        JOptionPane.showMessageDialog(comp, java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("UpdaterFailedText"), java.util.ResourceBundle.getBundle("com/myopa/nl/ApplicationResources").getString("UpdaterFailedHeading"), JOptionPane.ERROR_MESSAGE);
    }
}
