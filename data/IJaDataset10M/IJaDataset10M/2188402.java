package net.simpleframework.ado.db;

import net.simpleframework.ado.IDataObjectValue;
import net.simpleframework.core.IApplicationModuleAware;
import net.simpleframework.core.ado.IDataObjectListener;
import net.simpleframework.core.bean.IDataObjectBean;
import net.simpleframework.core.id.ID;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IBeanManagerAware<T extends IDataObjectBean> extends IApplicationModuleAware {

    void addListener(IDataObjectListener listener);

    IQueryEntitySet<T> query();

    IQueryEntitySet<T> query(IDataObjectValue dataObjectValue);

    T queryForObjectById(Object id);

    int update(T bean);

    int update(String[] columns, T bean);

    int insert(T bean);

    int delete(IDataObjectValue dataObjectValue);

    ITableEntityManager getEntityManager();

    ID id(Object id);

    String tblname();
}
