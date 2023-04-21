package com.peterhi.client.beans.wboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public class Text extends AbstractShape {

    public static final int JUSTIFY = -1;

    private String text;

    private int x;

    private int y;

    private int width;

    private int height;

    private String fontName = "Arial";

    private int fontSize = 16;

    private boolean bold;

    private boolean italic;

    private int align;

    private boolean underline;

    private boolean strike;

    public int getHeight() {
        evalHeight();
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public boolean isStrike() {
        return strike;
    }

    public void setStrike(boolean strike) {
        this.strike = strike;
    }

    public boolean isUnderline() {
        return underline;
    }

    public void setUnderline(boolean underline) {
        this.underline = underline;
    }

    @Override
    public void drawShape(PaintEvent e) {
        TextLayout lay = new TextLayout(e.display);
        int style = SWT.NORMAL;
        if (isBold()) {
            style |= SWT.BOLD;
        }
        if (isItalic()) {
            style |= SWT.ITALIC;
        }
        Font f = new Font(e.display, getFontName(), getFontSize(), style);
        lay.setText(getText());
        lay.setWidth(getWidth());
        lay.setFont(f);
        if (getAlign() == JUSTIFY) {
            lay.setJustify(true);
        } else {
            lay.setAlignment(getAlign());
        }
        if (underline || strike) {
            TextStyle s = new TextStyle(f, null, null);
            s.underline = underline;
            s.strikeout = strike;
            lay.setStyle(s, 0, text.length());
        }
        lay.draw(e.gc, getX(), getY());
        f.dispose();
        lay.dispose();
    }

    private void evalHeight() {
        TextLayout lay = new TextLayout(Display.getDefault());
        int style = SWT.NORMAL;
        if (isBold()) {
            style |= SWT.BOLD;
        }
        if (isItalic()) {
            style |= SWT.ITALIC;
        }
        Font f = new Font(Display.getDefault(), getFontName(), getFontSize(), style);
        lay.setText(getText());
        lay.setWidth(getWidth());
        lay.setFont(f);
        if (getAlign() == JUSTIFY) {
            lay.setJustify(true);
        } else {
            lay.setAlignment(getAlign());
        }
        if (underline || strike) {
            TextStyle s = new TextStyle(f, null, null);
            s.underline = underline;
            s.strikeout = strike;
            lay.setStyle(s, 0, text.length());
        }
        setHeight(lay.getBounds().height);
        f.dispose();
        lay.dispose();
    }
}
