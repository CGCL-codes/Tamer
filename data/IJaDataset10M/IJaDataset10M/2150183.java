package org.forzaframework;

import org.hibernate.annotations.GenericGenerator;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dom4j.Element;
import org.forzaframework.core.persistance.BaseEntity;
import javax.persistence.*;
import java.util.Date;

/**
 * @author cesarreyes
 *         Date: 10-sep-2008
 *         Time: 9:40:27
 */
@Entity
@Table(name = "system_log")
public class Log extends BaseEntity {

    private Long id;

    private String identifier;

    private Date date;

    private String targetClass;

    private String clazz;

    private String method;

    private String username;

    private String beforeChange;

    private String afterChange;

    private Boolean isError = false;

    private String errorMessage;

    private String stackTrace;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setKey(Object id) {
        this.id = Long.valueOf(id.toString());
    }

    @Transient
    public Object getKey() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "identifier")
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    @Column(name = "date_")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Column(name = "target_class")
    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    @Column(name = "clazz")
    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Column(name = "method")
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Column(name = "before_change", length = 4000)
    public String getBeforeChange() {
        return beforeChange;
    }

    public void setBeforeChange(String beforeChange) {
        this.beforeChange = beforeChange;
    }

    @Column(name = "after_change", length = 4000)
    public String getAfterChange() {
        return afterChange;
    }

    public void setAfterChange(String afterChange) {
        this.afterChange = afterChange;
    }

    @Column(name = "error_message", length = 4000)
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Column(name = "stack_trace", length = 4000)
    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Boolean isError() {
        return isError;
    }

    public void setError(Boolean error) {
        isError = error;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
        if (afterChange != null ? !afterChange.equals(log.afterChange) : log.afterChange != null) return false;
        if (beforeChange != null ? !beforeChange.equals(log.beforeChange) : log.beforeChange != null) return false;
        if (targetClass != null ? !targetClass.equals(log.targetClass) : log.targetClass != null) return false;
        if (id != null ? !id.equals(log.id) : log.id != null) return false;
        if (identifier != null ? !identifier.equals(log.identifier) : log.identifier != null) return false;
        return true;
    }

    public int hashCode() {
        int result;
        result = (id != null ? id.hashCode() : 0);
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        result = 31 * result + (targetClass != null ? targetClass.hashCode() : 0);
        result = 31 * result + (beforeChange != null ? beforeChange.hashCode() : 0);
        result = 31 * result + (afterChange != null ? afterChange.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(this.identifier).toString();
    }

    public Element toXml() {
        return null;
    }

    public Element toXml(String elementName) {
        return null;
    }
}
