package org.dmikis.jmmengine.models;

import javax.swing.JPanel;

/**
 * @author dmikis
 * @version 0.1
 * 
 * Abstract model's computation process result. 
 */
public interface Result {

    /**
     * Get panel contains result's data. It may be image, plot, any widget,
     * for example table of numerical values. Also, <code>Result<code> object
     * update panel if it is necessary.
     *  
     * @return link to <code>JPanel</code> object.
     */
    public JPanel getVisualizationPanel();
}
