package com.googlecode.boringengine;

import com.googlecode.boringengine.internal.Renderer;

public class Render {

    private static Renderer r;

    public static void init(Renderer renderer) {
        r = renderer;
    }

    public static void setPosition(int x, int y) {
        r.setPosition(x, y);
    }

    public static void moveOverWithScale(int x, int y) {
        r.moveOverWithScale(x, y);
    }

    public static void moveOverWithoutScale(int x, int y) {
        r.moveOverWithoutScale(x, y);
    }

    public static void setScale(double scale) {
        r.setScale(scale);
    }

    public static void addScale(double scale) {
        r.addScale(scale);
    }

    public static void setRotation(double rotation) {
        r.setRotation(rotation);
    }

    public static void addRotation(double rotation) {
        r.addRotation(rotation);
    }

    public static void setFlip(boolean flip) {
        r.setFlip(flip);
    }

    public static void addFlip(boolean flip) {
        r.addFlip(flip);
    }

    public static void setAlpha(int alpha) {
        r.setAlpha(alpha);
    }

    public static int getX() {
        return r.getX();
    }

    public static int getY() {
        return r.getY();
    }

    public static double getScale() {
        return r.getScale();
    }

    public static double getRotation() {
        return r.getRotation();
    }

    public static boolean isFlipped() {
        return r.isFlipped();
    }

    public static int getAlpha() {
        return r.getAlpha();
    }

    public static void clearScreen() {
        r.clearScreen();
    }

    public static void clip(int x, int y, int width, int height) {
        r.clip(x, y, width, height);
    }

    public static Sprite createSpriteFromScreen(int x, int y, int width, int height) {
        return r.createSpriteFromScreen(x, y, width, height);
    }

    public static Sprite createSprite(String sprite, int width, int height) {
        return r.createSprite(sprite, width, height);
    }

    public static void drawRect(int w, int h, int color) {
        r.drawRect(w, h, color);
    }

    public static void drawSprite(Sprite sprite) {
        r.drawSprite(sprite);
    }

    public static boolean takeScreenshot(String screenshotName) {
        return r.takeScreenshot(screenshotName);
    }

    public static void unClip() {
        r.unClip();
    }
}
