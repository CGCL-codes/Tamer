package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcProject<br><br>
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
public class IfcProject extends IfcObject implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcGloballyUniqueId", "IfcOwnerHistory", "IfcLabel", "IfcText", "IfcLabel", "IfcLabel", "IfcLabel", "SET<IfcRepresentationContext>", "IfcUnitAssignment" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public IfcLabel LongName;

    public IfcLabel Phase;

    public SET<IfcRepresentationContext> RepresentationContexts;

    public IfcUnitAssignment UnitsInContext;

    /**
	 * The default constructor.
	 **/
    public IfcProject() {
    }

    /**
	 * Constructs a new IfcProject object using the given parameters.
	 **/
    public IfcProject(IfcGloballyUniqueId GlobalId, IfcOwnerHistory OwnerHistory, IfcLabel Name, IfcText Description, IfcLabel ObjectType, IfcLabel LongName, IfcLabel Phase, SET<IfcRepresentationContext> RepresentationContexts, IfcUnitAssignment UnitsInContext) {
        this.GlobalId = GlobalId;
        this.OwnerHistory = OwnerHistory;
        this.Name = Name;
        this.Description = Description;
        this.ObjectType = ObjectType;
        this.LongName = LongName;
        this.Phase = Phase;
        this.RepresentationContexts = RepresentationContexts;
        this.UnitsInContext = UnitsInContext;
    }

    /**
	 * This method initializes the IfcProject object using the given parameters.
	 **/
    public void setParameters(IfcGloballyUniqueId GlobalId, IfcOwnerHistory OwnerHistory, IfcLabel Name, IfcText Description, IfcLabel ObjectType, IfcLabel LongName, IfcLabel Phase, SET<IfcRepresentationContext> RepresentationContexts, IfcUnitAssignment UnitsInContext) {
        this.GlobalId = GlobalId;
        this.OwnerHistory = OwnerHistory;
        this.Name = Name;
        this.Description = Description;
        this.ObjectType = ObjectType;
        this.LongName = LongName;
        this.Phase = Phase;
        this.RepresentationContexts = RepresentationContexts;
        this.UnitsInContext = UnitsInContext;
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
        this.ObjectType = (IfcLabel) parameters.get(4);
        this.LongName = (IfcLabel) parameters.get(5);
        this.Phase = (IfcLabel) parameters.get(6);
        this.RepresentationContexts = (SET<IfcRepresentationContext>) parameters.get(7);
        this.UnitsInContext = (IfcUnitAssignment) parameters.get(8);
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
        stepString = stepString.concat("IFCPROJECT(");
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
        if (getRedefinedDerivedAttributeTypes().contains("ObjectType")) stepString = stepString.concat("*,"); else {
            if (this.ObjectType != null) stepString = stepString.concat(((IfcRootInterface) this.ObjectType).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("LongName")) stepString = stepString.concat("*,"); else {
            if (this.LongName != null) stepString = stepString.concat(((IfcRootInterface) this.LongName).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Phase")) stepString = stepString.concat("*,"); else {
            if (this.Phase != null) stepString = stepString.concat(((IfcRootInterface) this.Phase).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("RepresentationContexts")) stepString = stepString.concat("*,"); else {
            if (this.RepresentationContexts != null) stepString = stepString.concat(((IfcRootInterface) this.RepresentationContexts).getStepParameter(IfcRepresentationContext.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("UnitsInContext")) stepString = stepString.concat("*);"); else {
            if (this.UnitsInContext != null) stepString = stepString.concat(((IfcRootInterface) this.UnitsInContext).getStepParameter(IfcUnitAssignment.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the ObjectType attribute to the given value.
	 * 
	 * @param ObjectType value to set
	 *            /
	 **/
    public void setObjectType(IfcLabel ObjectType) {
        this.ObjectType = ObjectType;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ObjectType attribute.
	 * 
	 * @return the value of ObjectType
	 *         /
	 **/
    public IfcLabel getObjectType() {
        return this.ObjectType;
    }

    /**
	 * This method sets the LongName attribute to the given value.
	 * 
	 * @param LongName value to set
	 *            /
	 **/
    public void setLongName(IfcLabel LongName) {
        this.LongName = LongName;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the LongName attribute.
	 * 
	 * @return the value of LongName
	 *         /
	 **/
    public IfcLabel getLongName() {
        return this.LongName;
    }

    /**
	 * This method sets the Phase attribute to the given value.
	 * 
	 * @param Phase value to set
	 *            /
	 **/
    public void setPhase(IfcLabel Phase) {
        this.Phase = Phase;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Phase attribute.
	 * 
	 * @return the value of Phase
	 *         /
	 **/
    public IfcLabel getPhase() {
        return this.Phase;
    }

    /**
	 * This method sets the RepresentationContexts attribute to the given value.
	 * 
	 * @param RepresentationContexts value to set
	 *            /
	 **/
    public void setRepresentationContexts(SET<IfcRepresentationContext> RepresentationContexts) {
        this.RepresentationContexts = RepresentationContexts;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the RepresentationContexts attribute.
	 * 
	 * @return the value of RepresentationContexts
	 *         /
	 **/
    public SET<IfcRepresentationContext> getRepresentationContexts() {
        return this.RepresentationContexts;
    }

    /**
	 * This method sets the UnitsInContext attribute to the given value.
	 * 
	 * @param UnitsInContext value to set
	 *            /
	 **/
    public void setUnitsInContext(IfcUnitAssignment UnitsInContext) {
        this.UnitsInContext = UnitsInContext;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the UnitsInContext attribute.
	 * 
	 * @return the value of UnitsInContext
	 *         /
	 **/
    public IfcUnitAssignment getUnitsInContext() {
        return this.UnitsInContext;
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
        IfcProject ifcProject = new IfcProject();
        if (this.GlobalId != null) ifcProject.setGlobalId((IfcGloballyUniqueId) this.GlobalId.clone());
        if (this.OwnerHistory != null) ifcProject.setOwnerHistory((IfcOwnerHistory) this.OwnerHistory.clone());
        if (this.Name != null) ifcProject.setName((IfcLabel) this.Name.clone());
        if (this.Description != null) ifcProject.setDescription((IfcText) this.Description.clone());
        if (this.ObjectType != null) ifcProject.setObjectType((IfcLabel) this.ObjectType.clone());
        if (this.LongName != null) ifcProject.setLongName((IfcLabel) this.LongName.clone());
        if (this.Phase != null) ifcProject.setPhase((IfcLabel) this.Phase.clone());
        if (this.RepresentationContexts != null) ifcProject.setRepresentationContexts((SET<IfcRepresentationContext>) this.RepresentationContexts.clone());
        if (this.UnitsInContext != null) ifcProject.setUnitsInContext((IfcUnitAssignment) this.UnitsInContext.clone());
        return ifcProject;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcProject ifcProject = new IfcProject();
        if (this.GlobalId != null) ifcProject.setGlobalId(this.GlobalId);
        if (this.OwnerHistory != null) ifcProject.setOwnerHistory(this.OwnerHistory);
        if (this.Name != null) ifcProject.setName(this.Name);
        if (this.Description != null) ifcProject.setDescription(this.Description);
        if (this.ObjectType != null) ifcProject.setObjectType(this.ObjectType);
        if (this.LongName != null) ifcProject.setLongName(this.LongName);
        if (this.Phase != null) ifcProject.setPhase(this.Phase);
        if (this.RepresentationContexts != null) ifcProject.setRepresentationContexts(this.RepresentationContexts);
        if (this.UnitsInContext != null) ifcProject.setUnitsInContext(this.UnitsInContext);
        return ifcProject;
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
