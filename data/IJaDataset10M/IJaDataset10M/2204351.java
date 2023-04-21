package com.butnet.myframe.dao;

import java.io.Serializable;
import java.util.List;
import com.butnet.myframe.hql.query.OrderBys;
import com.butnet.myframe.hql.query.PageList;
import com.butnet.myframe.hql.query.Wheres;

/**
 * DAO接口
 * 
 * @author Butnet
 */
public interface IDao<T> {

    /**
	 * 执行一个HQL语句，返回影响的条数
	 * @param hql
	 * @return
	 */
    int updateHQL(String hql);

    /**
	 * 执行HQL进行查询，返回查询结果集List<?>中的第一个对象，如果结果集不空则返回null
	 * @param hql
	 * @return
	 */
    Object exceHQL(String hql);

    /**
	 * 执行HQL进行查询，返回查询结果List<?>
	 * @param hql
	 * @return
	 */
    List<Object> listHQL(String hql);

    /**
	 * 删除一个对象
	 * 
	 * @param pk
	 *            主键
	 * @return　成功true,失败false
	 */
    boolean delete(Serializable pk);

    /**
	 * 删除对象
	 * 
	 * @param cls
	 * @param pk
	 * @return
	 */
    boolean delete(Class<?> cls, Serializable pk);

    /**
	 * 根据条件删除数据，返回影响的行数
	 * @param ws
	 * @return
	 */
    int delete(Wheres ws);

    /**
	 * 取得一个对象
	 * 
	 * @param pk
	 *            主键
	 * @return 返回主键对应的对象(可能为null)
	 */
    T get(Serializable pk);

    /**
	 * 查询对象
	 * 
	 * @param cls
	 * @param pk
	 * @return
	 */
    Object get(Class<?> cls, Serializable pk);

    /**
	 * 按条件查寻一个对象
	 * 
	 * @param wheres
	 *            条件
	 * @return 成功返回对象,失败返回null
	 */
    T get(Wheres wheres);

    /**
	 * 查询对象
	 * 
	 * @param cls
	 * @param wheres
	 * @return
	 */
    Object get(Class<?> cls, Wheres wheres);

    /**
	 * 按条件查询
	 * 
	 * @param colNames
	 *            列名
	 * @param exps
	 *            运算符
	 * @param values
	 *            值
	 * @param andOrs
	 *            逻辑运行符
	 * @param orderBy
	 *            排序方式
	 * @return 存在返回对象,失败返回false
	 */
    T get(String[] colNames, String[] exps, Object[] values, String[] andOrs, OrderBys orderBy);

    /**
	 * @param cls
	 * @param colNames
	 * @param exps
	 * @param values
	 * @param andOrs
	 * @param orderBy
	 * @return
	 */
    Object get(Class<?> cls, String[] colNames, String[] exps, Object[] values, String[] andOrs, OrderBys orderBy);

    /**
	 * 按条件查寻一个对象
	 * 
	 * @param asName
	 *            AS名称
	 * @param wheres
	 *            条件
	 * @param orderByString
	 *            排序方式
	 * @return 成功返回对象,失败返回null
	 */
    T get(Wheres wheres, OrderBys orderBy);

    /**
	 * @param cls
	 * @param wheres
	 * @param orderBy
	 * @return
	 */
    Object get(Class<?> cls, Wheres wheres, OrderBys orderBy);

    /**
	 * @param orderBys
	 * @return
	 */
    T get(OrderBys orderBys);

    /**
	 * @param cls
	 * @param orderBy
	 * @return
	 */
    Object get(Class<?> cls, OrderBys orderBy);

    /**
	 * 取得所有和T类型对象
	 * 
	 * @return 返回T类型的链表
	 */
    List<T> list();

    /**
	 * @param cls
	 * @return
	 */
    List<Object> list(Class<?> cls);

    /**
	 * 按指定的排序方式返回列表
	 * 
	 * @param orderBy
	 *            排序
	 * @return 列表
	 */
    List<T> list(OrderBys orderBy);

    List<Object> list(Class<?> cls, OrderBys orderBy);

    /**
	 * 取得所有和T类型对象
	 * 
	 * @param pageIndex
	 *            页面索引
	 * @param pageCount
	 *            页面数据个数
	 * @return 返回T类型的链表
	 */
    List<T> list(int pageIndex, int pageCount);

    List<Object> list(Class<?> cls, int pageIndex, int pageCount);

    /**
	 * 取得所有和T类型对象
	 * 
	 * @param pageIndex
	 *            页面索引
	 * @param pageCount
	 *            页面数据个数
	 * @return 返回T类型的链表
	 */
    List<T> list(int pageIndex, int pPageCount, int thisPageCount);

    List<Object> list(Class<?> cls, int pageIndex, int pPageCount, int thisPageCount);

    /**
	 * 取得所有和T类型对象
	 * 
	 * @param pageIndex
	 *            页面索引
	 * @param pageCount
	 *            页面数据个数
	 * @param bys
	 *            排序方式
	 * @return 返回T类型的链表
	 */
    List<T> list(int pageIndex, int pageCount, OrderBys bys);

    List<Object> list(Class<?> cls, int pageIndex, int pageCount, OrderBys bys);

    /**
	 * 取得所有和T类型对象
	 * 
	 * @param pageIndex
	 *            页面索引
	 * @param pPageCount
	 *            上一页面数据个数
	 * @param thisPageCount
	 *            当前查询要的记录条数
	 * @param bys
	 *            排序
	 * @return 返回T类型的链表
	 */
    List<T> list(int pageIndex, int pPageCount, int thisPageCount, OrderBys bys);

    List<Object> list(Class<?> cls, int pageIndex, int pPageCount, int thisPageCount, OrderBys bys);

    /**
	 * 取得所有和T类型对象
	 * 
	 * @param wheres
	 *            查询条件
	 * @return 返回T类型的链表
	 */
    List<T> list(Wheres wheres);

    List<Object> list(Class<?> cls, Wheres wheres);

    /**
	 * 取得所有和T类型对象
	 * 
	 * @param asName
	 *            AS名称
	 * @param wheres
	 *            查询条件
	 * @param orderByString
	 *            排序方式
	 * @return 返回T类型的链表
	 */
    List<T> list(Wheres wheres, OrderBys orderBy);

    List<Object> list(Class<?> cls, Wheres wheres, OrderBys orderBy);

    /**
	 * 取得所有和T类型对象
	 * 
	 * @param asName
	 *            AS名称
	 * @param cols
	 *            列名
	 * @param exps
	 *            运算符
	 * @param values
	 *            值
	 * @param andOrs
	 *            逻辑运算符
	 * @param orderByStr
	 *            排序方式
	 * @return 结果结果
	 */
    List<T> list(String[] cols, String[] exps, Object[] values, String[] andOrs, OrderBys orderBy);

    List<Object> list(Class<?> cls, String[] cols, String[] exps, Object[] values, String[] andOrs, OrderBys orderBy);

    /**
	 * 添加一个T对象
	 * 
	 * @param t
	 *            要保存的对象
	 */
    void insert(T t);

    /**
	 * 添加或更新一个对象.如果对象不存在则添加对象,如果存在则更新对象
	 * 
	 * @param t
	 *            需要处理的对象
	 */
    void insertOrUpdate(T t);

    /**
	 * 更新一个对象
	 * 
	 * @param t
	 *            要更新的对象
	 */
    void update(T t);

    /**
	 * 用指定值更新指定的列
	 * 
	 * @param colNames
	 *            列名
	 * @param values
	 *            值
	 * @param wheres
	 *            更新条件
	 * @return 返回更新的行数
	 * */
    int update(String[] colNames, Object[] values, Wheres wheres);

    int update(Class<?> cls, String[] colNames, Object[] values, Wheres wheres);

    /**
	 * 删除一个对象
	 * 
	 * @param t
	 *            要删除的对象
	 */
    void delete(T t);

    /**
	 * 合并
	 * 
	 * @param t
	 */
    void merge(T t);

    PageList<T> pageList(int pageIndex, int pageCount);
}
