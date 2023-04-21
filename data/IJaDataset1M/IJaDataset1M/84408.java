package com.c4j.diagram.basic;

import java.awt.Point;

public class TranslationRestriction implements IRestriction {

    private final FreePoint pointA;

    private final FreePoint pointB;

    public TranslationRestriction(final FreePoint pointA, final FreePoint pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    @Override
    public Point getClosestPoint(final int x, final int y) {
        final int diffX = pointB.getXPosition() - pointA.getXPosition();
        final int diffY = pointB.getYPosition() - pointA.getYPosition();
        final Point bRes = pointB.setRestriction.getClosestPoint(x + diffX, y + diffY);
        return new Point(bRes.x - diffX, bRes.y - diffY);
    }
}
