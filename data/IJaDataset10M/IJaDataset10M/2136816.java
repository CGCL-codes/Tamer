package org.eoti.gaea.db;

import org.eoti.mimir.MimirException;
import org.eoti.mimir.MimirRegistry;
import org.eoti.mimir.SchemeNotFoundException;
import org.eoti.spec.gaea.event.v1.EventType;
import org.eoti.spec.mimirdb.v1.DBMapping;
import java.math.BigInteger;

public class EventDB extends GaeaDB<EventType> {

    /**
	 * Create an instance of a database
	 *
	 * @param registry MimirRegistry responsible for maintaining the mappings
	 * @param dbMapping specification for this database
	 *
	 * @throws org.eoti.mimir.SchemeValidationException if the scheme is invalid
	 * @throws org.eoti.mimir.SchemeNotFoundException if the scheme is not found
	 */
    public EventDB(MimirRegistry registry, DBMapping dbMapping) throws SchemeNotFoundException, MimirException {
        super(registry, dbMapping);
    }

    protected void initializeDB() {
    }

    protected BigInteger getIDfromDATA(EventType eventType) {
        return eventType.getId();
    }

    public EventType createNew() throws MimirException {
        EventType event = new EventType();
        event.setId(getNextID());
        return event;
    }
}
