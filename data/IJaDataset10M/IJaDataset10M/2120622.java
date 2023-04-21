package com.germinus.telcoblocks.echarts.Click2Dial;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.echarts.servlet.sip.EChartsMachineToJava;
import org.hibernate.Session;
import com.germinus.telcoblocks.echarts.db.HibernateUtil;
import javax.servlet.sip.SipApplicationSession;

/**
   Test implementation of the Click2DialFlow1MachineToJava
   interface.
*/
public class Click2DialEChartsMachineToJavaImpl extends EChartsMachineToJava implements Click2DialEChartsMachineToJava {

    private static Logger logger = Logger.getLogger(Click2DialEChartsMachineToJavaImpl.class);

    private static String state = null;

    private static String sasId = null;

    public static void reset() {
        sasId = null;
        state = null;
    }

    public static String getSasId() {
        return sasId;
    }

    public void publish(String sasId) {
        logger.info("Machine published with id " + sasId);
        setSasId(sasId);
    }

    public static void setSasId(String sasId) {
        Click2DialEChartsMachineToJavaImpl.sasId = sasId;
        logger.info("Machine set with id " + sasId);
    }

    public static String getState() {
        return state;
    }

    public static void setState(String state) {
        Click2DialEChartsMachineToJavaImpl.state = state;
    }

    /**
	   {@inheritDoc}
	 */
    public void onConnected(String from, String to) {
        logger.info("Call Status connected call " + from + " to " + to);
        CallStatusContainer calls = CallStatusContainer.getCallStatusContainer();
        Call call = calls.addCall(from, to, "FFFF00");
        com.germinus.telcoblocks.echarts.db.Call callDB = new com.germinus.telcoblocks.echarts.db.Call(from, to);
        callDB.setStatus("connected");
        callDB.setSession(sasId);
        beginTransaction();
        getSession().save(callDB);
        commitTransaction();
        call.addSession(sasId);
        setState("connected");
    }

    /**
	   {@inheritDoc}
	 */
    public void onDisconnected() {
        logger.info("Call Status disconnected");
        CallStatusContainer calls = CallStatusContainer.getCallStatusContainer();
        Call call = calls.getCall(sasId);
        calls.removeCall(call);
        List<com.germinus.telcoblocks.echarts.db.Call> callsList = getCallActives(sasId);
        Iterator<com.germinus.telcoblocks.echarts.db.Call> it = callsList.iterator();
        beginTransaction();
        while (it.hasNext()) {
            com.germinus.telcoblocks.echarts.db.Call callDB = (com.germinus.telcoblocks.echarts.db.Call) it.next();
            callDB.setEndDate(new Date());
            callDB.setStatus("disconnected");
            getSession().update(callDB);
        }
        commitTransaction();
        setState("disconnected");
    }

    private List<com.germinus.telcoblocks.echarts.db.Call> getCallActives(String sasId) {
        beginTransaction();
        List<com.germinus.telcoblocks.echarts.db.Call> callsList = (List<com.germinus.telcoblocks.echarts.db.Call>) getSession().createQuery("FROM Call b where b.session=:session and b.status='connected'").setParameter("session", sasId).list();
        commitTransaction();
        return callsList;
    }

    private Session getSession() {
        return HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public void beginTransaction() {
        getSession().beginTransaction();
    }

    public void commitTransaction() {
        getSession().getTransaction().commit();
    }

    public void rollbackTransaction() {
        getSession().getTransaction().rollback();
    }
}
