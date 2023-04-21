package org.slasoi.monitoring.city.database.slasoiInteractionEvent;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import uk.ac.city.soi.database.EntityManagerCommons;
import uk.ac.city.soi.database.EntityManagerInterface;
import uk.ac.city.soi.database.PersistentEntity;
import uk.ac.city.soi.everest.SRNTEvent.TypeOfEvent;

/**
 * This class provides methods for managing the SlasoiInteractionEvent MySQL database table.
 * 
 */
public class SlasoiInteractionEventEntityManager extends EntityManagerCommons implements EntityManagerInterface, SlasoiInteractionEventEntityManagerInterface {

    private static Logger logger = Logger.getLogger(SlasoiInteractionEventEntityManager.class);

    /**
     * Constructor.
     * 
     * @param connection
     */
    public SlasoiInteractionEventEntityManager(Connection connection) {
        super(connection);
    }

    /**
     * @see org.slasoi.monitoring.city.database.slasoiInteractionEvent.SlasoiInteractionEventEntityManagerInterface#addEvent(java.lang.String,
     *      java.lang.String, uk.ac.city.soi.everest.SRNTEvent.TypeOfEvent)
     */
    public void addEvent(String slasoiEventId, String slasoiInteractionEventXml, TypeOfEvent everestEventObject) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        Blob everestEventObjectBlob = null;
        try {
            everestEventObjectBlob = toBlob(everestEventObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            pstmt = getConnection().prepareStatement("INSERT INTO slasoi_interaction_event " + "(slasoi_event_id, slasoi_interaction_event_xml, everest_event_object) " + "VALUES (?, ?, ?)");
            pstmt.setString(1, slasoiEventId);
            pstmt.setString(2, slasoiInteractionEventXml);
            pstmt.setBlob(3, everestEventObjectBlob);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            logger.debug("SQLException: " + ex.getMessage());
            logger.debug("SQLState: " + ex.getSQLState());
            logger.debug("VendorError: " + ex.getErrorCode());
            ex.printStackTrace();
        } finally {
            releaseResources(pstmt, resultSet);
        }
    }

    /**
     * @see org.slasoi.monitoring.city.database.slasoiInteractionEvent.SlasoiInteractionEventEntityManagerInterface#getAllEverestEvents()
     */
    public ArrayList<TypeOfEvent> getAllEverestEvents() {
        ArrayList<TypeOfEvent> everestEvents = new ArrayList<TypeOfEvent>();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = getConnection().prepareStatement("SELECT * FROM slasoi_interaction_event");
            resultSet = pstmt.executeQuery();
            if (resultSet.first()) {
                do {
                    Blob blob = resultSet.getBlob("everest_event_object");
                    TypeOfEvent event = null;
                    try {
                        event = (TypeOfEvent) toObject(blob);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (event != null) {
                        everestEvents.add(event);
                    }
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            logger.debug("SQLException: " + ex.getMessage());
            logger.debug("SQLState: " + ex.getSQLState());
            logger.debug("VendorError: " + ex.getErrorCode());
            ex.printStackTrace();
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return everestEvents;
    }

    public HashMap<String, TypeOfEvent> getAllEverestEventsWithPrimaryKey() {
        HashMap<String, TypeOfEvent> everestEvents = new HashMap<String, TypeOfEvent>();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = getConnection().prepareStatement("SELECT * FROM slasoi_interaction_event");
            resultSet = pstmt.executeQuery();
            if (resultSet.first()) {
                do {
                    Blob blob = resultSet.getBlob("everest_event_object");
                    TypeOfEvent event = null;
                    String key = resultSet.getString("slasoi_event_id");
                    try {
                        event = (TypeOfEvent) toObject(blob);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (event != null) {
                        everestEvents.put(key, event);
                    }
                } while (resultSet.next());
            }
        } catch (SQLException ex) {
            logger.debug("SQLException: " + ex.getMessage());
            logger.debug("SQLState: " + ex.getSQLState());
            logger.debug("VendorError: " + ex.getErrorCode());
            ex.printStackTrace();
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return everestEvents;
    }

    /**
     * @see uk.ac.city.soi.database.EntityManagerInterface#executeQuery(java.lang.String)
     */
    public ArrayList executeQuery(String query) {
        return null;
    }

    /**
     * @see uk.ac.city.soi.database.EntityManagerInterface#select()
     */
    public ArrayList select() {
        return null;
    }

    /**
     * @see uk.ac.city.soi.database.EntityManagerInterface#selectByPrimaryKey(java.lang.String)
     */
    public PersistentEntity selectByPrimaryKey(String entityPrimaryKey) {
        return null;
    }

    /**
     * @see uk.ac.city.soi.database.EntityManagerInterface#delete(java.lang.Object)
     */
    public int delete(Object arg0) {
        return 0;
    }

    /**
     * @see uk.ac.city.soi.database.EntityManagerInterface#deleteByPrimaryKey(java.lang.String)
     */
    public int deleteByPrimaryKey(String arg0) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = getConnection().prepareStatement("DELETE FROM slasoi_interaction_event " + "WHERE slasoi_event_id = ?");
            pstmt.setString(1, arg0);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            logger.debug("SQLException: " + ex.getMessage());
            logger.debug("SQLState: " + ex.getSQLState());
            logger.debug("VendorError: " + ex.getErrorCode());
            ex.printStackTrace();
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return 0;
    }

    /**
     * @see uk.ac.city.soi.database.EntityManagerInterface#insert(java.lang.Object)
     */
    public int insert(Object arg0) {
        return 0;
    }

    /**
     * @see uk.ac.city.soi.database.EntityManagerInterface#update(java.lang.Object)
     */
    public int update(Object arg0) {
        return 0;
    }
}
