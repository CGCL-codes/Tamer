package game.report;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: drchaj1
 * Date: Mar 18, 2009
 * Time: 4:14:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverviewData implements ReportData {

    private final Map<String, Object> params = new HashMap<String, Object>();

    private final int numberOfFolds;

    private final int numberOfClasses;

    public OverviewData(int numberOfFolds, int numberOfClasses) {
        this.numberOfFolds = numberOfFolds;
        this.numberOfClasses = numberOfClasses;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getName() {
        return "Overview";
    }

    public void retrieve() {
        params.put("reportNameLabel", "Report Name:");
        params.put("numberOfFoldsLabel", "Number of folds:");
        params.put("numberOfClassesLabel", "Number of classes:");
        params.put("reportName", "FAKEReport");
        params.put("numberOfFolds", numberOfFolds);
        params.put("numberOfClasses", numberOfClasses);
    }

    public void render(ReportDataImpl reportImpl) {
        reportImpl.renderOverview(this);
    }
}
