package org.activebpel.rt.bpel.def;

/**
 * BPEL constructs that parent the 'terminationHandler' construct should implement this interface.
 */
public interface IAeTerminationHandlerParentDef {

    /**
    * Gets the 'terminationHandler' def.
    */
    public AeTerminationHandlerDef getTerminationHandlerDef();

    /**
    * Sets the 'terminationHandler' def.
    * 
    * @param aDef
    */
    public void setTerminationHandlerDef(AeTerminationHandlerDef aDef);
}
