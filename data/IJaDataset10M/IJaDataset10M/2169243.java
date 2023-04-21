package com.af.financeomine.dao;

import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.af.financeomine.model.Periodo;

@Repository("com.af.financeomine.dao.PeriodoDao")
public class PeriodoDaoImpl extends DaoImpl<Periodo> implements PeriodoDao {

    private static Logger log = Logger.getLogger(PeriodoDaoImpl.class);

    public PeriodoDaoImpl() {
        super(Periodo.class);
        log.info("Call Constructor, Parameters: " + classe.getName());
    }

    @SuppressWarnings("unchecked")
    public List<Periodo> findAfterDate(Date date) {
        log.debug("Call lista, Parameters = date: " + date);
        Query q = em.createQuery("select e from com.af.financeomine.model.Periodo e where e.dataInicio > :data ");
        q.setParameter("data", date);
        List<Periodo> list = q.getResultList();
        log.debug("Result Size: " + list.size());
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Periodo> lista(Integer page, String qtype, Integer rp, String sortname, String sortorder, String query) {
        log.debug("Call lista, Parameters = page: " + page + " qtype: " + qtype + " rp: " + rp + " sortname: " + sortname + " sortorder: " + sortorder + " query: " + query);
        Query q = em.createQuery("select e from com.af.financeomine.model.Periodo e");
        if (page != null) {
            q.setFirstResult(page.intValue() - 1);
        }
        if (rp != null) {
            q.setMaxResults(rp.intValue());
        }
        List<Periodo> periodos = q.getResultList();
        log.debug("Result Size: " + periodos.size());
        return periodos;
    }
}
