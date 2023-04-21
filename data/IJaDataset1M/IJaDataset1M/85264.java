package com.hy.erp.inventory.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import com.hy.enterprise.framework.persistence.PersistentException;
import com.hy.enterprise.framework.persistence.api.MyAbstractDao;
import com.hy.erp.inventory.dao.interfaces.IQualityControlMannerDao;
import com.hy.erp.inventory.pojo.interfaces.IQualityControlManner;

/**
 * 
 * <ul>
 * <li>开发作者：汤莉</li>
 * <li>设计日期：2010-10-13；时间：上午09:36:56</li>
 * <li>类型名称：QualityControlMannerDao</li>
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
public class QualityControlMannerDao extends MyAbstractDao<IQualityControlManner> implements IQualityControlMannerDao {

    public QualityControlMannerDao() {
        super();
    }

    public QualityControlMannerDao(Class<IQualityControlManner> entityClass) {
        super(entityClass);
    }
}
