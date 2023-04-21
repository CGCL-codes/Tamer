package edu.ucdavis.genomics.metabolomics.binbase.server.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;
import org.apache.log4j.Logger;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.util.msp.BinToMspConverter;
import edu.ucdavis.genomics.metabolomics.exception.BinBaseException;

/**
 * simple helper class for query handling
 * 
 * @author wohlgemuth
 * 
 */
public class QueryHelper {

    public Logger logger = Logger.getLogger(getClass());

    /**
	 * return properties of the given bin
	 * 
	 * @author wohlgemuth
	 * @return
	 * @throws BinBaseException
	 * @throws BinBaseException
	 */
    public String getBin(int binID, Connection connection) throws BinBaseException {
        StringBuffer resultSet = new StringBuffer();
        resultSet.append("<bin>\n");
        try {
            PreparedStatement bin = connection.prepareStatement("SELECT * FROM BIN where \"bin_id\" = ?");
            bin.setInt(1, binID);
            createContent(bin.executeQuery(), resultSet);
            bin.close();
        } catch (Exception e) {
            throw new BinBaseException(e.getMessage(), e);
        }
        resultSet.append("</bin>\n");
        return resultSet.toString();
    }

    /**
	 * return properties of the given bin
	 * 
	 * @author wohlgemuth
	 * @return
	 * @throws BinBaseException
	 * @throws BinBaseException
	 */
    public String getBinWithRefrences(int binID, Connection connection) throws BinBaseException {
        StringBuffer resultSet = new StringBuffer();
        resultSet.append("<bin>\n");
        try {
            PreparedStatement bin = connection.prepareStatement("SELECT * FROM BIN where \"bin_id\" = ?");
            bin.setInt(1, binID);
            createContent(bin.executeQuery(), resultSet);
            bin.close();
        } catch (Exception e) {
            throw new BinBaseException(e.getMessage(), e);
        }
        resultSet.append(getRefrencesToBin(binID, connection));
        resultSet.append("</bin>\n");
        return resultSet.toString();
    }

    /**
	 * @author wohlgemuth
	 * @return
	 * @throws BinBaseException
	 */
    public String getRefrencesToBin(int binID, Connection connection) throws BinBaseException {
        try {
            PreparedStatement bin = connection.prepareStatement("SELECT value,\"NAME\",description,\"pattern\" FROM reference a, reference_class b where a.class_id = b.id and a.bin_id = ?");
            bin.setInt(1, binID);
            StringBuffer resultSet = new StringBuffer();
            resultSet.append("<ref>\n");
            createContent(bin.executeQuery(), resultSet);
            resultSet.append("</ref>\n");
            bin.close();
            return resultSet.toString();
        } catch (Exception e) {
            throw new BinBaseException(e.getMessage(), e);
        }
    }

    /**
	 * @author wohlgemuth
	 * @return
	 * @throws BinBaseException
	 */
    public int[] getAllUnNamedBinIds(Connection connection) throws BinBaseException {
        try {
            PreparedStatement bin = connection.prepareStatement("select \"bin_id\" from bin where \"name\" = TO_CHAR(\"bin_id\")");
            ResultSet result = bin.executeQuery();
            Vector<Integer> temp = new Vector<Integer>();
            while (result.next()) {
                temp.add(new Integer(result.getInt(1)));
            }
            bin.close();
            int[] res = new int[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                res[i] = temp.get(i);
            }
            return res;
        } catch (Exception e) {
            throw new BinBaseException(e.getMessage(), e);
        }
    }

    /**
	 * @author wohlgemuth
	 * @return
	 * @throws BinBaseException
	 */
    public int[] getAllNamedBinIds(Connection connection) throws BinBaseException {
        try {
            PreparedStatement bin = connection.prepareStatement("select \"bin_id\" from bin where \"name\" != TO_CHAR(\"bin_id\")");
            ResultSet result = bin.executeQuery();
            Vector<Integer> temp = new Vector<Integer>();
            while (result.next()) {
                temp.add(new Integer(result.getInt(1)));
            }
            bin.close();
            int[] res = new int[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                res[i] = temp.get(i);
            }
            return res;
        } catch (Exception e) {
            throw new BinBaseException(e.getMessage(), e);
        }
    }

    /**
	 * @author wohlgemuth
	 * @return
	 * @throws BinBaseException
	 */
    public int[] getAllBinIds(Connection connection) throws BinBaseException {
        try {
            PreparedStatement bin = connection.prepareStatement("select \"bin_id\" from bin");
            ResultSet result = bin.executeQuery();
            Vector<Integer> temp = new Vector<Integer>();
            while (result.next()) {
                temp.add(new Integer(result.getInt(1)));
            }
            bin.close();
            int[] res = new int[temp.size()];
            for (int i = 0; i < temp.size(); i++) {
                res[i] = temp.get(i);
            }
            return res;
        } catch (Exception e) {
            throw new BinBaseException(e.getMessage(), e);
        }
    }

    /**
	 * creates the content based on the result set
	 * 
	 * @param result
	 * @return
	 * @throws SQLException
	 * @throws CompoundNotFoundException
	 */
    public void createContent(ResultSet result, StringBuffer resultSet) throws SQLException {
        logger.info("creating content...");
        if (result.next()) {
            ResultSetMetaData meta = result.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                String name = meta.getColumnName(i);
                Object value = null;
                switch(meta.getColumnType(i)) {
                    case Types.INTEGER:
                        value = result.getInt(i);
                        break;
                    case Types.NUMERIC:
                        value = result.getDouble(i);
                        break;
                    case Types.DOUBLE:
                        value = result.getDouble(i);
                        break;
                    case Types.DATE:
                        value = result.getDate(i);
                        break;
                    case Types.BOOLEAN:
                        value = result.getBoolean(i);
                        break;
                    case Types.FLOAT:
                        value = result.getFloat(i);
                        break;
                    case Types.VARCHAR:
                        value = "<![CDATA[" + result.getString(i) + "]]>";
                        break;
                    case Types.NULL:
                        value = result.getString(i);
                        break;
                    default:
                        value = "<![CDATA[" + result.getString(i) + "]]>";
                }
                resultSet.append("<property key=\"" + name + "\">" + value + "</property>\n");
            }
        }
    }

    /**
	 * generate an msp file
	 * 
	 * @param binId
	 * @param connection
	 * @return
	 * @throws BinBaseException
	 */
    public String getMspFileForBin(int binId, Connection connection) throws BinBaseException {
        try {
            PreparedStatement bin = connection.prepareStatement("SELECT * FROM BIN where \"bin_id\" = ?");
            bin.setInt(1, binId);
            ResultSet set = bin.executeQuery();
            try {
                if (set.next()) {
                    String name = set.getString("name");
                    String id = set.getString("bin_id");
                    String ri = set.getString("retention_index");
                    String spectra = BinToMspConverter.convertSpectraToMspSpectra(set.getString("spectra"));
                    StringBuffer content = new StringBuffer();
                    content.append("Name: ").append(name).append("\n").append("Id: ").append(id).append("\n").append("RetentionIndex: ").append(ri).append("\n").append(spectra).append("\n");
                    return content.toString();
                } else {
                    throw new BinBaseException("bin not found!");
                }
            } finally {
                bin.close();
            }
        } catch (Exception e) {
            throw new BinBaseException(e.getMessage(), e);
        }
    }
}
