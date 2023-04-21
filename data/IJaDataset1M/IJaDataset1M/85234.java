package de.grogra.ext.x3d.io;

import java.io.IOException;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4d;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import de.grogra.ext.x3d.ObjectBase;
import de.grogra.ext.x3d.Util;
import de.grogra.ext.x3d.X3DExport;
import de.grogra.ext.x3d.objects.X3DTransform;
import de.grogra.graph.GraphState;
import de.grogra.graph.impl.Node;
import de.grogra.imp3d.objects.SceneTree.Leaf;

/**
 * Used to import and export a transformation.
 * 
 * @author Udo Bischof, Uwe Mannl
 *
 */
public class X3DTransformIO extends ObjectBase {

    protected Node doImportImpl(Attributes atts) {
        X3DTransform newTransform = new X3DTransform();
        String valueString;
        valueString = atts.getValue("translation");
        if (valueString != null) newTransform.setX3dTranslation(Util.splitStringToTuple3f(new Vector3f(), valueString));
        valueString = atts.getValue("rotation");
        if (valueString != null) newTransform.setX3dRotation(Util.splitStringToTuple4f(new Vector4f(), valueString));
        valueString = atts.getValue("scale");
        if (valueString != null) newTransform.setX3dScale(Util.splitStringToTuple3f(new Vector3f(), valueString));
        valueString = atts.getValue("scaleOrientation");
        if (valueString != null) newTransform.setX3dScaleOrientation(Util.splitStringToTuple4f(new Vector4f(), valueString));
        valueString = atts.getValue("center");
        if (valueString != null) newTransform.setX3dCenter(Util.splitStringToTuple3f(new Point3f(), valueString));
        valueString = atts.getValue("bboxCenter");
        if (valueString != null) newTransform.setX3dBboxCenter(Util.splitStringToTuple3f(new Point3f(), valueString));
        valueString = atts.getValue("bboxSize");
        if (valueString != null) newTransform.setX3dBboxSize(Util.splitStringToTuple3f(new Vector3f(), valueString));
        return newTransform;
    }

    @Override
    public void exportImpl(Leaf node, X3DExport export, Element parentElement) throws IOException {
    }

    /**
	 * Decompose a transformation matrix (translation, rotation, scale, scaleOrientation (not yet)
	 * and returns a x3d transform element with corresponding attributes.
	 * @param transMatrix
	 * @return
	 */
    public static Element handleTransformation(Matrix4d transMatrix) {
        return handleTransformation(transMatrix, false, null, null);
    }

    /**
	 * Decompose a transformation matrix (translation, rotation, scale, scaleOrientation (not yet)
	 * and returns a x3d transform element with corresponding attributes.
	 * If isAxis is true, node is a groimp axis node with a different center.
	 * @param transMatrix
	 * @param isAxis
	 * @param node
	 * @return
	 */
    public static Element handleTransformation(Matrix4d transMatrix, boolean isAxis, Node node, GraphState gs) {
        Element element = X3DExport.getTheExport().getDoc().createElement("Transform");
        Matrix4f tm = new Matrix4f(transMatrix);
        Vector3f trans = new Vector3f();
        tm.get(trans);
        if (isAxis) {
            trans.z += gs.getDouble(node, true, de.grogra.imp.objects.Attributes.LENGTH) / 2f;
        }
        Quat4f rot = new Quat4f();
        tm.get(rot);
        AxisAngle4f arot = new AxisAngle4f();
        arot.set(rot);
        float scale = tm.getScale();
        if (!trans.equals(new Vector3f(0, 0, 0))) element.setAttribute("translation", trans.x + " " + trans.z + " " + -trans.y);
        if (arot.angle != 0) element.setAttribute("rotation", arot.x + " " + arot.z + " " + -arot.y + " " + arot.angle);
        if (scale != 1) element.setAttribute("scale", scale + " " + scale + " " + scale);
        return element;
    }
}
