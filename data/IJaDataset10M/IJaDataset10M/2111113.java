package org.brickshadow.roboglk;

/**
 * The base class for text buffer and text grid windows. It provides
 * do-nothing implementations for methods not applicable to text windows.
 */
public abstract class AbstractGlkTextWindow implements GlkWindow {

    @Override
    public final void setArrangement(int method, int size, GlkWindow key) {
    }

    @Override
    public final boolean drawImage(BlorbResource bres, int x, int y) {
        return false;
    }

    @Override
    public final boolean drawImage(BlorbResource bres, int x, int y, int width, int height) {
        return false;
    }

    @Override
    public final void setBackgroundColor(int color) {
    }

    @Override
    public final void eraseRect(int left, int top, int width, int height) {
    }

    @Override
    public final void fillRect(int color, int left, int top, int width, int height) {
    }

    /**
     * Handles normal characters during single-character input.
     * 
     * @param c a character.
     */
    public abstract void recordKey(char c);

    /**
     * Handles special keys during single-character input.
     * 
     * @param c a keycode
     */
    public abstract void recordKey(int c);

    /**
     * Handles line input.
     */
    public abstract void recordLine(char[] line, int len, boolean isEvent);
}
