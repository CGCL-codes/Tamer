package wii.edu.core.model;

/**
 *
 * @author Retha@wii
 */
public class TranskripAkhirMahasiswa {

    private String kode;

    private String nama;

    private String semester;

    private String sks;

    private String nilai;

    private String angka;

    private String bobot;

    private String sksBayar;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSksBayar() {
        return sksBayar;
    }

    public void setSksBayar(String sksBayar) {
        this.sksBayar = sksBayar;
    }

    public String getAngka() {
        return angka;
    }

    public void setAngka(String angka) {
        this.angka = angka;
    }

    public String getBobot() {
        return bobot;
    }

    public void setBobot(String bobot) {
        this.bobot = bobot;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSks() {
        return sks;
    }

    public void setSks(String sks) {
        this.sks = sks;
    }
}
