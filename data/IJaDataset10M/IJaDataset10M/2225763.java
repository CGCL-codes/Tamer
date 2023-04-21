package org.zeroexchange.model.location;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.hibernate.annotations.Cascade;
import org.zeroexchange.model.StringPKPersistent;
import org.zeroexchange.model.i18n.CityStrings;
import org.zeroexchange.model.i18n.Strings;
import org.zeroexchange.model.i18n.TitlesAware;

/**
 * @author black
 *
 */
@Entity
public class City extends StringPKPersistent implements TitlesAware<City, CityStrings> {

    private static final long serialVersionUID = 1L;

    public static final String FIELD_COUNTRY = "country";

    private Country country;

    private CityStrings titles;

    @ManyToOne(optional = false)
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @OneToOne(cascade = CascadeType.ALL, mappedBy = Strings.FIELD_TARGET)
    @Cascade({ org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public CityStrings getTitles() {
        if (titles == null) {
            titles = new CityStrings();
            titles.setTarget(this);
        }
        return titles;
    }

    public void setTitles(CityStrings titles) {
        this.titles = titles;
    }
}
