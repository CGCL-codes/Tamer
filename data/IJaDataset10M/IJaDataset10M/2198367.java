package org.fao.fenix.domain.filterbuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.fao.fenix.domain.exception.FenixException;
import org.fao.fenix.domain.info.dataset.Dataset;

public class FilterBuilder4Aggregation implements FilterBuilder {

    List<Dataset> datasetList;

    AggregationFunction aggregationFunction;

    String aggregationField;

    @Override
    public String composeFilter() {
        validateInput();
        String datasetClass = ((Dataset) datasetList.get(0)).getClass().getSimpleName();
        String selectString = "select new java.lang.Double(" + aggregationFunction + "(c." + aggregationField + ")) ";
        String fromString = "from ";
        String whereString = "where ";
        fromString = fromString + datasetClass + " d inner join d.contentList c ";
        int i = 1;
        for (Dataset dataset : datasetList) {
            whereString = whereString + "d.resourceId = " + dataset.getResourceId() + " ";
            if (datasetList.size() > i) {
                whereString = whereString + "or ";
            }
            i++;
        }
        String queryString = selectString + fromString + whereString;
        return queryString;
    }

    @Override
    public void validateInput() {
        if (datasetList == null || datasetList.size() == 0) throw new FenixException("No dataset(s) set");
        Set<Class<? extends Dataset>> set = new HashSet<Class<? extends Dataset>>();
        for (Dataset dataset : datasetList) {
            set.add(dataset.getClass());
        }
        if (set.size() > 1) throw new FenixException("Types of Datasets in the list should be the same.");
        if (aggregationFunction == null) throw new FenixException("No aggregationFunction set");
        if (aggregationField == null) throw new FenixException("No aggregationField set");
    }

    public void setDatasetList(List<Dataset> datasetList) {
        this.datasetList = datasetList;
    }

    public void setFunction(AggregationFunction aggregationFunction) {
        this.aggregationFunction = aggregationFunction;
    }

    public void setAggregationField(String aggregationField) {
        this.aggregationField = aggregationField;
    }
}
