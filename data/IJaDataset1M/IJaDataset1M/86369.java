package com.loribel.tools.template.vm;

import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.gui.GB_VMFactoryByType;
import com.loribel.tools.template.GB_TJavaBOConfigurable;
import com.loribel.tools.template.GB_TJavaMVC;

/**
 * Factory of ViewManager to represent Template configuration.
 */
public class GB_TemplateVMFactory extends GB_VMFactoryByType {

    private static GB_TemplateVMFactory instance;

    private GB_TemplateVMFactory() {
        init();
    }

    private void init() {
        this.addToFactory(GB_TJavaMVC.class, GB_TJavaMVCVM.newPrototype());
        this.addToFactory(GB_TJavaBOConfigurable.class, GB_TJavaVM.newPrototype());
    }

    public GB_ViewManager newViewManagerDefault(Object a_model) {
        return null;
    }

    public static GB_TemplateVMFactory getInstance() {
        if (instance == null) {
            instance = new GB_TemplateVMFactory();
        }
        return instance;
    }
}
