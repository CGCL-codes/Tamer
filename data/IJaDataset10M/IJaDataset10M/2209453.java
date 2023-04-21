package openfield.persistence.business;

import java.io.File;
import java.util.Date;
import java.util.List;
import openfield.persistence.database.DBLookup;
import openfield.persistence.dbmgr.DBManager;
import openfield.persistence.entities.TipoMagnitud;
import openfield.persistence.entities.almacen.Articulo;
import openfield.persistence.entities.almacen.Historial.TipoMovimiento;
import openfield.persistence.entities.almacen.TipoArticulo;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shader
 */
public class AlmacenTest {

    private Almacen al;

    private BusinessFactory bf;

    public AlmacenTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        if (!DBManager.getDBManagerInstance().exists("testdb")) {
            DBManager.getDBManagerInstance().createDB("testdb");
        }
        DBManager.getDBManagerInstance().openDBLookup("testdb");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        DBLookup.clearOpenfieldDB();
    }

    @Before
    public void setUp() {
        bf = new BusinessFactory();
        al = bf.getAlmacen();
    }

    @After
    public void tearDown() {
        bf.close();
    }

    @Test
    public void testGetArticulos() {
        assertNotNull("Lista de artículos nula!", al.getArticulos());
    }

    @Test
    public void testGetTiposArticulo() {
        assertNotNull("Lista de tipos de artículo nula!", al.getTiposArticulo());
    }

    @Test
    public void testGetHistoriales() {
        assertNotNull("Lista de historial nula!", al.getHistoriales());
    }

    /**
     * Test of getArticulos method, of class Almacen.
     */
    @Test
    public void testCreaBorraUpdateArticulo() {
        System.out.println("Articulos");
        List result = al.getArticulos();
        assertNotNull("Lista de artículos nula", result);
        int osart = result.size();
        result = al.getTiposArticulo();
        assertNotNull("Lista de tipos de artículo nula", result);
        int ostart = result.size();
        Date now = new Date();
        TipoArticulo tart = new TipoArticulo("testtart" + now);
        TipoMagnitud tmag = new TipoMagnitud("tipo mag" + now);
        Articulo testart = new Articulo("abcd", "testart" + now, tart);
        al.addTipoArticulo(tart);
        bf.getUtiles().addTipoMagnitud(tmag);
        al.addArticulo(testart);
        assertTrue("Tamaño de lista de arts incorrecto tras inserción", al.getArticulos().size() == osart + 1);
        assertTrue("Tamaño de lista de tipos de artículo incorrecto tras inserrción de artículo", al.getTiposArticulo().size() == ostart + 1);
        testart.setNombre("pruebajar");
        al.updateArticulo(testart);
        testart = al.getArticuloById(testart.getId());
        assertEquals(testart.getNombre(), "pruebajar");
        al.removeArticulo(testart);
        assertTrue("Tamaño de lista de arts incorrecto tras eliminación", al.getArticulos().size() == osart);
        assertTrue("Tamaño de lista de tipos de artículo incorrecto tras eliminación de artículo", al.getTiposArticulo().size() == ostart + 1);
    }

    @Test
    public void testUpdateStockCompra() {
        Date now = new Date();
        TipoArticulo tart = new TipoArticulo("testtart2" + now);
        TipoMagnitud tmag = new TipoMagnitud("tipo mag2" + now);
        Articulo testart = new Articulo("abcd", "testart2" + now, tart);
        al.addTipoArticulo(tart);
        bf.getUtiles().addTipoMagnitud(tmag);
        al.addArticulo(testart);
        double cant = 10;
        double precio = 11.3;
        al.updateStock(testart, new Date(), TipoMovimiento.COMPRA, "prueba de regularizacion", cant, precio);
        assertTrue("Precio incorrecto", precio == testart.getPrecioMedioCompra());
        assertTrue("Stock incorrecto", cant == testart.getStock());
    }

    @Test
    public void testRegulariza() {
        Date now = new Date();
        TipoArticulo tart = new TipoArticulo("testtart3" + now);
        TipoMagnitud tmag = new TipoMagnitud("tipo mag3" + now);
        Articulo testart = new Articulo("abcd", "testart3" + now, tart);
        testart.setStock(10);
        testart.setPrecioMedioCompra(10);
        al.addTipoArticulo(tart);
        bf.getUtiles().addTipoMagnitud(tmag);
        al.addArticulo(testart);
        al.regularizaArticulo(testart, new Date(), -10);
        testart = al.getArticuloById(testart.getId());
        assertTrue("Precio incorrecto", 10 == testart.getPrecioMedioCompra());
        assertTrue("Stock incorrecto", 0 == testart.getStock());
    }
}
