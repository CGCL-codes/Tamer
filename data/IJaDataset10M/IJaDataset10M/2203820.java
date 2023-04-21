package strudle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;

public class WavePaintListener implements PaintListener {

    @SuppressWarnings("unused")
    private float framerate;

    public void paintControl(PaintEvent e) {
        WaveCanvas canvas = (WaveCanvas) e.widget;
        e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
        e.gc.drawLine(canvas.selectionCur, 0, canvas.selectionCur, canvas.getSize().y);
        e.gc.drawLine(canvas.curX, 0, canvas.curX, canvas.getSize().y);
        e.gc.dispose();
    }

    int getNormalizedSine(int x, int halfY, int maxX) {
        double piDouble = 2 * Math.PI;
        double factor = piDouble / maxX;
        return (int) (Math.sin(x * factor) * halfY + halfY);
    }

    public void setSelCur(boolean b) {
    }

    public void setFrameRate(float fr) {
        framerate = fr;
    }
}
