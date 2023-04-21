package com.cube42.util.datamodel;

/**
 * Listener to be notified when data has changed
 * 
 * @author	Matt Paulin
 */
public interface DataModelChangeListener {

    /**
	 * Data has changed
	 */
    public void dataChanged(DataModelChangeEvent e);
}
