package org.hinjector.examples.repository;

import java.util.List;

/**
 * @Author Fabio Kung
 */
public interface CategoryRepository {

    List<Category> getOpenedCategoriesFor(Product product);
}
