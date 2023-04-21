package com.hy.erp.inventory.dao.interfaces;

import com.hy.enterprise.framework.persistence.api.IDao;
import com.hy.erp.inventory.dao.UserDao;
import com.hy.erp.inventory.pojo.interfaces.IUser;
import com.hy.framework.lang.annotation.ImplementsBy;

/**
 * 
 * <ul>
 * <li>开发作者：汤莉</li>
 * <li>设计日期：2010-10-8；时间：下午02:19:44</li>
 * <li>类型名称：IUserDao</li>
 * <li>设计目的：表现用户数据访问对象的接口</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
@ImplementsBy(UserDao.class)
public interface IUserDao extends IDao<IUser> {
}
