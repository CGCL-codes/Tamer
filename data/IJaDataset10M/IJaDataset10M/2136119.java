package edu.unibi.agbi.dawismd.entities.biodwh.hprd;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "hprd_go_molecular_function_term")
@Table(name = "hprd_go_molecular_function_term")
public class HprdGoMolecularFunctionTerm implements java.io.Serializable {

    private static final long serialVersionUID = 7259167736545680197L;

    private HprdGoMolecularFunctionTermId id;

    private HprdGo hprdGo;

    public HprdGoMolecularFunctionTerm() {
    }

    public HprdGoMolecularFunctionTerm(HprdGoMolecularFunctionTermId id, HprdGo hprdGo) {
        this.id = id;
        this.hprdGo = hprdGo;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "id", nullable = false)), @AttributeOverride(name = "molecularFunctionTerm", column = @Column(name = "molecular_function_term", nullable = false)), @AttributeOverride(name = "goId", column = @Column(name = "go_id", nullable = false)) })
    public HprdGoMolecularFunctionTermId getId() {
        return this.id;
    }

    public void setId(HprdGoMolecularFunctionTermId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false, insertable = false, updatable = false)
    public HprdGo getHprdGo() {
        return this.hprdGo;
    }

    public void setHprdGo(HprdGo hprdGo) {
        this.hprdGo = hprdGo;
    }
}
