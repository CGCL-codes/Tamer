package com.atech.mobile.utils;

import com.atech.mobile.i18n.I18nControlAbstract;

/**
 *  This is abstract class for controling I18N. You need to extend this class, and set all variables. With setting 
 *  of variables half of work is done. Next half is way to create this class. You need to make constructor. Sample
 *  constructor for Singelton is in this source file.
 */
public class ATMobileI18nControl extends I18nControlAbstract {

    private static ATMobileI18nControl m_i18n = null;

    /**
     *
     *  This is I18nControl constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method. 
     *  This constructor should be implemented by implementing class<br><br>
     *
     */
    private ATMobileI18nControl() {
        init();
        getSelectedLanguage();
        setLanguage();
    }

    public void init() {
        def_language = "en";
        lang_file_root = "AtechTools";
    }

    /**
     *
     *  This method returns reference to OmniI18nControl object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *  This method should be implemented by implementing class, if we want to use singelton<br><br>
     *
     *  @return Reference to OmniI18nControl object
     * 
     */
    public static ATMobileI18nControl getInstance() {
        if (m_i18n == null) m_i18n = new ATMobileI18nControl();
        return m_i18n;
    }

    /**
     *
     *  This method sets handle to OmniI18NControl to null and deletes the instance. <br><br>
     *
     */
    public void deleteInstance() {
        m_i18n = null;
    }

    private void getSelectedLanguage() {
        this.selected_language = this.def_language;
    }

    /**
     * This method sets the language according to the preferences.<br>
     */
    public void setLanguage() {
        if (selected_language != null) setLanguage(selected_language); else setLanguage(def_language);
    }
}
