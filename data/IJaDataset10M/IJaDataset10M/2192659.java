package ao.com.bna.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import ao.com.bna.beans.Produto;

public class ProdutoDao extends HibernateDaoSupport {

    /**
	 * @see Metodo que lista.
	 */
    public List<Produto> listar() {
        return getHibernateTemplate().loadAll(Produto.class);
    }

    /**
	 * @see Metodo para obten��o de um registo passando como parametro o id.
	 */
    public Produto obterPorID(Long id) {
        return (Produto) getHibernateTemplate().get(Produto.class, id);
    }

    /**
	 * @see Metodo que actualiza o registo na tabela 
	 */
    public void gravar(Produto info) {
        getHibernateTemplate().saveOrUpdate(info);
    }

    /**
	 * @see Metodo que remove o registo na tabela
	 */
    public void remover(Produto info) {
        getHibernateTemplate().delete(info);
    }
}
