package org.genos.gmf.resources.reports;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import javax.naming.NamingException;
import org.genos.gmf.Configuration;
import org.genos.gmf.RequestParameters;
import org.genos.gmf.Core.Core;
import org.genos.gmf.Core.CreateResource;
import org.genos.gmf.Core.PatternString;
import org.genos.gmf.form.Form;
import org.genos.gmf.form.ParameterSourceSQL;
import org.genos.gmf.form.ParameterType;
import org.genos.gmf.form.filters.Filter;
import org.genos.gmf.form.filters.FilterComponent;
import org.genos.gmf.form.filters.ResourceFilter;
import org.genos.gmf.resources.MainWorkArea;
import org.genos.gmf.resources.Resource;
import org.genos.gmf.resources.ResourceContainer;
import org.genos.gmf.resources.WorkArea;
import org.genos.utils.StringUtils;

/**
 * Reports resource.
 */
public class Reports extends ResourceContainer {

    /**
	 * Constructor
	 */
    public Reports() throws Exception {
        super();
        resForm = new Form();
        ParameterType fpt = new ParameterType(ParameterType.PT_TEXT);
        fpt.setFlags(ParameterType.PARAM_OBLIGATORY);
        fpt.setMaxLength(128);
        resForm.addParameter("name", new PatternString(this, "$lang:s_res_reportsname$"), null, fpt, new ParameterSourceSQL(null, "name"));
        resForm.addParameter("description", new PatternString(this, "$lang:s_res_reportsdesc$"), null, new ParameterType(ParameterType.PT_TEXTAREA), new ParameterSourceSQL(null, "description"));
    }

    public String getResourceDescription() throws Exception {
        return Configuration.getLocaleString(uid, "s_res_reports");
    }

    /**
	 * Abstract for building a path.
	 * @param uid		User id.
	 * @return			Html link to this resource.
	 */
    public String executeAbstractPath(int uid) throws Exception {
        return (String) resForm.getPValue("name");
    }

    /**
	 * Create a new Report folder with current Year for this user.
	 * @param tt_owner	Owner
	 * @param ttid	TT id
	 * @param chgid Change Management id
	 */
    public void createYear(String tt_owner, String ttid, String chgid, String full) {
        Connection conn = null;
        try {
            int i = Integer.parseInt(full);
            if (i != 0 && i != 1) throw new Exception("Reports.createYear(): Invalid full value, must be 0 or 1");
            conn = Configuration._dbm.getConnection();
            createYear(conn, tt_owner, ttid, chgid, i);
        } catch (Exception e) {
            Configuration.logger.error("Reports.createYear(): " + e);
        } finally {
            Configuration._dbm.closeConnection(conn);
        }
    }

    /**
	 * Create a new Report folder with current Year for this user.
	 * @param conn		Connection
	 * @param tt_owner	Owner
	 * @param ttid		TT ID
	 * @param chgid		Change Management id
	 * @throws Exception
	 */
    public void createYear(Connection conn, String tt_owner, String ttid, String chgid, int mode) throws Exception {
        GregorianCalendar gc = new GregorianCalendar();
        Hashtable params = null;
        ResourceFilter rf = new ResourceFilter("Reports");
        rf.addComponent(new FilterComponent("name", gc.get(Calendar.YEAR) + "", FilterComponent.EQUAL));
        Filter f = new Filter();
        f.addResourceFilter(rf);
        java.util.LinkedList contains = getContainedResources(f);
        if (contains != null && contains.size() > 0) return;
        Integer newRep = createReports(conn, gc.get(Calendar.YEAR) + "", rid);
        Reports res = (Reports) Core.instantiateResource(conn, "Reports", newRep, new Integer(uid), null);
        for (int i = 12; i >= 1; i--) {
            Thread.sleep(1000);
            res.createMonth(conn, i, gc.get(Calendar.YEAR), tt_owner, ttid, chgid);
        }
        Thread.sleep(5000);
        params = new Hashtable();
        params.put("name", "ListOwnerYear");
        if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
        params.put("year", new Integer(gc.get(Calendar.YEAR)));
        params.put("ttid", ttid);
        if (tt_owner != null && tt_owner.length() != 0) createNewReport(conn, "ListOwnerYear", params, newRep); else createNewReport(conn, "OwnerYear", params, newRep);
        params.put("name", "SLAReportYear");
        createNewReport(conn, "SLAReportYear", params, newRep);
        params = new Hashtable();
        params.put("name", "WOByYear");
        if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
        params.put("year", new Integer(gc.get(Calendar.YEAR)));
        params.put("chgid", chgid);
        createNewReport(conn, "WOByCategory", params, newRep);
        params = new Hashtable();
        params.put("name", "Top10 Machines");
        if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
        params.put("year", new Integer(gc.get(Calendar.YEAR)));
        params.put("ttid", ttid);
        createNewReport(conn, "InventoryTop", params, newRep);
        if (mode == 1) {
            params = new Hashtable();
            params.put("name", "Tickets by Category ");
            if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
            params.put("year", new Integer(gc.get(Calendar.YEAR)));
            params.put("ttid", ttid);
            createNewReport(conn, "TCategoryByYear", params, newRep);
            params = new Hashtable();
            params.put("name", "Tickets New->Closed");
            if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
            params.put("year", new Integer(gc.get(Calendar.YEAR)));
            params.put("ttid", ttid);
            createNewReport(conn, "TicketsNCByYear", params, newRep);
            params = new Hashtable();
            params.put("name", "Tickets resolved average time");
            if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
            params.put("year", new Integer(gc.get(Calendar.YEAR)));
            params.put("ttid", ttid);
            createNewReport(conn, "TicketsResolAvByYear", params, newRep);
            params = new Hashtable();
            params.put("name", "Tickets response average time");
            if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
            params.put("year", new Integer(gc.get(Calendar.YEAR)));
            params.put("ttid", ttid);
            createNewReport(conn, "TicketsResponseAvByYear", params, newRep);
            params = new Hashtable();
            params.put("name", "Tickets created");
            if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
            params.put("year", new Integer(gc.get(Calendar.YEAR)));
            params.put("ttid", ttid);
            createNewReport(conn, "TicketsByYear", params, newRep);
        }
    }

    /**
	 * Create a new Report folder with current month for this user.
	 * @param tt_owner Owner
	 * @param ttid		TT id
	 * @param chgid		Change Management id
	 */
    public void createMonth(String tt_owner, String ttid, String chgid) {
        Connection conn = null;
        try {
            conn = Configuration._dbm.getConnection();
            createMonth(conn, null, null, tt_owner, ttid, chgid);
        } catch (Exception e) {
            Configuration.logger.error("Reports.createMonth(): " + e);
        } finally {
            Configuration._dbm.closeConnection(conn);
        }
    }

    /**
	 * Create a new Report folder with current month for this user.
	 * @param conn  Connection
	 * @param month	Month
	 * @param year	Year
	 * @param tt_owner	Owner
	 * @param ttid		TT id
	 * @param chgid		Change Management id
	 * @throws Exception
	 */
    private synchronized void createMonth(Connection conn, Integer month, Integer year, String tt_owner, String ttid, String chgid) throws Exception {
        GregorianCalendar gc = new GregorianCalendar();
        String parentName = null;
        if (month == null) parentName = (1 + gc.get(Calendar.MONTH)) + "-" + Configuration.getLocaleString(uid, "month_" + (gc.get(Calendar.MONTH) + 1)); else parentName = month + "-" + Configuration.getLocaleString(uid, "month_" + month);
        if (year == null) year = new Integer(gc.get(Calendar.YEAR));
        ResourceFilter rf = new ResourceFilter("Reports");
        rf.addComponent(new FilterComponent("name", parentName, FilterComponent.EQUAL));
        Filter f = new Filter();
        f.addResourceFilter(rf);
        java.util.LinkedList contains = getContainedResources(f);
        if (contains != null && contains.size() > 0) return;
        Integer newRep = createReports(conn, parentName, rid);
        Hashtable params = null;
        params = new Hashtable();
        params.put("name", "ListOwnerMonth");
        if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
        params.put("month", month);
        params.put("year", year);
        params.put("ttid", ttid);
        if (tt_owner != null && tt_owner.length() != 0) createNewReport(conn, "ListOwnerMonth", params, newRep); else createNewReport(conn, "OwnerMonth", params, newRep);
        params.put("name", "SLAReportMonth");
        createNewReport(conn, "SLAReportMonth", params, newRep);
        params = new Hashtable();
        params.put("name", "WOByMonth");
        if (tt_owner != null && tt_owner.length() != 0) params.put("owner", tt_owner);
        params.put("month", month);
        params.put("year", year);
        params.put("chgid", chgid);
        createNewReport(conn, "WOByCategory", params, newRep);
    }

    /**
	 * Creates a new report specified by resdefid with the params inside "params"
	 * @param conn			Connection
	 * @param resdefid		Resource definition id
	 * @param params		Parameters necessary for creating this report
	 * @param parent		Parent resource
	 * @throws Exception
	 */
    private void createNewReport(Connection conn, String resdefid, Hashtable params, Integer parent) throws Exception {
        RequestParameters rp = new RequestParameters();
        long t_start = System.currentTimeMillis();
        rp.put("usercommit", "1");
        for (java.util.Iterator it = params.keySet().iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            rp.put(key, params.get(key));
        }
        Resource res = Core.instantiateResource(resdefid);
        WorkArea wa = new MainWorkArea();
        wa.setRequestParameters(rp);
        res.setWorkArea(wa);
        Object result = CreateResource.createResource(conn, uid, res, parent);
        if (result instanceof Form) {
            Configuration.logger.error("Reports.createNewReport(): couldn't create resource " + resdefid);
        }
        long t_end = System.currentTimeMillis();
        Configuration.logger.debug("Reports.createNewReport(): TIME createResource " + resdefid + ", rid=" + rid + ": " + (t_end - t_start));
    }

    /**
	 * Creates a resource of kind Reports
	 * @param conn
	 * @param name
	 * @param parent
	 * @return
	 * @throws Exception
	 */
    private Integer createReports(Connection conn, String name, int parent) throws Exception {
        RequestParameters rp = new RequestParameters();
        long t_start = System.currentTimeMillis();
        rp.put("usercommit", "1");
        rp.put("name", name);
        Resource res = Core.instantiateResource("Reports");
        WorkArea wa = new MainWorkArea();
        wa.setRequestParameters(rp);
        res.setWorkArea(wa);
        Object result = CreateResource.createResource(conn, uid, res, parent);
        if (result instanceof Form) {
            Configuration.logger.error("Reports.createReports(): couldn't create resource Reports");
            return null;
        }
        long t_end = System.currentTimeMillis();
        Configuration.logger.debug("Reports.createReports(): TIME createResource Reports, rid=" + rid + ": " + (t_end - t_start));
        return (Integer) result;
    }
}
