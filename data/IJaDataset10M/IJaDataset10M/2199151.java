package com.businesslayerbuilder.dao;

import java.sql.SQLException;
import java.util.Collection;
import com.businesslayerbuilder.transferobject.TransferObject;

/**
 * Classe utilizada para tarefas de acesso a banco de dados.
 * @author Samuel Tauil
 * @param <T> O tipo do DAO
 */
public interface DAO<T extends DAO<T>> {

    /**
     * Gera um objeto DAO clone deste Objeto.
     * @return Um clone do DAO.
     * @see DAO
     */
    public abstract T copy();

    /**
     * Compara dois objetos DAO para estabelecer uma rela��o de ordem entre eles.
     * @param other_dao
     * @return um n�mero inteiro indicando se este item est� abaixo, igual ou acima em compara��o com o objeto
     *         passado como parametro.
     */
    public abstract int compareTo(T other_dao);

    /**
     * Este m�todo deve executar um SELECT e retornar uma lista com objetos
     * @return .
     * @throws SQLException Em exce��es que podem ocorrer em comunica��o com a base de dados, durante execu��o
     *             da query ou durante a leitura dos dados.
     * @see Collection
     */
    public abstract Collection<TransferObject> select() throws SQLException;

    /**
     * Faz a inser��o de v�rias entidades na base de dados.
     * @param entities lista com as entidades a serem inseridas na base de dados.
     * @throws SQLException Em exce��es que podem ocorrer em comunica��o com a base de dados ou durante
     *             execu��o do comando insert.
     */
    public abstract void insert(TransferObject... entities) throws SQLException;

    /**
     * Faz a exclus�o de v�rias entidades na base de dados.
     * @param entities lista com as entidades a serem excluidas na base de dados.
     * @throws SQLException Em exce��es que podem ocorrer em comunica��o com a base de dados ou durante
     *             execu��o do comando delete.
     */
    public abstract void delete(TransferObject... entities) throws SQLException;
}
