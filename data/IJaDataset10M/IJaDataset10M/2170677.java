package org.eclipse.update.core;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.update.internal.core.Messages;
import org.eclipse.update.internal.core.UpdateCore;

/**
 * Convenience implementation of a feature reference.
 * <p>
 * This class may be instantiated or subclassed by clients.
 * </p> 
 * <p>
 * <b>Note:</b> This class/interface is part of an interim API that is still under development and expected to
 * change significantly before reaching stability. It is being made available at this early stage to solicit feedback
 * from pioneering adopters on the understanding that any code that uses this API will almost certainly be broken
 * (repeatedly) as the API evolves.
 * </p>
 * @see org.eclipse.update.core.IFeatureReference
 * @see org.eclipse.update.core.model.FeatureReferenceModel
 * @see org.eclipse.update.core.ISiteFeatureReference
 * @see org.eclipse.update.core.SiteFeatureReferenceModel 
 * @since 2.1
 */
public class SiteFeatureReference extends SiteFeatureReferenceModel implements ISiteFeatureReference {

    private List categories;

    /**
	 * Feature reference default constructor
	 */
    public SiteFeatureReference() {
        super();
    }

    /**
	 * Constructor FeatureReference.
	 * @param ref the reference to copy
	 */
    public SiteFeatureReference(ISiteFeatureReference ref) {
        super(ref);
    }

    /**
	 * Returns an array of categories the referenced feature belong to.
	 * 
	 * @see ISiteFeatureReference#getCategories()
	 * @since 2.1 
	 */
    public ICategory[] getCategories() {
        if (categories == null) {
            categories = new ArrayList();
            String[] categoriesAsString = getCategoryNames();
            for (int i = 0; i < categoriesAsString.length; i++) {
                ICategory siteCat = getSite().getCategory(categoriesAsString[i]);
                if (siteCat != null) categories.add(siteCat); else {
                    String siteURL = getSite().getURL() != null ? getSite().getURL().toExternalForm() : null;
                    UpdateCore.warn("Category " + categoriesAsString[i] + " not found in Site:" + siteURL);
                }
            }
        }
        if (categories.size() == 0) {
            ICategory category = new Category(Messages.SiteCategory_other_label, Messages.SiteCategory_other_description);
            categories.add(category);
        }
        ICategory[] result = new ICategory[0];
        if (!(categories == null || categories.isEmpty())) {
            result = new ICategory[categories.size()];
            categories.toArray(result);
        }
        return result;
    }

    /**
	 * Adds a category to the referenced feature.
	 * 
	 * @see ISiteFeatureReference#addCategory(ICategory)
	 * @since 2.1 
	 */
    public void addCategory(ICategory category) {
        this.addCategoryName(category.getName());
    }
}
