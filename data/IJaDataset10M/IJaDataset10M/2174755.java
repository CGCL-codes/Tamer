package edu.umd.cs.mouseprecision;

import java.awt.Color;
import edu.umd.cs.piccolo.event.PBasicInputEventHandler;
import edu.umd.cs.piccolo.event.PInputEvent;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

public class FinishArea extends PPath {

    private TaskCanvas parentCanvas;

    float[] hsbColorBlack = Color.RGBtoHSB(0, 0, 0, null);

    float[] hsbColorRed = Color.RGBtoHSB(255, 0, 0, null);

    float[] hsbColorRed2 = Color.RGBtoHSB(255, 100, 100, null);

    float[] hsbColorGreen = Color.RGBtoHSB(0, 250, 100, null);

    public boolean disabled = true;

    public FinishArea(TaskCanvas parentCanvas) {
        this.parentCanvas = parentCanvas;
        this.setStrokePaint(Color.getHSBColor(hsbColorBlack[0], hsbColorBlack[1], hsbColorBlack[2]));
        this.setPaint(Color.getHSBColor(hsbColorGreen[0], hsbColorGreen[1], hsbColorGreen[2]));
        this.moveTo(0, 0);
        this.lineTo(Main.RASTER, 0);
        this.lineTo(Main.RASTER, Main.RASTER);
        this.lineTo(0, Main.RASTER);
        this.lineTo(0, 0);
        this.setScale(Main.SCALE);
        this.addInputEventListener(new PBasicInputEventHandler() {

            public void mouseEntered(PInputEvent event) {
                super.mouseEntered(event);
                if (!disabled) {
                    setPaint(Color.getHSBColor(hsbColorRed[0], hsbColorRed[1], hsbColorRed[2]));
                    taskFinished();
                    disabled = true;
                }
            }
        });
    }

    private void taskFinished() {
        parentCanvas.taskFinished();
    }

    public void enable() {
        this.disabled = false;
    }
}
