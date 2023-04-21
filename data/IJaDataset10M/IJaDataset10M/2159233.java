package org.fao.fenix.domain4.client.contentdefinition;

import java.util.List;
import java.io.Serializable;

public class DatasetGroup implements Serializable {

    Long id;

    private List datasetGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List getDatasetGroup() {
        return datasetGroup;
    }

    public void setDatasetGroup(List datasetGroup) {
        this.datasetGroup = datasetGroup;
    }
}
