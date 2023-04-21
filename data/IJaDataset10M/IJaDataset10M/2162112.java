package net.sf.jawp.gui.client.semi3d;

/**
 * 
 * @author from javaworld
 * @version $Revision: 1.3 $
 * 
 */
public class Sphere implements Obj {

    Texture texture;

    Light light;

    double sphereZ, sphereR;

    double c;

    public Sphere(final double sphereZ, final double sphereR, final Texture texture, final Light light) {
        this.sphereZ = sphereZ;
        this.sphereR = sphereR;
        this.texture = texture;
        this.light = light;
        c = sphereZ * sphereZ - sphereR * sphereR;
    }

    public RGB getIntersection(final Vec ray) {
        final double a = ray.x * ray.x + ray.y * ray.y + ray.z * ray.z;
        final double b = -2.0 * sphereZ * ray.z;
        final double tmp = b * b - 4.0 * a * c;
        if (tmp < 0) {
            return new RGB(0.0, 0.0, 0.0);
        } else {
            final double t = (-b - Math.sqrt(tmp)) / 2.0 / a;
            final Loc intersect = new Loc(ray.x * t, ray.y * t, ray.z * t);
            final double j = Math.asin(intersect.y / sphereR);
            final double i = Math.acos(intersect.x / sphereR / Math.cos(j));
            final RGB rgb = texture.getTexel(i / Math.PI, j / Math.PI + 0.5);
            final Vec norm = new Vec(intersect.x, intersect.y, intersect.z - sphereZ);
            final double length = norm.getLength();
            norm.scale(1.0 / length);
            final Vec dir = light.getDirection(intersect);
            final double coincidence = -norm.dot(dir);
            final double lighting = 0.25 + 0.75 * ((coincidence >= 0) ? coincidence : 0.0);
            rgb.scale(lighting);
            return rgb;
        }
    }
}
