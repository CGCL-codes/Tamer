package org.oobench.patterns.flyweight;

public class DrawContext {

    private Screen screen;

    private int pointer = 0;

    private byte[] screenBuffer;

    public DrawContext(Screen theScreen) {
        screen = theScreen;
        screenBuffer = screen.getScreenBuffer();
    }

    public void next(int step) {
        pointer += step;
    }

    public void next() {
        pointer++;
    }

    public void setCurrentByte(byte b) {
        screenBuffer[pointer] = b;
    }
}
