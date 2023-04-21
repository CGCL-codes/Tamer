package de.kuulware.afw;

import javax.swing.JFrame;

/**
 * This is the base Frame class within the Application Framework. This frame
 * accepts a View as the content pane for the frame, retrieves the menu bar
 * from the view class and installs it as the menu bar for the frame.
 *
 * @version $Id: Frame.java,v 1.2 2000/11/19 18:32:26 ridesmet Exp $
 * @author $Author: ridesmet $
 */
public class Frame extends JFrame {

    /**
	 * The default constructor is valid while the AFW refactorings are taking place.
	 * In a near future, Frames will only be created through the GUIBuilder.
	 *
	 * @deprecated
	 */
    public Frame() {
    }

    public Frame(View viewToPutInFrame) {
        this.setView(viewToPutInFrame);
    }

    public View getView() {
        return (View) this.getContentPane();
    }

    public void setView(View viewToPutInFrame) {
        this.setContentPane(viewToPutInFrame);
        this.setJMenuBar(viewToPutInFrame.getMenuBar());
    }
}
