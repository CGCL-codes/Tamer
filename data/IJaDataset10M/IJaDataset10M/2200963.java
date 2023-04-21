package com.inet.qlcbcc.service;

import java.util.List;
import org.webos.core.option.Option;
import org.webos.core.service.BusinessServiceException;
import com.inet.qlcbcc.domain.Dictionary;

/**
 * DictionaryService.
 *
 * @author Hien Nguyen Van
 * @version $Id: DictionaryService.java 2011-03-20 12:00:00z nguyen_hv $
 *
 * @since 1.0
 */
public interface DictionaryService {

    /**
   * Save dictionary
   *
   * @param dictionary the given dictionary.
   * @throws BusinessServiceException if an error occurs during save data
   */
    void save(Dictionary dictionary) throws BusinessServiceException;

    /**
   * Delete dictionary from the given dictionary identifiers.
   * 
   * @param ids the given array of dictionary identifiers.
   * @throws BusinessServiceException if an error occurs during deleting
   */
    boolean delete(String... ids) throws BusinessServiceException;

    /**
   * Update a given dictionary.
   *
   * @param dictionary the given dictionary object.
   * @throws BusinessServiceException if an error occurs during updating dictionary.
   */
    void update(Dictionary dictionary) throws BusinessServiceException;

    /**
   * Finds all dictionary that belong to the given dictionary.
   *
   * @param key the given dictionary key.
   * @param level the given dictionary level.
   * @return all {@link Dictionary dictionary} objects that belong to the given key.
   * @throws BusinessServiceException if an error occurs during finding the key.
   */
    List<Dictionary> findByKey(String key, int level) throws BusinessServiceException;

    /**
   * Finds all dictionary that belong to the given dictionary keys.
   *
   * @param level the given dictionary level.
   * @param keys the given dictionary keys.
   * @return all {@link Dictionary dictionary} objects that belong to the given key.
   * @throws BusinessServiceException if an error occurs during finding the key.
   */
    List<Dictionary> findByKeys(int level, String... key) throws BusinessServiceException;

    /**
   * load the Dictionary from the given dictionary key.
   *
   * @param key the given dictionary key.
   * @return the {@link Dictionary} object.
   * @throws BusinessServiceException if an error occurs during finding the dictionary.
   */
    Option<Dictionary> loadByKey(String key) throws BusinessServiceException;

    /**
   *  load the dictionary from the give key , desc and level ss
   * @param key
   * @param desc
   * @param level
   * @return the {@link Dictionary} object
   * @throws BusinessServiceException
   */
    List<Dictionary> findBy(String key, String desc, int level) throws BusinessServiceException;
}
