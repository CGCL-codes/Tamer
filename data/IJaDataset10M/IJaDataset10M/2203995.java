package com.hy.erp.inventory.dao.interfaces;

import com.hy.enterprise.framework.persistence.api.IDao;
import com.hy.erp.inventory.dao.AreaDao;
import com.hy.erp.inventory.pojo.interfaces.IArea;
import com.hy.framework.lang.annotation.ImplementsBy;

/**
 * 
 * <ul>
 * <li>开发作者：李冰</li>
 * <li>设计日期：2010-10-12；时间：下午03:47:10</li>
 * <li>类型名称：IAreaDao</li>
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
@ImplementsBy(AreaDao.class)
public interface IAreaDao extends IDao<IArea> {
}
