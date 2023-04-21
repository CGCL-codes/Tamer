package anima.db.jdbv;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Gerencia a conexao com um banco de dados. Utiliza o pattern Singleton
 * para garantir que so sera feita uma instancia deste gerente e uma
 * conexao com o banco.
 */
public class DbVManager {

    /**
     * Nome do arquivo externo de propriedades 
     */
    public static final String DEFAULT_PROPERTIES = "dbv.properties";

    private static DbVManager instance = null;

    /**
     * Metodo fabrica que retorna uma unica instancia do banco.
     * Este metodo cria uma unica vez a instancia se ainda nao foi
     * criada.
     * 
     * @return instancia (unica) do banco
     * @throws IOException erro ao tentar carregar o arquivo de propriedades
     * @throws ClassNotFoundException biblioteca do driver do bd nao encontrada
     */
    public static DbVManager getInstance() throws IOException, ClassNotFoundException {
        if (instance == null) instance = new DbVManager();
        return instance;
    }

    private Properties dbProperties = null;

    private Connection dbConnection = null;

    private int clients = 0;

    private DbVManager() throws IOException, ClassNotFoundException {
        dbProperties = new Properties();
        dbProperties.load(DbVManager.class.getResourceAsStream(DEFAULT_PROPERTIES));
        String dbDriver = dbProperties.getProperty("db.driver");
        Class.forName(dbDriver);
    }

    /**
     * Abre (quando necessaria) uma conexao com o banco e a retorna para o cliente.
     * Mantem uma unica conexao com o bd que eh entregue para todos os clientes, 
     * e controla o numero de clientes ativos para a mesma.
     * 
     * @return conexao com o bd
     * @throws SQLException problemas ao tentar estabelecer a conexao com o bd
     */
    public Connection getConnection() throws SQLException {
        if (dbConnection == null) {
            String dbUrlPrefix = dbProperties.getProperty("db.urlprefix"), dbLocation = dbProperties.getProperty("db.location"), dbUser = dbProperties.getProperty("db.user"), dbPassword = dbProperties.getProperty("db.password");
            File location = new File(dbLocation);
            String dbUrl = dbUrlPrefix + dbLocation;
            if (!location.exists()) {
                System.out.println("Database not found; creating a new database...");
                dbUrl += ";create=true";
            }
            if (dbUser.length() != 0) {
                dbConnection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            } else {
                dbConnection = DriverManager.getConnection(dbUrl);
            }
        }
        if (dbConnection != null) clients++;
        return dbConnection;
    }

    /**
     * Cliente notifica atraves deste metodo que nao ira utilizar mais a conexao.
     * Quando todos os clientes liberarem a conexao ela eh automaticamente encerrada.
     * 
     * @throws SQLException problemas ao tentar encerrar a conexao com o bd
     */
    public void releaseConnection() throws SQLException {
        clients--;
        if (clients <= 0) {
            dbConnection.close();
            dbConnection = null;
        }
    }

    private void createDbVTables() throws SQLException {
        Statement stm = dbConnection.createStatement();
        stm.executeUpdate("CREATE TABLE DCC (" + "pvid    VARCHAR(255) NOT NULL," + "oid     VARCHAR(255)," + "dbvid   VARCHAR(255)," + "dccType VARCHAR(255)," + "physicalLocation VARCHAR(255)," + "PRIMARY KEY(pvid)");
        stm.close();
    }
}
