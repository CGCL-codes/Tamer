package com.zazu.mycl.application.terminal;

import java.awt.BorderLayout;
import java.io.InputStream;
import java.util.logging.Level;
import com.zazu.mycl.application.ApplicationController;
import com.zazu.mycl.application.DefaultCommandRunner;
import javax.swing.JFrame;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathFactory;

public class MyClWindow extends JFrame {

    public MyClWindow() {
        this.windowID = nextID++;
    }

    public void init() {
        statusBar.setSize(defaultWidth, 30);
        input.setSize(defaultWidth, defaultHeight);
        setTitle("MyCL Terminal " + windowID);
        setSize(defaultWidth, defaultHeight);
        add(input, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public int getwindowID() {
        return windowID;
    }

    private static final long serialVersionUID = 1L;

    private static int defaultWidth = 500;

    private static int defaultHeight = 500;

    private MyClStatusBar statusBar = new MyClStatusBar();

    private MyClInput input = new MyClInput(this);

    private final int windowID;

    private static int nextID = 1;
}
