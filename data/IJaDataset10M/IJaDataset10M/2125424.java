package hu.schmidtsoft.map.model;

/**
 * Render style of an object.
 * @author rizsi
 *
 */
public class MRenderStyle {

    float size;

    int color;

    float[] parameters;

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public float[] getParams() {
        return parameters;
    }

    public void setParams(float[] paramters) {
        this.parameters = paramters;
    }
}
