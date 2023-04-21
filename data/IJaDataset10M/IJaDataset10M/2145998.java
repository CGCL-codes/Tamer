package imtek.optsuite.acquisition.routine;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.graphics.Image;

/**
 * Grouping elements for registrations of MeasurementRoutineSteps.
 * 
 * @author Alexander Bieber <alex@nightlabs.de>
 */
public class MeasurementRoutineStepCategory {

    protected String categoryID;

    protected String categoryDescription;

    protected MeasurementRoutineStepCategory parentCategory;

    protected String parentCategoryID;

    protected List<MeasurementRoutineStepCategory> childCategories = new ArrayList<MeasurementRoutineStepCategory>();

    protected Image icon;

    protected List<MeasurementRoutineStepFactory> stepFactories = new ArrayList<MeasurementRoutineStepFactory>();

    public MeasurementRoutineStepCategory(String categoryID, String categoryDescription, MeasurementRoutineStepCategory parent) {
        super();
        this.categoryID = categoryID;
        this.categoryDescription = categoryDescription;
        this.parentCategory = parent;
    }

    public void addChildCategory(MeasurementRoutineStepCategory child) {
        child.setParentCategory(this);
        childCategories.add(child);
    }

    /**
	 * @return Returns the parentCategory.
	 */
    public MeasurementRoutineStepCategory getParentCategory() {
        return parentCategory;
    }

    /**
	 * @param parentCategory The parentCategory to set.
	 */
    public void setParentCategory(MeasurementRoutineStepCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    /**
	 * @return Returns the childCategories.
	 */
    public List<MeasurementRoutineStepCategory> getChildCategories() {
        return childCategories;
    }

    /**
	 * @return Returns the categoryID.
	 */
    public String getCategoryID() {
        return categoryID;
    }

    /**
	 * @return Returns the categoryDescription.
	 */
    public String getCategoryDescription() {
        return categoryDescription;
    }

    /**
	 * @param categoryDescription The categoryDescription to set.
	 */
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    /**
	 * @param icon The icon to set.
	 */
    public void setIcon(Image icon) {
        this.icon = icon;
    }

    /**
	 * @return Returns the icon.
	 */
    public Image getIcon() {
        return icon;
    }

    /**
	 * @return Returns the stepFactories.
	 */
    public List<MeasurementRoutineStepFactory> getStepFactories() {
        return stepFactories;
    }

    public void addStepFactory(MeasurementRoutineStepFactory factory) {
        stepFactories.add(factory);
    }
}
