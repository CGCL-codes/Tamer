package de.enough.polish.ui.screenanimations;

import de.enough.polish.ui.Display;
import de.enough.polish.ui.Displayable;
import de.enough.polish.android.lcdui.Graphics;
import de.enough.polish.ui.ScreenChangeAnimation;
import de.enough.polish.ui.Style;

public class PaperScreenChangeAnimation extends ScreenChangeAnimation {

    private boolean stillRun = true;

    private int row = 0;

    private int[] scaleableHeight;

    private int width, height;

    public PaperScreenChangeAnimation() {
        super();
        this.useNextCanvasRgb = true;
        this.useLastCanvasRgb = true;
    }

    protected void onShow(Style style, Display dsplay, int width, int height, Displayable lstDisplayable, Displayable nxtDisplayable, boolean isForward) {
        this.row = 0;
        this.height = height;
        this.width = width;
        this.scaleableHeight = new int[width];
        for (int i = 0; i < this.scaleableHeight.length; i++) {
            this.scaleableHeight[i] = 0;
        }
        this.scaleableHeight[1] = 1;
        super.onShow(style, dsplay, width, height, lstDisplayable, nxtDisplayable, isForward);
    }

    protected boolean animate() {
        boolean Rowswitch = true;
        int row = 0;
        for (int i = 0; i < this.lastCanvasRgb.length; i++) {
            if (row <= this.row && this.scaleableHeight[row] != 0) {
                this.lastCanvasRgb[i] = this.nextCanvasRgb[i];
            }
            row = (row + 1) % this.width;
        }
        this.row += 4;
        if (this.row >= this.width) this.stillRun = false;
        return this.stillRun;
    }

    public void paintAnimation(Graphics g) {
        g.drawRGB(this.lastCanvasRgb, 0, this.width, 0, 0, this.width, this.height, false);
    }
}
