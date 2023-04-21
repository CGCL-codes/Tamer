package de.grogra.ray.event;

public interface RTProgressListener {

    public static final int RENDERING_PREPROCESSING = 0;

    public static final int RENDERING_PROCESSING = 1;

    public static final int RENDERING_POSTPROCESSING = 2;

    public static final int LIGHTNING_PREPROCESSING = 3;

    public static final int LIGHTNING_PROCESSING = 4;

    public static final int LIGHTNING_POSTPROCESSING = 5;

    public void progressChanged(int type, double progress, String text, int x, int y, int width, int height);
}
