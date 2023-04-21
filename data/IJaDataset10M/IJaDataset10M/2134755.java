package com.rai.framework.dao.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.OrderedMap;
import org.hibernate.Query;
import org.hibernate.Session;
import com.rai.framework.model.common.ConditionModel;
import com.rai.framework.model.common.QueryModel;
import com.rai.framework.model.common.QueryModel.UNION;
import com.rai.framework.utils.PageControl;

/**
 * 生成具体查询方法的工厂类(Hibernate实现)<br/>
 * 稍作修改可做EJB3实现<br/>
 * 此类在平时使用时，不用调用
 * 
 * @author zhaoxin
 * 
 */
public class QueryFactory {

    /**
	 * 根据QueryModel 查询结果List
	 * 
	 * @param em
	 * @param queryModel
	 * @return
	 */
    public static final List<?> findByQueryModel(Session em, QueryModel queryModel) throws Exception {
        Query query = createEJBQuery(em, queryModel, false);
        return query.list();
    }

    public static final void deleteByQueryModel(Session em, QueryModel queryModel) throws Exception {
        Query query = createEJBDeleteQuery(em, queryModel);
        query.executeUpdate();
    }

    /**
	 * 根据QueryModel,分页查询
	 * 
	 * @param em
	 * @param pageControl
	 * @param queryModel
	 */
    public static final void findPageByQueryModel(Session em, PageControl pageControl, QueryModel queryModel) throws Exception {
        Query countQuery = createEJBQuery(em, queryModel, true);
        int count = ((Long) countQuery.uniqueResult()).intValue();
        if (count > 0) {
            pageControl.setCount(count);
            Query query = createEJBQuery(em, queryModel, false);
            query.setMaxResults(pageControl.getPageSize());
            query.setFirstResult(pageControl.getBegin());
            pageControl.setList(query.list());
        }
    }

    /**
	 * 根据QueryModel， Count计算结果总数<br/>
	 * 
	 * @param em
	 * @param queryModel
	 * @return 返回int型总数，最小值0
	 */
    public static final int countByQueryModel(Session em, QueryModel queryModel) throws Exception {
        Query query = createEJBQuery(em, queryModel, true);
        return ((Long) query.uniqueResult()).intValue();
    }

    /**
	 * 根据queryModel，查询唯一对象,list.get(0)
	 * 
	 * @param em
	 * @param queryModel
	 * @return
	 */
    public static final Object getByQueryModel(Session em, QueryModel queryModel) throws Exception {
        Query query = createEJBQuery(em, queryModel, false);
        query.setMaxResults(1);
        return query.uniqueResult();
    }

    /**
	 * 用于生成查询Query
	 * 
	 * @param em
	 * @param queryModel
	 * @param count
	 * @return
	 */
    @SuppressWarnings("unused")
    private static Query createEJBQuery(Session em, QueryModel queryModel, boolean count) throws Exception {
        Query query = null;
        Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();
        valuesMap.put(0, 0);
        if (QueryModel.UNION.NOT.equals(queryModel.getUnion())) {
            String sql = createSelectSQLString(queryModel, count);
            String tableSQL = createTableSQLString(queryModel);
            String whereSQL = createWhereSQLString(valuesMap, queryModel);
            String groupBy = "";
            if (queryModel.getGroupby() != null) groupBy = " group by " + queryModel.getGroupby();
            String orderSQL = "";
            if (!count) {
                orderSQL = createOrderSQLString(queryModel);
            }
            sql = sql + tableSQL + whereSQL + groupBy + orderSQL;
            query = em.createQuery(sql);
        } else {
            String sql = null;
            for (QueryModel subQueryModel : queryModel.getQueryModelList()) {
                String subSql = createSelectSQLString(queryModel, count);
                String tableSQL = createTableSQLString(queryModel);
                String whereSQL = createWhereSQLString(valuesMap, queryModel);
                String groupBy = "";
                if (queryModel.getGroupby() != null) groupBy = " group by " + queryModel.getGroupby();
                subSql = subSql + tableSQL + groupBy + whereSQL;
                if (sql == null) sql = subSql; else {
                    String unionStr = " union ";
                    if (UNION.ALL.equals(queryModel.getUnion())) unionStr += "all ";
                    sql += unionStr + subSql;
                }
            }
            if (!queryModel.getOrderedMap().isEmpty()) {
                sql = "select " + ConditionModel.ALIAS_NAME + " from (" + sql + ") " + ConditionModel.ALIAS_NAME;
                String orderSQL = createOrderSQLString(queryModel);
                sql += orderSQL;
            }
            query = em.createQuery(sql);
        }
        if (!valuesMap.isEmpty()) {
            Iterator<Integer> it = valuesMap.keySet().iterator();
            while (it.hasNext()) {
                Integer key = it.next();
                if (key > 0) {
                    query.setParameter(key - 1, valuesMap.get(key));
                }
            }
        }
        return query;
    }

    /**
	 * 用于生成查询Query
	 * 
	 * @param em
	 * @param queryModel
	 * @param count
	 * @return
	 */
    @SuppressWarnings("unused")
    private static Query createEJBDeleteQuery(Session em, QueryModel queryModel) throws Exception {
        Query query = null;
        Map<Integer, Object> valuesMap = new HashMap<Integer, Object>();
        valuesMap.put(0, 0);
        String sql = createSelectSQLString(queryModel, false);
        String tableSQL = createTableSQLString(queryModel);
        String whereSQL = createWhereSQLString(valuesMap, queryModel);
        sql = sql + tableSQL + whereSQL;
        sql = "delete from " + queryModel.getQueryClass()[0].getSimpleName() + " d_0 where d_0 in (" + sql + ")";
        System.out.println(sql);
        query = em.createQuery(sql);
        if (!valuesMap.isEmpty()) {
            Iterator<Integer> it = valuesMap.keySet().iterator();
            while (it.hasNext()) {
                Integer key = it.next();
                if (key > 0) {
                    query.setParameter(key - 1, valuesMap.get(key));
                }
            }
        }
        return query;
    }

    /**
	 * 生成查询hql所用的select部分<br/>
	 * creatEJBQuery调用
	 * 
	 * @param queryModel
	 *            查询条件
	 * @param count
	 *            是否生成count(*)
	 * @return 返回select语句
	 */
    private static String createSelectSQLString(QueryModel queryModel, boolean count) throws Exception {
        String selectSQLString = "select ";
        if (queryModel.getSelect() != null) {
            selectSQLString += queryModel.getSelect();
        } else if (count) {
            if (queryModel.isDistinct()) selectSQLString += "count(distinct " + ConditionModel.ALIAS_NAME + ")"; else selectSQLString += "count(*)";
        } else {
            if (queryModel.isDistinct()) selectSQLString += "distinct ";
            selectSQLString += ConditionModel.ALIAS_NAME;
        }
        selectSQLString += " from ";
        return selectSQLString;
    }

    /**
	 * 根据QueryModel创建from字段，查询table<br/>
	 * creatEJBQuery调用
	 * 
	 * @param queryModel
	 * @return 返回from字段
	 */
    private static String createTableSQLString(QueryModel queryModel) throws Exception {
        String tableSQLString = "";
        tableSQLString += queryModel.getQueryClass()[0].getSimpleName() + " " + ConditionModel.ALIAS_NAME;
        OrderedMap aliasMap = queryModel.getAliasNames();
        if (!aliasMap.isEmpty()) {
            @SuppressWarnings("unchecked") Iterator<String> it = aliasMap.orderedMapIterator();
            while (it.hasNext()) {
                String key = it.next();
                tableSQLString += " " + queryModel.getJoin() + " join " + key + " " + aliasMap.get(key);
            }
        }
        return tableSQLString;
    }

    /**
	 * 返回order子字符串<br/>
	 * creatEJBQuery调用
	 * 
	 * @param queryModel
	 * @return
	 */
    private static String createOrderSQLString(QueryModel queryModel) throws Exception {
        String orderSQLString = "";
        List<String> orders = new ArrayList<String>();
        OrderedMap orderMap = queryModel.getOrderedMap();
        if (!orderMap.isEmpty()) {
            @SuppressWarnings("unchecked") Iterator<String> it = orderMap.orderedMapIterator();
            while (it.hasNext()) {
                String key = it.next();
                String value = (String) orderMap.get(key);
                String aliasName = ConditionModel.ALIAS_NAME;
                while (true) {
                    int index = key.indexOf(".");
                    if (index <= -1) break;
                    String searchKey = aliasName + "." + key.substring(0, index);
                    if (queryModel.getAliasNames().containsKey(searchKey)) {
                        aliasName = (String) queryModel.getAliasNames().get(searchKey);
                        key = key.substring(index + 1);
                    } else {
                        break;
                    }
                }
                orders.add(orders.size(), aliasName + "." + key + " " + value);
            }
            if (orders.size() > 0) orderSQLString = " order by ";
            for (String order : orders) {
                if (!orderSQLString.equals(" order by ")) orderSQLString += ",";
                orderSQLString += order;
            }
        }
        return orderSQLString;
    }

    /**
	 * 生成查询条件where部分<br/>
	 * creatEJBQuery调用
	 * 
	 * @param valuesMap
	 * @param queryModel
	 * @return
	 */
    private static String createWhereSQLString(Map<Integer, Object> valuesMap, QueryModel queryModel) throws Exception {
        String whereSQLString = "";
        List<ConditionModel> conditionModeList = queryModel.getConditionModeList();
        if (conditionModeList != null && conditionModeList.size() > 0) {
            for (ConditionModel conditionModel : conditionModeList) {
                if ("".equals(whereSQLString)) whereSQLString = " where "; else {
                    whereSQLString += " and ";
                }
                whereSQLString += createExpression(valuesMap, conditionModel);
            }
        }
        return whereSQLString;
    }

    /**
	 * 生成查询条件
	 * 
	 * @param valuesMap
	 * @param conditionModel
	 * @return
	 */
    private static String createExpression(Map<Integer, Object> valuesMap, ConditionModel conditionModel) throws Exception {
        String condition = null;
        String columnName = conditionModel.getColumnName();
        Object value = conditionModel.getValue();
        int key = 0;
        switch(conditionModel.getCondition()) {
            case eq:
                condition = columnName + " = ?";
                key = (Integer) valuesMap.get(0) + 1;
                valuesMap.put(key, value);
                valuesMap.put(0, key);
                break;
            case ne:
                condition = columnName + " != ?";
                key = (Integer) valuesMap.get(0) + 1;
                valuesMap.put(key, value);
                valuesMap.put(0, key);
                break;
            case ge:
                condition = columnName + " >= ?";
                key = (Integer) valuesMap.get(0) + 1;
                valuesMap.put(key, value);
                valuesMap.put(0, key);
                break;
            case le:
                condition = columnName + " <= ?";
                key = (Integer) valuesMap.get(0) + 1;
                valuesMap.put(key, value);
                valuesMap.put(0, key);
                break;
            case gt:
                condition = columnName + " > ?";
                key = (Integer) valuesMap.get(0) + 1;
                valuesMap.put(key, value);
                valuesMap.put(0, key);
                break;
            case lt:
                condition = columnName + " < ?";
                key = (Integer) valuesMap.get(0) + 1;
                valuesMap.put(key, value);
                valuesMap.put(0, key);
                break;
            case in:
                generateArrayParameter(conditionModel.getValues().length);
                if (conditionModel.getValues().length > 0) {
                    condition = columnName + " in (" + generateArrayParameter(conditionModel.getValues().length) + ")";
                    key = (Integer) valuesMap.get(0) + 1;
                    for (int i = 0; i < conditionModel.getValues().length; i++) {
                        valuesMap.put(key + i, conditionModel.getValues()[i]);
                    }
                }
                break;
            case notIn:
                generateArrayParameter(conditionModel.getValues().length);
                if (conditionModel.getValues().length > 0) {
                    condition = columnName + " not in (" + generateArrayParameter(conditionModel.getValues().length) + ")";
                    key = (Integer) valuesMap.get(0) + 1;
                    for (int i = 0; i < conditionModel.getValues().length; i++) {
                        valuesMap.put(key + i, conditionModel.getValues()[i]);
                    }
                }
                break;
            case or:
                condition = "";
                for (ConditionModel model : conditionModel.getConditionList()) {
                    String subCondition = createExpression(valuesMap, model);
                    if (!"".equals(subCondition)) {
                        if ("".equals(condition)) condition = " (" + subCondition; else condition += " or " + subCondition;
                    }
                }
                if (!"".equals(condition)) condition += ")";
                break;
            case like:
                condition = columnName + " like ?";
                switch(conditionModel.getMatchMode()) {
                    case ANYWHERE:
                        value = "%" + value + "%";
                        break;
                    case END:
                        value = "%" + value;
                        break;
                    case EXACT:
                        break;
                    case START:
                        value = value + "%";
                        break;
                }
                key = (Integer) valuesMap.get(0) + 1;
                valuesMap.put(key, value);
                valuesMap.put(0, key);
                break;
            case columnLike:
                condition = "? like ";
                switch(conditionModel.getMatchMode()) {
                    case ANYWHERE:
                        columnName = "'%' || " + columnName + " || '%'";
                        break;
                    case END:
                        columnName = "'%' || " + columnName;
                        break;
                    case EXACT:
                        break;
                    case START:
                        columnName = columnName + " || '%'";
                        break;
                }
                condition += columnName;
                key = (Integer) valuesMap.get(0) + 1;
                valuesMap.put(key, value);
                valuesMap.put(0, key);
                break;
            case isNull:
                condition = columnName + " is null";
                break;
            case isNotNull:
                condition = columnName + " is not null";
                break;
            case isEmpty:
                break;
            case isNotEmpty:
                break;
            case conjunction:
                condition = "";
                for (ConditionModel model : conditionModel.getConditionList()) {
                    String subCondition = createExpression(valuesMap, model);
                    if (!"".equals(subCondition)) {
                        if ("".equals(condition)) condition = " (" + subCondition; else condition += " and " + subCondition;
                    }
                }
                if (!"".equals(condition)) condition += ")";
                break;
            case disjunction:
                condition = "";
                for (ConditionModel model : conditionModel.getConditionList()) {
                    String subCondition = createExpression(valuesMap, model);
                    if (!"".equals(subCondition)) {
                        if ("".equals(condition)) condition = " (" + subCondition; else condition += " or " + subCondition;
                    }
                }
                if (!"".equals(condition)) condition += ")";
                break;
            default:
                condition = "";
        }
        return condition;
    }

    /**
	 * 查询条件中，出现IN数组等情况是<br>
	 * 使用此方法，自动生成绑定参数<br>
	 * 如: ?,?,?,?
	 * 
	 * @param size
	 *            绑定参数数量
	 * @return 绑定参数字符串
	 */
    protected static String generateArrayParameter(int size) throws Exception {
        String ret = null;
        for (int i = 0; i < size; i++) {
            if (ret == null) ret = "?"; else ret += ",?";
        }
        return ret;
    }
}
