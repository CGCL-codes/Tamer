package equilibrium.commons.report.mock;

import java.util.HashMap;
import equilibrium.commons.report.ReportContext;
import equilibrium.commons.report.generator.SubreportData;
import equilibrium.commons.report.generator.SubreportGenerator;

public class SubreportGeneratorMock extends SubreportGenerator {

    public SubreportData generateSubreportData(ReportContext context) {
        SubreportData data = createEmptySubreportData(context, false);
        data.addDataBean(new HashMap<String, Object>());
        data.addDataBean(new HashMap<String, Object>());
        return data;
    }
}
