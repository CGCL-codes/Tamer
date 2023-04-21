package vydavky.client.objects;

import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class TransakciaValueTest {

    public TransakciaValueTest() {
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

    @Test
    public void testContainsClovek() {
        System.out.println("containsClovek");
        TransakciaValue instance = new TransakciaValue();
        instance.getPlatili().add(new KoeficientVydavku(1f, 1L, null));
        instance.getPlatili().add(new KoeficientVydavku(1f, 2L, null));
        instance.getPlatili().add(new KoeficientVydavku(1f, 3L, null));
        instance.getPlatili().add(new KoeficientVydavku(1f, null, 1L));
        instance.getPlatili().add(new KoeficientVydavku(1f, null, 2L));
        instance.getPlatili().add(new KoeficientVydavku(1f, null, 3L));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, 4L, null));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, 5L, null));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, 6L, null));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, null, 4L));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, null, 5L));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, null, 6L));
        assertEquals(false, instance.containsClovek(null));
        assertEquals(false, instance.containsClovek(15L));
        assertEquals(true, instance.containsClovek(1L));
        assertEquals(true, instance.containsClovek(4L));
    }

    @Test
    public void testContainsSkupina() {
        System.out.println("containsSkupina");
        TransakciaValue instance = new TransakciaValue();
        instance.getPlatili().add(new KoeficientVydavku(1f, 1L, null));
        instance.getPlatili().add(new KoeficientVydavku(1f, 2L, null));
        instance.getPlatili().add(new KoeficientVydavku(1f, 3L, null));
        instance.getPlatili().add(new KoeficientVydavku(1f, null, 1L));
        instance.getPlatili().add(new KoeficientVydavku(1f, null, 2L));
        instance.getPlatili().add(new KoeficientVydavku(1f, null, 3L));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, 4L, null));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, 5L, null));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, 6L, null));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, null, 4L));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, null, 5L));
        instance.getSpotrebovali().add(new KoeficientVydavku(1f, null, 6L));
        assertEquals(false, instance.containsSkupina(null));
        assertEquals(false, instance.containsSkupina(15L));
        assertEquals(true, instance.containsSkupina(1L));
        assertEquals(true, instance.containsSkupina(4L));
    }

    public void testGetDatum() {
        System.out.println("getDatum");
        TransakciaValue instance = new TransakciaValue();
        Date expResult = null;
        Date result = instance.getDatum();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testSetDatum() {
        System.out.println("setDatum");
        Date datum = null;
        TransakciaValue instance = new TransakciaValue();
        instance.setDatum(datum);
        fail("The test case is a prototype.");
    }

    public void testGetPopis() {
        System.out.println("getPopis");
        TransakciaValue instance = new TransakciaValue();
        String expResult = "";
        String result = instance.getPopis();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testSetPopis() {
        System.out.println("setPopis");
        String popis = "";
        TransakciaValue instance = new TransakciaValue();
        instance.setPopis(popis);
        fail("The test case is a prototype.");
    }

    public void testGetSuma() {
        System.out.println("getSuma");
        TransakciaValue instance = new TransakciaValue();
        float expResult = 0.0F;
        float result = instance.getSuma();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testSetSuma() {
        System.out.println("setSuma");
        float suma = 0.0F;
        TransakciaValue instance = new TransakciaValue();
        instance.setSuma(suma);
        fail("The test case is a prototype.");
    }

    public void testGetMena() {
        System.out.println("getMena");
        TransakciaValue instance = new TransakciaValue();
        Long expResult = null;
        Long result = instance.getMena();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testSetMena() {
        System.out.println("setMena");
        Long mena = null;
        TransakciaValue instance = new TransakciaValue();
        instance.setMena(mena);
        fail("The test case is a prototype.");
    }

    public void testGetKurz() {
        System.out.println("getKurz");
        TransakciaValue instance = new TransakciaValue();
        float expResult = 0.0F;
        float result = instance.getKurz();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testSetKurz() {
        System.out.println("setKurz");
        float kurz = 0.0F;
        TransakciaValue instance = new TransakciaValue();
        instance.setKurz(kurz);
        fail("The test case is a prototype.");
    }

    public void testGetTypVydavku() {
        System.out.println("getTypVydavku");
        TransakciaValue instance = new TransakciaValue();
        Long expResult = null;
        Long result = instance.getTypVydavku();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testSetTypVydavku() {
        System.out.println("setTypVydavku");
        Long typVydavku = null;
        TransakciaValue instance = new TransakciaValue();
        instance.setTypVydavku(typVydavku);
        fail("The test case is a prototype.");
    }

    public void testGetProjekt() {
        System.out.println("getProjekt");
        TransakciaValue instance = new TransakciaValue();
        Long expResult = null;
        Long result = instance.getProjekt();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testSetProjekt() {
        System.out.println("setProjekt");
        Long projekt = null;
        TransakciaValue instance = new TransakciaValue();
        instance.setProjekt(projekt);
        fail("The test case is a prototype.");
    }

    public void testGetPlatili() {
        System.out.println("getPlatili");
        TransakciaValue instance = new TransakciaValue();
        List<KoeficientVydavku> expResult = null;
        List<KoeficientVydavku> result = instance.getPlatili();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testSetPlatili() {
        System.out.println("setPlatili");
        List<KoeficientVydavku> ludiaPlatili = null;
        TransakciaValue instance = new TransakciaValue();
        instance.setPlatili(ludiaPlatili);
        fail("The test case is a prototype.");
    }

    public void testGetSpotrebovali() {
        System.out.println("getSpotrebovali");
        TransakciaValue instance = new TransakciaValue();
        List<KoeficientVydavku> expResult = null;
        List<KoeficientVydavku> result = instance.getSpotrebovali();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testSetSpotrebovali() {
        System.out.println("setSpotrebovali");
        List<KoeficientVydavku> ludiaSpotrebovali = null;
        TransakciaValue instance = new TransakciaValue();
        instance.setSpotrebovali(ludiaSpotrebovali);
        fail("The test case is a prototype.");
    }

    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        TransakciaValue instance = new TransakciaValue();
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testHashCode() {
        System.out.println("hashCode");
        TransakciaValue instance = new TransakciaValue();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void testToString() {
        System.out.println("toString");
        TransakciaValue instance = new TransakciaValue();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
}
