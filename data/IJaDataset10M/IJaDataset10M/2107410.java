package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcRelAssignsToProcess<br><br>
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
public class IfcRelAssignsToProcess extends IfcRelAssigns implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcGloballyUniqueId", "IfcOwnerHistory", "IfcLabel", "IfcText", "SET<IfcObjectDefinition>", "IfcObjectTypeEnum", "IfcProcess", "IfcMeasureWithUnit" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public IfcProcess RelatingProcess;

    public IfcMeasureWithUnit QuantityInProcess;

    /**
	 * The default constructor.
	 **/
    public IfcRelAssignsToProcess() {
    }

    /**
	 * Constructs a new IfcRelAssignsToProcess object using the given parameters.
	 **/
    public IfcRelAssignsToProcess(IfcGloballyUniqueId GlobalId, IfcOwnerHistory OwnerHistory, IfcLabel Name, IfcText Description, SET<IfcObjectDefinition> RelatedObjects, IfcObjectTypeEnum RelatedObjectsType, IfcProcess RelatingProcess, IfcMeasureWithUnit QuantityInProcess) {
        this.GlobalId = GlobalId;
        this.OwnerHistory = OwnerHistory;
        this.Name = Name;
        this.Description = Description;
        this.RelatedObjects = RelatedObjects;
        this.RelatedObjectsType = RelatedObjectsType;
        this.RelatingProcess = RelatingProcess;
        this.QuantityInProcess = QuantityInProcess;
    }

    /**
	 * This method initializes the IfcRelAssignsToProcess object using the given parameters.
	 **/
    public void setParameters(IfcGloballyUniqueId GlobalId, IfcOwnerHistory OwnerHistory, IfcLabel Name, IfcText Description, SET<IfcObjectDefinition> RelatedObjects, IfcObjectTypeEnum RelatedObjectsType, IfcProcess RelatingProcess, IfcMeasureWithUnit QuantityInProcess) {
        this.GlobalId = GlobalId;
        this.OwnerHistory = OwnerHistory;
        this.Name = Name;
        this.Description = Description;
        this.RelatedObjects = RelatedObjects;
        this.RelatedObjectsType = RelatedObjectsType;
        this.RelatingProcess = RelatingProcess;
        this.QuantityInProcess = QuantityInProcess;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    @SuppressWarnings("unchecked")
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.GlobalId = (IfcGloballyUniqueId) parameters.get(0);
        this.OwnerHistory = (IfcOwnerHistory) parameters.get(1);
        this.Name = (IfcLabel) parameters.get(2);
        this.Description = (IfcText) parameters.get(3);
        this.RelatedObjects = (SET<IfcObjectDefinition>) parameters.get(4);
        this.RelatedObjectsType = (IfcObjectTypeEnum) parameters.get(5);
        this.RelatingProcess = (IfcProcess) parameters.get(6);
        this.QuantityInProcess = (IfcMeasureWithUnit) parameters.get(7);
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
        stepString = stepString.concat("IFCRELASSIGNSTOPROCESS(");
        if (getRedefinedDerivedAttributeTypes().contains("GlobalId")) stepString = stepString.concat("*,"); else {
            if (this.GlobalId != null) stepString = stepString.concat(((IfcRootInterface) this.GlobalId).getStepParameter(IfcGloballyUniqueId.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("OwnerHistory")) stepString = stepString.concat("*,"); else {
            if (this.OwnerHistory != null) stepString = stepString.concat(((IfcRootInterface) this.OwnerHistory).getStepParameter(IfcOwnerHistory.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Name")) stepString = stepString.concat("*,"); else {
            if (this.Name != null) stepString = stepString.concat(((IfcRootInterface) this.Name).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Description")) stepString = stepString.concat("*,"); else {
            if (this.Description != null) stepString = stepString.concat(((IfcRootInterface) this.Description).getStepParameter(IfcText.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("RelatedObjects")) stepString = stepString.concat("*,"); else {
            if (this.RelatedObjects != null) stepString = stepString.concat(((IfcRootInterface) this.RelatedObjects).getStepParameter(IfcObjectDefinition.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("RelatedObjectsType")) stepString = stepString.concat("*,"); else {
            if (this.RelatedObjectsType != null) stepString = stepString.concat(((IfcRootInterface) this.RelatedObjectsType).getStepParameter(IfcObjectTypeEnum.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("RelatingProcess")) stepString = stepString.concat("*,"); else {
            if (this.RelatingProcess != null) stepString = stepString.concat(((IfcRootInterface) this.RelatingProcess).getStepParameter(IfcProcess.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("QuantityInProcess")) stepString = stepString.concat("*);"); else {
            if (this.QuantityInProcess != null) stepString = stepString.concat(((IfcRootInterface) this.QuantityInProcess).getStepParameter(IfcMeasureWithUnit.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the GlobalId attribute to the given value.
	 * 
	 * @param GlobalId value to set
	 *            /
	 **/
    public void setGlobalId(IfcGloballyUniqueId GlobalId) {
        this.GlobalId = GlobalId;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the GlobalId attribute.
	 * 
	 * @return the value of GlobalId
	 *         /
	 **/
    public IfcGloballyUniqueId getGlobalId() {
        return this.GlobalId;
    }

    /**
	 * This method sets the OwnerHistory attribute to the given value.
	 * 
	 * @param OwnerHistory value to set
	 *            /
	 **/
    public void setOwnerHistory(IfcOwnerHistory OwnerHistory) {
        this.OwnerHistory = OwnerHistory;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the OwnerHistory attribute.
	 * 
	 * @return the value of OwnerHistory
	 *         /
	 **/
    public IfcOwnerHistory getOwnerHistory() {
        return this.OwnerHistory;
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
	 * This method sets the RelatedObjects attribute to the given value.
	 * 
	 * @param RelatedObjects value to set
	 *            /
	 **/
    public void setRelatedObjects(SET<IfcObjectDefinition> RelatedObjects) {
        this.RelatedObjects = RelatedObjects;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the RelatedObjects attribute.
	 * 
	 * @return the value of RelatedObjects
	 *         /
	 **/
    public SET<IfcObjectDefinition> getRelatedObjects() {
        return this.RelatedObjects;
    }

    /**
	 * This method sets the RelatedObjectsType attribute to the given value.
	 * 
	 * @param RelatedObjectsType value to set
	 *            /
	 **/
    public void setRelatedObjectsType(IfcObjectTypeEnum RelatedObjectsType) {
        this.RelatedObjectsType = RelatedObjectsType;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the RelatedObjectsType attribute.
	 * 
	 * @return the value of RelatedObjectsType
	 *         /
	 **/
    public IfcObjectTypeEnum getRelatedObjectsType() {
        return this.RelatedObjectsType;
    }

    /**
	 * This method sets the RelatingProcess attribute to the given value.
	 * 
	 * @param RelatingProcess value to set
	 *            /
	 **/
    public void setRelatingProcess(IfcProcess RelatingProcess) {
        this.RelatingProcess = RelatingProcess;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the RelatingProcess attribute.
	 * 
	 * @return the value of RelatingProcess
	 *         /
	 **/
    public IfcProcess getRelatingProcess() {
        return this.RelatingProcess;
    }

    /**
	 * This method sets the QuantityInProcess attribute to the given value.
	 * 
	 * @param QuantityInProcess value to set
	 *            /
	 **/
    public void setQuantityInProcess(IfcMeasureWithUnit QuantityInProcess) {
        this.QuantityInProcess = QuantityInProcess;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the QuantityInProcess attribute.
	 * 
	 * @return the value of QuantityInProcess
	 *         /
	 **/
    public IfcMeasureWithUnit getQuantityInProcess() {
        return this.QuantityInProcess;
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
        IfcRelAssignsToProcess ifcRelAssignsToProcess = new IfcRelAssignsToProcess();
        if (this.GlobalId != null) ifcRelAssignsToProcess.setGlobalId((IfcGloballyUniqueId) this.GlobalId.clone());
        if (this.OwnerHistory != null) ifcRelAssignsToProcess.setOwnerHistory((IfcOwnerHistory) this.OwnerHistory.clone());
        if (this.Name != null) ifcRelAssignsToProcess.setName((IfcLabel) this.Name.clone());
        if (this.Description != null) ifcRelAssignsToProcess.setDescription((IfcText) this.Description.clone());
        if (this.RelatedObjects != null) ifcRelAssignsToProcess.setRelatedObjects((SET<IfcObjectDefinition>) this.RelatedObjects.clone());
        if (this.RelatedObjectsType != null) ifcRelAssignsToProcess.setRelatedObjectsType((IfcObjectTypeEnum) this.RelatedObjectsType.clone());
        if (this.RelatingProcess != null) ifcRelAssignsToProcess.setRelatingProcess((IfcProcess) this.RelatingProcess.clone());
        if (this.QuantityInProcess != null) ifcRelAssignsToProcess.setQuantityInProcess((IfcMeasureWithUnit) this.QuantityInProcess.clone());
        return ifcRelAssignsToProcess;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcRelAssignsToProcess ifcRelAssignsToProcess = new IfcRelAssignsToProcess();
        if (this.GlobalId != null) ifcRelAssignsToProcess.setGlobalId(this.GlobalId);
        if (this.OwnerHistory != null) ifcRelAssignsToProcess.setOwnerHistory(this.OwnerHistory);
        if (this.Name != null) ifcRelAssignsToProcess.setName(this.Name);
        if (this.Description != null) ifcRelAssignsToProcess.setDescription(this.Description);
        if (this.RelatedObjects != null) ifcRelAssignsToProcess.setRelatedObjects(this.RelatedObjects);
        if (this.RelatedObjectsType != null) ifcRelAssignsToProcess.setRelatedObjectsType(this.RelatedObjectsType);
        if (this.RelatingProcess != null) ifcRelAssignsToProcess.setRelatingProcess(this.RelatingProcess);
        if (this.QuantityInProcess != null) ifcRelAssignsToProcess.setQuantityInProcess(this.QuantityInProcess);
        return ifcRelAssignsToProcess;
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
