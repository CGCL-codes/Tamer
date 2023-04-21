package com.jquery.dynamicdrive;

/**
 * 
 *
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public interface IMenu {

    /**
	 * If the toolbar is a top level one.
	 * 
	 * @return
	 */
    boolean isOnTop();

    public IMenu addItem(IMenuItem item);

    public IMenu removeItem(IMenuItem item);
}
