package com.hy.erp.inventory.dao;

import org.springframework.stereotype.Component;
import com.hy.enterprise.framework.persistence.api.MyAbstractDao;
import com.hy.erp.inventory.dao.interfaces.IOrganizationTypeDao;
import com.hy.erp.inventory.pojo.OrganizationType;
import com.hy.erp.inventory.pojo.interfaces.IOrganizationType;

/**
 * 
 * <ul>
 * <li>开发作者：李冰</li>
 * <li>设计日期：2010-10-13；时间：上午10:39:18</li>
 * <li>类型名称：OrganizationTypeDao</li>
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
@Component("organizationTypeDao")
public class OrganizationTypeDao extends MyAbstractDao<OrganizationType> implements IOrganizationTypeDao {

    public OrganizationTypeDao() {
        super();
    }
}
