package hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@SuppressWarnings("serial")
@Entity
@Table(name = "bekleyenhatalistesi", catalog = "emmy")
@NamedQueries({ @NamedQuery(name = "findBklynHataLtHataKodu", query = "from Bekleyenhatalistesi where hataKodu = :BhKodu"), @NamedQuery(name = "findBklynHataLtAygtid", query = "from Bekleyenhatalistesi where aygitid = :BhAygtid"), @NamedQuery(name = "findBklynHataLtAygtSrnNo", query = "from Bekleyenhatalistesi where aygitSeriNo = :BhAygtSrnNo") })
public class Bekleyenhatalistesi implements java.io.Serializable {

    private long bkid;

    private Aygit aygit;

    private long hataKodu;

    private long aygitSeriNo;

    public Bekleyenhatalistesi() {
    }

    public Bekleyenhatalistesi(long bkid, Aygit aygit, long hataKodu, long aygitSeriNo) {
        this.bkid = bkid;
        this.aygit = aygit;
        this.hataKodu = hataKodu;
        this.aygitSeriNo = aygitSeriNo;
    }

    @Id
    @Column(name = "bkid", unique = true, nullable = false)
    public long getBkid() {
        return this.bkid;
    }

    public void setBkid(long bkid) {
        this.bkid = bkid;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aygitid", nullable = false)
    public Aygit getAygit() {
        return this.aygit;
    }

    public void setAygit(Aygit aygit) {
        this.aygit = aygit;
    }

    @Column(name = "hataKodu", nullable = false)
    public long getHataKodu() {
        return this.hataKodu;
    }

    public void setHataKodu(long hataKodu) {
        this.hataKodu = hataKodu;
    }

    @Column(name = "aygitSeriNo", nullable = false)
    public long getAygitSeriNo() {
        return this.aygitSeriNo;
    }

    public void setAygitSeriNo(long aygitSeriNo) {
        this.aygitSeriNo = aygitSeriNo;
    }
}
