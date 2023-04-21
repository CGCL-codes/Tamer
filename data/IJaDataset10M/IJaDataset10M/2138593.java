package org.unitils.dataset.rowsource;

import java.util.List;
import java.util.Properties;

/**
 * @author Tim Ducheyne
 * @author Filip Neven
 */
public interface InlineDataSetRowSourceFactory {

    /**
     * @param configuration The unitils configuration, not null
     */
    void init(Properties configuration);

    DataSetRowSource createDataSetRowSource(List<String> dataSetRows);
}
