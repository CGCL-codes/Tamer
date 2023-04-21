package edu.unibi.agbi.dawismd.entities.biodwh.uniprot;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "uniprot_ref")
@Table(name = "uniprot_ref")
public class UniprotRef implements java.io.Serializable {

    private static final long serialVersionUID = -900734551047569863L;

    private Integer refId;

    private Uniprot uniprot;

    private String refGroupAuthor;

    private String refLocation;

    private String refTitle;

    private Set<UniprotCrossRef> uniprotCrossRefs = new HashSet<UniprotCrossRef>(0);

    public UniprotRef() {
    }

    public UniprotRef(String refGroupAuthor, String refLocation, String refTitle) {
        this.refGroupAuthor = refGroupAuthor;
        this.refLocation = refLocation;
        this.refTitle = refTitle;
    }

    public UniprotRef(Uniprot uniprot, String refGroupAuthor, String refLocation, String refTitle, Set<UniprotCrossRef> uniprotCrossRefs) {
        this.uniprot = uniprot;
        this.refGroupAuthor = refGroupAuthor;
        this.refLocation = refLocation;
        this.refTitle = refTitle;
        this.uniprotCrossRefs = uniprotCrossRefs;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ref_id", nullable = false)
    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    @Column(name = "ref_group_author", nullable = false, length = 8192)
    public String getRefGroupAuthor() {
        return refGroupAuthor;
    }

    public void setRefGroupAuthor(String refGroupAuthor) {
        this.refGroupAuthor = refGroupAuthor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uniprot_id", nullable = false)
    public Uniprot getUniprot() {
        return this.uniprot;
    }

    public void setUniprot(Uniprot uniprot) {
        this.uniprot = uniprot;
    }

    @Column(name = "ref_location", nullable = false, length = 512)
    public String getRefLocation() {
        return this.refLocation;
    }

    public void setRefLocation(String refLocation) {
        this.refLocation = refLocation;
    }

    @Column(name = "ref_title", nullable = false, length = 512)
    public String getRefTitle() {
        return this.refTitle;
    }

    public void setRefTitle(String refTitle) {
        this.refTitle = refTitle;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "uniprotRef")
    public Set<UniprotCrossRef> getUniprotCrossRefs() {
        return this.uniprotCrossRefs;
    }

    public void setUniprotCrossRefs(Set<UniprotCrossRef> uniprotCrossRefs) {
        this.uniprotCrossRefs = uniprotCrossRefs;
    }
}
