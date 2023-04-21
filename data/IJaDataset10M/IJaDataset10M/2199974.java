package edu.gsbme.yakitori.Renderer.RenderObj.Cells;

import java.awt.Color;
import java.util.ArrayList;
import javax.media.j3d.Appearance;
import javax.media.j3d.LineStripArray;
import javax.media.j3d.RenderingAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import edu.gsbme.geometrykernel.data.dim1.BSplineCurve;
import edu.gsbme.yakitori.Exception.IncorrectOptionException;
import edu.gsbme.yakitori.Exception.IncorrectRenderClassException;
import edu.gsbme.yakitori.Renderer.Controller.Reference.Cells.BSplineCurveReference;
import edu.gsbme.yakitori.Renderer.Controller.Reference.Cells.ObjReference;
import edu.gsbme.yakitori.Renderer.Controller.Reference.mesh.Dim2GroupReference;
import edu.gsbme.yakitori.Renderer.Pipeline.FMLPipelineOptions;
import edu.gsbme.yakitori.Renderer.RenderObj.RenderObject;
import edu.gsbme.yakitori.Renderer.draw.Dim1Drawer;

/**
 * render bspline curve object
 * @author David
 *
 */
public class RenderBSplineCurve extends RenderObject {

    public RenderBSplineCurve() {
        super("RenderBSPlineCurve");
    }

    @Override
    public TransformGroup render() throws IncorrectOptionException, IncorrectRenderClassException {
        TransformGroup trans = new TransformGroup();
        if (getData().length == 1) {
            Shape3D shape = generate((BSplineCurve) getData()[0]);
            BSplineCurveReference ref = (BSplineCurveReference) getObjReference();
            ref.setReferenceObj(shape);
            trans.addChild(shape);
        } else {
            ArrayList<Shape3D> ref_array = new ArrayList<Shape3D>();
            for (int i = 0; i < getData().length; i++) {
                Shape3D shape = generate((BSplineCurve) getData()[i]);
                trans.addChild(shape);
                ref_array.add(shape);
            }
            Dim2GroupReference ref = (Dim2GroupReference) getObjReference();
            ref.setReferenceObj(ref_array.toArray());
        }
        return trans;
    }

    private Shape3D generate(BSplineCurve cell) {
        FMLPipelineOptions options = (FMLPipelineOptions) getOption();
        LineStripArray line = Dim1Drawer.drawBSplineCurve(cell, 25, Color.black, options.getTransformScale());
        Shape3D shape = new Shape3D(line);
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
        if (ref.getIndex() == -1) options.getReferenceLib().insertDim1Ref(ref);
        line.setName("" + ref.getIndex());
        shape.setName("" + ref.getIndex());
        return shape;
    }
}
