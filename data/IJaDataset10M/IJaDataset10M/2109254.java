package cmjTracer.raytracer.objects;

import java.util.Map;
import cmjTracer.bvh.BoundingSphere;
import cmjTracer.bvh.BoundingVolume;
import cmjTracer.color.Color3d;
import cmjTracer.loader.InitializationException;
import cmjTracer.loader.LoadableType;
import cmjTracer.math.Vec3;
import cmjTracer.raytracer.basic.Ray;
import cmjTracer.raytracer.shaders.LightSphereShader;
import cmjTracer.raytracer.shaders.Shader;

public class LightSphere implements Shape, LoadableType {

    Vec3 center;

    public double r;

    Shader shader;

    public Color3d color;

    /**
	 * The default constructor should only be used when dynamically instantiating
	 * a LightSphere object with {@link #initialize(Map)}.
	 */
    public LightSphere() {
    }

    public LightSphere(Vec3 center_, double r_, Color3d color) {
        this.color = color;
        center = new Vec3(center_);
        r = r_;
        shader = new LightSphereShader(this);
    }

    public Vec3 getNormal(Vec3 point) {
        Vec3 normal = new Vec3(point);
        normal.sub(center);
        return normal;
    }

    public Shader getShader() {
        return shader;
    }

    public boolean intersect(Ray ray) {
        Vec3 ominusc = new Vec3(ray.org);
        ominusc.sub(center);
        double ddotominusc = ray.dir.dot(ominusc);
        double ddotd = ray.dir.lengthSquared();
        double toBeRooted = (ddotominusc * ddotominusc) / (ddotd * ddotd) - (ominusc.lengthSquared() - r * r) / ddotd;
        if (toBeRooted < 0) return false;
        double minusp = -ddotominusc / ddotd;
        double root = Math.sqrt(toBeRooted);
        double t = minusp - root;
        if (t < Ray.EPSILON || t > ray.length) {
            t = minusp + root;
            if (t < Ray.EPSILON || t > ray.length) return false;
        }
        ray.length = t;
        ray.setHit(this);
        return true;
    }

    public boolean occlude(Ray ray) {
        return false;
    }

    public Vec3 getMax() {
        return new Vec3(center.x + r, center.y + r, center.z + r);
    }

    public Vec3 getMin() {
        return new Vec3(center.x - r, center.y - r, center.z - r);
    }

    public Vec3 getPosition() {
        return center;
    }

    public void setPosition(Vec3 position) {
        center.set(position);
    }

    public BoundingVolume getBoundingVolume() {
        return new BoundingSphere(center, r);
    }

    /**
	 * Initialises this sphere. The parameter map should contain the following 
	 * values:
	 * <ul>
	 * <li><b>position</b> - Vector3d</li>
	 * <li><b>radius</b> - Double</li>
	 * <li><b>color</b> - Color3d</li>
	 * </ul>
	 */
    public void initialize(Map<String, Object> parameters) throws InitializationException {
        if (!(parameters.containsKey("position") && parameters.containsKey("radius") && parameters.containsKey("color"))) throw new InitializationException(InitializationException.PARAMETER_MISSING, "LightSphere");
        if (parameters.get("position") instanceof Vec3) {
            this.center = new Vec3((Vec3) parameters.get("position"));
        } else {
            throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "LightSphere");
        }
        if (parameters.get("radius") instanceof Double) {
            this.r = ((Double) parameters.get("radius")).doubleValue();
        } else {
            throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "LightSphere");
        }
        if (parameters.get("color") instanceof Color3d) {
            this.color = ((Color3d) parameters.get("color"));
        } else {
            throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "LightSphere");
        }
        shader = new LightSphereShader(this);
    }
}

;
