package pl.prv.consept.gestionnaire.database;

import java.util.ArrayList;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * For the table Secteurs.
 *
 * @version $22.01.2006 v1.00$
 */
public final class SecteursSDP extends AbstractSmallDataProvider {

    /**
	 * Check if this provided has been created. If not, creates it.
	 *
	 * @param da
	 */
    public static SmallDataProvider getSmallDataProvider(DatabaseAccess da) throws SQLException {
        if (!DataProviderManager.isDataProviderCreated(DataProviderManager.SECTEURS)) {
            DataProviderManager.addDataProvider(DataProviderManager.SECTEURS, new SecteursSDP(da));
        }
        return DataProviderManager.getSmallDataProvider(DataProviderManager.SECTEURS);
    }

    private SecteursSDP(DatabaseAccess da) throws SQLException {
        super(da, 0, 1);
    }

    void reload() throws SQLException {
        String query = "SELECT Code, Nom FROM Secteurs";
        rs = st.executeQuery(query);
    }
}
