package org.jlense.uiworks.internal.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.jlense.uiworks.internal.IWorkbenchConstants;
import org.jlense.uiworks.internal.WorkbenchPlugin;
import org.jlense.uiworks.internal.dialogs.AdaptableCollectionElement;
import org.jlense.uiworks.internal.dialogs.WorkbenchWizardElement;
import org.jlense.uiworks.internal.misc.Sorter;
import org.jlense.uiworks.internal.model.AdaptableList;

/**
 *	Instances of this class provide a simple API to the workbench for
 *	accessing of the core registry.  It accepts a registry at creation
 *	time and extracts workbench-related information from it as requested.
 */
public class NewWizardsRegistryReader extends WizardsRegistryReader {

    private boolean projectsOnly;

    private ArrayList deferWizards = null;

    private ArrayList deferCategories = null;

    public static final String BASE_CATEGORY = "Base";

    public static final String EXAMPLES_WIZARD_CATEGORY = "Examples";

    private static final String FULL_EXAMPLES_WIZARD_CATEGORY = "org.jlense.uiworks.Examples";

    private static final String TAG_CATEGORY = "category";

    private static final String UNCATEGORIZED_WIZARD_CATEGORY = "org.jlense.uiworks.Other";

    private static final String UNCATEGORIZED_WIZARD_CATEGORY_LABEL = "Other";

    private static final String CATEGORY_SEPARATOR = "/";

    private static final String ATT_CATEGORY = "category";

    private static final String ATT_PROJECT = "project";

    private static final String STR_TRUE = "true";

    private class CategoryNode {

        private Category category;

        private String path;

        public CategoryNode(Category cat) {
            category = cat;
            path = "";
            String[] categoryPath = category.getParentCategoryPath();
            if (categoryPath != null) {
                for (int nX = 0; nX < categoryPath.length; nX++) {
                    path += categoryPath[nX] + '/';
                }
            }
            path += cat.getID();
        }

        public String getPath() {
            return path;
        }

        public Category getCategory() {
            return category;
        }
    }

    /**
 * Constructs a new reader.  All wizards are read, including projects.
 */
    public NewWizardsRegistryReader() {
        this(false);
    }

    /**
 * Constructs a new reader.
 *
 * @param projectsOnly if true, only projects are read.
 */
    public NewWizardsRegistryReader(boolean projectsOnly) {
        super(IWorkbenchConstants.PL_NEW_WIZARDS);
        this.projectsOnly = projectsOnly;
    }

    protected void addNewElementToResult(WorkbenchWizardElement element, IConfigurationElement config, AdaptableList result) {
        deferWizard(element);
    }

    /**
 *	Create and answer a new AdaptableCollectionElement, configured as a
 *	child of <code>parent</code>
 *
 *	@return org.jlense.uiworks.internal.model.AdaptableCollectionElement
 *	@param parent org.jlense.uiworks.internal.model.AdaptableCollectionElement
 *	@param childName java.lang.String
 */
    protected AdaptableCollectionElement createCollectionElement(AdaptableCollectionElement parent, String id, String label) {
        AdaptableCollectionElement newElement = new AdaptableCollectionElement(id, label, parent);
        parent.add(newElement);
        return newElement;
    }

    /**
 * Creates empty element collection. Overrider to fill
 * initial elements, if needed.
 */
    protected AdaptableList createEmptyWizardCollection() {
        return new AdaptableCollectionElement("root", "root", null);
    }

    /**
 * Returns a new WorkbenchWizardElement configured according to the parameters
 * contained in the passed Registry.  
 *
 * May answer null if there was not enough information in the Extension to create 
 * an adequate wizard
 */
    protected WorkbenchWizardElement createWizardElement(IConfigurationElement element) {
        if (projectsOnly) {
            String flag = element.getAttribute(ATT_PROJECT);
            if (flag == null || !flag.equalsIgnoreCase(STR_TRUE)) return null;
        }
        return super.createWizardElement(element);
    }

    /**
 * Stores a category element for deferred addition.
 */
    private void deferCategory(IConfigurationElement config) {
        Category category = null;
        try {
            category = new Category(config);
        } catch (CoreException e) {
            WorkbenchPlugin.log("Cannot create category: ", e.getStatus());
            return;
        }
        if (deferCategories == null) deferCategories = new ArrayList(20);
        deferCategories.add(category);
    }

    /**
 * Stores a wizard element for deferred addition.
 */
    private void deferWizard(WorkbenchWizardElement element) {
        if (deferWizards == null) deferWizards = new ArrayList(50);
        deferWizards.add(element);
    }

    /**
 * Finishes the addition of categories.  The categories are sorted and
 * added in a root to depth traversal.
 */
    private void finishCategories() {
        if (deferCategories == null) return;
        CategoryNode[] flatArray = new CategoryNode[deferCategories.size()];
        for (int i = 0; i < deferCategories.size(); i++) {
            flatArray[i] = new CategoryNode((Category) deferCategories.get(i));
        }
        Sorter sorter = new Sorter() {

            public boolean compare(Object o1, Object o2) {
                String s1 = ((CategoryNode) o1).getPath();
                String s2 = ((CategoryNode) o2).getPath();
                return s2.compareTo(s1) > 0;
            }
        };
        Object[] sortedCategories = sorter.sort(flatArray);
        for (int nX = 0; nX < sortedCategories.length; nX++) {
            Category cat = ((CategoryNode) sortedCategories[nX]).getCategory();
            finishCategory(cat);
        }
        deferCategories = null;
    }

    /**
 * Save new category definition.
 */
    private void finishCategory(Category category) {
        AdaptableCollectionElement currentResult = (AdaptableCollectionElement) wizards;
        String[] categoryPath = category.getParentCategoryPath();
        AdaptableCollectionElement parent = currentResult;
        if (categoryPath != null) {
            for (int i = 0; i < categoryPath.length; i++) {
                AdaptableCollectionElement tempElement = getChildWithID(parent, categoryPath[i]);
                if (tempElement == null) {
                    return;
                } else parent = tempElement;
            }
        }
        Object test = getChildWithID(parent, category.getID());
        if (test != null) return;
        if (parent != null) createCollectionElement(parent, category.getID(), category.getLabel());
    }

    /**
 *	Insert the passed wizard element into the wizard collection appropriately
 *	based upon its defining extension's CATEGORY tag value
 *
 *	@param element WorkbenchWizardElement
 *	@param extension 
 *	@param currentResult AdaptableCollectionElement
 */
    private void finishWizard(WorkbenchWizardElement element, IConfigurationElement config, AdaptableList result) {
        AdaptableCollectionElement currentResult = (AdaptableCollectionElement) result;
        StringTokenizer familyTokenizer = new StringTokenizer(getCategoryStringFor(config), CATEGORY_SEPARATOR);
        AdaptableCollectionElement currentCollectionElement = currentResult;
        boolean moveToOther = false;
        while (familyTokenizer.hasMoreElements()) {
            AdaptableCollectionElement tempCollectionElement = getChildWithID(currentCollectionElement, familyTokenizer.nextToken());
            if (tempCollectionElement == null) {
                moveToOther = true;
                break;
            } else currentCollectionElement = tempCollectionElement;
        }
        if (moveToOther) moveElementToUncategorizedCategory(currentResult, element); else currentCollectionElement.add(element);
    }

    /**
 * Finishes the addition of wizards.  The wizards are processed and categorized.
 */
    private void finishWizards() {
        if (deferWizards != null) {
            Iterator iter = deferWizards.iterator();
            while (iter.hasNext()) {
                WorkbenchWizardElement wizard = (WorkbenchWizardElement) iter.next();
                IConfigurationElement config = wizard.getConfigurationElement();
                finishWizard(wizard, config, wizards);
            }
            deferWizards = null;
        }
    }

    /**
 *	Return the appropriate category (tree location) for this Wizard.
 *	If a category is not specified then return a default one.
 */
    protected String getCategoryStringFor(IConfigurationElement config) {
        String result = config.getAttribute(ATT_CATEGORY);
        if (result == null) result = UNCATEGORIZED_WIZARD_CATEGORY;
        return result;
    }

    /**
 *	Go through the children of  the passed parent and answer the child
 *	with the passed name.  If no such child is found then return null.
 *
 *	@return org.jlense.uiworks.internal.model.AdaptableCollectionElement
 *	@param parent org.jlense.uiworks.internal.model.AdaptableCollectionElement
 *	@param childName java.lang.String
 */
    protected AdaptableCollectionElement getChildWithID(AdaptableCollectionElement parent, String id) {
        Object[] children = parent.getChildren();
        for (int i = 0; i < children.length; ++i) {
            AdaptableCollectionElement currentChild = (AdaptableCollectionElement) children[i];
            if (currentChild.getId().equals(id)) return currentChild;
        }
        return null;
    }

    /**
 *	Moves given element to "Other" category, previously creating one if missing.
 */
    protected void moveElementToUncategorizedCategory(AdaptableCollectionElement root, WorkbenchWizardElement element) {
        AdaptableCollectionElement otherCategory = getChildWithID(root, UNCATEGORIZED_WIZARD_CATEGORY);
        if (otherCategory == null) otherCategory = createCollectionElement(root, UNCATEGORIZED_WIZARD_CATEGORY, UNCATEGORIZED_WIZARD_CATEGORY_LABEL);
        otherCategory.add(element);
    }

    /**
 * Removes the empty categories from a wizard collection. 
 */
    private void pruneEmptyCategories(AdaptableCollectionElement parent) {
        Object[] children = parent.getChildren();
        for (int nX = 0; nX < children.length; nX++) {
            AdaptableCollectionElement child = (AdaptableCollectionElement) children[nX];
            pruneEmptyCategories(child);
            boolean shouldPrune = projectsOnly || child.getId().equals(FULL_EXAMPLES_WIZARD_CATEGORY);
            if (child.isEmpty() && shouldPrune) parent.remove(child);
        }
    }

    /**
 * Implement this method to read element attributes.
 */
    protected boolean readElement(IConfigurationElement element) {
        if (element.getName().equals(TAG_CATEGORY)) {
            deferCategory(element);
            return true;
        } else {
            return super.readElement(element);
        }
    }

    /**
 * Reads the wizards in a registry.  
 * <p>
 * This implementation uses a defering strategy.  All of the elements 
 * (categories, wizards) are read.  The categories are created as the read occurs. 
 * The wizards are just stored for later addition after the read completes.
 * This ensures that wizard categorization is performed after all categories
 * have been read.
 * </p>
 */
    protected void readWizards() {
        super.readWizards();
        finishCategories();
        finishWizards();
        if (wizards != null) {
            AdaptableCollectionElement parent = (AdaptableCollectionElement) wizards;
            pruneEmptyCategories(parent);
        }
    }
}
