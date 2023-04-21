package pl.prv.consept.gestionnaire.database;

import java.util.ArrayList;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * For the table TypesDestinataires.
 *
 * @version $22.01.2006 v1.00$
 */
public final class JustificationsSDP extends AbstractSmallDataProvider {

    /**
	 * Check if this provided has been created. If not, creates it.
	 *
	 * @param da
	 */
    public static SmallDataProvider getSmallDataProvider(DatabaseAccess da) throws SQLException {
        if (!DataProviderManager.isDataProviderCreated(DataProviderManager.JUSTIFICATIONS)) {
            DataProviderManager.addDataProvider(DataProviderManager.JUSTIFICATIONS, new JustificationsSDP(da));
        }
        return DataProviderManager.getSmallDataProvider(DataProviderManager.JUSTIFICATIONS);
    }

    private JustificationsSDP(DatabaseAccess da) throws SQLException {
        super(da, 0, 1);
    }

    void reload() throws SQLException {
        String query = "SELECT Code, Nom FROM Justifications";
        rs = st.executeQuery(query);
    }
}
