package com.sun.pdfview.colorspace;

import java.awt.color.ColorSpace;
import java.io.IOException;
import com.sun.pdfview.PDFObject;

/**
 * A ColorSpace for Lab color
 * @author Mike Wessler
 */
public class LabColor extends ColorSpace {

    float white[] = { 1f, 1f, 1f };

    float black[] = { 0, 0, 0 };

    float range[] = { -100f, 100f, -100f, 100f };

    static ColorSpace cie = ColorSpace.getInstance(ColorSpace.CS_sRGB);

    /**
     * Create a new Lab color space object, given the description in
     * a PDF dictionary.
     * @param obj a dictionary that contains an Array of 3 Numbers for
     * "WhitePoint" and "BlackPoint", and an array of 4 Numbers for
     * "Range".
     */
    public LabColor(PDFObject obj) throws IOException {
        super(TYPE_Lab, 3);
        PDFObject ary = obj.getDictRef("WhitePoint");
        if (ary != null) {
            for (int i = 0; i < 3; i++) {
                white[i] = ary.getAt(i).getFloatValue();
            }
        }
        ary = obj.getDictRef("BlackPoint");
        if (ary != null) {
            for (int i = 0; i < 3; i++) {
                black[i] = ary.getAt(i).getFloatValue();
            }
        }
        ary = obj.getDictRef("Range");
        if (ary != null) {
            for (int i = 0; i < 4; i++) {
                range[i] = ary.getAt(i).getFloatValue();
            }
        }
    }

    /**
     * get the number of components for this color space (3)
     */
    @Override
    public int getNumComponents() {
        return 3;
    }

    /**
     * Stage 2 of the conversion algorithm.  Pulled out because
     * it gets invoked for each component
     */
    public final float stage2(float s1) {
        return (s1 >= 6f / 29f) ? s1 * s1 * s1 : 108f / 841f * (s1 - 4f / 29f);
    }

    /**
     * convert from Lab to RGB
     * @param comp the Lab values (0-1)
     * @return the RGB values (0-1)
     */
    public float[] toRGB(float comp[]) {
        if (comp.length == 3) {
            float l = (comp[0] + 16) / 116 + comp[1] / 500;
            float m = (comp[0] + 16) / 116;
            float n = (comp[0] + 16) / 116 - comp[2] / 200;
            float xyz[] = { white[0] * stage2(l), white[0] * stage2(m), white[0] * stage2(n) };
            float rgb[] = cie.fromCIEXYZ(xyz);
            return rgb;
        } else {
            return black;
        }
    }

    /**
     * convert from RGB to Lab.  NOT IMPLEMENTED
     */
    public float[] fromRGB(float[] rgbvalue) {
        return new float[3];
    }

    /**
     * convert from CIEXYZ to Lab.  NOT IMPLEMENTED
     */
    public float[] fromCIEXYZ(float[] colorvalue) {
        return new float[3];
    }

    /**
     * get the type of this colorspace (TYPE_Lab)
     */
    @Override
    public int getType() {
        return TYPE_Lab;
    }

    /**
     * convert from Lab to CIEXYZ.   NOT IMPLEMENTED
     */
    public float[] toCIEXYZ(float[] colorvalue) {
        return new float[3];
    }
}
