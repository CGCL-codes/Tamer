package utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class SQLiteDatabaseConnector implements Serializable {

    String path;

    File file;

    Connection conn = null;

    public SQLiteDatabaseConnector(String string) {
        this.path = string;
        this.file = new File(string);
    }

    private void initConnection() throws ClassNotFoundException, SQLException, IOException {
        if (!file.exists()) file.createNewFile(); else file.delete();
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + file.getCanonicalPath());
    }

    public void initDB() throws SQLException, ClassNotFoundException, IOException {
        initConnection();
        Statement statem = conn.createStatement();
        statem.execute("CREATE TABLE autores(dni CHAR PRIMARY KEY , nombre CHAR, trabajos CHAR );");
        statem.execute("CREATE TABLE ponentes(dni CHAR PRIMARY KEY , nombre CHAR, trabajos CHAR );");
    }

    public void reinitDB() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:" + file.getCanonicalPath());
    }

    public void insertaautor(String DNI, String nombre, String titulo) throws SQLException {
        Statement statem = conn.createStatement();
        statem.execute("INSERT INTO autores (dni,nombre,trabajos) values ('" + DNI + "', '" + nombre + "', '" + titulo + "' );");
    }

    public void insertaponente(String DNI, String nombre, String titulo) throws SQLException {
        Statement statem = conn.createStatement();
        statem.execute("INSERT INTO ponentes (dni,nombre,trabajos) values ('" + DNI + "', '" + nombre + "', '" + titulo + "' );");
    }

    public ArrayList<String> pedirtrabajosporautor(String string) throws SQLException {
        return convertquerytoArraylist(doQuery("SELECT trabajos FROM autores WHERE nombre='" + string + "' ORDER BY nombre;"));
    }

    public ArrayList<String> pedirtrabajosporponente(String string) throws SQLException {
        return convertquerytoArraylist(doQuery("SELECT trabajos FROM ponentes WHERE nombre='" + string + "' ORDER BY nombre;"));
    }

    private ResultSet doQuery(String query) {
        ResultSet res = null;
        Statement statem;
        try {
            statem = conn.createStatement();
            res = statem.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void DebugDB() throws SQLException {
        System.out.println("----------Tablas existentes-----------");
        printresultset(doQuery("SELECT name FROM sqlite_master WHERE type='table' ORDER BY name;"));
        System.out.println("------Contenido tabla autores-------");
        printresultset(doQuery("SELECT nombre FROM autores  ORDER BY nombre;"));
        System.out.println("------Contenido tabla ponentes-------");
        printresultset(doQuery("SELECT nombre FROM ponentes  ORDER BY nombre;"));
    }

    private ArrayList<String> convertquerytoArraylist(ResultSet result) throws SQLException {
        ArrayList<String> ret = new ArrayList<String>();
        while (result.next()) {
            ret.add(result.getString(1));
        }
        return ret;
    }

    private void printresultset(ResultSet result) throws SQLException {
        while (result.next()) {
            System.out.println(result.getString(1));
        }
    }
}
