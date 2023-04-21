package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcPropertyEnumeratedValue<br><br>
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
public class IfcPropertyEnumeratedValue extends IfcSimpleProperty implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcIdentifier", "IfcText", "LIST<IfcValue>", "IfcPropertyEnumeration" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public LIST<IfcValue> EnumerationValues;

    public IfcPropertyEnumeration EnumerationReference;

    /**
	 * The default constructor.
	 **/
    public IfcPropertyEnumeratedValue() {
    }

    /**
	 * Constructs a new IfcPropertyEnumeratedValue object using the given parameters.
	 **/
    public IfcPropertyEnumeratedValue(IfcIdentifier Name, IfcText Description, LIST<IfcValue> EnumerationValues, IfcPropertyEnumeration EnumerationReference) {
        this.Name = Name;
        this.Description = Description;
        this.EnumerationValues = EnumerationValues;
        this.EnumerationReference = EnumerationReference;
    }

    /**
	 * This method initializes the IfcPropertyEnumeratedValue object using the given parameters.
	 **/
    public void setParameters(IfcIdentifier Name, IfcText Description, LIST<IfcValue> EnumerationValues, IfcPropertyEnumeration EnumerationReference) {
        this.Name = Name;
        this.Description = Description;
        this.EnumerationValues = EnumerationValues;
        this.EnumerationReference = EnumerationReference;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    @SuppressWarnings("unchecked")
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.Name = (IfcIdentifier) parameters.get(0);
        this.Description = (IfcText) parameters.get(1);
        this.EnumerationValues = (LIST<IfcValue>) parameters.get(2);
        this.EnumerationReference = (IfcPropertyEnumeration) parameters.get(3);
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
        stepString = stepString.concat("IFCPROPERTYENUMERATEDVALUE(");
        if (getRedefinedDerivedAttributeTypes().contains("Name")) stepString = stepString.concat("*,"); else {
            if (this.Name != null) stepString = stepString.concat(((IfcRootInterface) this.Name).getStepParameter(IfcIdentifier.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Description")) stepString = stepString.concat("*,"); else {
            if (this.Description != null) stepString = stepString.concat(((IfcRootInterface) this.Description).getStepParameter(IfcText.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("EnumerationValues")) stepString = stepString.concat("*,"); else {
            if (this.EnumerationValues != null) stepString = stepString.concat(((IfcRootInterface) this.EnumerationValues).getStepParameter(IfcValue.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("EnumerationReference")) stepString = stepString.concat("*);"); else {
            if (this.EnumerationReference != null) stepString = stepString.concat(((IfcRootInterface) this.EnumerationReference).getStepParameter(IfcPropertyEnumeration.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the Name attribute to the given value.
	 * 
	 * @param Name value to set
	 *            /
	 **/
    public void setName(IfcIdentifier Name) {
        this.Name = Name;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Name attribute.
	 * 
	 * @return the value of Name
	 *         /
	 **/
    public IfcIdentifier getName() {
        return this.Name;
    }

    /**
	 * This method sets the Description attribute to the given value.
	 * 
	 * @param Description value to set
	 *            /
	 **/
    public void setDescription(IfcText Description) {
        this.Description = Description;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Description attribute.
	 * 
	 * @return the value of Description
	 *         /
	 **/
    public IfcText getDescription() {
        return this.Description;
    }

    /**
	 * This method sets the EnumerationValues attribute to the given value.
	 * 
	 * @param EnumerationValues value to set
	 *            /
	 **/
    public void setEnumerationValues(LIST<IfcValue> EnumerationValues) {
        this.EnumerationValues = EnumerationValues;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the EnumerationValues attribute.
	 * 
	 * @return the value of EnumerationValues
	 *         /
	 **/
    public LIST<IfcValue> getEnumerationValues() {
        return this.EnumerationValues;
    }

    /**
	 * This method sets the EnumerationReference attribute to the given value.
	 * 
	 * @param EnumerationReference value to set
	 *            /
	 **/
    public void setEnumerationReference(IfcPropertyEnumeration EnumerationReference) {
        this.EnumerationReference = EnumerationReference;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the EnumerationReference attribute.
	 * 
	 * @return the value of EnumerationReference
	 *         /
	 **/
    public IfcPropertyEnumeration getEnumerationReference() {
        return this.EnumerationReference;
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
        IfcPropertyEnumeratedValue ifcPropertyEnumeratedValue = new IfcPropertyEnumeratedValue();
        if (this.Name != null) ifcPropertyEnumeratedValue.setName((IfcIdentifier) this.Name.clone());
        if (this.Description != null) ifcPropertyEnumeratedValue.setDescription((IfcText) this.Description.clone());
        if (this.EnumerationValues != null) ifcPropertyEnumeratedValue.setEnumerationValues((LIST<IfcValue>) this.EnumerationValues.clone());
        if (this.EnumerationReference != null) ifcPropertyEnumeratedValue.setEnumerationReference((IfcPropertyEnumeration) this.EnumerationReference.clone());
        return ifcPropertyEnumeratedValue;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcPropertyEnumeratedValue ifcPropertyEnumeratedValue = new IfcPropertyEnumeratedValue();
        if (this.Name != null) ifcPropertyEnumeratedValue.setName(this.Name);
        if (this.Description != null) ifcPropertyEnumeratedValue.setDescription(this.Description);
        if (this.EnumerationValues != null) ifcPropertyEnumeratedValue.setEnumerationValues(this.EnumerationValues);
        if (this.EnumerationReference != null) ifcPropertyEnumeratedValue.setEnumerationReference(this.EnumerationReference);
        return ifcPropertyEnumeratedValue;
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
