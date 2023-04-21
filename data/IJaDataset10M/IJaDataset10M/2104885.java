package com.michael.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.apache.commons.lang.StringUtils;
import com.michael.common.PMF;
import com.michael.common.Util;

/**
 * 数据模型基类
 * 
 * @author Administrator
 * 
 */
public abstract class JDOModel<T> extends PageInfo implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -546378111333032746L;

    private transient PersistenceManager pm;

    private transient Map<String, JDOModel<T>> foreignKeys;

    /**
	 * 排序字段
	 */
    private String order;

    protected abstract Object getPK();

    protected abstract void setPK(Object pk);

    @SuppressWarnings("unchecked")
    public T save() {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        try {
            return (T) pm.makePersistent(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
        return null;
    }

    public Collection<T> save(Collection<T> list) {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        try {
            return pm.makePersistent(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public T saveAndCount() {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        this.updateCount(pm, 1, null);
        try {
            return (T) pm.makePersistent(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
        return null;
    }

    /**
	 * 根据字段保存并计数
	 * 
	 * @param countKey
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public T saveAndCount(String... countKey) {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        this.updateCount(pm, 1, countKey);
        try {
            return (T) pm.makePersistent(this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
        return null;
    }

    private void updateCount(PersistenceManager pm, int count, String... countKey) {
        DataCount c = new DataCount();
        String key = this.getClass().getName();
        if (countKey != null) {
            key += "_";
            for (String f : countKey) {
                Object param = Util.getProperty(this, f);
                if (param != null) key += f + "==" + param + " && ";
            }
            if (key.endsWith(" && ")) key = key.substring(0, key.length() - 4);
        }
        c.setSql(key);
        try {
            DataCount c1 = (DataCount) pm.getObjectById(DataCount.class, c.getSql());
            c.setCount(c1.getCount() + count);
        } catch (Exception e) {
            c.setCount(count);
        }
        pm.makePersistent(c);
    }

    @SuppressWarnings("unchecked")
    public void delete() {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        try {
            T model = (T) pm.getObjectById(this.getClass(), this.getPK());
            if (model != null) {
                pm.deletePersistent(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void deleteAndCount() {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        try {
            T model = (T) pm.getObjectById(this.getClass(), this.getPK());
            if (model != null) {
                pm.deletePersistent(model);
                this.updateCount(pm, -1, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
    }

    @SuppressWarnings("unchecked")
    public void deleteAndCount(String... countKey) {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        try {
            T model = (T) pm.getObjectById(this.getClass(), this.getPK());
            if (model != null) {
                pm.deletePersistent(model);
                this.updateCount(pm, -1, countKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
    }

    @SuppressWarnings("unchecked")
    public T load() {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        T model = null;
        try {
            model = (T) pm.getObjectById(this.getClass(), this.getPK());
        } catch (Exception e) {
            return null;
        } finally {
            pm.close();
        }
        return model;
    }

    @SuppressWarnings("unchecked")
    public T load(String where) {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        T model = null;
        String query = "select from " + this.getClass().getName();
        if (where != null) query += " where " + where;
        try {
            List<T> list = (List<T>) pm.newQuery(query).execute();
            if (!list.isEmpty()) {
                model = list.get(0);
                if (this.foreignKeys != null) {
                    Set<String> keySet = this.foreignKeys.keySet();
                    for (String field : keySet) {
                        JDOModel m = this.foreignKeys.get(field);
                        Object pk = Util.getProperty(model, field);
                        Object obj = null;
                        try {
                            obj = pm.getObjectById(this.getClass(), pk);
                        } catch (Exception e) {
                        }
                        if (obj != null) Util.setProperty(m, Util.getLowerCaseModelName(model), obj);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
        return model;
    }

    public boolean exist(String where) {
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        String query = "select from " + this.getClass().getName();
        if (where != null) query += " where " + where;
        try {
            if (pm.newQuery(query).execute() == null) return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
        return true;
    }

    public List<T> find() {
        return this.find(null);
    }

    @SuppressWarnings("unchecked")
    public List<T> find(String where) {
        List<T> models = new ArrayList<T>();
        if (this.getPerPageSize() > 0) {
            this.setDataCount(this.loadCount(where));
            if (this.getDataCount() == 0) return models;
            this.calculate();
        }
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        String query = "select from " + this.getClass().getName();
        if (where != null) query += " where " + where;
        try {
            Query q = pm.newQuery(query);
            if (this.getEndRecord() > 0) {
                q.setRange(this.getStartRecord(), this.getEndRecord());
            }
            if (this.order != null) q.setOrdering(order);
            List<T> list = (List<T>) q.execute();
            if (list != null) models.addAll(list);
            if (this.foreignKeys != null) {
                for (T m : models) {
                    Set<String> keySet = this.foreignKeys.keySet();
                    for (String field : keySet) {
                        JDOModel model = this.foreignKeys.get(field);
                        Object pk = Util.getProperty(m, field);
                        Object obj = null;
                        try {
                            obj = pm.getObjectById(this.getClass(), pk);
                        } catch (Exception e) {
                        }
                        if (obj != null) Util.setProperty(m, Util.getLowerCaseModelName(model), obj);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
        return models;
    }

    /**
	 * 无条件查询数量
	 * 
	 * @return
	 */
    public int loadCount() {
        DataCount c = new DataCount();
        c.setSql(this.getClass());
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        try {
            c = (DataCount) pm.getObjectById(DataCount.class, c.getPK());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pm.close();
        }
        return c.getCount();
    }

    /**
	 * 查询数量
	 * 
	 * @param where
	 *            条件
	 * @return
	 */
    public int loadCount(String where) {
        DataCount c = new DataCount();
        c.setSql(this.getClass(), where);
        if (pm == null || pm.isClosed()) pm = PMF.get().getPersistenceManager();
        try {
            c = (DataCount) pm.getObjectById(DataCount.class, c.getSql());
        } catch (Exception e) {
            return 0;
        } finally {
            pm.close();
        }
        return c.getCount();
    }

    /**
	 * 关联查询
	 * 
	 * @param field
	 *            关联的字段
	 * @param modelClass
	 *            关联的类
	 * @return IBatisModel对象
	 */
    @SuppressWarnings("unchecked")
    public JDOModel foreignKey(String field, Class<? extends JDOModel> modelClass) {
        if (field == null || StringUtils.isEmpty(field)) return this;
        if (this.foreignKeys == null) this.foreignKeys = new HashMap<String, JDOModel<T>>();
        try {
            this.foreignKeys.put(field, modelClass.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
	 * 设置每页多少记录
	 * 
	 * @param size
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public T setPageNum(int size) {
        this.setPerPageSize(size);
        return (T) this;
    }

    public String getOrder() {
        return order;
    }

    @SuppressWarnings("unchecked")
    public T setOrder(String order) {
        this.order = order;
        return (T) this;
    }
}
