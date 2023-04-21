package org.vardb.tags.dao;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Hibernate;
import org.vardb.resources.CResourceType;
import org.vardb.resources.IResource;
import org.vardb.sequences.ISequence;
import org.vardb.sequences.dao.CSequence;

@Entity
@Table(name = "tags")
public class CTag implements IResource {

    protected Integer id;

    protected Integer bundle_id;

    protected String name = "";

    protected String description = "";

    protected String color = "";

    protected String bgcolor = "";

    protected Integer numsequences = 0;

    protected CBundle bundle;

    protected Set<CAttribute> attributes = new LinkedHashSet<CAttribute>();

    protected Set<CSequence> sequences = new LinkedHashSet<CSequence>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(insertable = false, updatable = false)
    public Integer getBundle_id() {
        return this.bundle_id;
    }

    public void setBundle_id(final Integer bundle_id) {
        this.bundle_id = bundle_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getBgcolor() {
        return this.bgcolor;
    }

    public void setBgcolor(final String bgcolor) {
        this.bgcolor = bgcolor;
    }

    public Integer getNumsequences() {
        return this.numsequences;
    }

    public void setNumsequences(final Integer numsequences) {
        this.numsequences = numsequences;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bundle_id")
    public CBundle getBundle() {
        return this.bundle;
    }

    public void setBundle(final CBundle bundle) {
        this.bundle = bundle;
    }

    @OneToMany(mappedBy = "tag_id", cascade = CascadeType.ALL)
    @OrderBy("id")
    public Set<CAttribute> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(final Set<CAttribute> attributes) {
        this.attributes = attributes;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "sequences_tags", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "sequence_id"))
    public Set<CSequence> getSequences() {
        return this.sequences;
    }

    public void setSequences(final Set<CSequence> sequences) {
        this.sequences = sequences;
    }

    public CTag() {
    }

    public CTag(String name) {
        this.name = name;
    }

    @Transient
    public CResourceType getResourceType() {
        return CResourceType.TAG;
    }

    @Transient
    public String getIdentifier() {
        return this.bundle_id + "." + this.name;
    }

    @Transient
    public Date getUpdated() {
        return null;
    }

    @Transient
    public String getAbbreviation() {
        return this.name;
    }

    public void initialize() {
        Hibernate.initialize(this.attributes);
        Hibernate.initialize(this.bundle.definitions);
    }

    @Transient
    public CAttribute getAttributeByDefinition(CAttributeDefinition definition) {
        for (CAttribute attribute : this.attributes) {
            if (attribute.getDefinition_id().equals(definition.getId())) return attribute;
        }
        return null;
    }

    public CAttribute getAttribute(String name) {
        CAttributeDefinition definition = getBundle().getDefinition(name);
        if (definition == null) return null;
        int definition_id = definition.getId();
        for (CAttribute attribute : this.attributes) {
            if (attribute.getDefinition_id() != null && attribute.getDefinition_id() == definition_id) return attribute;
        }
        return null;
    }

    public void add(CAttribute attribute) {
        attribute.setTag(this);
        this.attributes.add(attribute);
    }

    public void add(CSequence sequence) {
        if (!this.sequences.contains(sequence)) this.sequences.add(sequence);
    }

    public void tagSequences(List<ISequence> sequences) {
        for (ISequence sequence : sequences) {
            add((CSequence) sequence);
        }
        this.numsequences = this.sequences.size();
    }

    public void untagSequences(List<ISequence> sequences) {
        this.sequences.removeAll(sequences);
        this.numsequences = 0;
    }
}
