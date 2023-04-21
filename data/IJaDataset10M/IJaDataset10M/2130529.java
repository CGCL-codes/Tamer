package com.loribel.commons.business.abstraction;

import com.loribel.commons.abstraction.*;

/**
 *  Abstraction
 *
 * @author Gregory Borelli
 */
public interface GB_BOEnumValuesGroup extends GB_LabelsDescOwner, GB_NameOwner {

    String getPropertyId();

    String getValues(String a_groupValue);

    String[] getValuesStr(String a_groupValue);
}
