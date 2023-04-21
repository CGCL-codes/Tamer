package org.projectnotes.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_task")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public final class Task {

    private Integer _taskId;

    private String _name;

    private String _descriptionShort;

    private Project _project;

    private Set<Comment> _comments = new HashSet<Comment>(0);

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    public Set<Comment> getComments() {
        return _comments;
    }

    public void setComments(Set<Comment> comments) {
        _comments = comments;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id")
    public Project getProject() {
        return _project;
    }

    public void setProject(Project project) {
        _project = project;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    public Integer getTaskId() {
        return _taskId;
    }

    public void setTaskId(Integer id) {
        _taskId = id;
    }

    @Column(name = "name")
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    @Column(name = "description_short")
    public String getDescriptionShort() {
        return _descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        _descriptionShort = descriptionShort;
    }
}
