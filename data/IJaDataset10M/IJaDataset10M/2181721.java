package com.somoconsulting.cbsc.graph.widget;

import java.awt.Graphics2D;

/**
 * 
 * <p>
 * cBSC - collaborative balanced scorecard � a web based tool for collaboration on strategy development 
 * Copyright (C) 2009 SOMO Consulting GmbH   <http://www.somo-consulting.com/>
 * 
 * This program comes with ABSOLUTELY NO WARRANTY; it is distributed under GNU General Public License. 
 * 
 * Draw an arrow (line with an arrow head) because Java won't.
 * </p>
 */
public final class Arrow {

    private Arrow() {
    }

    /**
     * Idea from http://www.bytemycode.com/snippets/snippet/82/
     */
    public static void drawArrow(final Graphics2D g, final int pX1, final int pY1, final int pX2, final int pY2) {
        final double lArrowHeight = 10.0;
        final double lArrowWidth = 5.0;
        final double lVectorX = pX2 - pX1;
        final double lVectorY = pY2 - pY1;
        final double lVectorLength = Math.sqrt(lVectorX * lVectorX + lVectorY * lVectorY);
        final double lVectorXNormalized = lVectorX / lVectorLength;
        final double lVectorYNormalized = lVectorY / lVectorLength;
        final double lArrowBaseX = pX2 - lVectorXNormalized * lArrowHeight;
        final double lArrowBaseY = pY2 - lVectorYNormalized * lArrowHeight;
        final double lOrthVectorX = -lVectorYNormalized;
        final double lOrthVectorY = lVectorXNormalized;
        final int[] pXPoints = new int[3];
        final int[] yPoints = new int[3];
        pXPoints[0] = pX2;
        yPoints[0] = pY2;
        pXPoints[1] = (int) (lArrowBaseX + lOrthVectorX * lArrowWidth);
        yPoints[1] = (int) (lArrowBaseY + lOrthVectorY * lArrowWidth);
        pXPoints[2] = (int) (lArrowBaseX - lOrthVectorX * lArrowWidth);
        yPoints[2] = (int) (lArrowBaseY - lOrthVectorY * lArrowWidth);
        g.drawLine(pX1, pY1, (int) lArrowBaseX, (int) lArrowBaseY);
        g.fillPolygon(pXPoints, yPoints, 3);
    }
}
