package metric;

import metric.core.Project;
import metric.core.ReportDefinition;
import metric.core.ReportDefinitionRepository;
import metric.core.exception.ReportException;
import metric.core.model.HistoryMetricData;
import metric.core.report.ReportFactory;
import metric.core.report.decorator.TextDecorator;
import metric.core.report.visitor.ReportVisitor;
import metric.core.util.logging.ConsoleHandler;
import metric.core.util.logging.LogOrganiser;

/**
 * Basic console program for running reports on a generated JSeat Projec.
 * 
 * @author Joshua Hayes,Swinburne University (ICT),2007
 */
public class JSeatReport {

    /**
     * Example usage: -i input.jpf -r default.rep reportNum JSeatReport -i
     * groovy.jpf -r default.rep 2
     */
    public static void main(String[] args) {
        String input = null, report = null;
        int reportNumber = -1;
        LogOrganiser.addHandler(new ConsoleHandler());
        if (args.length != 5) System.err.println("Invalid arguments provided.");
        try {
            if (args[0].equals("-i")) input = args[1]; else System.err.println("Invalid argument specified. Expected [-i]");
            if (args[2].equals("-r")) report = args[3]; else System.err.println("Invalid argument specified. Expected [-r]");
            try {
                reportNumber = Integer.parseInt(args[4]);
            } catch (Exception e) {
                System.err.println("Report number number either not specified or valid");
            }
        } catch (Exception e) {
            System.err.println("One or more invalid arguments specified.");
        }
        if (input != null && report != null && reportNumber != -1) {
            runReport(input, report, reportNumber);
        }
    }

    /**
     * Runs a report on a project.
     * 
     * @param input The JSeat project.
     * @param report The report file.
     * @param reportNumber The report number from the report file.
     */
    private static void runReport(String input, String report, int reportNumber) {
        Project p = new Project(input);
        HistoryMetricData hmd = p.build();
        if (hmd != null) {
            ReportDefinitionRepository rdr = new ReportDefinitionRepository(report);
            ReportDefinition definition = rdr.getDefinition(reportNumber);
            try {
                ReportVisitor rv = ReportFactory.getReport(definition);
                TextDecorator td = new TextDecorator(rv);
                hmd.accept(td);
            } catch (ReportException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.err.println("Unable to load project. Bad project file or project data?");
        }
    }
}
