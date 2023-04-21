package org.databene.mad4db.report.html;

import java.io.IOException;
import org.databene.mad4db.cmd.SchemaChange;
import org.databene.mad4db.report.AbstractReporter;
import org.databene.mad4db.report.ReportContext;

/**
 * Creates a report in HTML form.<br/><br/>
 * Created: 07.06.2011 09:41:41
 * @since 0.1
 * @author Volker Bergmann
 */
public class MainReporter extends AbstractReporter {

    public MainReporter(ReportContext context) {
        super(context);
    }

    public void report(SchemaChange schemaChange) throws IOException {
        new PriorityReporter(context).report(schemaChange);
        new TableDiffReporter(context).report(schemaChange);
        new TriggerDiffReporter(context).report(schemaChange);
        new PackageDiffReporter(context).report(schemaChange);
    }
}
