package com.ecs.etrade.daextn;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.ecs.etrade.da.Account;
import com.ecs.etrade.da.RcptAsgm;
import com.ecs.etrade.da.RcptAsgmDAO;

/**
 * @author Alok Ranjan
 *
 */
public class RcptAsgmDAOExtn extends RcptAsgmDAO {

    private static final Log log = LogFactory.getLog(RcptAsgmDAOExtn.class);

    /**
	 * 
	 */
    public RcptAsgmDAOExtn() {
    }

    /**
	 * 
	 * @param account
	 * @return
	 */
    public RcptAsgm[] getUnbilledRcptAsgm(Account account) {
        try {
            ArrayList<RcptAsgm> rcptAsgmList = null;
            DetachedCriteria rcptAsgmCriteria = DetachedCriteria.forClass(RcptAsgm.class);
            rcptAsgmCriteria.add(Restrictions.eq("account.accountId", account.getAccountId())).add(Restrictions.isNull("billNbr"));
            rcptAsgmList = (ArrayList<RcptAsgm>) getHibernateTemplate().findByCriteria(rcptAsgmCriteria);
            if (rcptAsgmList != null) {
                return rcptAsgmList.toArray(new RcptAsgm[rcptAsgmList.size()]);
            } else {
                return null;
            }
        } catch (RuntimeException ex) {
            log.error("Failed to retrieve credit assignments for account = " + account.getAccountId());
            throw ex;
        }
    }

    /**
	 * 
	 * @return
	 */
    public Integer getNextRcptAsgmId() {
        try {
            DetachedCriteria rcptAsgmCriteria = DetachedCriteria.forClass(RcptAsgm.class);
            String SQL_QUERY = " select max(rcptAsgmId) from RcptAsgm ";
            List maxRcptAsgmIdList = (ArrayList<RcptAsgm>) getHibernateTemplate().findByNamedQuery(SQL_QUERY);
            Integer nextAvailableNbr = new Integer(1);
            if (maxRcptAsgmIdList != null) {
                nextAvailableNbr += (Integer) maxRcptAsgmIdList.get(0);
            }
            return nextAvailableNbr;
        } catch (RuntimeException ex) {
            log.error("Failed to retrieve next receipt assignment id!");
            throw ex;
        }
    }
}
