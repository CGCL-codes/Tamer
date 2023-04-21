package org.dengues.designer.ui.properties.generators;

import org.dengues.designer.ui.properties.PropertyTabDynamicSection;
import org.dengues.designer.ui.properties.controllers.AbstractPropertyController;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * @since 2007-9-1 上午09:37:42
 */
public abstract class AbstractControllerGenerator {

    protected PropertyTabDynamicSection dps;

    public abstract AbstractPropertyController generate();

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "setDynamicPropertySection".
     * 
     * @param dtp
     */
    public void setDynamicPropertySection(PropertyTabDynamicSection dtp) {
        this.dps = dtp;
    }
}
