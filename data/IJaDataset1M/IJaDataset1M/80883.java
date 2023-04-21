package it.aco.mandragora.vo;

import java.util.Collection;

public class ProjectVO extends ValueObject {

    private Integer idProject;

    private String title;

    private String description;

    private Collection<PersonProjectVO> personProjectVOs;

    public Integer getIdProject() {
        return idProject;
    }

    public void setIdProject(Integer idProject) {
        this.idProject = idProject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<PersonProjectVO> getPersonProjectVOs() {
        return personProjectVOs;
    }

    public void setPersonProjectVOs(Collection<PersonProjectVO> personProjectVOs) {
        this.personProjectVOs = personProjectVOs;
    }
}
