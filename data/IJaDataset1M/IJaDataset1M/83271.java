package lt.bsprendimai.ddesk.dao;

import java.io.Serializable;
import java.util.Date;

/** @author Hibernate CodeGenerator */
public class TicketInfo extends AbstractTicket implements Serializable {

    /** identifier field */
    private Integer id;

    /** nullable persistent field */
    private String eventName;

    /** nullable persistent field */
    private String priorityName;

    /** nullable persistent field */
    private String personName;

    /** nullable persistent field */
    private String personEmail;

    /** nullable persistent field */
    private String personPosition;

    /** nullable persistent field */
    private String phoneNo;

    /** nullable persistent field */
    private String projectName;

    /** nullable persistent field */
    private String projectCode;

    private Integer projectsCompany;

    private Integer projectsPerson;

    private Integer projectsManager;

    /** nullable persistent field */
    private String moduleName;

    /** nullable persistent field */
    private String statusName;

    /** nullable persistent field */
    private String companyName;

    /** nullable persistent field */
    private String asignee;

    /** nullable persistent field */
    private String severityName;

    /** persistent field */
    private String name;

    /** persistent field */
    private Integer company;

    /** nullable persistent field */
    private Integer person;

    /** persistent field */
    private Date reportDate;

    /** nullable persistent field */
    private Integer reportBy;

    /** nullable persistent field */
    private String description;

    /** persistent field */
    private Integer status;

    /** nullable persistent field */
    private String acceptedNotes;

    /** nullable persistent field */
    private Date assignedDate;

    /** nullable persistent field */
    private Integer assignedTo;

    /** nullable persistent field */
    private Integer assignedBy;

    /** nullable persistent field */
    private Double worktime;

    /** nullable persistent field */
    private Double additionalTime;

    /** nullable persistent field */
    private Date planedDate;

    /** nullable persistent field */
    private Date actualDate;

    /** nullable persistent field */
    private String serviceCode;

    /** nullable persistent field */
    private Integer priority;

    /** persistent field */
    private Integer type;

    /** nullable persistent field */
    private String resolution;

    /** nullable persistent field */
    private Date dateClosed;

    /** nullable persistent field */
    private Integer closedBy;

    /** nullable persistent field */
    private Date editDate;

    /** nullable persistent field */
    private Integer editBy;

    /** persistent field */
    private String uniqueId;

    /** persistent field */
    private boolean chargeable;

    /** nullable persistent field */
    private String reporterMail;

    /** nullable persistent field */
    private String reporterXmpp;

    /** nullable persistent field */
    private Double version;

    /** nullable persistent field */
    private Integer project;

    /** nullable persistent field */
    private Integer module;

    /** nullable persistent field */
    private Integer severity;

    private Boolean administrative = false;

    /** default constructor */
    public TicketInfo() {
    }

    /** minimal constructor */
    public TicketInfo(Integer id) {
        this.id = id;
    }

    public static String getSafe(String s) {
        if (s == null) return ""; else return s;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventName() {
        return getSafe(eventName);
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPriorityName() {
        return getSafe(priorityName);
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getPersonName() {
        return getSafe(personName);
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return getSafe(personEmail);
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonPosition() {
        return getSafe(personPosition);
    }

    public void setPersonPosition(String personPosition) {
        this.personPosition = personPosition;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProjectName() {
        return getSafe(projectName);
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectCode() {
        return getSafe(projectCode);
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getModuleName() {
        return getSafe(moduleName);
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getStatusName() {
        return getSafe(statusName);
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCompanyName() {
        return getSafe(companyName);
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAsignee() {
        return getSafe(asignee);
    }

    public void setAsignee(String asignee) {
        this.asignee = asignee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public Integer getPerson() {
        return person;
    }

    public void setPerson(Integer person) {
        this.person = person;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getReportBy() {
        return reportBy;
    }

    public void setReportBy(Integer reportBy) {
        this.reportBy = reportBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAcceptedNotes() {
        return acceptedNotes;
    }

    public void setAcceptedNotes(String acceptedNotes) {
        this.acceptedNotes = acceptedNotes;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Integer assignedBy) {
        this.assignedBy = assignedBy;
    }

    public Double getWorktime() {
        return worktime;
    }

    public void setWorktime(Double worktime) {
        this.worktime = worktime;
    }

    public Double getAdditionalTime() {
        return additionalTime;
    }

    public void setAdditionalTime(Double additionalTime) {
        this.additionalTime = additionalTime;
    }

    public Date getPlanedDate() {
        return planedDate;
    }

    public void setPlanedDate(Date planedDate) {
        this.planedDate = planedDate;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Date getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    public Integer getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(Integer closedBy) {
        this.closedBy = closedBy;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    public Integer getEditBy() {
        return editBy;
    }

    public void setEditBy(Integer editBy) {
        this.editBy = editBy;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isChargeable() {
        return chargeable;
    }

    public void setChargeable(boolean chargeable) {
        this.chargeable = chargeable;
    }

    public String getReporterMail() {
        return reporterMail;
    }

    public void setReporterMail(String reporterMail) {
        this.reporterMail = reporterMail;
    }

    public String getReporterXmpp() {
        return reporterXmpp;
    }

    public void setReporterXmpp(String reporterXmpp) {
        this.reporterXmpp = reporterXmpp;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public String getSeverityName() {
        return getSafe(severityName);
    }

    public void setSeverityName(String severityName) {
        this.severityName = severityName;
    }

    public Boolean getAdministrative() {
        return administrative;
    }

    public void setAdministrative(Boolean administrative) {
        this.administrative = administrative;
    }

    public Integer getProjectsCompany() {
        return projectsCompany;
    }

    public void setProjectsCompany(Integer projectsCompany) {
        this.projectsCompany = projectsCompany;
    }

    public Integer getProjectsPerson() {
        return projectsPerson;
    }

    public void setProjectsPerson(Integer projectsPerson) {
        this.projectsPerson = projectsPerson;
    }

    public Integer getProjectsManager() {
        return projectsManager;
    }

    public void setProjectsManager(Integer projectsManager) {
        this.projectsManager = projectsManager;
    }
}
