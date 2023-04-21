package de.psychomatic.mp3db.core.dblayer;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.TABLE;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import de.psychomatic.mp3db.core.dblayer.dao.CoveritemDataDAO;
import de.psychomatic.mp3db.core.interfaces.CoveritemIf;
import de.psychomatic.mp3db.core.interfaces.WriteableIf;

@Entity
@Table(name = "coveritem")
public class Coveritem implements Serializable, CoveritemIf<Album>, WriteableIf {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "albumid")
    private Album album;

    @Id
    @GeneratedValue(strategy = TABLE, generator = "coveritemId")
    @TableGenerator(name = "coveritemId", pkColumnName = "idname", table = "idtable", valueColumnName = "pkid", initialValue = 1, allocationSize = 2)
    @Column(name = "ciid")
    private Integer ciid;

    @Column(nullable = false, length = 64, name = "citype")
    private String citype;

    public Coveritem() {
        super();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coveritem other = (Coveritem) obj;
        if (ciid == null) {
            if (other.ciid != null) {
                return false;
            }
            return super.equals(obj);
        } else if (!ciid.equals(other.ciid)) {
            return false;
        }
        return true;
    }

    @Override
    @Transient
    public byte[] getCidata() {
        return CoveritemDataDAO.getCoverData(this);
    }

    @Override
    public Album getAlbum() {
        return this.album;
    }

    @Override
    public Integer getCiid() {
        return this.ciid;
    }

    @Override
    public String getCitype() {
        return this.citype;
    }

    @Override
    @Transient
    public void setCidata(final byte[] cidata) {
        CoveritemDataDAO.setCoverData(this, cidata);
    }

    @Override
    public int hashCode() {
        if (ciid == null) {
            return super.hashCode();
        }
        final int prime = 19;
        int result = 1;
        result = prime * result + (ciid == null ? 0 : ciid.hashCode());
        return result;
    }

    @Override
    public void setAlbum(final Album album) {
        this.album = album;
    }

    @Override
    public void setCiid(final Integer ciid) {
        this.ciid = ciid;
    }

    @Override
    public void setCitype(final String citype) {
        this.citype = citype;
    }
}
