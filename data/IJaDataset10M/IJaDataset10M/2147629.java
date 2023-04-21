package net.kornr.abstractcanvas.client.test;

import net.kornr.abstractcanvas.client.ICanvas;

public class ArcTest {

    public static void drawArcs(ICanvas canvas) {
        for (double i = 0; i < 4; i++) {
            for (double j = 0; j < 3; j++) {
                double x = 25 + j * 50;
                double y = 25 + i * 50;
                double radius = 20;
                double startAngle = 0;
                double endAngle = Math.PI + (Math.PI * j) / 2;
                boolean clockwise = i % 2 == 0 ? false : true;
                canvas.beginPath();
                canvas.arc(x, y, radius, startAngle, endAngle, !clockwise);
                canvas.closePath();
                if (i > 1) {
                    canvas.fill();
                } else {
                    canvas.stroke();
                }
            }
        }
    }

    public static void drawArcs2(ICanvas canvas, int xoffset, boolean clockwise) {
        for (double i = 0; i < Math.PI * 2; i += Math.PI / 10) {
            double x = 25;
            double y = 50 + i * 50;
            double radius = 20;
            double startAngle = i;
            double endAngle = i + Math.PI / 2;
            canvas.beginPath();
            canvas.arc(x + xoffset, y, radius, startAngle, endAngle, clockwise);
            canvas.closePath();
            canvas.stroke();
        }
    }
}
