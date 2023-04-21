package br.gov.demoiselle.constel.persistence.dao.implementation;

import java.util.List;
import br.gov.component.demoiselle.hibernate.filter.dao.HibernateFilterGenericDAO;
import br.gov.demoiselle.constel.bean.Supervisao;
import br.gov.demoiselle.constel.persistence.dao.ISupervisaoDAO;
import br.gov.demoiselle.constel.persistence.dao.filter.FiltroSupervisao;
import br.gov.framework.demoiselle.util.page.Page;
import br.gov.framework.demoiselle.util.page.PagedResult;

/** *@Author Framework SERPRO */
public class SupervisaoDAO extends HibernateFilterGenericDAO<Supervisao> implements ISupervisaoDAO {

    public PagedResult<Supervisao> listarSupervisao(Page page) {
        FiltroSupervisao f = new FiltroSupervisao();
        page = (page == null) ? new Page(10) : page;
        PagedResult<Supervisao> results = find(f, page);
        if (results != null && results.getTotalResults() > 0) {
            return results;
        }
        return null;
    }

    public Supervisao buscarSupervisao(Supervisao arg0, String condition) {
        FiltroSupervisao f = new FiltroSupervisao();
        Object value = (long) arg0.getId();
        if (condition.equals(FiltroSupervisao.NOME)) {
            value = (String) arg0.getNome();
        }
        f.addEquals(condition, value);
        List<Supervisao> results = find(f);
        if (results != null && results.size() > 0) {
            return results.get(0);
        }
        return null;
    }
}
