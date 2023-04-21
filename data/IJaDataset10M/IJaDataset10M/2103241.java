package org.gvsig.georeferencing.process;

import java.awt.geom.Point2D;
import junit.framework.TestCase;
import org.gvsig.georeferencing.process.geotransform.GeoTransformDataResult;
import org.gvsig.georeferencing.process.geotransform.GeoTransformProcess;
import org.gvsig.raster.datastruct.GeoPoint;
import org.gvsig.raster.datastruct.GeoPointList;
import Jama.Matrix;

/**Test que prueba la el proceso de geotransfornmcion dados una serie de puntos de
 * control.  Compara los coeficientes obtenidos para el polinomio de transformacion  
 * (de coordenadas mapa a coordenadas pixel para las Y), con los obtenidos 
 * manualmente, resolviendo por el m�todo de Cramer.
 * 
 * 
 * @author aMu�oz (alejandro.munoz@uclm.es)
 * */
public class TGeoTransformProcessMapToPixelYTest extends TestCase {

    private GeoPointList gpl = new GeoPointList();

    private double geoPoints[][] = { { 1369.000000, 2985.750000 }, { 1673.500000, 2803.250000 }, { 2092.500000, 2933.250000 } };

    private double imagePoints[][] = { { 577.500000, 2427.500000 }, { 803.000000, 2235.500000 }, { 1165.500000, 2285.250000 } };

    public void start() {
        this.testStack();
    }

    public void testStack() {
        System.err.println("TGeoTransformProcessMapToPixelYTest running...");
        for (int i = 0; i < geoPoints.length; i++) {
            Point2D pW = new Point2D.Double(geoPoints[i][0], geoPoints[i][1]);
            Point2D pP = new Point2D.Double(imagePoints[i][0], imagePoints[i][1]);
            gpl.add(new GeoPoint(pP, pW));
        }
        GeoTransformProcess proceso = new GeoTransformProcess();
        proceso.addParam("gpcs", gpl);
        proceso.addParam("orden", new Integer(1));
        proceso.init();
        proceso.process();
        GeoTransformDataResult resultado = (GeoTransformDataResult) proceso.getResult();
        double M[][] = { { 3, 5135, 8722.25 }, { 5135, 9053319.5, 14916556.225 }, { 8722.25, 14916556.225, 25376869.1875 } };
        Matrix m_M = new Matrix(M);
        double det = m_M.det();
        double A0[][] = { { 6948.25, 5135, 8722.25 }, { 11846242.375, 9053319.5, 14916556.225 }, { 20217783.0625, 14916556.225, 25376869.1875 } };
        Matrix m_A0 = new Matrix(A0);
        double coef0 = m_A0.det() / det;
        double A1[][] = { { 3, 6948.25, 8722.25 }, { 5135, 11846242.375, 14916556.225 }, { 8722.25, 20217783.0625, 25376869.1875 } };
        Matrix m_A1 = new Matrix(A1);
        double coef1 = m_A1.det() / det;
        double A2[][] = { { 3, 5135, 6948.25 }, { 5135, 9053319.5, 11846242.375 }, { 8722.25, 14916556.225, 20217783.0625 } };
        Matrix m_A2 = new Matrix(A2);
        double coef2 = m_A2.det() / det;
        assertEquals(coef0, resultado.getMapToPixelCoefY()[0], 0.1);
        assertEquals(coef1, resultado.getMapToPixelCoefY()[1], 0.1);
        assertEquals(coef2, resultado.getMapToPixelCoefY()[2], 0.1);
    }
}
