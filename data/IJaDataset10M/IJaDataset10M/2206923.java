package com.beacon.rpg.server.api.dao;

import com.beacon.rpg.server.types.Category;
import java.util.List;

/**
 *
 * @author cternent
 */
public interface ICategoryDao {

    /**
     * Returns the list of all categories
     * @return
     */
    public List<Category> getCategories();

    /**
     * Adds a new category for Tiles
     * @param parent
     * @param name
     * @return
     */
    public int addCategory(int parent, String name);

    /**
     * moves category (given by id) to designated parent
     * @param id
     * @param parent
     */
    public void moveCategoryById(int id, int parent);
}
