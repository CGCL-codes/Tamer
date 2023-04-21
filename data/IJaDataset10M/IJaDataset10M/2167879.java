package hibernate.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.Parameter;

@SuppressWarnings("serial")
@Entity
@Table(name = "temsilci", catalog = "emmy", uniqueConstraints = { @UniqueConstraint(columnNames = "adresid"), @UniqueConstraint(columnNames = "tcKimlik") })
@NamedQueries({ @NamedQuery(name = "findTmslcUretici", query = "from Temsilci where ureticiid = :tUretici"), @NamedQuery(name = "findTmslcTcKimlik", query = "from Temsilci where tcKimlik = :tTcKimlik"), @NamedQuery(name = "findTmslcAd", query = "from Temsilci where ad = :tAd"), @NamedQuery(name = "findTmslcSoyad", query = "from Temsilci where soyad = :tSoyad"), @NamedQuery(name = "findTmslcDgTarihi", query = "from Temsilci where dogumTarihi = :tDgTarihi"), @NamedQuery(name = "findTmslcTel1", query = "from Temsilci where Tel1 = :tTel1"), @NamedQuery(name = "findTmslcTel2", query = "from Temsilci where Tel2 = :tTel2"), @NamedQuery(name = "findTmslcFax", query = "from Temsilci where fax = :tFax"), @NamedQuery(name = "findTmslcEmail", query = "from Temsilci where email = :tEmail"), @NamedQuery(name = "findTmslcYetkisi", query = "from Temsilci where yetkisi = :tYetkisi"), @NamedQuery(name = "findTmslcUnvan", query = "from Temsilci where unvan = :tUnvan"), @NamedQuery(name = "findTmslcGrup", query = "from Temsilci where grup = :tGrup"), @NamedQuery(name = "findTmslcKisiWebad", query = "from Temsilci where kisiselWebAdd = :tKisiselWebadres") })
public class Temsilci implements java.io.Serializable {

    private long kullaniciid;

    private Adres adres;

    private Kullanici kullanici;

    private Sirketprofil sirketprofil;

    private long tcKimlik;

    private String ad;

    private String soyad;

    private String dogumTarihi;

    private long tel1;

    private Long tel2;

    private Long fax;

    private String email;

    private int yetkisi;

    private String unvan;

    private int grup;

    private String kisiselWebAdd;

    private Set<Duyurular> duyurulars = new HashSet<Duyurular>(0);

    private Set<Aygityazilimlari> aygityazilimlaris = new HashSet<Aygityazilimlari>(0);

    public Temsilci() {
    }

    public Temsilci(Adres adres, Kullanici kullanici, Sirketprofil sirketprofil, long tcKimlik, String ad, String soyad, String dogumTarihi, long tel1, String email, int yetkisi, String unvan, int grup) {
        this.adres = adres;
        this.kullanici = kullanici;
        this.sirketprofil = sirketprofil;
        this.tcKimlik = tcKimlik;
        this.ad = ad;
        this.soyad = soyad;
        this.dogumTarihi = dogumTarihi;
        this.tel1 = tel1;
        this.email = email;
        this.yetkisi = yetkisi;
        this.unvan = unvan;
        this.grup = grup;
    }

    public Temsilci(Adres adres, Kullanici kullanici, Sirketprofil sirketprofil, long tcKimlik, String ad, String soyad, String dogumTarihi, long tel1, Long tel2, Long fax, String email, int yetkisi, String unvan, int grup, String kisiselWebAdd, Set<Duyurular> duyurulars, Set<Aygityazilimlari> aygityazilimlaris) {
        this.adres = adres;
        this.kullanici = kullanici;
        this.sirketprofil = sirketprofil;
        this.tcKimlik = tcKimlik;
        this.ad = ad;
        this.soyad = soyad;
        this.dogumTarihi = dogumTarihi;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.fax = fax;
        this.email = email;
        this.yetkisi = yetkisi;
        this.unvan = unvan;
        this.grup = grup;
        this.kisiselWebAdd = kisiselWebAdd;
        this.duyurulars = duyurulars;
        this.aygityazilimlaris = aygityazilimlaris;
    }

    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "kullanici"))
    @Id
    @GeneratedValue(generator = "generator")
    @Column(name = "kullaniciid", unique = true, nullable = false)
    public long getKullaniciid() {
        return this.kullaniciid;
    }

    public void setKullaniciid(long kullaniciid) {
        this.kullaniciid = kullaniciid;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "adresid", unique = true, nullable = false)
    public Adres getAdres() {
        return this.adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public Kullanici getKullanici() {
        return this.kullanici;
    }

    public void setKullanici(Kullanici kullanici) {
        this.kullanici = kullanici;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "firmaid", nullable = false)
    public Sirketprofil getSirketprofil() {
        return this.sirketprofil;
    }

    public void setSirketprofil(Sirketprofil sirketprofil) {
        this.sirketprofil = sirketprofil;
    }

    @Column(name = "tcKimlik", unique = true, nullable = false)
    public long getTcKimlik() {
        return this.tcKimlik;
    }

    public void setTcKimlik(long tcKimlik) {
        this.tcKimlik = tcKimlik;
    }

    @Column(name = "ad", nullable = false, length = 20)
    public String getAd() {
        return this.ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    @Column(name = "soyad", nullable = false, length = 20)
    public String getSoyad() {
        return this.soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    @Column(name = "dogumTarihi", nullable = false, length = 10)
    public String getDogumTarihi() {
        return this.dogumTarihi;
    }

    public void setDogumTarihi(String dogumTarihi) {
        this.dogumTarihi = dogumTarihi;
    }

    @Column(name = "tel1", nullable = false)
    public long getTel1() {
        return this.tel1;
    }

    public void setTel1(long tel1) {
        this.tel1 = tel1;
    }

    @Column(name = "tel2")
    public Long getTel2() {
        return this.tel2;
    }

    public void setTel2(Long tel2) {
        this.tel2 = tel2;
    }

    @Column(name = "fax")
    public Long getFax() {
        return this.fax;
    }

    public void setFax(Long fax) {
        this.fax = fax;
    }

    @Column(name = "email", nullable = false, length = 30)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "yetkisi", nullable = false)
    public int getYetkisi() {
        return this.yetkisi;
    }

    public void setYetkisi(int yetkisi) {
        this.yetkisi = yetkisi;
    }

    @Column(name = "unvan", nullable = false, length = 10)
    public String getUnvan() {
        return this.unvan;
    }

    public void setUnvan(String unvan) {
        this.unvan = unvan;
    }

    @Column(name = "grup", nullable = false)
    public int getGrup() {
        return this.grup;
    }

    public void setGrup(int grup) {
        this.grup = grup;
    }

    @Column(name = "kisiselWebAdd", length = 20)
    public String getKisiselWebAdd() {
        return this.kisiselWebAdd;
    }

    public void setKisiselWebAdd(String kisiselWebAdd) {
        this.kisiselWebAdd = kisiselWebAdd;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "temsilci")
    public Set<Duyurular> getDuyurulars() {
        return this.duyurulars;
    }

    public void setDuyurulars(Set<Duyurular> duyurulars) {
        this.duyurulars = duyurulars;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "temsilci")
    public Set<Aygityazilimlari> getAygityazilimlaris() {
        return this.aygityazilimlaris;
    }

    public void setAygityazilimlaris(Set<Aygityazilimlari> aygityazilimlaris) {
        this.aygityazilimlaris = aygityazilimlaris;
    }
}
