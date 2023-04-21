package jahuwaldt.swing;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;

/**
*  A window that is used to display a splash screen
*  at application startup.
*
*  <p>  Modified by:  Joseph A. Huwaldt  </p>
*
*  @author  Joseph A. Huwaldt   Date:  May 2, 2001
*  @version May 17, 2002
**/
public class SplashScreen extends JWindow {

    /**
	*  One second in milliseconds.
	**/
    public static final int ONE_SECOND = 1000;

    /**
	*  Construct a splash screen and display it for the specified number of milliseconds.
	*
	*  @param  filename  The path name of an image file to display as the splash screen.
	*  @param  f         The parent frame for this window.
	*  @param  waitTime  The amount of time to keep the dialog visible before automatically
	*                    closing it in milliseconds.
	**/
    public SplashScreen(String filename, Frame f, int waitTime) {
        super(f);
        setupSplashScreen(new ImageIcon(filename), waitTime);
    }

    /**
	*  Construct a splash screen and display it for the specified number of milliseconds.
	*
	*  @param  imageURL  The URL to an image file to display as the splash screen.
	*  @param  f         The parent frame for this window.
	*  @param  waitTime  The amount of time to keep the dialog visible before automatically
	*                    closing it in milliseconds.
	**/
    public SplashScreen(URL imageURL, Frame f, int waitTime) {
        super(f);
        setupSplashScreen(new ImageIcon(imageURL), waitTime);
    }

    /**
	*  Construct a splash screen and display it for the specified number of milliseconds.
	*
	*  @param  icon      The image icon to display as the splash screen.
	*  @param  f         The parent frame for this window.
	*  @param  waitTime  The amount of time to keep the dialog visible before automatically
	*                    closing it in milliseconds.
	**/
    public SplashScreen(ImageIcon icon, Frame f, int waitTime) {
        super(f);
        setupSplashScreen(icon, waitTime);
    }

    /**
	*  Actually does the setup and layout required for this splash screen.
	*
	*  @param  icon      The image icon to display as the splash screen.
	*  @param  waitTime  The amount of time to keep the dialog visible before automatically
	*                    closing it in milliseconds.
	**/
    protected final void setupSplashScreen(ImageIcon icon, int waitTime) {
        JLabel l = new JLabel(icon);
        getContentPane().add(l, BorderLayout.CENTER);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation(screenSize.width / 2 - (labelSize.width / 2), screenSize.height / 2 - (labelSize.height / 2));
        addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                setVisible(false);
                dispose();
            }
        });
        final int pause = waitTime;
        final Runnable closerRunner = new Runnable() {

            public void run() {
                setVisible(false);
                dispose();
            }
        };
        Runnable waitRunner = new Runnable() {

            public void run() {
                try {
                    Thread.sleep(pause);
                    SwingUtilities.invokeAndWait(closerRunner);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        setVisible(true);
        Thread splashThread = new Thread(waitRunner, "SplashThread");
        splashThread.start();
    }
}
