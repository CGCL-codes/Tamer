package ejb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author RostislavCh
 */
@Entity
@Table(name = "ARCHIVE", catalog = "", schema = "APP")
@NamedQueries({ @NamedQuery(name = "Archive.findAll", query = "SELECT a FROM Archive a"), @NamedQuery(name = "Archive.findByArchname", query = "SELECT a FROM Archive a WHERE a.archname = :archname"), @NamedQuery(name = "Archive.findByArchsender", query = "SELECT a FROM Archive a WHERE a.archsender = :archsender"), @NamedQuery(name = "Archive.findByArchlink", query = "SELECT a FROM Archive a WHERE a.archlink = :archlink"), @NamedQuery(name = "Archive.findByFilesize", query = "SELECT a FROM Archive a WHERE a.filesize = :filesize"), @NamedQuery(name = "Archive.findByArchId", query = "SELECT a FROM Archive a WHERE a.archId = :archId") })
public class Archive implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "ARCHNAME", length = 50)
    private String archname;

    @Column(name = "ARCHSENDER", length = 50)
    private String archsender;

    @Column(name = "ARCHLINK", length = 300)
    private String archlink;

    @Column(name = "FILESIZE")
    private int filesize;

    @Lob
    @Column(name = "BLOBDATA")
    private Serializable blobdata;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ARCH_ID", nullable = false)
    private Integer archId;

    public Archive() {
    }

    public Archive(Integer archId) {
        this.archId = archId;
    }

    public String getArchname() {
        return archname;
    }

    public void setArchname(String archname) {
        this.archname = archname;
    }

    public String getArchsender() {
        return archsender;
    }

    public void setArchsender(String archsender) {
        this.archsender = archsender;
    }

    public String getArchlink() {
        return archlink;
    }

    public void setArchlink(String archlink) {
        this.archlink = archlink;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public Serializable getBlobdata() {
        return blobdata;
    }

    public void setBlobdata(Serializable blobdata) {
        this.blobdata = blobdata;
    }

    public Integer getArchId() {
        return archId;
    }

    public void setArchId(Integer archId) {
        this.archId = archId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (archId != null ? archId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Archive)) {
            return false;
        }
        Archive other = (Archive) object;
        if ((this.archId == null && other.archId != null) || (this.archId != null && !this.archId.equals(other.archId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ejb.Archive[archId=" + archId + "]";
    }
}
