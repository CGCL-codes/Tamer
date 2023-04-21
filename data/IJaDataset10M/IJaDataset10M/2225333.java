package com.ahm.db.dao;

import java.util.List;
import com.ahm.db.dao.hibernate.crud.db.dao.GenericDAO;
import com.ahm.db.entity.Countries;
import com.ahm.db.entity.Country;
import com.ahm.exception.AhmDataException;
import com.ahm.exception.AhmSystemException;

/**
 * @author Basha
 *
 */
public interface CountriesDAO extends GenericDAO<Countries, Integer> {

    public Countries getCountryByCountryName(String countryName) throws AhmSystemException, AhmDataException;
}
