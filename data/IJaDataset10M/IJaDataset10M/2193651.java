package metric.core.report;

import java.lang.reflect.Constructor;
import metric.core.ReportDefinition;
import metric.core.ReportDefinitionRepository;
import metric.core.exception.MalformedReportDefinition;
import metric.core.exception.ReportException;
import metric.core.report.visitor.ReportVisitor;

public class ReportFactory {

    private static final String PACKAGE_NAME = "metric.core.report.visitor.";

    public static ReportVisitor getReport(ReportDefinition definition) throws ReportException {
        Constructor con = null;
        try {
            try {
                Class[] consArgs = new Class[] { ReportDefinition.class };
                con = Class.forName(PACKAGE_NAME + definition.className).getConstructor(consArgs);
            } catch (ClassNotFoundException e) {
                throw new ReportException("Unable to instantiate report: " + definition.description);
            }
            return (Report) con.newInstance(definition);
        } catch (Exception e) {
            throw new ReportException("Unable to instantiate report: " + definition.description);
        }
    }

    public static ReportVisitor getReport(String line) throws ReportException, MalformedReportDefinition {
        return getReport(ReportDefinitionRepository.parseDefinition(line));
    }
}
