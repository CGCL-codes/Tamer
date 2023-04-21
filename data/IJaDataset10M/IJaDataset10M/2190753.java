package org.thenesis.planetino2.math3D;

/**
 A PointLight3D is a point light that has an intensity
 (between 0 and 1) and optionally a distance falloff value,
 which causes the light to diminish with distance.
 */
public class PointLight3D extends Vector3D {

    public static final float NO_DISTANCE_FALLOFF = -1;

    private float intensity;

    private float distanceFalloff;

    /**
	 Creates a new PointLight3D at (0,0,0) with an intensity
	 of 1 and no distance falloff.
	 */
    public PointLight3D() {
        this(0, 0, 0, 1, NO_DISTANCE_FALLOFF);
    }

    /**
	 Creates a copy of the specified PointLight3D.
	 */
    public PointLight3D(PointLight3D p) {
        setTo(p);
    }

    /**
	 Creates a new PointLight3D with the specified location
	 and intensity. The created light has no distance falloff.
	 */
    public PointLight3D(float x, float y, float z, float intensity) {
        this(x, y, z, intensity, NO_DISTANCE_FALLOFF);
    }

    /**
	 Creates a new PointLight3D with the specified location.
	 intensity, and no distance falloff.
	 */
    public PointLight3D(float x, float y, float z, float intensity, float distanceFalloff) {
        setTo(x, y, z);
        setIntensity(intensity);
        setDistanceFalloff(distanceFalloff);
    }

    /**
	 Sets this PointLight3D to the same location, intensity,
	 and distance falloff as the specified PointLight3D.
	 */
    public void setTo(PointLight3D p) {
        setTo(p.x, p.y, p.z);
        setIntensity(p.getIntensity());
        setDistanceFalloff(p.getDistanceFalloff());
    }

    /**
	 Gets the intensity of this light from the specified
	 distance.
	 */
    public float getIntensity(float distance) {
        if (distanceFalloff == NO_DISTANCE_FALLOFF) {
            return intensity;
        } else if (distance >= distanceFalloff) {
            return 0;
        } else {
            return intensity * (distanceFalloff - distance) / (distanceFalloff + distance);
        }
    }

    /**
	 Gets the intensity of this light.
	 */
    public float getIntensity() {
        return intensity;
    }

    /**
	 Sets the intensity of this light.
	 */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    /**
	 Gets the distances falloff value. The light intensity is
	 zero beyond this distance.
	 */
    public float getDistanceFalloff() {
        return distanceFalloff;
    }

    /**
	 Sets the distances falloff value. The light intensity is
	 zero beyond this distance. Set to NO_DISTANCE_FALLOFF if
	 the light does not diminish with distance.
	 */
    public void setDistanceFalloff(float distanceFalloff) {
        this.distanceFalloff = distanceFalloff;
    }
}
