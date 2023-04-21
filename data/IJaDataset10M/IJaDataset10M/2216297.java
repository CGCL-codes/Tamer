package com.express.service.dto;

import com.googlecode.simpleobjectassembler.annotation.EntityDto;
import java.io.Serializable;
import java.util.List;

@EntityDto(id = "id")
public class BacklogItemDto implements Serializable, Comparable<BacklogItemDto> {

    private static final long serialVersionUID = -3941782566908593188L;

    private long id;

    private Long version;

    private String reference;

    private String title;

    private String summary;

    private String asA;

    private String want;

    private String soThat;

    private String detailedDescription;

    private String status;

    private Integer effort;

    private IssueDto impediment;

    private Integer businessValue;

    private UserDto assignedTo;

    private BacklogItemDto parent;

    private ProjectDto project;

    private IterationDto iteration;

    private List<BacklogItemDto> tasks;

    private List<ThemeDto> themes;

    private List<AcceptanceCriteriaDto> acceptanceCriteria;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public IssueDto getImpediment() {
        return impediment;
    }

    public void setImpediment(IssueDto impediment) {
        this.impediment = impediment;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getEffort() {
        return effort;
    }

    public void setEffort(Integer effort) {
        this.effort = effort;
    }

    public Integer getBusinessValue() {
        return businessValue;
    }

    public void setBusinessValue(Integer businessValue) {
        this.businessValue = businessValue;
    }

    public UserDto getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(UserDto assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<BacklogItemDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<BacklogItemDto> tasks) {
        this.tasks = tasks;
    }

    public BacklogItemDto getParent() {
        return parent;
    }

    public void setParent(BacklogItemDto parent) {
        this.parent = parent;
    }

    public String getAsA() {
        return asA;
    }

    public void setAsA(String asA) {
        this.asA = asA;
    }

    public String getSoThat() {
        return soThat;
    }

    public void setSoThat(String soThat) {
        this.soThat = soThat;
    }

    public String getWant() {
        return want;
    }

    public void setWant(String want) {
        this.want = want;
    }

    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public IterationDto getIteration() {
        return iteration;
    }

    public void setIteration(IterationDto iteration) {
        this.iteration = iteration;
    }

    public List<ThemeDto> getThemes() {
        return themes;
    }

    public void setThemes(List<ThemeDto> themes) {
        this.themes = themes;
    }

    public List<AcceptanceCriteriaDto> getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public void setAcceptanceCriteria(List<AcceptanceCriteriaDto> acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }

    public int compareTo(BacklogItemDto item) {
        return item.getBusinessValue().compareTo(this.businessValue);
    }
}
