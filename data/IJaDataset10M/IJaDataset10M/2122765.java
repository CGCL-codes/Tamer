package com.simpledata.bc.uitools;

import java.awt.*;
import java.awt.BorderLayout;
import javax.swing.*;
import org.apache.log4j.Logger;
import com.simpledata.bc.uicomponents.tools.NamedTitleDescriptionEditor;

/**
 * A small class to handle sized multiline label
 */
public class SLabel extends JPanel {

    private static final Logger log = Logger.getLogger(SLabel.class);

    private JLabel label;

    private String text;

    private int maxLength;

    public SLabel() {
        this("");
    }

    public SLabel(String text) {
        this("", -1);
    }

    public SLabel(String text, int maxLength) {
        super(new BorderLayout());
        this.text = text;
        this.maxLength = maxLength;
        initComponents();
    }

    public void setText(String text) {
        this.text = text;
        refresh();
    }

    public String getText() {
        return text;
    }

    public void setMaxLength(int length) {
        maxLength = length;
        refresh();
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getHTMLFormatedText() {
        return label.getText();
    }

    private JLabel label() {
        if (label == null) {
            label = new JLabel();
        }
        return label;
    }

    public Font getFont() {
        Font f = label().getFont();
        return f;
    }

    public void setFont(Font f) {
        label().setFont(f);
    }

    public Color getBackground() {
        Color c = label().getBackground();
        if (c == null) {
            c = super.getBackground();
        }
        return c;
    }

    public void setBackground(Color c) {
        label().setBackground(c);
    }

    public Color getForeground() {
        Color c = label().getForeground();
        if (c == null) {
            c = super.getForeground();
        }
        return c;
    }

    public void setForeground(Color c) {
        label().setForeground(c);
    }

    private void initComponents() {
        label().setOpaque(true);
        refresh();
    }

    private void refresh() {
        removeAll();
        if (text == null) text = "";
        text = replace(text, "<BR>", "\n");
        text = replace(text, "<br>", "\n");
        text = replace(text, "<Br>", "\n");
        text = replace(text, "<bR>", "\n");
        StringBuffer revised = new StringBuffer();
        if (maxLength > 0) {
            FontMetrics fm = label.getFontMetrics(label.getFont());
            if (!(maxLength > fm.getMaxAdvance())) {
                log.error("The maximum size of this label is inferior to the" + " maximum advance of a character of its font...");
                return;
            }
            int totWidth = 0;
            boolean cont;
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    totWidth = 0;
                    revised.append("<BR>");
                } else {
                    cont = true;
                    if (totWidth > NamedTitleDescriptionEditor.PREF_LABEL_WIDTH) {
                        if (c == ' ') {
                            totWidth = 0;
                            revised.append("<BR>");
                            cont = false;
                        }
                    }
                    if (cont) {
                        totWidth += fm.charWidth(c);
                        revised.append(c);
                    }
                }
            }
        } else {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c == '\n') {
                    revised.append("<BR>");
                } else {
                    revised.append(c);
                }
            }
        }
        String newText = "<html><body>" + revised.toString() + "</body></html>";
        label.setText(newText);
        add(label, BorderLayout.CENTER);
    }

    /**
     * @param text The String we want to modify
     * @param originalPattern the String that should be replaced
     * @param destPattern the pattern we want to replace it with
     * @return a new modified String
     */
    private static String replace(String text, String originalPattern, String destPattern) {
        int start = text.indexOf(originalPattern);
        if (start == -1) return text;
        int lf = originalPattern.length();
        char[] targetChars = text.toCharArray();
        StringBuffer buffer = new StringBuffer();
        int copyFrom = 0;
        while (start != -1) {
            buffer.append(targetChars, copyFrom, start - copyFrom);
            buffer.append(destPattern);
            copyFrom = start + lf;
            start = text.indexOf(originalPattern, copyFrom);
        }
        buffer.append(targetChars, copyFrom, targetChars.length - copyFrom);
        return buffer.toString();
    }
}
