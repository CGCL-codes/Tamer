package org.hyperimage.service.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Jens-Martin Loebel
 */
@Entity
public class HIFlexMetadataTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String namespacePrefix;

    String namespaceURI;

    String namespaceURL;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "template", targetEntity = HIFlexMetadataSet.class)
    List<HIFlexMetadataSet> entries;

    @Column(columnDefinition = "TEXT", length = 128000, nullable = false)
    String sortOrder;

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false, targetEntity = HIProject.class)
    HIProject project;

    public HIFlexMetadataTemplate() {
        this.entries = new ArrayList<HIFlexMetadataSet>();
        this.sortOrder = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }

    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    public String getNamespaceURL() {
        return namespaceURL;
    }

    public void setNamespaceURL(String namespaceURL) {
        this.namespaceURL = namespaceURL;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<HIFlexMetadataSet> getEntries() {
        return entries;
    }

    public void setEntries(List<HIFlexMetadataSet> entries) {
        this.entries = entries;
    }

    @XmlTransient
    public HIProject getProject() {
        return project;
    }

    public void setProject(HIProject project) {
        this.project = project;
    }

    public void addEntry(HIFlexMetadataSet set) {
        if (set == null) return;
        if (entries.contains(set)) return;
        getEntries().add(set);
        this.sortOrder = this.sortOrder + "," + set.getId();
        if (this.sortOrder.startsWith(",")) this.sortOrder = this.sortOrder.substring(1);
        setSortOrder(this.sortOrder);
    }

    public void removeEntry(HIFlexMetadataSet set) {
        if (set == null) return;
        if (!entries.contains(set)) return;
        int index = -1;
        for (int i = 0; i < getEntries().size(); i++) if (getEntries().get(i).getId() == set.getId()) index = i;
        if (index < 0) return;
        getEntries().remove(index);
        if (this.sortOrder != null && this.sortOrder.length() > 0) {
            try {
                String newOrder = "";
                for (String sortId : this.sortOrder.split(",")) {
                    if (Long.parseLong(sortId) != set.getId()) newOrder = newOrder + "," + sortId;
                }
                this.sortOrder = newOrder;
                if (this.sortOrder.startsWith(",")) this.sortOrder = this.sortOrder.substring(1);
                setSortOrder(this.sortOrder);
            } catch (Exception e) {
            }
        }
    }
}
