package net.sf.tacos.demo.pages.ajax;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * demonstrate the floating pane including the component and AjaxDirectLink parameter.
 *
 * @author Felix Sun
 */
public abstract class FloatingPaneExample extends BasePage {

    /**
     * logger
     */
    protected static final Log logger = LogFactory.getLog(FloatingPaneExample.class);

    /**
     * selected country for the parent TextField
     *
     * @return selected country.
     */
    public abstract String getSelectedCountry();

    /**
     * set selected country for the parent TextField
     */
    public abstract void setSelectedCountry(String selectedCountry);

    /**
     * popup selected country index
     *
     * @return index
     */
    public abstract String getPopupSelectedCountryIndex();

    /**
     * set popup selected country index
     */
    public abstract void setPopupSelectedCountryIndex(String popupSelectedCountryIndex);

    /**
     * country items list.
     */
    private List countryItems;

    /**
     * indicate if we should close the popup.
     *
     * @return boolean
     */
    public abstract boolean isClosePopup();

    /**
     * set if we should close the popup.
     */
    public abstract void setClosePopup(boolean closePopup);

    /**
     * Popup the country select dialog.
     *
     * @param cycle
     */
    public void popupCountrySelect(IRequestCycle cycle) {
        logger.debug("some process can be here before popup! query database or something.");
    }

    /**
     * Popup the country select dialog.
     *
     * @param cycle
     */
    public void selectCountry(IRequestCycle cycle) {
        int index = 0;
        String countryIndex = cycle.getParameter("popupSelectedCountryIndex");
        if (countryIndex != null) {
            index = Integer.parseInt(countryIndex);
        }
        setSelectedCountry(((Locale) getCountryItems().get(index)).getDisplayCountry(getLocale()));
        setClosePopup(true);
    }

    /**
     * Gets the items
     *
     * @return country items
     */
    public List getCountryItems() {
        if (countryItems == null) {
            countryItems = createCountryItems();
        }
        return countryItems;
    }

    /**
     * Create the country items.
     *
     * @return
     */
    private List createCountryItems() {
        List items = new ArrayList();
        Locale[] availableLocales = Locale.getAvailableLocales();
        for (int i = 0; i < availableLocales.length; i++) {
            Locale availableLocale = availableLocales[i];
            if (availableLocale.getDisplayCountry() == null || availableLocale.getDisplayCountry().length() == 0) {
                continue;
            }
            items.add(availableLocale);
        }
        return items;
    }

    /** Dummy listener */
    public void doNothing() {
    }
}
