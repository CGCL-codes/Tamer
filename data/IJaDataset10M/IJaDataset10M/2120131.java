package com.mockturtlesolutions.snifflib.flatfiletools.workbench;

import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorageXML;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryConnectivity;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryConnectivity;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Frame;
import java.awt.Dimension;
import com.mockturtlesolutions.snifflib.graphics.DefaultExceptionHandler;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.swing.UIManager;

public class FlatFileSetTool {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            throw new RuntimeException("Problem setting the default look and feel." + e);
        }
        ExceptionGroup exceptionThreadGroup = new ExceptionGroup();
        Thread th = new Thread(exceptionThreadGroup, "Init thread") {

            public void run() {
                createAndShowGUI();
            }
        };
        th.setDefaultUncaughtExceptionHandler(exceptionThreadGroup);
        th.start();
    }

    private static void createAndShowGUI() {
        FlatFileSetFrame F = new FlatFileSetFrame();
        F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        F.setVisible(true);
    }

    public static class ExceptionGroup extends ThreadGroup implements Thread.UncaughtExceptionHandler {

        public ExceptionGroup() {
            super("ExceptionGroup");
        }

        public void uncaughtException(Thread t, Throwable e) {
            showStackTrace(e);
            throw new RuntimeException(e);
        }

        public static String stack2string(Throwable e) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                return "------\r\n" + sw.toString() + "------\r\n";
            } catch (Exception e2) {
                return "Problem with converting stack to string.";
            }
        }

        public void showStackTrace(Throwable e) {
            String msg = this.stack2string(e);
            JOptionPane dialog = new JOptionPane();
            JTextArea area = new JTextArea(msg);
            JScrollPane jsp = new JScrollPane(area);
            jsp.setPreferredSize(new Dimension(500, 200));
            dialog.showMessageDialog(findActiveFrame(), jsp, "Error", JOptionPane.ERROR_MESSAGE);
        }

        public void showMessage(String msg) {
            JOptionPane dialog = new JOptionPane();
            JTextArea area = new JTextArea(msg);
            JScrollPane jsp = new JScrollPane(area);
            jsp.setPreferredSize(new Dimension(500, 200));
            dialog.showMessageDialog(findActiveFrame(), jsp, "Important!", JOptionPane.ERROR_MESSAGE);
        }

        private Frame findActiveFrame() {
            Frame[] frames = JFrame.getFrames();
            Frame frame = null;
            for (int i = 0; i < frames.length; i++) {
                Frame aframe = frames[i];
                if (aframe.isVisible()) {
                    frame = aframe;
                }
            }
            return (frame);
        }
    }
}
