package ru.arriah.servicedesk.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import ru.arriah.common.bean.GenericBean;

public class RequestBean extends GenericBean {

    private String name;

    private int organizationId;

    private int departmentId;

    private int requestTypeId;

    private int clientId;

    private int groupId;

    private int typeId;

    private String regNumber;

    private int statusId;

    private int leadExecutorId;

    private int registratorId;

    private String dispatcherText;

    private String clientText;

    private String room;

    private int priority;

    private RequestPriorityBean priorityBean;

    private String comment;

    private Date realEndDate;

    private Date approxEndDate;

    private Date registrationDate;

    private String leadExecutorReport;

    private UserBean client;

    private OrganizationBean organization;

    private DepartmentBean department;

    private RequestStatusBean requestStatus;

    private RequestTypeBean requestType;

    private GroupBean group;

    private UserBean leadExecutor;

    private Collection<TaskBean> tasksList;

    private Date searchFromRegistrationDate;

    private Date searchToRegistrationDate;

    private Date searchFromEndDate;

    private Date searchToEndDate;

    @Override
    public void fillFromResultSet(ResultSet resultSet) {
        try {
            setId(resultSet.getInt("request.id"));
            setName(resultSet.getString("request.name"));
            setDispatcherText(resultSet.getString("request.text"));
            setClientText(resultSet.getString("request.clienttext"));
            setRoom(resultSet.getString("request.room"));
            setRegNumber(resultSet.getString("request.registration_number"));
            setRegistrationDate(resultSet.getTimestamp("request.registrationdate"));
            setApproxEndDate(resultSet.getTimestamp("request.approxenddate"));
            setLeadExecutorId(resultSet.getInt("request.leadexecutor_id"));
            setRealEndDate(resultSet.getTimestamp("request.realenddate"));
            setLeadExecutorReport(resultSet.getString("request.leadexecutorreport"));
            setComment(resultSet.getString("request.comment"));
            setPriority(resultSet.getInt("request.priority"));
            setStatusId(resultSet.getInt("request.status_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RequestBean() {
        super();
    }

    public RequestBean(ResultSet resultSet) {
        fillFromResultSet(resultSet);
    }

    public Date getSearchFromRegistrationDate() {
        return searchFromRegistrationDate;
    }

    public void setSearchFromRegistrationDate(Date searchFromRegistrationDate) {
        this.searchFromRegistrationDate = searchFromRegistrationDate;
    }

    public Date getSearchToRegistrationDate() {
        return searchToRegistrationDate;
    }

    public void setSearchToRegistrationDate(Date searchToRegistrationDate) {
        this.searchToRegistrationDate = searchToRegistrationDate;
    }

    public GroupBean getGroup() {
        return group;
    }

    public void setGroup(GroupBean groupBean) {
        this.group = groupBean;
    }

    public Date getApproxEndDate() {
        return approxEndDate;
    }

    public void setApproxEndDate(Date approxEndDate) {
        this.approxEndDate = approxEndDate;
    }

    public String getClientText() {
        return clientText;
    }

    public void setClientText(String clientText) {
        this.clientText = clientText;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLeadExecutorReport() {
        return leadExecutorReport;
    }

    public void setLeadExecutorReport(String leadExecutorReport) {
        this.leadExecutorReport = leadExecutorReport;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getRealEndDate() {
        return realEndDate;
    }

    public void setRealEndDate(Date realEndDate) {
        this.realEndDate = realEndDate;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public UserBean getClient() {
        return client;
    }

    public void setClient(UserBean client) {
        this.client = client;
    }

    public OrganizationBean getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationBean organization) {
        this.organization = organization;
    }

    public RequestStatusBean getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatusBean requestStatus) {
        this.requestStatus = requestStatus;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public int getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(int requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getDispatcherText() {
        return dispatcherText;
    }

    public void setDispatcherText(String dispatcherText) {
        this.dispatcherText = dispatcherText;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getLeadExecutorId() {
        return leadExecutorId;
    }

    public void setLeadExecutorId(int leadExecutorId) {
        this.leadExecutorId = leadExecutorId;
    }

    public int getRegistratorId() {
        return registratorId;
    }

    public void setRegistratorId(int registratorId) {
        this.registratorId = registratorId;
    }

    public RequestTypeBean getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestTypeBean requestType) {
        this.requestType = requestType;
    }

    public DepartmentBean getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentBean departmentBean) {
        this.department = departmentBean;
    }

    public UserBean getLeadExecutor() {
        return leadExecutor;
    }

    public void setLeadExecutor(UserBean leadExecutor) {
        this.leadExecutor = leadExecutor;
    }

    public Collection<TaskBean> getTasksList() {
        return tasksList;
    }

    public void setTasksList(Collection<TaskBean> tasksList) {
        this.tasksList = tasksList;
    }

    public RequestPriorityBean getPriorityBean() {
        return priorityBean;
    }

    public void setPriorityBean(RequestPriorityBean priorityBean) {
        this.priorityBean = priorityBean;
    }

    public Date getSearchFromEndDate() {
        return searchFromEndDate;
    }

    public void setSearchFromEndDate(Date searchFromEndDate) {
        this.searchFromEndDate = searchFromEndDate;
    }

    public Date getSearchToEndDate() {
        return searchToEndDate;
    }

    public void setSearchToEndDate(Date searchToEndDate) {
        this.searchToEndDate = searchToEndDate;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
