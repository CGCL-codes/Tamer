package org.gvsig.rastertools.clipping;

import java.awt.geom.Point2D;
import org.gvsig.raster.buffer.BufferInterpolation;
import org.gvsig.raster.dataset.IBuffer;

/**
 * Test de pruebas del proceso de recorte
 * https://gvsig.org/web/docdev/docs/desarrollo/plugins/raster-tools/funcionalidades/recorte-de-raster/caracteristicas?portal_status_message=Changes%20saved.
 * 
 * 07/05/2008
 * @author Nacho Brodin nachobrodin@gmail.com
 */
public class TifFloatClippingProcessTest extends ClippingBaseTest {

    public void setUp() {
        resetTime();
        System.err.println("***********************************************");
        System.err.println("*** TifFloat ClippingProcessTest running... ***");
        System.err.println("***********************************************");
    }

    public void testStack() {
        openLayer(floatImg);
        clipping(new int[] { 0 }, false, BufferInterpolation.INTERPOLATION_NearestNeighbour, getColorInterpretation(1));
        dataset = open(out + ".tif");
        compareDatasets(lyr.getDataSource(), dataset, new Point2D.Double(coords[0], coords[3]), 1, new int[] { 0 }, IBuffer.TYPE_FLOAT);
        dataset.close();
        clipping(new int[] { 0 }, false, BufferInterpolation.INTERPOLATION_NearestNeighbour, getColorInterpretation(1), (coords[2] - coords[0] + 1) * 2, (coords[1] - coords[3] + 1) * 2);
        dataset = open(out + ".tif");
        compareDatasets(lyr.getDataSource(), dataset, new Point2D.Double(coords[0], coords[3]), 2, new int[] { 0 }, IBuffer.TYPE_FLOAT);
        dataset.close();
        if (lyr != null) lyr.removeLayerListener(null);
        System.err.println("***********************************************");
        System.err.println("*** Time:" + getTime());
        System.err.println("*** TifFloat ClippingProcessTest ending...  ***");
        System.err.println("***********************************************");
    }
}
