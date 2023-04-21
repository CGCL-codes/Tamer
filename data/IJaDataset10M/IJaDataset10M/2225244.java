package hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@SuppressWarnings("serial")
@Entity
@Table(name = "aygityazilimlari", catalog = "emmy")
@NamedQueries({ @NamedQuery(name = "findAygitYazAygid", query = "from Aygityazilimlari where aygitid = :ayAygtid"), @NamedQuery(name = "findAygitYazTemsid", query = "from Aygityazilimlari where kullaniciid = :ayTemsid"), @NamedQuery(name = "findAygitYazFirmaid", query = "from Aygityazilimlari where firmaid = :ayFirmaid"), @NamedQuery(name = "findAygitYazUpTrh", query = "from Aygityazilimlari where uploadTarihi = :ayUpTrh"), @NamedQuery(name = "findAygitYazVerNo", query = "from Aygityazilimlari where versiyonNo = :ayVerNo"), @NamedQuery(name = "findAygitYazDiskKnm", query = "from Aygityazilimlari where diskKonumu = :ayDiskKnm"), @NamedQuery(name = "findAygitYazAcklama", query = "from Aygityazilimlari where aciklama like :ayAcklama") })
public class Aygityazilimlari implements java.io.Serializable {

    private Long aygitYazid;

    private Temsilci temsilci;

    private Sirketprofil sirketprofil;

    private Aygit aygit;

    private String uploadTarihi;

    private String versiyonNo;

    private String diskKonumu;

    private String aciklama;

    public Aygityazilimlari() {
    }

    public Aygityazilimlari(Temsilci temsilci, Sirketprofil sirketprofil, Aygit aygit, String uploadTarihi, String versiyonNo, String diskKonumu, String aciklama) {
        this.temsilci = temsilci;
        this.sirketprofil = sirketprofil;
        this.aygit = aygit;
        this.uploadTarihi = uploadTarihi;
        this.versiyonNo = versiyonNo;
        this.diskKonumu = diskKonumu;
        this.aciklama = aciklama;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "aygitYazid", unique = true, nullable = false)
    public Long getAygitYazid() {
        return this.aygitYazid;
    }

    public void setAygitYazid(Long aygitYazid) {
        this.aygitYazid = aygitYazid;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kullaniciid", nullable = false)
    public Temsilci getTemsilci() {
        return this.temsilci;
    }

    public void setTemsilci(Temsilci temsilci) {
        this.temsilci = temsilci;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "firmaid", nullable = false)
    public Sirketprofil getSirketprofil() {
        return this.sirketprofil;
    }

    public void setSirketprofil(Sirketprofil sirketprofil) {
        this.sirketprofil = sirketprofil;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aygitid", nullable = false)
    public Aygit getAygit() {
        return this.aygit;
    }

    public void setAygit(Aygit aygit) {
        this.aygit = aygit;
    }

    @Column(name = "uploadTarihi", nullable = false, length = 10)
    public String getUploadTarihi() {
        return this.uploadTarihi;
    }

    public void setUploadTarihi(String uploadTarihi) {
        this.uploadTarihi = uploadTarihi;
    }

    @Column(name = "versiyonNo", nullable = false, length = 20)
    public String getVersiyonNo() {
        return this.versiyonNo;
    }

    public void setVersiyonNo(String versiyonNo) {
        this.versiyonNo = versiyonNo;
    }

    @Column(name = "diskKonumu", nullable = false, length = 300)
    public String getDiskKonumu() {
        return this.diskKonumu;
    }

    public void setDiskKonumu(String diskKonumu) {
        this.diskKonumu = diskKonumu;
    }

    @Column(name = "aciklama", nullable = false, length = 100)
    public String getAciklama() {
        return this.aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
