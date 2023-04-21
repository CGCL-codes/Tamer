package br.com.lawoffice.persistencia.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.lawoffice.dominio.Conta;
import br.com.lawoffice.dominio.HistoricoConta;
import br.com.lawoffice.persistencia.HistoricoContaDao;

/**
 * classe de implementacao para o {@link HistoricoContaDao} utilizando tecnologia EJB 3.1
 * 
 * @author robson
 *
 */
@Stateless
@Local(HistoricoContaDao.class)
public class HistoricoContaDaoBean extends BaseDaoBean implements HistoricoContaDao {

    @Override
    public List<HistoricoConta> getHistoricosConta(Date date, Conta conta) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HistoricoConta> criteriaQuery = criteriaBuilder.createQuery(HistoricoConta.class);
        Root<HistoricoConta> root = criteriaQuery.from(HistoricoConta.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.equal(root.get("dataTransacao").as(Date.class), date), criteriaBuilder.equal(root.get("conta"), conta))).orderBy(criteriaBuilder.asc(root.get("dataTransacao").as(Date.class)));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<HistoricoConta> getHistoricosConta(Date dataInicial, Date dataFinal, Conta conta) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HistoricoConta> criteriaQuery = criteriaBuilder.createQuery(HistoricoConta.class);
        Root<HistoricoConta> root = criteriaQuery.from(HistoricoConta.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.between(root.get("dataTransacao").as(Date.class), dataInicial, dataFinal), criteriaBuilder.equal(root.get("conta"), conta))).orderBy(criteriaBuilder.asc(root.get("dataTransacao").as(Date.class)));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
