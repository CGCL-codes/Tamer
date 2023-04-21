package de.shandschuh.jaolt.gui.core;

import java.util.Vector;
import javax.swing.JCheckBox;
import de.shandschuh.jaolt.core.Country;
import de.shandschuh.jaolt.tools.array.ArrayHelper;

public class CountryJCheckBox extends JCheckBox {

    /** Default serial version uid */
    private static final long serialVersionUID = 1L;

    private Country country;

    private int hooks;

    private boolean originallySelected;

    public CountryJCheckBox(Country country, boolean checked, boolean enabled) {
        super(country.getName(), checked);
        setEnabled(enabled);
        this.country = country;
    }

    public CountryJCheckBox(Country country, boolean checked) {
        this(country, checked, true);
    }

    public CountryJCheckBox(Country country) {
        this(country, false);
    }

    public Country getCountry() {
        return country;
    }

    public void addHook() {
        if (hooks == 0) {
            originallySelected = isSelected();
        }
        hooks++;
        setSelected(true);
        setEnabled(false);
    }

    public boolean addHook(Country[] countries) {
        if (ArrayHelper.indexOf(countries, country) > -1) {
            addHook();
            return true;
        }
        return false;
    }

    public void removeHook() {
        hooks--;
        setEnabled(hooks < 1);
        if (hooks == 0) {
            setSelected(originallySelected);
        }
    }

    public void resetHooks() {
        hooks = 0;
        setEnabled(true);
    }

    public void selectIfExists(Country[] countries) {
        setSelected(ArrayHelper.indexOf(countries, country) > -1);
    }

    public static CountryJCheckBox[] create(Vector<Country> shipToLocations, Country[] countries) {
        int length = shipToLocations.size();
        CountryJCheckBox[] result = new CountryJCheckBox[length];
        for (int n = 0; n < length; n++) {
            result[n] = new CountryJCheckBox(shipToLocations.get(n));
            result[n].selectIfExists(countries);
        }
        return result;
    }

    public static void updateSelection(CountryJCheckBox[] countryJCheckBoxes, Country[] countries) {
        for (int n = 0, i = countryJCheckBoxes != null ? countryJCheckBoxes.length : 0; n < i; n++) {
            countryJCheckBoxes[n].selectIfExists(countries);
        }
    }

    public static void addHooks(CountryJCheckBox[] countryJCheckBoxes, Country[] countries) {
        for (int n = 0, i = countryJCheckBoxes != null ? countryJCheckBoxes.length : 0; n < i; n++) {
            countryJCheckBoxes[n].addHook(countries);
        }
    }

    public static void resetHooks(CountryJCheckBox[] countryJCheckBoxes) {
        for (int n = 0, i = countryJCheckBoxes != null ? countryJCheckBoxes.length : 0; n < i; n++) {
            if (countryJCheckBoxes[n] != null) {
                countryJCheckBoxes[n].resetHooks();
            }
        }
    }
}
