package eDoktor.hastalik;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Onuralp
 */
public class BelirtiTest {

    public BelirtiTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * getNo metodunun testi.
     */
    @Test
    public void testGetNo() {
        System.out.println("getNo");
        Belirti ornek = new Belirti();
        int beklenen = 0;
        int sonuc = ornek.getNo();
        assertEquals(beklenen, sonuc);
    }

    /**
     * setNo metodunun testi.
     */
    @Test
    public void testSetNo() {
        System.out.println("setNo");
        int no = 10;
        Belirti ornek = new Belirti();
        ornek.setNo(no);
        int beklenen = 10;
        int sonuc = ornek.getNo();
        assertEquals(beklenen, sonuc);
    }

    /**
     * getTanim metodunun testi.
     */
    @Test
    public void testGetTanim() {
        System.out.println("getTanim");
        Belirti ornek = new Belirti();
        String beklenen = "";
        String sonuc = ornek.getTanim();
        assertEquals(beklenen, sonuc);
    }

    /**
     * setTanim metodunun testi.
     */
    @Test
    public void testSetTanim() {
        System.out.println("setTanim");
        String tanim = "Baş ağrısı";
        Belirti ornek = new Belirti();
        ornek.setTanim(tanim);
        String beklenen = "Baş ağrısı";
        String sonuc = ornek.getTanim();
        assertEquals(beklenen, sonuc);
    }
}
