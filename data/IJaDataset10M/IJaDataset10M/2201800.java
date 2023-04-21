package com.wizzer.m3g.toolkit.util;

/**
 * This class encapsulates a Color object.
 * 
 * @author Mark Millard
 */
public class Color {

    /** The alpha component. */
    public float m_a = 0.0f;

    /** The red component. */
    public float m_r = 0.0f;

    /** The green component. */
    public float m_g = 0.0f;

    /** The blue component. */
    public float m_b = 0.0f;

    /**
	 * A constructor that initializes the alpha, red, green and
	 * blue components of a Color object.
	 * 
	 * @param a The alpha component.
	 * @param r The red component.
	 * @param g The green component.
	 * @param b The blue component.
	 */
    public Color(float a, float r, float g, float b) {
        m_a = a;
        m_r = r;
        m_g = g;
        m_b = b;
    }

    /**
	 * A constructor that initializes the color from an integer.
	 * 
	 * @param color The alpha, red, green, and blue values encoded as an integer.
	 */
    public Color(int color) {
        m_a = ((float) (color >> 24)) / 255.0f;
        m_r = ((float) ((color & 0xFF0000) >> 16)) / 255.0f;
        m_g = ((float) ((color & 0xFF00) >> 8)) / 255.0f;
        m_b = ((float) (color & 0xFF)) / 255.0f;
    }

    /**
	 * Get the color as an array of 4 floating-point values.
	 * 
	 * @return An array is returned containing the red, green, blue and
	 * alpha components of the color (in that order).
	 */
    public float[] toArray() {
        float[] c = { m_r, m_g, m_b, m_a };
        return c;
    }

    /**
	 * Convert the encoded color integer to an array of 4 floating-point
	 * values.
	 * 
	 * @param color The alpha, red, green, and blue values encoded as an integer.
	 * 
	 * @return An array is returned containing the red, green, blue and
	 * alpha components of the color (in that order).
	 */
    public static float[] intToFloatArray(int color) {
        Color c = new Color(color);
        return c.toArray();
    }

    /**
	 * Retrieve the color as string.
	 * 
	 * @return A <code>String</code> will be returned.
	 */
    public String toString() {
        return "{" + m_r + ", " + m_g + ", " + m_b + ", " + m_a + "}";
    }
}
