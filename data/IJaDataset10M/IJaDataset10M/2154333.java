package jsesh.demos.tests;

import java.io.RandomAccessFile;
import jsesh.graphics.emf.EMFDeviceContext;
import jsesh.graphics.emf.EMFPoint;
import jsesh.graphics.generic.RandomAccessFileAdapter;
import jsesh.graphics.generic.RandomAccessStream;
import jsesh.graphics.wmf.WMFConstants;

/**
 * @author rosmord
 *
 */
public class EMFDeviceContextTests {

    public static void main(String[] args) throws Exception {
        RandomAccessStream out = new RandomAccessFileAdapter(new RandomAccessFile("/tmp/demo.emf", "rw"));
        EMFDeviceContext emf = new EMFDeviceContext(out, 2000, 1000);
        short a = emf.createBrush(0, 0xFF, WMFConstants.BS_SOLID);
        emf.selectObject(a);
        emf.beginPath();
        emf.moveToEx(100, 100);
        EMFPoint[] controls = new EMFPoint[] { new EMFPoint(100, 1000), new EMFPoint(1100, 1000), new EMFPoint(1100, 100) };
        emf.polyBezierTo(null, controls);
        emf.lineTo(100, 100);
        emf.closeFigure();
        emf.moveToEx(300, 300);
        controls = new EMFPoint[] { new EMFPoint(300, 400), new EMFPoint(900, 400), new EMFPoint(900, 300), new EMFPoint(900, 200), new EMFPoint(300, 200), new EMFPoint(300, 300) };
        emf.polyBezierTo(null, controls);
        emf.closeFigure();
        emf.endPath();
        emf.fillPath();
        emf.deleteObject(a);
        emf.closeMetafile();
    }

    public static void drawTriangle() throws Exception {
        RandomAccessStream out = new RandomAccessFileAdapter(new RandomAccessFile("/tmp/demo.emf", "rw"));
        EMFDeviceContext emf = new EMFDeviceContext(out, 400, 400);
        emf.setWindowOrg(0, 0);
        emf.setWindowExt(1000, 1000);
        short a = emf.createBrush(0, 0xFF, WMFConstants.BS_SOLID);
        emf.selectObject(a);
        emf.beginPath();
        emf.moveToEx(0, 0);
        emf.lineTo(1000, 5000);
        EMFPoint[] controls = new EMFPoint[] { new EMFPoint(200, 4000), new EMFPoint(10000, 2000), new EMFPoint(200, 2000) };
        emf.lineTo(2000, 0);
        emf.closeFigure();
        emf.moveToEx(1000, 4000);
        emf.lineTo(500, 2000);
        emf.lineTo(1500, 2000);
        emf.endPath();
        emf.fillPath();
        emf.deleteObject(a);
        emf.closeMetafile();
    }
}
