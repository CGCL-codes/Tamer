package org.sadhar.sipp.gelombang;

/**
 *
 * @author Yohan Hardinugroho
 */
public class Gelombang {

    private String kd_gelombang;

    private String nm_gelombang;

    private int aktifasi;

    private int kelompok;

    public Gelombang() {
    }

    public Gelombang(String kd_gelombang, String nm_gelombang, int aktifasi, int kelompok) {
        this.kd_gelombang = kd_gelombang;
        this.nm_gelombang = nm_gelombang;
        this.aktifasi = aktifasi;
        this.kelompok = kelompok;
    }

    public void setKd_gelombang(String kd_gelombang) {
        this.kd_gelombang = kd_gelombang;
    }

    public String getKd_gelombang() {
        return kd_gelombang;
    }

    public void setNm_gelombang(String nm_gelombang) {
        this.nm_gelombang = nm_gelombang;
    }

    public String getNm_gelombang() {
        return nm_gelombang;
    }

    public void setAktifasi(int aktifasi) {
        this.aktifasi = aktifasi;
    }

    public int getAktifasi() {
        return aktifasi;
    }

    public void setKelompok(int kelompok) {
        this.kelompok = kelompok;
    }

    public int getKelompok() {
        return kelompok;
    }
}
