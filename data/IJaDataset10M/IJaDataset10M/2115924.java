package com.loribel.commons.gui.abstraction;

import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;

/**
 * Table builder to build component to represent a list of business objects.
 *   
 * @author Gregory Borelli
 */
public interface GB_BOTableBuilder extends GB_TableBuilder {

    GB_SimpleBusinessObject[] getSelectedBos(Object[] a_return);

    void setBoSearch(GB_SimpleBusinessObject a_boSearch);

    GB_SimpleBusinessObject setUseBoSearch(boolean a_useBoSearch);
}
