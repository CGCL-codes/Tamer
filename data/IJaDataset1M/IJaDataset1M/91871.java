package br.com.helbert.financeira.persistencia.dao.banco;

import java.util.Collection;
import java.util.Iterator;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import br.com.helbert.financeira.persistencia.dao.comum.DAOImpl;
import br.com.helbert.financeira.persistencia.entidades.banco.Banco;

public class BancoDAO extends DAOImpl implements BancoDAOIF {

    @SuppressWarnings("unchecked")
    public Collection<Banco> buscaTodos() {
        Collection<Banco> cba = getHibernateTemplate().loadAll(Banco.class);
        for (Banco bc : cba) {
            System.out.println(bc.getNomeBanco());
        }
        Iterator it = cba.iterator();
        while (it.hasNext()) {
            Banco bc = (Banco) it.next();
            System.out.println(bc.getNomeBanco());
        }
        return cba;
    }
}
