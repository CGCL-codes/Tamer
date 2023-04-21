package fr.soleil.TangoArchiving.ArchivingApi.DbImpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import fr.soleil.TangoArchiving.ArchivingApi.ConfigConst;
import fr.soleil.TangoArchiving.ArchivingTools.Tools.ArchivingException;
import fr.soleil.TangoArchiving.ArchivingTools.Tools.ConvertClass;

public abstract class BasicExtractorDataBaseApiImpl implements IExtractorDataBaseApi {

    private IDataBaseApiTools m_dataBaseApiTools;

    /**
	 * @param dataBaseApiBAsic
	 * @roseuid 45CC37EE0154
	 */
    protected BasicExtractorDataBaseApiImpl(IDataBaseApiTools dataBaseApiBasic) {
        m_dataBaseApiTools = dataBaseApiBasic;
    }

    /**
	 * @return java.lang.String[]
	 * @throws fr.soleil.TangoArchiving.ArchivingTools.Tools.ArchivingException
	 * @roseuid 45EBFF3E037E
	 */
    public String[] getCurrentArchivedAtt() throws ArchivingException {
        return null;
    }

    /**
	 * <b>Description : </b> Gets all the registered domains.
	 *
	 * @return all the registered domains (array of strings)
	 * @throws ArchivingException
	 */
    public String[] get_domains() throws ArchivingException {
        Vector argout_vect = new Vector();
        String[] argout;
        Statement stmt = null;
        ResultSet rset;
        String sqlStr;
        sqlStr = "SELECT DISTINCT " + ConfigConst.TAB_DEF[4] + " FROM " + m_dataBaseApiTools.getDbSchema() + "." + ConfigConst.TABS[0];
        try {
            stmt = m_dataBaseApiTools.getDbConnection().createStatement();
            rset = stmt.executeQuery(sqlStr);
            while (rset.next()) argout_vect.addElement(rset.getString(ConfigConst.TAB_DEF[4]));
        } catch (SQLException e) {
            m_dataBaseApiTools.sqlExceptionTreatment(e, "get_domains");
        } finally {
            try {
                m_dataBaseApiTools.close(stmt);
            } catch (SQLException e) {
                m_dataBaseApiTools.sqlExceptionTreatment(e, "get_domains");
            }
        }
        argout = ConvertClass.toStringArray(argout_vect);
        return argout;
    }
}
