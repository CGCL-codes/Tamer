package org.sqlexp.view.properties;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import org.sqlexp.sql.SqlObject;

/**
 * Child view of properties view displaying object metadata information concerning capacities.
 * @author Matthieu Réjou
 */
public final class NullBehaviorView extends PropertiesDataView {

    @Override
    boolean isCheck() {
        return true;
    }

    @Override
    void getData(final Connection connection, final SqlObject<?, ?> object) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        add("nullPlusNonNullIsNull", metaData.nullPlusNonNullIsNull());
        add("sort");
        addTo("sort", "sortAtEnd", metaData.nullsAreSortedAtEnd());
        addTo("sort", "sortAtStart", metaData.nullsAreSortedAtStart());
        addTo("sort", "sortHigh", metaData.nullsAreSortedHigh());
        addTo("sort", "sortLow", metaData.nullsAreSortedLow());
    }
}
