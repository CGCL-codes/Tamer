package com.emental.mindraider;

import javax.swing.JApplet;
import com.emental.mindraider.kernel.MindRaider;
import com.emental.mindraider.ui.MindRaiderJFrame;

/**  * Applet class. * * @author Martin.Dvorak */
public class MindRaiderApplet extends JApplet {

    /**
     * serial vesion UID
     */
    private static final long serialVersionUID = 2659916896253990291L;

    public String getAppletInfo() {
        return MindRaider.getTitle();
    }

    public void init() {
        System.out.println("Got rdfLocation=" + this.getParameter("rdfLocation"));
        System.out.println("Got browser=" + this.getParameter("browser"));
        System.out.println("Got type=" + this.getParameter("type"));
        System.out.println("Got scriptable=" + this.getParameter("scriptable"));
        MindRaiderJFrame.getInstance(true);
    }
}
