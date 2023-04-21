package de.dlr.DAVInspector;

/**
 * TODO: wues_ha: Enter comment!
 *
 * @version $LastChangedRevision$
 * @author Jochen Wuest
 */
public interface EditPlugin extends Plugin {

    /**
     * Returns the processed data of the plugin.
     * 
     * @return String
     */
    String getOutput();
}
