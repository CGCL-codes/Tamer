package org.rosuda.javaGD;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;
import org.rosuda.javaGD.primitives.GDObject;
import org.rosuda.javaGD.primitives.GDState;

/**
 * <code>GDContainer</code> is the minimal interface that has to be implemented by classes that are used as back-ends for
 * JavaGD. The interface feeds graphics objects to the instance which are then free to use them for any purpose such as
 * display or printing.
 */
public interface GDContainer extends Serializable {

    /**
	 * add a new plot object to the list
	 * 
	 * @param o
	 *            plot object
	 */
    public void add(GDObject o);

    /** reset the plot- remove all objects */
    public void reset();

    /**
	 * retrieve graphics state
	 * 
	 * @return current graphics state
	 */
    public GDState getGState();

    /**
	 * retrieve graphics if this container is backed by some {@link Graphics} object.
	 * 
	 * @return current graphics object or <code>null</code> if this container is not associated with any
	 */
    public Graphics getGraphics();

    /**
	 * this method is called to notify the contained that a locator request is pending; the container must either return
	 * <code>false</code> and ignore the <code>ls</code> parameter *or* return <code>true</code> and call
	 * @link{LocatorSync.triggerAction} method at some point in the future (which may well be after returning from this
	 * method)
	 * 
	 * @param ls
	 *            locator synchronization object
	 */
    public boolean prepareLocator(LocatorSync ls);

    /**
	 * synchronize display with the graphics objects
	 * 
	 * @param finish
	 *            flag denoting whether the synchronization is desired or not (<code>true</code> for a finished batch,
	 *            <code>false</code> when a batch starts)
	 */
    public void syncDisplay(boolean finish);

    /**
	 * set the device number of this container
	 * 
	 * @param dn
	 *            device number
	 */
    public void setDeviceNumber(int dn);

    /** close the display associated with this container */
    public void closeDisplay();

    /**
	 * retrieve the current device number
	 * 
	 * @return current device number
	 */
    public int getDeviceNumber();

    /**
	 * retrieve the size of the container
	 * 
	 * @return size of the container
	 */
    public Dimension getSize();
}
