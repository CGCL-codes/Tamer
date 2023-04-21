package org.srvlnc.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.srvlnc.base.model.entity.BaseEntity;

@Entity
@Table(name = "url_job", schema = "public")
public class UrlJob extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private SmtpConfig smtpConfig;

    private String jobName;

    private String description;

    private String cronString;

    private char statusEnable;

    private Set<Sla> slas = new HashSet<Sla>(0);

    private Set<JobOcurrence> jobOcurrences = new HashSet<JobOcurrence>(0);

    public UrlJob() {
    }

    public UrlJob(Integer id, String jobName, String cronString, char statusEnable) {
        this.id = id;
        this.jobName = jobName;
        this.cronString = cronString;
        this.statusEnable = statusEnable;
    }

    public UrlJob(Integer id, SmtpConfig smtpConfig, String jobName, String description, String cronString, char statusEnable, Set<Sla> slas, Set<Sla> slas_1, Set<JobOcurrence> jobOcurrences, Set<JobOcurrence> jobOcurrences_1) {
        this.id = id;
        this.smtpConfig = smtpConfig;
        this.jobName = jobName;
        this.description = description;
        this.cronString = cronString;
        this.statusEnable = statusEnable;
        this.slas = slas;
        this.jobOcurrences = jobOcurrences;
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_smtp_config")
    public SmtpConfig getSmtpConfig() {
        return this.smtpConfig;
    }

    public void setSmtpConfig(SmtpConfig smtpConfig) {
        this.smtpConfig = smtpConfig;
    }

    @Column(name = "job_name", nullable = false, length = 100)
    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Column(name = "description", length = 5000)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "cron_string", nullable = false, length = 1000)
    public String getCronString() {
        return this.cronString;
    }

    public void setCronString(String cronString) {
        this.cronString = cronString;
    }

    @Column(name = "status_enable", nullable = false, length = 1)
    public char getStatusEnable() {
        return this.statusEnable;
    }

    public void setStatusEnable(char statusEnable) {
        this.statusEnable = statusEnable;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "urlJob")
    public Set<Sla> getSlas() {
        return this.slas;
    }

    public void setSlas(Set<Sla> slas) {
        this.slas = slas;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "urlJob")
    public Set<JobOcurrence> getJobOcurrences() {
        return this.jobOcurrences;
    }

    public void setJobOcurrences(Set<JobOcurrence> jobOcurrences) {
        this.jobOcurrences = jobOcurrences;
    }
}
