package securus.entity;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.ForeignKey;

/**
 * @author e.dorofeev
 */
@Entity
@Table(name = "s_shared")
public class SharedFile extends MutableEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private long id;

    private User user_from;

    private User user_to;

    private Device device;

    private String path;

    private Date expires;

    private int mode;

    private Date revoked;

    private boolean isArchive;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_from", nullable = false)
    @ForeignKey(name = "user_from")
    public User getFrom() {
        return user_from;
    }

    public void setFrom(User user) {
        this.user_from = user;
    }

    @ManyToOne(optional = false, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_to", nullable = false)
    @ForeignKey(name = "user_to")
    public User getTo() {
        return user_to;
    }

    public void setTo(User user) {
        this.user_to = user;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "device", nullable = false)
    @ForeignKey(name = "device")
    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @Column(name = "path", nullable = false)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name = "expires")
    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date date) {
        this.expires = date;
    }

    @Column(name = "mode")
    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Column(name = "revoked")
    public Date getRevoked() {
        return revoked;
    }

    public void setRevoked(Date date) {
        this.revoked = date;
    }

    @Column(name = "archive")
    public boolean isArchive() {
        return isArchive;
    }

    public void setArchive(boolean isArchive) {
        this.isArchive = isArchive;
    }
}
