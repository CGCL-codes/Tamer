package org.vardb.resources.dao;

import java.util.*;
import javax.persistence.*;
import org.vardb.resources.CResourceType;
import org.vardb.resources.IResource;

@Entity
@Table(name = "hosts")
public class CHost extends CAbstractTaxon implements IResource {

    protected CHost m_parent;

    protected Set<CHost> m_taxa = new LinkedHashSet<CHost>();

    public CHost() {
        super();
    }

    public CHost(int taxid) {
        super(taxid);
    }

    public CHost(String identifier) {
        super(identifier);
    }

    @Id
    public Integer getId() {
        return m_id;
    }

    public void setId(Integer id) {
        m_id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public CHost getParent() {
        return m_parent;
    }

    public void setParent(CHost parent) {
        m_parent = parent;
    }

    @OneToMany(mappedBy = "parent_id", cascade = CascadeType.ALL)
    @OrderBy("name")
    public Set<CHost> getTaxa() {
        return m_taxa;
    }

    public void setTaxa(Set<CHost> taxa) {
        m_taxa = taxa;
    }

    @Transient
    public CResourceType getResourceType() {
        return CResourceType.HOST;
    }

    public void initialize() {
        if (getParent() != null) getParent().getName();
        getTaxa().iterator();
    }

    public void add(CHost taxon) {
        taxon.setParent(this);
        m_taxa.add(taxon);
    }

    static Integer counter = 0;

    public void index() {
        for (CHost taxon : m_taxa) {
            index(taxon);
        }
    }

    private void index(CHost taxon) {
        taxon.setLeft(CHost.counter++);
        for (CHost child : taxon.getTaxa()) {
            index(child);
        }
        taxon.setRight(CHost.counter++);
    }
}
