package com.hdy.mapper;

import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 * 
 * @author uncleja
 * @since 2011-11-18 下午01:55:39
 * @comment BaseSqlMapper继承SqlMapper，对Mapper进行接口封装，提供常用的增删改查组件；
 * 也可以对该接口进行扩展和封装,该接口继承SqlMapper接口，但是该接口没有MyBatis的mapper实现。
 * 需要我们自己的业务mapper继承这个接口，完成上面的方法的实现.
 * @version 1.0
 */
public interface BaseMapper<T> extends SqlMapper {

    /**
	 * @comment 添加实体
	 * @param entity
	 * @throws DataAccessException
	 */
    public void add(T entity) throws DataAccessException;

    /**
	 * @comment 编辑实体
	 * @param entity
	 * @throws DataAccessException
	 */
    public void edit(T entity) throws DataAccessException;

    /**
	 * @comment 移除实体
	 * @param entity
	 * @throws DataAccessException
	 */
    public void remvoe(T entity) throws DataAccessException;

    /**
	 * @comment 获取实体
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
    public T get(T entity) throws DataAccessException;

    /**
	 * @comment 获取所有实体
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
    public List<T> getList(T entity) throws DataAccessException;
}
