package cmjTracer.raytracer.shaders;

import java.util.Map;
import cmjTracer.color.Color3d;
import cmjTracer.loader.InitializationException;
import cmjTracer.loader.LoadableType;
import cmjTracer.math.Vec3;
import cmjTracer.raytracer.basic.Intersection;
import cmjTracer.raytracer.textures.Texture2D;

public class CylindricalTextureShader implements Shader, LoadableType {

    private Texture2D texture;

    private Vec3 a, n, rot;

    private double length;

    /**
	 * The default constructor should only be used when dynamically instantiating
	 * a CylindricalTextureShader with {@link #initialize(Map)}.
	 */
    public CylindricalTextureShader() {
    }

    public CylindricalTextureShader(Vec3 a, Vec3 b, Vec3 rot, Texture2D texture) {
        this.texture = texture;
        this.a = a;
        n = new Vec3(b);
        n.sub(a);
        length = n.length();
        Vec3 temp = new Vec3();
        temp.cross(n, rot);
        this.rot = new Vec3();
        this.rot.cross(temp, n);
        this.rot.normalize();
        n.normalize();
    }

    public Color3d shade(Intersection intersection) {
        double u, v;
        Vec3 projected = new Vec3(n);
        Vec3 point = new Vec3(intersection.getPoint());
        point.sub(a);
        u = point.dot(projected);
        projected.scale(u);
        point.sub(projected);
        point.normalize();
        Vec3 c = new Vec3();
        c.cross(point, rot);
        v = n.dot(c);
        if (v < 0) {
            v = -Math.acos(point.dot(rot)) / (2 * Math.PI);
        } else {
            v = Math.acos(point.dot(rot)) / (2 * Math.PI);
        }
        return texture.getColor(v, u / length);
    }

    /**
	 * Dynamically initializes this CylindricalTextureShader instance. The parameter
	 * map must contain the following:
	 * <ul>
	 * <li><b>a</b> - Vector3d</li>
	 * <li><b>b</b> - Vector3d</li>
	 * <li><b>rotation</b> - Vector3d</li>
	 * <li><b>texture</b> - Texture2D</li>
	 * </ul>
	 */
    public void initialize(Map<String, Object> parameters) throws InitializationException {
        if (!(parameters.containsKey("a") && parameters.containsKey("b") && parameters.containsKey("rotation") && parameters.containsKey("texture"))) throw new InitializationException(InitializationException.PARAMETER_MISSING, "CylindricalTextureShader");
        if (parameters.get("a") instanceof Vec3) a = new Vec3((Vec3) parameters.get("a")); else throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "CylindricalTextureShader");
        Vec3 b;
        if (parameters.get("b") instanceof Vec3) b = new Vec3((Vec3) parameters.get("b")); else throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "CylindricalTextureShader");
        Vec3 rot;
        if (parameters.get("rotation") instanceof Vec3) rot = new Vec3((Vec3) parameters.get("rotation")); else throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "CylindricalTextureShader");
        n = new Vec3(b);
        n.sub(a);
        length = n.length();
        Vec3 temp = new Vec3();
        temp.cross(n, rot);
        this.rot = new Vec3();
        this.rot.cross(temp, n);
        this.rot.normalize();
        n.normalize();
        if (parameters.get("texture") instanceof Texture2D) texture = (Texture2D) parameters.get("texture"); else throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "CylindricalTextureShader");
    }
}
