package ch.fusun.baron.player.api;

import java.util.Map;
import ch.fusun.baron.core.injection.Inject;
import ch.fusun.baron.data.DataUpdate;

/**
 * Update class for {@link CountryService}
 */
public class CountryUpdate implements DataUpdate {

    @Inject
    private transient CountryService countryService;

    private Map<String, Country> countries;

    /**
	 * Kryo
	 */
    public CountryUpdate() {
    }

    /**
	 * @param countries
	 *            The countries
	 */
    public CountryUpdate(Map<String, Country> countries) {
        this.countries = countries;
    }

    @Override
    public void update() {
        countryService.setCountries(this.countries);
    }
}
