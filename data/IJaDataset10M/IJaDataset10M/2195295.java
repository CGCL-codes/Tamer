package org.plazmaforge.framework.erm.event;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface Event {

    String ENTITY_PARAMETER = "entity";

    String DATA_PARAMETER = "data";

    String ENTRY_POINT_PARAMETER = "entry-point";

    String ENTITY_MANAGER = "entityManager";

    EventType getType();

    void fire(Connection cn, Map<String, Object> parameters) throws SQLException;
}
