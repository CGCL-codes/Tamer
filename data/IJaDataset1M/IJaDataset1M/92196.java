package jmri.jmrix.grapevine.nodetable;

import java.awt.*;
import java.util.ResourceBundle;
import javax.swing.*;
import jmri.jmrix.grapevine.SerialTrafficController;

/**
 * Frame for user configuration of serial nodes
 * @author	Bob Jacobsen   Copyright (C) 2004, 2007
 * @author	Dave Duchamp   Copyright (C) 2004, 2006
 * @version	$Revision: 1.4 $
 */
public class NodeTableFrame extends jmri.util.JmriJFrame {

    ResourceBundle rb = ResourceBundle.getBundle("jmri.jmrix.grapevine.nodetable.NodeTableBundle");

    /**
     * Constructor method
     */
    public NodeTableFrame() {
        super();
    }

    NodeTablePane p;

    /** 
     *  Initialize the window
     */
    public void initComponents() {
        setTitle(rb.getString("WindowTitle"));
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        p = new NodeTablePane();
        p.initComponents();
        contentPane.add(p);
        addHelpMenu("package.jmri.jmrix.grapevine.nodetable.NodeTableFrame", true);
        SerialTrafficController.instance().addSerialListener(p);
        pack();
    }

    public void dispose() {
        SerialTrafficController.instance().removeSerialListener(p);
        super.dispose();
    }
}
