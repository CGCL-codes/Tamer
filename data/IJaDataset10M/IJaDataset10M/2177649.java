package com.fisoft.phucsinh.phucsinhsrv.eao;

import com.fisoft.phucsinh.phucsinhsrv.entity.ParamlistEntity;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityLockedException;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPEntityRemovedException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author vantinh
 */
@Stateless
public class ParamlistEAO implements IParamlistEAO {

    @PersistenceContext
    private EntityManager em;

    public ParamlistEntity find(Object id) {
        return em.find(ParamlistEntity.class, id);
    }

    public List<ParamlistEntity> findAll() {
        return em.createQuery("select object(o) from ParamlistEntity as o").getResultList();
    }

    public ParamlistEntity edit(ParamlistEntity eParamlist) throws ERPEntityLockedException, ERPEntityRemovedException {
        try {
            em.merge(eParamlist);
            em.flush();
        } catch (OptimisticLockException e) {
            throw new ERPEntityLockedException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ERPEntityRemovedException(e.getMessage());
        }
        return eParamlist;
    }
}
