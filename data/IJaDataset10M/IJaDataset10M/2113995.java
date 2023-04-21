package com.ar4j.bench.common.service;

import com.ar4j.bench.common.domain.CategoryWithProductsWithItems;
import com.ar4j.bench.common.domain.ICategory;

/**
 * The API for all category service implementations, defines the minimum set of operations required to run the benchmark tests.
 */
public interface ICategoryService {

    /**
   * Add the provided category to the backing store
   */
    Long addCategory(ICategory category);

    /**
   * Retrieve a category by its ID
   */
    ICategory findById(Long id);

    /**
   * Update the provided category in the backing store
   */
    void updateCategory(ICategory category);

    /**
   * Remove a category with the given ID from the backing store
   */
    void removeById(Long id);

    /**
   * Add a catalog subtree (category w/ product w/ item composite) to the backing store
   */
    Long addCategoryWithProductsWithItems(CategoryWithProductsWithItems category);

    /**
   * Retrieve a catalog subtree by its category's ID
   */
    CategoryWithProductsWithItems findCategoryWithProductsWithItemsById(Long id);

    /**
   * Update a catalog subtree in the backing store
   */
    void updateCategoryWithProductsWithItems(CategoryWithProductsWithItems category);

    /**
   * Remvoe a catalog subtree from the backint store
   */
    void removeCategoryWithProductsWithItemsById(Long id);
}
