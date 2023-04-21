package model.dao;

import Excessoes.ExcessaoDAO;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Alessandro
 */
public class ConexoesBD {

    private String db, login, password, url;

    public Connection getPostgres() throws ExcessaoDAO {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/tccSmartLAundry", "postgres", "postgres");
        } catch (Exception e) {
            throw new ExcessaoDAO("NÃO É POSSÍVEL ESTABELECER CONEXÃO COM BANCO DE DADOS");
        }
    }

    public ConexoesBD() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        db = "tccSmartLAundry";
        login = "postgres";
        password = "postgres";
        url = "jdbc:postgresql://localhost:5432/";
    }

    public Connection getConnection() throws Exception {
        return DriverManager.getConnection(url + db, login, password);
    }
}
