package com.cosmos.acacia.crm.assembling;

import java.io.Serializable;

/**
 *
 * @author Miro
 */
public class EmptySourceValuesException extends AlgorithmException implements Serializable {

    public EmptySourceValuesException() {
        super("Empty source values list.");
    }
}
