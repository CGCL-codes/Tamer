package org.vastenhouw.jphotar.imagebrowser.transitions;

import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.vastenhouw.util.Debug;
import org.vastenhouw.util.CsvWriter;
import org.vastenhouw.util.CsvReader;

/**
 * Overlapping Blinds effect
 */
public class OverlappingBlinds extends Transition {

    private int maxStep;

    private int lamelSize;

    private boolean horizontal;

    private Graphics2D big;

    private int step = 0;

    private int offset;

    private int lines;

    public OverlappingBlinds(int steps, int lamelSize, boolean horizontal) {
        this.maxStep = steps;
        this.lamelSize = lamelSize;
        this.horizontal = horizontal;
        this.step = 0;
    }

    public void start() {
        step = 0;
    }

    public boolean hasMoreSteps() {
        return step < maxStep;
    }

    public void nextStep() {
        offset = (step * lamelSize) / maxStep;
        lines = ((step + 1) * lamelSize) / maxStep - offset;
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

    public OverlappingBlinds(CsvReader r) throws IOException {
        super(r);
        this.maxStep = r.readInt();
        this.lamelSize = r.readInt();
        this.horizontal = r.readBool();
    }

    protected void serializeToText(CsvWriter w) throws IOException {
        super.serializeToText(w);
        w.write(maxStep);
        w.write(lamelSize);
        w.write(horizontal);
    }

    public void internalRender() {
        if (big == null) {
            big = fromImg.createGraphics();
        }
        if (horizontal) {
            int lamelCount = (scrH + lamelSize - 1) / lamelSize;
            for (int i = 0; i < lamelCount; i++) {
                int actualLines = lines;
                int endY = i * lamelSize + lines + offset;
                if (endY > scrH) {
                    actualLines = scrH - ((i - 1) * lamelSize + offset);
                }
                if (actualLines > 0) {
                    Image subImg = toImg.getSubimage(0, i * lamelSize + offset, scrW, actualLines);
                    big.drawImage(subImg, 0, i * lamelSize + offset, null);
                }
            }
        } else {
            int lamelCount = (scrW + lamelSize - 1) / lamelSize;
            for (int i = 0; i < lamelCount; i++) {
                int actualLines = lines;
                int endX = i * lamelSize + lines + offset;
                if (endX > scrW) {
                    actualLines = scrW - ((i - 1) * lamelSize + offset);
                }
                if (actualLines > 0) {
                    Image subImg = toImg.getSubimage(i * lamelSize + offset, 0, actualLines, scrH);
                    big.drawImage(subImg, i * lamelSize + offset, 0, null);
                }
            }
        }
    }
}
