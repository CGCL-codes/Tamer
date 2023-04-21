package Watermill.interfaces;

import java.io.*;

/**
 * An abstraction of objects identifying values
 *  of the dataset
 * @author Julien Lafaye
 */
public interface Identifier extends Serializable {

    /**
 * Retrieves the maximum distortion allowed for 
 * the value identified by the identifier
 * @return the maximum allowed distortion
 */
    public int getMaxDistortion();

    /**
  * Set the maximum allowed distortion
  * for the value identifier by the identifier
  * @param lc target maximum distortion
  */
    public void setMaxDistortion(int lc);
}
