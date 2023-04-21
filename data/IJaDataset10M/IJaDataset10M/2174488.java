package org.moltools.lib;

public interface Reidentifiable extends Identifiable {

    /**
   * Set the identifier for this object. 
   * In general, this identifier should be unique within some context.
   */
    public void setID(String newID);
}
