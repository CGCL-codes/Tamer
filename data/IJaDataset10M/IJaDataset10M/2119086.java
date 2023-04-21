package compoundDB.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import compoundDB.core.Compound;
import compoundDB.database.container.ColumnField;
import compoundDB.database.container.SearchOption;
import compoundDB.database.enumeration.DataType;

/**
 * This class is an implementation of the DatabaseAccessor interface. It is the
 * 'heart' of the database sub-system, performing most of the interactions with
 * the database.
 * 
 * @author Kohl Bromwich
 * 
 */
public class DBManager implements DatabaseAccessor {

    /**
	 * The database connection.
	 */
    private Connection m_connection;

    /**
	 * Constructs a new instance of DBManager. Will perform the class lookup for
	 * database library specified using DBConfig.databaseLoadUrl.
	 * 
	 * @throws ClassNotFoundException
	 *             if the library cannot be loaded.
	 */
    public DBManager() throws ClassNotFoundException {
        Class.forName(DBConfig.getDatabaseLoadUrl());
    }

    @Override
    public void addColumnField(String name, DataType type) throws SQLException {
        Statement st = null;
        try {
            String dataType = getSqlDataType(type);
            st = m_connection.createStatement();
            String query = "ALTER TABLE " + DBConfig.getCompoundsTable() + " ADD COLUMN(" + name + " " + dataType + ");";
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void addCompounds(List<Compound> compounds) throws SQLException {
        PreparedStatement prep = null;
        try {
            List<ColumnField> columns = getCompoundTableColumns();
            StringBuilder query = new StringBuilder("INSERT INTO " + DBConfig.getCompoundsTable() + " (");
            boolean first = true;
            for (ColumnField col : columns) {
                if (!first) {
                    query.append(", ");
                }
                query.append(col.getName());
                first = false;
            }
            query.append(") VALUES (");
            first = true;
            for (int x = 0; x < columns.size(); x++) {
                if (!first) {
                    query.append(", ");
                }
                query.append("?");
                first = false;
            }
            query.append(")");
            prep = m_connection.prepareStatement(query.toString());
            for (Compound comp : compounds) {
                prep.clearParameters();
                addIntoCompoundTable(comp, columns, prep);
                comp.setGUID(getLastAutoGuid());
            }
            addIntoNameTable(compounds, false);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (prep != null) {
                prep.close();
            }
        }
    }

    /**
	 * Adds a compound into the database according to the supplied prepared
	 * statement and columns.
	 * 
	 * @param comp
	 *            The compound to add.
	 * 
	 * @param columns
	 *            The columns to add data into.
	 * 
	 * @param prep
	 *            The prepared statement to execute.
	 * 
	 * @throws SQLException
	 *             if an error occurs communicating with the database.
	 */
    private void addIntoCompoundTable(Compound comp, List<ColumnField> columns, PreparedStatement prep) throws SQLException {
        for (int x = 1; x <= columns.size(); x++) {
            Object val = comp.getProperty(columns.get(x - 1).getName());
            switch(columns.get(x - 1).getDataType()) {
                case STRING:
                    if (val != null) {
                        prep.setString(x, (String) val);
                    } else {
                        prep.setNull(x, Types.VARCHAR);
                    }
                    break;
                case LONG:
                    if (val != null) {
                        prep.setLong(x, (Long) val);
                    } else {
                        prep.setNull(x, Types.BIGINT);
                    }
                    break;
                case INTEGER:
                    if (val != null) {
                        prep.setInt(x, (Integer) val);
                    } else {
                        prep.setNull(x, Types.INTEGER);
                    }
                    break;
                case FLOAT:
                    if (val != null) {
                        prep.setFloat(x, (Float) val);
                    } else {
                        prep.setNull(x, Types.FLOAT);
                    }
                    break;
                case DOUBLE:
                    if (val != null) {
                        prep.setDouble(x, (Double) val);
                    } else {
                        prep.setNull(x, Types.DOUBLE);
                    }
                    break;
                case BYTES:
                    if (val != null) {
                        prep.setBytes(x, (byte[]) val);
                    } else {
                        prep.setNull(x, Types.BLOB);
                    }
                    break;
                default:
                    break;
            }
        }
        prep.addBatch();
        prep.execute();
    }

    /**
	 * Inserts the names of a list of compounds into the database. Will first
	 * remove existing names for the compounds if update is true.
	 * 
	 * @param compounds
	 *            The list of compounds whos names are to be added.
	 * 
	 * @param update
	 *            Whether or not existing names for compounds should be removed.
	 * 
	 * @throws SQLException
	 *             if an error occurs communicating with the database.
	 */
    private void addIntoNameTable(List<Compound> compounds, boolean update) throws SQLException {
        Statement st = null;
        PreparedStatement prep = null;
        try {
            if (update) {
                st = m_connection.createStatement();
                StringBuilder delQuery = new StringBuilder("DELETE FROM " + DBConfig.getCompoundNamesTable() + " WHERE guid IN (");
                boolean first = true;
                for (Compound comp : compounds) {
                    if (!first) {
                        delQuery.append(", ");
                    }
                    delQuery.append(comp.getGUID());
                    first = false;
                }
                delQuery.append(")");
                st.executeUpdate(delQuery.toString());
            }
            String query = "INSERT INTO " + DBConfig.getCompoundNamesTable() + " (guid, compound_name) VALUES (?, ?)";
            prep = m_connection.prepareStatement(query);
            for (Compound comp : compounds) {
                for (String name : comp.getNames()) {
                    prep.clearParameters();
                    prep.setLong(1, comp.getGUID());
                    prep.setString(2, name);
                    prep.addBatch();
                }
            }
            prep.executeBatch();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
            if (prep != null) {
                prep.close();
            }
        }
    }

    @Override
    public List<String> autoCompleteName(String mess, int maxNum) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        List<String> names = null;
        try {
            st = m_connection.createStatement();
            String query = "SELECT compound_name FROM " + DBConfig.getCompoundNamesTable() + " WHERE compound_name LIKE '" + mess + "%' LIMIT " + maxNum;
            rs = st.executeQuery(query);
            names = getCompoundNames(rs);
            return names;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void closeDatabaseConnection() throws SQLException {
        if (m_connection != null) {
            m_connection.close();
        }
    }

    @Override
    public void deleteCompound(List<Long> guids) throws SQLException {
        PreparedStatement prep = null;
        try {
            String query = "DELETE FROM " + DBConfig.getCompoundsTable() + " WHERE guid = ?;";
            prep = m_connection.prepareStatement(query);
            for (Long id : guids) {
                prep.setLong(1, id);
                prep.addBatch();
            }
            prep.executeBatch();
            prep.close();
            query = "DELETE FROM " + DBConfig.getCompoundNamesTable() + " WHERE guid = ?;";
            prep = m_connection.prepareStatement(query);
            for (Long id : guids) {
                prep.setLong(1, id);
                prep.addBatch();
            }
            prep.executeBatch();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (prep != null) {
                prep.close();
            }
        }
    }

    /**
	 * This method takes a ResultSet from a database query and extracts names
	 * from the compound_name column.
	 * 
	 * @param resultSet
	 *            The ResultSet of a query, which must contain a column
	 *            'compound_name'.
	 * @return The list of names retrieved from the ResultSet.
	 * 
	 * @throws SQLException
	 *             if there is an error communicating with the database.
	 */
    private List<String> getCompoundNames(ResultSet resultSet) throws SQLException {
        List<String> names = new ArrayList<String>();
        while (resultSet.next()) {
            String str = resultSet.getString("compound_name");
            names.add(str);
        }
        return names;
    }

    @Override
    public QueryReader getCompounds(List<Long> guids) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        CachedQueryReader rdr = null;
        try {
            st = m_connection.createStatement();
            StringBuilder query = new StringBuilder("SELECT * FROM " + DBConfig.getCompoundsTable() + " WHERE guid IN (0");
            for (Long guid : guids) {
                query.append(", ");
                query.append(guid.toString());
            }
            query.append(")");
            rs = st.executeQuery(query.toString());
            rdr = new CachedQueryReader(this, rs);
            return rdr;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public QueryReader getCompounds(String name) throws SQLException {
        PreparedStatement prep = null;
        ResultSet rs = null;
        List<Long> guids = new ArrayList<Long>();
        try {
            name += "%";
            String query = "SELECT * FROM " + DBConfig.getCompoundNamesTable() + " WHERE (compound_name LIKE ?);";
            prep = m_connection.prepareStatement(query);
            prep.setString(1, name);
            rs = prep.executeQuery();
            while (rs.next()) {
                guids.add(rs.getLong("guid"));
            }
            return getCompounds(guids);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (prep != null) {
                prep.close();
            }
        }
    }

    @Override
    public List<ColumnField> getCompoundTableColumns() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        List<ColumnField> columns = new ArrayList<ColumnField>();
        try {
            st = m_connection.createStatement();
            String query = ("SELECT * FROM " + DBConfig.getCompoundsTable() + " LIMIT 0, 1;");
            rs = st.executeQuery(query.toString());
            ResultSetMetaData md = rs.getMetaData();
            for (int x = 1; x <= md.getColumnCount(); x++) {
                String name = md.getColumnName(x);
                DataType dt;
                String ctn = md.getColumnTypeName(x);
                if (ctn.equals("VARCHAR")) {
                    dt = DataType.STRING;
                } else if (ctn.equals("BIGINT")) {
                    dt = DataType.LONG;
                } else if (ctn.equals("INT")) {
                    dt = DataType.INTEGER;
                } else if (ctn.equals("FLOAT")) {
                    dt = DataType.FLOAT;
                } else if (ctn.equals("DOUBLE")) {
                    dt = DataType.DOUBLE;
                } else if (ctn.equals("BLOB")) {
                    dt = DataType.BYTES;
                } else {
                    continue;
                }
                ColumnField col = new ColumnField(name, dt, x);
                columns.add(col);
            }
            return columns;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

    /**
	 * Retrieves from the database the last auto_increment value (GUID)
	 * inserted.
	 * 
	 * @return the last auto_increment value inserted in the database.
	 * 
	 * @throws SQLException
	 *             if an error occurs communicating with the database.
	 */
    private int getLastAutoGuid() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        int id;
        try {
            String queryStr = "SELECT LAST_INSERT_ID()";
            st = m_connection.createStatement();
            rs = st.executeQuery(queryStr);
            if (rs.next()) {
                id = rs.getInt(1);
            } else {
                throw new SQLException("Could not obtain last Auto Increment GUID");
            }
            return id;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public List<String> getNames(long guid) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        List<String> names = null;
        try {
            st = m_connection.createStatement();
            String query = "SELECT * FROM " + DBConfig.getCompoundNamesTable() + " WHERE guid=" + guid;
            rs = st.executeQuery(query);
            names = getCompoundNames(rs);
            return names;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

    /**
	 * Converts the given local datatype to the datatype used in the database.
	 * 
	 * @param dt
	 *            The local datatype to convert.
	 * 
	 * @return The converted datatype as a string for the database to use.
	 */
    private String getSqlDataType(DataType dt) {
        switch(dt) {
            case STRING:
                return "TEXT";
            case LONG:
                return "BIGINT";
            case INTEGER:
                return "INT";
            case FLOAT:
                return "FLOAT";
            case DOUBLE:
                return "DOUBLE";
            case BYTES:
                return "BLOB";
            default:
                return null;
        }
    }

    @Override
    public void openDatabaseConnection() throws SQLException {
        String url = DBConfig.getConnectUrl() + "//" + DBConfig.getConnectHost() + ":" + DBConfig.getConnectPort() + "/" + DBConfig.getCompoundDatabase();
        m_connection = DriverManager.getConnection(url, DBConfig.getUser(), DBConfig.getPassword());
    }

    @Override
    public void removeColumnField(ColumnField column) throws SQLException {
        Statement st = null;
        try {
            if (column.getColumnNumber() <= DBConfig.getNumberOfDefaultColumns()) {
                throw new IllegalArgumentException("Cannot rename or remove default database columns!");
            }
            st = m_connection.createStatement();
            String query = "ALTER TABLE " + DBConfig.getCompoundsTable() + " DROP COLUMN " + column.getName();
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void renameColumnField(ColumnField column, String newName) throws SQLException {
        Statement st = null;
        try {
            if (column.getColumnNumber() <= DBConfig.getNumberOfDefaultColumns()) {
                throw new IllegalArgumentException("Cannot rename or remove default database columns!");
            }
            st = m_connection.createStatement();
            String query = "ALTER TABLE " + DBConfig.getCompoundsTable() + " CHANGE COLUMN " + column.getName() + " " + newName + " " + getSqlDataType(column.getDataType()) + ";";
            st.executeUpdate(query);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public QueryReader searchCompounds(List<SearchOption> searchValues) throws SQLException {
        Statement st = null;
        try {
            if (searchValues == null || searchValues.size() < 1) {
                throw new IllegalArgumentException("Must have at least one search filter");
            }
            st = m_connection.createStatement();
            StringBuilder query = new StringBuilder("SELECT * FROM " + DBConfig.getCompoundsTable() + " WHERE");
            boolean first = true;
            for (SearchOption so : searchValues) {
                if (!first) {
                    query.append(" AND");
                }
                query.append(" (" + so.getParsedExpression() + ")");
                first = false;
            }
            ResultSet rs = st.executeQuery(query.toString());
            return new CachedQueryReader(this, rs);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    @Override
    public void updateCompound(Compound compound) throws SQLException {
        PreparedStatement prep = null;
        try {
            List<ColumnField> columns = this.getCompoundTableColumns();
            StringBuilder query = new StringBuilder("UPDATE " + DBConfig.getCompoundsTable() + " SET");
            boolean first = true;
            for (ColumnField col : columns) {
                if (!first) {
                    query.append(",");
                }
                query.append(" " + col.getName() + " = ?");
                first = false;
            }
            query.append(" WHERE guid = ?");
            prep = m_connection.prepareStatement(query.toString());
            prep.setLong(columns.size() + 1, compound.getGUID());
            addIntoCompoundTable(compound, columns, prep);
            List<Compound> compounds = new ArrayList<Compound>();
            compounds.add(compound);
            addIntoNameTable(compounds, true);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (prep != null) {
                prep.close();
            }
        }
    }
}
