package es.gva.cit.jgdal;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;

public class TestWarpDataset extends TestCase {

    private GdalWarp warp = null;

    private String baseDir = "./test-images/";

    private String src = baseDir + "testGdalWarp.tif";

    private String dst = baseDir + "warpedImage.tif";

    private String frm = "GTiff";

    private String t_srs = "EPSG:23030";

    public void start() {
        setUp();
        testStack();
    }

    public void setUp() {
        warp = new GdalWarp();
        File f = new File(src);
        Gdal dataset = new Gdal();
        try {
            assertTrue("El fichero no existe", f.exists());
            assertTrue("El fichero no se puede leer", f.canRead());
            dataset.open(src, Gdal.GA_ReadOnly);
            GdalRasterBand band = dataset.getRasterBand(1);
            band.readRaster(0, 0, 10, 10, 10, 10, Gdal.GDT_Byte);
        } catch (GdalException e) {
            new AssertionError("Fallo en gdal al acceder al fichero fuente");
        } catch (IOException e) {
            new AssertionError("Fallo en gdal al acceder al fichero fuente");
            e.printStackTrace();
        }
    }

    public void testStack() {
        assertNotNull(t_srs);
        warp.warp(t_srs, src, dst, frm);
        System.err.println("Proceso completado al " + warp.getPercent() + " %");
        File f = new File(dst);
        assertTrue("El fichero destino no existe", f.exists());
        assertTrue("El fichero destino no se puede leer", f.canRead());
    }
}
