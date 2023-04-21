package com.hy.erp.inventory.dao;

import com.hy.enterprise.framework.persistence.api.MyAbstractDao;
import com.hy.erp.inventory.dao.interfaces.IDefineDepotReasonDao;
import com.hy.erp.inventory.pojo.interfaces.IDefineDepotReason;

/**
 * 
 * <ul>
 * <li>开发作者：李冰</li>
 * <li>设计日期：2010-10-13；时间：上午09:14:01</li>
 * <li>类型名称：DefineDepotReasonDao</li>
 * <li>设计目的：</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
public class DefineDepotReasonDao extends MyAbstractDao<IDefineDepotReason> implements IDefineDepotReasonDao {

    public DefineDepotReasonDao() {
        super();
    }

    public DefineDepotReasonDao(Class<IDefineDepotReason> entityClass) {
        super(entityClass);
    }
}
