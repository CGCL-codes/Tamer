package gov.ca.bdo.modeling.dsm2.map.server.data;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DSM2Study {

    /**
	 * A unique id for this file
	 */
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String studyName;

    @Persistent
    private String ownerName;

    @Persistent
    private String sharingKey;

    @Persistent
    private Date dateFirstShared;

    @Persistent
    private String sharingType;

    @Persistent
    private Text sharedUsersEmails;

    public Long getId() {
        return id;
    }

    public void setStudyName(String studyName) {
        this.studyName = studyName;
    }

    public String getStudyName() {
        return studyName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setSharingKey(String sharingKey) {
        this.sharingKey = sharingKey;
    }

    public String getSharingKey() {
        return sharingKey;
    }

    public void setDateFirstShared(Date dateFirstShared) {
        this.dateFirstShared = dateFirstShared;
    }

    public Date getDateFirstShared() {
        return dateFirstShared;
    }

    public String getSharingType() {
        return sharingType;
    }

    public void setSharingType(String sharingType) {
        this.sharingType = sharingType;
    }

    public String getSharedUsersEmails() {
        return sharedUsersEmails.getValue();
    }

    public void setSharedUsersEmails(String sharedUsersEmails) {
        this.sharedUsersEmails = new Text(sharedUsersEmails);
    }
}
