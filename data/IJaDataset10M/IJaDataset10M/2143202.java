package octopus.requests;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;
import hambo.svc.database.*;
import hambo.util.StringUtil;
import octopus.tools.Messages.OctopusErrorMessages;

/**
 * Class used to handles Application Requests
 * 
 */
public class ApplicationRequestFactory {

    /**
     * Method used to Add a New Application
     * 
     * @param _extension    Prefix of the new Application
     * @param _label        Label of the new Application
     * @param _author       User name who create the New Application
     * @return              An OctopusErrorMessages Message 
     */
    public static String addApplication(String _extension, String _label, String _author) {
        if (_extension == null || _extension.trim().equals("")) {
            return OctopusErrorMessages.MAIN_PARAMETER_EMPTY;
        }
        if (!StringUtil.isAlphaNumerical(_extension)) {
            return OctopusErrorMessages.EXTENSION_MUST_BE_ALPHANUMERIC;
        }
        if (_label == null || _label.trim().equals("")) {
            return OctopusErrorMessages.MAIN_PARAMETER_EMPTY;
        }
        if (_author == null || _author.trim().equals("")) {
            return OctopusErrorMessages.MAIN_PARAMETER_EMPTY;
        }
        String test_extension = applicationExist("extension", _extension);
        if (!test_extension.equals(OctopusErrorMessages.DOESNT_ALREADY_EXIST)) {
            return OctopusErrorMessages.APPLICATION_EXTENSION_TAKEN;
        }
        String test_label = applicationExist("label", _label);
        if (!test_label.equals(OctopusErrorMessages.DOESNT_ALREADY_EXIST)) {
            return OctopusErrorMessages.APPLICATION_LABEL_TAKEN;
        }
        String so = OctopusErrorMessages.UNKNOWN_ERROR;
        DBConnection theConnection = null;
        try {
            theConnection = DBServiceManager.allocateConnection();
            String query = "INSERT INTO tr_application (tr_application_id,tr_application_label,tr_application_lun,tr_application_lud) VALUES (?,?,?,getdate())";
            PreparedStatement state = theConnection.prepareStatement(query);
            state.setString(1, _extension);
            state.setString(2, _label);
            state.setString(3, _author);
            state.executeUpdate();
            so = OctopusErrorMessages.ACTION_DONE;
        } catch (SQLException e) {
            so = OctopusErrorMessages.ERROR_DATABASE;
        } finally {
            if (theConnection != null) theConnection.release();
        }
        return so;
    }

    /**
     * Method used to check if an Application already exists with a specific label or a specific prefix
     * 
     * @param type       label or extension according to what you want to test
     * @param appl       Parameter of the new Application to check
     * @return           An OctopusErrorMessages Message 
     */
    public static String applicationExist(String type, String appl) {
        String so = OctopusErrorMessages.UNKNOWN_ERROR;
        DBConnection theConnection = null;
        try {
            theConnection = DBServiceManager.allocateConnection();
            String query = "";
            PreparedStatement state = null;
            if (type.equals("label")) {
                query += "SELECT tr_application_id FROM tr_application WHERE tr_application_label=?";
                state = theConnection.prepareStatement(query);
                state.setString(1, appl);
            } else if (type.equals("extension")) {
                query += "SELECT tr_application_id FROM tr_application WHERE tr_application_id=?";
                state = theConnection.prepareStatement(query);
                state.setString(1, appl);
            } else {
                return OctopusErrorMessages.UNKNOWN_ERROR;
            }
            if (state == null) {
                return OctopusErrorMessages.UNKNOWN_ERROR;
            }
            ResultSet rs = state.executeQuery();
            if (rs.next()) {
                so = OctopusErrorMessages.ALREADY_EXIST;
            } else {
                so = OctopusErrorMessages.DOESNT_ALREADY_EXIST;
            }
        } catch (SQLException e) {
            so = OctopusErrorMessages.ERROR_DATABASE;
        } finally {
            if (theConnection != null) theConnection.release();
        }
        return so;
    }

    /**
     * Method used to Delete an Application<BR>
     * Remove all the Tags, all the Translation and the Application linked to the specific Application Code     
     * 
     * @param applicationExtension    Prefix of the Application to delete
     * @return              An OctopusErrorMessages Message 
     */
    public static void deleteApplication(String applicationExtension) {
        DBConnection theConnection = null;
        try {
            theConnection = DBServiceManager.allocateConnection();
            theConnection.setAutoCommit(false);
            String query = "DELETE FROM tr_translation WHERE tr_translation_trtagid IN (SELECT tr_tag_id FROM tr_tag WHERE tr_tag_applicationid=?)";
            PreparedStatement state = theConnection.prepareStatement(query);
            state.setString(1, applicationExtension);
            state.executeUpdate();
            String query2 = "DELETE FROM tr_tag WHERE tr_tag_applicationid=?";
            PreparedStatement state2 = theConnection.prepareStatement(query2);
            state2.setString(1, applicationExtension);
            state2.executeUpdate();
            String query3 = "DELETE FROM tr_application WHERE tr_application_id=?";
            PreparedStatement state3 = theConnection.prepareStatement(query3);
            state3.setString(1, applicationExtension);
            state3.executeUpdate();
            theConnection.commit();
        } catch (SQLException e) {
            try {
                theConnection.rollback();
            } catch (SQLException ex) {
            }
        } finally {
            if (theConnection != null) {
                try {
                    theConnection.setAutoCommit(true);
                } catch (SQLException ex) {
                }
                theConnection.release();
            }
        }
    }
}
