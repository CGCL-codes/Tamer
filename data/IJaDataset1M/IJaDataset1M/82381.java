package org.gvsig.rastertools.clipping;

import java.awt.geom.Point2D;
import org.gvsig.raster.buffer.BufferInterpolation;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.dataset.MultiRasterDataset;

/**
 * Test de pruebas del proceso de recorte
 * https://gvsig.org/web/docdev/docs/desarrollo/plugins/raster-tools/funcionalidades/recorte-de-raster/caracteristicas?portal_status_message=Changes%20saved.
 * 
 * 07/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class EcwClippingProcessTest extends ClippingBaseTest {

    public void setUp() {
        resetTime();
        System.err.println("******************************************");
        System.err.println("*** Ecw ClippingProcessTest running... ***");
        System.err.println("******************************************");
    }

    public void testStack() {
        openLayer(ecw);
        clipping(new int[] { 0, 1, 2 }, false, BufferInterpolation.INTERPOLATION_NearestNeighbour, getColorInterpretation(3));
        dataset = open(out + ".tif");
        compareDatasets(lyr.getDataSource(), dataset, new Point2D.Double(coords[0], coords[3]), 1, new int[] { 0, 1, 2 }, IBuffer.TYPE_BYTE);
        dataset.close();
        clipping(new int[] { 0, 1, 2 }, true, BufferInterpolation.INTERPOLATION_NearestNeighbour, getColorInterpretation(1));
        MultiRasterDataset d0 = open(out + "_B0.tif");
        compareDatasets(0, lyr.getDataSource(), d0, new Point2D.Double(coords[0], coords[3]), 1, new int[] { 0, 1, 2 }, IBuffer.TYPE_BYTE);
        d0.close();
        MultiRasterDataset d1 = open(out + "_B1.tif");
        compareDatasets(1, lyr.getDataSource(), d1, new Point2D.Double(coords[0], coords[3]), 1, new int[] { 0, 1, 2 }, IBuffer.TYPE_BYTE);
        d1.close();
        MultiRasterDataset d2 = open(out + "_B2.tif");
        compareDatasets(2, lyr.getDataSource(), d2, new Point2D.Double(coords[0], coords[3]), 1, new int[] { 0, 1, 2 }, IBuffer.TYPE_BYTE);
        d2.close();
        if (lyr != null) lyr.removeLayerListener(null);
        System.err.println("******************************************");
        System.err.println("*** Time:" + getTime());
        System.err.println("*** Ecw ClippingProcessTest ending...  ***");
        System.err.println("******************************************");
    }
}
