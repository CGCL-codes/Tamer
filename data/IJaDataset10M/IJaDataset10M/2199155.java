package org.phramer.mert.intersection;

import org.phramer.mert.intersection.*;

class Point {

    Point(double lambda, Line hRight, Line hLeft, int refIndex, int originalPositionInNbest) {
        this.refIndex = refIndex;
        this.lambda = lambda;
        this.onRight = hRight;
        this.onLeft = hLeft;
        this.originalPositionInNbest = originalPositionInNbest;
    }

    double lambda;

    final Line onRight, onLeft;

    final int refIndex;

    final int originalPositionInNbest;
}
