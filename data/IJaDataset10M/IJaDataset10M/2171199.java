package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcAnnotationCurveOccurrence<br><br>
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
public class IfcAnnotationCurveOccurrence extends IfcAnnotationOccurrence implements IfcClass, IfcDraughtingCalloutElement {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcRepresentationItem", "SET<IfcPresentationStyleAssignment>", "IfcLabel" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    /**
	 * The default constructor.
	 **/
    public IfcAnnotationCurveOccurrence() {
    }

    /**
	 * Constructs a new IfcAnnotationCurveOccurrence object using the given parameters.
	 **/
    public IfcAnnotationCurveOccurrence(IfcRepresentationItem Item, SET<IfcPresentationStyleAssignment> Styles, IfcLabel Name) {
        this.Item = Item;
        this.Styles = Styles;
        this.Name = Name;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    @SuppressWarnings("unchecked")
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.Item = (IfcRepresentationItem) parameters.get(0);
        this.Styles = (SET<IfcPresentationStyleAssignment>) parameters.get(1);
        this.Name = (IfcLabel) parameters.get(2);
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
        stepString = stepString.concat("IFCANNOTATIONCURVEOCCURRENCE(");
        if (getRedefinedDerivedAttributeTypes().contains("Item")) stepString = stepString.concat("*,"); else {
            if (this.Item != null) stepString = stepString.concat(((IfcRootInterface) this.Item).getStepParameter(IfcRepresentationItem.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Styles")) stepString = stepString.concat("*,"); else {
            if (this.Styles != null) stepString = stepString.concat(((IfcRootInterface) this.Styles).getStepParameter(IfcPresentationStyleAssignment.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Name")) stepString = stepString.concat("*);"); else {
            if (this.Name != null) stepString = stepString.concat(((IfcRootInterface) this.Name).getStepParameter(IfcLabel.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the Item attribute to the given value.
	 * 
	 * @param Item value to set
	 *            /
	 **/
    public void setItem(IfcRepresentationItem Item) {
        this.Item = Item;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Item attribute.
	 * 
	 * @return the value of Item
	 *         /
	 **/
    public IfcRepresentationItem getItem() {
        return this.Item;
    }

    /**
	 * This method sets the Styles attribute to the given value.
	 * 
	 * @param Styles value to set
	 *            /
	 **/
    public void setStyles(SET<IfcPresentationStyleAssignment> Styles) {
        this.Styles = Styles;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Styles attribute.
	 * 
	 * @return the value of Styles
	 *         /
	 **/
    public SET<IfcPresentationStyleAssignment> getStyles() {
        return this.Styles;
    }

    /**
	 * This method sets the Name attribute to the given value.
	 * 
	 * @param Name value to set
	 *            /
	 **/
    public void setName(IfcLabel Name) {
        this.Name = Name;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Name attribute.
	 * 
	 * @return the value of Name
	 *         /
	 **/
    public IfcLabel getName() {
        return this.Name;
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
    @SuppressWarnings("unchecked")
    public Object clone() {
        IfcAnnotationCurveOccurrence ifcAnnotationCurveOccurrence = new IfcAnnotationCurveOccurrence();
        if (this.Item != null) ifcAnnotationCurveOccurrence.setItem((IfcRepresentationItem) this.Item.clone());
        if (this.Styles != null) ifcAnnotationCurveOccurrence.setStyles((SET<IfcPresentationStyleAssignment>) this.Styles.clone());
        if (this.Name != null) ifcAnnotationCurveOccurrence.setName((IfcLabel) this.Name.clone());
        return ifcAnnotationCurveOccurrence;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcAnnotationCurveOccurrence ifcAnnotationCurveOccurrence = new IfcAnnotationCurveOccurrence();
        if (this.Item != null) ifcAnnotationCurveOccurrence.setItem(this.Item);
        if (this.Styles != null) ifcAnnotationCurveOccurrence.setStyles(this.Styles);
        if (this.Name != null) ifcAnnotationCurveOccurrence.setName(this.Name);
        return ifcAnnotationCurveOccurrence;
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
