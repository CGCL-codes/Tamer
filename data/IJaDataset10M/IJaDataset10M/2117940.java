package de.grogra.ext.sunshine.spectral.shader;

import java.util.Random;
import javax.vecmath.Color3f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import org.sunflow.image.RGBSpace;
import org.sunflow.image.XYZColor;
import org.sunflow.math.MathUtils;
import de.grogra.ext.sunshine.spectral.SPDConversion;
import de.grogra.ext.sunshine.spectral.SpectralColors;
import de.grogra.ext.sunshine.spectral.SunshineRegularSpectralCurve;
import de.grogra.persistence.ShareableBase;
import de.grogra.ray.physics.Environment;
import de.grogra.ray.physics.Spectrum;
import de.grogra.ray.util.Ray;
import de.grogra.ray.util.RayList;

public class SunshineBlackBodySpectrumChannel extends ShareableBase implements SunshineChannel {

    float temperature = 6500f;

    int intColor = -1;

    SunshineRegularSpectralCurve curve;

    public int getAverageColor() {
        if (intColor == -1) calculateRGB();
        return intColor;
    }

    public Object getContent() {
        if (curve == null) computeCurve();
        return curve;
    }

    public int getFlags() {
        return 0;
    }

    public float computeBSDF(Environment env, Vector3f in, Spectrum specIn, Vector3f out, boolean adjoint, Spectrum bsdf) {
        return 0;
    }

    public void generateRandomRays(Environment env, Vector3f out, Spectrum specOut, RayList rays, boolean adjoint, Random random) {
    }

    public void computeMaxRays(Environment env, Vector3f in, Spectrum specIn, Ray reflected, Tuple3f refVariance, Ray transmitted, Tuple3f transVariance) {
    }

    public boolean isTransparent() {
        return false;
    }

    public void shade(Environment env, RayList in, Vector3f out, Spectrum specOut, Tuple3d color) {
    }

    public void setTemperature(float temp) {
        temperature = temp;
        computeCurve();
    }

    private void computeCurve() {
        int nWavelenghts = SPDConversion.LAMBDA_MAX - SPDConversion.LAMBDA_MIN;
        float[] values = new float[nWavelenghts];
        for (int w = SPDConversion.LAMBDA_MIN, i = 0; w < SPDConversion.LAMBDA_MAX; w++, i++) {
            values[i] = SpectralColors.bb_spectrum(w, temperature);
        }
        curve = new SunshineRegularSpectralCurve(values, SPDConversion.LAMBDA_MIN, SPDConversion.LAMBDA_MAX);
        curve.normalize();
    }

    private static int f2i(float f) {
        int i = Math.round(f * 255);
        return (i < 0) ? 0 : (i > 255) ? 255 : i;
    }

    private void calculateRGB() {
        if (curve == null) computeCurve();
        Color3f color = new Color3f();
        XYZColor XYZcol = curve.toXYZBB();
        XYZcol.mul(SpectralColors.getK(SpectralColors.DISPLAY_WHITE_POINT_BBTEMP));
        RGBSpace.SRGB.convertXYZtoRGB(XYZcol, color);
        float m = -MathUtils.min(color.x, color.y, color.z);
        if (m > 0) {
            color.x += m;
            color.y += m;
            color.z += m;
        }
        color.scale((1.f / 3.f));
        intColor = (f2i(color.x) << 16) + (f2i(color.y) << 8) + f2i(color.z) + (f2i(1) << 24);
    }

    public static final Type $TYPE;

    public static final Type.Field temperature$FIELD;

    public static class Type extends de.grogra.persistence.SCOType {

        public Type(Class c, de.grogra.persistence.SCOType supertype) {
            super(c, supertype);
        }

        public Type(SunshineBlackBodySpectrumChannel representative, de.grogra.persistence.SCOType supertype) {
            super(representative, supertype);
        }

        Type(Class c) {
            super(c, de.grogra.persistence.SCOType.$TYPE);
        }

        private static final int SUPER_FIELD_COUNT = de.grogra.persistence.SCOType.FIELD_COUNT;

        protected static final int FIELD_COUNT = de.grogra.persistence.SCOType.FIELD_COUNT + 1;

        static Field _addManagedField(Type t, String name, int modifiers, de.grogra.reflect.Type type, de.grogra.reflect.Type componentType, int id) {
            return t.addManagedField(name, modifiers, type, componentType, id);
        }

        @Override
        protected void setFloat(Object o, int id, float value) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    ((SunshineBlackBodySpectrumChannel) o).setTemperature((float) value);
                    return;
            }
            super.setFloat(o, id, value);
        }

        @Override
        protected float getFloat(Object o, int id) {
            switch(id) {
                case Type.SUPER_FIELD_COUNT + 0:
                    return ((SunshineBlackBodySpectrumChannel) o).temperature;
            }
            return super.getFloat(o, id);
        }

        @Override
        public Object newInstance() {
            return new SunshineBlackBodySpectrumChannel();
        }
    }

    public de.grogra.persistence.ManageableType getManageableType() {
        return $TYPE;
    }

    static {
        $TYPE = new Type(SunshineBlackBodySpectrumChannel.class);
        temperature$FIELD = Type._addManagedField($TYPE, "temperature", 0 | Type.Field.SCO, de.grogra.reflect.Type.FLOAT, null, Type.SUPER_FIELD_COUNT + 0);
        temperature$FIELD.setQuantity(de.grogra.util.Quantity.TEMPERATURE);
        $TYPE.validate();
    }
}
