package de.xirp.ui.dock;

/**
 * 
 */
interface IPartDropListener {

    /**
	 * @param event
	 */
    void dragOver(PartDropEvent event);

    /**
	 * @param event
	 */
    void drop(PartDropEvent event);
}
