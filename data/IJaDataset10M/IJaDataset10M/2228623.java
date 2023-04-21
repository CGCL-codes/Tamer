package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author TYA
 */
@Entity
@Table(name = "hunian")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Hunian.findAll", query = "SELECT h FROM Hunian h"), @NamedQuery(name = "Hunian.findByKodehunian", query = "SELECT h FROM Hunian h WHERE h.kodehunian = :kodehunian"), @NamedQuery(name = "Hunian.findByNoktp", query = "SELECT h FROM Hunian h WHERE h.noktp = :noktp"), @NamedQuery(name = "Hunian.findByKoderumah", query = "SELECT h FROM Hunian h WHERE h.koderumah = :koderumah"), @NamedQuery(name = "Hunian.findByNamalengkap", query = "SELECT h FROM Hunian h WHERE h.namalengkap = :namalengkap"), @NamedQuery(name = "Hunian.findByTanggalmasuk", query = "SELECT h FROM Hunian h WHERE h.tanggalmasuk = :tanggalmasuk"), @NamedQuery(name = "Hunian.findByTanggalkeluar", query = "SELECT h FROM Hunian h WHERE h.tanggalkeluar = :tanggalkeluar") })
public class Hunian implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "kodehunian")
    private String kodehunian;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "noktp")
    private String noktp;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "koderumah")
    private String koderumah;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "namalengkap")
    private String namalengkap;

    @Basic(optional = false)
    @NotNull
    @Column(name = "tanggalmasuk")
    @Temporal(TemporalType.DATE)
    private Date tanggalmasuk;

    @Column(name = "tanggalkeluar")
    @Temporal(TemporalType.DATE)
    private Date tanggalkeluar;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "tanggalmasuk_hari")
    private int tanggalmasuk_hari;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "tanggalmasuk_bulan")
    private String tanggalmasuk_bulan;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "tanggalmasuk_tahun")
    private int tanggalmasuk_tahun;

    public Hunian() {
    }

    public Hunian(String kodehunian) {
        this.kodehunian = kodehunian;
    }

    public Hunian(String kodehunian, String noktp, String koderumah, String namalengkap, Date tanggalmasuk) {
        this.kodehunian = kodehunian;
        this.noktp = noktp;
        this.koderumah = koderumah;
        this.namalengkap = namalengkap;
        this.tanggalmasuk = tanggalmasuk;
    }

    public String getKodehunian() {
        return kodehunian;
    }

    public void setKodehunian(String kodehunian) {
        this.kodehunian = kodehunian;
    }

    public String getNoktp() {
        return noktp;
    }

    public void setNoktp(String noktp) {
        this.noktp = noktp;
    }

    public String getKoderumah() {
        return koderumah;
    }

    public void setKoderumah(String koderumah) {
        this.koderumah = koderumah;
    }

    public String getNamalengkap() {
        return namalengkap;
    }

    public void setNamalengkap(String namalengkap) {
        this.namalengkap = namalengkap;
    }

    public Date getTanggalmasuk() {
        return tanggalmasuk;
    }

    public void setTanggalmasuk(Date tanggalmasuk) {
        this.tanggalmasuk = tanggalmasuk;
    }

    public int getTanggalmasukhari() {
        return tanggalmasuk_hari;
    }

    public void setTanggalmasukhari(int tanggalmasuk_hari) {
        this.tanggalmasuk_hari = tanggalmasuk_hari;
    }

    public String getTanggalmasukbulan() {
        return tanggalmasuk_bulan;
    }

    public void setTanggalmasukbulan(String tanggalmasuk_bulan) {
        this.tanggalmasuk_bulan = tanggalmasuk_bulan;
    }

    public int getTanggalmasuktahun() {
        return tanggalmasuk_tahun;
    }

    public void setTanggalmasuktahun(int tanggalmasuk_tahun) {
        this.tanggalmasuk_tahun = tanggalmasuk_tahun;
    }

    public Date getTanggalkeluar() {
        return tanggalkeluar;
    }

    public void setTanggalkeluar(Date tanggalkeluar) {
        this.tanggalkeluar = tanggalkeluar;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kodehunian != null ? kodehunian.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Hunian)) {
            return false;
        }
        Hunian other = (Hunian) object;
        if ((this.kodehunian == null && other.kodehunian != null) || (this.kodehunian != null && !this.kodehunian.equals(other.kodehunian))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Hunian[ kodehunian=" + kodehunian + " ]";
    }
}
