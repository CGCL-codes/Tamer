package com.tictactec.ta.lib.test;

public class InputData {

    String name;

    double[] doubleData;

    float[] floatData;

    int[] intData;

    public InputData(String name, int size) {
        this.name = name;
        this.doubleData = new double[size];
        this.floatData = new float[size];
        this.intData = new int[size];
    }

    /**
    * copy constructor to avoid clone()
    * @param that
    */
    public InputData(InputData that) {
        this(that.name, that.size());
        System.arraycopy(that.doubleData, 0, this.doubleData, 0, this.doubleData.length);
        System.arraycopy(that.floatData, 0, this.floatData, 0, this.floatData.length);
        System.arraycopy(that.intData, 0, this.intData, 0, this.intData.length);
    }

    public String getName() {
        return name;
    }

    public int size() {
        return doubleData.length;
    }

    public void setData(int index, double dd, float fd, int id) {
        doubleData[index] = dd;
        floatData[index] = fd;
        intData[index] = id;
    }

    public double[] getDoubleData() {
        return doubleData;
    }

    public float[] getFloatData() {
        return floatData;
    }

    public int[] getIntData() {
        return intData;
    }
}
