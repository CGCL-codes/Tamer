package org.activebpel.rt.bpel.def;

/**
 * Bpel constructs that have a child called 'faultHandlers' should implement this interface.
 */
public interface IAeFaultHandlersParentDef {

    /**
    * Gets the 'faultHandlers' def.
    */
    public AeFaultHandlersDef getFaultHandlersDef();

    /**
    * Sets the 'faultHandlers' def.
    * 
    * @param aDef
    */
    public void setFaultHandlersDef(AeFaultHandlersDef aDef);
}
