package de.nava.informa.core;

/**
 * Meta-, or markerinterface, specifying objects, 
 * having <strong>Unread</strong>.
 * 
 * @author <a href="mailto:alexei@matiouchkine.de">Alexei Matiouchkine</a>
 * @version $Id: WithUnreadMIF.java 779 2005-09-27 22:17:06Z niko_schmuck $
*/
public interface WithUnreadMIF {

    /** 
	 * @return boolean indicating whether this item is currently unread.
	 */
    boolean getUnRead();

    /** 
   * @param val boolean to indicate whether this item is unread or not.
   */
    void setUnRead(boolean val);
}
