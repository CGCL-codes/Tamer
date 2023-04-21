package org.interlogy;

import org.jboss.seam.ScopeType;
import org.jboss.seam.log.Log;
import org.jboss.seam.framework.EntityController;
import org.jboss.seam.annotations.*;
import javax.faces.FacesException;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: naryzhny
 * Date: 11.06.2007
 * Time: 20:41:58
 * To change this template use File | Settings | File Templates.
 */
@Name("navigation")
public class Navigation {

    @In(scope = ScopeType.SESSION, required = false)
    @Out(scope = ScopeType.SESSION, required = false)
    private Object currentObject;

    @In(scope = ScopeType.SESSION, required = false)
    @Out(scope = ScopeType.SESSION, required = false)
    private Category cacheCurrentCategory;

    @In
    private EntityManager entityManager;

    @Logger
    private Log log;

    public void setCurrentObject(Object object) {
        currentObject = object;
        cacheCurrentCategory = null;
    }

    private EntityManager getEntityManager() {
        return entityManager;
    }

    public Category getCurrentCategory() {
        try {
            if (cacheCurrentCategory == null) {
                if (currentObject == null) cacheCurrentCategory = null; else {
                    if (currentObject instanceof Category) {
                        cacheCurrentCategory = (Category) currentObject;
                    } else if (currentObject instanceof Problematic) {
                        cacheCurrentCategory = ((Problematic) currentObject).getCategory();
                    } else if (currentObject instanceof Case) {
                        cacheCurrentCategory = ((Case) currentObject).getProblematic().getCategory();
                    } else if (currentObject instanceof Solution) {
                        cacheCurrentCategory = ((Solution) currentObject).getProblematic().getCategory();
                    }
                }
            }
            if (cacheCurrentCategory == null) cacheCurrentCategory = getEntityManager().find(Category.class, (long) 1);
            return cacheCurrentCategory;
        } catch (Throwable e) {
            log.error("Unknown throwable: ", e);
            throw new FacesException(e);
        }
    }

    private <T> T reinitiate(T t) {
        return getEntityManager().merge(t);
    }

    public List<Category> getNavigationLine() {
        List<Category> ret = new ArrayList();
        Category currentCat = getCurrentCategory();
        Category cat = currentCat;
        while (cat != null) {
            ret.add(cat);
            if (cat.getType().equals(CategoryType.MAIN) && !cat.equals(currentCat)) break;
            cat = cat.getParent();
        }
        Collections.reverse(ret);
        return ret;
    }

    public Category[] getMainCategories() {
        return new Category[] { getMainCategory() };
    }

    public Category getMainCategory() {
        Category category = getCurrentCategory();
        while (category != null && category.getParent() != null) {
            category = category.getParent();
            if (CategoryType.MAIN.equals(category.getType())) return category;
        }
        return category;
    }
}
