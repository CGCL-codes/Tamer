package com.mysolution.persistence;

import com.mysolution.persistence.cache.index.IndexValue;
import java.util.Set;
import java.util.List;

/**
 * DKu
 * Date: Mar 7, 2009
 * Time: 9:47:12 PM
 */
public interface PersistenceService {

    /**
     * Loads domain objects specified by class and domain's index key.
     *
     * @param domainClass calss of domain
     * @param idx index's key
     * @param <T> type of domain
     * @return loaded domain objects or empty list if not found
     */
    <T extends Domain> Set<T> loadDomains(Class<? extends T> domainClass, IndexValue idx);

    /**
     * Loads domain object specified by class and domain's index key.
     * Use method when you are sure that index is unique.
     * Method throws RuntimeException if there are returned more then one element.
     *
     * @param domainClass calss of domain
     * @param idx index's key
     * @param <T> type of domain
     * @return loaded domain objects or empty list if not found
     */
    <T extends Domain> T loadDomain(Class<? extends T> domainClass, IndexValue idx);

    /**
     * Loads domain objects specified by class and domain objects' ids.
     *
     * @param domainClass class of domain
     * @param ids domain objects' ids
     * @param <T> type fo domain
     * @return loaded recourses or empty list if not found
     */
    <T extends Domain> List<T> loadDomains(Class<? extends T> domainClass, List<DomainID> ids);

    /**
     * Loads domain specified by class and domain objects' id.
     *
     * @param domainClass class of domain
     * @param id domain objects' id
     * @param <T> type fo domain
     * @return loaded recourses or empty list if not found
     */
    <T extends Domain> T loadDomain(Class<? extends T> domainClass, DomainID id);

    /**
     * Saves domain objects.
     * Call this method for newly created objects
     *
     * @param domainClass class of domain objects
     * @param domain domain objects
     * @param <T> domain class
     */
    <T extends Domain> void save(Class<? extends T> domainClass, T domain);

    /**
     * Updates domain objects.
     *
     * @param domainClass class of domain objects
     * @param domain domain objects
     * @param <T> domain class
     */
    <T extends Domain> void update(Class<? extends T> domainClass, T domain);

    /**
     * Removes domain object.
     *
     * @param domainClass domain class
     * @param id domain object's id
     * @param <T> type of domain
     */
    <T extends Domain> void remove(Class<? extends T> domainClass, DomainID id);
}
