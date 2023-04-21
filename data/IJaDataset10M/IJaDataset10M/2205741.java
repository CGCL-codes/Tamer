package com.liferay.portlet.stocks;

import com.liferay.portlet.stocks.model.Stocks;
import com.liferay.portlet.stocks.util.StocksUtil;
import java.util.ArrayList;
import java.util.List;
import javax.portlet.PortletPreferences;
import javax.portlet.PreferencesValidator;
import javax.portlet.ValidatorException;

/**
 * <a href="StocksPreferencesValidator.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class StocksPreferencesValidator implements PreferencesValidator {

    public void validate(PortletPreferences prefs) throws ValidatorException {
        List badSymbols = new ArrayList();
        String[] symbols = prefs.getValues("symbols", new String[0]);
        for (int i = 0; i < symbols.length; i++) {
            Stocks stocks = StocksUtil.getStocks(symbols[i]);
            if (stocks == null) {
                badSymbols.add(symbols[i]);
            }
        }
        if (badSymbols.size() > 0) {
            throw new ValidatorException("Failed to retrieve symbols", badSymbols);
        }
    }
}
