package au.org.tpac.portal.repository;

import java.util.List;
import au.org.tpac.portal.domain.Category;

/**
 * The Interface CategoryDao.
 */
public interface CategoryDao {

    /**
     * Gets the categories.
     * 
     * @return the categories
     */
    List<Category> findCategories();

    /**
     * Gets the categories.
     * 
     * @param ids the category ids
     * @return the categories
     */
    List<Category> findCategories(List<Integer> ids);

    /**
     * Gets the category.
     * 
     * @param id
     *            the id
     * 
     * @return the category
     */
    Category findCategory(int id);

    /**
     * Insert category.
     * 
     * @param category the category
     * @return the new category Id
     */
    int insert(Category category);

    /**
     * Delete category.
     * 
     * @param categoryId the category id
     * @return true, if successful
     */
    boolean delete(int categoryId);

    /**
     * Update category.
     * 
     * @param category the category
     */
    void update(Category category);
}
