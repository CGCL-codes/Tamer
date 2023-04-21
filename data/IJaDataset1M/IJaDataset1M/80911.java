package fab.formatic.backend.dao.impl;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import fab.formatic.backend.dao.FabBaseDao;
import fab.formatic.backend.dao.FabTariffDao;
import fab.formatic.backend.domain.FabModule;
import fab.formatic.backend.domain.FabPackage;
import fab.formatic.backend.domain.FabService;
import fab.formatic.backend.domain.FabTariff;
import fab.formatic.backend.util.FabBackendHelper;

/**
 * @author gilang
 * 
 */
@Repository(FabBackendHelper.TARIFF_DAO)
public class FabTariffDaoImpl extends FabBaseDao<FabTariff, Integer> implements FabTariffDao {

    public FabTariffDaoImpl() {
        super();
    }

    @SuppressWarnings("unchecked")
    public List<FabTariff> findByFabService(FabService service) {
        Query q = entityManager.createQuery("select t from FabTariff t where t.fabService=:fabService");
        q.setParameter("fabService", service);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<FabTariff> findByFabPackage(FabPackage pack) {
        Query q = entityManager.createQuery("select t from FabTariff t where t.fabPackage=:fabPackage");
        q.setParameter("fabPackage", pack);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<FabTariff> findByFabModule(FabModule module) {
        Query q = entityManager.createQuery("select t from FabTariff t where t.fabModule=:fabModule");
        q.setParameter("fabModule", module);
        return q.getResultList();
    }

    public FabTariff findByLastDate(FabService service) {
        Query q = entityManager.createQuery("select t from FabTariff t where t.fabService=:fabService" + " order by t.salesEnd desc");
        q.setParameter("fabService", service).setMaxResults(1);
        return (FabTariff) q.getSingleResult();
    }

    public FabTariff findByLastDate(FabPackage pack) {
        Query q = entityManager.createQuery("select t from FabTariff t where t.fabPackage=:fabPackage" + " order by t.salesEnd desc");
        q.setParameter("fabPackage", pack).setMaxResults(1);
        return (FabTariff) q.getSingleResult();
    }

    public FabTariff findByLastDate(FabModule module) {
        Query q = entityManager.createQuery("select t from FabTariff t where t.fabModule=:fabModule" + " order by t.salesEnd desc");
        q.setParameter("fabModule", module).setMaxResults(1);
        return (FabTariff) q.getSingleResult();
    }
}
