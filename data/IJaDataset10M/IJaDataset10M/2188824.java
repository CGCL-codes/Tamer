package org.eteamcs.service;

import java.util.List;
import org.eteamcs.dao.ICountryDAO;
import org.eteamcs.model.Country;
import org.springframework.validation.Validator;

/**
 * @author Pneves
 *
 */
public interface ICountryManager {

    public void setCountryDAO(ICountryDAO dao);

    public List getCountries();

    public Country getCountry(String acronym);

    public Country saveCountry(Country category);

    public void removeCountry(String acronym);

    public void setValidator(Validator validator);
}
