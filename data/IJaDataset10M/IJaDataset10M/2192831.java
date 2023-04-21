package org.unitils.dataset.structure;

import org.unitils.dataset.database.DataSourceWrapperFactory;
import java.util.Properties;
import static org.unitils.core.util.ConfigUtils.getInstanceOf;

/**
 * Helper class for constructing parts of the data set module.
 *
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public class DataSetStructureGeneratorFactory {

    /**
     * Property key for the xsd target directory
     */
    public static final String PROPKEY_XSD_TARGETDIRNAME = "dataset.xsd.targetDirName";

    protected Properties configuration;

    protected DataSourceWrapperFactory dataSourceWrapperFactory;

    protected DataSetStructureGenerator dataSetStructureGenerator;

    public DataSetStructureGeneratorFactory(Properties configuration, DataSourceWrapperFactory dataSourceWrapperFactory) {
        this.configuration = configuration;
        this.dataSourceWrapperFactory = dataSourceWrapperFactory;
    }

    public DataSetStructureGenerator getDataSetStructureGenerator() {
        if (dataSetStructureGenerator == null) {
            String defaultTargetDirectory = configuration.getProperty(PROPKEY_XSD_TARGETDIRNAME);
            dataSetStructureGenerator = getInstanceOf(DataSetStructureGenerator.class, configuration);
            dataSetStructureGenerator.init(dataSourceWrapperFactory, defaultTargetDirectory);
        }
        return dataSetStructureGenerator;
    }
}
