package cn.edu.pku.dr.requirement.elicitation.data;

public class Project implements java.io.Serializable {

    public static final String TABLE_NAME = "project";

    public static final String VIEW_NAME = "v_project";

    public static final String PRIMARY_KEY = "projectId";

    private Long projectId;

    private Long domainId;

    private String useState;

    private java.sql.Date buildTime;

    private java.sql.Date updateTime;

    private String projectName;

    private Long contactPersonId;

    private String projectDiscription;

    private Long creatorId;

    private String domainName;

    public static final String ID_PROPERTY = "projectId";

    public static final String DISPLAY_PROPERTY = "projectName";

    public static Boolean isUpdatable(String property) {
        if ("domainName".equals(property)) return new Boolean(false);
        return new Boolean(true);
    }

    public static String getSubClass(String property) {
        return null;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getDomainId() {
        return this.domainId;
    }

    public Long getDomainIdValue() {
        return getDomainId();
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public String getUseState() {
        return this.useState;
    }

    public void setUseState(String useState) {
        this.useState = useState;
    }

    public java.sql.Date getBuildTime() {
        return this.buildTime;
    }

    public void setBuildTime(java.sql.Date buildTime) {
        this.buildTime = buildTime;
    }

    public java.sql.Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(java.sql.Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getContactPersonId() {
        return this.contactPersonId;
    }

    public void setContactPersonId(Long contactPersonId) {
        this.contactPersonId = contactPersonId;
    }

    public String getProjectDiscription() {
        return this.projectDiscription;
    }

    public void setProjectDiscription(String projectDiscription) {
        this.projectDiscription = projectDiscription;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Object clone() {
        Object object = null;
        try {
            object = easyJ.common.BeanUtil.cloneObject(this);
        } catch (easyJ.common.EasyJException ee) {
            return null;
        }
        return object;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Project)) return false;
        Project bean = (Project) o;
        if (projectId.equals(bean.getProjectId())) return true; else return false;
    }

    public int hashCode() {
        return projectId.hashCode();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append("projectId=" + projectId + ",");
        buffer.append("domainId=" + domainId + ",");
        buffer.append("useState=" + useState + ",");
        buffer.append("buildTime=" + buildTime + ",");
        buffer.append("updateTime=" + updateTime + ",");
        buffer.append("projectName=" + projectName + ",");
        buffer.append("contactPersonId=" + contactPersonId + ",");
        buffer.append("projectDiscription=" + projectDiscription + ",");
        buffer.append("creatorId=" + creatorId + ",");
        buffer.append("domainName=" + domainName);
        buffer.append("]");
        return buffer.toString();
    }
}
