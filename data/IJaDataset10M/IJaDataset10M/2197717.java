package com.codemonster.nato;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 */
public class WebWindow extends JFrame {

    protected JEditorPane renderPane = new JEditorPane();

    private JScrollPane scroller = null;

    public WebWindow(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        init();
    }

    private void init() {
        renderPane.setEditable(false);
        renderPane.setContentType("text/html");
        scroller = new JScrollPane(renderPane);
        this.getContentPane().add(scroller);
        renderPane.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
            }
        });
    }

    public void setText(String text) {
        renderPane.setText(text);
    }
}
