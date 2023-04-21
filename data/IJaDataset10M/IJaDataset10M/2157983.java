package eDoktor;

import eDoktor.doktor.Doktor;
import eDoktor.hasta.Hasta;
import eDoktor.hasta.Vaka;
import javax.faces.event.ActionEvent;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Onuralp
 */
public class DoktorDenetciTest {

    public DoktorDenetciTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * getDoktor metodunun testi.
     */
    @Test
    public void testGetDoktor() {
        System.out.println("getDoktor");
        DoktorDenetci ornek = new DoktorDenetci();
        Doktor beklenen = new Doktor();
        Doktor sonuc = ornek.getDoktor();
        assertEquals(beklenen.getNo(), sonuc.getNo());
    }

    /**
     * setDoktor metodunun testi.
     */
    @Test
    public void testSetDoktor() {
        System.out.println("setDoktor");
        Doktor doktor = new Doktor();
        DoktorDenetci ornek = new DoktorDenetci();
        ornek.setDoktor(doktor);
        Doktor beklenen = new Doktor();
        assertEquals(beklenen.getNo(), ornek.getDoktor().getNo());
    }

    /**
     * getGirisMesaji metodunun testi.
     */
    @Test
    public void testGetGirisMesaji() {
        System.out.println("getGirisMesaji");
        DoktorDenetci ornek = new DoktorDenetci();
        String beklenen = "";
        String sonuc = ornek.getGirisMesaji();
        assertEquals(beklenen, sonuc);
    }

    /**
     * setGirisMesaji metodunun testi.
     */
    @Test
    public void testSetGirisMesaji() {
        System.out.println("setGirisMesaji");
        String girisMesaji = "";
        DoktorDenetci ornek = new DoktorDenetci();
        ornek.setGirisMesaji(girisMesaji);
        String beklenen = "";
        String sonuc = ornek.getGirisMesaji();
        assertEquals(beklenen, sonuc);
    }

    /**
     * getKullaniciAdi metodunun testi.
     */
    @Test
    public void testGetKullaniciAdi() {
        System.out.println("getKullaniciAdi");
        DoktorDenetci ornek = new DoktorDenetci();
        String beklenen = "";
        String sonuc = ornek.getKullaniciAdi();
        assertEquals(beklenen, sonuc);
    }

    /**
     * setKullaniciAdi metodunun testi.
     */
    @Test
    public void testSetKullaniciAdi() {
        System.out.println("setKullaniciAdi");
        String kullaniciAdi = "Onuralp";
        DoktorDenetci ornek = new DoktorDenetci();
        ornek.setKullaniciAdi(kullaniciAdi);
        String beklenen = "Onuralp";
        String sonuc = ornek.getKullaniciAdi();
        assertEquals(beklenen, sonuc);
    }

    /**
     * getSifre metodunun testi.
     */
    @Test
    public void testGetSifre() {
        System.out.println("getSifre");
        DoktorDenetci ornek = new DoktorDenetci();
        String beklenen = "";
        String sonuc = ornek.getSifre();
        assertEquals(beklenen, sonuc);
    }

    /**
     * setSifre metodunun testi.
     */
    @Test
    public void testSetSifre() {
        System.out.println("setSifre");
        String sifre = "14789632";
        DoktorDenetci ornek = new DoktorDenetci();
        ornek.setSifre(sifre);
        String beklenen = "14789632";
        String sonuc = ornek.getSifre();
        assertEquals(beklenen, sonuc);
    }

    /**
     * getHasta metodunun testi.
     */
    @Test
    public void testGetHasta() {
        System.out.println("getHasta");
        DoktorDenetci ornek = new DoktorDenetci();
        int beklenen = 0;
        assertEquals(beklenen, ornek.getHasta().getNo());
    }

    /**
     * setHasta metodunun testi.
     */
    @Test
    public void testSetHasta() {
        System.out.println("setHasta");
        DoktorDenetci ornek = new DoktorDenetci();
        ornek.setHasta(new Hasta());
        int beklenen = 0;
        assertEquals(beklenen, ornek.getHasta().getNo());
    }

    /**
     * getVaka metodunun testi.
     */
    @Test
    public void testGetVaka() {
        System.out.println("getVaka");
        DoktorDenetci ornek = new DoktorDenetci();
        Vaka beklenen = new Vaka();
        Vaka sonuc = ornek.getVaka();
        assertEquals(beklenen.getNo(), sonuc.getNo());
    }

    /**
     * setVaka metodunun testi.
     */
    @Test
    public void testSetVaka() {
        System.out.println("setVaka");
        Vaka vaka = new Vaka();
        DoktorDenetci ornek = new DoktorDenetci();
        ornek.setVaka(vaka);
        Vaka beklenen = new Vaka();
        Vaka sonuc = ornek.getVaka();
        assertEquals(beklenen.getNo(), sonuc.getNo());
    }
}
