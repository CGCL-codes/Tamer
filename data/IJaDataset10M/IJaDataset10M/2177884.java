package bz.ziro.kanbe.model;

import java.io.Serializable;
import java.util.Date;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;
import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

@Model
public class CommentConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    private Integer schemaVersion = 1;

    @Attribute
    private Key ownerTemplateKey;

    @Attribute
    private boolean approvalFlag = true;

    @Attribute
    private boolean nameFlag = true;

    @Attribute
    private boolean mailFlag = true;

    @Attribute
    private boolean sendMailFlag = true;

    @Attribute
    private User creator;

    @Attribute
    private Date createDate;

    @Attribute
    private User editor;

    @Attribute
    private Date editDate;

    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Returns the schema version.
     *
     * @return the schema version
     */
    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Sets the schema version.
     *
     * @param schemaVersion
     *            the schema version
     */
    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
	 * @param approvalFlag the approvalFlag to set
	 */
    public void setApprovalFlag(boolean approvalFlag) {
        this.approvalFlag = approvalFlag;
    }

    /**
	 * @return the approvalFlag
	 */
    public boolean isApprovalFlag() {
        return approvalFlag;
    }

    /**
	 * @param nameFlag the nameFlag to set
	 */
    public void setNameFlag(boolean nameFlag) {
        this.nameFlag = nameFlag;
    }

    /**
	 * @return the nameFlag
	 */
    public boolean isNameFlag() {
        return nameFlag;
    }

    /**
	 * @param mailFlag the mailFlag to set
	 */
    public void setMailFlag(boolean mailFlag) {
        this.mailFlag = mailFlag;
    }

    /**
	 * @return the mailFlag
	 */
    public boolean isMailFlag() {
        return mailFlag;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getEditor() {
        return editor;
    }

    public void setEditor(User editor) {
        this.editor = editor;
    }

    public Date getEditDate() {
        return editDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    /**
	 * @param ownerPageKey the ownerPageKey to set
	 */
    public void setOwnerTemplateKey(Key ownerPageKey) {
        this.ownerTemplateKey = ownerPageKey;
    }

    /**
	 * @return the ownerPageKey
	 */
    public Key getOwnerTemplateKey() {
        return ownerTemplateKey;
    }

    /**
	 * @param sendMailFlag the sendMailFlag to set
	 */
    public void setSendMailFlag(boolean sendMailFlag) {
        this.sendMailFlag = sendMailFlag;
    }

    /**
	 * @return the sendMailFlag
	 */
    public boolean isSendMailFlag() {
        return sendMailFlag;
    }
}
