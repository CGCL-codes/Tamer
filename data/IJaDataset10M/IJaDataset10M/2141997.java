package com.rai.framework.service.common;

import java.io.Serializable;

public interface GeneralManager extends CommonService<Object, Serializable> {

    /**
	 * 根据ID，获取某类对象<br/>
	 * 此方法只用于主键属性名为“id”时，使用<br/>
	 * 如主键属性名不是“id”，请使用get(clazz,idname,id)方法,定义id名
	 * 
	 * @param <T>
	 *            类型
	 * @param clazz
	 *            类.class
	 * @param id
	 *            id对象
	 * @return 返回查询对象
	 */
    <T> T get(Class<?> clazz, Serializable id) throws Exception;

    /**
	 * 根据主键对象，返回某类对象<br/>
	 * 此方法用于，如主键属性名不是“id”的情况
	 * 
	 * @param <T>
	 * @param clazz
	 *            类.class
	 * @param idname
	 *            id属性名称
	 * @param id
	 *            id对象
	 * @return
	 */
    <T> T get(Class<?> clazz, String idname, Serializable id) throws Exception;

    /**
	 * 删除指定clazz的对象<br/>
	 * 此方法只用于主键属性名为“id”时，使用<br/>
	 * 如主键属性名不是“id”，请使用delete(clazz,idname,id)方法,定义id名
	 * 
	 * @param clazz
	 *            类.class
	 * @param id
	 *            id对象
	 */
    void delete(Class<?> clazz, Serializable id) throws Exception;

    /**
	 * 删除指定clazz的对象<br/>
	 * 此方法用于如主键属性名不是“id”
	 * 
	 * @param clazz
	 *            类.class
	 * @param idname
	 *            id属性名称
	 * @param id
	 *            id对象
	 */
    void delete(Class<?> clazz, String idname, Serializable id) throws Exception;

    /**
	 * 删除对象
	 * 
	 * @param object
	 */
    void deleteObject(Object object) throws Exception;
}
