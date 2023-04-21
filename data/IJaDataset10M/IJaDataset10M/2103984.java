package Dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Tiago Peres
 */
public interface Dao<T> {

    int salvar(T object) throws SQLException;

    void excluir(int id) throws SQLException;

    void editar(T object) throws SQLException;

    T recuperarPorId(int id) throws SQLException;

    Connection getConexao();

    void setConexao(Connection conexao);
}
