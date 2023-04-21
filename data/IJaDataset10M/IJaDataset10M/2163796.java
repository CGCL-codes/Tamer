package jiv;

/**
 * An adapter (convenience) class for receiving
 * <code>PositionEvent</code>-s.  Listener classes that don't produce
 * slice data don't have to implement the
 * <code>getMaxSliceNumber</code> and <code>getOrthoStep</code> methods.
 *
 * @author Chris Cocosco (crisco@bic.mni.mcgill.ca)
 * @version $Id: PositionListenerAdapter.java,v 1.2 2002/04/24 14:31:56 cc Exp $ 
 */
public abstract class PositionListenerAdapter implements PositionListener {

    public abstract void positionChanged(PositionEvent e);

    /** the implementing class has to return some meaningful (ie >0) value 
	only if it actually produces slice data... */
    public int getMaxSliceNumber() {
        return -1;
    }

    public float getOrthoStep() {
        return Float.NaN;
    }
}
