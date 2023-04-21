package com.ttdev.centralwire;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.hibernate.Hibernate;

/**
 * <strong>Note:</strong> As the content is a Clob, one should call
 * {@link #getContentAsString()}, {@link #hasChanges()}, and
 * {@link #getStatus(Clock, int)} only within a transaction and exactly only one
 * of them can be called because the current position in the stream will NOT be
 * reset.
 * 
 * @author Kent Tong
 * 
 */
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "server_id" }) })
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String reportFileName;

    private Date creationTime;

    private Clob content;

    private boolean hasChanges;

    private Server server;

    public Report() {
    }

    public Report(Server server, String reportFileName, Date creationTime) {
        this.server = server;
        this.reportFileName = reportFileName;
        this.creationTime = creationTime;
    }

    public Report(Server server, String reportFileName, Date creationTime, String content) {
        this(server, reportFileName, creationTime);
        setContentAsString(content);
    }

    public void setContentAsString(String s) {
        this.content = Hibernate.createClob(s == null ? "" : s);
        setHasChangesFromContent(s);
    }

    public void setHasChanges(boolean hasChanges) {
        this.hasChanges = hasChanges;
    }

    public boolean isHasChanges() {
        return hasChanges;
    }

    private void setHasChangesFromContent(String content) {
        Pattern pattern = Pattern.compile("(?s)(?m).*^Total violations found:\\s+(\\d+)$.*");
        Matcher matcher = pattern.matcher(content != null ? content : "");
        if (matcher.matches()) {
            int noViolations = Integer.parseInt(matcher.group(1));
            hasChanges = noViolations > 0;
            return;
        } else {
            throw new IllegalArgumentException("Report content doesn't indicate # of violations");
        }
    }

    @Lob
    public Clob getContent() {
        return content;
    }

    public void setContent(Clob content) {
        this.content = content;
    }

    @ManyToOne
    @JoinColumn(name = "server_id")
    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public boolean isExpired(Date lowerBound) {
        return creationTime.before(lowerBound);
    }

    public String getReportFileName() {
        return reportFileName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Status getStatus(Date lowerBound) {
        if (isExpired(lowerBound)) {
            return Status.ERROR_OBTAINING_REPORT;
        } else {
            return hasChanges ? Status.HAS_CHANGES : Status.NO_CHANGES;
        }
    }

    @Transient
    public String getContentAsString() {
        return new ClobRetriever().getAsString(content);
    }
}
