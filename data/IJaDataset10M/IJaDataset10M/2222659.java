package architetris;

import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Material;
import javax.media.j3d.Texture;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

public class Circle extends BranchGroup {

    public int booleanOperation;

    public Material material = new Material();

    public TransformGroup transformGroup = new TransformGroup();

    public static Texture tex_circle;

    Point3d hotPoint = new Point3d(0.5, 0.5, 0);

    static {
        tex_circle = new TextureLoader("textures/circle.gif", ArchiTetris.mainFrame).getTexture();
    }

    public Circle(Transform3D transform) {
        transformGroup.setTransform(transform);
        transform.transform(hotPoint);
        Point3d p[] = { new Point3d(0, 0, 0), new Point3d(1, 0, 0), new Point3d(1, 1, 0), new Point3d(0, 1, 0) };
        transformGroup.addChild(new TexturedQuad(p, new Vector3f(0, 0, 1), tex_circle, material));
        addChild(transformGroup);
    }
}
