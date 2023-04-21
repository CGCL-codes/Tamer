package org.genos.gmf.resources.reports;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.AttributedString;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import org.genos.gmf.Configuration;
import org.genos.gmf.DiskStorage;
import org.genos.gmf.ResPublisher;
import org.genos.gmf.ResourceDesc;
import org.genos.gmf.Core.Core;
import org.genos.gmf.Core.PatternString;
import org.genos.gmf.form.ParameterSourceSQL;
import org.genos.gmf.form.ParameterType;
import org.genos.gmf.form.ParameterTypeSelectExt;
import org.genos.gmf.form.ParameterTypeSelectRD;
import org.genos.gmf.resources.Resource;
import org.genos.gmf.resources.ResourceContainer;
import org.genos.gmf.resources.generic.ldap.LDAPUsers;
import org.genos.gmf.security.GroupMgmt;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 * Class handling jaspert reports.
 */
public class TCategoryByMonth extends JasperReport {

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
    public TCategoryByMonth() throws Exception {
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
        return Configuration.getLocaleString(uid, "s_res_report_tcategorybymonth");
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
                parameters.put("title", new String("Tickets by Category TT=" + ttid + " on " + month + "/" + year));
                parameters.put("barchart", generatePieChart(conn));
                parameters.put("month", month);
                parameters.put("year", year);
            }
        }
        return parameters;
    }

    /**
     * Returns the piechart related to this TT
     * @param conn	Database connection
     * @return
     */
    protected File generatePieChart(Connection conn) {
        Hashtable categories = new Hashtable();
        try {
            String owner = (String) resForm.getPValue("owner");
            String ttrid = (String) resForm.getPValue("ttid");
            ResourceContainer rescont = (ResourceContainer) Core.instantiateResource(conn, "TechSupportTT", Integer.valueOf(ttrid), uid, null);
            if (rescont == null) {
                Configuration.logger.error("TCategoryByMonth.generatePieChart(): Cannot instanciate TT=" + ttrid);
                return new File("");
            }
            LinkedList ll = rescont.getContainedResources(null);
            for (Iterator it = ll.iterator(); it.hasNext(); ) {
                ResourceDesc rd = (ResourceDesc) it.next();
                Resource res = Core.instantiateResource(conn, "TechSupportTTTicket", rd.rid, uid, null);
                if (owner != null && !owner.equals(res.getForm().getPValue("owner"))) continue;
                Date d = (Date) res.getForm().getPValue("creationtime");
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTimeInMillis(d.getTime());
                if ((gc.get(Calendar.MONTH) + 1) != ((Integer) resForm.getPValue("month")).intValue() || gc.get(Calendar.YEAR) != ((Integer) resForm.getPValue("year")).intValue()) continue;
                String category = (String) res.getForm().getPValue("category");
                if (categories.get(category) == null) {
                    categories.put(category, new Integer(1));
                } else {
                    categories.put(category, new Integer(((Integer) categories.get(category)).intValue() + 1));
                }
            }
        } catch (Exception e) {
            Configuration.logger.error("TCategoryByMonth.generatePieChart: " + e.getMessage());
        }
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        for (Iterator it = categories.keySet().iterator(); it.hasNext(); ) {
            String category = (String) it.next();
            Integer value = (Integer) categories.get(category);
            pieDataset.setValue(category, value);
        }
        JFreeChart chart = ChartFactory.createPieChart3D("Tickets by Category", pieDataset, true, false, false);
        PiePlot piePlot = (PiePlot) chart.getPlot();
        piePlot.setStartAngle(15.0);
        piePlot.setCircular(true);
        piePlot.setForegroundAlpha(.7f);
        piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})", NumberFormat.getNumberInstance(), NumberFormat.getPercentInstance()));
        piePlot.setLegendLabelGenerator(new CustomLabelGenerator());
        piePlot.setNoDataMessage("No data available");
        File filename = null;
        try {
            File tmpDir = new File(DiskStorage.getStaticDirectory(uid));
            tmpDir.mkdirs();
            filename = new File(tmpDir + "/piechart-" + rid + ".png");
            ChartUtilities.saveChartAsPNG(filename, chart, 500, 450);
        } catch (Exception e) {
            Configuration.logger.error("TCategoryByMonth.generatePieChart(): Couldn't create pie chart, ", e);
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
            name = "TicketsByMonth-" + rid + "-" + ttid + "-" + month + "-" + year;
        }
        return name;
    }

    /**
     * Returns the file name for the JasperReport file used to generate the report
     */
    public String getJasperFile() {
        return "tickets_category.jrxml";
    }

    /**
     * A custom label generator in order to show only the name category
     */
    protected class CustomLabelGenerator implements PieSectionLabelGenerator {

        /**
         * Generates a label for a pie section.
         * 
         * @param dataset  the dataset (<code>null</code> not permitted).
         * @param key  the section key (<code>null</code> not permitted).
         * 
         * @return the label (possibly <code>null</code>).
         */
        public String generateSectionLabel(final PieDataset dataset, final Comparable key) {
            String result = null;
            if (dataset != null) {
                result = key.toString();
            }
            return result;
        }

        public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
            AttributedString result = null;
            if (dataset != null) {
                result = new AttributedString(key.toString());
            }
            return result;
        }
    }
}
