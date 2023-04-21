package seismosurfer.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import seismosurfer.data.MetaData;
import seismosurfer.util.SeismoException;

/**
 * A DAO class that encapsulates all access to the
 * METADATA table.
 *
 */
public class MetaDataDAO {

    protected static final String METADATA_TABLE = " SELECT * FROM metadata ";

    private List metadata = new ArrayList(4);

    /**
     * Metadata is a small table so all entries are loaded into memory,
     * during instantiation.
     *
     */
    public MetaDataDAO() {
        loadMetaData();
    }

    /**
     * Retrieves a list with all the metadata in the database.
     * 
     * @return a list with all the metadata.
     */
    public List getMetaData() {
        return metadata;
    }

    public int getMetaDataCount() {
        return metadata.size();
    }

    /**
     * Gets a resource`s path given its id.
     * 
     * @param resID the id of a resource
     * @return a String which contains the resource`s id.
     */
    public String getResourcePath(int resID) {
        String path = null;
        for (int i = 0; i < metadata.size(); i++) {
            MetaData resData = (MetaData) metadata.get(i);
            if (resData.getMetaDataID() == resID) {
                path = resData.getResourcePath();
                break;
            }
        }
        return path;
    }

    /**
     * Given a resource`s id gets its metadata.
     * 
     * @param resID the id of the resource
     * @return the MetaData associated with a resource
     */
    public MetaData getMetaData(int resID) {
        MetaData resData = new MetaData();
        for (int i = 0; i < metadata.size(); i++) {
            resData = (MetaData) metadata.get(i);
            if (resData.getMetaDataID() == resID) {
                System.out.println(resData.getMetaDataID());
                return resData;
            }
        }
        return resData;
    }

    /**
     * Loads a row from the database into a MetaData object.
     * 
     * @param rs The resultset from which the row will be loaded.
     * @return A MetaData object which contains the data of a row.
     * @throws SQLException
     */
    public static MetaData load(ResultSet rs) throws SQLException {
        MetaData d = new MetaData();
        d.setMetaDataID(rs.getInt(1));
        d.setResourceCategory(rs.getString(2));
        d.setResourceDescription(rs.getString(3));
        d.setResourcePath(rs.getString(4));
        d.setResourceSize(rs.getString(5));
        d.setResourceSource(rs.getString(6));
        d.setResourceSourceURL(rs.getString(7));
        d.setResourceInfo(rs.getString(8));
        return d;
    }

    /**
     * Loads all data from the METADATA table into memory.
     *
     */
    protected void loadMetaData() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            metadata.clear();
            stmt = DB.prepare(METADATA_TABLE);
            rs = stmt.executeQuery();
            while (rs.next()) {
                metadata.add(load(rs));
            }
            System.out.println("Maps Count :" + this.metadata.size());
        } catch (SQLException e) {
            throw new SeismoException(e);
        } finally {
            DB.cleanUp(stmt, rs);
        }
    }
}
