package it.webscience.kpeople.dal.activity.dao;

import it.webscience.kpeople.be.ActivityType;
import it.webscience.kpeople.dal.Singleton;
import it.webscience.kpeople.dal.activity.ActivityTypeFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe per l'accesso ai dati delle ActivityType
 * @author gnoni
 *
 */
public class ActivityTypeDAOUtil {

    /**
	 * Metodo che effettua la lettura delle tiplogie di attività
	 * @param pIdActivityType id tipologia attività.
	 * @return ActivityType
	 * @throws SQLException eccezione
	 */
    public final ActivityType loadActivityTypeByIdActivityType(final ActivityType pActivityType) throws SQLException {
        ActivityType activityType = pActivityType;
        Connection conn = null;
        try {
            conn = Singleton.getInstance().getConnection();
            StringBuffer sbQuery = new StringBuffer();
            String query = null;
            sbQuery.append("SELECT * FROM activity_type ");
            sbQuery.append(" WHERE id_activity_type = ? ");
            query = sbQuery.toString();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, activityType.getIdActivityType());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                activityType = ActivityTypeFactory.createActivityType(rs);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            conn.close();
        }
        return activityType;
    }

    /**
	 * 
	 * @param pRelatedForm
	 * @return
	 * @throws SQLException
	 */
    public final ActivityType getActivityTypeByRelatedForm(String pRelatedForm) throws SQLException {
        String relatedForm = pRelatedForm;
        String query = null;
        StringBuffer sbQuery = new StringBuffer();
        sbQuery.append("SELECT * FROM activity_type ");
        sbQuery.append(" WHERE related_form = ? ");
        query = sbQuery.toString();
        Connection conn = null;
        ActivityType activityType = null;
        try {
            conn = Singleton.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, relatedForm);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                activityType = ActivityTypeFactory.createActivityType(rs);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
        } finally {
            conn.close();
        }
        return activityType;
    }
}
