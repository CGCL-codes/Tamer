package br.com.sysconstruct.base.persistencia;

import java.util.List;

/**
 *
 * @author Anderson Zanichelli
 */
public interface IBaseDao<T extends IObjetoPersistente> {

    long recuperarContagem();

    List<T> list();

    List<T> list(int[] intervalo);

    T locate(Long id);

    T insert(T object);

    void delete(T object);
}
