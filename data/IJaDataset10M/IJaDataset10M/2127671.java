package com.loribel.commons.abstraction;

import com.loribel.commons.exception.GB_SaveException;

/**
 * Interface for Objects witch can execute save operation.
 *
 * @author Gregory Borelli
 */
public interface GB_SavableO {

    /**
     * M�thode save.
     */
    void save(int a_option, boolean a_overwrite) throws GB_SaveException;
}
