package com.x3.dishub.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;

/**
 *
 * @author Hendro Steven
 */
@Entity
public class SuratKeterangan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomor;

    @Temporal(value = javax.persistence.TemporalType.DATE)
    private Date tanggal;

    @ManyToOne
    private Kendaraan kendaraan;

    @ManyToOne
    private Pemilik pemilik;

    @ManyToOne
    private Pemilik pemilikBaru;

    private String lainLain;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "com.x3.dishub.entity.SuratKeterangan[id=" + id + "]";
    }

    /**
     * @return the nomor
     */
    public String getNomor() {
        return nomor;
    }

    /**
     * @param nomor the nomor to set
     */
    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    /**
     * @return the tanggal
     */
    public Date getTanggal() {
        return tanggal;
    }

    /**
     * @param tanggal the tanggal to set
     */
    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    /**
     * @return the kendaraan
     */
    public Kendaraan getKendaraan() {
        return kendaraan;
    }

    /**
     * @param kendaraan the kendaraan to set
     */
    public void setKendaraan(Kendaraan kendaraan) {
        this.kendaraan = kendaraan;
    }

    /**
     * @return the pemilik
     */
    public Pemilik getPemilik() {
        return pemilik;
    }

    /**
     * @param pemilik the pemilik to set
     */
    public void setPemilik(Pemilik pemilik) {
        this.pemilik = pemilik;
    }

    /**
     * @return the lainLain
     */
    public String getLainLain() {
        return lainLain;
    }

    /**
     * @param lainLain the lainLain to set
     */
    public void setLainLain(String lainLain) {
        this.lainLain = lainLain;
    }

    /**
     * @return the pemilikBaru
     */
    public Pemilik getPemilikBaru() {
        return pemilikBaru;
    }

    /**
     * @param pemilikBaru the pemilikBaru to set
     */
    public void setPemilikBaru(Pemilik pemilikBaru) {
        this.pemilikBaru = pemilikBaru;
    }
}
