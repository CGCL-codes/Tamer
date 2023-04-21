package org.hswgt.teachingbox.usecases.realkorsel.simulator;

import java.awt.Rectangle;
import javax.vecmath.Point2d;

public class SimThread extends Thread {

    private SimKorsel korsel;

    private org.hswgt.teachingbox.usecases.realkorsel.simulator.OutputWindow outputWindow;

    SimThread(org.hswgt.teachingbox.usecases.realkorsel.simulator.OutputWindow outputWindow, SimKorsel korsel) {
        this.outputWindow = outputWindow;
        this.korsel = korsel;
    }

    @Override
    public void run() {
        for (; ; ) {
            try {
                sleep(10);
            } catch (InterruptedException e) {
            }
            korsel.nextStep();
            outputWindow.drawArea.setCalcedPosition(korsel.getPosition());
            outputWindow.drawArea.setPoint2(korsel.getLWheelPosition());
            outputWindow.drawArea.setPoint3(korsel.getRWheelPosition());
            outputWindow.drawArea.setApex(korsel.getSensorPosition());
            Rectangle rect = new Rectangle();
            rect = CalcRepaintRectangle(korsel.getLWheelPosition(), korsel.getRWheelPosition(), korsel.getSensorPosition());
            outputWindow.drawArea.repaint(rect);
        }
    }

    private Rectangle CalcRepaintRectangle(Point2d point1, Point2d point2, Point2d point3) {
        int x_min = (int) Math.min(point1.x, point2.x);
        x_min = (int) Math.min(x_min, point3.x);
        int y_min = (int) Math.min(point1.y, point2.y);
        y_min = (int) Math.min(y_min, point3.y);
        int x_max = (int) Math.max(point1.x, point2.x);
        x_max = (int) Math.max(x_max, point3.x);
        int y_max = (int) Math.max(point1.y, point2.y);
        y_max = (int) Math.max(y_max, point3.y);
        int border = 10;
        Rectangle rect = new Rectangle(x_min - border, y_min - border, x_max - x_min + 2 * border, y_max - y_min + 2 * border);
        return rect;
    }
}
