package org.sadhar.sipp.barang;

/**
 *
 * @author hendro
 */
public class Barang {

    private int idBarang;

    private String kodeBarang;

    private String namaBarang;

    private int idJenisBarang;

    private String kodeSatuanBarang;

    private String kodeUnitPenginput;

    public Barang() {
    }

    /**
     * @return the idBarang
     */
    public int getIdBarang() {
        return idBarang;
    }

    /**
     * @param idBarang the idBarang to set
     */
    public void setIdBarang(int idBarang) {
        this.idBarang = idBarang;
    }

    /**
     * @return the kodeBarang
     */
    public String getKodeBarang() {
        return kodeBarang;
    }

    /**
     * @param kodeBarang the kodeBarang to set
     */
    public void setKodeBarang(String kodeBarang) {
        this.kodeBarang = kodeBarang;
    }

    /**
     * @return the namaBarang
     */
    public String getNamaBarang() {
        return namaBarang;
    }

    /**
     * @param namaBarang the namaBarang to set
     */
    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    /**
     * @return the idJenisBarang
     */
    public int getIdJenisBarang() {
        return idJenisBarang;
    }

    /**
     * @param idJenisBarang the idJenisBarang to set
     */
    public void setIdJenisBarang(int idJenisBarang) {
        this.idJenisBarang = idJenisBarang;
    }

    /**
     * @return the kodeSatuanBarang
     */
    public String getKodeSatuanBarang() {
        return kodeSatuanBarang;
    }

    /**
     * @param kodeSatuanBarang the kodeSatuanBarang to set
     */
    public void setKodeSatuanBarang(String kodeSatuanBarang) {
        this.kodeSatuanBarang = kodeSatuanBarang;
    }

    /**
     * @return the kodeUnitPenginput
     */
    public String getKodeUnitPenginput() {
        return kodeUnitPenginput;
    }

    /**
     * @param kodeUnitPenginput the kodeUnitPenginput to set
     */
    public void setKodeUnitPenginput(String kodeUnitPenginput) {
        this.kodeUnitPenginput = kodeUnitPenginput;
    }
}
