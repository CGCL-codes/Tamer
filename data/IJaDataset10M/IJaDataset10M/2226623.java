package org.fluid.mdd.core.repository;

import org.eclipse.swt.graphics.Image;

/**
 * This class allows create generic categories for classify elements.
 * A category class should be specified in order to distingue different
 * category objects.
 * @author linesta
 */
public class Category {

    private String id;

    private String name;

    private Class<?> categoryClass;

    private Image icon;

    /**
	 * Constructor
	 * @param categoryClass The class where this category can be applied
	 * @param id The identifier of the category
	 * @param name The displayable name of the category
	 */
    public Category(Class<?> categoryClass, String id, String name, Image icon) {
        this.categoryClass = categoryClass;
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    /** @return the id */
    public String getId() {
        return id;
    }

    /** @return the name */
    public String getName() {
        return name;
    }

    /** @return the categoryClass */
    public Class<?> getCategoryClass() {
        return categoryClass;
    }

    /** @return the icon */
    public Image getIcon() {
        return icon;
    }

    /**
	 * @param categoryClass
	 * @return if the category corresponds to the specified category class or not
	 */
    public boolean isCategoryOf(Class<?> categoryClass) {
        if (this.categoryClass == null) return false;
        return this.categoryClass.equals(categoryClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category) if (id != null) return id.equals(((Category) obj).id); else return ((Category) obj).id == null;
        return false;
    }

    @Override
    public int hashCode() {
        if (id != null) return id.hashCode();
        return super.hashCode();
    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }
}
