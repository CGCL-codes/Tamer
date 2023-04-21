package org.usixml.admin.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import org.jboss.seam.annotations.Name;
import org.usixml.entity.util.AbstractEntity;

/**
 *
 * @author htmfilho
 */
@Entity
@Name("organization")
public class Organization extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private List<Project> projects;

    @Id
    @Override
    public String getId() {
        return super.getId();
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "organizations")
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
