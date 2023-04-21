package com.genia.toolbox.projects.toolbox_basics_project.web.gwt.form.edition.impl;

import org.springframework.transaction.annotation.Transactional;
import com.genia.toolbox.basics.exception.BundledException;
import com.genia.toolbox.basics.exception.technical.TechnicalException;
import com.genia.toolbox.projects.toolbox_basics_project.bean.model.Country;
import com.genia.toolbox.projects.toolbox_basics_project.bean.model.I18nDynamicString;
import com.genia.toolbox.projects.toolbox_basics_project.bean.model.impl.CountryImpl;
import com.genia.toolbox.projects.toolbox_basics_project.business.dao.CountryBusinessDao;
import com.genia.toolbox.projects.toolbox_basics_project.spring.manager.CommonFormManager;
import com.genia.toolbox.projects.toolbox_basics_project.web.gwt.form.constant.CountryFormKey;
import com.genia.toolbox.projects.toolbox_basics_project.web.gwt.form.edition.CountryEditionFormProvider;
import com.genia.toolbox.web.gwt.form.client.value.DataIdentifier;
import com.genia.toolbox.web.gwt.form.client.value.FormValues;
import com.genia.toolbox.web.gwt.form.client.value.impl.DataIdentifierImpl;
import com.genia.toolbox.web.gwt.form.client.value.impl.FormSimpleValueImpl;
import com.genia.toolbox.web.gwt.form.client.value.impl.FormValuesImpl;
import com.genia.toolbox.web.gwt.form.server.provider.impl.AbstractEditionForm;

/**
 * CountryEditionForm.
 */
public class AbstractCountryEditionFormProvider extends AbstractEditionForm implements CountryEditionFormProvider {

    /**
   * CountryBusinessDao.
   */
    private CountryBusinessDao countryBusinessDao;

    /**
   * {@link CommonFormManager}.
   */
    private CommonFormManager commonFormManager;

    /**
   * retrieve the initial value of the object that is edited.
   * 
   * @param country
   *          country used
   * @return a FormValues filled
   * @throws TechnicalException
   *           if an error occured
   */
    public FormValues getInitialValue(Country country) throws TechnicalException {
        FormValuesImpl fv = new FormValuesImpl();
        fv.setFormIdentifier(getFormIdentifier());
        fv.setDataIdentifier(new DataIdentifierImpl(country.getIdentifier()));
        FormSimpleValueImpl fsvi = new FormSimpleValueImpl();
        fsvi.setName(CountryFormKey.NAME);
        fsvi.setValues(getCommonFormManager().createStringList(country.getName()));
        fv.addFormSimpleValue(fsvi);
        fsvi = new FormSimpleValueImpl();
        fsvi.setName(CountryFormKey.NATIONALITY);
        fsvi.setValues(getCommonFormManager().createStringList(country.getNationality()));
        fv.addFormSimpleValue(fsvi);
        return fv;
    }

    /**
   * retrieve the initial value of the object that is edited.
   * 
   * @param dataIdentifier
   *          identifier to use to get initialValues
   * @return FormValues filled
   * @throws BundledException
   *           when an error occurs
   */
    public FormValues getInitialValue(DataIdentifier dataIdentifier) throws BundledException {
        if (dataIdentifier.getIdentifier() == null) {
            return getDefaultValues();
        } else {
            return getInitialValue(getCountryBusinessDao().getCountry(Long.valueOf(dataIdentifier.getIdentifier())));
        }
    }

    /**
   * save the form on the server.
   * 
   * @param newValues
   *          the new values entered by the user
   * @return the new values that have been saved
   * @throws BundledException
   *           when an error occurred
   */
    @Transactional(rollbackFor = { BundledException.class })
    @SuppressWarnings("unchecked")
    public FormValues saveForm(FormValues newValues) throws BundledException {
        DataIdentifier dataIdentifier = newValues.getDataIdentifier();
        I18nDynamicString name = getCommonFormManager().createI18nDynamicString(newValues.getFormSimpleValue(CountryFormKey.NAME).getValues());
        I18nDynamicString nationality = getCommonFormManager().createI18nDynamicString(newValues.getFormSimpleValue(CountryFormKey.NATIONALITY).getValues());
        Country country;
        if (dataIdentifier.getIdentifier() == null) {
            country = new CountryImpl();
        } else {
            country = getCountryBusinessDao().getCountry(Long.valueOf(dataIdentifier.getIdentifier()));
        }
        country.setName(name);
        country.setNationality(nationality);
        if (dataIdentifier.getIdentifier() == null) {
            getCountryBusinessDao().saveCountry(country);
            newValues.getDataIdentifier().setIdentifier(String.valueOf(country.getIdentifier()));
        } else {
            getCountryBusinessDao().updateCountry(country);
        }
        return newValues;
    }

    /**
   * getter for the countryBusinessDao property.
   * 
   * @return countryBusinessDao
   */
    public CountryBusinessDao getCountryBusinessDao() {
        return countryBusinessDao;
    }

    /**
   * setter for the countryBusinessDao property.
   * 
   * @param countryBusinessDao
   *          the countryBusinessDao to set
   */
    public void setCountryBusinessDao(CountryBusinessDao countryBusinessDao) {
        this.countryBusinessDao = countryBusinessDao;
    }

    /**
   * getter for the commonFormManager property.
   * 
   * @return the commonFormManager
   */
    public CommonFormManager getCommonFormManager() {
        return commonFormManager;
    }

    /**
   * setter for the commonFormManager property.
   * 
   * @param commonFormManager
   *          the commonFormManager to set
   */
    public void setCommonFormManager(CommonFormManager commonFormManager) {
        this.commonFormManager = commonFormManager;
    }
}
