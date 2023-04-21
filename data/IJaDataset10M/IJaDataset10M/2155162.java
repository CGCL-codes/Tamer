package net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase5;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import net.sourceforge.iwii.db.dev.persistence.entities.IEntity;

/**
 * Class represents entity mapped to database relation 'DT_USE_CASE_OPERATION_DATA_PACKAGE_PROPERTIES'.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
@Entity
@Table(name = "DT_USE_CASE_OPERATION_DATA_PACKAGE_PROPERTIES")
@NamedQueries({  })
public class UseCaseOperationDataPackagePropertyEntity implements IEntity<Long> {

    @Transient
    private boolean initialized = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROPERTY_ID", nullable = false)
    private Long id;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "MODIFICATION_DATE", nullable = false)
    private Date modificationDate;

    @Column(name = "PROPERTY_NAME", length = 100, nullable = true)
    private String property;

    @ManyToOne()
    @JoinColumn(name = "PACKAGE_ID", referencedColumnName = "PACKAGE_ID", nullable = false)
    private UseCaseOperationDataPackageEntity dataPackage;

    @Column(name = "PROPERTY_TYPE", length = 100, nullable = true)
    private String type;

    public UseCaseOperationDataPackagePropertyEntity() {
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public UseCaseOperationDataPackageEntity getDataPackage() {
        return dataPackage;
    }

    public void setDataPackage(UseCaseOperationDataPackageEntity dataPackage) {
        this.dataPackage = dataPackage;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void initialize() {
        if (!this.initialized) {
            this.initialized = true;
            this.dataPackage.initialize();
        }
    }

    @Override
    public void resetInitialization() {
        this.initialized = false;
    }

    @Override
    public String toString() {
        return "entity://data-table/" + this.getClass().getName() + "[id=" + this.id + "]";
    }
}
