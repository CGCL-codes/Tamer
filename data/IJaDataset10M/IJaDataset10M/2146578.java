package javango.contrib.admin;

import java.io.Serializable;
import javango.db.Manager;
import javango.db.QuerySet;

@Deprecated
public class BaseModelManager<T> implements ModelManager<T> {

    protected Class clazz;

    protected Manager<T> dao;

    public BaseModelManager(Class clazz, Manager<T> dao) {
        super();
        this.clazz = clazz;
        this.dao = dao;
    }

    public T get(Serializable pk) throws Exception {
        return dao.get(pk);
    }

    public Object getPk(T bean) throws Exception {
        return dao.getPk(bean);
    }

    public QuerySet<T> filter() throws Exception {
        return dao.all();
    }

    public boolean canView() {
        return true;
    }

    public boolean canCreate() {
        return true;
    }

    public boolean canDelete(Object bean) {
        return true;
    }

    public boolean canUpdate(Object bean) {
        return true;
    }
}
