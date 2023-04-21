package com.amd.aparapi.test;

public class ForAndMandel {

    int width = 1024;

    float scale = 1f;

    int maxIterations = 10;

    public void run() {
        int tid = 0;
        int i = tid % width;
        int j = tid / width;
        float x0 = ((i * scale) - ((scale / 2) * width)) / width;
        float y0 = ((j * scale) - ((scale / 2) * width)) / width;
        float x = x0;
        float y = y0;
        float x2 = x * x;
        float y2 = y * y;
        float scaleSquare = scale * scale;
        int count = 0;
        for (int iter = 0; x2 + y2 <= scaleSquare && (iter < maxIterations); ++iter) {
            y = 2 * x * y + y0;
            x = x2 - y2 + x0;
            x2 = x * x;
            y2 = y * y;
            count++;
        }
        @SuppressWarnings("unused") int value = (256 * count) / maxIterations;
    }
}
