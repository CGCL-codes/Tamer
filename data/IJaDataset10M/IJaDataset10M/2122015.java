package org.vardb.resources.dao;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Hibernate;
import org.vardb.resources.CResourceType;
import org.vardb.resources.IResource;
import org.vardb.util.CStringHelper;

@Entity
@Table(name = "taxons")
public class CTaxon extends CAbstractTaxon implements IResource {

    public static final int BACTERIA = 2;

    public static final int EUKARYOTES = 2759;

    public static final int VIRUSES = 10239;

    protected CTaxon parent;

    protected Set<CTaxon> taxa = new LinkedHashSet<CTaxon>();

    public CTaxon() {
        super();
    }

    public CTaxon(int taxid) {
        super(taxid);
    }

    public CTaxon(String identifier) {
        super(identifier);
    }

    @Id
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public CTaxon getParent() {
        return this.parent;
    }

    public void setParent(CTaxon parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent_id", cascade = CascadeType.ALL)
    @OrderBy("name")
    public Set<CTaxon> getTaxa() {
        return this.taxa;
    }

    public void setTaxa(Set<CTaxon> taxa) {
        this.taxa = taxa;
    }

    @Transient
    public CResourceType getResourceType() {
        return CResourceType.TAXON;
    }

    public void initialize() {
        if (this.parent_id != null) Hibernate.initialize(this.parent);
        Hibernate.initialize(this.taxa);
    }

    public void add(CTaxon taxon) {
        taxon.setParent(this);
        this.taxa.add(taxon);
    }

    public void getXml(StringBuilder buffer) {
        getXml(buffer, 0);
    }

    public void getXml(StringBuilder buffer, int indent) {
        String padding = CStringHelper.repeatString("\t", indent);
        buffer.append(padding + "<taxon");
        buffer.append(" identifier=\"" + getIdentifier() + "\"");
        if (this.parent != null) buffer.append(" parent=\"" + this.parent.getIdentifier() + "\"");
        buffer.append(">\n");
        buffer.append(padding + "\t<name>" + this.name + "</name>\n");
        buffer.append(padding + "\t<level>" + this.level.name() + "</level>\n");
        buffer.append(padding + "\t<taxid>" + this.taxid + "</taxid>\n");
        buffer.append(padding + "</taxon>\n");
    }

    private static Integer counter = 0;

    public void index() {
        for (CTaxon taxon : this.taxa) {
            index(taxon);
        }
    }

    private void index(CTaxon taxon) {
        taxon.setLeft(CTaxon.counter++);
        for (CTaxon child : taxon.getTaxa()) {
            index(child);
        }
        taxon.setRight(CTaxon.counter++);
        taxon.setInitialized(true);
    }
}
