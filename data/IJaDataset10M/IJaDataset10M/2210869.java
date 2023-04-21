package br.org.pizzaria.dao;

import java.util.List;
import br.org.pizzaria.beans.Produto;

public interface ProdutoDAO {

    /**
	 * 
	 * @param produto
	 * @return
	 */
    public List<Produto> pesquisar(Produto produto);
}
