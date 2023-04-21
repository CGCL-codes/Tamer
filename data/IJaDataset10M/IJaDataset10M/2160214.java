package br.edu.ufcg.ccc.javalog.view;

import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/** 
 * Abstract class for View classes.
 * @author Allyson Lima, Diego Pedro, Victor Freire
 * @version 22/11/09 
 */
@SuppressWarnings("serial")
public abstract class AbstractView extends JFrame {

    private static final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();

    protected void thisWindowClosed(WindowEvent evt) {
        WindowManager.getInstance().closeLastWindow();
    }

    /**
	 * Automatically sets the default Window event listeners.
	 */
    public AbstractView() {
        this.setMaximizedBounds(env.getMaximumWindowBounds());
        this.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent evt) {
                thisWindowClosed(evt);
            }
        });
    }

    protected void maximize() {
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }
}
