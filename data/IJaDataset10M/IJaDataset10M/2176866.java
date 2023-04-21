package com.serena.xmlbridge.adapter.qc9.gen;

import com4j.*;

/**
 * Services for managing parameters for manual runs of Business Process Component tests.
 */
@IID("{6BA5E493-366C-4068-B572-93D27D405DFE}")
public interface IBPStepParam extends com.serena.xmlbridge.adapter.qc9.gen.IBaseField {

    /**
     * The name of this parameter.
     */
    @VTID(20)
    java.lang.String name();

    /**
     * The name of this parameter.
     */
    @VTID(21)
    void name(java.lang.String pVal);

    /**
     * The value of this parameter.
     */
    @VTID(22)
    java.lang.String value();

    /**
     * The value of this parameter.
     */
    @VTID(23)
    void value(java.lang.String pVal);

    /**
     * The parameter type.
     */
    @VTID(24)
    int type();

    /**
     * The parameter type.
     */
    @VTID(25)
    void type(int pVal);

    /**
     * The parameter ID of the referenced parameter
     */
    @VTID(26)
    int referencedParamID();

    /**
     * The parameter ID of the referenced parameter
     */
    @VTID(27)
    void referencedParamID(int pVal);

    /**
     * The ID of the step in which this parameter occurs.
     */
    @VTID(28)
    int stepID();
}
