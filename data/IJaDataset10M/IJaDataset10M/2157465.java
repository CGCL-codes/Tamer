package com.tangledcode.mtm_game.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class Label extends Object {

    public static final int CENTER = 1;

    public static final int LEFT = 2;

    public static final int RIGHT = 3;

    protected String text;

    protected Font font;

    protected Color color;

    protected FontMetrics metrics;

    protected int align;

    protected long lifetime;

    protected long timeTotal;

    public Label(String text, Font font, FontMetrics metrics, Color color) {
        this.text = text;
        this.font = font;
        this.color = color;
        this.metrics = metrics;
        this.align = Label.LEFT;
        this.lifetime = -1L;
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (this.visible) {
            g2d.setFont(this.font);
            g2d.setColor(this.color);
            g2d.drawString(this.text, this.position.getX(), this.position.getY());
        }
    }

    @Override
    public void update(long timeDiff) {
        this.timeTotal += timeDiff;
        if (this.lifetime != -1 && this.timeTotal > this.lifetime) {
            this.visible = false;
            this.destroyed = true;
        }
    }

    public float getWidth() {
        return this.metrics.stringWidth(this.text);
    }

    public float getHeight() {
        return this.metrics.getHeight();
    }

    public void setPosition(float x, float y) {
        this.setPosition(x, y, Label.LEFT);
    }

    public void setPosition(float x, float y, int align) {
        switch(align) {
            case Label.CENTER:
                x -= this.metrics.stringWidth(this.text) / 2;
                break;
            case Label.RIGHT:
                x -= this.metrics.stringWidth(this.text);
                break;
        }
        this.align = align;
        this.visible = true;
        this.position.set(x, y);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(int value) {
        this.setText(String.valueOf(value));
    }

    public void setLifetime(long lifetime) {
        this.lifetime = lifetime;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
