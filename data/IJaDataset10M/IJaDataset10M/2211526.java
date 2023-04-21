package vrml.field;

import vrml.*;

public class MFColor extends MField {

    float[] colors;

    public MFColor(float colors[][]) {
        this.colors = new float[colors.length * 3];
        for (int i = 0; i < colors.length; i++) {
            this.colors[i * 3] = colors[i][0];
            this.colors[i * 3 + 1] = colors[i][1];
            this.colors[i * 3 + 2] = colors[i][2];
        }
    }

    public MFColor(float colors[]) {
        this.colors = new float[colors.length];
        System.arraycopy(colors, 0, this.colors, 0, colors.length);
    }

    public MFColor(int size, float colors[]) {
        this.colors = new float[size];
        System.arraycopy(colors, 0, this.colors, 0, size);
    }

    public MFColor() {
    }

    public void getValue(float colors[][]) {
        for (int i = 0; i < colors.length; i++) {
            colors[i][0] = this.colors[i * 3];
            colors[i][1] = this.colors[i * 3 + 1];
            colors[i][2] = this.colors[i * 3 + 2];
        }
    }

    public void getValue(float colors[]) {
        System.arraycopy(this.colors, 0, colors, 0, colors.length);
    }

    public void get1Value(int index, float colors[]) {
        colors[0] = this.colors[index * 3];
        colors[1] = this.colors[index * 3 + 1];
        colors[2] = this.colors[index * 3 + 2];
    }

    public void get1Value(int index, SFColor color) {
        color.red = colors[index * 3];
        color.green = colors[index * 3 + 1];
        color.blue = colors[index * 3 + 2];
    }

    public void setValue(float colors[][]) {
        this.colors = new float[colors.length * 3];
        for (int i = 0; i < colors.length; i++) {
            this.colors[i * 3] = colors[i][0];
            this.colors[i * 3 + 1] = colors[i][1];
            this.colors[i * 3 + 2] = colors[i][2];
        }
    }

    public void setValue(float colors[]) {
        this.colors = new float[colors.length];
        System.arraycopy(colors, 0, this.colors, 0, colors.length);
    }

    public void setValue(int size, float colors[]) {
        this.colors = new float[size];
        System.arraycopy(colors, 0, this.colors, 0, size);
    }

    public void setValue(MFColor colors) {
        this.colors = new float[colors.colors.length];
        System.arraycopy(colors.colors, 0, this.colors, 0, colors.colors.length);
    }

    public void setValue(ConstMFColor colors) {
        this.colors = new float[colors.colors.length];
        System.arraycopy(colors.colors, 0, this.colors, 0, colors.colors.length);
    }

    public void set1Value(int index, ConstSFColor color) {
        this.colors[index * 3] = color.red;
        this.colors[index * 3 + 1] = color.green;
        this.colors[index * 3 + 2] = color.blue;
    }

    public void set1Value(int index, SFColor color) {
        this.colors[index * 3] = color.red;
        this.colors[index * 3 + 1] = color.green;
        this.colors[index * 3 + 2] = color.blue;
    }

    public void set1Value(int index, float red, float green, float blue) {
        this.colors[index * 3] = red;
        this.colors[index * 3 + 1] = green;
        this.colors[index * 3 + 2] = blue;
    }

    public void addValue(ConstSFColor color) {
    }

    public void addValue(SFColor color) {
    }

    public void addValue(float red, float green, float blue) {
    }

    public void insertValue(int index, ConstSFColor color) {
    }

    public void insertValue(int index, SFColor color) {
    }

    public void insertValue(int index, float red, float green, float blue) {
    }

    public String toString() {
        StringBuffer ret = new StringBuffer('[');
        for (int i = 0; i < colors.length; i++) {
            ret.append(' ');
            ret.append(colors[i]);
        }
        ret.append(" ]");
        return ret.toString();
    }
}
