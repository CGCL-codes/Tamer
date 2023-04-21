package enml.validity;

import enml.documents.Certification;
import enml.measures.Measure;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Entity;

@Entity
@Table(name = "VALIDITY")
public class Validity implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "VALIDITY_ID")
    private Measure measure;

    private Measure Measure;

    private Long id;

    @Column(name = "VALID", nullable = false)
    private boolean valid;

    private Certification certification;

    public Validity() {
        this.valid = false;
        this.certification = new Certification();
    }

    public Validity(boolean v) {
        this.valid = v;
    }

    @Override
    public String toString() {
        String retVal = "";
        if (valid) {
            retVal = "valid";
        } else {
            retVal = "not valid";
        }
        return retVal;
    }

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean val) {
        this.valid = val;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Certification getCertification() {
        return certification;
    }

    public void setCertification(Certification certification) {
        this.certification = certification;
    }

    public Measure getMeasure() {
        return null;
    }

    public void setMeasure(Measure val) {
    }
}
