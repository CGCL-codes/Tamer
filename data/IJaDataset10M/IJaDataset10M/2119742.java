package org.genos.gmf.resources.reports;

import java.sql.Connection;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.genos.gmf.Configuration;
import org.genos.gmf.Core.PatternString;
import org.genos.gmf.form.ParameterSourceSQL;
import org.genos.gmf.form.ParameterType;

/**
 * Class handling jaspert reports.
 */
public class ProjectManager extends JasperReport {

    private HashMap parameters = null;

    private String name = null;

    /**
	 * Constructor
	 */
    public ProjectManager() throws Exception {
        super();
        ParameterType fpt = new ParameterType(ParameterType.PT_INTEGER);
        fpt.setFlags(ParameterType.PARAM_OBLIGATORY);
        fpt.setMaxLength(32);
        resForm.addParameter("year", new PatternString(this, "$lang:s_res_reportsyear$"), null, fpt, new ParameterSourceSQL(null, "year"));
    }

    /**
     * Tells us if we have to re-generate the report or not.
     * @param conn  Database connection.
     * @return True to generate the report from scratch.
     */
    protected boolean generate(Connection conn) throws Exception {
        Calendar now = new GregorianCalendar();
        Map p = getParameters(conn);
        boolean currentDate = (now.get(Calendar.YEAR) == ((Integer) p.get("year")).intValue());
        if (currentDate) return true;
        return super.generate(conn);
    }

    /**
     * Resource description.
     */
    public String getResourceDescription() throws Exception {
        return Configuration.getLocaleString(uid, "s_res_projectsbymanager");
    }

    /**
     * Return the name for this report, this will be the name for the file that will contain
     * the files, but without the extension
     * @param conn  Database connection.
     * @return      Report's name.
     */
    public String getNameFile(Connection conn) throws Exception {
        if (name == null) {
            Integer year = (Integer) resForm.getPValue("year");
            name = "ProjectsByManager-" + rid + "-" + year;
        }
        return name;
    }

    /**
	 * Return the parameters from this report or null if it haven't any
     * @param conn  Database connection.
     * @return      Map of parameters for the report.
	 */
    public Map getParameters(Connection conn) throws Exception {
        if (parameters == null) {
            Integer year = (Integer) resForm.getPValue("year");
            if (year != null) {
                parameters = new HashMap();
                parameters.put("year", year);
            }
        }
        return parameters;
    }

    /**
     * Returns the file name for the JasperReport file used to generate the report
     * @return  Jasper report file name.
     */
    public String getJasperFile() {
        return "project_manager.jrxml";
    }
}
