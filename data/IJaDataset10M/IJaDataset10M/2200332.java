package corner.demo.model.one;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import corner.demo.model.AbstractModel;
import corner.model.IBlobModel;
import corner.orm.hibernate.v3.MatrixRow;
import corner.orm.tapestry.component.selectBox.ISelectBox;
import corner.service.svn.IVersionable;

/**
 *
 * @author jcai
 * @version $Revision:1162 $
 * @since 0.5.2
 * @hibernate.class table="oneA"
 * @hibernate.cache usage="read-write"
 * @hibernate.mapping auto-import="false"
 */
@Entity(name = "oneA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class A extends AbstractModel implements IBlobModel, IVersionable, ISelectBox {

    /**
	 *
	 */
    private static final long serialVersionUID = -1296703401706863951L;

    /**
	 * @hibernate.property type="corner.orm.hibernate.v3.VectorType"
	 */
    private MatrixRow<String> colors;

    private String password;

    private double num;

    private double maxnum;

    private double plusnum;

    private String birthday;

    private String bornDate;

    /**
	 * @return Returns the bornDate.
	 */
    public String getBornDate() {
        return bornDate;
    }

    /**
	 * @param bornDate The bornDate to set.
	 */
    public void setBornDate(String bornDate) {
        this.bornDate = bornDate;
    }

    /**
	 * @return Returns the num.
	 */
    public double getNum() {
        return num;
    }

    /**
	 * @param num The num to set.
	 */
    public void setNum(double num) {
        this.num = num;
    }

    /**
	 * @return Returns the colors.
	 */
    @Type(type = "corner.orm.hibernate.v3.VectorType")
    public MatrixRow<String> getColors() {
        return colors;
    }

    /**
	 * @param colors
	 *            The colors to set.
	 */
    public void setColors(MatrixRow<String> colors) {
        this.colors = colors;
    }

    /**
	 * @return Returns the password.
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password The password to set.
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * blob数据.
	 * @hibernate.property column="BlobData" length="2147483647"
	 *                     type="org.springframework.orm.hibernate3.support.BlobByteArrayType"
	 * 
	 */
    private byte[] blobData;

    /**
	 * blob数据的类型,此类型用来web页面的显示,可能的结果为:image/jpeg,image/gif,application/pdf 等.
	 * @hibernate.property column="ContentType" length="30"
	 */
    private String contentType;

    /**
	 * @see corner.model.IBlobModel#getBlobData()
	 */
    @Lob
    @Type(type = "org.springframework.orm.hibernate3.support.BlobByteArrayType")
    public byte[] getBlobData() {
        return blobData;
    }

    /**
	 * 文件名称
	 */
    private String blobName;

    private String filePath;

    /**
	 * @return Returns the blobName.
	 */
    public String getBlobName() {
        return blobName;
    }

    /**
	 * @param blobName The blobName to set.
	 */
    public void setBlobName(String blobName) {
        this.blobName = blobName;
    }

    /**
	 * @see corner.model.IBlobModel#setBlobData(byte[])
	 */
    public void setBlobData(byte[] blobData) {
        this.blobData = blobData;
    }

    /**
	 * @see corner.model.IBlobModel#getContentType()
	 */
    public String getContentType() {
        return contentType;
    }

    /**
	 * @see corner.model.IBlobModel#setContentType(java.lang.String)
	 */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public double getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(double maxnum) {
        this.maxnum = maxnum;
    }

    public double getPlusnum() {
        return plusnum;
    }

    public void setPlusnum(double plusnum) {
        this.plusnum = plusnum;
    }

    /**
	 * @return Returns the birthday.
	 */
    public String getBirthday() {
        return birthday;
    }

    /**
	 * @param birthday The birthday to set.
	 */
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    private String revision;

    private boolean svnCommit;

    private String svnLog;

    /**
	 * @see corner.service.svn.IVersionable#getNeedVersionableProperties()
	 */
    @Transient
    public String[] getNeedVersionableProperties() {
        return new String[] { "name", "password", "blobData" };
    }

    /**
	 * @see corner.service.svn.IVersionable#getRevision()
	 */
    @Transient
    public String getRevision() {
        return revision;
    }

    /**
	 * @see corner.service.svn.IVersionable#getSvnAuthor()
	 */
    @Transient
    public String getSvnAuthor() {
        return "testUser";
    }

    /**
	 * @see corner.service.svn.IVersionable#getSvnLog()
	 */
    @Transient
    public String getSvnLog() {
        return svnLog;
    }

    /**
	 * @see corner.service.svn.IVersionable#isSvnCommit()
	 */
    @Transient
    public boolean isSvnCommit() {
        return svnCommit;
    }

    /**
	 * @see corner.service.svn.IVersionable#setRevision(java.lang.String)
	 */
    public void setRevision(String revison) {
        this.revision = revison;
    }

    /**
	 * @param svnCommit The svnCommit to set.
	 */
    public void setSvnCommit(boolean svnCommit) {
        this.svnCommit = svnCommit;
    }

    /**
	 * @param svnLog The svnLog to set.
	 */
    public void setSvnLog(String svnLog) {
        this.svnLog = svnLog;
    }

    /**
	 * @see corner.service.svn.IVersionable#getParentObject()
	 */
    @Transient
    public Object getParentObject() {
        return null;
    }

    /**
	 * @see corner.orm.tapestry.component.selectBox.ISelectBox#getLabel()
	 */
    @Transient
    public String getLabel() {
        return this.getName();
    }

    /**
	 * @see corner.orm.tapestry.component.selectBox.ISelectBox#getValue()
	 */
    @Transient
    public String getValue() {
        return this.getId();
    }

    /**
	 * @return Returns the filePath.
	 */
    public String getFilePath() {
        return filePath;
    }

    /**
	 * @param filePath The filePath to set.
	 */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
