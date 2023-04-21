package org.activebpel.rt.bpel.def.faults;

import javax.xml.namespace.QName;

public interface IAeCatch {

    /**
    * Returns true if the catch defines a variable
    */
    public boolean hasFaultVariable();

    /**
    * Returns the QName of the fault element or null if no variable is defined or if its catching a message
    */
    public QName getFaultElementName();

    /**
    * Returns the QName of the fault message or null if no variable is defined or if its catching an element
    */
    public QName getFaultMessageType();

    /**
    * Returns the QName of the fault being caught or null if not provided
    */
    public QName getFaultName();
}
