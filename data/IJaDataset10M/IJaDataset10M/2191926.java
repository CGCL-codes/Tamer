package com.ramp.microswing;

import java.util.Vector;

public class ProcessText {

    private Vector lines;

    private String txt;

    private int lastCharIndex;

    private Vector indexes;

    private int width;

    private int height;

    private int page = 0;

    private Component component;

    public ProcessText(String text) {
        txt = text;
        init();
    }

    private void init() {
        lines = new Vector();
        indexes = new Vector();
        indexes.addElement(new Integer(0));
    }

    public boolean hasMoreTextLines() {
        return lastCharIndex < txt.length() - 1;
    }

    public Vector next() {
        if (height > 0) {
            removeText();
            indexes.addElement(new Integer(lastCharIndex));
            page++;
            return getLines(component, width, height);
        } else {
            return lines;
        }
    }

    public Vector back() {
        if (page - 1 >= 0) {
            if (page > 0) {
                indexes.removeElementAt(page);
            }
            removeText();
            page--;
            lastCharIndex = ((Integer) indexes.elementAt(page)).intValue();
            return getLines(component, width, height);
        } else {
            return lines;
        }
    }

    public void removeText() {
        current_height = 0;
        lines.removeAllElements();
    }

    private int current_height = 0;

    private String buff;

    private String tc;

    public Vector getLines(Component comp, int w, int h) {
        this.component = comp;
        width = w;
        height = h;
        txt = txt.trim();
        buff = new String("");
        tc = "";
        for (int i = lastCharIndex; i < txt.length(); i++) {
            if (stringWidth(tc + txt.charAt(i)) >= w - 8) {
                buff = tc + txt.charAt(i);
                validate(i);
                buff = "";
            } else {
                if (txt.charAt(i) != ' ' && txt.charAt(i) != '\n') {
                    tc += txt.charAt(i);
                } else {
                    validate(i);
                }
                if (i == txt.length() - 1 && !tc.equals("")) {
                    lines.addElement(buff + tc);
                    lastCharIndex = i;
                }
                if (h >= getHeight() && current_height >= h && h > 0) {
                    lastCharIndex = i;
                    break;
                }
            }
        }
        return lines;
    }

    private void validate(int i) {
        tc += txt.charAt(i);
        String temp = buff + tc;
        if (stringWidth(temp) <= width && !tc.endsWith("\n")) {
            buff += tc;
        } else {
            if (tc.endsWith("\n")) {
                buff += tc;
                tc = "";
            }
            jumpLine(buff);
            buff = tc;
        }
        tc = "";
    }

    private void jumpLine(String buff) {
        buff = buff.trim();
        lines.addElement(buff);
        current_height += getHeight();
    }

    public int getLinesCount() {
        return lines.size();
    }

    private int stringWidth(String str) {
        return component.font.stringWidth(str);
    }

    private int getHeight() {
        return component.font.getHeight();
    }

    public int getLastCharIndex() {
        return lastCharIndex;
    }

    public int getPage() {
        return page;
    }

    public String getString() {
        String txt = "";
        for (int i = 0; i < lines.size(); i++) {
            txt += lines.elementAt(i) + "\n";
        }
        return txt;
    }
}
