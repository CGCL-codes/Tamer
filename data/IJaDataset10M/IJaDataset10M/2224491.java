package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcRoof<br><br>
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
public class IfcRoof extends IfcBuildingElement implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcGloballyUniqueId", "IfcOwnerHistory", "IfcLabel", "IfcText", "IfcLabel", "IfcObjectPlacement", "IfcProductRepresentation", "IfcIdentifier", "IfcRoofTypeEnum" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public IfcRoofTypeEnum ShapeType;

    /**
	 * The default constructor.
	 **/
    public IfcRoof() {
    }

    /**
	 * Constructs a new IfcRoof object using the given parameters.
	 **/
    public IfcRoof(IfcGloballyUniqueId GlobalId, IfcOwnerHistory OwnerHistory, IfcLabel Name, IfcText Description, IfcLabel ObjectType, IfcObjectPlacement ObjectPlacement, IfcProductRepresentation Representation, IfcIdentifier Tag, IfcRoofTypeEnum ShapeType) {
        this.GlobalId = GlobalId;
        this.OwnerHistory = OwnerHistory;
        this.Name = Name;
        this.Description = Description;
        this.ObjectType = ObjectType;
        this.ObjectPlacement = ObjectPlacement;
        this.Representation = Representation;
        this.Tag = Tag;
        this.ShapeType = ShapeType;
    }

    /**
	 * This method initializes the IfcRoof object using the given parameters.
	 **/
    public void setParameters(IfcGloballyUniqueId GlobalId, IfcOwnerHistory OwnerHistory, IfcLabel Name, IfcText Description, IfcLabel ObjectType, IfcObjectPlacement ObjectPlacement, IfcProductRepresentation Representation, IfcIdentifier Tag, IfcRoofTypeEnum ShapeType) {
        this.GlobalId = GlobalId;
        this.OwnerHistory = OwnerHistory;
        this.Name = Name;
        this.Description = Description;
        this.ObjectType = ObjectType;
        this.ObjectPlacement = ObjectPlacement;
        this.Representation = Representation;
        this.Tag = Tag;
        this.ShapeType = ShapeType;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.GlobalId = (IfcGloballyUniqueId) parameters.get(0);
        this.OwnerHistory = (IfcOwnerHistory) parameters.get(1);
        this.Name = (IfcLabel) parameters.get(2);
        this.Description = (IfcText) parameters.get(3);
        this.ObjectType = (IfcLabel) parameters.get(4);
        this.ObjectPlacement = (IfcObjectPlacement) parameters.get(5);
        this.Representation = (IfcProductRepresentation) parameters.get(6);
        this.Tag = (IfcIdentifier) parameters.get(7);
        this.ShapeType = (IfcRoofTypeEnum) parameters.get(8);
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
        stepString = stepString.concat("IFCROOF(");
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
        if (getRedefinedDerivedAttributeTypes().contains("ObjectPlacement")) stepString = stepString.concat("*,"); else {
            if (this.ObjectPlacement != null) stepString = stepString.concat(((IfcRootInterface) this.ObjectPlacement).getStepParameter(IfcObjectPlacement.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Representation")) stepString = stepString.concat("*,"); else {
            if (this.Representation != null) stepString = stepString.concat(((IfcRootInterface) this.Representation).getStepParameter(IfcProductRepresentation.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Tag")) stepString = stepString.concat("*,"); else {
            if (this.Tag != null) stepString = stepString.concat(((IfcRootInterface) this.Tag).getStepParameter(IfcIdentifier.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("ShapeType")) stepString = stepString.concat("*);"); else {
            if (this.ShapeType != null) stepString = stepString.concat(((IfcRootInterface) this.ShapeType).getStepParameter(IfcRoofTypeEnum.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the ObjectPlacement attribute to the given value.
	 * 
	 * @param ObjectPlacement value to set
	 *            /
	 **/
    public void setObjectPlacement(IfcObjectPlacement ObjectPlacement) {
        this.ObjectPlacement = ObjectPlacement;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ObjectPlacement attribute.
	 * 
	 * @return the value of ObjectPlacement
	 *         /
	 **/
    public IfcObjectPlacement getObjectPlacement() {
        return this.ObjectPlacement;
    }

    /**
	 * This method sets the Representation attribute to the given value.
	 * 
	 * @param Representation value to set
	 *            /
	 **/
    public void setRepresentation(IfcProductRepresentation Representation) {
        this.Representation = Representation;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Representation attribute.
	 * 
	 * @return the value of Representation
	 *         /
	 **/
    public IfcProductRepresentation getRepresentation() {
        return this.Representation;
    }

    /**
	 * This method sets the Tag attribute to the given value.
	 * 
	 * @param Tag value to set
	 *            /
	 **/
    public void setTag(IfcIdentifier Tag) {
        this.Tag = Tag;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Tag attribute.
	 * 
	 * @return the value of Tag
	 *         /
	 **/
    public IfcIdentifier getTag() {
        return this.Tag;
    }

    /**
	 * This method sets the ShapeType attribute to the given value.
	 * 
	 * @param ShapeType value to set
	 *            /
	 **/
    public void setShapeType(IfcRoofTypeEnum ShapeType) {
        this.ShapeType = ShapeType;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ShapeType attribute.
	 * 
	 * @return the value of ShapeType
	 *         /
	 **/
    public IfcRoofTypeEnum getShapeType() {
        return this.ShapeType;
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
        IfcRoof ifcRoof = new IfcRoof();
        if (this.GlobalId != null) ifcRoof.setGlobalId((IfcGloballyUniqueId) this.GlobalId.clone());
        if (this.OwnerHistory != null) ifcRoof.setOwnerHistory((IfcOwnerHistory) this.OwnerHistory.clone());
        if (this.Name != null) ifcRoof.setName((IfcLabel) this.Name.clone());
        if (this.Description != null) ifcRoof.setDescription((IfcText) this.Description.clone());
        if (this.ObjectType != null) ifcRoof.setObjectType((IfcLabel) this.ObjectType.clone());
        if (this.ObjectPlacement != null) ifcRoof.setObjectPlacement((IfcObjectPlacement) this.ObjectPlacement.clone());
        if (this.Representation != null) ifcRoof.setRepresentation((IfcProductRepresentation) this.Representation.clone());
        if (this.Tag != null) ifcRoof.setTag((IfcIdentifier) this.Tag.clone());
        if (this.ShapeType != null) ifcRoof.setShapeType((IfcRoofTypeEnum) this.ShapeType.clone());
        return ifcRoof;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcRoof ifcRoof = new IfcRoof();
        if (this.GlobalId != null) ifcRoof.setGlobalId(this.GlobalId);
        if (this.OwnerHistory != null) ifcRoof.setOwnerHistory(this.OwnerHistory);
        if (this.Name != null) ifcRoof.setName(this.Name);
        if (this.Description != null) ifcRoof.setDescription(this.Description);
        if (this.ObjectType != null) ifcRoof.setObjectType(this.ObjectType);
        if (this.ObjectPlacement != null) ifcRoof.setObjectPlacement(this.ObjectPlacement);
        if (this.Representation != null) ifcRoof.setRepresentation(this.Representation);
        if (this.Tag != null) ifcRoof.setTag(this.Tag);
        if (this.ShapeType != null) ifcRoof.setShapeType(this.ShapeType);
        return ifcRoof;
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
