package org.middleheaven.global.atlas.modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.middleheaven.core.wiring.service.Service;
import org.middleheaven.global.atlas.AtlasLocale;
import org.middleheaven.global.atlas.AtlasModule;
import org.middleheaven.global.atlas.AtlasService;
import org.middleheaven.global.atlas.ChronologicalCountryBuilder;
import org.middleheaven.global.atlas.Country;
import org.middleheaven.global.atlas.CountryNotFoundException;
import org.middleheaven.global.atlas.Town;

@Service
public class ModularAtlasService implements AtlasService {

    private ChronologicalCountryBuilder context = new ChronologicalCountryBuilder();

    public ModularAtlasService() {
        Collection<AtlasModule> modules = new ArrayList<AtlasModule>();
        modules.add(new ISOFileAtlasModule());
        for (AtlasModule module : modules) {
            try {
                module.loadAtlas(context);
            } catch (AtlasNotFoundException e) {
            }
        }
        if (context.countries().isEmpty()) {
            new DefaultAtlasModule().loadAtlas(context);
        }
        context.build();
    }

    @Override
    public Country findCountry(String isoCode) {
        Country country = context.get(isoCode);
        if (country == null) {
            throw new CountryNotFoundException("Country " + isoCode + " was not found");
        }
        return country;
    }

    @Override
    public Collection<Country> findALLCountries() {
        return Collections.unmodifiableCollection(context.countries());
    }

    @Override
    public Town findTown(Country country, String name) {
        for (AtlasLocale division : country.getChildren()) {
            for (AtlasLocale city : division.getChildren()) {
                if (((Town) city).getName().equals(name)) {
                    return (Town) city;
                }
            }
        }
        return null;
    }

    @Override
    public Town findTown(String isoCountryCode, String name) {
        return findTown(this.findCountry(isoCountryCode), name);
    }
}
