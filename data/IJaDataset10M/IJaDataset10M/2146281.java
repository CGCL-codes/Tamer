package br.com.jnfe.core.adapter;

import br.com.jnfe.core.ProtNFe;
import br.com.jnfe.core.RetCancNFe;

/**
 * Estrat�gia para criar solicita��o e retorno de cancelamento.
 * 
 * @param <T>
 *  
 * @author Mauricio Fernandes de Castro
 */
public interface CancNFeAdapter<T> extends Ret<RetCancNFe> {

    /**
	 * Cria uma solicita��o de cancelamento.
	 * 
	 * @param protNFe
	 * @param xJust
	 */
    public T newCancNFe(ProtNFe protNFe, String xJust) throws Exception;
}
