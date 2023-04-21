package com.quickrss.view.swing;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Panel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author yxli
 * 
 */
public class SWTBrowser extends Panel {

    DisplayThread displayThread;

    private Canvas canvas;

    private Browser browser;

    public static void main(String[] args) {
        JFrame frame = new JFrame("SWTBrowser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new SWTBrowser(), BorderLayout.CENTER);
        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public SWTBrowser() {
        System.setProperty("sun.awt.xembedserver", "true");
        displayThread = new DisplayThread();
        displayThread.start();
        canvas = new Canvas();
        setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);
    }

    public void addNotify() {
        super.addNotify();
        Display dis = displayThread.getDisplay();
        dis.asyncExec(new Runnable() {

            public void run() {
                Shell shell = SWT_AWT.new_Shell(displayThread.getDisplay(), canvas);
                shell.setLayout(new FillLayout());
                browser = new Browser(shell, SWT.NONE);
                browser.setLayoutData(BorderLayout.CENTER);
                browser.setText("");
            }
        });
    }

    public void back() {
        if (browser != null) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    browser.back();
                }
            });
        }
    }

    public void setUrl(final String url) {
        if (browser != null) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    browser.setUrl(url);
                }
            });
        }
    }

    public void setText(final String html) {
        if (browser != null) {
            Display.getDefault().asyncExec(new Runnable() {

                @Override
                public void run() {
                    browser.setText(html);
                }
            });
        }
    }

    private class DisplayThread extends Thread {

        private Display display;

        Object sem = new Object();

        public void run() {
            synchronized (sem) {
                display = Display.getDefault();
                sem.notifyAll();
            }
            swtEventLoop();
        }

        private void swtEventLoop() {
            while (true) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        }

        public Display getDisplay() {
            try {
                synchronized (sem) {
                    while (display == null) sem.wait();
                    return display;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }
}
