package com.spleeb.pixelengine;

public interface Screen {

    public void set(int x, int y, int color);

    public void setpc(int x, int y, int color);

    public int get(int x, int y);

    public void set(float x, float y, int color);

    public void setpc(float x, float y, int color);

    public int get(float x, float y);

    public void alp(float x, float y, int color, int weight);

    public float chbx(float x);

    public float chby(float y);

    public int chbx(int x);

    public int chby(int y);

    public void clear();
}
