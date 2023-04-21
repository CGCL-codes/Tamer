package com.indragunawan.restobiz.app.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author indra
 */
@Entity
@Table(name = "kas")
@NamedQueries({ @NamedQuery(name = "Kas.findByTransaksi", query = "SELECT k FROM Kas k WHERE k.kasPK.transaksi = :transaksi"), @NamedQuery(name = "Kas.findByTanggal", query = "SELECT k FROM Kas k WHERE k.kasPK.tanggal = :tanggal"), @NamedQuery(name = "Kas.findByKelompok", query = "SELECT k FROM Kas k WHERE k.kelompok = :kelompok"), @NamedQuery(name = "Kas.findByKeterangan", query = "SELECT k FROM Kas k WHERE k.keterangan = :keterangan"), @NamedQuery(name = "Kas.findByJumlah", query = "SELECT k FROM Kas k WHERE k.jumlah = :jumlah"), @NamedQuery(name = "Kas.findByShift", query = "SELECT k FROM Kas k WHERE k.shift = :shift") })
public class Kas implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected KasPK kasPK;

    @Column(name = "kelompok", nullable = false)
    private String kelompok;

    @Column(name = "keterangan", nullable = false)
    private String keterangan;

    @Column(name = "jumlah", nullable = false)
    private float jumlah;

    @Column(name = "shift", nullable = false)
    private short shift;

    @JoinColumn(name = "namauser", referencedColumnName = "namauser")
    @ManyToOne
    private Operator namauser;

    public Kas() {
    }

    public Kas(KasPK kasPK) {
        this.kasPK = kasPK;
    }

    public Kas(KasPK kasPK, String kelompok, String keterangan, float jumlah, short shift) {
        this.kasPK = kasPK;
        this.kelompok = kelompok;
        this.keterangan = keterangan;
        this.jumlah = jumlah;
        this.shift = shift;
    }

    public Kas(short transaksi, Date tanggal) {
        this.kasPK = new KasPK(transaksi, tanggal);
    }

    public KasPK getKasPK() {
        return kasPK;
    }

    public void setKasPK(KasPK kasPK) {
        this.kasPK = kasPK;
    }

    public String getKelompok() {
        return kelompok;
    }

    public void setKelompok(String kelompok) {
        this.kelompok = kelompok;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public float getJumlah() {
        return jumlah;
    }

    public void setJumlah(float jumlah) {
        this.jumlah = jumlah;
    }

    public short getShift() {
        return shift;
    }

    public void setShift(short shift) {
        this.shift = shift;
    }

    public Operator getNamauser() {
        return namauser;
    }

    public void setNamauser(Operator namauser) {
        this.namauser = namauser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (kasPK != null ? kasPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Kas)) {
            return false;
        }
        Kas other = (Kas) object;
        if ((this.kasPK == null && other.kasPK != null) || (this.kasPK != null && !this.kasPK.equals(other.kasPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.indragunawan.restobiz.app.entity.Kas[kasPK=" + kasPK + "]";
    }
}
