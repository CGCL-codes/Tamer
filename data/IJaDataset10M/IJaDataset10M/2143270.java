package br.rfpm.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, Pk extends Serializable> {

    T consultar(Pk pk);

    T carregar(Pk pk);

    T inserir(T entity);

    T inserirRetornando(T entity);

    T editar(T entity);

    T salvarOuEditar(T entity);

    void excluir(T entity);

    List<T> listar();

    List<T> listar(String orderByColumn, boolean asc);

    List<T> listar(int offset, int maxResults);

    List<T> listar(int offset, int maxResults, String orderByColumn, boolean asc);

    List<T> listar(T example);

    List<T> listar(T example, String orderByColumn, boolean asc);

    List<T> listar(T example, int offset, int maxResults);

    List<T> listar(T example, int offset, int maxResults, String orderByColumn, boolean asc);

    Integer count();

    Integer count(T example);
}
