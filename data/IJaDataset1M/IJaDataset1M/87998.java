package org.campware.cream.modules.screens;

import java.util.List;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import org.apache.turbine.Turbine;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.security.AccessControlList;
import org.apache.torque.util.Criteria;
import org.campware.cream.om.ContactPeer;
import org.campware.cream.om.InboxEventPeer;
import org.campware.cream.om.OutboxEventPeer;
import org.campware.cream.om.TaskPeer;
import org.campware.cream.om.CustomerPeer;
import org.campware.cream.om.ProductPeer;

/**
 * To read comments for this class, please see
 * the ancestor class
 */
public class TaskList extends CreamList {

    protected void initScreen() {
        setModuleType(DOCUMENT);
        setModuleName("TASK");
        setIdName(TaskPeer.TASK_ID);
        setDefOrderColumn(TaskPeer.CREATED);
    }

    protected String getSortColumn(int sortNo) {
        if (sortNo == 1) {
            return TaskPeer.TASK_CODE;
        } else if (sortNo == 2) {
            return TaskPeer.SUBJECT;
        } else if (sortNo == 3) {
            return CustomerPeer.CUSTOMER_DISPLAY;
        } else if (sortNo == 4) {
            return TaskPeer.ASSIGNED_TO;
        } else if (sortNo == 5) {
            return TaskPeer.PROGRESS;
        } else if (sortNo == 6) {
            return TaskPeer.DUE_DATE;
        }
        return "";
    }

    protected void setFilter(int filterNo, Criteria listCriteria, RunData data) {
        int nowDay, nowMonth, nowYear;
        Calendar rightNow = Calendar.getInstance();
        nowMonth = rightNow.get(Calendar.MONTH);
        nowYear = rightNow.get(Calendar.YEAR);
        nowDay = rightNow.get(Calendar.DAY_OF_MONTH);
        rightNow.set(nowYear, nowMonth, nowDay);
        Date fToday = rightNow.getTime();
        rightNow.set(nowYear, nowMonth, 1, 0, 0, 0);
        Date fDayThisMonth = rightNow.getTime();
        rightNow.set(nowYear, nowMonth + 1, 1, 0, 0, 0);
        Date fDayNextMonth = rightNow.getTime();
        rightNow.set(nowYear, nowMonth - 1, 1, 0, 0, 0);
        Date fDayLastMonth = rightNow.getTime();
        rightNow.set(nowYear, 1, 1, 0, 0, 0);
        Date fDayThisYear = rightNow.getTime();
        rightNow.set(nowYear + 1, 1, 1, 0, 0, 0);
        Date fDayNextYear = rightNow.getTime();
        rightNow.set(nowYear - 1, 1, 1, 0, 0, 0);
        Date fDayLastYear = rightNow.getTime();
        try {
            if (filterNo == 1001) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(TaskPeer.ACCESS, new Integer(50), Criteria.EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(TaskPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
                Criteria.Criterion b3 = listCriteria.getNewCriterion(TaskPeer.ASSIGNED_TO, (Object) data.getUser().getName(), Criteria.EQUAL);
                listCriteria.add(b1.or(b2.or(b3)));
                listCriteria.add(TaskPeer.DUE_DATE, fToday, Criteria.EQUAL);
            } else if (filterNo == 1002) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(TaskPeer.ACCESS, new Integer(50), Criteria.EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(TaskPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
                Criteria.Criterion b3 = listCriteria.getNewCriterion(TaskPeer.ASSIGNED_TO, (Object) data.getUser().getName(), Criteria.EQUAL);
                listCriteria.add(b1.or(b2.or(b3)));
                Criteria.Criterion a1 = listCriteria.getNewCriterion(TaskPeer.DUE_DATE, fDayThisMonth, Criteria.GREATER_EQUAL);
                Criteria.Criterion a2 = listCriteria.getNewCriterion(TaskPeer.DUE_DATE, fDayNextMonth, Criteria.LESS_THAN);
                listCriteria.add(a1.and(a2));
            } else if (filterNo == 1003) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(TaskPeer.ACCESS, new Integer(50), Criteria.EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(TaskPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
                Criteria.Criterion b3 = listCriteria.getNewCriterion(TaskPeer.ASSIGNED_TO, (Object) data.getUser().getName(), Criteria.EQUAL);
                listCriteria.add(b1.or(b2.or(b3)));
                listCriteria.add(TaskPeer.DUE_DATE, (Object) "DUE_DATE is not null", Criteria.CUSTOM);
            } else if (filterNo == 1004) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(TaskPeer.ACCESS, new Integer(50), Criteria.EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(TaskPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
                Criteria.Criterion b3 = listCriteria.getNewCriterion(TaskPeer.ASSIGNED_TO, (Object) data.getUser().getName(), Criteria.EQUAL);
                listCriteria.add(b1.or(b2.or(b3)));
                listCriteria.add(TaskPeer.DUE_DATE, (Object) "DUE_DATE is null", Criteria.CUSTOM);
            } else if (filterNo == 1005) {
                listCriteria.add(TaskPeer.ASSIGNED_TO, (Object) data.getUser().getName(), Criteria.EQUAL);
            } else if (filterNo == 1006) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(TaskPeer.ACCESS, new Integer(50), Criteria.EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(TaskPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
                Criteria.Criterion b3 = listCriteria.getNewCriterion(TaskPeer.ASSIGNED_TO, (Object) data.getUser().getName(), Criteria.EQUAL);
                listCriteria.add(b1.or(b2.or(b3)));
                listCriteria.add(TaskPeer.STATUS, new Integer(10), Criteria.EQUAL);
            } else if (filterNo == 1007) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(TaskPeer.ACCESS, new Integer(50), Criteria.EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(TaskPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
                Criteria.Criterion b3 = listCriteria.getNewCriterion(TaskPeer.ASSIGNED_TO, (Object) data.getUser().getName(), Criteria.EQUAL);
                listCriteria.add(b1.or(b2.or(b3)));
                listCriteria.add(TaskPeer.STATUS, new Integer(30), Criteria.EQUAL);
            } else if (filterNo == 1008) {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(TaskPeer.ACCESS, new Integer(50), Criteria.EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(TaskPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
                Criteria.Criterion b3 = listCriteria.getNewCriterion(TaskPeer.ASSIGNED_TO, (Object) data.getUser().getName(), Criteria.EQUAL);
                listCriteria.add(b1.or(b2.or(b3)));
                listCriteria.add(TaskPeer.STATUS, new Integer(50), Criteria.EQUAL);
            } else if (filterNo == 1009) {
                listCriteria.add(TaskPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
            } else {
                Criteria.Criterion b1 = listCriteria.getNewCriterion(TaskPeer.ACCESS, new Integer(50), Criteria.EQUAL);
                Criteria.Criterion b2 = listCriteria.getNewCriterion(TaskPeer.CREATED_BY, (Object) data.getUser().getName(), Criteria.EQUAL);
                Criteria.Criterion b3 = listCriteria.getNewCriterion(TaskPeer.ASSIGNED_TO, (Object) data.getUser().getName(), Criteria.EQUAL);
                listCriteria.add(b1.or(b2.or(b3)));
            }
            listCriteria.addAscendingOrderByColumn(TaskPeer.STATUS);
            listCriteria.addAscendingOrderByColumn(TaskPeer.DUE_DATE);
        } catch (Exception e) {
        }
    }

    protected void setFind(String findStr, Criteria listCriteria) {
        try {
            listCriteria.add(TaskPeer.SUBJECT, (Object) findStr, Criteria.LIKE);
        } catch (Exception e) {
        }
    }

    protected List getEntries(Criteria criteria) {
        try {
            criteria.addJoin(CustomerPeer.CUSTOMER_ID, TaskPeer.CUSTOMER_ID);
            return TaskPeer.doSelect(criteria);
        } catch (Exception e) {
            return null;
        }
    }

    protected boolean isAuthorized(RunData data) throws Exception {
        initScreen();
        boolean isAuthorized = false;
        if (data.getUser().hasLoggedIn()) {
            isAuthorized = true;
        } else {
            data.setScreenTemplate(Turbine.getConfiguration().getString("template.login"));
            isAuthorized = false;
        }
        return isAuthorized;
    }
}
