package org.kootox.episodesmanager.services;

import java.util.Map;

/**
 *
 * @author couteau
 */
public interface WebsiteService {

    /**
     * Make a search on a website, return the id and the name of all the series
     * returned by this search.
     * @param name the serie name to search
     * @return a map containing the id as key and the serie title as value.
     */
    Map<Integer, String> search(String name);

    /**
     * Add a serie to the database
     * @param id the serie id
     */
    void add(Integer id);
}
