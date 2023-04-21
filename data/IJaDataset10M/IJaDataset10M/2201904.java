package com.googlecode.icefusion.ui.commons.navigation;

import org.springframework.beans.factory.annotation.Autowired;
import com.googlecode.icefusion.ui.commons.BackingBeanForm;
import com.googlecode.icefusion.ui.commons.constant.Context;

/**
 * Menu icon management.
 * 
 * @author Rainer Eschen
 */
public class MenuIcons extends BackingBeanForm {

    @Autowired
    private Context context;

    /**
     * Activate en locale.
     * 
     * @return navigation id
     */
    public String switchToEn() {
        this.context.setLocale("en");
        return null;
    }

    /**
     * Activate de locale.
     * 
     * @return navigation id
     */
    public String switchToDe() {
        this.context.setLocale("de");
        return null;
    }

    /**
     * Deliver current skin.
     * 
     * @return skin
     */
    public String getSkin() {
        return this.context.getSkin();
    }
}
