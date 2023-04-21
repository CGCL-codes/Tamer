package de.grogra.imp3d.glsl.light;

import javax.vecmath.Vector3f;
import de.grogra.imp3d.objects.DirectionalLight;
import de.grogra.math.RGBColor;

public class SunSkyToDirectionalLightWrapper extends DirectionalLight {

    RGBColor curcol = new RGBColor();

    public void setCurcol(Vector3f in) {
        this.curcol.x = in.x;
        this.curcol.y = in.y;
        this.curcol.z = in.z;
    }

    @Override
    public RGBColor getColor() {
        return curcol;
    }
}
