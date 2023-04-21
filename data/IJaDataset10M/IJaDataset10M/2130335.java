package com.jme3.scene.plugins.blender.textures.generating;

import com.jme3.math.FastMath;
import com.jme3.scene.plugins.blender.BlenderContext;
import com.jme3.scene.plugins.blender.file.Structure;
import com.jme3.scene.plugins.blender.textures.TexturePixel;
import com.jme3.texture.Image.Format;

/**
 * This class generates the 'clouds' texture.
 * @author Marcin Roguski (Kaelthas)
 */
public class TextureGeneratorClouds extends TextureGenerator {

    protected static final int TEX_NOISESOFT = 0;

    protected static final int TEX_NOISEPERL = 1;

    protected static final int TEX_DEFAULT = 0;

    protected static final int TEX_COLOR = 1;

    protected float noisesize;

    protected int noiseDepth;

    protected int noiseBasis;

    protected int noiseType;

    protected boolean isHard;

    protected int sType;

    /**
	 * Constructor stores the given noise generator.
	 * @param noiseGenerator
	 *        the noise generator
	 */
    public TextureGeneratorClouds(NoiseGenerator noiseGenerator) {
        super(noiseGenerator, Format.Luminance8);
    }

    @Override
    public void readData(Structure tex, BlenderContext blenderContext) {
        super.readData(tex, blenderContext);
        noisesize = ((Number) tex.getFieldValue("noisesize")).floatValue();
        noiseDepth = ((Number) tex.getFieldValue("noisedepth")).intValue();
        noiseBasis = ((Number) tex.getFieldValue("noisebasis")).intValue();
        noiseType = ((Number) tex.getFieldValue("noisetype")).intValue();
        isHard = noiseType != TEX_NOISESOFT;
        sType = ((Number) tex.getFieldValue("stype")).intValue();
        if (sType == TEX_COLOR) {
            this.imageFormat = Format.RGBA8;
        }
    }

    @Override
    public void getPixel(TexturePixel pixel, float x, float y, float z) {
        pixel.intensity = NoiseGenerator.NoiseFunctions.turbulence(x, y, z, noisesize, noiseDepth, noiseBasis, isHard);
        pixel.intensity = FastMath.clamp(pixel.intensity, 0.0f, 1.0f);
        if (colorBand != null) {
            int colorbandIndex = (int) (pixel.intensity * 1000.0f);
            pixel.red = colorBand[colorbandIndex][0];
            pixel.green = colorBand[colorbandIndex][1];
            pixel.blue = colorBand[colorbandIndex][2];
            pixel.alpha = colorBand[colorbandIndex][3];
            this.applyBrightnessAndContrast(bacd, pixel);
        } else if (sType == TEX_COLOR) {
            pixel.red = pixel.intensity;
            pixel.green = NoiseGenerator.NoiseFunctions.turbulence(y, x, z, noisesize, noiseDepth, noiseBasis, isHard);
            pixel.blue = NoiseGenerator.NoiseFunctions.turbulence(y, z, x, noisesize, noiseDepth, noiseBasis, isHard);
            pixel.green = FastMath.clamp(pixel.green, 0.0f, 1.0f);
            pixel.blue = FastMath.clamp(pixel.blue, 0.0f, 1.0f);
            pixel.alpha = 1.0f;
            this.applyBrightnessAndContrast(bacd, pixel);
        } else {
            this.applyBrightnessAndContrast(pixel, bacd.contrast, bacd.brightness);
        }
    }
}
