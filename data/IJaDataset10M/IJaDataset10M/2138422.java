package jhomenet.ui.window.responsive;

import javax.swing.*;
import jhomenet.ui.window.AbstractInternalFrame;
import jhomenet.ui.window.JStatusBar;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Progress window.
 * <br>
 * Id: $Id: ProgressWindow.java 1710 2006-04-13 21:42:19Z dhirwinjr $
 * 
 * @author dhirwinjr
 */
public class ProgressWindow extends AbstractInternalFrame {

    /***
     * Serial version ID information - used for the serialization process.
     */
    private static final long serialVersionUID = 00001;

    /**
     * Instance of the progress window.
     */
    private static ProgressWindow instance;

    private JLabel displayText_l;

    /**
     * Default constructor.
     */
    private ProgressWindow() {
        super();
    }

    /**
     * Singleton instance method.
     *
     * @return A reference to the singleton progress window
     */
    public static ProgressWindow instance() {
        if (instance == null) {
            instance = new ProgressWindow();
        }
        return instance;
    }

    /** 
     * @see jhomenet.ui.window.AbstractInternalFrame#getWindowId()
     */
    @Override
    public String getWindowId() {
        return this.getClass().getName();
    }

    public String getWindowTitle() {
        return "Progress window";
    }

    /**
     * Set the display text.
     *
     * @param text
     */
    public void setDisplayText(String text) {
        displayText_l.setText(text);
    }

    /**
     * Build the GUI.
     *  
     * @see jhomenet.ui.window.AbstractInternalFrame#buildInternalPanel()
     */
    @Override
    protected JPanel buildInternalPanel() {
        FormLayout panelLayout = new FormLayout("6dlu, center:65dlu, 6dlu", "5dlu, pref, 15dlu");
        CellConstraints cc = new CellConstraints();
        PanelBuilder builder = new PanelBuilder(panelLayout);
        displayText_l = new JLabel("Default message");
        builder.add(displayText_l, cc.xy(2, 2));
        return builder.getPanel();
    }

    /**
     * Build and add the status bar to the Plan editor window.
     * 
     * @see jhomenet.ui.window.AbstractInternalFrame#buildStatusBar()
     */
    @Override
    protected JStatusBar buildStatusBar() {
        JStatusBar statusBar = new JStatusBar(this);
        return statusBar;
    }

    /**
     * Reset the GUI.
     *  
     * @see jhomenet.ui.window.AbstractInternalFrame#resetInternalFrame()
     */
    @Override
    protected void resetInternalFrame() {
    }
}
