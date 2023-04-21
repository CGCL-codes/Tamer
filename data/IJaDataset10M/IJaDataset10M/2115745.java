package de.kout.wlFxp.view;

import javax.swing.*;
import java.io.*;
import javax.swing.event.*;
import de.kout.wlFxp.ftp.Transfer;

/**
 *  the special implementation of a list model for file lists
 *
 *@author     Alexander Kout
 *@created    03. April 2002
 */
public class QueueListModel implements ListModel {

    Transfer[] transfers;

    /**
	 *  Constructor for the MainListModel object
	 *
	 *@param  transfers  Description of the Parameter
	 */
    public QueueListModel(Transfer transfers[]) {
        this.transfers = transfers;
    }

    /**
	 *  Gets the size attribute of the MainListModel object
	 *
	 *@return    The size value
	 */
    public int getSize() {
        if (transfers != null) {
            return transfers.length;
        } else {
            return 0;
        }
    }

    /**
	 *  Gets the elementAt attribute of the MainListModel object
	 *
	 *@param  index  Description of Parameter
	 *@return        The elementAt value
	 */
    public Object getElementAt(int index) {
        if (transfers.length > index && index > -1) {
            return transfers[index];
        } else {
            return new Transfer();
        }
    }

    /**
	 *  Adds a feature to the ListDataListener attribute of the MainListModel
	 *  object
	 *
	 *@param  l  The feature to be added to the ListDataListener attribute
	 */
    public void addListDataListener(ListDataListener l) {
    }

    /**
	 *  Description of the Method
	 *
	 *@param  l  Description of Parameter
	 */
    public void removeListDataListener(ListDataListener l) {
    }
}
