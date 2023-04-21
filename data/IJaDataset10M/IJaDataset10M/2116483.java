package de.grogra.blocks.arrangeBlock;

import raskob.geometry.Point;
import raskob.geometry.PointArrayList;

public class Jarvis12Dither extends ArrangeBase {

    public Jarvis12Dither(float maxX, float maxY, float threshold, float maxThreshold, float[][] field) {
        final int FIELD_LENGTH = field.length;
        threshold *= maxThreshold;
        final double a = 7.0 / 48.0;
        final double b = 5.0 / 48.0;
        final double c = 3.0 / 48.0;
        final double d = 5.0 / 48.0;
        final double e = 7.0 / 48.0;
        final double f = 5.0 / 48.0;
        final double g = 3.0 / 48.0;
        final double h = 1.0 / 48.0;
        final double i = 3.0 / 48.0;
        final double j = 5.0 / 48.0;
        final double k = 3.0 / 48.0;
        final double l = 1.0 / 48.0;
        double error = 0.0, old = 0;
        for (int ii = 0; ii < FIELD_LENGTH - 2; ii++) {
            for (int jj = 2; jj < FIELD_LENGTH - 2; jj++) {
                old = field[jj][ii];
                if (field[jj][ii] < threshold) {
                    field[jj][ii] = 0;
                } else {
                    field[jj][ii] = maxThreshold;
                }
                error = Math.abs(old - field[jj][ii]);
                field[jj + 1][ii] += error * a;
                field[jj + 2][ii] += error * b;
                field[jj - 2][ii + 1] += error * c;
                field[jj - 1][ii + 1] += error * d;
                field[jj][ii + 1] += error * e;
                field[jj + 1][ii + 1] += error * f;
                field[jj + 2][ii + 1] += error * g;
                field[jj - 2][ii + 2] += error * h;
                field[jj - 1][ii + 2] += error * i;
                field[jj][ii + 2] += error * j;
                field[jj + 1][ii + 2] += error * k;
                field[jj + 2][ii + 2] += error * l;
            }
        }
        pointList = new PointArrayList();
        for (int ii = 0; ii < FIELD_LENGTH; ii++) {
            for (int jj = 0; jj < FIELD_LENGTH; jj++) {
                if (field[jj][ii] == maxThreshold) {
                    pointList.add(new Point(ii * (maxX - 1) / FIELD_LENGTH, jj * (maxY - 1) / FIELD_LENGTH));
                }
            }
        }
        pointListToArrays();
    }
}
