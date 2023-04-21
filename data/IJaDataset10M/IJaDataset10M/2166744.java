package net.aa3sd.SMT;

import java.awt.Cursor;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import net.aa3sd.SMT.ui.MainFrame;
import java.awt.Color;

/**
 * Splash screen for SMT application.  Provides a JFrame that can be displayed during startup
 * and can report progress of startup events. 
 * 
 * @author mole
 *
 */
public class SplashScreen {

    private JFrame frame;

    private Cursor cursor;

    private JLabel progress;

    private JLabel lblNewLabel;

    /**
	 * Create the SplashScreen.
	 * Invoke with: 
	 * <code>
	 *     SplashScreen splash = new SplashScreen();
	 *     splash.show();
	 *     // Do start up operations
	 *     splash.setProgress("starting....");
	 *     // When done with startup operations
	 *     splash.hide();
	 * 
	 */
    public SplashScreen() {
        initialize();
    }

    /**
	 * Initialize the contents of the frame.
	 */
    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon jumpIcon = new ImageIcon(MainFrame.class.getResource("/net/aa3sd/SMT/ui/resources/shield.png"));
        frame.setIconImage(jumpIcon.getImage());
        frame.setUndecorated(true);
        JLabel lblSarManagerStarting = new JLabel(SMT.NAME + " " + SMT.VERSION + " Starting.....");
        lblSarManagerStarting.setForeground(Color.WHITE);
        lblSarManagerStarting.setBackground(Color.BLACK);
        frame.getContentPane().add(lblSarManagerStarting, BorderLayout.NORTH);
        progress = new JLabel(" ");
        progress.setForeground(Color.WHITE);
        progress.setBackground(Color.BLACK);
        frame.getContentPane().add(progress, BorderLayout.SOUTH);
        lblNewLabel = new JLabel("");
        lblNewLabel.setIcon(new ImageIcon(SplashScreen.class.getResource("/net/aa3sd/SMT/splash.png")));
        frame.getContentPane().add(lblNewLabel, BorderLayout.CENTER);
        cursor = frame.getCursor();
    }

    /**
	 * Makes the splash screen visible and sets the cursor to a wait cursor.
	 * 
	 */
    public void show() {
        frame.setVisible(true);
        cursor = frame.getCursor();
        frame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    /**
	 * Hides the splash screen and sets the cursor to the value it was at at the time show() was called.
	 * @see SplashScreen.show()
	 */
    public void hide() {
        frame.setVisible(false);
        frame.setCursor(cursor);
    }

    /**
	 * Sets the status text on the splash screen to the supplied text value.  
	 * 
	 * @param text String to display on the splash screen to report progress during startup.
	 */
    public void setProgress(String text) {
        progress.setText(text);
    }
}
