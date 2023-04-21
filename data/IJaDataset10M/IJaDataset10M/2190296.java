package com.liferay.portlet.currencyconverter;

import com.liferay.portlet.currencyconverter.model.Currency;
import com.liferay.portlet.currencyconverter.util.CurrencyUtil;
import java.util.ArrayList;
import java.util.List;
import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;
import javax.portlet.ValidatorException;

/**
 * <a href="CurrencyPreferencesValidator.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CurrencyPreferencesValidator implements PreferencesValidator {

    public void validate(PortletPreferences prefs) throws ValidatorException {
        List badSymbols = new ArrayList();
        String[] symbols = prefs.getValues("symbols", new String[0]);
        for (int i = 0; i < symbols.length; i++) {
            Currency currency = CurrencyUtil.getCurrency(symbols[i]);
            if (currency == null) {
                badSymbols.add(symbols[i]);
            }
        }
        if (badSymbols.size() > 0) {
            throw new ValidatorException("Failed to retrieve symbols", badSymbols);
        }
    }
}
