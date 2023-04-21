package br.com.spb.persistence;

import br.com.spb.domain.Governador;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import br.com.spb.support.Dao;

@SuppressWarnings("unchecked")
public class GovernadorDao extends Dao<Governador> {

    /**
     * @param sigUf
     * @param codMunicipioOrigem
     * @param codPartido
     * @param nome
     * @return
     */
    public List<Governador> findByParametros(String sigUf, Long codMunicipioOrigem, Long codPartido, String nome) {
        Criteria criteria = createCriteria();
        if (sigUf != null) {
            criteria.add(Restrictions.like("sigUf", sigUf, MatchMode.ANYWHERE).ignoreCase());
        }
        if (codMunicipioOrigem != null) {
            criteria.add(Restrictions.eq("codMunicipioOrigem", codMunicipioOrigem).ignoreCase());
        }
        if (codPartido != null) {
            criteria.add(Restrictions.eq("codPartido", codPartido).ignoreCase());
        }
        if (nome != null) {
            criteria.add(Restrictions.like("nome", nome, MatchMode.ANYWHERE).ignoreCase());
        }
        criteria.addOrder(Order.asc("nome"));
        List<Governador> lista = (List<Governador>) criteria.list();
        return lista;
    }
}
