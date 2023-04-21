package com.asoft.common.base.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Data Access Object (DAO) interface.   This is an interface
 * used to tag our DAO classes and to provide common methods to all DAOs.
 *
 * <p><a href="DAO.java.html"><i>View Source</i></a></p>
 *
 * @author  amon
 */
public interface DAO {

    /**
         * 新增实体
         * reutnr 新增or修改后的实体
         */
    public Object save(Object obj);

    /**
         * 新增实体s
         * reutnr 修改后的实体s
         */
    public List saveAll(List objs);

    /**
         * 修改实体
         * reutnr 新增or修改后的实体
         */
    public Object update(Object obj);

    /**
         * 修改实体s
         * reutnr 修改后的实体s
         */
    public List updateAll(List objs);

    /**
         * 批量新增or修改实体
         * reutnr 新增or修改后的实体
         */
    public List saveOrUpdateAll(List objs);

    /**
         * 删除实体
         * return 新增or修改后的实体
         */
    public void remove(Object obj);

    /**
         * 批量删除实体
         * no return
         */
    public void removeAll(List objs);

    /**
         * 清空所有数据
         * @warn:　用于单元测试用,危险的api
         */
    public void clearAll();

    /**
         * 查询byid
         */
    public Object get(Serializable id);

    /**
         * 查询byCode
         */
    public Object getByCode(String code);

    /**
         * 作废然后新增
         *    用于维护记录的所有版本而使用的
         */
    public Object blankOutThenSave(Object obj);

    /**
         * 清楚session缓存
         */
    public void evict(Object obj);
}
