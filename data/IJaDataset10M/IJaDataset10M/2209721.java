package edu.gsbme.yakitori.Renderer.RenderObj.Cells;

import java.awt.Color;
import java.util.ArrayList;
import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3b;
import javax.vecmath.Point3d;
import edu.gsbme.geometrykernel.data.dim2.tri;
import edu.gsbme.yakitori.Algorithm.ScalingController;
import edu.gsbme.yakitori.Exception.IncorrectOptionException;
import edu.gsbme.yakitori.Exception.IncorrectRenderClassException;
import edu.gsbme.yakitori.Renderer.Controller.Reference.Cells.CellReference;
import edu.gsbme.yakitori.Renderer.Controller.Reference.Cells.ObjReference;
import edu.gsbme.yakitori.Renderer.Controller.Reference.mesh.Dim2GroupReference;
import edu.gsbme.yakitori.Renderer.Pipeline.FMLPipelineOptions;
import edu.gsbme.yakitori.Renderer.RenderObj.RenderObject;

/**
 * render triangle
 * @author David
 *
 */
public class RenderTriangle extends RenderObject {

    public RenderTriangle() {
        super("RenderTriangle");
    }

    public LineArray getOutline(Point3d[] vtx) {
        LineArray array = new LineArray(6, GeometryArray.COORDINATES | LineStripArray.COLOR_3);
        array.setCoordinate(0, vtx[0]);
        array.setCoordinate(1, vtx[1]);
        array.setCoordinate(2, vtx[1]);
        array.setCoordinate(3, vtx[2]);
        array.setCoordinate(4, vtx[2]);
        array.setCoordinate(5, vtx[0]);
        array.setCapability(LineStripArray.ALLOW_COLOR_WRITE);
        array.setCapability(LineStripArray.ALLOW_COLOR_READ);
        return array;
    }

    public TriangleArray getGeometry(Point3d[] vtx) {
        TriangleArray array = new TriangleArray(6, GeometryArray.COORDINATES | LineStripArray.COLOR_3);
        array.setCoordinate(0, vtx[0]);
        array.setColor(0, new Color3b(Color.black));
        array.setCoordinate(1, vtx[1]);
        array.setColor(1, new Color3b(Color.black));
        array.setCoordinate(2, vtx[2]);
        array.setColor(2, new Color3b(Color.black));
        array.setCoordinate(3, vtx[2]);
        array.setColor(3, new Color3b(Color.black));
        array.setCoordinate(4, vtx[1]);
        array.setColor(4, new Color3b(Color.black));
        array.setCoordinate(5, vtx[0]);
        array.setColor(5, new Color3b(Color.black));
        array.setCapability(LineStripArray.ALLOW_COLOR_WRITE);
        array.setCapability(LineStripArray.ALLOW_COLOR_READ);
        return array;
    }

    @Override
    public TransformGroup render() throws IncorrectOptionException, IncorrectRenderClassException {
        TransformGroup tg = new TransformGroup();
        FMLPipelineOptions options = (FMLPipelineOptions) getOption();
        if (getData().length == 1) {
            CellReference ref = (CellReference) getObjReference();
            ref.setClassID("Triangle");
            Shape3D tri = generate((tri) getData()[0]);
            tg.addChild(tri);
            ref.setReferenceObj(tri);
        } else {
            ArrayList<Object> ref_array = new ArrayList<Object>();
            Dim2GroupReference ref = (Dim2GroupReference) getObjReference();
            ref.setClassID("Triangle Group");
            for (int i = 0; i < getData().length; i++) {
                Shape3D tri = generate((tri) getData()[i]);
                tg.addChild(tri);
                ref_array.add(tri);
            }
            ref.setReferenceObj(ref_array.toArray());
        }
        return tg;
    }

    private Shape3D generate(tri cell) {
        FMLPipelineOptions options = (FMLPipelineOptions) getOption();
        Point3d[] array = new Point3d[cell.vertex.length];
        double[][] coord = (double[][]) cell.getCoordinates();
        for (int i = 0; i < array.length; i++) {
            array[i] = new Point3d(ScalingController.convertCoordinate(coord[i][0], options.getTransformScale()), ScalingController.convertCoordinate(coord[i][1], options.getTransformScale()), ScalingController.convertCoordinate(coord[i][2], options.getTransformScale()));
        }
        LineArray geom = getOutline(array);
        Shape3D shape = new Shape3D(geom);
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        Appearance appr = new Appearance();
        appr.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_READ);
        appr.setCapability(Appearance.ALLOW_RENDERING_ATTRIBUTES_WRITE);
        RenderingAttributes rAttr = new RenderingAttributes();
        rAttr.setCapability(RenderingAttributes.ALLOW_VISIBLE_READ);
        rAttr.setCapability(RenderingAttributes.ALLOW_VISIBLE_WRITE);
        appr.setRenderingAttributes(rAttr);
        shape.setAppearance(appr);
        ObjReference ref = getObjReference();
        if (ref.getIndex() == -1) options.getReferenceLib().insertDim2Ref(ref);
        geom.setName("" + ref.getIndex());
        shape.setName("" + ref.getIndex());
        return shape;
    }
}
