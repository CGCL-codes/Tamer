package org.usca.workshift.database.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.usca.workshift.database.dao.MemberPasswordDAO;
import org.usca.workshift.database.servermodel.MemberPassword;

/**
 * Created by IntelliJ IDEA.
 * User: danny
 * Date: Nov 23, 2007
 * Time: 3:08:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class MemberPasswordDAOImpl extends GenericDAOImpl<MemberPassword> implements MemberPasswordDAO {

    protected Criteria getCriteriaEmail(Criteria crit, String email) {
        return crit.add(Restrictions.eq("email", email));
    }

    public MemberPassword verifyMember(String email, String password) {
        MemberPassword m = (MemberPassword) getCriteriaEmail(getRootCriteria(), email).uniqueResult();
        if (m == null || !m.checkPassword(password)) return null; else return m;
    }
}
