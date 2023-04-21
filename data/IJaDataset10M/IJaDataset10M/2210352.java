package org.jdesktop.j3d.examples.alternate_appearance;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;

public class SphereGroup extends Group {

    Shape3D[] shapes;

    int numShapes = 0;

    public SphereGroup() {
        this(0.25f, 0.75f, 0.75f, 5, 5, null, false);
    }

    public SphereGroup(Appearance app) {
        this(0.25f, 0.75f, 0.75f, 5, 5, app, false);
    }

    public SphereGroup(float radius, float xSpacing, float ySpacing, int xCount, int yCount, boolean overrideflag) {
        this(radius, xSpacing, ySpacing, xCount, yCount, null, overrideflag);
    }

    public SphereGroup(float radius, float xSpacing, float ySpacing, int xCount, int yCount, Appearance app, boolean overrideflag) {
        if (app == null) {
            app = new Appearance();
            Material material = new Material();
            material.setDiffuseColor(new Color3f(0.8f, 0.8f, 0.8f));
            material.setSpecularColor(new Color3f(0.0f, 0.0f, 0.0f));
            material.setShininess(0.0f);
            app.setMaterial(material);
        }
        double xStart = -xSpacing * (double) (xCount - 1) / 2.0;
        double yStart = -ySpacing * (double) (yCount - 1) / 2.0;
        Sphere sphere = null;
        TransformGroup trans = null;
        Transform3D t3d = new Transform3D();
        Vector3d vec = new Vector3d();
        double x, y = yStart, z = 0.0;
        shapes = new Shape3D[xCount * yCount];
        for (int i = 0; i < yCount; i++) {
            x = xStart;
            for (int j = 0; j < xCount; j++) {
                vec.set(x, y, z);
                t3d.setTranslation(vec);
                trans = new TransformGroup(t3d);
                addChild(trans);
                sphere = new Sphere(radius, Primitive.GENERATE_NORMALS, 16, app);
                trans.addChild(sphere);
                x += xSpacing;
                shapes[numShapes] = sphere.getShape();
                if (overrideflag) shapes[numShapes].setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
                numShapes++;
            }
            y += ySpacing;
        }
    }

    Shape3D[] getShapes() {
        return shapes;
    }
}
