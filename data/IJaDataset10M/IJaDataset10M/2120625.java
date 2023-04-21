package de.exilab.pixmgr.gui;

import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.border.SoftBevelBorder;
import de.exilab.pixmgr.gui.model.StatusBarModel;

/**
 * Statusbar of the application. Displays information about the currently
 * selected image.
 * @author <a href="andreas@exilab.de">Andreas Heidt</a>
 * @version $Revision: 1.2 $ - $Date: 2004/07/30 16:30:36 $
 */
public class StatusBar extends JLabel {

    /**
     * Datamodel of the statusbar
     */
    private StatusBarModel m_model;

    /**
     * Constructor of the class <code>StatusBar</code> 
     */
    public StatusBar() {
        super(" ");
        setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
        m_model = new StatusBarModel();
    }

    /**
     * Returns the data model of this component
     * @return A <code>StatusBarModel</code> object
     */
    public StatusBarModel getModel() {
        return m_model;
    }

    /**
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        setText(m_model.getData());
        super.paint(g);
    }

    /**
     * Sets a new data model for this component
     * @param model A new <code>StatusBarModel</code>
     */
    public void setModel(StatusBarModel model) {
        m_model = model;
        repaint();
    }
}
