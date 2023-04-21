package br.com.mobilit.components.application;

import br.com.mobilit.components.i18n.I18nManager;
import br.com.mobilit.components.i18n.Ii18nMgt;
import br.com.mobilit.components.i18n.exception.I18nException;
import br.com.mobilit.components.properties.IPropertieMgt;
import br.com.mobilit.components.properties.PropertieManager;
import br.com.mobilit.components.properties.excepetion.PropertieException;
import br.com.mobilit.components.service.IServiceMgt;
import br.com.mobilit.components.service.ServiceManager;
import br.com.mobilit.components.service.entity.Parameter;
import br.com.mobilit.components.service.entity.ResponseListener;
import br.com.mobilit.components.service.excepiton.InvalidArgumentException;

public class MoSOAFrameworkManager implements IMoSOAFramework {

    private static final Ii18nMgt i18n = I18nManager.getInstance();

    private static final IPropertieMgt propertie = PropertieManager.getInstance();

    private IServiceMgt service;

    private static MoSOAFrameworkManager INSTANCE;

    private MoSOAFrameworkManager() {
    }

    public static IMoSOAFramework getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoSOAFrameworkManager();
        }
        return INSTANCE;
    }

    public void callService(Parameter parameter, ResponseListener responseListener) throws InvalidArgumentException {
        this.service = new ServiceManager();
        this.service.callService(parameter, responseListener);
    }

    public double getDoublePropertie(String key) {
        double ret = 0;
        try {
            ret = propertie.getDouble(key);
        } catch (PropertieException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int getIntPropertie(String key) {
        int ret = 0;
        try {
            ret = propertie.getInt(key);
        } catch (PropertieException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String getLanguage() {
        return i18n.getLanguage();
    }

    public long getLongPropertie(String key) {
        long ret = 0;
        try {
            ret = propertie.getLong(key);
        } catch (PropertieException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String getString(String key) {
        String ret = null;
        try {
            ret = i18n.getString(key);
        } catch (I18nException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String getStringPropertie(String key) {
        String ret = null;
        try {
            ret = propertie.getString(key);
        } catch (PropertieException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public void loudLanguageTerms(String fileName) throws I18nException {
        i18n.loudLanguageTerms(fileName);
    }

    public void loudProperties(String fileName) throws PropertieException {
        propertie.loudProperties(fileName);
    }

    public void setLanguage(String language) {
        i18n.setLanguage(language);
    }
}
