package com.hy.erp.inventory.service;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import com.hy.enterprise.architecture.foundation.ArchitectureFoundationException;
import com.hy.enterprise.framework.service.business.AbstractBusinessService;
import com.hy.erp.inventory.dao.NationDao;
import com.hy.erp.inventory.dao.interfaces.INationDao;
import com.hy.erp.inventory.pojo.Nation;
import com.hy.erp.inventory.pojo.interfaces.INation;
import com.hy.erp.inventory.service.interfaces.INationService;

@Component("nationService")
public class NationService extends AbstractBusinessService<NationDao> implements INationService {

    private INationDao nationDao = null;

    @Resource(name = "nationDao")
    public void setNationDao(INationDao nationDao) {
        this.nationDao = nationDao;
    }

    @Override
    public boolean addNationalities(Nation nationalities) {
        if (null == nationalities) {
            return true;
        } else {
            return nationDao.persist(nationalities);
        }
    }

    @Override
    public boolean modifyNationalities(Nation nationalities) {
        if ((null == nationalities) || (null == nationalities.getIdentifier()) || (nationalities.getIdentifier().trim().length() == 0)) {
            return true;
        } else {
            if (null == nationalities) {
                throw new ArchitectureFoundationException("修改实体数据时发生错误，所需要修改的实体在数据库中并不存在");
            }
            return (nationDao.merge(nationalities) != null);
        }
    }

    @Override
    public Integer removeNationalities(String[] nationalitiesIds) {
        if ((null == nationalitiesIds) || (nationalitiesIds.length == 0)) {
            return new Integer(0);
        } else {
            return nationDao.remove(nationalitiesIds);
        }
    }

    @Override
    public String[] addNationalities(Object[] nationalities) {
        if (null == nationalities) {
            return null;
        } else {
            Nation[] nationalitiess = new Nation[nationalities.length];
            for (int i = 0; i < nationalities.length; i++) {
                Nation nationalities2 = (Nation) nationalities[i];
                nationalitiess[i] = nationalities2;
            }
            return nationDao.persist(nationalitiess);
        }
    }

    @Override
    public List<Nation> getAllNationalities() {
        return nationDao.find();
    }

    @Override
    public String[] modifyNationalities(Object[] objects) {
        if (null == objects) {
            return null;
        } else {
            Nation[] types = new Nation[objects.length];
            for (int i = 0; i < objects.length; i++) {
                Nation type = (Nation) objects[i];
                types[i] = type;
            }
            return nationDao.merge(types);
        }
    }
}
