package org.psystems.bpm.dashboard.client.widgets;

import gwt.canvas.client.Canvas;
import java.util.Calendar;
import java.util.Date;
import org.psystems.bpm.dashboard.client.DashWidget;
import com.google.gwt.core.client.Duration;
import com.google.gwt.user.client.Timer;

public class DashWidgetClock extends DashWidget {

    private Canvas canvas;

    private Timer timer;

    public DashWidgetClock() {
        canvas = new Canvas();
        canvas.setPixelSize(240, 240);
        add(canvas);
        timer = new Timer() {

            @Override
            public void run() {
                drawClock(canvas);
            }
        };
        timer.scheduleRepeating(1000);
    }

    private void drawClock(Canvas canvas) {
        int a = (int) Duration.currentTimeMillis();
        canvas.save();
        canvas.clear();
        canvas.translate(120, 120);
        canvas.scale(0.8f, 0.8f);
        canvas.rotate((float) (-Math.PI / 2));
        canvas.save();
        canvas.beginPath();
        canvas.setLineWidth(7);
        canvas.setStrokeStyle("#325FA2");
        canvas.setFillStyle("#fff");
        canvas.arc(0, 0, 142, 0, (float) (Math.PI * 2), true);
        canvas.fill();
        canvas.arc(0, 0, 142, 0, (float) (Math.PI * 2), true);
        canvas.stroke();
        canvas.restore();
        canvas.setStrokeStyle("rgb(0,0,0)");
        canvas.setFillStyle("rgb(255,255,255)");
        canvas.setLineWidth(4);
        canvas.setLineCap("round");
        canvas.save();
        for (int i = 0; i < 12; ++i) {
            canvas.beginPath();
            canvas.rotate((float) (Math.PI / 6));
            canvas.moveTo(100, 0);
            canvas.lineTo(120, 0);
            canvas.stroke();
        }
        canvas.restore();
        canvas.save();
        canvas.setLineWidth(2.5f);
        for (int i = 0; i < 60; ++i) {
            if (i % 5 != 0) {
                canvas.beginPath();
                canvas.moveTo(117, 0);
                canvas.lineTo(120, 0);
                canvas.stroke();
            }
            canvas.rotate((float) (Math.PI / 30));
        }
        canvas.restore();
        Date date = new Date();
        int sec = date.getSeconds();
        int min = date.getMinutes();
        int hr = date.getHours();
        hr = (hr >= 12) ? hr - 12 : hr;
        canvas.setFillStyle("rgb(0,0,0)");
        canvas.save();
        canvas.rotate((float) (hr * Math.PI / 6 + Math.PI / 360 * min + Math.PI / 21600 * sec));
        canvas.setLineWidth(7);
        canvas.beginPath();
        canvas.moveTo(-20, 0);
        canvas.lineTo(80, 0);
        canvas.stroke();
        canvas.restore();
        canvas.save();
        canvas.rotate((float) (Math.PI / 30 * min + Math.PI / 1800 * sec));
        canvas.setLineWidth(5);
        canvas.beginPath();
        canvas.moveTo(-28, 0);
        canvas.lineTo(112, 0);
        canvas.stroke();
        canvas.restore();
        canvas.save();
        canvas.rotate((float) (sec * Math.PI / 30));
        canvas.setStrokeStyle("#D40000");
        canvas.setFillStyle("#D40000");
        canvas.setLineWidth(3);
        canvas.beginPath();
        canvas.moveTo(-30, 0);
        canvas.lineTo(83, 0);
        canvas.stroke();
        canvas.beginPath();
        canvas.moveTo(107, 0);
        canvas.lineTo(121, 0);
        canvas.stroke();
        canvas.beginPath();
        canvas.arc(0, 0, 10, 0, (float) (Math.PI * 2), true);
        canvas.fill();
        canvas.beginPath();
        canvas.arc(95, 0, 10, 0, (float) (Math.PI * 2), true);
        canvas.stroke();
        canvas.beginPath();
        canvas.setFillStyle("#555");
        canvas.arc(0, 0, 3, 0, (float) (Math.PI * 2), true);
        canvas.fill();
        canvas.restore();
        canvas.restore();
        int b = (int) Duration.currentTimeMillis() - a;
        timer.schedule(1000 - b);
    }

    @Override
    public void init() {
    }

    @Override
    public void showConfDialog(DashWidget w) {
    }
}
