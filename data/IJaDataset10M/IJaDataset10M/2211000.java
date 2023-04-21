package br.com.sse.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 
 * Retorna uma conex�o com o Banco de Dados
 * 
 * @author Thiago Moura
 *
 */
public class ConnectionUtility {

    public static Connection geraConnection() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://smac.4java.ca:5432/smac_estoque", "smac_database2", "sm4cdb");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
