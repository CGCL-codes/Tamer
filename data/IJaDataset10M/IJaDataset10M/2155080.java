package modelo.dao;

/**
 *
 * @author Cinthia
 */
public interface DAO<T> {

    void criar(T t) throws Exception;

    void alterar(T t) throws Exception;

    void excluir(T t) throws Exception;
}
