package ao.com.bna.dao;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import ao.com.bna.beans.TipoGarantia;

public class TipoGarantiaDao extends HibernateDaoSupport {

    /**
	 * @see Metodo que lista.
	 */
    public List<TipoGarantia> listar() {
        return getHibernateTemplate().loadAll(TipoGarantia.class);
    }

    /**
	 * @see Metodo para obtencao de um registo passando como parametro o id.
	 */
    public TipoGarantia obterPorID(Long id) {
        return (TipoGarantia) getHibernateTemplate().get(TipoGarantia.class, id);
    }

    /**
	 * @see Metodo que actualiza o registo na tabela 
	 */
    public void gravar(TipoGarantia info) {
        getHibernateTemplate().saveOrUpdate(info);
    }

    /**
	 * @see Metodo que remove o registo na tabela
	 */
    public void remover(TipoGarantia info) {
        getHibernateTemplate().delete(info);
    }
}
