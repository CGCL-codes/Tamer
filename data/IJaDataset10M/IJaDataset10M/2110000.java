package net.innig.imre.web;

import net.innig.framework.web.tapestry.InnigValidationDelegate;
import net.innig.imre.domain.Category;
import net.innig.imre.domain.Permission;
import net.innig.imre.domain.User;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.annotations.Bean;
import org.apache.tapestry.event.PageAttachListener;
import org.apache.tapestry.valid.IValidationDelegate;

public abstract class AdminHome extends ImreBasePage implements IExternalPage, PageAttachListener {

    public abstract boolean getAllowCategoryEdit();

    public abstract void setAllowCategoryEdit(boolean value);

    public abstract String getUserDisplayName();

    public abstract void setUserDisplayName(String value);

    @Bean(InnigValidationDelegate.class)
    public abstract IValidationDelegate getValidation();

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {
        init();
    }

    public void init() {
        User currentUser = requireAuthenticatedUser();
        setAllowCategoryEdit(currentUser.hasPermission(Permission.MODIFY, Category.class));
        setUserDisplayName(currentUser.getDisplayName());
    }

    @Override
    public void validate(IRequestCycle cycle) {
        super.validate(cycle);
    }
}
