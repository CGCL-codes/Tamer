package org.gvsig.raster.buffer;

import junit.framework.TestCase;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.InvalidSetViewException;
import org.gvsig.raster.dataset.NotSupportedExtensionException;
import org.gvsig.raster.dataset.RasterDataset;
import org.gvsig.raster.dataset.io.RasterDriverException;

/**
 * Este test prueba el acceso a datos a traves de un DataSource sin resampleo
 * para un raster leido con Gdal con coordenadas pixel. 
 * 
 * Lee el raster completo y comprueba que los datos leidos sean correctos 
 * comparando los valores de las cuatro esquinas y algunos valores dentro de la imagen.
 * Despu�s hace selecciona un �rea dentro de la imagen de 2x2 y compara que los valores
 * leidos sean correctos.
 * 
 * @author Nacho Brodin (nachobrodin@gmail.com)
 *
 */
public class TDSDoubleAdjustToExtentGdal extends TestCase {

    private String baseDir = "./test-images/";

    private String path = baseDir + "miniRaster25x24.tif";

    private RasterDataset f = null;

    private BufferFactory ds = null;

    public void start() {
        this.setUp();
        this.testStack();
    }

    public void setUp() {
        System.err.println("TDSDoubleAdjustToExtentGdal running...");
    }

    static {
        RasterLibrary.wakeUp();
    }

    public void testStack() {
        int[] drawableBands = { 0, 1, 2 };
        try {
            f = RasterDataset.open(null, path);
        } catch (NotSupportedExtensionException e) {
            e.printStackTrace();
            return;
        } catch (RasterDriverException e) {
            e.printStackTrace();
            return;
        }
        ds = new BufferFactory(f);
        ds.setDrawableBands(drawableBands);
        try {
            ds.setAreaOfInterest(645817.0, 4923851.0, 40, 40);
            dataTest1();
        } catch (RasterDriverException e) {
            e.printStackTrace();
        } catch (InvalidSetViewException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            ds.setAreaOfInterest(645829.8, 4923840.4, 2, 2);
            dataTest2();
        } catch (RasterDriverException e) {
            e.printStackTrace();
        } catch (InvalidSetViewException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void dataTest2() throws InterruptedException {
        IBuffer raster = ds.getRasterBuf();
        assertEquals((int) (raster.getElemByte(0, 0, 0) & 0xff), 64);
        assertEquals((int) (raster.getElemByte(0, 0, 1) & 0xff), 81);
        assertEquals((int) (raster.getElemByte(0, 0, 2) & 0xff), 39);
        assertEquals((int) (raster.getElemByte(0, 1, 0) & 0xff), 64);
        assertEquals((int) (raster.getElemByte(0, 1, 1) & 0xff), 81);
        assertEquals((int) (raster.getElemByte(0, 1, 2) & 0xff), 39);
        assertEquals((int) (raster.getElemByte(1, 0, 0) & 0xff), 64);
        assertEquals((int) (raster.getElemByte(1, 0, 1) & 0xff), 81);
        assertEquals((int) (raster.getElemByte(1, 0, 2) & 0xff), 39);
        assertEquals((int) (raster.getElemByte(1, 1, 0) & 0xff), 64);
        assertEquals((int) (raster.getElemByte(1, 1, 1) & 0xff), 81);
        assertEquals((int) (raster.getElemByte(1, 1, 2) & 0xff), 39);
    }

    private void dataTest1() throws InterruptedException {
        IBuffer raster = ds.getRasterBuf();
        assertEquals((int) (raster.getElemByte(0, 0, 0) & 0xff), 14);
        assertEquals((int) (raster.getElemByte(0, 0, 1) & 0xff), 14);
        assertEquals((int) (raster.getElemByte(0, 0, 2) & 0xff), 0);
        assertEquals((int) (raster.getElemByte(0, 24, 0) & 0xff), 68);
        assertEquals((int) (raster.getElemByte(0, 24, 1) & 0xff), 90);
        assertEquals((int) (raster.getElemByte(0, 24, 2) & 0xff), 52);
        assertEquals((int) (raster.getElemByte(23, 0, 0) & 0xff), 129);
        assertEquals((int) (raster.getElemByte(23, 0, 1) & 0xff), 122);
        assertEquals((int) (raster.getElemByte(23, 0, 2) & 0xff), 106);
        assertEquals((int) (raster.getElemByte(23, 24, 0) & 0xff), 145);
        assertEquals((int) (raster.getElemByte(23, 24, 1) & 0xff), 140);
        assertEquals((int) (raster.getElemByte(23, 24, 2) & 0xff), 134);
        assertEquals((int) (raster.getElemByte(6, 6, 0) & 0xff), 21);
        assertEquals((int) (raster.getElemByte(6, 6, 1) & 0xff), 37);
        assertEquals((int) (raster.getElemByte(6, 6, 2) & 0xff), 10);
        assertEquals((int) (raster.getElemByte(6, 23, 0) & 0xff), 91);
        assertEquals((int) (raster.getElemByte(6, 23, 1) & 0xff), 105);
        assertEquals((int) (raster.getElemByte(6, 23, 2) & 0xff), 92);
        assertEquals((int) (raster.getElemByte(23, 6, 0) & 0xff), 153);
        assertEquals((int) (raster.getElemByte(23, 6, 1) & 0xff), 133);
        assertEquals((int) (raster.getElemByte(23, 6, 2) & 0xff), 122);
        assertEquals((int) (raster.getElemByte(9, 14, 0) & 0xff), 63);
        assertEquals((int) (raster.getElemByte(9, 14, 1) & 0xff), 69);
        assertEquals((int) (raster.getElemByte(9, 14, 2) & 0xff), 55);
        assertEquals((int) (raster.getElemByte(6, 13, 0) & 0xff), 70);
        assertEquals((int) (raster.getElemByte(6, 13, 1) & 0xff), 78);
        assertEquals((int) (raster.getElemByte(6, 13, 2) & 0xff), 55);
    }
}
