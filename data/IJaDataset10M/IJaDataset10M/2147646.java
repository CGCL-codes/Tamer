package entity;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Fortunela Park
 */
public class DaftarBarangTest extends TestCase {

    private User user1;

    private User user2;

    private Barang barang1;

    private Barang barang2;

    DaftarBarang dg = new DaftarBarang();

    public DaftarBarangTest(String testName) {
        super(testName);
    }

    @BeforeClass
    @Override
    public void setUp() throws Exception {
        super.setUp();
        barang1 = new Barang();
        long id = 144;
        barang1.setId(id);
        barang1.setNama("Blouse");
        barang1.setJumlah(2);
        barang1.setHarga(75000);
        barang1.setUkuran("M");
        barang1.setWarna("Biru");
        barang2 = new Barang();
        barang2.setId(id);
        barang2.setNama("Celana Panjang");
        barang2.setJumlah(1);
        barang2.setHarga(120000);
        barang2.setUkuran("L");
        barang2.setWarna("Merah");
    }

    @Test
    public void testAddBarang() {
        System.out.println("AddBarang");
        dg.addBarang(barang1);
        dg.addBarang(barang2);
    }

    @Test
    public void testGetDaftarBarang_0args() {
        System.out.println("getDaftarBarang");
        assertEquals(2, dg.getDaftarBarang().size());
    }

    @Test
    public void testEditBarang() {
        System.out.println("editBarang");
        barang1 = dg.getBarang(barang1.getId());
        barang1.setNama(barang2.getNama());
        dg.editBarang(barang1);
        assertEquals(barang2.getNama(), dg.getBarang(barang1.getId()).getNama());
    }

    /**
     * bagian ini dilakukan pengetesan pada method DeleteBarang() 
     */
    @Test
    public void testDeleteBarang() throws Exception {
        System.out.println("deleteBarang");
        dg.deleteBarang(barang2.getId());
    }
}
