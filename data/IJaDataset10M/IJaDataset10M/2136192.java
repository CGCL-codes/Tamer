package com.fddtool.ui.view.projecttype;

import java.io.Serializable;
import com.fddtool.pd.fddproject.MilestoneSetDescription;
import com.fddtool.pd.fddproject.ProjectTypeAspect;
import com.fddtool.util.Utils;

/**
 * This class represents a aspect of project type in the form that is suitable
 * for handling it in UI.
 * 
 * @author Serguei Khramtchenko
 */
public class UIProjectTypeAspect implements Serializable {

    /**
     * Initial serialization ID
     */
    private static final long serialVersionUID = -8951582301271701912L;

    /**
     * Name of project aspect.
     */
    private String name;

    /**
     * Indicates if the aspect is designated as development aspect.
     */
    private boolean development;

    /**
     * The error message that might be assiciated with a aspect.
     */
    private String error;

    /**
     * Weight of project aspect in the range of 0-100.
     */
    private int weight;

    /**
     * Identifier of milestone set that is associated with this project aspect.
     */
    private String milestoneSetId;

    /**
     * Creates a new, empty instance of this class. Use this constructor to
     * create a new aspect to be later inserted into persistent storage.
     */
    public UIProjectTypeAspect() {
    }

    /**
     * Creates a new instance and populates it using properties of the given
     * project aspect. Use this constructor to create a representation of
     * esxisting aspect.
     */
    public UIProjectTypeAspect(ProjectTypeAspect aspect, boolean typeReadOnly) {
        this.name = aspect.getName();
        this.development = aspect.isDevelopment();
        this.weight = aspect.getWeight();
        this.milestoneSetId = aspect.getMilestones() == null ? null : aspect.getMilestones().getId();
    }

    /**
     * Indicates if the project aspect is auto-calculated.
     * 
     * @return boolean value of <code>true</code> if the aspect is
     *         auto-calculated and <code>false</code> otherwise.
     */
    public boolean isDevelopment() {
        return development;
    }

    /**
     * Returns error message associated with this aspect.
     * 
     * @return String with error message or <code>null</code> if this aspect
     *         has no errors.
     */
    public String getError() {
        return error;
    }

    /**
     * Returns the name of project aspect.
     * 
     * @return String name of aspect. It may return <code>null</code> if user
     *         did not provide (erased) the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the indication if the project aspect is auto-calculated. JSF
     * framework calls this method when user submits the view.
     * 
     * @param development
     *            boolean value of <code>true</code> if the aspect should be
     *            auto-calculated and <code>false</code> otherwise.
     */
    public void setDevelopment(boolean autoCalculate) {
        this.development = autoCalculate;
    }

    /**
     * Associates an error message with the project aspect.
     * 
     * @param error
     *            String to be displayed in UI next to project aspect.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Sets the name of the project aspect. JSF framework calls this method when
     * user submits the view.
     * 
     * @param name
     *            String containg aspect name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Indicates if a user provided any data for the project aspect.
     * 
     * @return boolean value of <code>true</code> if either aouto-calculation
     *         mark is present or name is not empty.
     */
    public boolean isEmpty() {
        return Utils.isEmpty(getName()) && !isDevelopment() && getWeight() == 0;
    }

    /**
     * Returns weight of the aspect in the project.
     * 
     * @return the integer value in teh range 0-100.
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the weight for the aspect.
     * 
     * @param weight
     *            integer value of weight that should be in the range 0-100.
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Returns the milestone set associated with this project aspect.
     * 
     * @return MilestoneSetDescription object or <code>null</code> if no
     *         milestone sets are associated with this project aspect.
     */
    public MilestoneSetDescription getMilestoneSet() {
        if (!Utils.isEmpty(getMilestoneSetId())) {
            return MilestoneSetDescription.findById(getMilestoneSetId());
        } else {
            return null;
        }
    }

    /**
     * Returns identifier of feature milestone set that describes progress of
     * features associated with this project aspect.
     * 
     * @return String with milestone set identifier, or <code>null</code>.
     */
    public String getMilestoneSetId() {
        return milestoneSetId;
    }

    /**
     * Sets identifier of feature milestone set that will be used to describes
     * progress of features associated with this project aspect.
     * 
     * @param milestoneSetId
     *            String with milestone set identifier.
     */
    public void setMilestoneSetId(String milestoneSetId) {
        this.milestoneSetId = milestoneSetId;
    }
}
