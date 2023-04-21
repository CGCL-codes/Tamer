package net.rmanager.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class DrawableText extends DrawableObject implements Drawable {

    private Color color;

    private String text = "";

    private boolean bold = false;

    private String value = null;

    private DrawableImage icon;

    public DrawableText(String id, int x, int y) {
        this(id, null, Color.black, x, y, false, "");
    }

    public DrawableText(String text, Color color, int x, int y, boolean bold) {
        this(null, text, color, x, y, bold, null);
    }

    public DrawableText(String id, DrawableImage icon, int x, int y, String value) {
        super.setId(id);
        this.setLocationX(x);
        this.setLocationY(y);
        this.setIcon(icon);
        this.setValue(value);
    }

    public DrawableText(String id, String text, Color color, int x, int y, boolean bold, String value) {
        super.setId(id);
        this.setLocationX(x);
        this.setLocationY(y);
        this.setColor(color);
        this.setText(text);
        this.bold = bold;
        this.value = value;
    }

    public void setIcon(DrawableImage img) {
        this.icon = img;
        icon.setLocationX(getLocationX());
        icon.setLocationY(getLocationY());
    }

    public void draw(Graphics2D g) {
        if (icon != null) {
            icon.draw(g);
        }
        Font font = null;
        if (bold) {
            font = new Font("Arial", 1, 14);
        } else {
            font = new Font("Arial", 0, 14);
        }
        g.setFont(font);
        g.setColor(getColor());
        if (hasValue()) {
            if (icon != null) {
                int dimX = icon.getDimensionX();
                int dimY = Math.round(icon.getDimensionY() / 2);
                g.drawString(getText() + getValue(), getLocationX() + dimX, getLocationY() + dimY);
            } else {
                g.drawString(getText() + getValue(), getLocationX(), getLocationY());
            }
        } else {
            g.drawString(getText(), getLocationX(), getLocationY());
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean hasValue() {
        if (value != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DrawableText " + getText());
        sb.append(" LocationX: " + getLocationX());
        sb.append(" LocationY: " + getLocationY());
        sb.append(" Color: " + getColor());
        sb.append(" Bold: " + bold);
        return sb.toString();
    }
}
