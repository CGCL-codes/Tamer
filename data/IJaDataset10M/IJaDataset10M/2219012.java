package com.worldware.ichabod.node;

/** String constants used in DataNode.
  */
public interface DataNodeKeys {

    /** key we use to get this object's name from the property file 
	  */
    static final String targetNameKey = "objectname";

    /** key we use to get this object's name from the property file 
	  */
    static final String targetClassKey = "objectclass";

    /** key for this objects description. this is for UI only
	  * Should probably replace targetNameFiendly in targethost?
	  */
    static final String targetDescKey = "objectdesc";

    /**
	 * Key we use to get this object's password. Not all objects have passwords,
	 * but all that do should use this key.
	 */
    static final String passwordKey = "data";

    /** Key for errors-to address
	  */
    static final String errorsToKey = "errorsto";
}
