package br.com.sysconstruct.base.persistencia;

import br.com.sysconstruct.base.dominio.IObjetoDominio;

/**
 *
 * @author Anderson Zanichelli
 */
public interface IObjetoPersistente extends IObjetoDominio {

    Long getId();

    Long getVersion();

    boolean isPersistente();
}
