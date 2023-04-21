package org.bpmsuite.dao.impl;

import org.bpmsuite.constants.ValueObjects;
import org.bpmsuite.dao.AbstractHibernateDao;
import org.bpmsuite.dao.DaoException;
import org.bpmsuite.vo.timerecord.Timedetail;
import org.hibernate.Session;

/**
 * @author Dirk Weiser
 */
public class StoreTimedetailDao extends AbstractHibernateDao {

    private Timedetail _timedetail;

    public void doInHibernateTransaction(Session session) {
        session.save(_timedetail);
        _result = null;
    }

    protected void initParams() throws DaoException {
        Object timedetail = _params.get(ValueObjects.TIMEDETAIL);
        if (timedetail == null) {
            throw new DaoException("Mandatory params are null!");
        } else {
            _timedetail = (Timedetail) timedetail;
        }
    }
}
