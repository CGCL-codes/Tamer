package com.ahm.db.dao;

import java.util.List;
import com.ahm.db.dao.hibernate.crud.db.dao.GenericDAO;
import com.ahm.db.entity.State;
import com.ahm.exception.AhmDataException;
import com.ahm.exception.AhmSystemException;

/**
 * @author TILAK
 *
 */
public interface StateDAO extends GenericDAO<State, Long> {

    List<State> getStatesForCountryName(Long countryId) throws AhmSystemException, AhmDataException;

    State getStateIdForStateName(String stateName, Long countryId) throws AhmSystemException, AhmDataException;

    void deleteState(Long id) throws AhmSystemException, AhmDataException;
}
