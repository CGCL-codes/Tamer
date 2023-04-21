package de.objectcode.time4u.server.ejb.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PROJECT_PROPERTIES")
public class ProjectProperty {

    private long m_id;

    private String m_name;

    private String m_strValue;

    private Integer m_intValue;

    private Boolean m_boolValue;

    private Date m_dateValue;

    private Project m_project;

    @Id
    @GeneratedValue
    public long getId() {
        return m_id;
    }

    public void setId(long id) {
        m_id = id;
    }

    @Column(length = 200, nullable = false)
    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    @Column(name = "strValue", length = 1000, nullable = true)
    public String getStrValue() {
        return m_strValue;
    }

    public void setStrValue(String strValue) {
        m_strValue = strValue;
    }

    @Column(name = "boolValue", nullable = true)
    public Boolean getBoolValue() {
        return m_boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        m_boolValue = boolValue;
    }

    @Column(name = "dateValue", nullable = true)
    public Date getDateValue() {
        return m_dateValue;
    }

    public void setDateValue(Date dateValue) {
        m_dateValue = dateValue;
    }

    @Column(name = "intValue", nullable = true)
    public Integer getIntValue() {
        return m_intValue;
    }

    public void setIntValue(Integer intValue) {
        m_intValue = intValue;
    }

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    public Project getProject() {
        return m_project;
    }

    public void setProject(Project project) {
        m_project = project;
    }
}
