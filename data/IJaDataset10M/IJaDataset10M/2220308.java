package org.wdcode.base.interfaces.dao.impl;

import java.util.List;
import org.wdcode.base.bean.EntityBean;
import org.wdcode.base.bean.PageBean;
import org.wdcode.base.dao.spring.hibernate.BaseHibernateDao;
import org.wdcode.base.interfaces.dao.Dao;
import org.wdcode.common.tools.Lists;

/**
 * 通用Dao的HibernateDao抽象实现
 * @author WD
 * @since JDK6
 * @version 1.0 2009-09-23
 */
public abstract class BaseDaoHibernate<E extends EntityBean> extends BaseHibernateDao implements Dao<E> {

    /**
	 * 根据ID 获得实体
	 * @param id ID
	 * @return 实体
	 */
    public E get(int id) {
        return getHibernateDao().get(getEntityClass(), id);
    }

    /**
	 * 根据传入的条件，返回唯一的实体 如果有多个返回第一个实体
	 * @param entity 实体
	 * @return 实体
	 */
    public E get(E entity) {
        return getHibernateDao().get(entity);
    }

    /**
	 * 查询全部
	 * @return 全部实体
	 */
    public List<E> queryByAll() {
        return getHibernateDao().queryByAll(getEntityClass());
    }

    /**
	 * 查询出size条实体
	 * @param size 多少行实体
	 * @return 数据实体
	 */
    public List<E> queryBySize(int size) {
        return getHibernateDao().queryByAll(getEntityClass(), 0, size);
    }

    /**
	 * 分页查询
	 * @param entity 实体
	 * @param page 分页Bean
	 * @return 列表
	 */
    public List<E> queryByPage(E entity, PageBean page) {
        return getHibernateDao().queryByExample(entity, page);
    }

    /**
	 * 根据实体条件查询
	 * @param entity 实体
	 * @param page 分页Bean
	 * @return 列表
	 */
    public List<E> queryByEntity(E entity) {
        return getHibernateDao().queryByExample(entity);
    }

    /**
	 * 添加
	 * @param entity 实体
	 * @return ID
	 */
    public int insert(E entity) {
        return getHibernateDao().insert(entity);
    }

    /**
	 * 添加
	 * @param entity 实体
	 * @return 影响行数
	 */
    public int insert(List<E> list) {
        return getHibernateDao().insertOrUpdateAll(list).size();
    }

    /**
	 * 更新
	 * @param entity 实体
	 * @return 影响的行数
	 */
    public int update(E entity) {
        getHibernateDao().update(entity);
        return 1;
    }

    /**
	 * 更新
	 * @param list 实体列表
	 * @return 影响的行数
	 */
    public int update(List<E> list) {
        return getHibernateDao().insertOrUpdateAll(list).size();
    }

    /**
	 * 删除
	 * @param entity 实体
	 * @return 影响的行数
	 */
    public int delete(E entity) {
        getHibernateDao().delete(entity);
        return 1;
    }

    /**
	 * 删除
	 * @param list 实体列表
	 * @return 影响的行数
	 */
    public int delete(List<E> list) {
        return getHibernateDao().deleteAll(list).size();
    }

    /**
	 * 根据ID删除 完全删除
	 * @param id ID
	 * @return 实体
	 */
    public E delete(int id) {
        E e = newInstance(id);
        delete(e);
        return e;
    }

    /**
	 * 根据ID数组删除 完全删除
	 * @param ids ID数组
	 * @return 实体列表
	 */
    public List<E> delete(int[] ids) {
        List<E> list = newInstance(ids);
        delete(list);
        return list;
    }

    /**
	 * 根据ID数组返回一个用户列表
	 * @param ids 用户数组
	 * @return 用户列表
	 */
    protected List<E> newInstance(int[] ids) {
        int length = ids.length;
        List<E> lsUser = Lists.getList(length);
        for (int i = 0; i < length; i++) {
            lsUser.add(newInstance(ids[i]));
        }
        return lsUser;
    }

    /**
	 * 根据ID构造一个实体
	 * @param id 主键ID
	 * @return 实体
	 */
    protected abstract E newInstance(int id);

    /**
	 * 获得子类的Class
	 * @return 子类的Class
	 */
    protected abstract Class<E> getEntityClass();
}
