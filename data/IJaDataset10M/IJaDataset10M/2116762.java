package net.infonode.gui.button;

import java.io.*;
import javax.swing.*;

/**
 * Creates a flat button with mouse over highlighting.
 *
 * @author $Author: jesper $
 * @version $Revision: 1.5 $
 */
public class FlatButtonFactory implements ButtonFactory, Serializable {

    private static final long serialVersionUID = 1;

    public AbstractButton createButton(Object object) {
        return net.infonode.gui.ButtonFactory.createFlatHighlightButton(null, null, 0, null);
    }
}
