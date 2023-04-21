package com.ramp.microswing;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import com.ramp.microswing.plaf.LookAndFeel;
import com.ramp.microswing.plaf.UIManager;

public class JDisplayText extends JTextComponent {

    protected String text;

    private String currentText;

    private ProcessText process;

    private Vector lines;

    private int yText = -1;

    private boolean animate;

    private boolean autoAdjustable;

    private String subString = null;

    private int currentLine = 0;

    private int indexes = 0;

    private int viewLines = 4;

    public JDisplayText(String text) {
        this.text = text;
        updateUI();
        processText();
    }

    public JDisplayText(String text, int width, int lines) {
        this.text = text;
        viewLines = lines;
        this.width = width;
        updateUI();
        processText();
    }

    public JDisplayText(String text, boolean animate) {
        this.animate = animate;
        this.text = text;
        updateUI();
        processText();
    }

    public JDisplayText(String text, boolean animate, int width, int lines) {
        this.animate = animate;
        this.text = text;
        viewLines = lines;
        this.width = width;
        updateUI();
        processText();
    }

    public JDisplayText(String text, boolean autoAdjustable, boolean animate) {
        this.autoAdjustable = autoAdjustable;
        this.animate = animate;
        this.text = text;
        updateUI();
        processText();
    }

    public JDisplayText(String text, boolean autoAdjustable, boolean animate, int width, int lines) {
        this.autoAdjustable = autoAdjustable;
        this.animate = animate;
        this.text = text;
        viewLines = lines;
        this.width = width;
        updateUI();
        processText();
    }

    private void processText() {
        editModeSupport = true;
        process = new ProcessText(text);
        if (autoAdjustable) {
            if (width == 0) {
                width = MSFThreadController.getInstance().getWidth();
            }
            lines = process.getLines(this, width, 0);
            if (height == 0) {
                height = font.getHeight() * lines.size();
            }
        } else {
            if (width == 0) {
                width = MSFThreadController.getInstance().getWidth();
            }
            if (height == 0) {
                height = font.getHeight() * viewLines;
            }
            lines = process.getLines(this, width, height);
        }
    }

    public void resetAnimation() {
        if (animate) {
            indexes = lines.size();
            currentLine = lines.size();
        }
    }

    public int keyPressed(int key) {
        if (editMode && focusable) {
            int gaction = getGameAction(key);
            switch(gaction) {
                case Canvas.UP:
                    if (process.getPage() > 0) {
                        lines = process.back();
                        if (animate) {
                            subString = null;
                            indexes = 0;
                            currentLine = 0;
                            yText = getY() + 1;
                        }
                    }
                    return 1;
                case Canvas.DOWN:
                    if (animate) {
                        if (currentLine < lines.size()) {
                            resetAnimation();
                        } else {
                            if (process.hasMoreTextLines()) {
                                subString = null;
                                indexes = 0;
                                currentLine = 0;
                                yText = getY() + 1;
                                lines = process.next();
                            }
                        }
                        return 1;
                    }
                    if (process.hasMoreTextLines()) {
                        lines = process.next();
                    }
                    return 1;
                case Canvas.FIRE:
                    editMode = false;
                    return 0;
            }
            return 0;
        } else {
            return 0;
        }
    }

    public void paintComponent(Graphics g) {
        if (backgroundPaint) {
            g.setColor(background);
            g.fillRect(x, y, width, height);
        }
        g.setFont(font);
        g.setColor(foreground);
        if (!animate) {
            yText = getY() + 1;
            for (int i = 0; i < lines.size(); i++) {
                g.drawString((String) lines.elementAt(i), getX() + 2, yText, Graphics.LEFT | Graphics.TOP);
                yText += font.getHeight();
            }
        } else {
            if (yText == -1) {
                yText = getY() + 1;
            }
            if (subString == null) {
                subString = (String) lines.elementAt(currentLine);
            }
            int yl = getY() + 1;
            for (int i = 0; i < currentLine; i++) {
                g.drawString((String) lines.elementAt(i), getX() + 2, yl, Graphics.LEFT | Graphics.TOP);
                yl += font.getHeight();
            }
            if (currentLine < lines.size()) {
                g.drawString(subString.substring(0, indexes), getX() + 2, yText, Graphics.LEFT | Graphics.TOP);
            }
            if (indexes < subString.length()) {
                indexes++;
            } else {
                if (currentLine < lines.size() - 1) {
                    currentLine++;
                    subString = (String) lines.elementAt(currentLine);
                } else {
                    currentLine = lines.size();
                }
                indexes = 0;
                yText += font.getHeight();
            }
        }
    }

    protected void paint(Graphics g) {
        g.setClip(x, y, width, height);
        if (behaviour == null) {
            paintComponent(g);
        } else {
            paintBehaviour(g);
        }
    }

    public String getText() {
        return process.getString();
    }

    public int getStringWidth() {
        return font.stringWidth(process.getString());
    }

    public int getLines() {
        return lines.size();
    }

    public void setText(String text) {
        this.text = text;
        if (!text.equals(currentText)) {
            if (animate) {
                yText = -1;
                subString = null;
                currentLine = 0;
                indexes = 0;
            }
            processText();
            currentText = text;
        }
    }

    protected void updateUI() {
        currentText = text;
        font = UIManager.getFont(LookAndFeel.displayTextFont);
        foreground = UIManager.getColor(LookAndFeel.displayTextForeground);
        background = UIManager.getColor(LookAndFeel.displayTextBackground);
    }
}
