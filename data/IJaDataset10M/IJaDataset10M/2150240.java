package com.sodad.weka.gui.graphvisualizer;

/**
 * This interface should be implemented by any class
 * which needs to receive LayoutCompleteEvents from
 * the LayoutEngine. Typically this would be implemented
 * by the Visualization class.
 *
 * @author Ashraf M. Kibriya (amk14@cs.waikato.ac.nz)
 * @version $Revision: 1.5 $ - 24 Apr 2003 - Initial version (Ashraf M. Kibriya)
 */
public interface LayoutCompleteEventListener {

    void layoutCompleted(LayoutCompleteEvent le);
}
