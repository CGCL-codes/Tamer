package org.genos.gmf.resources.reports;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.genos.gmf.Configuration;
import org.genos.gmf.DiskStorage;
import org.genos.gmf.ResPublisher;
import org.genos.gmf.Core.PatternString;
import org.genos.gmf.form.ParameterSourceSQL;
import org.genos.gmf.form.ParameterType;
import org.genos.gmf.form.ParameterTypeSelectExt;
import org.genos.gmf.form.ParameterTypeSelectRD;
import org.genos.gmf.resources.generic.ldap.LDAPUsers;
import org.genos.gmf.security.GroupMgmt;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * Class handling jaspert reports.
 */
public class TicketsResponseAvByMonth extends JasperReport {

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
    public TicketsResponseAvByMonth() throws Exception {
        super();
        ParameterTypeSelectRD fpts = new ParameterTypeSelectRD();
        fpts.setFlags(ParameterType.PARAM_OBLIGATORY);
        fpts.setMaxLength(32);
        resForm.addParameter("ttid", new PatternString(this, "$lang:s_res_reportstt$"), null, fpts, new ParameterSourceSQL(null, "ttid"));
        FPTSOwner fpo = new FPTSOwner();
        fpo.setFlags(ParameterType.PARAM_OPTIONAL);
        resForm.addParameter("owner", new PatternString(this, "$lang:s_res_reportsowner$"), null, fpo, new ParameterSourceSQL(null, "owner"));
        ParameterType fpt = new ParameterType(ParameterType.PT_INTEGER);
        fpt.setFlags(ParameterType.PARAM_OBLIGATORY);
        fpt.setMaxLength(32);
        resForm.addParameter("month", new PatternString(this, "$lang:s_res_reportsmonth$"), null, fpt, new ParameterSourceSQL(null, "month"));
        fpt = new ParameterType(ParameterType.PT_INTEGER);
        fpt.setFlags(ParameterType.PARAM_OBLIGATORY);
        fpt.setMaxLength(32);
        resForm.addParameter("year", new PatternString(this, "$lang:s_res_reportsyear$"), null, fpt, new ParameterSourceSQL(null, "year"));
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
        return Configuration.getLocaleString(uid, "s_res_report_tresponseavbymonth");
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
            if (year != null) {
                parameters = new HashMap();
                parameters.put("rid", ttid);
                parameters.put("title", new String("Average Response for TT=" + ttid + " on " + month + "-" + year));
                parameters.put("barchart", generateBarChart(conn));
                parameters.put("month", month);
                parameters.put("year", year);
            }
        }
        return parameters;
    }

    /**
     * Returns the barchart related to this TT
     * @param conn	Database connection
     * @return
     */
    private File generateBarChart(Connection conn) {
        Vector seriesName = new Vector();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            String owner = (String) resForm.getPValue("owner");
            StringBuilder sql = new StringBuilder();
            Statement st = conn.createStatement();
            sql.append("select category " + "from sta_tickets_average where year(creation)='" + resForm.getPValue("year") + "'" + " and month(creation)='" + resForm.getPValue("month") + "'" + " and ttrid='" + resForm.getPValue("ttid") + "'");
            if (owner != null) sql.append(" and tt_owner='" + owner + "'");
            sql.append(" group by creation");
            sql.append(" order by creation");
            ResultSet rset = st.executeQuery(sql.toString());
            ArrayList categories = new ArrayList();
            while (rset.next()) categories.add(rset.getString("category"));
            rset.close();
            for (Iterator it = categories.iterator(); it.hasNext(); ) {
                String serie = (String) it.next();
                seriesName.add(serie);
                sql = new StringBuilder();
                sql.append("select creation, response " + "from sta_tickets_average where year(creation)='" + resForm.getPValue("year") + "'" + " and month(creation)='" + resForm.getPValue("month") + "'" + " and ttrid='" + resForm.getPValue("ttid") + "' and category='" + serie + "'");
                if (owner != null) sql.append(" and tt_owner='" + owner + "'");
                ResultSet rset2 = st.executeQuery(sql.toString());
                GregorianCalendar gc = new GregorianCalendar();
                gc.clear();
                gc.set(Integer.parseInt(resForm.getPValue("year").toString()), Integer.parseInt(resForm.getPValue("month").toString()) - 1, 1);
                int maxdia = gc.getActualMaximum(Calendar.DAY_OF_MONTH);
                String prefix = null;
                if (Integer.parseInt(resForm.getPValue("month").toString()) < 10) prefix = resForm.getPValue("year") + "-0" + resForm.getPValue("month") + "-"; else prefix = resForm.getPValue("year") + "-" + resForm.getPValue("month") + "-";
                for (int i = 1; i <= maxdia; i++) {
                    if (i < 10) dataset.setValue(new Integer(0), serie, prefix + "0" + i); else dataset.setValue(new Integer(0), serie, prefix + i);
                }
                while (rset2.next()) {
                    dataset.setValue(rset2.getFloat("response"), serie, rset2.getObject("creation") + "");
                }
                rset2.close();
            }
        } catch (Exception e) {
            Configuration.logger.error("TicketsResponseAvByMonth.generateBarChart(): " + e.getMessage());
        }
        String ttid = null;
        try {
            ttid = (String) resForm.getPValue("ttid");
        } catch (Exception e) {
        }
        JFreeChart chart = ChartFactory.createBarChart3D("Avg Response Time TTID=" + ttid, "Day", "Avg Times in Minutes", dataset, PlotOrientation.HORIZONTAL, true, true, false);
        File filename = null;
        try {
            File tmpDir = new File(DiskStorage.getStaticDirectory(uid));
            tmpDir.mkdirs();
            filename = new File(tmpDir + "/barchart-" + rid + ".png");
            ChartUtilities.saveChartAsPNG(filename, chart, 500, 450);
        } catch (Exception e) {
            Configuration.logger.error("TicketsResponseAvByMonth.generateBarChart(): Couldn't create barchart, ", e);
        }
        return filename;
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
            name = "TicketsRespAvgByMonth-" + rid + "-" + ttid + "-" + month + "-" + year;
        }
        return name;
    }

    /**
     * Returns the file name for the JasperReport file used to generate the report
     */
    public String getJasperFile() {
        return "tickets_number_month.jrxml";
    }
}
