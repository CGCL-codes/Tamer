package com.jpatch.boundary.headupdisplay;

import static javax.media.opengl.GL.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.media.opengl.*;
import com.jpatch.boundary.*;
import com.sun.opengl.util.texture.*;

public class HUD implements ViewportOverlay {

    private final List<Slider> widgets = new ArrayList<Slider>();

    private static final TextureData BORDER_TEXTUREDATA;

    private Texture texture;

    private ActiveArea leftCorner;

    private ActiveArea border;

    private ActiveArea minimize;

    private ActiveArea maximize;

    private ActiveArea close;

    private int activeButton = 1;

    private Rectangle bounds = new Rectangle(100, 100, 300, 200);

    static {
        try {
            BORDER_TEXTUREDATA = TextureIO.newTextureData(ClassLoader.getSystemResourceAsStream("com/jpatch/boundary/headupdisplay/windowBorders.png"), false, "png");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Viewport viewport;

    public void bindToViewport(final Viewport viewport) {
        System.out.println("### bindToViewport(" + viewport + ") called");
        if (this.viewport != null) {
            this.viewport.removeOverlay(this);
        }
        this.viewport = viewport;
        if (viewport != null) {
            viewport.addOverlay(this);
        }
        System.out.println("viewport=" + viewport + " " + this.viewport);
        viewport.getComponent().addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
                int old = activeButton;
                if (!bounds.contains(e.getX(), e.getY()) || e.getY() > bounds.y + 20) {
                    activeButton = -1;
                } else {
                    activeButton = 0;
                    if (minimize.bounds.contains(e.getX(), e.getY())) {
                        activeButton = 1;
                    } else if (maximize.bounds.contains(e.getX(), e.getY())) {
                        activeButton = 2;
                    } else if (close.bounds.contains(e.getX(), e.getY())) {
                        activeButton = 3;
                    }
                }
                if (old != activeButton) {
                    viewport.redrawOverlays();
                }
            }
        });
    }

    public void addWidget(Slider slider) {
        widgets.add(slider);
        System.out.println("viewport=" + viewport);
        viewport.getComponent().addMouseListener(slider.getMouseAdapter());
        viewport.getComponent().addMouseMotionListener(slider.getMouseAdapter());
    }

    public void redraw() {
        viewport.redrawOverlays();
    }

    public void drawOverlay(Viewport viewport) {
        GL gl = viewport.getGL();
        viewport.rasterMode(gl);
        for (Slider slider : widgets) {
            slider.draw(viewport.getGL());
        }
        if (texture == null) {
            texture = TextureIO.newTexture(BORDER_TEXTUREDATA);
            leftCorner = new ActiveArea(texture, 0, 0, 8, 20);
            border = new ActiveArea(texture, 8, 0, 16, 20);
            minimize = new ActiveArea(texture, 30, 0, 16, 20);
            maximize = new ActiveArea(texture, 46, 0, 16, 20);
            close = new ActiveArea(texture, 62, 0, 18, 20);
        }
        texture.bind();
        texture.enable();
        gl.glColor4f(1, 1, 1, 1);
        leftCorner.setPos(bounds.x, bounds.y);
        leftCorner.draw(gl, activeButton == -1 ? 2 : 0);
        border.setPos(bounds.x + 8, bounds.y);
        border.setWidth(bounds.width - 58);
        border.draw(gl, activeButton == -1 ? 2 : 0);
        minimize.setPos(bounds.x + bounds.width - 50, bounds.y);
        minimize.draw(gl, activeButton == -1 ? 2 : activeButton == 1 ? 0 : 1);
        maximize.setPos(bounds.x + bounds.width - 34, bounds.y);
        maximize.draw(gl, activeButton == -1 ? 2 : activeButton == 2 ? 0 : 1);
        close.setPos(bounds.x + bounds.width - 18, bounds.y);
        close.draw(gl, activeButton == -1 ? 2 : activeButton == 3 ? 0 : 1);
        texture.disable();
        gl.glColor4f(0.5f, 0.5f, 0.5f, 0.5f);
        gl.glBegin(GL_TRIANGLE_FAN);
        gl.glVertex2i(bounds.x, bounds.y + 20);
        gl.glVertex2i(bounds.x + bounds.width, bounds.y + 20);
        gl.glVertex2i(bounds.x + bounds.width, bounds.y + bounds.height);
        gl.glVertex2i(bounds.x, bounds.y + bounds.height);
        gl.glEnd();
        viewport.spatialMode(gl);
    }

    public static class ActiveArea {

        private final Texture texture;

        private final TextureCoords[] texCoords = new TextureCoords[3];

        private final Rectangle bounds = new Rectangle();

        ActiveArea(Texture texture, int textureX, int textureY, int width, int height) {
            this.texture = texture;
            bounds.width = width;
            bounds.height = height;
            for (int i = 0; i < 3; i++) {
                texCoords[i] = texture.getSubImageTexCoords(textureX, textureY + i * height, textureX + width, textureY + (i + 1) * height);
            }
        }

        public void setPos(int x, int y) {
            bounds.x = x;
            bounds.y = y;
        }

        public void setWidth(int width) {
            bounds.width = width;
        }

        public void draw(GL gl, int style) {
            gl.glBegin(GL_TRIANGLE_FAN);
            gl.glTexCoord2f(texCoords[style].left(), texCoords[style].top());
            gl.glVertex2i(bounds.x, bounds.y);
            gl.glTexCoord2f(texCoords[style].right(), texCoords[style].top());
            gl.glVertex2i(bounds.x + bounds.width, bounds.y);
            gl.glTexCoord2f(texCoords[style].right(), texCoords[style].bottom());
            gl.glVertex2i(bounds.x + bounds.width, bounds.y + bounds.height);
            gl.glTexCoord2f(texCoords[style].left(), texCoords[style].bottom());
            gl.glVertex2i(bounds.x, bounds.y + bounds.height);
            gl.glEnd();
        }
    }
}
