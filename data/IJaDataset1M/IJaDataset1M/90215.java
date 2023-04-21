package com.liferay.portal.service.persistence;

import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.SQLQuery;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.Type;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.PasswordPolicy;
import com.liferay.portal.model.impl.PasswordPolicyImpl;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;
import com.liferay.util.dao.orm.CustomSQLUtil;
import java.util.Iterator;
import java.util.List;

/**
 * <a href="PasswordPolicyFinderImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PasswordPolicyFinderImpl extends BasePersistenceImpl implements PasswordPolicyFinder {

    public static String COUNT_BY_C_N = PasswordPolicyFinder.class.getName() + ".countByC_N";

    public static String FIND_BY_C_N = PasswordPolicyFinder.class.getName() + ".findByC_N";

    public int countByC_N(long companyId, String name) throws SystemException {
        name = StringUtil.lowerCase(name);
        Session session = null;
        try {
            session = openSession();
            String sql = CustomSQLUtil.get(COUNT_BY_C_N);
            SQLQuery q = session.createSQLQuery(sql);
            q.addScalar(COUNT_COLUMN_NAME, Type.LONG);
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(companyId);
            qPos.add(name);
            qPos.add(name);
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

    public List<PasswordPolicy> findByC_N(long companyId, String name, int start, int end, OrderByComparator obc) throws SystemException {
        name = StringUtil.lowerCase(name);
        Session session = null;
        try {
            session = openSession();
            String sql = CustomSQLUtil.get(FIND_BY_C_N);
            sql = CustomSQLUtil.replaceOrderBy(sql, obc);
            SQLQuery q = session.createSQLQuery(sql);
            q.addEntity("PasswordPolicy", PasswordPolicyImpl.class);
            QueryPos qPos = QueryPos.getInstance(q);
            qPos.add(companyId);
            qPos.add(name);
            qPos.add(name);
            return (List<PasswordPolicy>) QueryUtil.list(q, getDialect(), start, end);
        } catch (Exception e) {
            throw new SystemException(e);
        } finally {
            closeSession(session);
        }
    }
}
