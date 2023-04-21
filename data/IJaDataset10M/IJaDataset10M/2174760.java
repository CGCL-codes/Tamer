package org.datacollection.model.metadata;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.datacollection.model.data.DCSingleSelectData;
import org.datacollection.model.data.DCTextData;

@Entity
@Table(name = "DC_FORM_TEXT_FIELDS")
public class DCFormTextField extends DCFormField {

    @Column(name = "MAX_LENGTH")
    private Integer maxLength;

    @OneToMany(mappedBy = "field")
    private List<DCTextData> values = new ArrayList<DCTextData>();

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public List<DCTextData> getValues() {
        return values;
    }

    public void setValues(List<DCTextData> values) {
        this.values = values;
    }

    private static final long serialVersionUID = 1L;
}
