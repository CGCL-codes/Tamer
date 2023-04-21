package edu.unibi.agbi.biodwh.entity.kegg.medicus.drug;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "kegg_drug_activity")
@Table(name = "kegg_drug_activity")
public class KeggDrugActivity implements java.io.Serializable {

    private static final long serialVersionUID = 426025755129516339L;

    private Integer id;

    private KeggDrug keggDrug;

    private String activity;

    public KeggDrugActivity() {
    }

    public KeggDrugActivity(KeggDrug keggDrug, String activity) {
        this.keggDrug = keggDrug;
        this.activity = activity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry", nullable = false)
    public KeggDrug getKeggDrug() {
        return this.keggDrug;
    }

    public void setKeggDrug(KeggDrug keggDrug) {
        this.keggDrug = keggDrug;
    }

    @Column(name = "activity", nullable = false, length = 768)
    public String getActivity() {
        return this.activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
