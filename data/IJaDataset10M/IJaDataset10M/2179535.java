package com.peusoft.ptcollect.core.persistance.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Implements project activity.
 * 
 * @author Yauheni Prykhodzka
 * @version 1.0
 *
 */
@Entity(name = "ProjectActivity")
@Table(name = "PROJECT_ACTIVITY", uniqueConstraints = { @UniqueConstraint(columnNames = { "PROJECT_ID", "PROJECT_ACTIVITY_NAME" }) })
public class ProjectActivity extends AbstractDomainObject<Long> {

    /** project activity id */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "PROJECT_ACTIVITY_ID")
    private Long id;

    /** prject activity name */
    @Column(name = "PROJECT_ACTIVITY_NAME", length = 20, nullable = false, unique = true)
    private String projectActivityName;

    /** project */
    @ManyToOne(optional = false)
    @JoinColumn(name = "PROJECT_ID", referencedColumnName = "PROJECT_ID")
    private Project project;

    /**
     * Default constructor.
     */
    public ProjectActivity() {
        super();
    }

    /**
     * Uniq key is:
     * {@link #projectActivityName}
     * {@link #project}.
     *
     * @see AbstractDomainObject#getLogicalKey()
     */
    @Override
    public Object[] getLogicalKey() {
        Object[] keys = new Object[2];
        keys[0] = getProjectActivityName();
        keys[1] = getProject();
        return keys;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Returns the projectActivityName.
     */
    public String getProjectActivityName() {
        return projectActivityName;
    }

    /**
     * @param projectActivityName The projectActivityName to set.
     */
    public void setProjectActivityName(String projectActivityName) {
        this.projectActivityName = projectActivityName;
    }

    /**
     * @return Returns the project.
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project The project to set.
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return Returns the projectId or null if project isn't assigned.
     */
    public String getProjectNumber() {
        if (project != null) {
            return project.getProjectNumber();
        }
        return null;
    }
}
