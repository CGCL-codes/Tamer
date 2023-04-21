package com.phasotron.nucleon.dao;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.phasotron.nucleon.dao.support.Filter;
import com.phasotron.nucleon.dao.support.Limit;
import com.phasotron.nucleon.dao.support.Page;

public class GenericDao<F, E> extends HibernateDaoSupport {

    private String selectFormHql;

    private String selectEntityHql;

    private String fromHql;

    private String whereHql;

    private Pattern filedNamePattern = Pattern.compile("\\$\\{\\s*(\\w+)\\.(logical|filed|operator|data)\\s*\\}");

    public void setSelectFormHql(String selectFormHql) {
        this.selectFormHql = selectFormHql;
    }

    public void setSelectEntityHql(String selectEntityHql) {
        this.selectEntityHql = selectEntityHql;
    }

    public void setFromHql(String fromHql) {
        this.fromHql = fromHql;
    }

    public void setWhereHql(String whereHql) {
        this.whereHql = whereHql;
    }

    /**
	 * 通过ID获得一个实体Form
	 * @param id
	 * @return
	 */
    public F getForm(String id) {
        return getForm("id", id);
    }

    /**
	 * 通过一个属性获得一个Form
	 * @param Map<String, Object> filters
	 * @return
	 */
    public F getForm(String propertyName, Object value) {
        Limit limit = new Limit();
        Filter filter = new Filter(propertyName, value);
        limit.addFilter(filter);
        List<F> forms = getFormList(limit);
        if (forms == null || forms.size() != 1) return null;
        return forms.get(0);
    }

    /**
	 * 获得所有实体Form
	 * @return
	 */
    public List<F> getAllForm() {
        return getFormList(new Limit());
    }

    @Deprecated
    public List<F> getFormList(Map<String, Object> filters) {
        Limit limit = new Limit();
        for (String key : filters.keySet()) {
            Filter filter = new Filter(key, filters.get(key));
            limit.addFilter(filter);
        }
        return getFormList(limit);
    }

    public List<F> getFormList(Limit limit) {
        return find(selectFormHql, limit);
    }

    /**
	 * 根据PageLimit获得实体Form的Page
	 * @param pageLimit
	 * @return
	 */
    public Page<F> getFormPage(Limit limit) {
        Integer totalCount = count(limit);
        if (totalCount < 1) return new Page<F>();
        List<F> list = getFormList(limit);
        return new Page<F>(limit.getStartOfPage(), totalCount, limit.getPageSize(), list);
    }

    /**
	 * 通过ID获得一个实体
	 * @param id
	 * @return
	 */
    public E getEntity(String id) {
        return getEntity("id", id);
    }

    /**
	 * 通过filters获得一个实体
	 * @param Map<String, Object> filters
	 * @return
	 */
    public E getEntity(String propertyName, Object value) {
        Limit limit = new Limit();
        Filter filter = new Filter(propertyName, value);
        limit.addFilter(filter);
        List<E> entityList = getEntityList(limit);
        if (entityList == null || entityList.size() != 1) return null;
        return entityList.get(0);
    }

    /**
	 * 获得所有实体
	 * @return
	 */
    public List<E> getAllEntity() {
        return getEntityList(new Limit());
    }

    @Deprecated
    public List<E> getEntityList(Map<String, Object> filters) {
        Limit limit = new Limit();
        for (String key : filters.keySet()) {
            Filter filter = new Filter(key, filters.get(key));
            limit.addFilter(filter);
        }
        return getEntityList(limit);
    }

    public List<E> getEntityList(Limit limit) {
        return find(selectEntityHql, limit);
    }

    /**
	 * 根据Limit获得实体的Page
	 * @param limit
	 * @return
	 */
    public Page<E> getEntityPage(Limit limit) {
        Integer totalCount = count(limit);
        if (totalCount < 1) return new Page<E>();
        List<E> list = getEntityList(limit);
        return new Page<E>(limit.getStartOfPage(), totalCount, limit.getPageSize(), list);
    }

    public Integer count(Limit limit) {
        String selectHql;
        if (selectEntityHql == null || selectEntityHql.trim().equals("")) selectHql = "count(*)"; else selectHql = "count(" + selectEntityHql + ")";
        int pageSize = limit.getPageSize();
        limit.setPageSize(0);
        List<?> countlist = find(selectHql, limit);
        limit.setPageSize(pageSize);
        return ((Long) countlist.get(0)).intValue();
    }

    /**
	 * 保存实体
	 * @param entity
	 */
    public boolean save(E entity) {
        try {
            getHibernateTemplate().saveOrUpdate(entity);
            return true;
        } catch (Exception e) {
            logger.error(e);
            return false;
        }
    }

    /**
	 * 删除实体
	 * @param entity
	 */
    public void delete(E entity) {
        getHibernateTemplate().delete(entity);
    }

    /**
	 * 通过ID删除实体
	 * @param id
	 */
    public void deleteById(String id) {
        delete(getEntity(id));
    }

    /**
	 * 强制完成数据库更新
	 */
    public void flush() {
        getHibernateTemplate().flush();
    }

    /**
	 * 清除缓存
	 */
    public void clear() {
        getHibernateTemplate().clear();
    }

    @SuppressWarnings("unchecked")
    protected <T> List<T> find(String selectHql, Limit limit) {
        Session session = getSession();
        Query query = session.createQuery(createHql(selectHql, limit));
        for (String parameter : query.getNamedParameters()) {
            if (limit.containsFilterKey(parameter)) {
                Object value = limit.getFilter(parameter).getDataValue();
                if (value instanceof Collection<?>) {
                    query.setParameterList(parameter, (Collection<?>) value);
                } else {
                    query.setParameter(parameter, value);
                }
            } else {
                logger.error("Filter Don't have the '" + parameter + "' Value");
            }
        }
        List<T> list = null;
        if (limit.getPageSize() == 0) list = query.list(); else list = query.setFirstResult(limit.getStartOfPage()).setMaxResults(limit.getPageSize()).list();
        releaseSession(session);
        return list;
    }

    protected String createHql(String selectHql, Limit limit) {
        Set<String> usedParameterSet = new HashSet<String>();
        String condition = " ";
        if (whereHql != null) {
            String[] hqls = whereHql.split("/~");
            for (String hql : hqls) {
                if (hql.trim().equals("")) continue;
                hql = hql.trim() + " ";
                boolean isInclude = true;
                Matcher matcher = filedNamePattern.matcher(hql);
                while (isInclude && matcher.find()) {
                    String fieldValue = matcher.group(1);
                    List<Filter> filters = limit.getFilters(fieldValue);
                    if (filters.size() > 0) {
                        String tempHql = "";
                        for (Filter filter : filters) {
                            tempHql = tempHql + filter.toHql(hql);
                        }
                        hql = tempHql;
                        usedParameterSet.add(fieldValue);
                        matcher = filedNamePattern.matcher(hql);
                    } else {
                        isInclude = false;
                    }
                }
                if (isInclude) {
                    condition = condition + hql;
                }
            }
        }
        for (Filter filter : limit.getFilters()) if (!usedParameterSet.contains(filter.getFieldValue())) condition = filter.toHql() + condition;
        String cdt = condition.toLowerCase();
        if (cdt.startsWith(" and ")) condition = " where" + condition.substring(4); else if (cdt.startsWith(" or ")) condition = " where" + condition.substring(3);
        String sortField = limit.getSortField();
        if (sortField != null && !sortField.trim().equals("")) condition = removeOrders(condition) + " order by " + sortField + " " + limit.getSortOrder();
        if (selectHql == null || selectHql.trim().equals("")) return "from " + fromHql + condition; else return "select " + selectHql + " from " + fromHql + condition;
    }

    protected static String removeOrders(String hql) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
