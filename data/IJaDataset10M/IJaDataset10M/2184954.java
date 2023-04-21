package org.greyfire.transcendancy.ui;

import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ScrollableArtCanvas extends JScrollPane implements ArtSurface {

    private static final long serialVersionUID = 1L;

    ArtCanvas subcanvas;

    public ScrollableArtCanvas(int width, int height) {
        super(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        subcanvas = new ArtCanvas(width, height);
        this.setViewportView(subcanvas);
    }

    public void clear(Color clr) {
        this.subcanvas.clear(clr);
    }

    public int height() {
        return this.subcanvas.height();
    }

    public void pset(int x, int y, int c) {
        this.subcanvas.pset(x, y, c);
    }

    public void pset(int x, int y, Color clr) {
        this.subcanvas.pset(x, y, clr);
    }

    public void pset_safe(int x, int y, int c) {
        this.subcanvas.pset_safe(x, y, c);
    }

    public void pset_safe(int x, int y, Color clr) {
        this.subcanvas.pset_safe(x, y, clr);
    }

    public void line(int x0, int y0, int x1, int y1, int c) {
        this.subcanvas.line(x0, y0, x1, y1, c);
    }

    public void line(int x0, int y0, int x1, int y1, Color clr) {
        this.subcanvas.line(x0, y0, x1, y1, clr);
    }

    public void line_safe(int x0, int y0, int x1, int y1, int c) {
        this.subcanvas.line_safe(x0, y0, x1, y1, c);
    }

    public void line_safe(int x0, int y0, int x1, int y1, Color clr) {
        this.subcanvas.line_safe(x0, y0, x1, y1, clr);
    }

    public void refresh() {
        this.subcanvas.refresh();
    }

    public void refresh(int x, int y, int w, int h) {
        this.subcanvas.refresh(x, y, w, h);
    }

    public static int translated_colour(Color clr) {
        return ArtCanvas.translated_colour(clr);
    }

    public int width() {
        return this.subcanvas.width();
    }

    public int pget(int x, int y) {
        return this.subcanvas.pget(x, y);
    }

    public void blit(int x, int y, BufferedImage img) {
        this.subcanvas.blit(x, y, img);
    }

    public void circle(int x0, int y0, int radius, int c) {
        this.subcanvas.circle(x0, y0, radius, c);
    }

    public void circle(int x0, int y0, int radius, Color clr) {
        this.subcanvas.circle(x0, y0, radius, clr);
    }

    public void circle_safe(int x0, int y0, int radius, int c) {
        this.subcanvas.circle_safe(x0, y0, radius, c);
    }

    public void circle_safe(int x0, int y0, int radius, Color clr) {
        this.subcanvas.circle_safe(x0, y0, radius, clr);
    }
}
