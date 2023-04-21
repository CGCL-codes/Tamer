package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcGridAxis<br><br>
 * 
 * Copyright: CCPL BY-NC-SA 3.0 (cc) 2008 Eike Tauscher, Jan Tulke <br><br>
 * The OPEN IFC JAVA TOOLBOX package is licensed under <br> <a rel="license"
 * href="http://creativecommons.org/licenses/by-nc-sa/3.0/de/">Creative Commons Attribution-Non-Commercial-Share Alike
 * 3.0 Germany</a>.<br>
 * Please visit <a href="http://www.openifctools.com">http://www.openifctools.com</a> for more information.<br>
 * 
 * <br>
 * If you use this package, please send a message to one of the authors:<br>
 * <a href="mailto:eike.tauscher@openifctools.com">eike.tauscher@openifctools.com</a><br>
 * <a href="mailto:jan.tulke@openifctools.com">jan.tulke@openifctools.com</a><br>
 **/
public class IfcGridAxis implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcLabel", "IfcCurve", "IfcBoolean" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public IfcLabel AxisTag;

    public IfcCurve AxisCurve;

    public IfcBoolean SameSense;

    public SET<IfcGrid> PartOfW_Inverse;

    public SET<IfcGrid> PartOfV_Inverse;

    public SET<IfcGrid> PartOfU_Inverse;

    public SET<IfcVirtualGridIntersection> HasIntersections_Inverse;

    /**
	 * The default constructor.
	 **/
    public IfcGridAxis() {
    }

    /**
	 * Constructs a new IfcGridAxis object using the given parameters.
	 **/
    public IfcGridAxis(IfcLabel AxisTag, IfcCurve AxisCurve, IfcBoolean SameSense) {
        this.AxisTag = AxisTag;
        this.AxisCurve = AxisCurve;
        this.SameSense = SameSense;
    }

    /**
	 * This method initializes the IfcGridAxis object using the given parameters.
	 **/
    public void setParameters(IfcLabel AxisTag, IfcCurve AxisCurve, IfcBoolean SameSense) {
        this.AxisTag = AxisTag;
        this.AxisCurve = AxisCurve;
        this.SameSense = SameSense;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.AxisTag = (IfcLabel) parameters.get(0);
        this.AxisCurve = (IfcCurve) parameters.get(1);
        this.SameSense = (IfcBoolean) parameters.get(2);
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public String[] getNonInverseAttributeTypes() {
        return nonInverseAttributes;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public java.util.HashSet<String> getRedefinedDerivedAttributeTypes() {
        java.util.HashSet<String> redefinedDerivedAttributes = new java.util.HashSet<String>();
        return redefinedDerivedAttributes;
    }

    /**
	 * This method returns the object IFC STEP representation. This method is called by the IfcModel object to write IFC
	 * STEP files.
	 * 
	 * @return the IFC STEP representation of this object
	 **/
    public String getStepLine() {
        String stepString = new String("#" + this.stepLineNumber + "= ");
        stepString = stepString.concat("IFCGRIDAXIS(");
        if (getRedefinedDerivedAttributeTypes().contains("AxisTag")) stepString = stepString.concat("*,"); else {
            if (this.AxisTag != null) stepString = stepString.concat(((IfcRootInterface) this.AxisTag).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("AxisCurve")) stepString = stepString.concat("*,"); else {
            if (this.AxisCurve != null) stepString = stepString.concat(((IfcRootInterface) this.AxisCurve).getStepParameter(IfcCurve.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("SameSense")) stepString = stepString.concat("*);"); else {
            if (this.SameSense != null) stepString = stepString.concat(((IfcRootInterface) this.SameSense).getStepParameter(IfcBoolean.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
        }
        return stepString;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public String getStepParameter(boolean isSelectType) {
        return "#" + this.stepLineNumber;
    }

    /**
	 * This method returns the line number within a IFC STEP representation. This method is called from other objects,
	 * where this one is referenced.
	 * 
	 * @return the STEP line number
	 **/
    public int getStepLineNumber() {
        return this.stepLineNumber;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public void setStepLineNumber(int number) {
        this.stepLineNumber = number;
    }

    /**
	 * This method sets the AxisTag attribute to the given value.
	 * 
	 * @param AxisTag value to set
	 *            /
	 **/
    public void setAxisTag(IfcLabel AxisTag) {
        this.AxisTag = AxisTag;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the AxisTag attribute.
	 * 
	 * @return the value of AxisTag
	 *         /
	 **/
    public IfcLabel getAxisTag() {
        return this.AxisTag;
    }

    /**
	 * This method sets the AxisCurve attribute to the given value.
	 * 
	 * @param AxisCurve value to set
	 *            /
	 **/
    public void setAxisCurve(IfcCurve AxisCurve) {
        this.AxisCurve = AxisCurve;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the AxisCurve attribute.
	 * 
	 * @return the value of AxisCurve
	 *         /
	 **/
    public IfcCurve getAxisCurve() {
        return this.AxisCurve;
    }

    /**
	 * This method sets the SameSense attribute to the given value.
	 * 
	 * @param SameSense value to set
	 *            /
	 **/
    public void setSameSense(IfcBoolean SameSense) {
        this.SameSense = SameSense;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the SameSense attribute.
	 * 
	 * @return the value of SameSense
	 *         /
	 **/
    public IfcBoolean getSameSense() {
        return this.SameSense;
    }

    /**
	 * This method sets the PartOfW_Inverse attribute to the given value.
	 * 
	 * @param PartOfW_Inverse value to set
	 *            /
	 **/
    public void setPartOfW_Inverse(SET<IfcGrid> PartOfW_Inverse) {
        this.PartOfW_Inverse = PartOfW_Inverse;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the PartOfW_Inverse attribute.
	 * 
	 * @return the value of PartOfW_Inverse
	 *         /
	 **/
    public SET<IfcGrid> getPartOfW_Inverse() {
        return this.PartOfW_Inverse;
    }

    /**
	 * This method sets the PartOfV_Inverse attribute to the given value.
	 * 
	 * @param PartOfV_Inverse value to set
	 *            /
	 **/
    public void setPartOfV_Inverse(SET<IfcGrid> PartOfV_Inverse) {
        this.PartOfV_Inverse = PartOfV_Inverse;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the PartOfV_Inverse attribute.
	 * 
	 * @return the value of PartOfV_Inverse
	 *         /
	 **/
    public SET<IfcGrid> getPartOfV_Inverse() {
        return this.PartOfV_Inverse;
    }

    /**
	 * This method sets the PartOfU_Inverse attribute to the given value.
	 * 
	 * @param PartOfU_Inverse value to set
	 *            /
	 **/
    public void setPartOfU_Inverse(SET<IfcGrid> PartOfU_Inverse) {
        this.PartOfU_Inverse = PartOfU_Inverse;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the PartOfU_Inverse attribute.
	 * 
	 * @return the value of PartOfU_Inverse
	 *         /
	 **/
    public SET<IfcGrid> getPartOfU_Inverse() {
        return this.PartOfU_Inverse;
    }

    /**
	 * This method sets the HasIntersections_Inverse attribute to the given value.
	 * 
	 * @param HasIntersections_Inverse value to set
	 *            /
	 **/
    public void setHasIntersections_Inverse(SET<IfcVirtualGridIntersection> HasIntersections_Inverse) {
        this.HasIntersections_Inverse = HasIntersections_Inverse;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the HasIntersections_Inverse attribute.
	 * 
	 * @return the value of HasIntersections_Inverse
	 *         /
	 **/
    public SET<IfcVirtualGridIntersection> getHasIntersections_Inverse() {
        return this.HasIntersections_Inverse;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public void setStepParameter(java.util.ArrayList<CloneableObject> parameter) {
        this.stepParameter = parameter;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public java.util.ArrayList<CloneableObject> getStepParameter() {
        return this.stepParameter;
    }

    /**
	 * This method registers an IfcObjectChangeListener to this object. An event is fired whenever one of its values was
	 * changed.
	 * 
	 *@param listener the listener to register
	 **/
    public void addIfcObjectChangeListener(IfcObjectChangeListener listener) {
        if (listenerList == null) listenerList = new java.util.HashSet<IfcObjectChangeListener>(1, 1);
        listenerList.add(listener);
    }

    /**
	 * This method unregisters an IfcObjectChangeListener from this object.
	 * 
	 *@param listener the listener to unregister
	 **/
    public void removeIfcObjectChangeListener(IfcObjectChangeListener listener) {
        if (listenerList == null) return;
        listenerList.remove(listener);
    }

    /**
	 * This method removes all currently registered IfcObjectChangeListeners from this object.
	 **/
    public void removeAllIfcObjectChangeListeners() {
        listenerList = null;
    }

    protected void fireChangeEvent() {
        if (listenerList == null) return;
        for (IfcObjectChangeListener listener : listenerList) listener.ifcModelObjectChange(this);
    }

    /**
	 * This method can be used to map user objects to this object.
	 * 
	 *@param id the user objects key
	 * @param userObject the corresponding userObject
	 **/
    public void addUserObject(String id, Object userObject) throws Exception {
        if (userObjectMap == null) userObjectMap = new java.util.HashMap<String, Object>(2, 1);
        if (userObjectMap.containsKey(id)) throw new Exception("ID already exists for user object.");
        userObjectMap.put(id, userObject);
    }

    /**
	 * This method removes a user object from this object using the given key.
	 * 
	 *@param id the user objects key
	 **/
    public void removeUserObject(String id) {
        if (userObjectMap == null) return;
        userObjectMap.remove(id);
    }

    /**
	 * This method returns a user object for the given key.
	 * 
	 *@param id the user objects key
	 * @return the corresponding userObject, null if there is no userObject for the given key
	 **/
    public Object getUserObject(String id) {
        if (userObjectMap == null) return null;
        return userObjectMap.get(id);
    }

    /**
	 * This method clones the object (deep cloning).
	 * 
	 * @return the cloned object
	 **/
    public Object clone() {
        IfcGridAxis ifcGridAxis = new IfcGridAxis();
        if (this.AxisTag != null) ifcGridAxis.setAxisTag((IfcLabel) this.AxisTag.clone());
        if (this.AxisCurve != null) ifcGridAxis.setAxisCurve((IfcCurve) this.AxisCurve.clone());
        if (this.SameSense != null) ifcGridAxis.setSameSense((IfcBoolean) this.SameSense.clone());
        return ifcGridAxis;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcGridAxis ifcGridAxis = new IfcGridAxis();
        if (this.AxisTag != null) ifcGridAxis.setAxisTag(this.AxisTag);
        if (this.AxisCurve != null) ifcGridAxis.setAxisCurve(this.AxisCurve);
        if (this.SameSense != null) ifcGridAxis.setSameSense(this.SameSense);
        return ifcGridAxis;
    }

    /**
	 * This method returns the objects standard description.
	 * 
	 * @return the standard description
	 **/
    public String toString() {
        return "#" + this.getStepLineNumber() + " " + this.getClass().getSimpleName();
    }
}
