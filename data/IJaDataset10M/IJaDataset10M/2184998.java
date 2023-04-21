package engine.graphics.synthesis.texture;

import engine.base.Vector3;
import engine.base.Vector4;
import engine.parameters.AbstractParam;
import engine.parameters.BoolParam;
import engine.parameters.ColorGradientParam;
import engine.parameters.FloatParam;
import engine.parameters.IntParam;

public final class PatternPerlinNoise extends Pattern {

    public String getName() {
        return "Perlin Noise";
    }

    public String getHelpText() {
        return "An implementation of Improved Perlin Noise. \n" + "See (http://mrl.nyu.edu/~perlin/noise/).\n" + "A seed of -1 uses the reference permutation.";
    }

    ColorGradientParam colorGradientParam = CreateLocalColorGradientParam("Color Mapping");

    final Noise3D_ImprovedPerlin noise = new Noise3D_ImprovedPerlin(1, -1);

    FloatParam scaleX;

    FloatParam scaleY;

    FloatParam persistence;

    FloatParam valueScale;

    IntParam startBand;

    IntParam endBand;

    IntParam seed;

    BoolParam periodic;

    public PatternPerlinNoise() {
        scaleX = CreateLocalFloatParam("ScaleX", 1.0f, 1.0f, Float.MAX_VALUE);
        scaleY = CreateLocalFloatParam("ScaleY", 1.0f, 1.0f, Float.MAX_VALUE);
        valueScale = CreateLocalFloatParam("ValueScale", 1.0f, 0.0f, Float.MAX_VALUE);
        valueScale.setDefaultIncrement(0.125f);
        persistence = CreateLocalFloatParam("Persistence", 0.5f, 0.0f, Float.MAX_VALUE);
        persistence.setDefaultIncrement(0.125f / 2.0f);
        startBand = CreateLocalIntParam("StartBand", 0, 1, 16);
        endBand = CreateLocalIntParam("EndBand", 8, 1, 16);
        seed = CreateLocalIntParam("Seed", -1, -1, Integer.MAX_VALUE);
        periodic = CreateLocalBoolParam("Periodic", true);
    }

    public PatternPerlinNoise(float sx, float sy) {
        this();
        scaleX.set(sx);
        scaleY.set(sy);
    }

    public void parameterChanged(AbstractParam source) {
        if (source == null || source == seed) {
            noise.setSeed(seed.get());
        }
        super.parameterChanged(source);
    }

    public Vector4 _valueRGBA(float u, float v) {
        float val = 0.0f;
        float mult = 1.0f;
        float freq = 1.0f;
        for (int i = 1; i < startBand.get(); i++) {
            freq *= 2.0f;
        }
        for (int i = startBand.get(); i < endBand.get(); i++) {
            float valueAdd = 0.0f;
            if (periodic.get()) valueAdd = noise.sample3dPeriodic(new Vector3(u * freq * scaleX.get(), v * freq * scaleY.get(), 0.0f), (int) (freq * scaleX.get()), (int) (freq * scaleY.get()), 256) * mult; else valueAdd = noise.sample(new Vector3(u * freq * scaleX.get(), v * freq * scaleY.get(), 0.0f)) * mult;
            val += valueAdd;
            freq *= 2.0f;
            mult *= persistence.get();
        }
        val = val * 0.5f + 0.5f;
        val *= valueScale.get();
        if (val > 1.0f) val = 1.0f;
        if (val < 0.0f) val = 0.0f;
        return colorGradientParam.get().getColor(val);
    }
}
