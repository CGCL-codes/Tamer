package org.ximtec.igesture.tool.explorer;

import java.awt.event.ActionEvent;
import org.sigtec.graphix.widget.BasicAction;

/**
 * This is a dummy action and is replaced in NodeInfoImpl with a separator.
 * 
 * @author Ueli Kurmann
 * 
 */
public class SeparatorAction extends BasicAction {

    @Override
    public void actionPerformed(ActionEvent arg0) {
        throw new RuntimeException("do not use this action.");
    }
}
