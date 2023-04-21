package com.hack23.cia.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.googlecode.ehcache.annotations.Cacheable;
import com.hack23.cia.model.external.worldbank.countries.impl.CountryElement;
import com.hack23.cia.model.internal.application.data.impl.AggregatedCountryData;
import com.hack23.cia.model.internal.application.data.impl.CountryMetaDataType;
import com.hack23.cia.service.api.DataContainer;
import com.hack23.cia.service.data.api.CountryElementDAO;

/**
 * The Class AggregatedCountryDataContainer.
 */
@Component(value = "AggregatedCountryDataContainer")
@Transactional(propagation = Propagation.SUPPORTS)
public final class AggregatedCountryDataContainer implements DataContainer<AggregatedCountryData, String> {

    /** The country element dao. */
    @Autowired
    private CountryElementDAO countryElementDAO;

    @Override
    @Cacheable(cacheName = "AggregatedCountryData")
    public List<AggregatedCountryData> getAll() {
        final List<AggregatedCountryData> result = new ArrayList<AggregatedCountryData>();
        for (final CountryElement element : countryElementDAO.getAll()) {
            final AggregatedCountryData aggregatedCountryData = new AggregatedCountryData();
            aggregatedCountryData.setCountry(element);
            if (element.getLongitude() != null && element.getLongitude().trim().length() > 0) {
                aggregatedCountryData.setType(CountryMetaDataType.WORLDBANK_DATA);
                result.add(aggregatedCountryData);
            }
        }
        return result;
    }

    @Override
    @Cacheable(cacheName = "AggregatedCountryData")
    public AggregatedCountryData load(final String id) {
        if (id != null) {
            for (final AggregatedCountryData countryData : getAll()) {
                if (countryData.getCountry().getName().toLowerCase().trim().equals(id.toLowerCase().trim())) {
                    return countryData;
                }
            }
        }
        return null;
    }
}
