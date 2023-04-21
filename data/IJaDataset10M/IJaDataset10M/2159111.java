package org.genos.gmf.resources.reports;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.genos.gmf.Configuration;
import org.genos.gmf.ResPublisher;
import org.genos.gmf.Core.PatternString;
import org.genos.gmf.form.ParameterSourceSQL;
import org.genos.gmf.form.ParameterType;
import org.genos.gmf.form.ParameterTypeSelectExt;
import org.genos.gmf.form.ParameterTypeSelectRD;
import org.genos.gmf.resources.generic.ldap.LDAPUsers;
import org.genos.gmf.security.GroupMgmt;

/**
 * Class handling jaspert reports.
 */
public class ListOwnerMonth extends JasperReport {

    private HashMap parameters = null;

    private String name = null;

    /**
     * Specific ParameterTypeSelect class for owner field, to populate only
     * when it's necessary.
     */
    private class FPTSOwner extends ParameterTypeSelectExt {

        /**
         * Populated flag.
         */
        private boolean populated = false;

        /**
         * Constructor.
         * 
         * @throws Exception
         */
        public FPTSOwner() throws Exception {
            super();
        }

        /**
         * Populate select field from option sources.
         */
        protected void populateSourceOptions(Connection conn) throws SQLException, Exception {
            if (populated) return;
            String dn;
            String caption;
            ArrayList e = new ArrayList(GroupMgmt.getAllUsers().keySet());
            for (Iterator it = e.iterator(); it.hasNext(); ) {
                dn = (String) it.next();
                caption = LDAPUsers.sLookupUserDN(conn, uid, dn, Configuration.ldapuserdisplayattr);
                addSelectOption(dn, caption);
            }
            populated = true;
        }
    }

    /**
	 * Constructor
	 */
    public ListOwnerMonth() throws Exception {
        super();
        FPTSOwner fpo = new FPTSOwner();
        fpo.setFlags(ParameterType.PARAM_OBLIGATORY);
        resForm.addParameter("owner", new PatternString(this, "$lang:s_res_reportsowner$"), null, fpo, new ParameterSourceSQL(null, "owner"));
        ParameterType fpt;
        fpt = new ParameterType(ParameterType.PT_INTEGER);
        fpt.setFlags(ParameterType.PARAM_OBLIGATORY);
        fpt.setMaxLength(32);
        resForm.addParameter("month", new PatternString(this, "$lang:s_res_reportsmonth$"), null, fpt, new ParameterSourceSQL(null, "month"));
        fpt = new ParameterType(ParameterType.PT_INTEGER);
        fpt.setFlags(ParameterType.PARAM_OBLIGATORY);
        fpt.setMaxLength(32);
        resForm.addParameter("year", new PatternString(this, "$lang:s_res_reportsyear$"), null, fpt, new ParameterSourceSQL(null, "year"));
        ParameterTypeSelectRD fpts = new ParameterTypeSelectRD();
        fpts.setFlags(ParameterType.PARAM_OBLIGATORY);
        fpts.setMaxLength(32);
        resForm.addParameter("ttid", new PatternString(this, "$lang:s_res_reportstt$"), null, fpts, new ParameterSourceSQL(null, "ttid"));
    }

    /**
     * Init method.
     * @param conn  Database connection
     * @param irid  Resource id.
     * @param iuid  User id.
     * @throws Exception
     */
    public void init(Connection conn, Integer irid, Integer iuid) throws Exception {
        super.init(conn, irid, iuid);
        ParameterTypeSelectRD fpt = (ParameterTypeSelectRD) resForm.getComponentStorage("ttid").getType();
        Integer aux = irid;
        if (aux == null) aux = new Integer(getParent());
        fpt.addSourceOptions(aux, iuid.intValue(), "TTNAME", ResPublisher.SCOPE_GLOBAL);
    }

    /**
	 * Tells us if we have to re-generate the report or not.
     * @param conn  Database connection.
	 * @return True to generate the report from scratch.
	 */
    protected boolean generate(Connection conn) throws Exception {
        Calendar now = new GregorianCalendar();
        Map p = getParameters(conn);
        boolean currentDate = (now.get(Calendar.MONTH) + 1 == ((Integer) p.get("month")).intValue()) && (now.get(Calendar.YEAR) == ((Integer) p.get("year")).intValue());
        if (currentDate) return true;
        return super.generate(conn);
    }

    /**
     * Resource description.
     */
    public String getResourceDescription() throws Exception {
        return Configuration.getLocaleString(uid, "s_res_listownermonth");
    }

    /**
     * Return the parameters from this report or null if it haven't any
     * @param conn  Database connection.
     * @return      Map of parameters for the report.
     */
    public Map getParameters(Connection conn) throws Exception {
        if (parameters == null) {
            Integer month = (Integer) resForm.getPValue("month");
            Integer year = (Integer) resForm.getPValue("year");
            Integer ttid = Integer.valueOf((String) resForm.getPValue("ttid"));
            String owner = (String) resForm.getPValue("owner");
            if (year != null) {
                parameters = new HashMap();
                parameters.put("year", year);
                parameters.put("month", month);
                parameters.put("ttid", ttid);
                parameters.put("owner", owner);
                System.setProperty("jasper.reports.compile.class.path", Configuration.homeLib + Configuration.JasperReportsLib);
                System.setProperty("jasper.reports.compile.temp", Configuration.homeTemp);
                net.sf.jasperreports.engine.JasperReport jasperReport = null;
                try {
                    JasperDesign jasperDesign = JRXmlLoader.load(new File(Configuration.homeConfig + "/reports/technicians_time.jrxml"));
                    jasperReport = JasperCompileManager.compileReport(jasperDesign);
                } catch (Exception e) {
                    Configuration.logger.error("JasperReport.execute(): could'nt execute subreport: ", e);
                }
                parameters.put("SUBREPORT", jasperReport);
            }
        }
        return parameters;
    }

    /**
     * Return the name for this report, this will be the name for the file that will contain
     * the files, but without the extension
     * @param conn  Database connection.
     * @return      Report's name.
     */
    public String getNameFile(Connection conn) throws Exception {
        if (name == null) {
            Integer month = (Integer) resForm.getPValue("month");
            Integer year = (Integer) resForm.getPValue("year");
            Integer ttid = Integer.valueOf((String) resForm.getPValue("ttid"));
            name = "ListTicketsByOwner-" + rid + "-" + ttid + "-" + month + "-" + year;
        }
        return name;
    }

    /**
     * Returns the file name for the JasperReport file used to generate the report
     */
    public String getJasperFile() {
        return "list_tickets_owner_month.jrxml";
    }
}
