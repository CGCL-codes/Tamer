package alphahr;

/**
 * Database
 *
 * @version     0.00 21 Feb 2011
 * @author      Andrey Pudov
 */
public class Database {

    private java.sql.Connection dbConnection = null;

    private java.util.Properties dbProperties = null;

    private boolean isConnected = false;

    private String database = null;

    private java.sql.PreparedStatement stmtSelectGeneral;

    private java.sql.PreparedStatement stmtSelectGeneralItem;

    private java.sql.PreparedStatement stmtSelectAddress;

    private java.sql.PreparedStatement stmtSelectCountry;

    private java.sql.PreparedStatement stmtSelectState;

    private java.sql.PreparedStatement stmtSelectCity;

    private java.sql.PreparedStatement stmtSelectPostalCode;

    private java.sql.PreparedStatement stmtSelectPhone;

    private java.sql.PreparedStatement stmtSelectWebAddress;

    private java.sql.PreparedStatement stmtInsertGeneral;

    private java.sql.PreparedStatement stmtInsertAddress;

    private java.sql.PreparedStatement stmtInsertCountry;

    private java.sql.PreparedStatement stmtInsertState;

    private java.sql.PreparedStatement stmtInsertCity;

    private java.sql.PreparedStatement stmtInsertPostalCode;

    private java.sql.PreparedStatement stmtInsertPhone;

    private java.sql.PreparedStatement stmtInsertWebAddress;

    private java.sql.PreparedStatement stmtDeleteGeneral;

    private java.sql.PreparedStatement stmtDeletePhone;

    private java.sql.PreparedStatement stmtDeleteAllPhones;

    private java.sql.PreparedStatement stmtDeleteWebAddress;

    private java.sql.PreparedStatement stmtDeleteAllWebAddresses;

    private static final String strSelectGeneral = "SELECT * FROM APP.GENERAL ORDER BY LASTNAME ASC";

    private static final String strSelectGeneralItem = "SELECT * FROM APP.GENERAL WHERE ID = ?";

    private static final String strSelectAddress = "SELECT a.ID, a.ADDRESS1, a.ADDRESS2, ct.CITY, " + "       s.STATE, p.POSTALCODE, c.COUNTRY" + "    FROM APP.ADDRESS a" + "        LEFT JOIN APP.CITY ct ON ct.ID = a.CITY" + "        LEFT JOIN APP.STATE s ON s.ID = a.STATE" + "        LEFT JOIN APP.POSTALCODE p ON p.ID = a.POSTALCODE" + "        LEFT JOIN APP.COUNTRY c ON c.ID = a.COUNTRY" + "    WHERE a.ID = ?";

    private static final String strSelectCountry = "SELECT * FROM APP.COUNTRY ORDER BY COUNTRY ASC";

    private static final String strSelectState = "SELECT * FROM APP.STATE s, APP.COUNTRY c " + "   WHERE s.COUNTRY = c.ID AND c.COUNTRY = ? " + "   ORDER BY STATE ASC";

    private static final String strSelectCity = "SELECT * FROM APP.CITY c, APP.STATE s " + "   WHERE c.STATE = s.ID AND s.STATE = ? " + "   ORDER BY CITY ASC";

    private static final String strSelectPostalCode = "SELECT * FROM APP.POSTALCODE WHERE ID = ?";

    private static final String strSelectPhone = "SELECT * FROM APP.PHONE WHERE ID = ?";

    private static final String strSelectWebAddress = "SELECT * FROM APP.WEBADDRESS WHERE ID = ?";

    private static final String strInsertGeneral = "INSERT INTO APP.GENERAL " + "   (LASTNAME, FIRSTNAME, MIDDLENAME, SEX, TITLE, BIRTHDAY) " + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String strInsertAddress = "INSERT INTO APP.ADDRESS " + "    (ID, ADDRESS1, ADDRESS2, CITY, STATE, POSTALCODE, COUNTRY) " + "VALUES (?, ?, ?, " + "    (SELECT ID FROM APP.CITY" + "        WHERE CITY = ?" + "        AND STATE = (SELECT ID FROM APP.STATE" + "            WHERE STATE = ?" + "            AND COUNTRY = (SELECT ID FROM APP.COUNTRY " + "                WHERE COUNTRY = ?))), " + "    (SELECT ID FROM APP.STATE " + "        WHERE STATE = ?" + "        AND COUNTRY = (SELECT ID FROM APP.COUNTRY" + "            WHERE COUNTRY = ?)), " + "    (SELECT ID FROM APP.POSTALCODE WHERE POSTALCODE = ?), " + "    (SELECT ID FROM APP.COUNTRY WHERE COUNTRY = ?))";

    private static final String strInsertCountry = "INSERT INTO APP.COUNTRY " + "   (COUNTRY) " + "(SELECT ? FROM APP.COUNTRY " + "   WHERE COUNTRY = ? HAVING COUNT(*) = 0)";

    private static final String strInsertState = "INSERT INTO APP.STATE " + "   (COUNTRY, STATE) " + "(SELECT " + "      (SELECT ID FROM APP.COUNTRY WHERE COUNTRY = ?), ? " + "   FROM APP.STATE " + "   WHERE " + "      COUNTRY = (SELECT ID FROM APP.COUNTRY WHERE COUNTRY = ?) AND " + "      STATE = ? " + "      HAVING COUNT(*) = 0)";

    private static final String strInsertCity = "INSERT INTO APP.CITY " + "   (STATE, CITY) " + "(SELECT " + "      (SELECT ID FROM APP.STATE WHERE STATE = ? AND" + "      COUNTRY = (SELECT ID FROM APP.COUNTRY WHERE COUNTRY = ?)), ? " + "   FROM APP.CITY " + "   WHERE " + "      STATE = (SELECT ID FROM APP.STATE WHERE STATE = ?) AND " + "      CITY = ? " + "      HAVING COUNT(*) = 0)";

    private static final String strInsertPostalCode = "INSERT INTO APP.POSTALCODE " + "   (POSTALCODE) " + "(SELECT ? FROM APP.POSTALCODE " + "   WHERE POSTALCODE = ? HAVING COUNT(*) = 0)";

    private static final String strInsertPhone = "INSERT INTO APP.PHONE " + "   (ID, PHONE, TYPE) " + "VALUES (?, ?, ?)";

    private static final String strInsertWebAddress = "INSERT INTO APP.WEBADDRESS " + "   (ID, ADDRESS, TYPE) " + "VALUES (?, ?, ?)";

    private static final String strDeleteGeneral = "DELETE FROM APP.GENERAL WHERE ID = ?";

    private static final String strDeleteAllPhones = "DELETE FROM APP.PHONE WHERE ID = ?";

    private static final String strDeletePhone = "DELETE FROM APP.PHONE WHERE ID = ? AND PHONE = ?";

    private static final String strDeleteAllWebAddresses = "DELETE FROM APP.WEBADDRESS WHERE ID = ?";

    private static final String strDeleteWebAddress = "DELETE FROM APP.WEBADDRESS WHERE ID = ? AND ADDRESS = ?";

    private static final String GENERAL_TABLE = "CREATE table APP.GENERAL (" + "ID         INTEGER NOT NULL " + "           PRIMARY KEY GENERATED ALWAYS AS IDENTITY " + "           (START WITH 1, INCREMENT BY 1)," + "LASTNAME   VARCHAR(30)," + "FIRSTNAME  VARCHAR(30)," + "MIDDLENAME VARCHAR(30)," + "SEX        SMALLINT," + "TITLE      SMALLINT," + "BIRTHDAY   DATE" + ")";

    private static final String ADDRESS_TABLE = "CREATE table APP.ADDRESS (" + "ID         INTEGER REFERENCES APP.GENERAL(ID) " + "           ON DELETE CASCADE," + "ADDRESS1   VARCHAR(30)," + "ADDRESS2   VARCHAR(30)," + "CITY       INTEGER," + "STATE      INTEGER," + "POSTALCODE INTEGER," + "COUNTRY    INTEGER" + ")";

    private static final String COUNTRY_TABLE = "CREATE table APP.COUNTRY (" + "ID         INTEGER NOT NULL " + "           PRIMARY KEY GENERATED ALWAYS AS IDENTITY " + "           (START WITH 1, INCREMENT BY 1)," + "COUNTRY    VARCHAR(30) UNIQUE" + ")";

    private static final String STATE_TABLE = "CREATE table APP.STATE (" + "ID         INTEGER NOT NULL " + "           PRIMARY KEY GENERATED ALWAYS AS IDENTITY " + "           (START WITH 1, INCREMENT BY 1)," + "COUNTRY    INTEGER REFERENCES APP.COUNTRY(ID) " + "           ON DELETE CASCADE," + "STATE      VARCHAR(30)" + ")";

    private static final String CITY_TABLE = "CREATE table APP.CITY (" + "ID         INTEGER NOT NULL " + "           PRIMARY KEY GENERATED ALWAYS AS IDENTITY " + "           (START WITH 1, INCREMENT BY 1)," + "STATE      INTEGER REFERENCES APP.STATE(ID) " + "           ON DELETE CASCADE," + "CITY       VARCHAR(30)" + ")";

    private static final String POSTALCODE_TABLE = "CREATE table APP.POSTALCODE (" + "ID         INTEGER NOT NULL " + "           PRIMARY KEY GENERATED ALWAYS AS IDENTITY " + "           (START WITH 1, INCREMENT BY 1)," + "POSTALCODE VARCHAR(20) UNIQUE" + ")";

    private static final String PHONE_TABLE = "CREATE table APP.PHONE (" + "ID         INTEGER REFERENCES APP.GENERAL(ID) " + "           ON DELETE CASCADE," + "PHONE      VARCHAR(20)," + "TYPE       SMALLINT" + ")";

    private static final String WEBADDRESS_TABLE = "CREATE table APP.WEBADDRESS (" + "ID         INTEGER REFERENCES APP.GENERAL(ID) " + "           ON DELETE CASCADE," + "ADDRESS    VARCHAR(30)," + "TYPE       SMALLINT" + ")";

    private static final String userHomeDir = System.getProperty("user.home", ".");

    private static final String systemDir = userHomeDir + java.io.File.separator + ".alphahr";

    public Database(String database) {
        super();
        this.database = database;
        System.setProperty("derby.system.home", systemDir);
        java.io.File fileSystemDir = new java.io.File(systemDir);
        fileSystemDir.mkdir();
        dbProperties = new java.util.Properties();
        dbProperties.put("user", "alphahruser");
        dbProperties.put("password", "alphahrpassword");
        dbProperties.put("derby.driver", "org.apache.derby.jdbc.EmbeddedDriver");
        dbProperties.put("derby.url", "jdbc:derby:");
        dbProperties.put("db.table", "GENERAL");
        dbProperties.put("db.schema", "APP");
        try {
            Class.forName(dbProperties.getProperty("derby.driver"));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        String dbLocation = systemDir + java.io.File.separator + database;
        java.io.File dbFileDir = new java.io.File(dbLocation);
        if (!dbFileDir.exists()) {
            createDatabase();
        }
    }

    public boolean connect() {
        String dbUrl = dbProperties.getProperty("derby.url") + database;
        try {
            dbConnection = java.sql.DriverManager.getConnection(dbUrl, dbProperties);
            stmtSelectGeneral = dbConnection.prepareStatement(strSelectGeneral);
            stmtSelectGeneralItem = dbConnection.prepareStatement(strSelectGeneralItem);
            stmtSelectAddress = dbConnection.prepareStatement(strSelectAddress);
            stmtSelectCountry = dbConnection.prepareStatement(strSelectCountry);
            stmtSelectState = dbConnection.prepareStatement(strSelectState);
            stmtSelectCity = dbConnection.prepareStatement(strSelectCity);
            stmtSelectPostalCode = dbConnection.prepareStatement(strSelectPostalCode);
            stmtSelectPhone = dbConnection.prepareStatement(strSelectPhone);
            stmtSelectWebAddress = dbConnection.prepareStatement(strSelectWebAddress);
            stmtInsertGeneral = dbConnection.prepareStatement(strInsertGeneral, java.sql.Statement.RETURN_GENERATED_KEYS);
            stmtInsertAddress = dbConnection.prepareStatement(strInsertAddress);
            stmtInsertCountry = dbConnection.prepareStatement(strInsertCountry);
            stmtInsertState = dbConnection.prepareStatement(strInsertState);
            stmtInsertCity = dbConnection.prepareStatement(strInsertCity);
            stmtInsertPostalCode = dbConnection.prepareStatement(strInsertPostalCode);
            stmtInsertPhone = dbConnection.prepareStatement(strInsertPhone);
            stmtInsertWebAddress = dbConnection.prepareStatement(strInsertWebAddress);
            stmtDeleteGeneral = dbConnection.prepareStatement(strDeleteGeneral);
            stmtDeleteAllPhones = dbConnection.prepareStatement(strDeleteAllPhones);
            stmtDeletePhone = dbConnection.prepareStatement(strDeletePhone);
            stmtDeleteAllWebAddresses = dbConnection.prepareStatement(strDeleteAllWebAddresses);
            stmtDeleteWebAddress = dbConnection.prepareStatement(strDeleteWebAddress);
            isConnected = dbConnection != null;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return isConnected;
    }

    public void disconnect() {
        if (isConnected) {
            String dbUrl = dbProperties.getProperty("derby.url") + database;
            dbProperties.put("shutdown", "true");
            try {
                java.sql.DriverManager.getConnection(dbUrl, dbProperties);
            } catch (java.sql.SQLException sqle) {
                sqle.printStackTrace();
            }
            isConnected = false;
        }
    }

    public static java.util.List<String> getDatabaseList() {
        java.util.ArrayList<String> dbList = new java.util.ArrayList<String>();
        java.io.File dbDir = new java.io.File(systemDir);
        if (!dbDir.exists()) {
            return dbList;
        }
        for (java.io.File file : dbDir.listFiles()) {
            if (file.isDirectory()) {
                dbList.add(file.getName());
            }
        }
        return dbList;
    }

    public boolean createDatabase() {
        boolean bCreated = false;
        java.sql.Connection connection = null;
        String dbUrl = dbProperties.getProperty("derby.url") + database;
        dbProperties.put("create", "true");
        try {
            connection = java.sql.DriverManager.getConnection(dbUrl, dbProperties);
            bCreated = createTables(connection);
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        dbProperties.remove("create");
        return bCreated;
    }

    private boolean createTables(java.sql.Connection connection) {
        boolean bCreatedTables = false;
        java.sql.Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(GENERAL_TABLE);
            statement.execute(ADDRESS_TABLE);
            statement.execute(COUNTRY_TABLE);
            statement.execute(STATE_TABLE);
            statement.execute(CITY_TABLE);
            statement.execute(POSTALCODE_TABLE);
            statement.execute(PHONE_TABLE);
            statement.execute(WEBADDRESS_TABLE);
            bCreatedTables = true;
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
        }
        return bCreatedTables;
    }

    public java.util.List<alphahr.types.General> getGeneralList() {
        java.util.List<alphahr.types.General> listEntries = new java.util.ArrayList<alphahr.types.General>(20);
        try {
            stmtSelectGeneral.clearParameters();
            java.sql.ResultSet result = stmtSelectGeneral.executeQuery();
            while (result.next()) {
                int id = result.getInt("ID");
                String lastname = result.getString("LASTNAME");
                String firstname = result.getString("FIRSTNAME");
                String middlename = result.getString("MIDDLENAME");
                int sex = result.getInt("SEX");
                int title = result.getInt("TITLE");
                java.sql.Date birthday = result.getDate("BIRTHDAY");
                alphahr.types.General entry = new alphahr.types.General(id, lastname, firstname, middlename, sex, title, birthday);
                listEntries.add(entry);
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return listEntries;
    }

    public alphahr.types.General getGeneral(int id) {
        alphahr.types.General entry = null;
        try {
            stmtSelectGeneralItem.clearParameters();
            stmtSelectGeneralItem.setInt(1, id);
            java.sql.ResultSet result = stmtSelectGeneralItem.executeQuery();
            if (result.next()) {
                String lastname = result.getString("LASTNAME");
                String firstname = result.getString("FIRSTNAME");
                String middlename = result.getString("MIDDLENAME");
                int sex = result.getInt("SEX");
                int title = result.getInt("TITLE");
                java.sql.Date birthday = result.getDate("BIRTHDAY");
                entry = new alphahr.types.General(id, lastname, firstname, middlename, sex, title, birthday);
                return entry;
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return entry;
    }

    public alphahr.types.Address getAddress(int id) {
        alphahr.types.Address entry = null;
        try {
            stmtSelectAddress.clearParameters();
            stmtSelectAddress.setInt(1, id);
            java.sql.ResultSet result = stmtSelectAddress.executeQuery();
            if (result.next()) {
                String address1 = result.getString("ADDRESS1");
                String address2 = result.getString("ADDRESS2");
                String city = result.getString("CITY");
                String state = result.getString("STATE");
                String postalcode = result.getString("POSTALCODE");
                String country = result.getString("COUNTRY");
                entry = new alphahr.types.Address(address1, address2, city, state, postalcode, country);
                return entry;
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return entry;
    }

    public java.util.List<String> getCountryList() {
        java.util.List<String> listEntries = new java.util.ArrayList<String>(0);
        try {
            stmtSelectCountry.clearParameters();
            java.sql.ResultSet result = stmtSelectCountry.executeQuery();
            while (result.next()) {
                listEntries.add(result.getString("COUNTRY"));
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return listEntries;
    }

    public java.util.List<String> getStateList(String country) {
        java.util.List<String> listEntries = new java.util.ArrayList<String>(1);
        try {
            stmtSelectState.clearParameters();
            stmtSelectState.setString(1, country);
            java.sql.ResultSet result = stmtSelectState.executeQuery();
            while (result.next()) {
                listEntries.add(result.getString("STATE"));
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return listEntries;
    }

    public java.util.List<String> getCityList(String state) {
        java.util.List<String> listEntries = new java.util.ArrayList<String>(1);
        try {
            stmtSelectCity.clearParameters();
            stmtSelectCity.setString(1, state);
            java.sql.ResultSet result = stmtSelectCity.executeQuery();
            while (result.next()) {
                listEntries.add(result.getString("CITY"));
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return listEntries;
    }

    public java.util.List<alphahr.types.Phone> getPhoneList(int id) {
        java.util.List<alphahr.types.Phone> listEntries = new java.util.ArrayList<alphahr.types.Phone>(5);
        try {
            stmtSelectPhone.clearParameters();
            stmtSelectPhone.setInt(1, id);
            java.sql.ResultSet result = stmtSelectPhone.executeQuery();
            while (result.next()) {
                String phone = result.getString("PHONE");
                int type = result.getInt("TYPE");
                alphahr.types.Phone entry = new alphahr.types.Phone(phone, type);
                if (!phone.equals("")) {
                    listEntries.add(entry);
                }
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return listEntries;
    }

    public java.util.List<alphahr.types.WebAddress> getWebAddressList(int id) {
        java.util.List<alphahr.types.WebAddress> listEntries = new java.util.ArrayList<alphahr.types.WebAddress>(5);
        try {
            stmtSelectWebAddress.clearParameters();
            stmtSelectWebAddress.setInt(1, id);
            java.sql.ResultSet result = stmtSelectWebAddress.executeQuery();
            while (result.next()) {
                String address = result.getString("ADDRESS");
                int type = result.getInt("TYPE");
                alphahr.types.WebAddress entry = new alphahr.types.WebAddress(address, type);
                if (!address.equals("")) {
                    listEntries.add(entry);
                }
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return listEntries;
    }

    public int insertGeneral(alphahr.types.General record) {
        int id = -1;
        try {
            stmtInsertGeneral.clearParameters();
            stmtInsertGeneral.setString(1, record.getLastName());
            stmtInsertGeneral.setString(2, record.getFirstName());
            stmtInsertGeneral.setString(3, record.getMiddleName());
            stmtInsertGeneral.setInt(4, record.getSex());
            stmtInsertGeneral.setInt(5, record.getTitle());
            stmtInsertGeneral.setDate(6, record.getBirthday());
            int rowCount = stmtInsertGeneral.executeUpdate();
            java.sql.ResultSet results = stmtInsertGeneral.getGeneratedKeys();
            if (results.next()) {
                id = results.getInt(1);
            }
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return id;
    }

    public boolean insertAddress(int id, alphahr.types.Address record) {
        boolean bInsert = false;
        try {
            stmtInsertAddress.clearParameters();
            stmtInsertAddress.setInt(1, id);
            stmtInsertAddress.setString(2, record.getAddress1());
            stmtInsertAddress.setString(3, record.getAddress2());
            stmtInsertAddress.setString(4, record.getCity());
            stmtInsertAddress.setString(5, record.getState());
            stmtInsertAddress.setString(6, record.getCountry());
            stmtInsertAddress.setString(7, record.getState());
            stmtInsertAddress.setString(8, record.getCountry());
            stmtInsertAddress.setString(9, record.getPostalCode());
            stmtInsertAddress.setString(10, record.getCountry());
            stmtInsertAddress.executeUpdate();
            bInsert = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bInsert;
    }

    public boolean insertCountry(String record) {
        boolean bInsert = false;
        try {
            stmtInsertCountry.clearParameters();
            stmtInsertCountry.setString(1, record);
            stmtInsertCountry.setString(2, record);
            stmtInsertCountry.executeUpdate();
            bInsert = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bInsert;
    }

    public boolean insertState(String country, String state) {
        boolean bInsert = false;
        try {
            stmtInsertState.clearParameters();
            stmtInsertState.setString(1, country);
            stmtInsertState.setString(2, state);
            stmtInsertState.setString(3, country);
            stmtInsertState.setString(4, state);
            stmtInsertState.executeUpdate();
            bInsert = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bInsert;
    }

    public boolean insertCity(String country, String state, String city) {
        boolean bInsert = false;
        try {
            stmtInsertCity.clearParameters();
            stmtInsertCity.setString(1, state);
            stmtInsertCity.setString(1, country);
            stmtInsertCity.setString(3, city);
            stmtInsertCity.setString(4, state);
            stmtInsertCity.setString(5, city);
            stmtInsertCity.executeUpdate();
            bInsert = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bInsert;
    }

    public boolean insertPostalCode(String record) {
        boolean bInsert = false;
        try {
            stmtInsertPostalCode.clearParameters();
            stmtInsertPostalCode.setString(1, record);
            stmtInsertPostalCode.setString(2, record);
            stmtInsertPostalCode.executeUpdate();
            bInsert = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bInsert;
    }

    public boolean insertPhone(int id, alphahr.types.Phone record) {
        boolean bInsert = false;
        try {
            stmtInsertPhone.clearParameters();
            stmtInsertPhone.setInt(1, id);
            stmtInsertPhone.setString(2, record.getPhone());
            stmtInsertPhone.setInt(3, record.getType());
            stmtInsertPhone.executeUpdate();
            bInsert = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bInsert;
    }

    public boolean insertWebAddress(int id, alphahr.types.WebAddress record) {
        boolean bInsert = false;
        try {
            stmtInsertWebAddress.clearParameters();
            stmtInsertWebAddress.setInt(1, id);
            stmtInsertWebAddress.setString(2, record.getAddress());
            stmtInsertWebAddress.setInt(3, record.getType());
            stmtInsertWebAddress.executeUpdate();
            bInsert = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bInsert;
    }

    public boolean deleteGeneral(int id) {
        boolean bDeleted = false;
        try {
            stmtDeleteGeneral.clearParameters();
            stmtDeleteGeneral.setInt(1, id);
            stmtDeleteGeneral.executeUpdate();
            bDeleted = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bDeleted;
    }

    public boolean deleteAllPhones(int id) {
        boolean bDeleted = false;
        try {
            stmtDeleteAllPhones.clearParameters();
            stmtDeleteAllPhones.setInt(1, id);
            stmtDeleteAllPhones.executeUpdate();
            bDeleted = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bDeleted;
    }

    public boolean deletePhone(int id, alphahr.types.Phone record) {
        boolean bDeleted = false;
        try {
            stmtDeletePhone.clearParameters();
            stmtDeletePhone.setInt(1, id);
            stmtDeletePhone.setString(1, record.getPhone());
            stmtDeletePhone.executeUpdate();
            bDeleted = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bDeleted;
    }

    public boolean deleteAllWebAddresses(int id) {
        boolean bDeleted = false;
        try {
            stmtDeleteAllWebAddresses.clearParameters();
            stmtDeleteAllWebAddresses.setInt(1, id);
            stmtDeleteAllWebAddresses.executeUpdate();
            bDeleted = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bDeleted;
    }

    public boolean deleteWebAddress(int id, alphahr.types.WebAddress record) {
        boolean bDeleted = false;
        try {
            stmtDeleteWebAddress.clearParameters();
            stmtDeleteWebAddress.setInt(1, id);
            stmtDeleteWebAddress.setString(1, record.getAddress());
            stmtDeleteWebAddress.executeUpdate();
            bDeleted = true;
        } catch (java.sql.SQLException sqle) {
            sqle.printStackTrace();
        }
        return bDeleted;
    }
}
