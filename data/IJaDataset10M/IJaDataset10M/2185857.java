package com.peterhi;

import java.awt.Canvas;
import java.awt.Window;
import java.awt.Graphics;
import java.awt.EventQueue;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class CanvasJNI extends Canvas {

    static {
        System.loadLibrary("canvasjni");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                JFrame frame = new JFrame();
                frame.setPreferredSize(new Dimension(640, 480));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                final CanvasJNI cjni = new CanvasJNI();
                frame.add(cjni, BorderLayout.CENTER);
                frame.addWindowListener(new WindowAdapter() {

                    @Override
                    public void windowOpened(WindowEvent e) {
                        int peer = handleOf(cjni);
                        System.out.println("peer is: " + peer);
                    }
                });
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    public static native int handleOf(Window window);

    public static native int handleOf(Canvas canvas);
}
