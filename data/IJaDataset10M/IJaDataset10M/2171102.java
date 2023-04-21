package com.hy.erp.inventory.dao.interfaces;

import com.hy.enterprise.framework.persistence.api.IDao;
import com.hy.erp.inventory.dao.PutDepotTypeDao;
import com.hy.erp.inventory.pojo.PutDepotType;
import com.hy.erp.inventory.pojo.interfaces.IPutDepotType;
import com.hy.framework.lang.annotation.ImplementsBy;

/**
 * 
 * <ul>
 * <li>开发作者：汤莉</li>
 * <li>设计日期：2010-10-13；时间：上午10:35:03</li>
 * <li>类型名称：IPutDepotTypeDao</li>
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
@ImplementsBy(PutDepotTypeDao.class)
public interface IPutDepotTypeDao extends IDao<PutDepotType> {
}
