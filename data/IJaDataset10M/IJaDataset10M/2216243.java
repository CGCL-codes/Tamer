package ao.com.bna.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import ao.com.bna.beans.BoleanDesconto;

/**
 * @see Autor Maria De Fatima Gomes
 * @see Data : 06/09/2011 - BNA
 * @see com.bna.dao.BoleanDesconto#save(java.io.Serializable)
 * @see @see Classe na qual se encontram todos os metodos a serem ivocados e as poss�veis queries
 */
public class BoleanDescontoDao extends HibernateDaoSupport {

    /**
	 * @see Metodo que lista os BoleanDesconto de ocorrencias.
	 */
    public List<BoleanDesconto> listar() {
        return getHibernateTemplate().loadAll(BoleanDesconto.class);
    }

    /**
	 * @see Metodo para obtencao de um registo passando como parametro o id.
	 */
    public BoleanDesconto obterPorID(Long idBoleanDesconto) {
        return (BoleanDesconto) getHibernateTemplate().get(BoleanDesconto.class, idBoleanDesconto);
    }

    /**
	 * @see Metodo que actualiza o registo na tabela BoleanDesconto
	 */
    public void gravar(BoleanDesconto boleanDesconto) {
        getHibernateTemplate().save(boleanDesconto);
    }

    public void actualiza(BoleanDesconto boleanDesconto) {
        getHibernateTemplate().merge(boleanDesconto);
    }

    /**
	 * @see Metodo que remove o registo na tabela BoleanDesconto
	 */
    public void remover(BoleanDesconto boleanDesconto) {
        getHibernateTemplate().delete(boleanDesconto);
    }
}
