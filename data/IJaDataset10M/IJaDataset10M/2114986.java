package net.sf.jtreemap.swing;

import javax.swing.JToolTip;

/**
 * Interface used to build your own JToolTip for the jTreeMap.
 * 
 * @see net.sf.jtreemap.swing.JTreeMap#setToolTipBuilder(IToolTipBuilder)
 * @author Laurent DUTHEIL
 */
public interface IToolTipBuilder {

    /**
     * Return the instance of the JToolTip.<BR>
     * Override this method to build your own JToolTip
     * 
     * @return the instance of the JToolTip
     */
    JToolTip getToolTip();
}
