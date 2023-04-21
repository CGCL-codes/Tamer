package eDoktor.kullaniciBilgileri;

/**
 * Adres bilgileri sınıfı
 * @author Onuralp
 */
public class AdresBilgileri {

    private int no;

    private String adres;

    private String il;

    private String ilce;

    private String postaKodu;

    private String gorevYeri;

    /**
     * AdresBilgileri sınıfının yapılandırıcısı
     */
    public AdresBilgileri() {
        no = 0;
        adres = "";
        il = "";
        ilce = "";
        postaKodu = "";
        gorevYeri = "";
    }

    /**
     * AdresBilgileri sınıfının degiskenlerine erişmeyi sağlayan fonksiyonlar 
     */
    public String getAdres() {
        return adres;
    }

    public void setAdres(String adres) {
        this.adres = adres;
    }

    public String getGorevYeri() {
        return gorevYeri;
    }

    public void setGorevYeri(String gorevYeri) {
        this.gorevYeri = gorevYeri;
    }

    public String getIl() {
        return il;
    }

    public void setIl(String il) {
        this.il = il;
    }

    public String getIlce() {
        return ilce;
    }

    public void setIlce(String ilce) {
        this.ilce = ilce;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getPostaKodu() {
        return postaKodu;
    }

    public void setPostaKodu(String postaKodu) {
        this.postaKodu = postaKodu;
    }
}
