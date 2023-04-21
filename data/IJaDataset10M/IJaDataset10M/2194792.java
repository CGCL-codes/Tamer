package com.wonebiz.crm.server.dao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import com.isomorphic.datasource.DSRequest;
import com.isomorphic.datasource.DSResponse;
import com.wonebiz.crm.client.entity.VisitManner;
import com.wonebiz.crm.client.entity.VisitTrack;

@Repository("visitDao")
public class VisitDao extends HibernateDaoSupport {

    private static final Logger log = Logger.getLogger(CustomerDao.class);

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public List<VisitManner> queryValidManner() {
        String hql = "from VisitManner visitManner where visitManner.isValid=1 ";
        return this.getHibernateTemplate().find(hql);
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public int querThisMonthQty() {
        log.info("querThisMonthQty 运行...");
        int result = 0;
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.set(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date firstDay = c.getTime();
        String firstString = firstDay.toLocaleString();
        String todayString = today.toLocaleString();
        log.debug("firstString:" + firstString);
        log.debug("todayString:" + todayString);
        String sql = "select count(*) from Visit where date  between '" + firstString + "' and '" + todayString + "'";
        log.info("查询新增拜访量sql:" + sql);
        List list = this.getHibernateTemplate().find(sql);
        Number l = (Number) list.get(0);
        result = Integer.valueOf(l.toString());
        log.info("querThisMonthQty 运行结束, 当月新增拜访量 :" + result);
        return result;
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public DSResponse queryTrack(DSRequest dsRequest) {
        log.info("运行 queryTrack()...");
        DSResponse dsResponse = new DSResponse();
        try {
            Session session = sessionFactory.getCurrentSession();
            String mannerList = "";
            String userList = "";
            log.debug("看这里：测试输出 manner :" + dsRequest.getCriteria().get("manner"));
            if (!dsRequest.getCriteria().get("manner").toString().equals("ALL")) {
                List manner = (List) dsRequest.getCriteria().get("manner");
                log.debug("manner.size() : " + manner.size());
                String var0 = "";
                for (int i = 0; i < manner.size(); i++) {
                    log.info(manner.get(i).toString());
                    if (i == 0) {
                        var0 = "where manner = " + manner.get(i);
                    }
                    if (i >= 1) {
                        var0 = var0 + " or manner = " + manner.get(i);
                    }
                }
                log.debug("动态拼接的manner条件语句为 :" + var0);
                mannerList = var0;
            }
            List owner = (List) dsRequest.getCriteria().get("owner");
            log.debug("owner.size() : " + owner.size());
            String var = "";
            for (int i = 0; i < owner.size(); i++) {
                log.debug(owner.get(i).toString());
                if (i == 0) {
                    var = "id = " + owner.get(i);
                }
                if (i >= 1) {
                    var = var + " or id = " + owner.get(i);
                }
            }
            log.debug("动态拼接的条件语句为 :" + var);
            userList = "(select id from user where( " + var + ") ) u ";
            Date from = (Date) dsRequest.getCriteria().get("from");
            Date to = (Date) dsRequest.getCriteria().get("to");
            long l = (to.getTime() - from.getTime()) / 1000 / 60 / 60 / 24;
            String isWork = (String) dsRequest.getCriteria().get("isWork");
            String days = "";
            if ("1".equals(isWork)) {
                log.info("按照工作日计算");
                Calendar c = Calendar.getInstance();
                c.setTime(from);
                int startWeek = c.get(Calendar.DAY_OF_WEEK);
                log.debug("开始星期号为 :" + startWeek);
                Map<Integer, Integer> m = new HashMap<Integer, Integer>();
                for (int j = 1; j <= l; j++) {
                    if (startWeek != 1 && startWeek != 7) {
                        m.put(j, startWeek);
                    }
                    startWeek++;
                    if (startWeek > 7) {
                        startWeek = 1;
                    }
                }
                days = String.valueOf(m.size());
            }
            if ("0".equals(isWork)) {
                log.info("按照自然日计算");
                days = String.valueOf(l);
            }
            log.debug("计算出的天数为:" + days);
            String fromString = from.toLocaleString();
            String toString = to.toLocaleString();
            String sql = "select u.id, total ,isFirst , notFirst from " + userList + " left join " + "(select bywhom,count(*)as 'total' from (select * from visit where date between '" + fromString + "' and '" + toString + "')as aa " + mannerList + " group by bywhom)a " + "on u.id = a.byWhom " + "left join " + "(select bywhom,count(*)as 'isfirst' from (select * from visit where isfirsttime=1 and date between '" + fromString + "' and '" + toString + "')as bb " + mannerList + " group by bywhom)b " + "on u.id=b.bywhom " + "left join " + "(select bywhom,count(*)as 'notfirst' from (select * from visit where isfirsttime=0 and date between '" + fromString + "' and '" + toString + "')as cc " + mannerList + " group by bywhom)c " + " on u.id=c.bywhom";
            List list = session.createSQLQuery(sql).list();
            List<VisitTrack> result = new ArrayList<VisitTrack>();
            for (int i = 0; i < list.size(); i++) {
                Object[] o = (Object[]) list.get(i);
                String byWhom = "";
                String total = "";
                String isFirst = "";
                String notFirst = "";
                if (o[0] != null) {
                    byWhom = o[0].toString();
                }
                if (o[1] != null) {
                    total = o[1].toString();
                }
                if (o[2] != null) {
                    isFirst = o[2].toString();
                }
                if (o[3] != null) {
                    notFirst = o[3].toString();
                }
                VisitTrack vt = new VisitTrack();
                vt.setDays(days);
                vt.setByWhom(byWhom);
                vt.setTotal(total);
                vt.setIsFirst(isFirst);
                vt.setNotFirst(notFirst);
                String avg = "";
                if (!"".equals(total)) {
                    if (!days.equals("0")) {
                        float t = Float.valueOf(total);
                        float d = Float.valueOf(days);
                        DecimalFormat df2 = new DecimalFormat("###0.00%");
                        avg = String.valueOf(df2.format((t / d) / 100));
                        log.debug("日均拜访量 : " + avg);
                    }
                }
                vt.setDailyAvg(avg);
                result.add(vt);
            }
            dsResponse.setData(result);
        } catch (Exception e) {
            log.fatal("queryTrack()运行异常:" + e.toString());
        }
        log.info("queryTrack()运行结束...");
        return dsResponse;
    }
}
