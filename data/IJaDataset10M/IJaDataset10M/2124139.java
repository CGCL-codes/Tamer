package com.hdy.biz;

import java.util.List;
import com.hdy.entity.Department;

/**
 * 
 * @author uncleja
 * @since 2011-11-24 上午10:10:40
 * @comment 
 * @version 1.0
 */
public interface DepartmentBiz<T> {

    /**
	 * @comment 添加实体 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
    public boolean add(T entity) throws Exception;

    /**
	 * @comment 编辑实体
	 * @param entity
	 * @return
	 * @throws Exception
	 */
    public boolean edit(T entity) throws Exception;

    /**
	 * @comment 获取实体 
	 * @param entity
	 * @return
	 * @throws Exception
	 */
    public T get(T entity) throws Exception;

    /**
	 * @comment 获取所有实体 
	 * @return
	 * @throws Exception
	 */
    public List<T> getAll() throws Exception;

    /**
	 * @comment 移除实体
	 * @param entity
	 * @return
	 * @throws Exception
	 */
    public boolean remove(T entity) throws Exception;
}
