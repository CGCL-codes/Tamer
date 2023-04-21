package com.inet.qlcbcc.repository;

import org.webos.core.repository.RepositoryException;
import org.webos.repository.hibernate.HibernateModifiableRepository;
import com.inet.qlcbcc.domain.Dictionary;

/**
 * DictionaryModifiableRepository.
 *
 * @author Dung Nguyen
 * @version $Id: DictionaryModifiableRepository.java 2011-05-09 12:05:21z nguyen_dv $
 *
 * @since 1.0
 */
public interface DictionaryModifiableRepository extends HibernateModifiableRepository<Dictionary, String> {

    /**
   * Deletes all dictionary from the given dictionary id.
   *
   * @param ids the given array of dictionary identifiers.
   * @return the real of number of delete object.
   * @throws RepositoryException if an error occurs during delete dictionaries.
   */
    int delete(String... ids) throws RepositoryException;
}
