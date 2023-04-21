package org.fao.fenix.domain.info.pattern.meteorology;

import java.util.Date;
import javax.persistence.Entity;
import org.fao.fenix.domain.info.dataset.NumericDataset;

@Entity
public class EvaporationDataset extends NumericDataset {

    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    private Date time;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
