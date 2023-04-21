package com.mockturtlesolutions.snifflib.guitools.components;

import java.io.*;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.event.*;
import java.awt.Image;
import java.io.*;
import java.awt.Frame;
import java.awt.Container;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class DefaultExceptionHandler {

    public DefaultExceptionHandler() {
    }

    /**
	This can be over-ridden to process exceptions in some specific way.
	*/
    public void processException(Exception err) {
        err.printStackTrace();
    }
}
