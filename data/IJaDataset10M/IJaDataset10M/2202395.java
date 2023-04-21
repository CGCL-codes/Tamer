package es.optsicom.lib.analysis.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import es.optsicom.lib.util.Properties;

public class FilteredRowTitles {

    private List<Properties> originalRowTitles;

    private Properties commonProperties;

    private List<Properties> filteredRowTitles;

    public FilteredRowTitles(List<Properties> rowTitles) {
        this.originalRowTitles = rowTitles;
        processOriginalRowTitles();
    }

    private void processOriginalRowTitles() {
        Set<Entry<String, Object>> commonPropoertiesEntries = new HashSet<Map.Entry<String, Object>>(originalRowTitles.get(0).entrySet());
        for (int i = 1; i < originalRowTitles.size(); i++) {
            commonPropoertiesEntries.retainAll(originalRowTitles.get(i).entrySet());
        }
        commonProperties = new Properties();
        for (Entry<String, Object> entry : commonPropoertiesEntries) {
            commonProperties.put(entry.getKey(), entry.getValue());
        }
        filteredRowTitles = new ArrayList<Properties>();
        for (Properties properties : originalRowTitles) {
            Properties filteredProps = new Properties(properties);
            for (String propName : commonProperties.keySet()) {
                filteredProps.remove(propName);
            }
            filteredRowTitles.add(filteredProps);
        }
    }

    public List<Properties> getFilteredRowTitles() {
        return filteredRowTitles;
    }

    public Properties getCommonProperties() {
        return commonProperties;
    }
}
