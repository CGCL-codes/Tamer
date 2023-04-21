package org.fao.fenix.domain.info.pattern.disasterrisk;

import javax.persistence.Entity;
import org.fao.fenix.domain.info.dataset.NumericDataset;

@Entity
public class DisasterDataset extends NumericDataset {

    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    String disasterImpactTypeCode;

    public String setDisasterImpactTypeCode() {
        return disasterImpactTypeCode;
    }

    public void setDisasterImpactTypeCode(String disasterImpactTypeCode) {
        this.disasterImpactTypeCode = disasterImpactTypeCode;
    }
}
