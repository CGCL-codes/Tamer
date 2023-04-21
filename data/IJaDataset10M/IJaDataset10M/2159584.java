package com.loribel.commons.gui.factory;

import com.loribel.commons.abstraction.swing.GB_ViewManager;

/**
 * Factory to produce {@link GB_ViewManager} associated to any Object.
 * Use the type of object to create the best ViewManager.
 *
 * @author Gregory Borelli
 */
public class GB_TypeVMFactoryDefault extends GB_TypeVMFactoryAbstract {

    public GB_TypeVMFactoryDefault() {
        super();
    }

    public boolean acceptModel(Object a_model) {
        return true;
    }
}
