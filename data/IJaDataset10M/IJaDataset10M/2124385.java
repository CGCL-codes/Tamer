package dbConnections;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import dbStruct.Column;
import dbStruct.Connector;
import dbStruct.Connectors;
import dbStruct.DatabaseStructure;
import dbStruct.Node;
import dbStruct.Nodes;
import dbStruct.Table;
import dbStruct.Connector.Cardinality;
import dbStruct.Connector.RelationType;

public class MySQLConnection extends DBConnection {

    private static MySQLConnection instance;

    private DatabaseStructure dbStruct;

    private Nodes nodes;

    private Connectors connectors;

    public static MySQLConnection getInstance() {
        if (instance == null) instance = new MySQLConnection();
        return instance;
    }

    public MySQLConnection() {
        super(DBConnection.mySQL);
        dbStruct = DatabaseStructure.getInstance();
        nodes = dbStruct.getNodes();
        connectors = dbStruct.getConnectors();
    }

    @Override
    protected void getRelations() {
        DatabaseMetaData db;
        try {
            db = connection.getMetaData();
            Vector<Node> tables = nodes.getTables();
            for (int i = 0; i < tables.size(); i++) {
                Table table = (Table) tables.get(i);
                ResultSet rs = db.getImportedKeys(this.connection.getCatalog(), null, table.getName());
                while (rs.next()) {
                    String pkIndexName = rs.getString("PKCOLUMN_NAME");
                    System.out.println("PKCOLUMN:" + pkIndexName);
                    String fkIndexName = rs.getString("FKCOLUMN_NAME");
                    System.out.println("FKCOLUMN:" + fkIndexName);
                    String pkTableName = rs.getString("PKTABLE_NAME");
                    System.out.println("PKTABLE :" + pkTableName);
                    String fkTableName = rs.getString("FKTABLE_NAME");
                    System.out.println("FKTABLE :" + fkTableName);
                    String pkColumnName = pkTableName + "." + pkIndexName;
                    System.out.println("pkColumn :" + pkColumnName);
                    Column pkColumn = (Column) nodes.get(pkColumnName);
                    String fkColumnName = fkTableName + "." + fkIndexName;
                    System.out.println("fkColumn :" + fkColumnName);
                    Column fkColumn = (Column) nodes.get(fkColumnName);
                    Cardinality card = Cardinality.NONE;
                    System.out.println(pkColumnName + " <- " + fkColumnName);
                    boolean pkIdx = ((Column) nodes.get(pkColumnName)).isPrimary();
                    boolean fkIdx = ((Column) nodes.get(fkColumnName)).isPrimary();
                    if (pkIdx & fkIdx) card = Cardinality.ONE2ONE;
                    if (pkIdx & !fkIdx) card = Cardinality.ONE2MANY;
                    if (!pkIdx & fkIdx) card = Cardinality.MANY2ONE;
                    if (!pkIdx & !fkIdx) card = Cardinality.MANY2MANY;
                    Table pkTable = (Table) nodes.get(pkTableName);
                    Table fkTable = (Table) nodes.get(fkTableName);
                    Connector conn = new Connector(pkTable, fkTable, RelationType.RELATION, card);
                    connectors.add(conn);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String[] getDatabases(String host, String user, String pwd) {
        this.host = host;
        this.user = user;
        this.pwd = pwd;
        Vector<String> databases = new Vector<String>();
        Connection c = this.getConnection();
        Statement s;
        try {
            s = c.createStatement();
            ResultSet rs = s.executeQuery("Show Databases");
            while (rs.next()) {
                databases.add((String) rs.getObject(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        int n = databases.size();
        String[] retDB = new String[n];
        for (int i = 0; i < n; i++) {
            retDB[i] = databases.get(i);
        }
        return retDB;
    }

    public ResultSet getResultSet(String qry) {
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(qry);
        } catch (SQLException e) {
            System.out.println("ERRORE NELLA CREAZIONE DELLA QUERY");
        }
        return rs;
    }

    public static String getTypeName(int type) {
        return map.get(type);
    }
}
