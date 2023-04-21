package model.util;

import java.nio.FloatBuffer;

public class Color {

    float r;

    float g;

    float b;

    float alpha;

    public Color(String hexCode) {
        this.r = (float) (Integer.valueOf(hexCode.substring(0, 2), 16)) / 255.0f;
        this.g = (float) (Integer.valueOf(hexCode.substring(2, 4), 16)) / 255.0f;
        this.b = (float) (Integer.valueOf(hexCode.substring(4, 6), 16)) / 255.0f;
        this.alpha = 1.0f;
    }

    public Color(float red, float green, float blue) {
        this.r = red;
        this.g = green;
        this.b = blue;
        this.alpha = 1.0f;
    }

    public Color(float red, float green, float blue, float alpha) {
        super();
        this.r = red;
        this.g = green;
        this.b = blue;
        this.alpha = alpha;
    }

    public FloatBuffer getColorFB() {
        FloatBuffer color = FloatBuffer.allocate(4);
        color.put(0, r);
        color.put(1, g);
        color.put(2, b);
        color.put(3, alpha);
        return color;
    }

    public float getR() {
        return r;
    }

    public float getG() {
        return g;
    }

    public float getB() {
        return b;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setR(float r) {
        this.r = r;
    }

    public void setG(float g) {
        this.g = g;
    }

    public void setB(float b) {
        this.b = b;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Color other = (Color) obj;
        if (Float.floatToIntBits(alpha) != Float.floatToIntBits(other.alpha)) return false;
        if (Float.floatToIntBits(b) != Float.floatToIntBits(other.b)) return false;
        if (Float.floatToIntBits(g) != Float.floatToIntBits(other.g)) return false;
        if (Float.floatToIntBits(r) != Float.floatToIntBits(other.r)) return false;
        return true;
    }
}
