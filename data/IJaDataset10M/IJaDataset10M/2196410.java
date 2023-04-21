package org.bpmsuite.dao.impl;

import org.bpmsuite.constants.ValueObjectParams;
import org.bpmsuite.dao.AbstractHibernateDao;
import org.bpmsuite.dao.DaoException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * @author Dirk Weiser
 */
public class RetrieveTimerecordByIdDao extends AbstractHibernateDao {

    private Long _id;

    public void doInHibernateTransaction(Session session) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("from TimerecordImpl tr ");
        stringBuffer.append("where tr.id='").append(_id.toString()).append("' ");
        ;
        Query query = session.createQuery(stringBuffer.toString());
        _result = query.uniqueResult();
    }

    protected void initParams() throws DaoException {
        Object id = _params.get(ValueObjectParams.ID);
        if (id == null) {
            throw new DaoException("Mandatory params are null!");
        } else {
            _id = (Long) id;
        }
    }
}
