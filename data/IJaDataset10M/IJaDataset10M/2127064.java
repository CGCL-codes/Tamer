package ch.unibas.jmeter.snmp.webservice.data;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the WEBSERVICEDATA database table.
 * 
 */
@Entity
@Table(name = "WEBSERVICEDATA")
public class WebserviceData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    private String agentIp;

    @Lob()
    private String checkUrl;

    @Lob()
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private boolean degraded;

    @Lob()
    private String errorUrl;

    private String name;

    private String probeId;

    private byte processed;

    @Lob()
    private String reporterLabel;

    @Lob()
    private String sampleLabel;

    private long sampleTime;

    public WebserviceData() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAgentIp() {
        return this.agentIp;
    }

    public void setAgentIp(String agentIp) {
        this.agentIp = agentIp;
    }

    public String getCheckUrl() {
        return this.checkUrl;
    }

    public void setCheckUrl(String checkUrl) {
        this.checkUrl = checkUrl;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getDegraded() {
        return this.degraded;
    }

    public void setDegraded(boolean degraded) {
        this.degraded = degraded;
    }

    public String getErrorUrl() {
        return this.errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProbeId() {
        return this.probeId;
    }

    public void setProbeId(String probeId) {
        this.probeId = probeId;
    }

    public byte getProcessed() {
        return this.processed;
    }

    public void setProcessed(byte processed) {
        this.processed = processed;
    }

    public String getReporterLabel() {
        return this.reporterLabel;
    }

    public void setReporterLabel(String reporterLabel) {
        this.reporterLabel = reporterLabel;
    }

    public String getSampleLabel() {
        return this.sampleLabel;
    }

    public void setSampleLabel(String sampleLabel) {
        this.sampleLabel = sampleLabel;
    }

    public long getSampleTime() {
        return this.sampleTime;
    }

    public void setSampleTime(long sampleTime) {
        this.sampleTime = sampleTime;
    }
}
