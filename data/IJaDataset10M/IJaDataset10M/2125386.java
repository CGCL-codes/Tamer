package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcElementComponentType<br><br>
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
public abstract class IfcElementComponentType extends IfcElementType implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcGloballyUniqueId", "IfcOwnerHistory", "IfcLabel", "IfcText", "IfcLabel", "SET<IfcPropertySetDefinition>", "LIST<IfcRepresentationMap>", "IfcLabel", "IfcLabel" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    /**
	 * The default constructor.
	 **/
    public IfcElementComponentType() {
    }

    /**
	 * Constructs a new IfcElementComponentType object using the given parameters.
	 **/
    public IfcElementComponentType(IfcGloballyUniqueId GlobalId, IfcOwnerHistory OwnerHistory, IfcLabel Name, IfcText Description, IfcLabel ApplicableOccurrence, SET<IfcPropertySetDefinition> HasPropertySets, LIST<IfcRepresentationMap> RepresentationMaps, IfcLabel Tag, IfcLabel ElementType) {
        this.GlobalId = GlobalId;
        this.OwnerHistory = OwnerHistory;
        this.Name = Name;
        this.Description = Description;
        this.ApplicableOccurrence = ApplicableOccurrence;
        this.HasPropertySets = HasPropertySets;
        this.RepresentationMaps = RepresentationMaps;
        this.Tag = Tag;
        this.ElementType = ElementType;
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
        this.ApplicableOccurrence = (IfcLabel) parameters.get(4);
        this.HasPropertySets = (SET<IfcPropertySetDefinition>) parameters.get(5);
        this.RepresentationMaps = (LIST<IfcRepresentationMap>) parameters.get(6);
        this.Tag = (IfcLabel) parameters.get(7);
        this.ElementType = (IfcLabel) parameters.get(8);
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
        stepString = stepString.concat("IFCELEMENTCOMPONENTTYPE(");
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
        if (getRedefinedDerivedAttributeTypes().contains("ApplicableOccurrence")) stepString = stepString.concat("*,"); else {
            if (this.ApplicableOccurrence != null) stepString = stepString.concat(((IfcRootInterface) this.ApplicableOccurrence).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("HasPropertySets")) stepString = stepString.concat("*,"); else {
            if (this.HasPropertySets != null) stepString = stepString.concat(((IfcRootInterface) this.HasPropertySets).getStepParameter(IfcPropertySetDefinition.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("RepresentationMaps")) stepString = stepString.concat("*,"); else {
            if (this.RepresentationMaps != null) stepString = stepString.concat(((IfcRootInterface) this.RepresentationMaps).getStepParameter(IfcRepresentationMap.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Tag")) stepString = stepString.concat("*,"); else {
            if (this.Tag != null) stepString = stepString.concat(((IfcRootInterface) this.Tag).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("ElementType")) stepString = stepString.concat("*);"); else {
            if (this.ElementType != null) stepString = stepString.concat(((IfcRootInterface) this.ElementType).getStepParameter(IfcLabel.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the ApplicableOccurrence attribute to the given value.
	 * 
	 * @param ApplicableOccurrence value to set
	 *            /
	 **/
    public void setApplicableOccurrence(IfcLabel ApplicableOccurrence) {
        this.ApplicableOccurrence = ApplicableOccurrence;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ApplicableOccurrence attribute.
	 * 
	 * @return the value of ApplicableOccurrence
	 *         /
	 **/
    public IfcLabel getApplicableOccurrence() {
        return this.ApplicableOccurrence;
    }

    /**
	 * This method sets the HasPropertySets attribute to the given value.
	 * 
	 * @param HasPropertySets value to set
	 *            /
	 **/
    public void setHasPropertySets(SET<IfcPropertySetDefinition> HasPropertySets) {
        this.HasPropertySets = HasPropertySets;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the HasPropertySets attribute.
	 * 
	 * @return the value of HasPropertySets
	 *         /
	 **/
    public SET<IfcPropertySetDefinition> getHasPropertySets() {
        return this.HasPropertySets;
    }

    /**
	 * This method sets the RepresentationMaps attribute to the given value.
	 * 
	 * @param RepresentationMaps value to set
	 *            /
	 **/
    public void setRepresentationMaps(LIST<IfcRepresentationMap> RepresentationMaps) {
        this.RepresentationMaps = RepresentationMaps;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the RepresentationMaps attribute.
	 * 
	 * @return the value of RepresentationMaps
	 *         /
	 **/
    public LIST<IfcRepresentationMap> getRepresentationMaps() {
        return this.RepresentationMaps;
    }

    /**
	 * This method sets the Tag attribute to the given value.
	 * 
	 * @param Tag value to set
	 *            /
	 **/
    public void setTag(IfcLabel Tag) {
        this.Tag = Tag;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Tag attribute.
	 * 
	 * @return the value of Tag
	 *         /
	 **/
    public IfcLabel getTag() {
        return this.Tag;
    }

    /**
	 * This method sets the ElementType attribute to the given value.
	 * 
	 * @param ElementType value to set
	 *            /
	 **/
    public void setElementType(IfcLabel ElementType) {
        this.ElementType = ElementType;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ElementType attribute.
	 * 
	 * @return the value of ElementType
	 *         /
	 **/
    public IfcLabel getElementType() {
        return this.ElementType;
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
    public abstract Object clone();

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public abstract Object shallowCopy();

    /**
	 * This method returns the objects standard description.
	 * 
	 * @return the standard description
	 **/
    public String toString() {
        return "#" + this.getStepLineNumber() + " " + this.getClass().getSimpleName();
    }
}
