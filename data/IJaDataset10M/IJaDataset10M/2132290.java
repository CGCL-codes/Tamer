package com.sun.opengl.util;

class StrokeCharRec {

    int num_strokes;

    StrokeRec[] stroke;

    float center;

    float right;

    StrokeCharRec(int num_strokes, StrokeRec[] stroke, float center, float right) {
        this.num_strokes = num_strokes;
        this.stroke = stroke;
        this.center = center;
        this.right = right;
    }
}
