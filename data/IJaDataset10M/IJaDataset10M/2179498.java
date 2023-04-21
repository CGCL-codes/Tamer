package org.equanda.tapestry.pages.reportsCategoryIndex;

import org.equanda.tapestry.navigation.PageParameters;
import org.equanda.tapestry.navigation.PageParametersImpl;

/**
 * Description!!!
 *
 * @author <a href="mailto:andrei@paragon-software.ro">Andrei Chiritescu</a>
 */
public class ReportsCategoryParams extends PageParametersImpl {

    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        ensureUnlocked();
        this.category = category;
    }

    @Override
    public ReportsCategoryParams clone(PageParameters newParams) {
        ReportsCategoryParams clone = (ReportsCategoryParams) super.clone(newParams);
        clone.setCategory(category);
        return clone;
    }

    @Override
    protected ReportsCategoryParams newInstance() {
        return new ReportsCategoryParams();
    }

    @Override
    public String toString() {
        return super.toString() + "\n            category   :  " + category;
    }
}
