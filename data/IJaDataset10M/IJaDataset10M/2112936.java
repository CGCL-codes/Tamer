package com.hy.erp.inventory.dao.interfaces;

import com.hy.enterprise.framework.persistence.api.IDao;
import com.hy.erp.inventory.dao.WorkingDao;
import com.hy.erp.inventory.pojo.interfaces.IWorking;
import com.hy.framework.lang.annotation.ImplementsBy;

@ImplementsBy(WorkingDao.class)
public interface IWorkingDao extends IDao<IWorking> {
}
