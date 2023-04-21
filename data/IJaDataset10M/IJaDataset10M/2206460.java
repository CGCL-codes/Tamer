package fr.pingtimeout.jtail.gui.view;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: plaporte
 * Date: 8 avr. 2010
 * Time: 14:25:20
 * To change this template use File | Settings | File Templates.
 */
public class NoWrapJTextPane extends JTextPane {

    public boolean getScrollableTracksViewportWidth() {
        Component parent = getParent();
        ComponentUI ui = getUI();
        return parent == null || (ui.getPreferredSize(this).width <= parent.getSize().width);
    }
}
