package net.dzzd.core;

import net.dzzd.access.*;
import net.dzzd.utils.*;
import net.dzzd.DzzD;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.Cursor;
import java.util.*;
import java.awt.Color;
import java.awt.Image;

public class Render2D extends Render implements IRender2D {

    protected PCanvas canvas;

    protected int viewPixelWidth;

    protected int viewPixelHeight;

    protected int antialias;

    protected int maxAntialias;

    protected int minXValue;

    protected int maxXValue;

    protected int minYValue;

    protected int maxYValue;

    protected boolean isScreenUpdateEnabled;

    protected boolean isPixelUpdateEnabled;

    protected int numImage;

    protected boolean rendering;

    protected IDirectInput directInput;

    protected int render2DMode;

    class PCanvas extends Canvas {

        public Image image;

        public PCanvas() {
            super();
        }

        public void paint(Graphics g) {
            this.update(g);
        }

        public void update(Graphics g) {
            if (this.image != null) g.drawImage(this.image, 0, 0, null);
            this.validate();
        }
    }

    public Render2D() {
        super();
        this.render2DMode = DzzD.RM_ALL;
        this.canvas = new PCanvas();
        this.rendering = false;
        this.antialias = 1;
        this.maxAntialias = 1;
        this.directInput = null;
        this.numImage = 0;
        this.isScreenUpdateEnabled = true;
        this.isPixelUpdateEnabled = true;
    }

    public Canvas getCanvas() {
        return this.canvas;
    }

    public IDirectInput getDirectInput() {
        return this.directInput;
    }

    public void setSize(int viewPixelWidth, int viewPixelHeight, int maxAntialias) {
        this.canvas.setSize(viewPixelWidth, viewPixelHeight);
        this.viewPixelWidth = viewPixelWidth;
        this.viewPixelHeight = viewPixelHeight;
        this.maxAntialias = maxAntialias;
        this.antialias = maxAntialias;
    }

    public void setSize(int viewPixelWidth, int viewPixelHeight) {
        this.setSize(viewPixelWidth, viewPixelHeight, this.maxAntialias);
    }

    public void setAntialiasLevel(int level) {
        if (level == this.antialias) return;
        if (level > this.maxAntialias) this.setSize(this.viewPixelWidth, this.viewPixelHeight, level);
        this.antialias = level;
    }

    public int getWidth() {
        return this.viewPixelWidth;
    }

    public int getHeight() {
        return this.viewPixelHeight;
    }

    public void setCursor(Cursor cursor) {
        this.canvas.setCursor(cursor);
    }

    public boolean isScreenUpdateEnabled() {
        return this.isScreenUpdateEnabled;
    }

    public void setScreenUpdateEnabled(boolean flag) {
        this.isScreenUpdateEnabled = flag;
    }

    public boolean isPixelUpdateEnabled() {
        return this.isPixelUpdateEnabled;
    }

    public void setPixelUpdateEnabled(boolean flag) {
        this.isPixelUpdateEnabled = flag;
    }

    public String getImplementationName() {
        return "NONE";
    }

    protected void compileScene2DObject(IScene2D scene) {
        this.compileSceneObject(scene);
    }

    public void removeSceneObject(ISceneObject sceneObject) {
        super.removeSceneObject(sceneObject);
    }

    protected void startFrame(IScene2D scene) {
    }

    protected void renderFrame(IScene2D scene) {
    }

    protected void endFrame(IScene2D scene) {
    }

    public void renderScene2D(IScene2D scene) {
        this.rendering = true;
        this.startFrame(scene);
        this.renderFrame(scene);
        this.endFrame(scene);
        this.numImage++;
        this.rendering = false;
    }

    public IRender2DMode getRender2DMode() {
        return this;
    }

    public void enableRender2DMode(int flag) {
        this.render2DMode |= flag;
    }

    public void disableRender2DMode(int flag) {
        this.render2DMode &= (flag ^ DzzD.RM_ALL);
    }

    public void setRender2DModeFlags(int flag) {
        this.render2DMode = flag;
    }

    public int getRender2DModeFlags() {
        return this.render2DMode;
    }
}
