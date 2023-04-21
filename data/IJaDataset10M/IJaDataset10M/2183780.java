package org.fao.fenix.domain.contentdefinition;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * 
 * @deprecated moved to org.fao.fenix.domain.info
 */
@Entity
public class DatasetGroupOld {

    @Id
    @GeneratedValue
    Long id;

    @OneToMany(cascade = CascadeType.ALL)
    private List<DatasetOld> datasetGroup;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DatasetOld> getDatasetGroup() {
        return datasetGroup;
    }

    public void setDatasetGroup(List<DatasetOld> datasetGroup) {
        this.datasetGroup = datasetGroup;
    }
}
