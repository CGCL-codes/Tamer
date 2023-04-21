package com.fisoft.phucsinh.phucsinhsrv.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Thanh Liem
 *
 */
@Entity
@Table(name = "rp_report")
@NamedQueries({ @NamedQuery(name = "RpReport.findAll", query = "SELECT r FROM RpReport r"), @NamedQuery(name = "RpReport.findByReportID", query = "SELECT r FROM RpReport r WHERE r.reportID = :reportID"), @NamedQuery(name = "RpReport.findByFileName", query = "SELECT r FROM RpReport r WHERE r.fileName like :fileName"), @NamedQuery(name = "RpReport.findByReportName", query = "SELECT r FROM RpReport r WHERE r.reportName = :reportName"), @NamedQuery(name = "RpReport.findByUpoadDate", query = "SELECT r FROM RpReport r WHERE r.upoadDate = :upoadDate"), @NamedQuery(name = "RpReport.findByReportType", query = "SELECT r FROM RpReport r WHERE r.reportType = :reportType") })
public class RpReport implements Serializable, ICommonEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ReportID", nullable = false)
    private Integer reportID;

    @Basic(optional = false)
    @Column(name = "FileName")
    private String fileName;

    @Basic(optional = false)
    @Column(name = "ReportName")
    private String reportName;

    @Basic(optional = false)
    @Column(name = "UpoadDate")
    @Temporal(TemporalType.DATE)
    private Date upoadDate;

    @Basic(optional = false)
    @Column(name = "Uploader")
    private String uploader;

    @OneToMany(mappedBy = "reportID")
    private Collection<RpParam> rpParamCollection;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "rpreport_user", joinColumns = @JoinColumn(name = "ReportID", referencedColumnName = "ReportID"), inverseJoinColumns = @JoinColumn(name = "UserName", referencedColumnName = "UserName"))
    private Set<UserEntity> userEntityCollection = new HashSet<UserEntity>();

    @Enumerated(EnumType.STRING)
    @Column(name = "ReportType", nullable = false)
    private ReportType reportType;

    public enum ReportType {

        General("General", 1), Payable("Payable", 3), Receivable("Receivable", 2), Warehouse("Warehouse", 6), Purchase("Purchase", 5), Sales("Sales", 4), Finance("Finance", 0);

        private final String value;

        private int index;

        ReportType(String value, int index) {
            this.value = value;
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public String getValue() {
            return value;
        }

        public static ReportType getEnum(String value) {
            for (ReportType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    ;

    public RpReport() {
    }

    public RpReport(Integer reportID) {
        this.reportID = reportID;
    }

    public RpReport(Integer reportID, String fileName, String reportName, Date upoadDate, String uploader) {
        this.reportID = reportID;
        this.fileName = fileName;
        this.reportName = reportName;
        this.upoadDate = upoadDate;
        this.uploader = uploader;
    }

    public Integer getReportID() {
        return reportID;
    }

    public void setReportID(Integer reportID) {
        this.reportID = reportID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Date getUpoadDate() {
        return upoadDate;
    }

    public void setUpoadDate(Date upoadDate) {
        this.upoadDate = upoadDate;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public Collection<RpParam> getRpParamCollection() {
        return rpParamCollection;
    }

    public void setRpParamCollection(Collection<RpParam> rpParamCollection) {
        this.rpParamCollection = rpParamCollection;
    }

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public Collection<UserEntity> getUserEntityCollection() {
        return userEntityCollection;
    }

    public void setUserEntityCollection(HashSet<UserEntity> userCollection) {
        this.userEntityCollection = userCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reportID != null ? reportID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RpReport)) {
            return false;
        }
        RpReport other = (RpReport) object;
        if ((this.reportID == null && other.reportID != null) || (this.reportID != null && !this.reportID.equals(other.reportID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.fisoft.phucsinh.phucsinhsrv.entity.RpReport[reportID=" + reportID + "]";
    }

    public Object getID() {
        return this.reportID;
    }

    public void setID(Object id) {
        this.reportID = (Integer) id;
    }

    public Integer getActiveStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setActiveStatus(Integer activeStatus) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
