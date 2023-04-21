package ces.coffice.webmail.datamodel.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MailAffix implements Serializable {

    /** identifier field */
    private Long id;

    /** nullable persistent field */
    private long boxId;

    /** nullable persistent field */
    private long mailId;

    /** nullable persistent field */
    private String attachName;

    /** nullable persistent field */
    private BigDecimal attachSize;

    /** nullable persistent field */
    private String fileSave;

    /** nullable persistent field */
    private String attachAlias;

    /** nullable persistent field */
    private String flag;

    /** persistent field */
    private long userId;

    /** full constructor */
    public MailAffix(Long id, long boxId, long mailId, String attachName, BigDecimal attachSize, String fileSave, String attachAlias, String flag, long userId) {
        this.id = id;
        this.boxId = boxId;
        this.mailId = mailId;
        this.attachName = attachName;
        this.attachSize = attachSize;
        this.fileSave = fileSave;
        this.attachAlias = attachAlias;
        this.flag = flag;
        this.userId = userId;
    }

    /** default constructor */
    public MailAffix() {
    }

    /** minimal constructor */
    public MailAffix(Long id, long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getBoxId() {
        return this.boxId;
    }

    public void setBoxId(long boxId) {
        this.boxId = boxId;
    }

    public long getMailId() {
        return this.mailId;
    }

    public void setMailId(long mailId) {
        this.mailId = mailId;
    }

    public String getAttachName() {
        return this.attachName;
    }

    public void setAttachName(String attachName) {
        this.attachName = attachName;
    }

    public BigDecimal getAttachSize() {
        return this.attachSize;
    }

    public void setAttachSize(BigDecimal attachSize) {
        this.attachSize = attachSize;
    }

    public String getFileSave() {
        return this.fileSave;
    }

    public void setFileSave(String fileSave) {
        this.fileSave = fileSave;
    }

    public String getAttachAlias() {
        return this.attachAlias;
    }

    public void setAttachAlias(String attachAlias) {
        this.attachAlias = attachAlias;
    }

    public String getFlag() {
        return this.flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public boolean equals(Object other) {
        if (!(other instanceof MailAffix)) return false;
        MailAffix castOther = (MailAffix) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
