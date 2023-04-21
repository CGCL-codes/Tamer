package kz.simplex.photobox.action;

import kz.simplex.photobox.model.*;
import java.util.ArrayList;
import java.util.List;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("categoryHome")
public class CategoryHome extends EntityHome<Category> {

    @In(create = true)
    CategoryHome categoryHome;

    public void setCategoryId(Integer id) {
        setId(id);
    }

    public Integer getCategoryId() {
        return (Integer) getId();
    }

    @Override
    protected Category createInstance() {
        Category category = new Category();
        return category;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Category getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }

    public List<Category> getSubcategories() {
        return getInstance() == null ? null : new ArrayList<Category>(getInstance().getSubcategories());
    }
}
