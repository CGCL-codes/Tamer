package com.igentics.horsepower;

import javax.microedition.lcdui.Graphics;

/**
 * Renders a number with old style red LED clusters
 * @author Simon Williams
 */
public class VectorRedLedNumberRenderer implements NumberRenderer {

    private static final int LED_WIDTH_MIN = 6;

    private static final int LED_HEIGHT_MIN = 11;

    private int _screenWidth;

    private int _screenPaddingX = 3;

    private int _screenPaddingY = 3;

    private int _ledPaddingX = 3;

    private int _ledPaddingY = 3;

    private int _ledWidth = 6;

    private int _ledHeight = 11;

    private int _ledHeightCentre;

    private int _ledSegmentWidth = 1;

    public VectorRedLedNumberRenderer() {
    }

    public void renderNumber(Graphics g, String number) {
        int startX = (_screenWidth - number.length() * getLedBlockWidth()) / 2;
        g.setColor(0);
        g.fillRect(0, 0, _screenWidth, getLedBlockHeight() + 2 * _screenPaddingY);
        for (int i = 0; i < number.length(); i++) {
            renderChar(g, number.charAt(i), startX + i * getLedBlockWidth(), _screenPaddingY);
        }
    }

    private void render5(Graphics g, char c, int offsetX, int offsetY) {
        renderElementA(g, c, offsetX, offsetY);
        renderElementC(g, c, offsetX, offsetY);
        renderElementD(g, c, offsetX, offsetY);
        renderElementF(g, c, offsetX, offsetY);
        renderElementG(g, c, offsetX, offsetY);
    }

    private void render6(Graphics g, char c, int offsetX, int offsetY) {
        renderElementA(g, c, offsetX, offsetY);
        renderElementC(g, c, offsetX, offsetY);
        renderElementD(g, c, offsetX, offsetY);
        renderElementE(g, c, offsetX, offsetY);
        renderElementF(g, c, offsetX, offsetY);
        renderElementG(g, c, offsetX, offsetY);
    }

    private void render7(Graphics g, char c, int offsetX, int offsetY) {
        renderElementA(g, c, offsetX, offsetY);
        renderElementB(g, c, offsetX, offsetY);
        renderElementC(g, c, offsetX, offsetY);
    }

    private void render8(Graphics g, char c, int offsetX, int offsetY) {
        renderElementA(g, c, offsetX, offsetY);
        renderElementB(g, c, offsetX, offsetY);
        renderElementC(g, c, offsetX, offsetY);
        renderElementD(g, c, offsetX, offsetY);
        renderElementE(g, c, offsetX, offsetY);
        renderElementF(g, c, offsetX, offsetY);
        renderElementG(g, c, offsetX, offsetY);
    }

    private void render9(Graphics g, char c, int offsetX, int offsetY) {
        renderElementA(g, c, offsetX, offsetY);
        renderElementB(g, c, offsetX, offsetY);
        renderElementC(g, c, offsetX, offsetY);
        renderElementD(g, c, offsetX, offsetY);
        renderElementF(g, c, offsetX, offsetY);
        renderElementG(g, c, offsetX, offsetY);
    }

    private void setElementColour(Graphics g) {
        g.setColor(128, 0, 0);
    }

    private void renderA(Graphics g, char c, int offsetX, int offsetY) {
    }

    private void renderB(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderC(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderChar(Graphics g, char c, int offsetX, int offsetY) {
        renderBackground(g, offsetX, offsetY);
        switch(c) {
            case '0':
                render0(g, c, offsetX, offsetY);
                break;
            case '1':
                render1(g, c, offsetX, offsetY);
                break;
            case '2':
                render2(g, c, offsetX, offsetY);
                break;
            case '3':
                render3(g, c, offsetX, offsetY);
                break;
            case '4':
                render4(g, c, offsetX, offsetY);
                break;
            case '5':
                render5(g, c, offsetX, offsetY);
                break;
            case '6':
                render6(g, c, offsetX, offsetY);
                break;
            case '7':
                render7(g, c, offsetX, offsetY);
                break;
            case '8':
                render8(g, c, offsetX, offsetY);
                break;
            case '9':
                render9(g, c, offsetX, offsetY);
                break;
            case 'A':
                renderA(g, c, offsetX, offsetY);
                break;
            case 'B':
            case 'b':
                renderB(g, c, offsetX, offsetY);
                break;
            case 'C':
                renderC(g, c, offsetX, offsetY);
                break;
            case 'D':
                renderD(g, c, offsetX, offsetY);
                break;
            case 'E':
                renderE(g, c, offsetX, offsetY);
                break;
            case 'e':
                renderLowerE(g, c, offsetX, offsetY);
                break;
            case 'F':
                renderF(g, c, offsetX, offsetY);
                break;
            case 'H':
                renderH(g, c, offsetX, offsetY);
                break;
            case 'L':
                renderL(g, c, offsetX, offsetY);
                break;
            case 'P':
                renderP(g, c, offsetX, offsetY);
                break;
            case '-':
                renderDash(g, c, offsetX, offsetY);
                break;
            case 'r':
                renderR(g, c, offsetX, offsetY);
                break;
            case 'o':
                renderLowerO(g, c, offsetX, offsetY);
                break;
        }
    }

    private int getLedBlockWidth() {
        return _ledWidth + 2 * _ledPaddingX;
    }

    private int getLedBlockHeight() {
        return _ledHeight + 2 * _ledPaddingY;
    }

    private void renderBackground(Graphics g, int offsetX, int offsetY) {
        g.setColor(32, 0, 0);
        g.fillRect(offsetX, offsetY, getLedBlockWidth() - 1, getLedBlockHeight() - 1);
    }

    private void render0(Graphics g, char c, int offsetX, int offsetY) {
        renderElementA(g, c, offsetX, offsetY);
        renderElementB(g, c, offsetX, offsetY);
        renderElementC(g, c, offsetX, offsetY);
        renderElementD(g, c, offsetX, offsetY);
        renderElementE(g, c, offsetX, offsetY);
        renderElementF(g, c, offsetX, offsetY);
    }

    private void render1(Graphics g, char c, int offsetX, int offsetY) {
        renderElementB(g, c, offsetX, offsetY);
        renderElementC(g, c, offsetX, offsetY);
    }

    private void render2(Graphics g, char c, int offsetX, int offsetY) {
        renderElementA(g, c, offsetX, offsetY);
        renderElementB(g, c, offsetX, offsetY);
        renderElementD(g, c, offsetX, offsetY);
        renderElementE(g, c, offsetX, offsetY);
        renderElementG(g, c, offsetX, offsetY);
    }

    private void render3(Graphics g, char c, int offsetX, int offsetY) {
        renderElementA(g, c, offsetX, offsetY);
        renderElementB(g, c, offsetX, offsetY);
        renderElementC(g, c, offsetX, offsetY);
        renderElementD(g, c, offsetX, offsetY);
        renderElementG(g, c, offsetX, offsetY);
    }

    private void render4(Graphics g, char c, int offsetX, int offsetY) {
        renderElementB(g, c, offsetX, offsetY);
        renderElementC(g, c, offsetX, offsetY);
        renderElementF(g, c, offsetX, offsetY);
        renderElementG(g, c, offsetX, offsetY);
    }

    private void renderD(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderDash(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderE(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderElementA(Graphics g, char c, int offsetX, int offsetY) {
        setElementColour(g);
        g.drawLine(offsetX + _ledPaddingX, offsetY + _ledPaddingY, offsetX + _ledPaddingX + _ledWidth, offsetY + _ledPaddingY);
    }

    private void renderElementB(Graphics g, char c, int offsetX, int offsetY) {
        setElementColour(g);
        g.drawLine(offsetX + _ledPaddingX + _ledWidth, offsetY + _ledPaddingY, offsetX + _ledPaddingX + _ledWidth, offsetY + _ledPaddingY + _ledHeightCentre);
    }

    private void renderElementC(Graphics g, char c, int offsetX, int offsetY) {
        g.drawLine(offsetX + _ledPaddingX + _ledWidth, offsetY + _ledPaddingY + _ledHeightCentre, offsetX + _ledPaddingX + _ledWidth, offsetY + _ledPaddingY + _ledHeight);
    }

    private void renderElementD(Graphics g, char c, int offsetX, int offsetY) {
        setElementColour(g);
        g.drawLine(offsetX + _ledPaddingX, offsetY + _ledPaddingY + _ledHeight, offsetX + _ledPaddingX + _ledWidth, offsetY + _ledPaddingY + _ledHeight);
    }

    private void renderElementE(Graphics g, char c, int offsetX, int offsetY) {
        setElementColour(g);
        g.drawLine(offsetX + _ledPaddingX, offsetY + _ledPaddingY + _ledHeightCentre, offsetX + _ledPaddingX, offsetY + _ledPaddingY + _ledHeight);
    }

    private void renderElementF(Graphics g, char c, int offsetX, int offsetY) {
        setElementColour(g);
        g.drawLine(offsetX + _ledPaddingX, offsetY + _ledPaddingY, offsetX + _ledPaddingX, offsetY + _ledPaddingY + _ledHeightCentre);
    }

    private void renderElementG(Graphics g, char c, int offsetX, int offsetY) {
        setElementColour(g);
        g.drawLine(offsetX + _ledPaddingX, offsetY + _ledPaddingY + _ledHeightCentre, offsetX + _ledPaddingX + _ledWidth, offsetY + _ledPaddingY + _ledHeightCentre);
    }

    public int getScreenHeight() {
        return getLedBlockHeight() + 2 * _screenPaddingY;
    }

    /**
     * Max number of characters supported across the screen width
     * @param width Screen width in pixels
     * @return Num of chars
     */
    public int getMaxCharacters() {
        return (_screenWidth - 2 * _screenPaddingX) / getLedBlockWidth();
    }

    public void reset(int maxWidth, int maxHeight, int minChars) {
        _screenWidth = maxWidth;
        _ledWidth = maxWidth / minChars / 2;
        if (_ledWidth < LED_HEIGHT_MIN) {
            _ledWidth = LED_WIDTH_MIN;
        }
        _ledHeight = _ledWidth * LED_HEIGHT_MIN / LED_WIDTH_MIN;
        if (_ledHeight < LED_HEIGHT_MIN) {
            _ledHeight = LED_HEIGHT_MIN;
            _ledWidth = _ledHeight * LED_WIDTH_MIN / LED_HEIGHT_MIN;
        }
        _ledPaddingX = _ledWidth / 2;
        _ledPaddingY = _ledHeight / 2;
        _ledHeightCentre = (int) (_ledHeight / 2);
        _screenPaddingY = _ledPaddingY / 2;
        _ledSegmentWidth = _ledWidth / LED_WIDTH_MIN;
    }

    private void renderF(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderH(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderL(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderLowerE(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderLowerO(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderP(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void renderR(Graphics g, char c, int offsetX, int offsetY) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
