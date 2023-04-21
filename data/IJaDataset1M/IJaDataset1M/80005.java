package com.liferay.portal.service.persistence;

import com.liferay.portal.NoSuchUserGroupException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.model.impl.UserGroupImpl;
import com.liferay.portal.model.impl.UserGroupModelImpl;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="UserGroupFinderImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Charles May
 *
 */
public class UserGroupFinderImpl extends BasePersistenceImpl implements UserGroupFinder {

    public static String COUNT_BY_C_N_D = UserGroupFinder.class.getName() + ".countByC_N_D";

    public static String FIND_BY_C_N = UserGroupFinder.class.getName() + ".findByC_N";

    public static String FIND_BY_C_N_D = UserGroupFinder.class.getName() + ".findByC_N_D";

    public static String JOIN_BY_GROUPS_PERMISSIONS = UserGroupFinder.class.getName() + ".joinByGroupsPermissions";

    public static String JOIN_BY_USER_GROUPS_GROUPS = UserGroupFinder.class.getName() + ".joinByUserGroupsGroups";

    public static String JOIN_BY_USER_GROUPS_ROLES = UserGroupFinder.class.getName() + ".joinByUserGroupsRoles";

    public int countByC_N_D(long companyId, String name, String description, LinkedHashMap<String, Object> params) throws SystemException {
        name = StringUtil.lowerCase(name);
        description = StringUtil.lowerCase(description);
        Session session = null;
        try {
            session = openSession();
            String sql = CustomSQLUtil.get(COUNT_BY_C_N_D);
            sql = StringUtil.replace(sql, "[$JOIN$]", getJoin(params));
            sql = StringUtil.replace(sql, "[$WHERE$]", getWhere(params));
            SQLQuery q = session.createSQLQuery(sql);
            q.addScalar(COUNT_COLUMN_NAME, Type.LONG);
            QueryPos qPos = QueryPos.getInstance(q);
            setJoin(qPos, params);
            qPos.add(companyId);
            qPos.add(name);
            qPos.add(name);
            qPos.add(description);
            qPos.add(description);
            Iterator<Long> itr = q.list().iterator();
            if (itr.hasNext()) {
                Long count = itr.next();
                if (count != null) {
                    return count.intValue();
                }
            }
            return 0;
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            closeSession(session);
        }
    }

    public UserGroup findByC_N(long companyId, String name) throws NoSuchUserGroupException, SystemException {
        name = StringUtil.lowerCase(name);
        boolean finderClassNameCacheEnabled = UserGroupModelImpl.CACHE_ENABLED;
        String finderClassName = UserGroup.class.getName();
        String finderMethodName = "customFindByC_N";
        String finderParams[] = new String[] { Long.class.getName(), String.class.getName() };
        Object finderArgs[] = new Object[] { new Long(companyId), name };
        Object result = FinderCacheUtil.getResult(finderClassName, finderMethodName, finderParams, finderArgs, this);
        if (result == null) {
            Session session = null;
            try {
                session = openSession();
                String sql = CustomSQLUtil.get(FIND_BY_C_N);
                SQLQuery q = session.createSQLQuery(sql);
                q.addEntity("UserGroup", UserGroupImpl.class);
                QueryPos qPos = QueryPos.getInstance(q);
                qPos.add(companyId);
                qPos.add(name);
                Iterator<UserGroup> itr = q.list().iterator();
                if (itr.hasNext()) {
                    UserGroup userGroup = itr.next();
                    FinderCacheUtil.putResult(finderClassNameCacheEnabled, finderClassName, finderMethodName, finderParams, finderArgs, userGroup);
                    return userGroup;
                }
            } catch (Exception e) {
                throw new SystemException(e);
            } finally {
                closeSession(session);
            }
            throw new NoSuchUserGroupException("No UserGroup exists with the key {companyId=" + companyId + ", name=" + name + "}");
        } else {
            return (UserGroup) result;
        }
    }

    public List<UserGroup> findByC_N_D(long companyId, String name, String description, LinkedHashMap<String, Object> params, int start, int end, OrderByComparator obc) throws SystemException {
        name = StringUtil.lowerCase(name);
        description = StringUtil.lowerCase(description);
        Session session = null;
        try {
            session = openSession();
            String sql = CustomSQLUtil.get(FIND_BY_C_N_D);
            sql = StringUtil.replace(sql, "[$JOIN$]", getJoin(params));
            sql = StringUtil.replace(sql, "[$WHERE$]", getWhere(params));
            sql = CustomSQLUtil.replaceOrderBy(sql, obc);
            SQLQuery q = session.createSQLQuery(sql);
            q.addEntity("UserGroup", UserGroupImpl.class);
            QueryPos qPos = QueryPos.getInstance(q);
            setJoin(qPos, params);
            qPos.add(companyId);
            qPos.add(name);
            qPos.add(name);
            qPos.add(description);
            qPos.add(description);
            return (List<UserGroup>) QueryUtil.list(q, getDialect(), start, end);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            closeSession(session);
        }
    }

    protected String getJoin(LinkedHashMap<String, Object> params) {
        if (params == null) {
            return StringPool.BLANK;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> itr = params.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> entry = itr.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (Validator.isNotNull(value)) {
                sb.append(getJoin(key));
            }
        }
        return sb.toString();
    }

    protected String getJoin(String key) {
        String join = StringPool.BLANK;
        if (key.equals("permissionsResourceId")) {
            join = CustomSQLUtil.get(JOIN_BY_GROUPS_PERMISSIONS);
        } else if (key.equals("userGroupsGroups")) {
            join = CustomSQLUtil.get(JOIN_BY_USER_GROUPS_GROUPS);
        } else if (key.equals("userGroupsRoles")) {
            join = CustomSQLUtil.get(JOIN_BY_USER_GROUPS_ROLES);
        }
        if (Validator.isNotNull(join)) {
            int pos = join.indexOf("WHERE");
            if (pos != -1) {
                join = join.substring(0, pos);
            }
        }
        return join;
    }

    protected String getWhere(LinkedHashMap<String, Object> params) {
        if (params == null) {
            return StringPool.BLANK;
        }
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> itr = params.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> entry = itr.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (Validator.isNotNull(value)) {
                sb.append(getWhere(key));
            }
        }
        return sb.toString();
    }

    protected String getWhere(String key) {
        String join = StringPool.BLANK;
        if (key.equals("permissionsResourceId")) {
            join = CustomSQLUtil.get(JOIN_BY_GROUPS_PERMISSIONS);
        } else if (key.equals("userGroupsGroups")) {
            join = CustomSQLUtil.get(JOIN_BY_USER_GROUPS_GROUPS);
        } else if (key.equals("userGroupsRoles")) {
            join = CustomSQLUtil.get(JOIN_BY_USER_GROUPS_ROLES);
        }
        if (Validator.isNotNull(join)) {
            int pos = join.indexOf("WHERE");
            if (pos != -1) {
                StringBuilder sb = new StringBuilder();
                sb.append(join.substring(pos + 5, join.length()));
                sb.append(" AND ");
                join = sb.toString();
            } else {
                join = StringPool.BLANK;
            }
        }
        return join;
    }

    protected void setJoin(QueryPos qPos, LinkedHashMap<String, Object> params) {
        if (params != null) {
            Iterator<Map.Entry<String, Object>> itr = params.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<String, Object> entry = itr.next();
                Object value = entry.getValue();
                if (value instanceof Long) {
                    Long valueLong = (Long) value;
                    if (Validator.isNotNull(valueLong)) {
                        qPos.add(valueLong);
                    }
                } else if (value instanceof String) {
                    String valueString = (String) value;
                    if (Validator.isNotNull(valueString)) {
                        qPos.add(valueString);
                    }
                }
            }
        }
    }
}
