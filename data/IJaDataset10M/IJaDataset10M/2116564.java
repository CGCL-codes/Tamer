package com.loribel.commons.business.abstraction;

import com.loribel.commons.exception.GB_BOException;

/**
 *  Abstraction of a value got by XPath.
 *
 * @author Gregory Borelli
 */
public interface GB_BOXpathValueSet extends GB_BOXpathValue {

    /**
     * Met � jour la valeur associ�e � la propri�t�.
     * If filter is null, use bo.setValue(a_value).
     * Otherwise, this method use addValue or putValue to set value of the list or 
     * map according to the filter.
     */
    public void setValue(Object a_value) throws GB_BOException;
}
