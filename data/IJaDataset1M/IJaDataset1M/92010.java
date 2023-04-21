package mpower_hibernate;

import java.io.Serializable;

/**
 * 
 *
 * 
 *
 */
public class AuditLog implements Serializable {

    public AuditLog() {
    }

    public AuditLog(long audit_log_date_time, long owner_data_id, String serviceName, String serviceMethod, LevelType audit_level, AuditStatusType audit_status, AuditType audit_type, String description, User user) {
        this.audit_log_date_time = audit_log_date_time;
        this.serviceName = serviceName;
        this.serviceMethod = serviceMethod;
        this.audit_level = audit_level;
        this.audit_type = audit_type;
        this.audit_status = audit_status;
        this.owner_data_id = owner_data_id;
        this.description = description;
        this.user = user;
        this.user.addAuditLog(this);
    }

    private long owner_data_id;

    private long audit_log_date_time;

    private String serviceName;

    private String serviceMethod;

    private String description;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private LevelType audit_level;

    public LevelType getAudit_level() {
        return audit_level;
    }

    public void setAudit_level(LevelType audit_level) {
        this.audit_level = audit_level;
    }

    private AuditStatusType audit_status;

    public AuditStatusType getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(AuditStatusType audit_status) {
        this.audit_status = audit_status;
    }

    private AuditType audit_type;

    public AuditType getAudit_type() {
        return audit_type;
    }

    public void setAudit_type(AuditType audit_type) {
        this.audit_type = audit_type;
    }

    private long id;

    /**
  *   @hibernate.id
  *     generator-class="increment"
  */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(String serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAudit_log_date_time() {
        return audit_log_date_time;
    }

    public void setAudit_log_date_time(long audit_log_date_time) {
        this.audit_log_date_time = audit_log_date_time;
    }

    public long getOwner_data_id() {
        return owner_data_id;
    }

    public void setOwner_data_id(long owner_data_id) {
        this.owner_data_id = owner_data_id;
    }
}
