package com.cvo.scrumtoolkit.server.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.*;
import com.cvo.scrumtoolkit.server.EMF;

@Entity
public class Project implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Version
    @Column(name = "version")
    private Integer version;

    private String projectname;

    @OneToMany(mappedBy = "project")
    private List<BacklogItem> backlogItems;

    private String scrummaster;

    public Project() {
        backlogItems = new LinkedList<BacklogItem>();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public Long getId() {
        return Id;
    }

    public String getScrummaster() {
        return scrummaster;
    }

    public void setScrummaster(String scrummaster) {
        this.scrummaster = scrummaster;
    }

    public void setBacklogItem(BacklogItem backlogItem) {
        backlogItems.add(backlogItem);
    }

    public void addbacklogItem(BacklogItem backlogItem) {
        if (!getBacklogItems().contains(backlogItem)) {
            getBacklogItems().add(backlogItem);
            if (backlogItem.getProject() != null) {
                backlogItem.getProject().getBacklogItems().remove(backlogItem);
            }
            backlogItem.setProject(this);
        }
    }

    public Collection<BacklogItem> getBacklogItems() {
        return backlogItems;
    }

    public static List<Project> findAllProjects() {
        EntityManager em = entityManager();
        try {
            @SuppressWarnings("unchecked") List<Project> list = em.createQuery("select o from Project o").getResultList();
            list.size();
            return list;
        } finally {
            em.close();
        }
    }

    public static Project findProject(Long id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        try {
            Project employee = em.find(Project.class, id);
            return employee;
        } finally {
            em.close();
        }
    }

    public static final EntityManager entityManager() {
        return EMF.get().createEntityManager();
    }

    public void persist() {
        EntityManager em = entityManager();
        try {
            em.persist(this);
        } finally {
            em.close();
        }
    }

    public void remove() {
        EntityManager em = entityManager();
        try {
            Project attached = em.find(Project.class, this.Id);
            em.remove(attached);
        } finally {
            em.close();
        }
    }
}
