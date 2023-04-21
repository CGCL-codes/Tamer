package com.actionbazaar.buslogic;

import java.util.LinkedList;
import java.util.List;
import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.actionbazaar.persistence.Category;

/**
 * This bean is responsible for managing pick lists.
 * @author Ryan Cuprak
 */
@Named
@Singleton
public class PickListBean {

    /**
     * Persistence Context
     */
    @PersistenceContext(unitName = "actionbazaar")
    private EntityManager entityManager;

    /**
     * Returns the list of available categories
     * @return categories
     */
    public List<Category> getCategories() {
        Query q = entityManager.createQuery("select c from Category c where c.parentCategory is null");
        List<Category> categories = (List<Category>) q.getResultList();
        return categories;
    }

    /**
     * Given a set of keywords, find matching categories
     * @param keywords - keywords
     * @return List<Category>
     */
    public List<Category> findCategories(String keywords[]) {
        List<Category> categories = new LinkedList<Category>();
        for (String keyword : keywords) {
            Query q = entityManager.createNativeQuery("select category_category_id from keywords where keywords = ?keyword");
            q.setParameter("keyword", keyword);
            List<Long> ids = q.getResultList();
            for (Long id : ids) {
                categories.add(entityManager.find(Category.class, id));
            }
        }
        return categories;
    }

    /**
     * Loads the categories into the database
     * @param categories - categories
     */
    public void addCategories(List<Category> categories) {
        for (Category category : categories) {
            entityManager.persist(category);
        }
    }
}
