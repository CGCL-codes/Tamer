package br.com.caelum.stella.gateway.core;

/**
 * Interface que deve ser implementada por toda classe que queira
 * fazer uma integra��o.
 * @author Alberto Pc
 *
 * @param <T> Como as integra��es podem gerar diferentes retornos, o mesmo � especificado pela classe que implementa
 */
public interface IntegrationHandler<T> {

    public T handle();
}
