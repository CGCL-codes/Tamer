package com.consultcare2.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.consultcare2.classe.*;
import com.consultcare2.vo.*;

/**
 * @author Domingues
 * 
 */
public class Conexao {

    private Connection connection = null;

    private Statement statement = null;

    private String url;

    private String user;

    private String senha;

    private void Dados() {
        DadosBanco dados = LeArquivo.LerDados();
        url = dados.getUrl();
        user = dados.getUser();
        senha = dados.getSenha();
    }

    public void Conectar() throws SQLException {
        Dados();
        DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        connection = DriverManager.getConnection(url, user, senha);
        statement = connection.createStatement();
    }

    public void Finalizar() throws SQLException {
        connection.close();
    }

    public Connection getConexao() {
        return connection;
    }

    public int Inserir(String cmd) throws SQLException {
        return (statement.executeUpdate(cmd));
    }

    public int Delete(String cmd) throws SQLException {
        return (statement.executeUpdate(cmd));
    }

    public int Update(String cmd) throws SQLException {
        return (statement.executeUpdate(cmd));
    }

    public ResultSet Select(String cmd) throws SQLException {
        ResultSet rs = statement.executeQuery(cmd);
        return rs;
    }
}
