package org.rapla.entities;

import java.util.Locale;

/** Hierarchical categorization of information.
 * Categories can be used as attribute values.
 *   @see org.rapla.entities.dynamictype.Attribute
 */
public interface Category extends MultiLanguageNamed, Entity<Category>, RaplaObject, Annotatable {

    final RaplaType TYPE = new RaplaType(Category.class, "category");

    /** add a sub-category.
     * This category is set as parent of the passed category.*/
    void addCategory(Category category);

    /** remove a sub-category */
    void removeCategory(Category category);

    /** returns all subcategories */
    Category[] getCategories();

    /** returns the subcategory with the specified key.
     * null if subcategory was not found. */
    Category getCategory(String key);

    /** find a sub-category in that equals the specified category. */
    Category findCategory(Category copy);

    /** Returns the parent of this category or null if the category has no parent.*/
    Category getParent();

    /** returns true if the passed category is a direct child of this category */
    boolean hasCategory(Category category);

    /** set the key of the category. The can be used in the getCategory() method for lookup. */
    void setKey(String key);

    /** returns the key of the category */
    String getKey();

    /** returns true this category is an ancestor
     *  (parent or parent of parent, ...) of the specified
     * category */
    boolean isAncestorOf(Category category);

    /** returns the path form the rootCategory to this category.
     *   Path elements are the category-names in the selected locale separated
     *   with the / operator. If the rootCategory is null the path will be calculated
     *   to the top-most parent.
     *   Example: <strong>area51/aliencell</strong>
    */
    String getPath(Category rootCategory, Locale locale);

    /** returns the number of ancestors.
     * (How many Time you must call getParent() until you receive null) */
    int getDepth();

    Category[] CATEGORY_ARRAY = new Category[0];
}
