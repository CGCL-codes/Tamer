package br.com.cps.distribuidora.utils;

import java.io.Serializable;
import java.util.Collection;

public interface DAOGenerico<T, ID extends Serializable> {

    /**
	 * Retorna um único objeto, buscando pela chave primária dele
	 * 
	 * @param id
	 * @return Object
	 * @author Rudy
	 */
    public abstract T consultar(ID id);

    /**
	 * Retorna o objeto salvo, recebendo a instância dele por parâmetro
	 * 
	 * @param entidade
	 * @return Object
	 * @author Rudy
	 */
    public abstract T salvar(T entidade);

    /**
	 * Exclui um objeto do banco de dados
	 * 
	 * @param entidade
	 * @author Rudy
	 */
    public abstract void excluir(T entidade);

    /**
	 * Retorna todos os objetos de uma determinada classe, na ordem que esta no banco de dados
	 * 
	 * @return Collection - Objeto
	 * @author Rudy
	 */
    public abstract Collection<T> listarTodos();
}
