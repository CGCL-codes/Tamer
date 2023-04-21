package com.quesofttech.business.domain.general;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PostPersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.TableGenerator;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.persistence.Embedded;
import com.quesofttech.business.common.exception.BusinessException;
import com.quesofttech.business.domain.base.BaseEntity;
import com.quesofttech.business.domain.embeddable.RowInfo;
import com.quesofttech.business.domain.inventory.Material;
import com.quesofttech.business.common.exception.ValueRequiredException;
import com.quesofttech.business.common.exception.GenericBusinessException;
import com.quesofttech.util.StringUtil;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "BOM", uniqueConstraints = { @UniqueConstraint(columnNames = { "fk_Material", "bom_Type" }) })
@SuppressWarnings("serial")
public class BOM extends BaseEntity {

    @TableGenerator(name = "BOM_id", table = "PrimaryKeys", pkColumnName = "tableName", pkColumnValue = "BOM", valueColumnName = "keyField")
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "BOM_id")
    @Column(name = "id_BOM", nullable = false)
    private Long id;

    @Version
    @Column(nullable = false)
    private Long version;

    @Column(name = "bom_Type", length = 1, nullable = false)
    private String type;

    @Embedded
    RowInfo rowInfo;

    @ManyToOne
    @JoinColumn(name = "fk_Material")
    private Material material;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "bom", targetEntity = BomDetail.class)
    private List<BomDetail> bomDetails;

    public BOM() {
        super();
        this.rowInfo = new RowInfo();
    }

    public BOM(String type, String recordStatus, String sessionId, String createLogin, String createApp, Timestamp createTimestamp, String modifyLogin, String modifyApp, Timestamp modifyTimestamp) {
        this();
        this.type = type;
        this.rowInfo.setRecordStatus(recordStatus);
        this.rowInfo.setSessionId(sessionId);
        this.rowInfo.setCreateLogin(createLogin);
        this.rowInfo.setCreateApp(createApp);
        this.rowInfo.setCreateTimestamp(createTimestamp);
        this.rowInfo.setModifyLogin(modifyLogin);
        this.rowInfo.setModifyApp(modifyApp);
        this.rowInfo.setModifyTimestamp(modifyTimestamp);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("BOM: [");
        buf.append("id=" + id + ", ");
        buf.append("type=" + type + ", ");
        buf.append("record status=" + rowInfo.getRecordStatus() + ", ");
        buf.append("session id=" + rowInfo.getSessionId() + ", ");
        buf.append("create login=" + rowInfo.getCreateLogin() + ", ");
        buf.append("create app=" + rowInfo.getCreateApp() + ", ");
        buf.append("create timestamp=" + rowInfo.getCreateTimestamp() + ", ");
        buf.append("modify login=" + rowInfo.getModifyLogin() + ", ");
        buf.append("modify app=" + rowInfo.getModifyApp() + ", ");
        buf.append("modify time=" + rowInfo.getModifyTimestamp() + ", ");
        buf.append("version=" + version);
        buf.append("]");
        return buf.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this) || (obj instanceof BOM) && getId() != null && ((BOM) obj).getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return getId() == null ? super.hashCode() : getId().hashCode();
    }

    @Override
    public Serializable getIdForMessages() {
        return getId();
    }

    @PrePersist
    void prePersist() throws BusinessException {
        validate();
        rowInfo.setRecordStatus("A");
        java.util.Date today = new java.util.Date();
        rowInfo.setModifyTimestamp(new java.sql.Timestamp(today.getTime()));
        rowInfo.setCreateTimestamp(rowInfo.getModifyTimestamp());
    }

    @PostPersist
    void postPersist() throws BusinessException {
    }

    @PostLoad
    void postLoad() {
    }

    @PreUpdate
    void preUpdate() throws BusinessException {
        if (rowInfo.getRecordStatus() != "D") {
            validate();
        }
        java.util.Date today = new java.util.Date();
        rowInfo.setModifyTimestamp(new java.sql.Timestamp(today.getTime()));
    }

    @PreRemove
    void preRemove() throws BusinessException {
    }

    public void validate() throws BusinessException {
        if (StringUtil.isEmpty(type)) {
            throw new ValueRequiredException(this, "BOM_Type");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getRecordStatus() {
        return rowInfo.getRecordStatus();
    }

    public void setRecordStatus(String recordStatus) {
        this.rowInfo.setRecordStatus(recordStatus);
    }

    public String getSessionId() {
        return rowInfo.getSessionId();
    }

    public void setSessionId(String sessionId) {
        this.rowInfo.setSessionId(sessionId);
    }

    public String getCreateLogin() {
        return rowInfo.getCreateLogin();
    }

    public void setCreateLogin(String createLogin) {
        this.rowInfo.setCreateLogin(createLogin);
    }

    public String getCreateApp() {
        return rowInfo.getCreateApp();
    }

    public void setCreateApp(String createApp) {
        this.rowInfo.setCreateApp(createApp);
    }

    public java.sql.Timestamp getCreateTimestamp() {
        return rowInfo.getCreateTimestamp();
    }

    public void setCreateTimestamp(java.sql.Timestamp createTimestamp) {
        this.rowInfo.setCreateTimestamp(createTimestamp);
    }

    public String getModifyLogin() {
        return rowInfo.getModifyLogin();
    }

    public void setModifyLogin(String modifyLogin) {
        this.rowInfo.setModifyLogin(modifyLogin);
    }

    public String getModifyApp() {
        return rowInfo.getModifyApp();
    }

    public void setModifyApp(String modifyApp) {
        this.rowInfo.setModifyApp(modifyApp);
    }

    public java.sql.Timestamp getModifyTimestamp() {
        return rowInfo.getModifyTimestamp();
    }

    public void setModifyTimestamp(java.sql.Timestamp modifyTimestamp) {
        this.rowInfo.setModifyTimestamp(modifyTimestamp);
    }

    /**
	 * @return the type
	 */
    public String getType() {
        return type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    public void addBomDetail(BomDetail bomDetail) {
        if (this.bomDetails == null) bomDetails = new ArrayList<BomDetail>();
        bomDetails.add(bomDetail);
        if (!bomDetails.contains(bomDetail)) {
            bomDetails.add(bomDetail);
        }
    }
}
