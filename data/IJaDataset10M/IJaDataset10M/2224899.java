package net.sourceforge.pebble.dao;

import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.Blog;

public interface CategoryDAO {

    /**
   * Loads the categories.
   *
   * @param blog    the owning blog
   * @return  a Collection of Category instances
   * @throws  PersistenceException    if categories cannot be loaded
   */
    public Category getCategories(Blog blog) throws PersistenceException;

    /**
   * Adds the specified category.
   *
   * @param category    the Category instance to be added
   * @param blog    the owning blog
   * @throws PersistenceException   if something goes wrong storing the category
   */
    public void addCategory(Category category, Blog blog) throws PersistenceException;

    /**
   * Updates the specified category.
   *
   * @param category    the Category instance to be updated
   * @param blog    the owning blog
   * @throws PersistenceException   if something goes wrong storing the category
   */
    public void updateCategory(Category category, Blog blog) throws PersistenceException;

    /**
   * Removes the specified category.
   *
   * @param category    the Category instance to be removed
   * @param blog    the owning blog
   * @throws PersistenceException   if something goes wrong removing the category
   */
    public void deleteCategory(Category category, Blog blog) throws PersistenceException;
}
