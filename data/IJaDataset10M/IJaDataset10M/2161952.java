package edu.univalle.lingweb.persistence;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

/**
 * AbstractToMetadataType entity provides the base persistence definition of the
 * ToMetadataType entity.
 * 
 * @author LingWeb
 */
@MappedSuperclass
public abstract class AbstractToMetadataType implements java.io.Serializable {

    private Long metadataTypeId;

    private String description;

    private Set<ToAssistenceMetadata> toAssistenceMetadatas = new HashSet<ToAssistenceMetadata>(0);

    /** default constructor */
    public AbstractToMetadataType() {
    }

    /** minimal constructor */
    public AbstractToMetadataType(Long metadataTypeId, String description) {
        this.metadataTypeId = metadataTypeId;
        this.description = description;
    }

    /** full constructor */
    public AbstractToMetadataType(Long metadataTypeId, String description, Set<ToAssistenceMetadata> toAssistenceMetadatas) {
        this.metadataTypeId = metadataTypeId;
        this.description = description;
        this.toAssistenceMetadatas = toAssistenceMetadatas;
    }

    @Id
    @Column(name = "metadata_type_id", unique = true, nullable = false, insertable = true, updatable = true, precision = 15, scale = 0)
    public Long getMetadataTypeId() {
        return this.metadataTypeId;
    }

    public void setMetadataTypeId(Long metadataTypeId) {
        this.metadataTypeId = metadataTypeId;
    }

    @Column(name = "description", unique = false, nullable = false, insertable = true, updatable = true, length = 60)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "toMetadataType")
    public Set<ToAssistenceMetadata> getToAssistenceMetadatas() {
        return this.toAssistenceMetadatas;
    }

    public void setToAssistenceMetadatas(Set<ToAssistenceMetadata> toAssistenceMetadatas) {
        this.toAssistenceMetadatas = toAssistenceMetadatas;
    }
}
