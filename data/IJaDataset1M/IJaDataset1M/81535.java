package edu.gsbme.yakitori.Renderer.draw;

import java.awt.Color;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import com.sun.j3d.utils.geometry.Sphere;
import edu.gsbme.geometrykernel.data.dim0.point;
import edu.gsbme.yakitori.Algorithm.ScalingController;

/**
 * Point drawer
 * @author David
 *
 */
public class PointDrawer {

    public static TransformGroup drawPoint(point pt, float r, Color color, double scaleFactor) {
        TransformGroup sphTran = new TransformGroup();
        Transform3D position = new Transform3D();
        Vector3d coord = new Vector3d(ScalingController.convertCoordinate(pt.getX(), scaleFactor), ScalingController.convertCoordinate(pt.getY(), scaleFactor), ScalingController.convertCoordinate(pt.getZ(), scaleFactor));
        position.setTranslation(coord);
        Appearance appr = new Appearance();
        appr.setColoringAttributes(new ColoringAttributes(new Color3f(color), ColoringAttributes.NICEST));
        Sphere vtx = new Sphere(r, appr);
        sphTran.addChild(vtx);
        sphTran.setTransform(position);
        vtx.getShape().getGeometry().setName(pt.getID());
        return sphTran;
    }

    public static TransformGroup drawPoint(Sphere vtx, double x, double y, double z, double scaleFactor) {
        TransformGroup sphTran = new TransformGroup();
        Transform3D position = new Transform3D();
        x = ScalingController.convertCoordinate(x, scaleFactor);
        y = ScalingController.convertCoordinate(y, scaleFactor);
        z = ScalingController.convertCoordinate(z, scaleFactor);
        Vector3d vect = new Vector3d(x, y, z);
        position.setTranslation(vect);
        sphTran.addChild(vtx);
        sphTran.setTransform(position);
        return sphTran;
    }

    public static TransformGroup drawPoint(Sphere vtx, double x, double y, double z) {
        TransformGroup sphTran = new TransformGroup();
        Transform3D position = new Transform3D();
        Vector3d vect = new Vector3d(x, y, z);
        position.setTranslation(vect);
        sphTran.addChild(vtx);
        sphTran.setTransform(position);
        return sphTran;
    }

    public static TransformGroup drawPoint(Sphere vtx, Point3d pt, String id, double scaleFactor) {
        TransformGroup sphTran = new TransformGroup();
        Transform3D position = new Transform3D();
        Vector3d coord = new Vector3d(ScalingController.convertCoordinate(pt.getX(), scaleFactor), ScalingController.convertCoordinate(pt.getY(), scaleFactor), ScalingController.convertCoordinate(pt.getZ(), scaleFactor));
        position.setTranslation(coord);
        sphTran.addChild(vtx);
        sphTran.setTransform(position);
        vtx.getShape().getGeometry().setName(id);
        return sphTran;
    }
}
