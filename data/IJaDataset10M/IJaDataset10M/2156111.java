package org.compiere.minigrid;

import javax.swing.*;
import java.util.*;

/**
 *  Read Only Cell Renderer
 *
 *  @author     Jorg Janke
 *  @version    $Id: ROCellEditor.java,v 1.2 2006/07/30 00:51:28 jjanke Exp $
 */
public final class ROCellEditor extends DefaultCellEditor {

    /**
	 *  Constructor
	 */
    public ROCellEditor() {
        super(new JTextField());
    }

    /**
	 *  Indicate RO
	 *  @param anEvent
	 *  @return false
	 */
    public boolean isCellEditable(EventObject anEvent) {
        return false;
    }
}
