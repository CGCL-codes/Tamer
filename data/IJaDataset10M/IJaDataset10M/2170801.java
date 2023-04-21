package it.webscience.kpeople.dal.activity;

import it.webscience.kpeople.be.PatternMetadata;
import it.webscience.kpeople.dal.dataTraceClass.DataTraceClassFactory;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActivityMetadataFactory {

    /**
     * factory per l'oggetto ActivityMetadata.
     * @param rs resultset
     * @return istanza dell'oggetto ActivityMetadata
     * @throws SQLException label colonne non valido
     */
    public static PatternMetadata createActivityMetadata(final ResultSet rs) throws SQLException {
        PatternMetadata pm = new PatternMetadata();
        pm.setIdPatternMetadata(rs.getInt("ID_ACTIVITY_METADATA"));
        pm.setIdPattern(rs.getInt("ID_ACTIVITY"));
        pm.setKeyname(rs.getString("KEYNAME"));
        pm.setValue(rs.getString("VALUE"));
        DataTraceClassFactory.createDataTraceClass(pm, rs);
        return pm;
    }
}
