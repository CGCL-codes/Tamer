package pl.ehotelik.portal.domain.spa;

import net.sf.oval.constraint.NotNegative;
import net.sf.oval.constraint.NotNull;
import org.hibernate.annotations.Cascade;
import pl.ehotelik.portal.domain.Entity;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mkr
 * Date: Aug 16, 2010
 * Time: 10:56:22 PM
 * This is a representation of TreatmentPath Task object.
 */
@javax.persistence.Entity
@Table(name = "SPA_TREATMENT_PATHS")
public class TreatmentPath extends Entity {

    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.MERGE)
    @JoinColumn(name = "TREATMENT_PACKAGE_ID", nullable = false)
    private TreatmentPackage treatmentPackage;

    @Column(name = "DESCRIPTION", nullable = true)
    private String description;

    @OneToMany(mappedBy = "treatmentPath", fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<TreatmentDay> treatmentDays;

    public TreatmentPath() {
        super();
        treatmentDays = new ArrayList<TreatmentDay>();
    }

    public TreatmentPackage getTreatmentPackage() {
        return treatmentPackage;
    }

    public void setTreatmentPackage(TreatmentPackage treatmentPackage) {
        this.treatmentPackage = treatmentPackage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TreatmentDay> getTreatmentDays() {
        return treatmentDays;
    }
}
