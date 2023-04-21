package net.sourceforge.ondex.parser.biopaxmodel;

/**
 * Implementations of this listener may be registered with instances of net.sourceforge.ondex.parser.biopaxmodel.utilityClass to 
 * receive notification when properties changed, added or removed.
 * <br>
 */
public interface utilityClassListener extends com.ibm.adtech.jastor.ThingListener {

    /**
	 * Called when a value of COMMENT has been added
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.utilityClass
	 * @param newValue the object representing the new value
	 */
    public void COMMENTAdded(net.sourceforge.ondex.parser.biopaxmodel.utilityClass source, java.lang.String newValue);

    /**
	 * Called when a value of COMMENT has been removed
	 * @param source the affected instance of net.sourceforge.ondex.parser.biopaxmodel.utilityClass
	 * @param oldValue the object representing the removed value
	 */
    public void COMMENTRemoved(net.sourceforge.ondex.parser.biopaxmodel.utilityClass source, java.lang.String oldValue);
}
