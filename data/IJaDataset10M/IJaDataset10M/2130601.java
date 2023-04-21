package qwicket.dao;

/**
 * Created Jul 12, 2006
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public interface BaseDao<T> {

    void save(T t);
}
