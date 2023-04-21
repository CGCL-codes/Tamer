package net.sf.jtracer.core;

import net.sf.jtracer.objects.Sphere;
import net.sf.jtracer.util.Color;
import net.sf.jtracer.util.Vector3D;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class RayObjectIntersectionTest {

    @Test
    public void raySphereIntersect() {
        Vector3D rayOrigin = new Vector3D(1f, 1f, 1f);
        Vector3D rayDirection = new Vector3D(-1f, -1f, -1f);
        rayDirection.normalize();
        Vector3D sphereCenter = new Vector3D(0, -1, -1);
        Ray ray = new Ray(rayOrigin, rayDirection);
        Sphere sphere = new Sphere(sphereCenter, 0.5f, new Color());
        assertTrue(sphere.intersectsWithRay(ray));
    }

    @Test
    public void raySphereDoesntIntersect() {
        Vector3D rayOrigin = new Vector3D(1f, 1f, 1f);
        Vector3D rayDirection = new Vector3D(1f, 0f, 0f);
        Vector3D sphereCenter = new Vector3D();
        Ray ray = new Ray(rayOrigin, rayDirection);
        Sphere sphere = new Sphere(sphereCenter, 0.5f, new Color());
        assertTrue(!sphere.intersectsWithRay(ray));
    }
}
