package com.genia.toolbox.projects.toolbox_basics_project.business.dao;

import com.genia.toolbox.persistence.exception.PersistenceException;
import com.genia.toolbox.projects.toolbox_basics_project.bean.model.City;

/**
 * the city business dao interface.
 */
public interface CityBusinessDao {

    /**
   * update {@link City} city.
   * 
   * @param city
   *          to update
   * @return the {@link City}.
   * @throws PersistenceException
   *           the persistence exception.
   */
    public City updateCity(City city) throws PersistenceException;

    /**
   * save {@link City} city.
   * 
   * @param city
   *          to save
   * @return the {@link City}.
   * @throws PersistenceException
   *           the persistence exception.
   */
    public City saveCity(City city) throws PersistenceException;

    /**
   * returns a {@link City} knowing its identifier.
   * 
   * @param identifier
   *          the identifier of the {@link City}
   * @return the {@link City} associated to the given identifier or
   *         <code>null</code> if no such city exists.
   */
    public City getCity(Long identifier);

    /**
   * delete {@link City} with this identifier.
   * 
   * @param identifier
   *          {@link City} identifier.
   * @throws PersistenceException
   *           the persistence exception.
   */
    public void delete(Long identifier) throws PersistenceException;
}
