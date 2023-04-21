package org.dlib.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;

public class TTextArea extends JScrollPane {

    private JTextArea txtArea;

    public TTextArea() {
        this(10, 10);
    }

    public TTextArea(int rows, int cols) {
        this("", rows, cols);
    }

    public TTextArea(String text, int rows, int cols) {
        this(text, rows, cols, false);
    }

    public TTextArea(String text, int rows, int cols, boolean isLabel) {
        txtArea = new JTextArea("", rows, cols) {

            public void paintComponent(Graphics g) {
                CustomLook.setup(g);
                super.paintComponent(g);
            }
        };
        setViewportView(txtArea);
        txtArea.setTabSize(3);
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setFont(CustomLook.defaultFont);
        setBorder(BorderFactory.createEtchedBorder(Color.white, Color.gray));
        setText(text);
        if (isLabel) {
            setEditable(false);
            setTextBackground(new Color(204, 204, 204));
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
    }

    public void setToolTipText(String text) {
        txtArea.setToolTipText(text);
    }

    public void scrollToTop() {
        getViewport().scrollRectToVisible(new Rectangle(0, 0, 10, 10));
    }

    public void setText(String text) {
        txtArea.setText(text);
    }

    public String getText() {
        return txtArea.getText();
    }

    public void append(String text) {
        txtArea.append(text);
    }

    public void setEditable(boolean yesno) {
        txtArea.setEditable(yesno);
    }

    public void setLineWrap(boolean yesno) {
        txtArea.setLineWrap(yesno);
    }

    public void setTextBackground(Color c) {
        txtArea.setBackground(c);
    }

    public void setFont(Font f) {
        if (txtArea != null) txtArea.setFont(f);
    }

    public void addKeyListener(KeyListener kl) {
        txtArea.addKeyListener(kl);
    }

    public void removeKeyListener(KeyListener kl) {
        txtArea.removeKeyListener(kl);
    }

    public JTextArea getTextArea() {
        return txtArea;
    }

    public Document getDocument() {
        return txtArea.getDocument();
    }
}
