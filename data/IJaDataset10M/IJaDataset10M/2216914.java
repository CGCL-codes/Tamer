package senai.cronos.database.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import senai.cronos.util.Observado;

/**
 * 
 * Interface de um objeto de acesso a dados, com metodo de manipulacao de uma tabela
 * de um banco.
 * 
 * @author Sergio Lisan e Carlos Melo
 */
public interface DAO<T> extends Observado {

    /**
     * adiciona um elemento ao banco de dados
     * @param u 
     */
    void add(T u) throws SQLException;

    /**
     * remove um elemento do banco de dados
     * @param id 
     */
    void remove(Serializable id) throws SQLException;

    /**
     * altera um elemento do banco de dados
     * @param u 
     */
    void update(T u) throws SQLException;

    /**
     * retorna um objeto idenfiticado por sua id
     * @param id
     * @return
     * @throws Exception 
     */
    T get(Serializable id) throws SQLException;

    /**
     * lista elementos de uma tabela do banco de dados
     * @return 
     */
    List<T> get() throws SQLException;

    /**
     * abre a conexao com o banco de dados
     * @throws SQLException 
     */
    void open() throws SQLException;

    /**
     * fecha a conexao com o banco de dados
     * @throws SQLException 
     */
    void close() throws SQLException;
}
