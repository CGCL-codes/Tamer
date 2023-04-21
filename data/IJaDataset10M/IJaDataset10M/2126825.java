package equilibrium.commons.report.generator;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperReport;
import equilibrium.commons.bean.BeansUtil;
import equilibrium.commons.report.ReportContext;
import equilibrium.commons.report.util.ReportUtils;

/**
 * Base class for implementing custom subreport generators.
 * 
 * @author Michal Bartyzel
 * @author Mariusz Sieraczkiewicz
 * 
 * @since 0.1
 */
public abstract class SubreportGenerator {

    protected String subreportId;

    protected Map<String, Object> localParameters = new HashMap<String, Object>();

    /**
     * @see equilibrium.commons.reports.jasper.subreports.SubreportGenerator#generateSubreport(equilibrium.commons.reports.jasper.ReportModel)
     */
    public abstract SubreportData generateSubreportData(ReportContext context);

    public void addLocalParameters(Map<String, Object> parameters) {
        for (Map.Entry entry : parameters.entrySet()) {
            addLocalParameter((String) entry.getKey(), entry.getValue());
        }
    }

    public void addLocalParameter(String name, Object value) {
        localParameters.put(name, value);
    }

    public boolean hasLocalParameters() {
        return localParameters.isEmpty() == false;
    }

    protected final SubreportData createEmptySubreportData(ReportContext context, boolean visibleWhenEmpty) {
        SubreportData result = new SubreportData(visibleWhenEmpty);
        result.setLocalParameters(ReportUtils.createEmptyParameters());
        Map<String, ? extends Object> globalParameters = context.findGlobalParameters();
        if (BeansUtil.isNotNull(globalParameters)) {
            result.addLocalParameters(globalParameters);
        }
        if (hasLocalParameters()) {
            Map notTypeParams = localParameters;
            Map<String, String> parsedLocalParameters = context.parseMappedStringExpression(notTypeParams);
            result.addLocalParameters(parsedLocalParameters);
        }
        JasperReport jrReport = context.findCompiledReport(subreportId);
        result.setJasperReport(jrReport);
        return result;
    }

    public String getSubreportId() {
        return subreportId;
    }

    public void setSubreportId(String subreportId) {
        this.subreportId = subreportId;
    }

    public Map<String, Object> getLocalParameters() {
        return localParameters;
    }
}
