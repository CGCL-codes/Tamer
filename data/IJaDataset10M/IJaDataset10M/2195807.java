package com.jvito.plot.trivariat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import com.jvito.data.ExampleColoring;
import com.jvito.exception.CompilerException;
import com.jvito.parameter.ParameterTypeColor;
import com.jvito.parameter.ParameterTypeDynamicCategory;
import com.jvito.plot.ColorPanel;
import com.jvito.plot.SimplePlot;
import com.jvito.util.Utils;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Statistics;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;

/**
 * Plots two attributes against the function value.
 * 
 * @author Daniel Hakenjos
 * @version $Id: FunctionValuePlot3D.java,v 1.7 2008/04/19 09:21:46 djhacker Exp $
 */
public class FunctionValuePlot3D extends SimplePlot implements Pickable3DPlot {

    private int number_atts = 0;

    private int number_of_samples = 0;

    private int x_attr = 0, y_attr = 0, c_attr;

    private Attribute x_attribute, y_attribute, z_attribute, colorattribute;

    private double[] min, max;

    private float[] x_samples;

    private float[] y_samples;

    private float[] z_samples;

    private float[] labelvalues;

    private float[] colorsamples;

    private Color[] colortable;

    private Color startcolor, endcolor;

    private JPanel mainpane;

    private JTextArea textarea;

    private JViToCanvas3D canvas3D;

    /**
	 * Init the ScatterPlot3D.
	 */
    public FunctionValuePlot3D() {
        super();
    }

    /**
	 * Init the ScatterPlot3D with the name.
	 */
    public FunctionValuePlot3D(String name) {
        setName(name);
    }

    @Override
    public JPanel getPlotPanel() {
        return mainpane;
    }

    @Override
    public Component getSaveableComponent() {
        canvas3D.setSaveImage(true);
        canvas3D.repaint();
        return null;
    }

    private SimpleUniverse u = null;

    public BranchGroup createSceneGraph() {
        BranchGroup objRoot = new BranchGroup();
        if (getParameterAsBoolean("show title")) {
            Font3D font3D = new Font3D(new Font("SansSerif", Font.PLAIN, 10), new FontExtrusion());
            Text3D text_title = new Text3D(font3D, getParameterAsString("title"), new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_CENTER, Text3D.PATH_RIGHT);
            ColoringAttributes catts = new ColoringAttributes();
            catts.setColor(new Color3f(getParameterAsColor("foreground")));
            PolygonAttributes patts = new PolygonAttributes();
            patts.setCullFace(PolygonAttributes.CULL_NONE);
            patts.setBackFaceNormalFlip(true);
            Appearance app = new Appearance();
            app.setColoringAttributes(catts);
            app.setPolygonAttributes(patts);
            Transform3D trans = new Transform3D();
            trans.set(new Vector3f(0.0f, 0.48f, 0.48f));
            Transform3D skal = new Transform3D();
            skal.setScale(0.01);
            TransformGroup text3D = new TransformGroup(trans);
            objRoot.addChild(text3D);
            TransformGroup stext3D = new TransformGroup(skal);
            text3D.addChild(stext3D);
            Shape3D textShape;
            textShape = new Shape3D();
            textShape.setGeometry(text_title);
            textShape.setAppearance(app);
            textShape.setPickable(false);
            stext3D.addChild(textShape);
        }
        BranchGroup objRoot2 = new BranchGroup();
        objRoot.addChild(objRoot2);
        TransformGroup objDreh = new TransformGroup();
        objDreh.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objDreh.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        objRoot2.addChild(objDreh);
        Shape3D pointShape = createPointShape();
        pointShape.setPickable(false);
        objDreh.addChild(pointShape);
        BoundingSphere sceneBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        Background bg = new Background(new Color3f(getParameterAsColor("background")));
        bg.setApplicationBounds(sceneBounds);
        objDreh.addChild(bg);
        Shape3D cubeShape = createCoordinateSystem();
        objDreh.addChild(cubeShape);
        Shape3D[] planes = createHyperplaneShape();
        for (int i = 0; i < planes.length; i++) {
            objDreh.addChild(planes[i]);
        }
        Shape3D axisShape = createAxisShape();
        objDreh.addChild(axisShape);
        createAxisNames(objDreh);
        createAxisNumbers(objDreh);
        BoundingSphere sphere = new BoundingSphere();
        MouseRotate myrotate = new MouseRotate();
        myrotate.setTransformGroup(objDreh);
        myrotate.setSchedulingBounds(sphere);
        objRoot2.addChild(myrotate);
        MouseZoom myzoom = new MouseZoom();
        myzoom.setTransformGroup(objDreh);
        myzoom.setSchedulingBounds(sphere);
        objRoot2.addChild(myzoom);
        MouseTranslate mytrans = new MouseTranslate();
        mytrans.setTransformGroup(objDreh);
        mytrans.setSchedulingBounds(sphere);
        objRoot2.addChild(mytrans);
        PickBehavior pick = new PickBehavior(this.canvas3D, objRoot, this);
        pick.setSchedulingBounds(new BoundingSphere());
        objRoot.addChild(pick);
        objRoot.compile();
        return objRoot;
    }

    public void showPickedPoint(Point3d point) {
        double x = min[0] + (max[0] - min[0]) * point.x;
        double y = min[1] + (max[1] - min[1]) * point.y;
        double z = min[2] + (max[2] - min[2]) * point.z;
        String text = new String("");
        text += x_attribute.getName() + ":\n" + ((float) x) + "\n\n";
        text += y_attribute.getName() + ":\n" + ((float) y) + "\n\n";
        text += z_attribute.getName() + ":\n" + ((float) z);
        textarea.setText(text);
    }

    public void showPickedPoint() {
        textarea.setText("");
    }

    private void createAxisNumbers(TransformGroup objDreh) {
        double[] min = new double[3];
        double[] max = new double[3];
        Attribute[] atts = new Attribute[] { x_attribute, y_attribute, z_attribute };
        for (int i = 0; i < 3; i++) {
            if (atts[i].isNominal()) {
                min[i] = 0.0d;
                max[i] = atts[i].getMapping().size() - 1.0d;
            } else {
                min[i] = source.getExampleSet().getStatistics(atts[i], Statistics.MINIMUM);
                max[i] = source.getExampleSet().getStatistics(atts[i], Statistics.MAXIMUM);
            }
        }
        String[][] numbers = new String[3][2];
        for (int i = 0; i < 3; i++) {
            numbers[i] = Utils.formatDouble(min[i], max[i]);
        }
        Transform3D[][] trans = new Transform3D[3][2];
        for (int i = 0; i < 3; i++) {
            trans[i][0] = new Transform3D();
            trans[i][1] = new Transform3D();
        }
        trans[0][0].set(new Vector3f(-0.5f, -0.55f, 0.55f));
        trans[0][1].set(new Vector3f(0.5f, -0.55f, 0.55f));
        trans[1][0].set(new Vector3f(-0.55f, -0.5f, 0.55f));
        trans[1][1].set(new Vector3f(-0.55f, 0.5f, 0.55f));
        trans[2][0].set(new Vector3f(-0.55f, -0.55f, 0.5f));
        trans[2][1].set(new Vector3f(-0.55f, -0.55f, -0.5f));
        TransformGroup[][] transgroup = new TransformGroup[3][2];
        for (int i = 0; i < 3; i++) {
            transgroup[i][0] = new TransformGroup(trans[i][0]);
            objDreh.addChild(transgroup[i][0]);
            transgroup[i][1] = new TransformGroup(trans[i][1]);
            objDreh.addChild(transgroup[i][1]);
        }
        Transform3D[][] skal = new Transform3D[3][2];
        for (int i = 0; i < 3; i++) {
            skal[i][0] = new Transform3D();
            skal[i][0].setScale(0.004d);
            skal[i][1] = new Transform3D();
            skal[i][1].setScale(0.004d);
        }
        TransformGroup[][] skalgroup = new TransformGroup[3][2];
        for (int i = 0; i < 3; i++) {
            skalgroup[i][0] = new TransformGroup(skal[i][0]);
            transgroup[i][0].addChild(skalgroup[i][0]);
            skalgroup[i][1] = new TransformGroup(skal[i][1]);
            transgroup[i][1].addChild(skalgroup[i][1]);
        }
        Font3D font3D = new Font3D(new Font("Courier", Font.PLAIN, 10), new FontExtrusion());
        Text3D[][] text3d = new Text3D[3][2];
        text3d[0][0] = new Text3D(font3D, numbers[0][0], new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_FIRST, Text3D.PATH_RIGHT);
        text3d[0][1] = new Text3D(font3D, numbers[0][1], new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_LAST, Text3D.PATH_RIGHT);
        text3d[1][0] = new Text3D(font3D, numbers[1][0], new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_LAST, Text3D.PATH_RIGHT);
        text3d[1][1] = new Text3D(font3D, numbers[1][1], new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_LAST, Text3D.PATH_RIGHT);
        text3d[2][0] = new Text3D(font3D, numbers[2][0], new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_LAST, Text3D.PATH_RIGHT);
        text3d[2][1] = new Text3D(font3D, numbers[2][1], new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_FIRST, Text3D.PATH_RIGHT);
        ColoringAttributes catts = new ColoringAttributes();
        catts.setColor(new Color3f(getParameterAsColor("foreground")));
        PolygonAttributes patts = new PolygonAttributes();
        patts.setCullFace(PolygonAttributes.CULL_NONE);
        patts.setBackFaceNormalFlip(true);
        Appearance app = new Appearance();
        app.setColoringAttributes(catts);
        app.setPolygonAttributes(patts);
        Shape3D[][] shape3D = new Shape3D[3][2];
        for (int i = 0; i < 3; i++) {
            shape3D[i][0] = new Shape3D();
            shape3D[i][0].setGeometry(text3d[i][0]);
            shape3D[i][0].setAppearance(app);
            shape3D[i][1] = new Shape3D();
            shape3D[i][1].setGeometry(text3d[i][1]);
            shape3D[i][1].setAppearance(app);
        }
        skalgroup[0][0].addChild(shape3D[0][0]);
        skalgroup[0][1].addChild(shape3D[0][1]);
        skalgroup[1][0].addChild(shape3D[1][0]);
        skalgroup[1][1].addChild(shape3D[1][1]);
        Transform3D rotzmin = new Transform3D();
        rotzmin.rotY(3.0d * Math.PI / 2.0d);
        Transform3D rotzmax = new Transform3D();
        rotzmax.rotY(3.0d * Math.PI / 2.0d);
        TransformGroup rotgroupmin = new TransformGroup(rotzmin);
        skalgroup[2][0].addChild(rotgroupmin);
        rotgroupmin.addChild(shape3D[2][0]);
        TransformGroup rotgroupmax = new TransformGroup(rotzmax);
        skalgroup[2][1].addChild(rotgroupmax);
        rotgroupmax.addChild(shape3D[2][1]);
    }

    private void createAxisNames(TransformGroup objDreh) {
        Font3D font3D = new Font3D(new Font("Courier", Font.PLAIN, 10), new FontExtrusion());
        Text3D text_att_x = new Text3D(font3D, x_attribute.getName(), new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_CENTER, Text3D.PATH_RIGHT);
        Text3D text_att_y = new Text3D(font3D, y_attribute.getName(), new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_CENTER, Text3D.PATH_DOWN);
        Text3D text_att_z = new Text3D(font3D, z_attribute.getName(), new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_CENTER, Text3D.PATH_RIGHT);
        ColoringAttributes catts = new ColoringAttributes();
        catts.setColor(new Color3f(getParameterAsColor("foreground")));
        PolygonAttributes patts = new PolygonAttributes();
        patts.setCullFace(PolygonAttributes.CULL_NONE);
        patts.setBackFaceNormalFlip(true);
        Appearance app = new Appearance();
        app.setColoringAttributes(catts);
        app.setPolygonAttributes(patts);
        Transform3D transx = new Transform3D();
        transx.set(new Vector3f(0.0f, -0.6f, 0.6f));
        Transform3D skalx = new Transform3D();
        skalx.setScale(0.008);
        Transform3D transy = new Transform3D();
        transy.set(new Vector3f(-0.6f, 0.0f, 0.6f));
        Transform3D skaly = new Transform3D();
        skaly.setScale(0.008);
        Transform3D transz = new Transform3D();
        transz.set(new Vector3f(-0.6f, -0.6f, 0.0f));
        Transform3D skalz = new Transform3D();
        skalz.setScale(0.008);
        Transform3D rotz = new Transform3D();
        rotz.rotY(3.0d * Math.PI / 2.0d);
        TransformGroup text3D_x = new TransformGroup(transx);
        TransformGroup text3D_y = new TransformGroup(transy);
        TransformGroup text3D_z = new TransformGroup(transz);
        objDreh.addChild(text3D_x);
        objDreh.addChild(text3D_y);
        objDreh.addChild(text3D_z);
        TransformGroup stext3D_x = new TransformGroup(skalx);
        TransformGroup stext3D_y = new TransformGroup(skaly);
        TransformGroup stext3D_z = new TransformGroup(skalz);
        text3D_x.addChild(stext3D_x);
        text3D_y.addChild(stext3D_y);
        text3D_z.addChild(stext3D_z);
        TransformGroup rtext3D_z = new TransformGroup(rotz);
        stext3D_z.addChild(rtext3D_z);
        Shape3D textShape;
        textShape = new Shape3D();
        textShape.setGeometry(text_att_x);
        textShape.setAppearance(app);
        stext3D_x.addChild(textShape);
        textShape = new Shape3D();
        textShape.setGeometry(text_att_y);
        textShape.setAppearance(app);
        stext3D_y.addChild(textShape);
        textShape = new Shape3D();
        textShape.setGeometry(text_att_z);
        textShape.setAppearance(app);
        rtext3D_z.addChild(textShape);
    }

    private Shape3D createAxisShape() {
        Shape3D axisShape = new Shape3D();
        LineArray linea = new LineArray(18, GeometryArray.COORDINATES);
        linea.setCoordinate(0, new Point3f(-0.5f, -0.53f, 0.53f));
        linea.setCoordinate(1, new Point3f(0.5f, -0.53f, 0.53f));
        linea.setCoordinate(2, new Point3f(-0.5f, -0.53f, 0.53f));
        linea.setCoordinate(3, new Point3f(-0.5f, -0.55f, 0.55f));
        linea.setCoordinate(4, new Point3f(0.5f, -0.53f, 0.53f));
        linea.setCoordinate(5, new Point3f(0.5f, -0.55f, 0.55f));
        linea.setCoordinate(6, new Point3f(-0.53f, -0.5f, 0.53f));
        linea.setCoordinate(7, new Point3f(-0.53f, 0.5f, 0.53f));
        linea.setCoordinate(8, new Point3f(-0.53f, -0.5f, 0.53f));
        linea.setCoordinate(9, new Point3f(-0.55f, -0.5f, 0.55f));
        linea.setCoordinate(10, new Point3f(-0.53f, 0.5f, 0.53f));
        linea.setCoordinate(11, new Point3f(-0.55f, 0.5f, 0.55f));
        linea.setCoordinate(12, new Point3f(-0.53f, -0.53f, -0.5f));
        linea.setCoordinate(13, new Point3f(-0.53f, -0.53f, 0.5f));
        linea.setCoordinate(14, new Point3f(-0.53f, -0.53f, -0.5f));
        linea.setCoordinate(15, new Point3f(-0.55f, -0.55f, -0.5f));
        linea.setCoordinate(16, new Point3f(-0.53f, -0.53f, 0.5f));
        linea.setCoordinate(17, new Point3f(-0.55f, -0.55f, 0.5f));
        axisShape.setGeometry(linea);
        Appearance a = new Appearance();
        PolygonAttributes p = new PolygonAttributes();
        p.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        p.setCullFace(PolygonAttributes.CULL_NONE);
        a.setPolygonAttributes(p);
        LineAttributes latts = new LineAttributes();
        latts.setLineWidth(2.0f);
        a.setLineAttributes(latts);
        ColoringAttributes catts = new ColoringAttributes();
        catts.setColor(new Color3f(getParameterAsColor("foreground")));
        a.setColoringAttributes(catts);
        axisShape.setAppearance(a);
        return axisShape;
    }

    private Shape3D createPointShape() {
        Shape3D pointShape = new Shape3D();
        PointArray pa;
        if (c_attr >= 0) {
            pa = new PointArray(number_of_samples, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
        } else {
            pa = new PointArray(number_of_samples, GeometryArray.COORDINATES);
        }
        Point3f[] pts = new Point3f[number_of_samples];
        float xmin, ymin, zmin, xmax, ymax, zmax;
        float xband, yband, zband;
        if (x_attribute.isNominal()) {
            xmin = 0.0f;
            xmax = x_attribute.getMapping().size() - 1.0f;
        } else {
            xmin = (float) source.getExampleSet().getStatistics(x_attribute, Statistics.MINIMUM);
            xmax = (float) source.getExampleSet().getStatistics(x_attribute, Statistics.MAXIMUM);
        }
        xband = xmax - xmin;
        if (y_attribute.isNominal()) {
            ymin = 0.0f;
            ymax = y_attribute.getMapping().size() - 1.0f;
        } else {
            ymin = (float) source.getExampleSet().getStatistics(y_attribute, Statistics.MINIMUM);
            ymax = (float) source.getExampleSet().getStatistics(y_attribute, Statistics.MAXIMUM);
        }
        yband = ymax - ymin;
        if (z_attribute.isNominal()) {
            zmin = 0.0f;
            zmax = z_attribute.getMapping().size() - 1.0f;
        } else {
            zmin = (float) source.getExampleSet().getStatistics(z_attribute, Statistics.MINIMUM);
            zmax = (float) source.getExampleSet().getStatistics(z_attribute, Statistics.MAXIMUM);
        }
        zband = zmax - zmin;
        float x, y, z;
        for (int i = 0; i < number_of_samples; i++) {
            x = -0.5f + ((x_samples[i] - xmin) / xband);
            y = -0.5f + ((y_samples[i] - ymin) / yband);
            z = 0.5f - ((z_samples[i] - zmin) / zband);
            pts[i] = new Point3f(x, y, z);
        }
        pa.setCoordinates(0, pts);
        if (c_attr >= 0) {
            ExampleSet set = source.getExampleSet();
            ExampleColoring coloring = source.getExampleColoring();
            Color3f[] colors = new Color3f[number_of_samples];
            Color color;
            for (int i = 0; i < number_of_samples; i++) {
                color = getParameterAsColor("foreground");
                if ((set.getAttributes().getLabel() != null) && (set.getAttributes().getLabel().getName().equals(colorattribute.getName()))) {
                    color = coloring.getColorOfLabelValue(colorattribute, colorsamples[i]);
                } else if ((set.getAttributes().getPredictedLabel() != null) && (set.getAttributes().getPredictedLabel().getName().equals(colorattribute.getName()))) {
                    if (colorattribute.isNominal()) {
                        color = colortable[(int) colorsamples[i]];
                    } else {
                        int index = (int) (((colorsamples[i] - source.getExampleSet().getStatistics(colorattribute, Statistics.MINIMUM)) / (set.getStatistics(colorattribute, Statistics.MAXIMUM) - source.getExampleSet().getStatistics(colorattribute, Statistics.MINIMUM))) * 99.0f);
                        if (index < 0) index = 0;
                        if (index > 99) index = 99;
                        color = colortable[index];
                    }
                } else {
                    if (colorattribute.isNominal()) {
                        color = colortable[(int) colorsamples[i]];
                    }
                    if (Utils.isNumeric(colorattribute)) {
                        int index = (int) (((colorsamples[i] - source.getExampleSet().getStatistics(colorattribute, Statistics.MINIMUM)) / (set.getStatistics(colorattribute, Statistics.MAXIMUM) - source.getExampleSet().getStatistics(colorattribute, Statistics.MINIMUM))) * 99.0f);
                        if (index < 0) index = 0;
                        if (index > 99) index = 99;
                        color = colortable[index];
                    }
                }
                colors[i] = new Color3f(color);
            }
            pa.setColors(0, colors);
        }
        pointShape.setGeometry(pa);
        Appearance app = new Appearance();
        PointAttributes patts = new PointAttributes();
        patts.setPointSize((float) getParameterAsDouble("point_size"));
        app.setPointAttributes(patts);
        if (c_attr < 0) {
            ColoringAttributes catts = new ColoringAttributes();
            catts.setColor(new Color3f(getParameterAsColor("foreground")));
            app.setColoringAttributes(catts);
        }
        pointShape.setAppearance(app);
        return pointShape;
    }

    private Shape3D[] createHyperplaneShape() {
        float bound0 = (float) ((0.0f - min[2]) / (max[2] - min[2]));
        float bound1 = (float) ((-1.0f - min[2]) / (max[2] - min[2]));
        float bound2 = (float) ((1.0f - min[2]) / (max[2] - min[2]));
        LineArray lowboundline = new LineArray(44, GeometryArray.COORDINATES);
        for (int i = 0; i < 11; i++) {
            lowboundline.setCoordinate(i * 2, new Point3f(-0.5f + i * 0.1f, 0.5f, 0.5f - bound1));
            lowboundline.setCoordinate(i * 2 + 1, new Point3f(-0.5f + i * 0.1f, -0.5f, 0.5f - bound1));
        }
        for (int i = 11; i < 22; i++) {
            lowboundline.setCoordinate(i * 2, new Point3f(0.5f, -0.5f + (i - 11) * 0.1f, 0.5f - bound1));
            lowboundline.setCoordinate(i * 2 + 1, new Point3f(-0.5f, -0.5f + (i - 11) * 0.1f, 0.5f - bound1));
        }
        LineArray upboundline = new LineArray(44, GeometryArray.COORDINATES);
        for (int i = 0; i < 11; i++) {
            upboundline.setCoordinate(i * 2, new Point3f(-0.5f + i * 0.1f, 0.5f, 0.5f - bound2));
            upboundline.setCoordinate(i * 2 + 1, new Point3f(-0.5f + i * 0.1f, -0.5f, 0.5f - bound2));
        }
        for (int i = 11; i < 22; i++) {
            upboundline.setCoordinate(i * 2, new Point3f(0.5f, -0.5f + (i - 11) * 0.1f, 0.5f - bound2));
            upboundline.setCoordinate(i * 2 + 1, new Point3f(-0.5f, -0.5f + (i - 11) * 0.1f, 0.5f - bound2));
        }
        LineArray hyperline = new LineArray(44, GeometryArray.COORDINATES);
        for (int i = 0; i < 11; i++) {
            hyperline.setCoordinate(i * 2, new Point3f(-0.5f + i * 0.1f, 0.5f, 0.5f - bound0));
            hyperline.setCoordinate(i * 2 + 1, new Point3f(-0.5f + i * 0.1f, -0.5f, 0.5f - bound0));
        }
        for (int i = 11; i < 22; i++) {
            hyperline.setCoordinate(i * 2, new Point3f(0.5f, -0.5f + (i - 11) * 0.1f, 0.5f - bound0));
            hyperline.setCoordinate(i * 2 + 1, new Point3f(-0.5f, -0.5f + (i - 11) * 0.1f, 0.5f - bound0));
        }
        Shape3D lowboundlineshape = new Shape3D();
        lowboundlineshape.setGeometry(lowboundline);
        Shape3D upboundlineshape = new Shape3D();
        upboundlineshape.setGeometry(upboundline);
        Shape3D hyperlineshape = new Shape3D();
        hyperlineshape.setGeometry(hyperline);
        Appearance boundslineapp = new Appearance();
        PolygonAttributes p = new PolygonAttributes();
        p.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        p.setCullFace(PolygonAttributes.CULL_NONE);
        boundslineapp.setPolygonAttributes(p);
        LineAttributes latts = new LineAttributes();
        latts.setLineWidth(2.0f);
        latts.setLinePattern(LineAttributes.PATTERN_DOT);
        boundslineapp.setLineAttributes(latts);
        ColoringAttributes catts = new ColoringAttributes();
        catts.setColor(new Color3f(getParameterAsColor("bound_color")));
        boundslineapp.setColoringAttributes(catts);
        lowboundlineshape.setAppearance(boundslineapp);
        upboundlineshape.setAppearance(boundslineapp);
        Appearance hyperlineapp = new Appearance();
        PolygonAttributes p2 = new PolygonAttributes();
        p2.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        p2.setCullFace(PolygonAttributes.CULL_NONE);
        hyperlineapp.setPolygonAttributes(p2);
        LineAttributes latts2 = new LineAttributes();
        latts2.setLineWidth(2.0f);
        hyperlineapp.setLineAttributes(latts2);
        ColoringAttributes catts2 = new ColoringAttributes();
        catts2.setColor(new Color3f(getParameterAsColor("hyperplane_color")));
        hyperlineapp.setColoringAttributes(catts2);
        hyperlineshape.setAppearance(hyperlineapp);
        int anzahl = 0;
        if (getParameterAsBoolean("show_hyperplane")) anzahl++;
        if (getParameterAsBoolean("show_lower_bound")) anzahl++;
        if (getParameterAsBoolean("show_upper_bound")) anzahl++;
        int zaehler = 0;
        Shape3D[] shapes = new Shape3D[anzahl];
        if (getParameterAsBoolean("show_hyperplane")) {
            shapes[zaehler] = hyperlineshape;
            zaehler++;
        }
        if (getParameterAsBoolean("show_lower_bound")) {
            shapes[zaehler] = lowboundlineshape;
            zaehler++;
        }
        if (getParameterAsBoolean("show_upper_bound")) {
            shapes[zaehler] = upboundlineshape;
            zaehler++;
        }
        return shapes;
    }

    private Shape3D createCoordinateSystem() {
        QuadArray wuerfel = createCube();
        wuerfel.setCapability(QuadArray.ALLOW_COORDINATE_READ);
        wuerfel.setCapability(QuadArray.ALLOW_INTERSECT);
        Shape3D s3d = new Shape3D();
        s3d.setGeometry(wuerfel);
        Appearance a = new Appearance();
        PolygonAttributes p = new PolygonAttributes();
        p.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        p.setCullFace(PolygonAttributes.CULL_NONE);
        a.setPolygonAttributes(p);
        LineAttributes latts = new LineAttributes();
        latts.setLineWidth(2.0f);
        a.setLineAttributes(latts);
        ColoringAttributes catts = new ColoringAttributes();
        catts.setColor(new Color3f(getParameterAsColor("foreground")));
        a.setColoringAttributes(catts);
        s3d.setAppearance(a);
        s3d.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        s3d.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
        return s3d;
    }

    private QuadArray createCube() {
        QuadArray plane = new QuadArray(24, GeometryArray.COORDINATES);
        Point3f pa = new Point3f(-0.5f, 0.5f, -0.5f);
        Point3f pb = new Point3f(-0.5f, -0.5f, -0.5f);
        Point3f pc = new Point3f(0.5f, -0.5f, -0.5f);
        Point3f pd = new Point3f(0.5f, 0.5f, -0.5f);
        Point3f pe = new Point3f(-0.5f, 0.5f, 0.5f);
        Point3f pf = new Point3f(-0.5f, -0.5f, 0.5f);
        Point3f pg = new Point3f(0.5f, -0.5f, 0.5f);
        Point3f ph = new Point3f(0.5f, 0.5f, 0.5f);
        plane.setCoordinate(0, pa);
        plane.setCoordinate(1, pb);
        plane.setCoordinate(2, pc);
        plane.setCoordinate(3, pd);
        plane.setCoordinate(4, pe);
        plane.setCoordinate(5, pf);
        plane.setCoordinate(6, pg);
        plane.setCoordinate(7, ph);
        plane.setCoordinate(8, pe);
        plane.setCoordinate(9, pf);
        plane.setCoordinate(10, pb);
        plane.setCoordinate(11, pa);
        plane.setCoordinate(12, pd);
        plane.setCoordinate(13, pc);
        plane.setCoordinate(14, pg);
        plane.setCoordinate(15, ph);
        plane.setCoordinate(16, pe);
        plane.setCoordinate(17, pa);
        plane.setCoordinate(18, pd);
        plane.setCoordinate(19, ph);
        plane.setCoordinate(20, pf);
        plane.setCoordinate(21, pb);
        plane.setCoordinate(22, pc);
        plane.setCoordinate(23, pg);
        return plane;
    }

    private void initDisplay() {
        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        canvas3D = new JViToCanvas3D(config);
        u = new SimpleUniverse(canvas3D);
        BranchGroup scene = createSceneGraph();
        u.getViewingPlatform().setNominalViewingTransform();
        u.addBranchGraph(scene);
    }

    private void initComponents() {
        initDisplay();
        mainpane = new JPanel();
        mainpane.setLayout(new BorderLayout());
        ExampleSet set;
        if (this.source.isCompiled()) set = source.getExampleSet(); else return;
        mainpane.add(canvas3D, BorderLayout.CENTER);
        JPanel settings = new JPanel();
        settings.setLayout(new GridLayout(2, 1));
        mainpane.add(settings, BorderLayout.EAST);
        if (getParameterAsInt("color_attribute") != 0) {
            JPanel colorpanel = new JPanel();
            if (Utils.isNumeric(this.colorattribute)) {
                colorpanel = new ColorPanel((float) source.getExampleSet().getStatistics(colorattribute, Statistics.MINIMUM), (float) set.getStatistics(colorattribute, Statistics.MAXIMUM), this.colortable, getParameterAsColor("foreground"), getParameterAsColor("background"));
            } else {
                colorpanel = new ColorPanel(colorattribute, (float) set.getStatistics(colorattribute, Statistics.MINIMUM), (float) set.getStatistics(colorattribute, Statistics.MAXIMUM), this.colortable, getParameterAsColor("foreground"), getParameterAsColor("background"));
            }
            Color[] colors;
            if (getParameterAsInt("color_attribute") <= set.getAttributes().size()) {
            } else if ((set.getAttributes().getLabel() != null) && (set.getAttributes().getLabel().isNominal()) && (getParameterAsInt("color_attribute") == set.getAttributes().size() + 1)) {
                colors = new Color[set.getAttributes().getLabel().getMapping().getValues().size()];
                Collection values = set.getAttributes().getLabel().getMapping().getValues();
                Object[] valuesstr = values.toArray();
                for (int i = 0; i < valuesstr.length; i++) {
                    colors[i] = source.getExampleColoring().getColorOfLabelValue(set.getAttributes().getLabel(), (String) valuesstr[i]);
                }
                colorpanel = new ColorPanel(set.getAttributes().getLabel(), (float) set.getStatistics(set.getAttributes().getLabel(), Statistics.MINIMUM), (float) set.getStatistics(set.getAttributes().getLabel(), Statistics.MAXIMUM), colors, getParameterAsColor("foreground"), getParameterAsColor("background"));
            } else if ((set.getAttributes().getPredictedLabel() != null) && (set.getAttributes().getPredictedLabel().isNominal())) {
                Collection values = set.getAttributes().getPredictedLabel().getMapping().getValues();
                Object[] valuesstr = values.toArray();
                colors = new Color[set.getAttributes().getPredictedLabel().getMapping().getValues().size() * set.getAttributes().getPredictedLabel().getMapping().getValues().size()];
                String[] valuestring = new String[valuesstr.length * valuesstr.length];
                for (int i = 0; i < valuesstr.length; i++) {
                    for (int j = 0; j < valuesstr.length; j++) {
                        valuestring[i * valuesstr.length + j] = new String((String) valuesstr[i] + " - " + (String) valuesstr[j]);
                        colors[i * valuesstr.length + j] = source.getExampleColoring().getColorOfLabelvsPred(set.getAttributes().getLabel(), set.getAttributes().getPredictedLabel(), (String) valuesstr[i], (String) valuesstr[j]);
                    }
                }
                colorpanel = new ColorPanel(set.getAttributes().getPredictedLabel(), (float) set.getStatistics(set.getAttributes().getPredictedLabel(), Statistics.MINIMUM), (float) set.getStatistics(set.getAttributes().getPredictedLabel(), Statistics.MAXIMUM), valuestring, colors, getParameterAsColor("foreground"), getParameterAsColor("background"));
            }
            colorpanel.setPreferredSize(new Dimension(125, colorpanel.getHeight()));
            settings.add(colorpanel);
        }
        textarea = new JTextArea();
        textarea.setEditable(false);
        JScrollPane scrollpane = new JScrollPane(textarea);
        settings.add(scrollpane);
        if (getParameterAsInt("color_attribute") == 0) {
            settings.add(new JPanel());
            scrollpane.setPreferredSize(new Dimension(125, scrollpane.getHeight()));
        }
    }

    @Override
    public void compile() throws CompilerException {
        this.iscompiling = true;
        if (source == null) {
            throw new CompilerException("Cannot compile ScatterPlot2D. " + "This operator must be a child of a data-operator.");
        }
        ExampleSet set = source.getExampleSet();
        if (set.getAttributes().getPredictedLabel() == null) {
            throw new CompilerException("The ExampleSet must have a numerical predicted label.");
        }
        if (set.getAttributes().getPredictedLabel().isNominal()) {
            throw new CompilerException("The ExampleSet must have a numerical predicted label.");
        }
        number_of_samples = set.size();
        number_atts = set.getAttributes().size();
        x_samples = new float[number_of_samples];
        y_samples = new float[number_of_samples];
        z_samples = new float[number_of_samples];
        labelvalues = new float[number_of_samples];
        if (getParameterAsInt("color_attribute") != 0) {
            colorsamples = new float[number_of_samples];
        }
        x_attr = getParameterAsInt("x_attribute");
        y_attr = getParameterAsInt("y_attribute");
        c_attr = getParameterAsInt("color_attribute") - 1;
        min = new double[3];
        max = new double[3];
        if (x_attr < number_atts) {
            x_attribute = getAttribute(x_attr);
        } else if ((x_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
            x_attribute = set.getAttributes().getLabel();
        } else {
            x_attribute = set.getAttributes().getPredictedLabel();
        }
        if (x_attribute.isNominal()) {
            min[0] = 0.0d;
            max[0] = x_attribute.getMapping().size() - 1.0d;
        } else {
            min[0] = source.getExampleSet().getStatistics(x_attribute, Statistics.MINIMUM);
            max[0] = set.getStatistics(x_attribute, Statistics.MAXIMUM);
        }
        if (y_attr < number_atts) {
            y_attribute = getAttribute(y_attr);
        } else if ((y_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
            y_attribute = set.getAttributes().getLabel();
        } else {
            y_attribute = set.getAttributes().getPredictedLabel();
        }
        if (y_attribute.isNominal()) {
            min[1] = 0.0d;
            max[1] = y_attribute.getMapping().size() - 1.0d;
        } else {
            min[1] = source.getExampleSet().getStatistics(y_attribute, Statistics.MINIMUM);
            max[1] = set.getStatistics(y_attribute, Statistics.MAXIMUM);
        }
        z_attribute = set.getAttributes().getPredictedLabel();
        if (z_attribute.isNominal()) {
            min[2] = 0.0d;
            max[2] = z_attribute.getMapping().size() - 1.0d;
        } else {
            min[2] = source.getExampleSet().getStatistics(z_attribute, Statistics.MINIMUM);
            max[2] = set.getStatistics(z_attribute, Statistics.MAXIMUM);
        }
        if (c_attr >= 0) {
            if (c_attr < number_atts) {
                colorattribute = getAttribute(c_attr);
            } else if ((c_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
                colorattribute = set.getAttributes().getLabel();
            } else {
                colorattribute = set.getAttributes().getPredictedLabel();
            }
        }
        boolean haslabel = set.getAttributes().getLabel() != null;
        boolean haspredlabel = set.getAttributes().getPredictedLabel() != null;
        Iterator<Example> reader = set.iterator();
        Example example;
        for (int sample = 0; sample < number_of_samples; sample++) {
            example = reader.next();
            if (x_attr < number_atts) {
                x_samples[sample] = (float) example.getValue(x_attribute);
            } else if ((x_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
                x_samples[sample] = (float) example.getLabel();
            } else {
                x_samples[sample] = (float) example.getPredictedLabel();
            }
            if (y_attr < number_atts) {
                y_samples[sample] = (float) example.getValue(y_attribute);
            } else if ((y_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
                y_samples[sample] = (float) example.getLabel();
            } else {
                y_samples[sample] = (float) example.getPredictedLabel();
            }
            z_samples[sample] = (float) example.getPredictedLabel();
            if (haslabel) {
                labelvalues[sample] = (float) example.getLabel();
            }
            if (c_attr >= 0) {
                if (c_attr < number_atts) {
                    colorsamples[sample] = (float) example.getValue(colorattribute);
                } else if ((c_attr == number_atts) && (set.getAttributes().getLabel() != null)) {
                    colorsamples[sample] = (float) example.getLabel();
                } else {
                    colorsamples[sample] = (float) example.getPredictedLabel();
                }
            }
        }
        if (c_attr < 0) {
        } else if ((haslabel) && (colorattribute.getName().equals(set.getAttributes().getLabel().getName()))) {
            startcolor = source.getExampleColoring().getStartColorOfLabel(colorattribute);
            endcolor = source.getExampleColoring().getEndColorOfLabel(colorattribute);
            initColorTable();
        } else if ((haspredlabel) && (colorattribute.getName().equals(source.getExampleSet().getAttributes().getPredictedLabel().getName())) && (colorattribute.isNominal())) {
            Collection values = set.getAttributes().getPredictedLabel().getMapping().getValues();
            Object[] valuesstr = values.toArray();
            colortable = new Color[set.getAttributes().getPredictedLabel().getMapping().getValues().size() * set.getAttributes().getPredictedLabel().getMapping().getValues().size()];
            for (int i = 0; i < valuesstr.length; i++) {
                for (int j = 0; j < valuesstr.length; j++) {
                    colortable[i * valuesstr.length + j] = source.getExampleColoring().getColorOfLabelvsPred(set.getAttributes().getLabel(), set.getAttributes().getPredictedLabel(), (String) valuesstr[i], (String) valuesstr[j]);
                }
            }
            float[] pvalues = new float[number_of_samples];
            for (int i = 0; i < colorsamples.length; i++) {
                pvalues[i] = labelvalues[i] * valuesstr.length + colorsamples[i];
            }
            startcolor = source.getExampleColoring().getStartColorOfPredLabel(colorattribute);
            endcolor = source.getExampleColoring().getEndColorOfPredLabel(colorattribute);
        } else if ((haspredlabel) && (colorattribute.getName().equals(source.getExampleSet().getAttributes().getPredictedLabel().getName())) && Utils.isNumeric(colorattribute)) {
            startcolor = source.getExampleColoring().getStartColorOfPredLabel(colorattribute);
            endcolor = source.getExampleColoring().getEndColorOfPredLabel(colorattribute);
            initColorTable();
        } else {
            startcolor = source.getExampleColoring().getStartColorOfAttribute(colorattribute);
            endcolor = source.getExampleColoring().getEndColorOfAttribute(colorattribute);
            initColorTable();
        }
        initComponents();
        this.iscompiling = false;
        this.iscompiled = true;
    }

    /**
	 * Inits the colortable.
	 * 
	 */
    public void initColorTable() {
        int size = 100;
        if (colorattribute.isNominal()) {
            size = colorattribute.getMapping().size();
        }
        if (startcolor == null) startcolor = Color.red;
        if (endcolor == null) endcolor = Color.blue;
        colortable = new Color[size];
        float red_start = (float) (startcolor.getRed());
        float green_start = (float) (startcolor.getGreen());
        float blue_start = (float) (startcolor.getBlue());
        float red_end = (float) (endcolor.getRed());
        float green_end = (float) (endcolor.getGreen());
        float blue_end = (float) (endcolor.getBlue());
        float d_red = (red_end - red_start) / (size - 1.0f);
        float d_green = (green_end - green_start) / (size - 1.0f);
        float d_blue = (blue_end - blue_start) / (size - 1.0f);
        for (int i = 0; i < colortable.length; i++) {
            colortable[i] = new Color((int) red_start, (int) green_start, (int) blue_start);
            red_start += d_red;
            green_start += d_green;
            blue_start += d_blue;
        }
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        ParameterType type;
        type = new ParameterTypeDynamicCategory("x_attribute", "Attribute for the x-axis.", getAttributes(), 0);
        types.add(type);
        type = new ParameterTypeDynamicCategory("y_attribute", "Attribute for the y-axis.", getAttributes(), 0);
        types.add(type);
        type = new ParameterTypeDynamicCategory("color_attribute", "This attribute is the color-dimension.", getColorAttributes(), 0);
        types.add(type);
        type = new ParameterTypeDouble("point_size", "Size of the points.", 0, 100, 3);
        types.add(type);
        type = new ParameterTypeBoolean("show_hyperplane", "Shows the hyperplane in the plot.", true);
        types.add(type);
        type = new ParameterTypeBoolean("show_upper_bound", "Shows the lower bound in the plot.", false);
        types.add(type);
        type = new ParameterTypeBoolean("show_lower_bound", "Shows the lower bound in the plot.", false);
        types.add(type);
        type = new ParameterTypeColor("hyperplane_color", "Color of the hyperplane.", Color.RED);
        types.add(type);
        type = new ParameterTypeColor("bound_color", "Color of the bounds.", Color.BLUE);
        types.add(type);
        return types;
    }

    /**
	 * @see com.jvito.plot.Plot#parentIsCompiled()
	 */
    @Override
    public void parentIsCompiled() {
        ((ParameterTypeDynamicCategory) getParameterType("color_attribute")).setValues(getColorAttributes());
        ((ParameterTypeDynamicCategory) getParameterType("x_attribute")).setValues(getAttributes());
        ((ParameterTypeDynamicCategory) getParameterType("y_attribute")).setValues(getAttributes());
    }

    /**
	 * @see com.jvito.plot.Plot#refreshParameter()
	 */
    @Override
    public void refreshParameter() {
        ((ParameterTypeDynamicCategory) getParameterType("color_attribute")).setValues(getColorAttributes());
        ((ParameterTypeDynamicCategory) getParameterType("x_attribute")).setValues(getAttributes());
        ((ParameterTypeDynamicCategory) getParameterType("y_attribute")).setValues(getAttributes());
    }

    public String[] getAttributes() {
        if (source == null) return new String[] { "" };
        ExampleSet set = source.getExampleSet();
        if (set == null) return new String[] { "" };
        int count = 0;
        count += set.getAttributes().size();
        if (set.getAttributes().getLabel() != null) {
            count++;
        }
        if (set.getAttributes().getPredictedLabel() != null) {
            count++;
        }
        String[] categories = new String[count];
        for (int i = 0; i < set.getAttributes().size(); i++) {
            categories[i] = getAttribute(i).getName();
        }
        int index = set.getAttributes().size();
        if (set.getAttributes().getLabel() != null) {
            categories[index] = set.getAttributes().getLabel().getName();
            index++;
        }
        if (set.getAttributes().getPredictedLabel() != null) {
            categories[index] = set.getAttributes().getPredictedLabel().getName();
        }
        return categories;
    }

    public String[] getColorAttributes() {
        if (source == null) {
            return new String[] { ATTRIBUTE_NONE };
        }
        if (!source.isCompiled()) return new String[] { ATTRIBUTE_NONE };
        ExampleSet set = source.getExampleSet();
        if (set == null) return new String[] { ATTRIBUTE_NONE };
        int count = 1;
        count += set.getAttributes().size();
        if (set.getAttributes().getLabel() != null) {
            count++;
        }
        if (set.getAttributes().getPredictedLabel() != null) {
            count++;
        }
        String[] categories = new String[count];
        categories[0] = ATTRIBUTE_NONE;
        for (int i = 1; i <= set.getAttributes().size(); i++) {
            categories[i] = getAttribute(i - 1).getName();
        }
        int index = set.getAttributes().size() + 1;
        if (set.getAttributes().getLabel() != null) {
            categories[index] = set.getAttributes().getLabel().getName();
            index++;
        }
        if (set.getAttributes().getPredictedLabel() != null) {
            categories[index] = set.getAttributes().getPredictedLabel().getName();
            index++;
        }
        return categories;
    }
}
