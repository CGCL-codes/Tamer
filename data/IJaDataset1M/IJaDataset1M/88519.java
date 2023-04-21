package scpn.model.each_projects_singleparticle;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aquintana <aquintana@cnb.csic.es>
 */
@Entity
@Table(name = "objects", catalog = "scipion", schema = "each_projects_singleparticle")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Objects.findAll", query = "SELECT o FROM Objects o"), @NamedQuery(name = "Objects.findByObjectId", query = "SELECT o FROM Objects o WHERE o.objectId = :objectId"), @NamedQuery(name = "Objects.findByDeleted", query = "SELECT o FROM Objects o WHERE o.deleted = :deleted"), @NamedQuery(name = "Objects.findByDescription", query = "SELECT o FROM Objects o WHERE o.description = :description"), @NamedQuery(name = "Objects.findByObjectValue", query = "SELECT o FROM Objects o WHERE o.objectValue = :objectValue") })
public class Objects implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "object_id", nullable = false)
    private Integer objectId;

    @Basic(optional = false)
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "description", length = 2147483647)
    private String description;

    @Column(name = "object_value", length = 2147483647)
    private String objectValue;

    @JoinTable(name = "set_of_objects", joinColumns = { @JoinColumn(name = "child_object_id", referencedColumnName = "object_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "parent_object_id", referencedColumnName = "object_id", nullable = false) })
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Objects> objectsList;

    @ManyToMany(mappedBy = "objectsList", fetch = FetchType.LAZY)
    private List<Objects> objectsList1;

    @JoinTable(name = "outs", joinColumns = { @JoinColumn(name = "object_id", referencedColumnName = "object_id", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "step_id", referencedColumnName = "step_id", nullable = false) })
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Steps> stepsList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "objectId", fetch = FetchType.LAZY)
    private List<FilePaths> filePathsList;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "objects", fetch = FetchType.LAZY)
    private SetOfAttributes setOfAttributes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "objects1", fetch = FetchType.LAZY)
    private List<SetOfAttributes> setOfAttributesList;

    @OneToMany(mappedBy = "objectId", fetch = FetchType.LAZY)
    private List<ActualParameters> actualParametersList;

    public Objects() {
    }

    public Objects(Integer objectId) {
        this.objectId = objectId;
    }

    public Objects(Integer objectId, boolean deleted) {
        this.objectId = objectId;
        this.deleted = deleted;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObjectValue() {
        return objectValue;
    }

    public void setObjectValue(String objectValue) {
        this.objectValue = objectValue;
    }

    @XmlTransient
    public List<Objects> getObjectsList() {
        return objectsList;
    }

    public void setObjectsList(List<Objects> objectsList) {
        this.objectsList = objectsList;
    }

    @XmlTransient
    public List<Objects> getObjectsList1() {
        return objectsList1;
    }

    public void setObjectsList1(List<Objects> objectsList1) {
        this.objectsList1 = objectsList1;
    }

    @XmlTransient
    public List<Steps> getStepsList() {
        return stepsList;
    }

    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
    }

    @XmlTransient
    public List<FilePaths> getFilePathsList() {
        return filePathsList;
    }

    public void setFilePathsList(List<FilePaths> filePathsList) {
        this.filePathsList = filePathsList;
    }

    public SetOfAttributes getSetOfAttributes() {
        return setOfAttributes;
    }

    public void setSetOfAttributes(SetOfAttributes setOfAttributes) {
        this.setOfAttributes = setOfAttributes;
    }

    @XmlTransient
    public List<SetOfAttributes> getSetOfAttributesList() {
        return setOfAttributesList;
    }

    public void setSetOfAttributesList(List<SetOfAttributes> setOfAttributesList) {
        this.setOfAttributesList = setOfAttributesList;
    }

    @XmlTransient
    public List<ActualParameters> getActualParametersList() {
        return actualParametersList;
    }

    public void setActualParametersList(List<ActualParameters> actualParametersList) {
        this.actualParametersList = actualParametersList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (objectId != null ? objectId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Objects)) {
            return false;
        }
        Objects other = (Objects) object;
        if ((this.objectId == null && other.objectId != null) || (this.objectId != null && !this.objectId.equals(other.objectId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "scpn.model.each_projects_singleparticle.Objects[ objectId=" + objectId + " ]";
    }
}
