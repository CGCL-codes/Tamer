package riceSystem.dao;

import java.util.List;

/**
 * 
 * @author zhangyi
 * 接口的模板，具体的dao实现需实现
 * @param <T>
 */
public interface DaoTemplateInterface<T> {

    /**
	 * 存储一个对象
	 * @param entity 对象
	 */
    public void save(T entity);

    /**
	 * 通过id删除一个对象
	 * @param id 对象id
	 */
    public void deleteById(long id);

    /**
	 * 通过对象删除一个对象
	 * @param entity
	 */
    public void delete(T entity);

    /**
	 * 更新一个对象
	 * @param entity 对象
	 */
    public long update(T entity);

    /**
	 * 通过查询一个对象
	 * @param id 对象id
	 * @return
	 */
    public T loadById(long id);

    /**
	 * 查找所有的对象
	 * (此方法后期可能被分页方法替换)
	 * @return
	 */
    public List<T> loadAll();
}
