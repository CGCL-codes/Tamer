package org.timepedia.chronoscope.client.canvas;

/**
 * RadialGradients must implement this interface
 */
public interface RadialGradient extends PaintStyle {

    void addColorStop(double position, String color);
}
