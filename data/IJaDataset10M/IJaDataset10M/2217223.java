package org.campware.dream.modules.screens;

import java.util.List;
import java.util.Date;
import java.util.Calendar;
import org.apache.turbine.util.RunData;
import org.apache.torque.util.Criteria;
import org.campware.dream.om.DshipmentPeer;
import org.campware.dream.om.DistributorPeer;
import org.campware.dream.om.DorderPeer;
import org.campware.dream.om.ProductPeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class DshipmentList extends CreamList {

    protected void initScreen() {
        setModuleType(DOCUMENT);
        setModuleName("DSHIPMENT");
        setIdName(DshipmentPeer.DSHIPMENT_ID);
        setDefOrderColumn(DshipmentPeer.ISSUED_DATE);
    }

    protected String getSortColumn(int sortNo) {
        if (sortNo == 1) {
            return DshipmentPeer.DSHIPMENT_CODE;
        } else if (sortNo == 2) {
            return DistributorPeer.DISTRIBUTOR_DISPLAY;
        } else if (sortNo == 3) {
            return ProductPeer.PRODUCT_DISPLAY;
        } else if (sortNo == 4) {
            return DorderPeer.DORDER_CODE;
        } else if (sortNo == 5) {
            return DshipmentPeer.ISSUED_DATE;
        }
        return "";
    }

    protected void setFilter(int filterNo, Criteria listCriteria, RunData data) {
        int nowDay, nowMonth, nowYear;
        Calendar rightNow = Calendar.getInstance();
        nowMonth = rightNow.get(Calendar.MONTH);
        nowYear = rightNow.get(Calendar.YEAR);
        rightNow.set(nowYear, nowMonth, 1);
        Date fDayThisMonth = rightNow.getTime();
        rightNow.set(nowYear, nowMonth + 1, 1);
        Date fDayNextMonth = rightNow.getTime();
        rightNow.set(nowYear, nowMonth - 1, 1);
        Date fDayLastMonth = rightNow.getTime();
        rightNow.set(nowYear, 1, 1);
        Date fDayThisYear = rightNow.getTime();
        rightNow.set(nowYear + 1, 1, 1);
        Date fDayNextYear = rightNow.getTime();
        rightNow.set(nowYear - 1, 1, 1);
        Date fDayLastYear = rightNow.getTime();
        try {
            if (filterNo == 1001) {
                listCriteria.add(DshipmentPeer.ISSUED_DATE, new Date(), Criteria.EQUAL);
            } else if (filterNo == 1002) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(DshipmentPeer.ISSUED_DATE, fDayThisMonth, Criteria.GREATER_EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(DshipmentPeer.ISSUED_DATE, fDayNextMonth, Criteria.LESS_THAN);
                listCriteria.add(b1.and(b2));
            } else if (filterNo == 1003) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(DshipmentPeer.ISSUED_DATE, fDayLastMonth, Criteria.GREATER_EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(DshipmentPeer.ISSUED_DATE, fDayThisMonth, Criteria.LESS_THAN);
                listCriteria.add(b1.and(b2));
            } else if (filterNo == 1004) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(DshipmentPeer.ISSUED_DATE, fDayThisYear, Criteria.GREATER_EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(DshipmentPeer.ISSUED_DATE, fDayNextYear, Criteria.LESS_THAN);
                listCriteria.add(b1.and(b2));
            } else if (filterNo == 1005) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(DshipmentPeer.ISSUED_DATE, fDayLastYear, Criteria.GREATER_EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(DshipmentPeer.ISSUED_DATE, fDayThisYear, Criteria.LESS_THAN);
                listCriteria.add(b1.and(b2));
            } else if (filterNo == 1006) {
                listCriteria.add(DshipmentPeer.STATUS, new Integer(10), Criteria.EQUAL);
            } else if (filterNo == 1007) {
                listCriteria.add(DshipmentPeer.STATUS, new Integer(30), Criteria.EQUAL);
            } else if (filterNo == 1008) {
                listCriteria.add(DshipmentPeer.STATUS, new Integer(50), Criteria.EQUAL);
            } else if (filterNo == 1009) {
                listCriteria.add(DshipmentPeer.CREATED_BY, (Object) data.getUser().getUserName(), Criteria.EQUAL);
            }
        } catch (Exception e) {
        }
    }

    protected void setFind(String findStr, Criteria listCriteria) {
        try {
            listCriteria.add(DshipmentPeer.DSHIPMENT_CODE, (Object) findStr, Criteria.LIKE);
        } catch (Exception e) {
        }
    }

    protected List getEntries(Criteria criteria) {
        try {
            criteria.addJoin(DistributorPeer.DISTRIBUTOR_ID, DshipmentPeer.DISTRIBUTOR_ID);
            criteria.addJoin(DorderPeer.DORDER_ID, DshipmentPeer.DORDER_ID);
            criteria.addJoin(ProductPeer.PRODUCT_ID, DorderPeer.PRODUCT_ID);
            return DshipmentPeer.doSelect(criteria);
        } catch (Exception e) {
            return null;
        }
    }
}
