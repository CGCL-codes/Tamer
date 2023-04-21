package net.sourceforge.originalsynth.tool;

import net.sourceforge.originalsynth.global.SynProperties;
import net.sourceforge.originalsynth.tab.content.SynPoint;

/**
 * Draws quadratic curves using three points.
 *
 *
 */
public class CurveTool implements Tool {

    private int prevX = 0;

    private int prevY = 0;

    private SynPoint curPt = null;

    private boolean canDraw = false;

    /**
     * Empty Constructor
     *
     */
    public CurveTool() {
    }

    public void mouseMotionStarted(int xCoord, int yCoord) {
        if (SynProperties.getCurCanvas().getPoints().get(xCoord) == null) {
            canDraw = true;
            prevX = xCoord;
            prevY = yCoord;
            curPt = new SynPoint(SynPoint.LINE_CURVE_QUADRATIC_MIDDLE_PT, prevX, prevY);
            SynProperties.getCurCanvas().getPoints().put(curPt);
            SynPoint startCurvePt = SynProperties.getCurCanvas().getPoints().getPrevPt(curPt);
            SynPoint endCurvePt = SynProperties.getCurCanvas().getPoints().getNextPt(curPt);
            curPt.setQuadraticRefPts(startCurvePt, endCurvePt);
        } else {
            canDraw = false;
        }
    }

    public void mouseMotionProgressing(int xCoord, int yCoord) {
        if (canDraw && (SynProperties.getCurCanvas().getPoints().get(xCoord) == null || xCoord == prevX)) {
            SynProperties.getCurCanvas().getPoints().remove(curPt.getXVal());
            prevX = xCoord;
            prevY = yCoord;
            curPt.setXVal(prevX);
            curPt.setYVal(prevY);
            SynProperties.getCurCanvas().getPoints().put(curPt, curPt.getPointNum());
            SynPoint startCurvePt = SynProperties.getCurCanvas().getPoints().getPrevPt(curPt);
            SynPoint endCurvePt = SynProperties.getCurCanvas().getPoints().getNextPt(curPt);
            curPt.setQuadraticRefPts(startCurvePt, endCurvePt);
        }
    }

    public void mouseMotionCompleted(int xCoord, int yCoord) {
        if (canDraw) {
            SynProperties.getCurCanvas().getPoints().remove(curPt.getXVal());
            if (xCoord <= 0) {
                xCoord = 1;
            } else if (xCoord >= SynProperties.WIN_WIDTH - 1) {
                xCoord = SynProperties.WIN_WIDTH - 2;
            }
            curPt.setXVal(xCoord);
            curPt.setYVal(yCoord);
            SynProperties.getCurCanvas().getPoints().put(curPt, curPt.getPointNum());
            SynPoint startCurvePt = SynProperties.getCurCanvas().getPoints().getPrevPt(curPt);
            SynPoint endCurvePt = SynProperties.getCurCanvas().getPoints().getNextPt(curPt);
            curPt.setQuadraticRefPts(startCurvePt, endCurvePt);
        }
    }

    public void mouseMotionCanceled(int xCoord, int yCoord) {
        if (canDraw) {
            SynProperties.getCurCanvas().getPoints().remove(curPt.getXVal());
        }
    }
}
