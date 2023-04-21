package visad.bom;

import visad.*;
import visad.java3d.*;
import java.awt.event.*;
import javax.swing.*;
import java.rmi.RemoteException;

public class Swells extends Exception {

    private static final int NDIRS = 721;

    private static final int NHEIGHTS = 51;

    private static final float SIZE = 0.05f;

    private static WindPolarCoordinateSystem wcs = null;

    private static float[][] arrow_zero = { { 0.0f, 0.0f, 7.0f, -7.0f }, { 0.9f * SIZE, 1.9f * SIZE, 1.6f * SIZE, 1.6f * SIZE } };

    /** set up ScalarMaps from swellDir and swellHeight to Display.Shape
      in display; swellDir default Unit must be degree and swellHeight
      default Unit must be meter */
    public static void setupSwellDisplay(RealType swellDir, RealType swellHeight, DisplayImpl display) throws VisADException, RemoteException {
        if (wcs == null) wcs = new WindPolarCoordinateSystem();
        if (!CommonUnit.degree.equals(swellDir.getDefaultUnit())) {
            throw new UnitException("swellDir Unit must be degree");
        }
        if (!CommonUnit.meter.equals(swellHeight.getDefaultUnit())) {
            throw new UnitException("swellHeight Unit must be meter");
        }
        Linear1DSet dir_set = new Linear1DSet(swellDir, -360.0, 360.0, NDIRS);
        float[][] dirs = dir_set.getSamples();
        VisADGeometryArray[] dir_shapes = new VisADGeometryArray[NDIRS];
        for (int i = 0; i < NDIRS; i++) {
            dir_shapes[i] = new VisADLineArray();
            dir_shapes[i].vertexCount = 6;
            float[][] arrow = new float[2][4];
            System.arraycopy(arrow_zero[0], 0, arrow[0], 0, 4);
            System.arraycopy(arrow_zero[1], 0, arrow[1], 0, 4);
            for (int j = 0; j < arrow[0].length; j++) {
                arrow[0][j] += (dirs[0][i] + 180.0f);
            }
            arrow = wcs.toReference(arrow);
            dir_shapes[i].coordinates = new float[] { arrow[0][1], arrow[1][1], 0.0f, arrow[0][0], arrow[1][0], 0.0f, arrow[0][1], arrow[1][1], 0.0f, arrow[0][2], arrow[1][2], 0.0f, arrow[0][1], arrow[1][1], 0.0f, arrow[0][3], arrow[1][3], 0.0f };
        }
        Integer1DSet height_set = new Integer1DSet(swellHeight, NHEIGHTS);
        double[] start = { 0.0, -0.5 * SIZE, 0.0 };
        double[] base = { SIZE, 0.0, 0.0 };
        double[] up = { 0.0, SIZE, 0.0 };
        VisADGeometryArray[] height_shapes = new VisADGeometryArray[NHEIGHTS];
        for (int i = 0; i < NHEIGHTS; i++) {
            height_shapes[i] = PlotText.render_label(Integer.toString(i), start, base, up, true);
        }
        ScalarMap dir_map = new ScalarMap(swellDir, Display.Shape);
        display.addMap(dir_map);
        ShapeControl dir_control = (ShapeControl) dir_map.getControl();
        dir_control.setShapeSet(dir_set);
        dir_control.setShapes(dir_shapes);
        ScalarMap height_map = new ScalarMap(swellHeight, Display.Shape);
        display.addMap(height_map);
        ShapeControl height_control = (ShapeControl) height_map.getControl();
        height_control.setShapeSet(height_set);
        height_control.setShapes(height_shapes);
        return;
    }

    static final int N = 5;

    public static void main(String args[]) throws VisADException, RemoteException {
        double mid_lat = -30.0;
        RealType lat = RealType.Latitude;
        RealType lon = RealType.Longitude;
        RealType red = RealType.getRealType("red");
        RealType green = RealType.getRealType("green");
        RealType index = RealType.getRealType("index");
        RealType swell_dir = RealType.getRealType("swell_dir", CommonUnit.degree);
        RealType swell_height = RealType.getRealType("swell_speed", CommonUnit.meter);
        RealTupleType range = new RealTupleType(new RealType[] { lon, lat, swell_dir, swell_height, red, green });
        FunctionType swell_field = new FunctionType(index, range);
        DisplayImpl display = new DisplayImplJ3D("display1");
        display.addMap(new ScalarMap(lon, Display.XAxis));
        display.addMap(new ScalarMap(lat, Display.YAxis));
        display.addMap(new ScalarMap(red, Display.Red));
        display.addMap(new ScalarMap(green, Display.Green));
        display.addMap(new ConstantMap(1.0, Display.Blue));
        setupSwellDisplay(swell_dir, swell_height, display);
        Integer1DSet set = new Integer1DSet(N * N);
        double[][] values = new double[6][N * N];
        int m = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double u = 2.0 * i / (N - 1.0) - 1.0;
                double v = 2.0 * j / (N - 1.0) - 1.0;
                values[0][m] = 10.0 * u;
                values[1][m] = 10.0 * v + mid_lat;
                double sx = 10.0 * u;
                double sy = 10.0 * v;
                values[2][m] = Data.RADIANS_TO_DEGREES * Math.atan2(sx, sy);
                values[3][m] = 1.0 + Math.sqrt(sx * sx + sy * sy);
                values[4][m] = u;
                values[5][m] = v;
                m++;
            }
        }
        FlatField field = new FlatField(swell_field, set);
        field.setSamples(values);
        DataReferenceImpl ref = new DataReferenceImpl("ref");
        ref.setData(field);
        display.addReference(ref);
        JFrame frame = new JFrame("test BarbRendererJ3D");
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentY(JPanel.TOP_ALIGNMENT);
        panel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        frame.getContentPane().add(panel);
        panel.add(display.getComponent());
        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
