package artofillusion.texture;

import artofillusion.*;
import artofillusion.math.*;

/** Linear2DTriangle is a subclass of RenderingTriangle, which represents a triangle whose
    properties are defined by a linear mapping of a Texture2D. */
public class Linear2DTriangle extends RenderingTriangle {

    double x1, x2, x3, y1, y2, y3;

    boolean bumpMapped;

    public Linear2DTriangle(int v1, int v2, int v3, int n1, int n2, int n3, double t1x, double t1y, double t2x, double t2y, double t3x, double t3y) {
        super(v1, v2, v3, n1, n2, n3);
        x1 = t1x;
        y1 = t1y;
        x2 = t2x;
        y2 = t2y;
        x3 = t3x;
        y3 = t3y;
    }

    /** Set the mesh that this triangle is part of.  This is automatically called when the
      triangle is added to the mesh.
      @param mesh      the RenderingMesh this triangle belongs to
      @param index     the index of this triangle within the mesh
  */
    public void setMesh(RenderingMesh mesh, int index) {
        super.setMesh(mesh, index);
        bumpMapped = theMesh.mapping.getTexture().hasComponent(Texture.BUMP_COMPONENT);
    }

    public void getTextureSpec(TextureSpec spec, double angle, double u, double v, double w, double size, double time) {
        ProjectionMapping map = (ProjectionMapping) theMesh.mapping;
        double s, t;
        if (!map.appliesToFace(angle > 0.0)) {
            spec.diffuse.setRGB(0.0f, 0.0f, 0.0f);
            spec.specular.setRGB(0.0f, 0.0f, 0.0f);
            spec.transparent.setRGB(1.0f, 1.0f, 1.0f);
            spec.emissive.setRGB(0.0f, 0.0f, 0.0f);
            spec.roughness = spec.cloudiness = 0.0;
            spec.bumpGrad.set(0.0, 0.0, 0.0);
            return;
        }
        ((Texture2D) map.getTexture()).getTextureSpec(spec, x1 * u + x2 * v + x3 * w, y1 * u + y2 * v + y3 * w, size * map.matScaleX, size * map.matScaleY, angle, time, getParameters(u, v, w));
        if (bumpMapped) {
            s = spec.bumpGrad.x;
            t = spec.bumpGrad.y;
            spec.bumpGrad.set(s * map.ax + t * map.ay, s * map.bx + t * map.by, s * map.cx + t * map.cy);
        }
    }

    public void getTransparency(RGBColor trans, double angle, double u, double v, double w, double size, double time) {
        ProjectionMapping map = (ProjectionMapping) theMesh.mapping;
        if (!map.appliesToFace(angle > 0.0)) {
            trans.setRGB(1.0f, 1.0f, 1.0f);
            return;
        }
        ((Texture2D) map.getTexture()).getTransparency(trans, x1 * u + x2 * v + x3 * w, y1 * u + y2 * v + y3 * w, size * map.matScaleX, size * map.matScaleY, angle, time, getParameters(u, v, w));
    }

    public double getDisplacement(double u, double v, double w, double size, double time) {
        ProjectionMapping map = (ProjectionMapping) theMesh.mapping;
        return ((Texture2D) map.getTexture()).getDisplacement(x1 * u + x2 * v + x3 * w, y1 * u + y2 * v + y3 * w, size * map.matScaleX, size * map.matScaleY, time, getParameters(u, v, w));
    }
}
