package com.qotsa.lib.String;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.StringItem;

/**
 *
 * @author Francisco
 */
public class StringItemRoll extends StringItem {

    private boolean scroll = false;

    private Display display;

    private String textValue;

    private int width;

    private int pixelsChar;

    private Scroll scrollClass;

    private int idx = 0;

    private String strAux = "";

    /** Creates a new instance of StringItemRoll */
    public StringItemRoll(String label, String text, int appearanceMode, Display display, int pixelsChar) {
        super(label, text, appearanceMode);
        this.display = display;
        this.pixelsChar = pixelsChar;
        scrollClass = new Scroll();
        scrollClass.start();
    }

    public boolean isScroll() {
        return scroll;
    }

    public void setScroll(boolean scrool) {
        this.scroll = scrool;
    }

    public void superSetText(String text) {
        super.setText(text);
    }

    public void setText(String text) {
        if (!text.equals(getTextValue())) {
            width = display.getCurrent().getWidth();
            int strWidth = (text.length() + this.getLabel().length()) * pixelsChar;
            setTextValue(text);
            setIdx(0);
            setStrAux("");
            int strLabelWidth = getLabel().length() * pixelsChar;
            if (strWidth > width) {
                superSetText(text.substring(0, (width - strLabelWidth) / pixelsChar));
                setScroll(true);
                scrollClass.notifyScroll();
            } else {
                superSetText(text);
                setScroll(false);
            }
        }
    }

    class Scroll extends Thread {

        public void notifyScroll() {
            synchronized (this) {
                this.notify();
            }
        }

        public void scrollItem() {
            while (true) {
                synchronized (this) {
                    try {
                        if (isScroll()) this.wait(500); else this.wait();
                    } catch (InterruptedException ex) {
                        System.out.println(ex.toString());
                    }
                }
                setStrAux(getTextValue().substring(getIdx()));
                idx++;
                int strLabelWidth = getLabel().length() * pixelsChar;
                int strAuxWidth = getStrAux().length() * pixelsChar;
                int strWidth = strAuxWidth + strLabelWidth;
                if (getStrAux().length() == 1) {
                    setIdx(0);
                    setStrAux("");
                }
                if (strWidth > width) superSetText(getStrAux().substring(0, ((width - strLabelWidth) / pixelsChar))); else superSetText(getStrAux());
            }
        }

        public void run() {
            scrollItem();
        }
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getStrAux() {
        return strAux;
    }

    public void setStrAux(String strAux) {
        this.strAux = strAux;
    }
}
