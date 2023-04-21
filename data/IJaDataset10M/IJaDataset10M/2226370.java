package org.vastenhouw.jphotar.imagebrowser.transitions;

import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.vastenhouw.util.Debug;
import org.vastenhouw.util.CsvWriter;
import org.vastenhouw.util.CsvReader;

/**
 * Turning Lamel effect
 */
public class TurningBlinds extends Transition {

    private int maxStep;

    private int lamelCount;

    private boolean horizontal;

    private Color background;

    private Graphics2D big;

    private int step = 0;

    private boolean closing = false;

    public TurningBlinds(int steps, int lamelCount, boolean horizontal, Color background) {
        this.maxStep = steps - steps % 2;
        this.lamelCount = lamelCount;
        this.horizontal = horizontal;
        this.background = background;
        this.step = 0;
    }

    public void start() {
        step = 0;
        closing = false;
    }

    public boolean hasMoreSteps() {
        return step < maxStep;
    }

    public void nextStep() {
        closing = step >= (maxStep / 2);
        internalRender();
        step++;
    }

    public void render(Graphics2D g2) {
        g2.drawImage(fromImg, 0, 0, null);
    }

    public void end() {
        if (big != null) {
            big.dispose();
            big = null;
        }
    }

    public TurningBlinds(CsvReader r) throws IOException {
        super(r);
        this.maxStep = r.readInt();
        this.lamelCount = r.readInt();
        this.horizontal = r.readBool();
        this.background = r.readColor();
    }

    protected void serializeToText(CsvWriter w) throws IOException {
        super.serializeToText(w);
        w.write(maxStep);
        w.write(lamelCount);
        w.write(horizontal);
        w.write(background);
    }

    public void internalRender() {
        if (big == null) {
            big = fromImg.createGraphics();
        }
        for (int i = 0; i < lamelCount; i++) {
            int lStart = (horizontal ? scrH : scrW) * i / lamelCount;
            int lEnd = (horizontal ? scrH : scrW) * (i + 1) / lamelCount;
            int lSize = lEnd - lStart;
            int slStart = lSize * step / maxStep;
            int slEnd = lSize * (step + 1) / maxStep;
            int slSize = slEnd - slStart;
            int clStart = lSize * (maxStep - step - 1) / maxStep;
            int clEnd = lSize * (maxStep - step) / maxStep;
            int clSize = clEnd - clStart;
            if (horizontal) {
                copyOrFill(closing, 0, lStart + slStart, scrW, slSize);
                copyOrFill(closing, 0, lStart + clStart, scrW, clSize);
            } else {
                copyOrFill(closing, lStart + slStart, 0, slSize, scrH);
                copyOrFill(closing, lStart + clStart, 0, clSize, scrH);
            }
        }
    }

    private void copyOrFill(boolean copy, int x, int y, int w, int h) {
        if (x + w > scrW) {
            w = scrW - x;
            if (w < 1) {
                return;
            }
        }
        if (y + h > scrH) {
            h = scrH - y;
            if (h < 1) {
                return;
            }
        }
        if (copy) {
            Image subImg = toImg.getSubimage(x, y, w, h);
            big.drawImage(subImg, x, y, null);
        } else {
            big.setColor(background);
            big.fillRect(x, y, w, h);
        }
    }
}
