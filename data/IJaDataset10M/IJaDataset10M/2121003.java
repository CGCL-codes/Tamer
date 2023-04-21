package whf.framework.dao;

import java.io.Serializable;
import java.util.List;
import whf.framework.exception.FindException;

/**
 * 数据访问对象(DAO)支持接口
 * @author wanghaifeng
 * 
 */
public interface DAOSupport<T> {

    /**
	 * 保存业务对象
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param entity 业务对象
	 */
    public abstract void save(T entity);

    /**
	 * 根据业务类型和对象主键装载对象,但是不能装载hibernate中定义的lazy属性
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param clazz 业务类型
	 * @param key 主键
	 * @return 业务实体
	 */
    public abstract T load(Class<T> clazz, Serializable key);

    /**
	 * 对上述方法的补充,可以装载对象的lazy属性
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param clazz
	 * @param key
	 * @return
	 */
    public abstract T loadWithoutLazy(Class<T> clazz, Serializable key);

    /**
	 * 修改保存业务对象
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param entity 业务对象
	 */
    public abstract void modify(T entity);

    /**
	 * 删除业务对象
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param entity 删除
	 */
    public abstract void delete(T entity);

    /**
	 * 根据条件字符串查询业务对象(能够装载对象的lazy属性)
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param queryString 查询字符串,通产格式为from xxx where xxx order by xxx,可以取其中任意一节
	 * @return 返回对象列表
	 */
    public abstract List<T> findWithoutLazy(String queryString);

    /**
	 * 带参数的查询,同上
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param queryString
	 * @param params
	 * @return
	 */
    public abstract List<T> findWithoutLazy(String queryString, Object[] params);

    /**
	 * 不带参数的查询,但是可以限制结果范围
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param queryString
	 * @param start 从xx对象开始
	 * @param pageSize 输出对象的最大数量
	 * @return
	 */
    public abstract List<T> findWithoutLazy(String queryString, int start, int pageSize);

    /**
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param queryString
	 * @param arg
	 * @return
	 * @throws FindException
	 */
    public abstract List<T> findWithoutLazy(String queryString, Object arg) throws FindException;

    /**
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param queryString
	 * @param arg
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws FindException
	 */
    public abstract List<T> findWithoutLazy(String queryString, Object arg, int start, int pageSize) throws FindException;

    /**
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param query
	 * @return
	 */
    public abstract List<T> findWithListResult(String query);

    /**
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param query
	 * @param arg
	 * @return
	 */
    public abstract List<T> findWithListResult(String query, Object arg);

    /**
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param query
	 * @param args
	 * @return
	 */
    public abstract List<T> findWithListResult(String query, Object[] args);

    /**
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param queryString
	 * @param start
	 * @param pageSize
	 * @return
	 */
    public abstract List<T> list(String queryString, int start, int pageSize);

    /**
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param queryString
	 * @param paramValues
	 * @param start
	 * @param pageSize
	 * @return
	 */
    public abstract List<T> list(String queryString, Object[] paramValues, int start, int pageSize);

    /**
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param queryString
	 * @param paramNames
	 * @param paramValues
	 * @param start
	 * @param pageSize
	 * @return
	 */
    public abstract List<T> list(String queryString, String[] paramNames, Object[] paramValues, int start, int pageSize);

    /**
	 * @modify wanghaifeng Aug 25, 2006 1:09:31 PM
	 * @param boClass
	 * @param queryString
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws FindException
	 */
    public List<T> queryWithoutLazy(Class boClass, String queryString, int start, int pageSize) throws FindException;

    /**
	 * 使用Native sql查询
	 * @modify wanghaifeng Mar 10, 2007 11:56:58 AM
	 * @param sql
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws FindException
	 */
    public List<T> findBySql(Class boClass, String tableName, String whereStatement, String orderStatement, int start, int pageSize) throws FindException;

    /**
	 * 获取命名查询结果
	 * @author king
	 * @create 2008-1-21 下午04:23:53
	 * @param queryName
	 * @param params
	 * @param start
	 * @param pageSize
	 * @return
	 */
    public List<T> getNamedQueryResult(String queryName, Object[] params, int start, int pageSize) throws FindException;

    /**
	 * @author king
	 * @create 2008-1-21 下午04:35:08
	 * @param queryName
	 * @param paramNames
	 * @param params
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws FindException
	 */
    public List<T> getNamedQueryResult(String queryName, String[] paramNames, Object[] params, int start, int pageSize) throws FindException;
}
