package org.identifylife.character.store.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.identifylife.character.store.model.agent.Person;
import org.identifylife.character.store.oxm.jaxb.FeatureRefAdapter;
import org.identifylife.character.store.oxm.jaxb.FeatureTreeRefAdapter;
import org.identifylife.character.store.oxm.jaxb.PersonRefAdapter;
import org.identifylife.character.store.oxm.jaxb.XsDateTimeAdapter;

/**
 * @author dbarnier
 *
 */
@Entity
@Table(name = "dataset")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "uuid", "datasetType", "coreLang", "labels", "features", "featureTrees" })
public class Dataset extends Model {

    @XmlAttribute
    @XmlID
    @Column(unique = true)
    private String uuid;

    private DatasetTypeEnum datasetType = DatasetTypeEnum.UNKNOWN;

    private Language coreLang;

    @XmlElementWrapper(name = "labels")
    @XmlElement(name = "label")
    private List<Label> labels = new ArrayList<Label>();

    @XmlElementWrapper(name = "features")
    @XmlElement(name = "feature")
    @XmlJavaTypeAdapter(FeatureRefAdapter.class)
    private Set<Feature> features = new HashSet<Feature>();

    @XmlElementWrapper(name = "featureTrees")
    @XmlElement(name = "featureTree")
    @XmlJavaTypeAdapter(FeatureTreeRefAdapter.class)
    private Set<FeatureTree> featureTrees = new HashSet<FeatureTree>();

    @XmlJavaTypeAdapter(XsDateTimeAdapter.class)
    private Date created;

    @XmlJavaTypeAdapter(PersonRefAdapter.class)
    private Person createdBy;

    @Index(name = "uuidIndex")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(columnDefinition = "integer", nullable = false)
    @Type(type = "org.identifylife.character.store.orm.hibernate.GenericEnumUserType", parameters = { @Parameter(name = "enumClass", value = "org.identifylife.character.store.model.DatasetTypeEnum"), @Parameter(name = "identifierMethod", value = "toInt"), @Parameter(name = "valueOfMethod", value = "fromInt") })
    public DatasetTypeEnum getDatasetType() {
        return datasetType;
    }

    public void setDatasetType(DatasetTypeEnum datasetType) {
        this.datasetType = datasetType;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    public Language getCoreLang() {
        return coreLang;
    }

    public void setCoreLang(Language coreLang) {
        this.coreLang = coreLang;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinTable(name = "dataset_labels", joinColumns = @JoinColumn(name = "dataset_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    @IndexColumn(name = "label_index")
    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    @OneToMany(mappedBy = "dataset", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    public Set<FeatureTree> getFeatureTrees() {
        return featureTrees;
    }

    public void setFeatureTrees(Set<FeatureTree> featureTrees) {
        this.featureTrees = featureTrees;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Person getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }

    public void addLabel(Label label) {
        if (!labels.contains(label)) {
            labels.add(label);
        }
    }

    public void addFeature(Feature feature) {
        if (!features.contains(feature)) {
            features.add(feature);
            feature.setDataset(this);
        }
    }

    public void addFeatures(Collection<Feature> features) {
        for (Feature feature : features) {
            addFeature(feature);
        }
    }

    public Feature getFeature(String uuid) {
        for (Feature feature : features) {
            if (feature.getUuid().equals(uuid)) {
                return feature;
            }
        }
        return null;
    }

    public boolean hasFeature(String uuid) {
        return getFeature(uuid) != null;
    }

    public void addFeatureTree(FeatureTree featureTree) {
        if (!featureTrees.contains(featureTree)) {
            featureTrees.add(featureTree);
            featureTree.setDataset(this);
        }
    }

    public void addFeatureTrees(Collection<FeatureTree> featureTrees) {
        for (FeatureTree featureTree : featureTrees) {
            addFeatureTree(featureTree);
        }
    }

    public FeatureTree getFeatureTree(String uuid) {
        for (FeatureTree featureTree : featureTrees) {
            if (featureTree.getUuid().equals(uuid)) {
                return featureTree;
            }
        }
        return null;
    }

    public boolean hasFeatureTree(String uuid) {
        return getFeatureTree(uuid) != null;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Dataset) {
            BaseObject other = (BaseObject) object;
            if (getId() != null) {
                return getId().equals(other.getId());
            } else if (getUuid() != null) {
                return getUuid().equals(other.getUuid());
            } else if (this == object) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (getUuid() != null) {
            return getUuid().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("uuid", getUuid()).append("datasetType", getDatasetType()).append("coreLang", getCoreLang()).append("labels", getLabels()).toString();
    }
}
