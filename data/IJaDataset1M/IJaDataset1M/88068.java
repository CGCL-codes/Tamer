package net.sourceforge.iwii.db.dev.persistence.entities.data.project.artifact.phase1;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import net.sourceforge.iwii.db.dev.persistence.entities.IEntity;

/**
 * Class represents entity mapped to database relation 'DT_SIMILAR_PROJECT_EXPERIENCES'.
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
@Entity
@Table(name = "DT_SIMILAR_PROJECT_EXPERIENCES")
@NamedQueries({  })
public class SimilarProjectsExperienceEntity implements IEntity<Long> {

    @Transient
    private boolean initialized = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPE_ID", nullable = false)
    private Long id;

    @Column(name = "SUMMARY", length = 1000, nullable = true)
    private String summary;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    protected Date creationDate;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "MODIFICATION_DATE", nullable = false)
    protected Date modificationDate;

    @OneToMany(mappedBy = "superObject", cascade = CascadeType.ALL)
    private List<ProjectExperienceEntity> experiences = new LinkedList<ProjectExperienceEntity>();

    @OneToOne(mappedBy = "similarProjectsExperience")
    private FeasibilityStudyArtifactDataEntity artifactData;

    public SimilarProjectsExperienceEntity() {
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<ProjectExperienceEntity> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ProjectExperienceEntity> experiences) {
        this.experiences = experiences;
    }

    public FeasibilityStudyArtifactDataEntity getArtifactData() {
        return artifactData;
    }

    public void setArtifactData(FeasibilityStudyArtifactDataEntity artifactData) {
        this.artifactData = artifactData;
    }

    @Override
    public void initialize() {
        if (!this.initialized) {
            this.initialized = true;
            for (ProjectExperienceEntity experience : this.experiences) {
                experience.initialize();
            }
            this.artifactData.initialize();
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
