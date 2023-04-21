package configuration.fakegame;

import org.ytoh.configurations.annotations.Property;
import org.ytoh.configurations.annotations.Component;
import common.gui.Slider;
import configuration.report.ISRConfig;

/**
 * Created by IntelliJ IDEA.
 * User: drchaj1
 * Date: Nov 3, 2009
 * Time: 11:29:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Component(name = "ImportFileConfig", description = "Import File configuration")
public class ImportFileConfig implements ISRConfig {

    @Property(name = "Dataset file name", description = "Dataset file name")
    private String datasetFileName = "data/iris.txt";

    public String getDatasetFileName() {
        return datasetFileName;
    }

    public void setDatasetFileName(String datasetFileName) {
        this.datasetFileName = datasetFileName;
    }
}
