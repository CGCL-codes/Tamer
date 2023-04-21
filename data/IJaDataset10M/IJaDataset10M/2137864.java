package it.webscience.kpeople.dal.cross;

import java.sql.ResultSet;
import java.sql.SQLException;
import it.webscience.kpeople.be.EventMetadata;

/**
 * @author depascalis
 * Factory per la classe EventMetadata.
 */
public class EventMetadataFactory {

    /**
     * factory per l'oggetto EventMetadata.
     * @param rs ResultSet.
     * @return istanza dell'oggetto eventMetadata.
     * @throws SQLException eccezione DB.
     */
    public static EventMetadata createEventMetadata(final ResultSet rs) throws SQLException {
        EventMetadata eventMetadata = new EventMetadata();
        eventMetadata.setIdEventMetadata(rs.getInt("ID_EVENT_METADATA"));
        eventMetadata.setKeyname(rs.getString("KEYNAME"));
        eventMetadata.setValue(rs.getString("VALUE"));
        return eventMetadata;
    }
}
