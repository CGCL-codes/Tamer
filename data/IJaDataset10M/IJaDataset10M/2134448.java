package entities;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sumurmunding
 */
public class DaftarMasjidTest {

    public DaftarMasjidTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of check method, of class DaftarMasjid.
     */
    @Test
    public void testCheck() {
        System.out.println("check");
        String email = "";
        String password = "";
        DaftarMasjid instance = new DaftarMasjid();
        boolean expResult = false;
        boolean result = instance.check(email, password);
        try {
            assertEquals(expResult, result);
            System.out.println("Tes Check Sukses");
        } catch (Exception e) {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of checkId method, of class DaftarMasjid.
     */
    @Test
    public void testCheckId() {
        System.out.println("checkId");
        Long id = null;
        DaftarMasjid instance = new DaftarMasjid();
        boolean expResult = false;
        boolean result = instance.checkId(id);
        try {
            assertEquals(expResult, result);
            System.out.println("Tes CheckId Sukses");
        } catch (Exception e) {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of checkEmail method, of class DaftarMasjid.
     */
    @Test
    public void testCheckEmail() {
        System.out.println("checkEmail");
        String email = "";
        DaftarMasjid instance = new DaftarMasjid();
        boolean expResult = false;
        boolean result = instance.checkEmail(email);
        try {
            assertEquals(expResult, result);
            System.out.println("Tes CheckEmail Sukses");
        } catch (Exception e) {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of getMasjid method, of class DaftarMasjid.
     */
    @Test
    public void testGetMasjid() {
        System.out.println("getMasjid");
        String email = "unluckiers@gmail.com";
        String password = "unluckiers";
        DaftarMasjid instance = new DaftarMasjid();
        Masjid result = instance.getMasjid(email, password);
        String resultNmMasjid = result.getNmMasjid();
        String expResult = "Masjid Al Akbar";
        try {
            assertEquals(expResult, resultNmMasjid);
            System.out.println("Tes getMasjid Sukses");
        } catch (Exception e) {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of findMasjid method, of class DaftarMasjid.
     */
    @Test
    public void testFindMasjid() {
        System.out.println("findMasjid");
        Long id = Long.parseLong("251");
        DaftarMasjid instance = new DaftarMasjid();
        String expResult = "Masjid Al Akbar";
        Masjid result = instance.findMasjid(id);
        String hasil = result.getNmMasjid();
        try {
            assertEquals(expResult, hasil);
            System.out.println("Tes findMasjid Sukses");
        } catch (Exception e) {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of editMasjid method, of class DaftarMasjid.
     */
    @Test
    public void testEditMasjid() {
        System.out.println("editMasjid");
        Masjid masjid;
        DaftarMasjid instance = new DaftarMasjid();
        List<Masjid> list = instance.getMasjids();
        masjid = list.get(1);
        masjid.setAlmtMasjid("Jalan Alun-Alun");
        masjid.setKotaMasjid("Kabupaten Pemalang");
        try {
            instance.editMasjid(masjid);
        } catch (Exception e) {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of addMasjid method, of class DaftarMasjid.
     */
    @Test
    public void testAddMasjid() {
        System.out.println("addMasjid");
        Masjid masjid = new Masjid();
        masjid.setNmMasjid("Masjid Agung");
        masjid.setEmail("a@mail.com");
        masjid.setAlmtMasjid("Jalan Surabaya");
        masjid.setKotaMasjid("Surabaya");
        masjid.setPassword("password");
        masjid.setTelpMasjid("08176549288");
        DaftarMasjid instance = new DaftarMasjid();
        int hasil1 = instance.getMasjids().size();
        instance.addMasjid(masjid);
        int hasil2 = instance.getMasjids().size();
        try {
            assertEquals(hasil1 + 1, hasil2);
            System.out.println("Tes <List> getMasjid Sukses");
        } catch (Exception e) {
            fail("The test case is a prototype.");
        }
    }

    /**
     * Test of deleteMasjid method, of class DaftarMasjid.
     */
    @Test
    public void testDeleteMasjid() throws Exception {
        System.out.println("deleteMasjid");
        Long id = Long.parseLong("4270");
        DaftarMasjid instance = new DaftarMasjid();
        int hasil1 = instance.getMasjids().size();
        instance.deleteMasjid(id);
        int hasil2 = instance.getMasjids().size();
        try {
            assertEquals(hasil1 - 1, hasil2);
            System.out.println("Tes deleteMasjid Sukses");
        } catch (Exception e) {
            fail("The test case is a prototype.");
        }
    }
}
