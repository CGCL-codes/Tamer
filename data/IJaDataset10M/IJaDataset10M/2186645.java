package fi.kaila.suku.util.pojo;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * PersonLongData contains complete copy of person data from database.
 * 
 * @author Kalle
 */
public class PersonLongData implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int pid = 0;

    private String tag = null;

    private String privacy = null;

    private String groupId = null;

    private String sex = null;

    private String sourceText = null;

    private String privateText = null;

    private String userRefn = null;

    private Timestamp created = null;

    private Timestamp modified = null;

    private String createdBy = null;

    private String modifiedBy = null;

    private boolean orderModified = false;

    private boolean mainModified = false;

    private UnitNotice[] notices = null;

    /**
	 * Gets the pid.
	 * 
	 * @return pid
	 */
    public int getPid() {
        return pid;
    }

    /**
	 * Gets the tag.
	 * 
	 * @return tag
	 */
    public String getTag() {
        return tag;
    }

    /**
	 * reset teh modified status.
	 */
    public void resetModified() {
        mainModified = false;
    }

    /**
	 * set that order has been modified.
	 */
    public void setOrderModified() {
        orderModified = true;
    }

    /**
	 * Checks if is order modified.
	 * 
	 * @return if order has been modified
	 */
    public boolean isOrderModified() {
        return orderModified;
    }

    /**
	 * Checks if is main modified.
	 * 
	 * @return true if main has been modified
	 */
    public boolean isMainModified() {
        return mainModified;
    }

    /**
	 * Gets the privacy.
	 * 
	 * @return privacy
	 */
    public String getPrivacy() {
        return privacy;
    }

    /**
	 * Gets the group id.
	 * 
	 * @return groupid
	 */
    public String getGroupId() {
        return groupId;
    }

    /**
	 * Gets the sex.
	 * 
	 * @return sex
	 */
    public String getSex() {
        return sex;
    }

    /**
	 * Sets the sex.
	 * 
	 * @param text
	 *            = sex
	 */
    public void setSex(String text) {
        if (!nv(this.sex).equals(nv(text))) {
            mainModified = true;
            this.sex = vn(text);
        }
    }

    /**
	 * Gets the source.
	 * 
	 * @return the source
	 */
    public String getSource() {
        return trim(sourceText);
    }

    /**
	 * Gets the private text.
	 * 
	 * @return private text
	 */
    public String getPrivateText() {
        return trim(privateText);
    }

    /**
	 * Gets the refn.
	 * 
	 * @return refn
	 */
    public String getRefn() {
        return userRefn;
    }

    /**
	 * Gets the created.
	 * 
	 * @return time creted
	 */
    public Timestamp getCreated() {
        return created;
    }

    /**
	 * Gets the modified.
	 * 
	 * @return tiem modified
	 */
    public Timestamp getModified() {
        return modified;
    }

    /**
	 * 
	 * @return userid of modifier
	 */
    public String getModifiedBy() {
        return this.modifiedBy;
    }

    /**
	 * 
	 * @return userid of creator
	 */
    public String getCreatedBy() {
        return this.createdBy;
    }

    /**
	 * Sets the pid.
	 * 
	 * @param pid
	 *            the new pid
	 */
    public void setPid(int pid) {
        this.pid = pid;
    }

    /**
	 * sets array of notices.
	 * 
	 * @param notices
	 *            the new notices
	 */
    public void setNotices(UnitNotice[] notices) {
        this.notices = notices;
    }

    /**
	 * Gets the notices.
	 * 
	 * @return array of notices
	 */
    public UnitNotice[] getNotices() {
        return this.notices;
    }

    /**
	 * Sets the source.
	 * 
	 * @param text
	 *            the new source
	 */
    public void setSource(String text) {
        if (!nv(this.sourceText).equals(nv(text))) {
            mainModified = true;
            this.sourceText = vn(text);
        }
    }

    /**
	 * extract unit data from select * from unit.
	 * 
	 * @param rs
	 *            the rs
	 * @throws SQLException
	 *             the sQL exception
	 */
    public PersonLongData(ResultSet rs) throws SQLException {
        this.pid = rs.getInt("pid");
        this.tag = rs.getString("tag");
        this.privacy = rs.getString("privacy");
        this.groupId = rs.getString("groupId");
        this.sex = rs.getString("sex");
        this.sourceText = rs.getString("sourceText");
        this.privateText = rs.getString("privateText");
        this.userRefn = rs.getString("userrefn");
        this.created = rs.getTimestamp("createdate");
        this.modified = rs.getTimestamp("modified");
        this.createdBy = rs.getString("createdBy");
        this.modifiedBy = rs.getString("modifiedBy");
    }

    /**
	 * Instantiates a new person long data.
	 * 
	 * @param pid
	 *            the pid
	 * @param tag
	 *            the tag
	 * @param sex
	 *            the sex
	 */
    public PersonLongData(int pid, String tag, String sex) {
        this.pid = pid;
        this.tag = tag;
        this.sex = sex;
    }

    private String trim(String text) {
        return text;
    }

    /**
	 * Sets the privacy.
	 * 
	 * @param text
	 *            "P" of null
	 */
    public void setPrivacy(String text) {
        if (!nv(this.privacy).equals(nv(text))) {
            mainModified = true;
            this.privacy = vn(text);
        }
    }

    /**
	 * Sets the group id.
	 * 
	 * @param text
	 *            the new group id
	 */
    public void setGroupId(String text) {
        if (!nv(this.groupId).equals(nv(text))) {
            mainModified = true;
            this.groupId = vn(text);
        }
    }

    /**
	 * Sets the user refn.
	 * 
	 * @param text
	 *            the new user refn
	 */
    public void setUserRefn(String text) {
        if (!nv(this.userRefn).equals(nv(text))) {
            mainModified = true;
            this.userRefn = vn(text);
        }
    }

    /**
	 * Sets the private text.
	 * 
	 * @param text
	 *            the new private text
	 */
    public void setPrivateText(String text) {
        if (!nv(this.privateText).equals(nv(text))) {
            mainModified = true;
            this.privateText = vn(text);
        }
    }

    private String nv(String text) {
        if (text == null) return "";
        return text;
    }

    private String vn(String text) {
        if (text == null || text.length() == 0) {
            text = null;
        }
        return text;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(pid);
        sb.append(",");
        sb.append(sex);
        return sb.toString();
    }
}
