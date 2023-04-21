package net.fuhrparkservice.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_PKW")
public class PKW extends FahrzeugType {

    private int tueren;

    public PKW() {
    }

    public PKW(String id, Model model, long ps, long maxKmH, int tueren) {
        super(id, model, ps, maxKmH);
        this.tueren = tueren;
    }

    public int getTueren() {
        return tueren;
    }

    public void setTueren(int tueren) {
        this.tueren = tueren;
    }
}
