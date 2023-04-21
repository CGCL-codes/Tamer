package com.x3.dishub.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 *
 * @author Hendro Steven
 */
@Entity
public class Pemilik implements Serializable {

    @ManyToMany(mappedBy = "pemilik")
    private List<Kendaraan> kendaraans;

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaDepan;

    private String namaBelakang;

    private String alamat;

    @ManyToOne
    private KabupatenKota kabKota;

    @ManyToOne
    private Provinsi provinsi;

    private String tlp;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getNamaDepan() + " " + this.getNamaBelakang();
    }

    /**
     * @return the namaDepan
     */
    public String getNamaDepan() {
        return namaDepan;
    }

    /**
     * @param namaDepan the namaDepan to set
     */
    public void setNamaDepan(String namaDepan) {
        this.namaDepan = namaDepan;
    }

    /**
     * @return the namaBelakang
     */
    public String getNamaBelakang() {
        return namaBelakang;
    }

    /**
     * @param namaBelakang the namaBelakang to set
     */
    public void setNamaBelakang(String namaBelakang) {
        this.namaBelakang = namaBelakang;
    }

    /**
     * @return the alamat
     */
    public String getAlamat() {
        return alamat;
    }

    /**
     * @param alamat the alamat to set
     */
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    /**
     * @return the kabKota
     */
    public KabupatenKota getKabKota() {
        return kabKota;
    }

    /**
     * @param kabKota the kabKota to set
     */
    public void setKabKota(KabupatenKota kabKota) {
        this.kabKota = kabKota;
    }

    /**
     * @return the provinsi
     */
    public Provinsi getProvinsi() {
        return provinsi;
    }

    /**
     * @param provinsi the provinsi to set
     */
    public void setProvinsi(Provinsi provinsi) {
        this.provinsi = provinsi;
    }

    /**
     * @return the tlp
     */
    public String getTlp() {
        return tlp;
    }

    /**
     * @param tlp the tlp to set
     */
    public void setTlp(String tlp) {
        this.tlp = tlp;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the kendaraans
     */
    public List<Kendaraan> getKendaraans() {
        return kendaraans;
    }

    /**
     * @param kendaraans the kendaraans to set
     */
    public void setKendaraans(List<Kendaraan> kendaraans) {
        this.kendaraans = kendaraans;
    }
}
