package org.mcisb.ui.util.list;

import javax.swing.*;

/**
 * 
 * @author Neil Swainston
 */
public interface MutableListModel extends ListModel {

    /**
	 * 
	 * @param index
	 * @return boolean
	 */
    public boolean isCellEditable(int index);

    /**
	 * 
	 * @param value
	 * @param index
	 */
    public void setValueAt(Object value, int index);
}
