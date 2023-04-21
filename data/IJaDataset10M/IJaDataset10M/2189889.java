package org.blueoxygen.komodo.category.actions;

import org.blueoxygen.cimande.LogInformation;
import org.blueoxygen.cimande.persistence.PersistenceAware;
import org.blueoxygen.cimande.security.SessionCredentials;
import org.blueoxygen.cimande.security.SessionCredentialsAware;
import org.blueoxygen.komodo.category.ArticleCategory;

/**
 * @author Harry
 *  
 */
public class AddCategory extends FormCategory implements SessionCredentialsAware, PersistenceAware {

    private SessionCredentials sessionCredentials;

    public String execute() {
        if (getName().equalsIgnoreCase("")) addActionError("Please insert the name of Category");
        if (hasErrors()) return INPUT; else {
            ArticleCategory category = new ArticleCategory();
            category.setName(getName());
            category.setDescription(getDescription());
            LogInformation log = new LogInformation();
            log.setCreateBy(sessionCredentials.getCurrentUser().getId());
            category.setLogInformation(log);
            ArticleCategory parentCat = (ArticleCategory) pm.getById(ArticleCategory.class, getParentCategoryId());
            if (parentCat != null) {
                category.setParentCategory(parentCat);
            }
            pm.save(category);
            return SUCCESS;
        }
    }

    public void setSessionCredentials(SessionCredentials sessionCredentials) {
        this.sessionCredentials = sessionCredentials;
    }
}
