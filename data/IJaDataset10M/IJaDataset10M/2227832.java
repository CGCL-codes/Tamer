package de.simplydevelop.mexs.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class MTFMessageType {

    @SequenceGenerator(name = "mtfMessageTypeGen", sequenceName = "mtfMessageTypePK")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mtfMessageTypeGen")
    private long id;

    private String baseline;

    private String mtfid;

    public String getBaseline() {
        return baseline;
    }

    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    public String getMtfid() {
        return mtfid;
    }

    public void setMtfid(String mtfid) {
        this.mtfid = mtfid;
    }

    @Override
    public String toString() {
        return "MTFMessageType [id=" + id + ", baseline=" + baseline + ", mtfid=" + mtfid + "]";
    }
}
