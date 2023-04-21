package de.grogra.imp3d.glsl.light;

import de.grogra.imp3d.glsl.utility.GLSLCollection;
import de.grogra.imp3d.glsl.utility.GLSLManagedShader;

/**
 * Interface for all GLSLLightShaders.
 * @author Konni Hartmann
 */
public class LightCollection {

    private static final GLSLCollection col = new GLSLCollection();

    public static GLSLManagedShader getGLSLManagedObject(Object inp) {
        return col.getGLSLManagedObject(inp);
    }

    public static void initMap() {
        col.AddToMap(new GLSLSpotLight());
        col.AddToMap(new GLSLPointLight());
        col.AddToMap(new GLSLDirectionalLight());
        col.AddToMap(new GLSLSkyReflectionLight());
        col.AddToMap(new GLSLSkyLight());
        col.AddToMap(new GLSLAreaLight());
    }
}
