package org.vardb.util.logging.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.vardb.util.CStringHelper;
import org.vardb.util.dao.CAbstractDaoImpl;

public class CLoggingDaoImpl extends CAbstractDaoImpl implements ILoggingDao {

    public void addRequestLogItem(IRequestLogItem item) {
        save(item);
    }

    public void addLoginLogItem(ILoginLogItem item) {
        save(item);
    }

    public void addActivityLogItem(IActivityLogItem item) {
        save(item);
    }

    public void addChangeLogItem(IChangeLogItem item) {
        save(item);
    }

    public List<IRequestLogItem> getRequestLogItems(Date startdate, Date enddate) {
        return getRequestLogItems(new CLogReport.Params(startdate, enddate));
    }

    @SuppressWarnings("unchecked")
    public List<IRequestLogItem> getRequestLogItems(final CLogReport.Params params) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("from CLogItem as logitem\n");
        buffer.append("where 1=1\n");
        if (params.getStartdate() != null) buffer.append("and logitem.date>:startdate\n");
        if (params.getEnddate() != null) buffer.append("and logitem.date<=:enddate\n");
        addParameter(buffer, "user_id", params.getUser_id());
        addParameter(buffer, "ipaddress", params.getIpaddress());
        addParameter(buffer, "browser", params.getBrowser());
        addParameter(buffer, "url", params.getUrl());
        Map<String, Object> model = new HashMap<String, Object>();
        addParameter(model, "startdate", params.getStartdate());
        addParameter(model, "enddate", params.getEnddate());
        addParameter(model, "user_id", params.getUser_id());
        addParameter(model, "ipaddress", params.getIpaddress());
        addParameter(model, "browser", params.getBrowser());
        addParameter(model, "url", params.getUrl());
        Query query = getSession().createQuery("select count(*) " + buffer.toString());
        query.setProperties(model);
        Long total = (Long) (query.uniqueResult());
        params.setTotal(total.intValue());
        System.out.println("total=" + params.getTotal());
        query = getSession().createQuery("select logitem\n" + buffer.toString());
        query.setProperties(model);
        if (params.getPagesize() != null) {
            System.out.println("start=" + params.getStart() + ", number=" + params.getPagesize());
            query.setFirstResult(params.getStart());
            query.setMaxResults(params.getPagesize());
        }
        return query.list();
    }

    private void addParameter(StringBuilder buffer, String property, String value) {
        if (CStringHelper.hasContent(value)) buffer.append("and logitem." + property + "=:" + property + "\n");
    }

    private void addParameter(Map<String, Object> model, String property, Object value) {
        if (CStringHelper.hasContent(value)) model.put(property, value);
    }

    @SuppressWarnings("unchecked")
    public List<IErrorLogItem> getErrors(Date startdate, Date enddate) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("select item\n");
        buffer.append("from CErrorLogItem as item\n");
        buffer.append("where item.date>=:startdate\n");
        buffer.append("and item.date<=:enddate\n");
        buffer.append("order by item.date desc\n");
        return findByNamedParam(buffer, "startdate", startdate, "enddate", enddate);
    }

    public IErrorLogItem getErrorLogItem(int id) {
        return (CErrorLogItem) get(CErrorLogItem.class, id);
    }

    public void addErrorLogItem(IErrorLogItem item) {
        try {
            save(item);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
