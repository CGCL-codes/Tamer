package bitWave.geometry.impl;

import java.awt.Color;
import bitWave.geometry.Geometry;
import bitWave.geometry.GeometryFactory;
import bitWave.geometry.Ray;
import bitWave.geometry.Sphere;
import bitWave.geometry.impl.util.GeometricIntersections;
import bitWave.linAlg.Matrix4;
import bitWave.linAlg.Vec4;

/**
 * Implementation of a sphere.
 * 
 * @author fw
 */
public class SphereImpl extends GeometryImpl implements Sphere {

    private Color m_color;

    private Matrix4 m_transform;

    /**
     * Creates a sphere for the given transformation matrix.
     * @param transform
     */
    SphereImpl(GeometryFactory factory, Color color, Matrix4 transform) {
        super(factory);
        this.m_color = color;
        this.m_transform = transform;
    }

    public double getVolume() {
        double[] v = this.m_transform.getValues();
        return (4 / 3) * Math.PI * v[0] * v[5] * v[10];
    }

    /**
	 * The inertia matrix of an ellipsoid is is given by:
     * Ixx = m*((b^2+c^2)/5) 
     * Iyy = m*((c^2+a^2)/5) 
     * Izz = m*((a^2+b^2)/5) 
     * @param inertia
     *            The matrix that will hold the inertia tensor.
     * @param centerOfMass
     *            The vector that will hold the center of mass.
     * @param density
     *            The density of the geometry, which is assumed to be uniform.
     * @return The total mass of the body.
	 */
    public double getMassProperties(Matrix4 inertia, Vec4 centerOfMass, double density) {
        double mass = getVolume() * density;
        double[] tv = this.m_transform.getValues();
        double a2 = tv[0] * tv[0];
        double b2 = tv[5] * tv[5];
        double c2 = tv[10] * tv[10];
        double[] v = inertia.getValues();
        v[0] = mass * ((b2 + c2) / 5);
        v[5] = mass * ((c2 + a2) / 5);
        v[10] = mass * ((a2 + b2) / 5);
        v[15] = 1;
        return mass;
    }

    public Geometry intersectWith(Geometry other) {
        if (other instanceof Ray) return GeometricIntersections.intersectRayWithSphere((Ray) other, this); else throw new RuntimeException("Unsupported intersection");
    }

    public Geometry transform(Matrix4 transform) {
        Matrix4 nt = transform.getFactory().multiplyMatrices(this.m_transform, transform);
        return new SphereImpl(this.m_factory, this.m_color, nt);
    }

    public Matrix4 getTransform() {
        return this.m_transform;
    }

    @Override
    public Geometry clone() throws CloneNotSupportedException {
        return new SphereImpl(this.m_factory, new Color(this.m_color.getRGB()), this.m_transform.clone());
    }
}
