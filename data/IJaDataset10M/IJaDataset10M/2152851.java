package org.jrman.render;

import javax.vecmath.Color4f;
import org.jrman.util.Calc;

public class Sample {

    private float colorRed;

    private float colorGreen;

    private float colorBlue;

    private float opacityRed;

    private float opacityGreen;

    private float opacityBlue;

    private float z;

    private boolean opaque;

    public Sample(float cRed, float cGreen, float cBlue, float oRed, float oGreen, float oBlue, float z) {
        colorRed = cRed;
        colorGreen = cGreen;
        colorBlue = cBlue;
        opacityRed = oRed;
        opacityGreen = oGreen;
        opacityBlue = oBlue;
        this.z = z;
        opaque = (opacityRed == 1f && opacityGreen == 1f && opacityBlue == 1f);
    }

    public boolean behind(Sample sample) {
        return z > sample.z;
    }

    public boolean behind(float otherZ) {
        return z > otherZ;
    }

    public boolean isOpaque() {
        return opaque;
    }

    public void overlayOver(Color4f ocolor) {
        if (opaque) {
            ocolor.x = colorRed;
            ocolor.y = colorGreen;
            ocolor.z = colorBlue;
            ocolor.w = 1f;
        } else {
            ocolor.x = ocolor.x * (1f - opacityRed) + colorRed;
            ocolor.y = ocolor.y * (1f - opacityGreen) + colorGreen;
            ocolor.z = ocolor.z * (1f - opacityBlue) + colorBlue;
            float opacity = (opacityRed + opacityGreen + opacityBlue) / 3f;
            ocolor.w = Calc.min(1f, opacity + ocolor.w);
        }
    }

    public float getZ() {
        return z;
    }
}
