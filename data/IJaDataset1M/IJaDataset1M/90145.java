package com.ramp.microswing;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import com.pixels59.font.CustomFont;
import com.ramp.microswing.event.KeyListener;
import com.ramp.microswing.geom.Rectangle;

public abstract class Component {

    public static final byte BOTTOM_ALIGNMENT = 0;

    public static final byte TOP_ALIGNMENT = 1;

    public static final byte CENTER_ALIGNMENT = 2;

    public static final byte LEFT_ALIGNMENT = 3;

    public static final byte RIGHT_ALIGNMENT = 4;

    protected Font font;

    protected CustomFont customFont;

    protected int foreground;

    protected int background;

    public int x;

    public int y;

    public int width;

    public int height;

    protected boolean focusable = true;

    protected boolean enabled = true;

    protected boolean visible = true;

    protected boolean focusPaint;

    protected boolean backgroundPaint = true;

    protected KeyListener keyListener;

    protected boolean editMode;

    protected boolean editModeSupport;

    protected boolean itemSelectable;

    protected boolean modalComponent;

    protected Graphics graphics;

    protected boolean inEvent;

    protected boolean focus;

    protected int borderColor;

    protected Rectangle bounds;

    protected int x_orig;

    protected int y_orig;

    protected int scrollY;

    private void initCoords() {
        this.x_orig = x;
        this.y_orig = y;
        scrollY = y_orig;
    }

    public void addKeyListener(KeyListener listener) {
        this.keyListener = listener;
    }

    public int getBackground() {
        return background;
    }

    public Font getFont() {
        return font;
    }

    public int getForeground() {
        return foreground;
    }

    public int getHeight() {
        return height;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public int getWidth() {
        return width;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isFocusable() {
        return focusable;
    }

    public boolean isFocusPaint() {
        return focusPaint;
    }

    public boolean isVisible() {
        return visible;
    }

    protected int keyPressed(int key) {
        return 0;
    }

    protected int keyReleased(int key) {
        return 0;
    }

    protected void paint(Graphics g) {
    }

    protected void paintAll(Graphics g) {
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    public void setFocusPaint(boolean focusPaint) {
        this.focusPaint = focusPaint;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setForeground(int foreground) {
        this.foreground = foreground;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        initCoords();
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public boolean isEditModeSupport() {
        return editModeSupport;
    }

    public void setEditModeSupport(boolean editModeSupport) {
        this.editModeSupport = editModeSupport;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle getBounds() {
        if (bounds == null) {
            bounds = new Rectangle(x, y, width, height);
        } else {
            bounds.setX(x);
            bounds.setY(y);
            bounds.setWidth(width);
            bounds.setHeight(height);
        }
        return bounds;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    protected int getGameAction(int key) {
        return MSFThreadController.getInstance().getGameAction(key);
    }

    public CustomFont getCustomFont() {
        return customFont;
    }

    public void setCustomFont(CustomFont customFont) {
        this.customFont = customFont;
    }

    public int getHorizontalSpace() {
        return x + width;
    }

    public int getVerticalSpace() {
        return y + height;
    }

    protected void paintComponentBorder(Graphics g) {
    }

    public boolean isBackgroundPaint() {
        return backgroundPaint;
    }

    public void setBackgroundPaint(boolean backgroundPaint) {
        this.backgroundPaint = backgroundPaint;
    }

    public int getXCenter() {
        return (MSFThreadController.getInstance().getWidth() - width) / 2;
    }
}
