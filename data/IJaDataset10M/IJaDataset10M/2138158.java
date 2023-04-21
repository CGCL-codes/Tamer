package com.testonica.kickelhahn.core.elements.pin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import com.testonica.kickelhahn.core.Constant;
import com.testonica.kickelhahn.core.elements.board.Board;
import com.testonica.kickelhahn.core.elements.board.Highlightable;
import com.testonica.kickelhahn.core.elements.bschip.BSChip;
import com.testonica.kickelhahn.core.elements.bschip.ShiftRegister;
import com.testonica.kickelhahn.core.elements.pin.indicator.InputIndicatorOutputButton;
import com.testonica.kickelhahn.core.formats.bsdl.BSDLCellDescription;

public class BiDirCellPin extends SimplePin implements ShiftRegister, Highlightable {

    private int outlook = Board.SYSTEM_OVERVIEW_OUTLOOK;

    private char definedValue;

    private char defaultValue;

    private char updatedValue;

    private char value;

    private BSDLCellDescription description;

    protected ShiftRegister next;

    private int index = -1;

    private boolean control;

    private boolean serialPinsOnly;

    private char disableValue = '1';

    private char disableResult = 'U';

    private boolean testMode = false;

    private boolean input = true;

    private boolean enabled = true;

    protected List<BiDirCellPin> controlled = new ArrayList<BiDirCellPin>();

    private Font boldFont;

    private Font plainFont;

    private int length = 1;

    private boolean highlighted = false;

    public BiDirCellPin(BSChip chip, String name, int type, boolean left, int x, int y, char defaultValue, ShiftRegister next) {
        super(chip, type, x, y, name, left);
        this.next = next;
        this.defaultValue = defaultValue;
        reset();
    }

    public void captureValue() {
        if (isInput()) {
            if (getNet() != null) {
                if (getNet().getValue() == 'Z') this.value = 'X'; else this.value = getNet().getValue();
            }
        } else this.value = logicValue;
        refresh();
        if (next != null) next.captureValue();
    }

    public char shiftValue(char value) {
        char oldValue = this.value;
        this.value = value;
        repaint();
        if (next != null) return next.shiftValue(oldValue);
        return oldValue;
    }

    public void updateValue() {
        updatedValue = value;
        if (isTestMode()) {
            if (isInput()) {
                inputValue(updatedValue);
                updateControlled();
                refresh();
            } else outputValue();
        }
        if (next != null) next.updateValue();
    }

    public void paint(Graphics g) {
        if ((g == null) || !getBoardComponent().getBoard().isVisible() || getBoardComponent().getBoard().getIgnoreRepaint()) return;
        if ((g.getClipBounds() != null) && !g.getClipBounds().intersects(getBounds())) return;
        if (boldFont == null) boldFont = g.getFont().deriveFont(Font.BOLD);
        if (plainFont == null) plainFont = g.getFont().deriveFont(Font.PLAIN);
        int x = getX() + getBoardComponent().getLocation().x;
        int y = getY() + getBoardComponent().getLocation().y;
        g.setColor(Color.black);
        if (!serialPinsOnly) paintFunctionTriagle(g, x, y);
        if (!getChip().isShowCellNames()) {
            g.setColor(Color.black);
            if (!serialPinsOnly) {
                g.drawLine(x + 19, y + 10, x + 25, y + 10);
                g.drawLine(x - 6, y + 10, x, y + 10);
            }
            g.drawLine(x + 5, y - 5, x + 5, y + 25);
            if (outlook == Board.DIAGNOSTIC_OUTLOOK) g.setColor(Color.white); else g.setColor(Constant.SHIFT_REGISTER_BACKGROUND);
            g.fillRect(x, y, 10, 20);
            g.setColor(Constant.SHIFT_REGISTER_UPDATE_BACKGROUND);
            g.fillRect(x + 10, y, 10, 20);
            if (highlighted) getChip().getBoard().paintHighlightedRect(this, g);
            g.setColor(Color.black);
            g.drawRect(x, y, 10, 20);
            g.drawRect(x + 10, y, 10, 20);
            if (outlook == Board.DIAGNOSTIC_OUTLOOK) {
                g.setColor(Color.black);
                g.setFont(boldFont);
                g.drawString("" + definedValue, x + 2, y + 15);
            } else {
                g.setFont(plainFont);
                if (value == '0') g.setColor(Color.blue);
                if (value == '1') g.setColor(new Color(0, 128, 0));
                g.drawString("" + value, x + 2, y + 15);
            }
            g.setFont(plainFont);
            g.setColor(Color.black);
            g.drawString("" + updatedValue, x + 12, y + 15);
        } else {
            g.setFont(plainFont);
            g.drawString(getName(), isLeft() ? x : x - 10, y + 15);
        }
        paintHighlightedPin(g);
    }

    protected void paintFunctionTriagle(Graphics g, int x, int y) {
        int xGap = isLeft() ? -10 : 30;
        paintTriangle(g, true, 5, x + xGap, y + 10);
        paintTriangle(g, false, 5, x + xGap, y + 10);
    }

    protected void paintTriangle(Graphics g, boolean left, int sideLength, int x, int y) {
        int[] xp = new int[3];
        int[] yp = new int[3];
        if (!left) sideLength = -sideLength;
        xp[0] = x - sideLength;
        xp[1] = x;
        xp[2] = x - sideLength;
        yp[0] = y + sideLength;
        yp[1] = y;
        yp[2] = y - sideLength;
        g.fillPolygon(xp, yp, 3);
    }

    public char getValue() {
        if (isTestMode()) return value;
        return super.getValue();
    }

    public BSDLCellDescription getDescription() {
        return description;
    }

    public void setDescription(BSDLCellDescription description) {
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setOutlook(int outlook) {
        this.outlook = outlook;
    }

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public void reset() {
        value = defaultValue;
        updatedValue = defaultValue;
        definedValue = defaultValue;
        refresh();
    }

    public void setDefinedValue(char definedValue) {
        this.definedValue = definedValue;
        repaint();
    }

    public void setValue(char value) {
        if (isTestMode()) {
            if (value != disableResult) {
                if (value == 'Z') value = 'X';
                this.value = value;
                refresh();
            }
            return;
        }
        super.setValue(value);
        updateControlled();
    }

    public char getDefinedValue() {
        return definedValue;
    }

    public BSChip getChip() {
        return (BSChip) getBoardComponent();
    }

    public boolean isControl() {
        return control;
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    public void setSerialPinsOnly(boolean pinless) {
        this.serialPinsOnly = pinless;
    }

    public boolean isSerialPinsOnly() {
        return serialPinsOnly;
    }

    public Point getPoint() {
        return new Point((isLeft() ? getX() - 15 : getX() + 35) + getBoardComponent().getLocation().x, getY() + 10 + getBoardComponent().getLocation().y);
    }

    public boolean contains(Point point) {
        if (isSerialPinsOnly()) return false;
        return super.contains(point);
    }

    public char getDisableResult() {
        return disableResult;
    }

    public void setDisableResult(char disableResult) {
        this.disableResult = disableResult;
    }

    public void setDisableValue(char disableValue) {
        this.disableValue = disableValue;
    }

    private void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (isBidir()) {
            input = enabled;
            if (getNet() != null && getNet().getPins() != null) {
                for (int i = 0; i < getNet().getPins().size(); i++) {
                    if (!(getNet().getPin(i) instanceof InputIndicatorOutputButton)) continue;
                    ((InputIndicatorOutputButton) getNet().getPin(i)).setInput(!input);
                }
                getNet().pinValueChanged(this);
            }
        } else setValue(disableResult);
    }

    private void refresh() {
        if (getNet() != null) {
            getNet().pinValueChanged(this);
            repaint();
        }
    }

    public boolean isEnabled() {
        if (isBidir()) return true;
        return enabled;
    }

    public boolean isTestMode() {
        return testMode;
    }

    public boolean isInput() {
        return input;
    }

    public char getDisableValue() {
        return disableValue;
    }

    public void addControlled(BiDirCellPin cell) {
        controlled.add(cell);
    }

    protected void updateControlled() {
        if (!isControl()) return;
        for (int i = 0; i < controlled.size(); i++) controlled.get(i).setEnabled(getValue() != controlled.get(i).getDisableValue());
    }

    public boolean cellContains(Point point) {
        return new Rectangle(getZoomedX(), getZoomedY(), new Double(20 * getBoardComponent().getBoard().getZoom()).intValue(), new Double(20 * getBoardComponent().getBoard().getZoom()).intValue()).contains(point);
    }

    public void setLogicValue(char logicValue) {
        if (isTestMode()) this.logicValue = logicValue; else super.setLogicValue(logicValue);
    }

    public void setNext(ShiftRegister next) {
        this.next = next;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public Rectangle getHighlightBounds() {
        return new Rectangle(getX() + getBoardComponent().getX(), getY() + getBoardComponent().getY(), 20, 20);
    }

    public char getDefaultValue() {
        return defaultValue;
    }

    public char getUpdatedValue() {
        return updatedValue;
    }
}
