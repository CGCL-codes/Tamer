package cmjTracer.raytracer.lights;

import java.util.Map;
import cmjTracer.color.Color3d;
import cmjTracer.loader.InitializationException;
import cmjTracer.loader.LoadableType;
import cmjTracer.math.Vec3;
import cmjTracer.raytracer.basic.Ray;
import cmjTracer.raytracer.basic.Scene;

public class PointLight implements Light, LoadableType {

    private Color3d color;

    private double attAbsolute, attLinear, attQuadratic;

    private Vec3 position;

    /**
	 * The default constructor should only be used when dynamically intialising
	 * a PointLight with {@link #initialize(Map)}.
	 *
	 */
    public PointLight() {
    }

    public PointLight(Vec3 position, double red, double green, double blue, double attAbsolute, double attLinear, double attQuadratic) {
        this.position = position;
        this.color = new Color3d(red, green, blue);
        this.attAbsolute = attAbsolute;
        this.attQuadratic = attQuadratic;
    }

    public boolean isIlluminated(Vec3 point) {
        return true;
    }

    public Color3d getIlluminance(Scene scene, Vec3 point) {
        Ray toLight = genRay(point);
        if (scene.occlude(toLight)) return Color3d.BLACK;
        Vec3 toPoint = new Vec3(point);
        toPoint.sub(position);
        double distSquared = toPoint.lengthSquared();
        double factor = 1 / (attAbsolute + attLinear * Math.sqrt(distSquared) + attQuadratic * distSquared);
        double r = color.getRedDouble() * factor;
        double g = color.getGreenDouble() * factor;
        double b = color.getBlueDouble() * factor;
        if (r > 1) r = 1;
        if (g > 1) g = 1;
        if (b > 1) b = 1;
        return new Color3d(r, g, b);
    }

    public Ray genRay(Vec3 org) {
        Vec3 dir = new Vec3(position);
        dir.sub(org);
        return new Ray(org, dir, 1);
    }

    /**
	 * Dynamically initialises this PointLight. The parameter map must contain
	 * the following:
	 * <ul>
	 * <li><b>position</b> - Vector3d</li>
	 * <li><b>color</b> - Color3d</li>
	 * <li><b>constantAttenuation</b> - Double</li>
	 * <li><b>linearAttenuation</b> - Double</li>
	 * <li><b>quadraticAttenuation</b> - Double</li>
	 * </ul>
	 */
    public void initialize(Map<String, Object> parameters) throws InitializationException {
        if (!(parameters.containsKey("position") && parameters.containsKey("color") && parameters.containsKey("constantAttenuation") && parameters.containsKey("linearAttenuation") && parameters.containsKey("quadraticAttenuation"))) throw new InitializationException(InitializationException.PARAMETER_MISSING, "PointLight");
        if (parameters.get("position") instanceof Vec3) this.position = new Vec3((Vec3) parameters.get("position")); else throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "PointLight");
        if (parameters.get("color") instanceof Color3d) this.color = (Color3d) parameters.get("color"); else throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "PointLight");
        if (parameters.get("constantAttenuation") instanceof Double) this.attAbsolute = (Double) parameters.get("constantAttenuation"); else throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "PointLight");
        if (parameters.get("linearAttenuation") instanceof Double) this.attLinear = (Double) parameters.get("linearAttenuation"); else throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "PointLight");
        if (parameters.get("quadraticAttenuation") instanceof Double) this.attQuadratic = (Double) parameters.get("quadraticAttenuation"); else throw new InitializationException(InitializationException.PARAMETER_WRONG_TYPE, "PointLight");
    }
}
