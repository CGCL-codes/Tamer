package com.loribel.commons.abstraction.preference;

import com.loribel.commons.abstraction.GB_IdOwnerSet;

/**
 * Abstraction of a preference.
 * Two types of preferences:
 * <ul>
 *   <li>GB_PreferenceValue for single value
 *   <li>GB_PreferenceValues for multi values
 * </ul>
 */
public interface GB_Preference extends GB_IdOwnerSet {

    /**
     * Action after load this preference.
     */
    void afterLoad();

    /**
     * Action before save this preference.
     * Example : limit the number of values, ...
     */
    void beforeSave();

    /**
     * Reteurn the value of the preference.
     * For multi values, the return is a Collection.
     * @return Object
     */
    Object getValue();
}
