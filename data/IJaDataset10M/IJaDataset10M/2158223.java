package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcTopologyRepresentation<br><br>
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
public class IfcTopologyRepresentation extends IfcShapeModel implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcRepresentationContext", "IfcLabel", "IfcLabel", "SET<IfcRepresentationItem>" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    /**
	 * The default constructor.
	 **/
    public IfcTopologyRepresentation() {
    }

    /**
	 * Constructs a new IfcTopologyRepresentation object using the given parameters.
	 **/
    public IfcTopologyRepresentation(IfcRepresentationContext ContextOfItems, IfcLabel RepresentationIdentifier, IfcLabel RepresentationType, SET<IfcRepresentationItem> Items) {
        this.ContextOfItems = ContextOfItems;
        this.RepresentationIdentifier = RepresentationIdentifier;
        this.RepresentationType = RepresentationType;
        this.Items = Items;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    @SuppressWarnings("unchecked")
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.ContextOfItems = (IfcRepresentationContext) parameters.get(0);
        this.RepresentationIdentifier = (IfcLabel) parameters.get(1);
        this.RepresentationType = (IfcLabel) parameters.get(2);
        this.Items = (SET<IfcRepresentationItem>) parameters.get(3);
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
        stepString = stepString.concat("IFCTOPOLOGYREPRESENTATION(");
        if (getRedefinedDerivedAttributeTypes().contains("ContextOfItems")) stepString = stepString.concat("*,"); else {
            if (this.ContextOfItems != null) stepString = stepString.concat(((IfcRootInterface) this.ContextOfItems).getStepParameter(IfcRepresentationContext.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("RepresentationIdentifier")) stepString = stepString.concat("*,"); else {
            if (this.RepresentationIdentifier != null) stepString = stepString.concat(((IfcRootInterface) this.RepresentationIdentifier).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("RepresentationType")) stepString = stepString.concat("*,"); else {
            if (this.RepresentationType != null) stepString = stepString.concat(((IfcRootInterface) this.RepresentationType).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Items")) stepString = stepString.concat("*);"); else {
            if (this.Items != null) stepString = stepString.concat(((IfcRootInterface) this.Items).getStepParameter(IfcRepresentationItem.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the ContextOfItems attribute to the given value.
	 * 
	 * @param ContextOfItems value to set
	 *            /
	 **/
    public void setContextOfItems(IfcRepresentationContext ContextOfItems) {
        this.ContextOfItems = ContextOfItems;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ContextOfItems attribute.
	 * 
	 * @return the value of ContextOfItems
	 *         /
	 **/
    public IfcRepresentationContext getContextOfItems() {
        return this.ContextOfItems;
    }

    /**
	 * This method sets the RepresentationIdentifier attribute to the given value.
	 * 
	 * @param RepresentationIdentifier value to set
	 *            /
	 **/
    public void setRepresentationIdentifier(IfcLabel RepresentationIdentifier) {
        this.RepresentationIdentifier = RepresentationIdentifier;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the RepresentationIdentifier attribute.
	 * 
	 * @return the value of RepresentationIdentifier
	 *         /
	 **/
    public IfcLabel getRepresentationIdentifier() {
        return this.RepresentationIdentifier;
    }

    /**
	 * This method sets the RepresentationType attribute to the given value.
	 * 
	 * @param RepresentationType value to set
	 *            /
	 **/
    public void setRepresentationType(IfcLabel RepresentationType) {
        this.RepresentationType = RepresentationType;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the RepresentationType attribute.
	 * 
	 * @return the value of RepresentationType
	 *         /
	 **/
    public IfcLabel getRepresentationType() {
        return this.RepresentationType;
    }

    /**
	 * This method sets the Items attribute to the given value.
	 * 
	 * @param Items value to set
	 *            /
	 **/
    public void setItems(SET<IfcRepresentationItem> Items) {
        this.Items = Items;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Items attribute.
	 * 
	 * @return the value of Items
	 *         /
	 **/
    public SET<IfcRepresentationItem> getItems() {
        return this.Items;
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
        IfcTopologyRepresentation ifcTopologyRepresentation = new IfcTopologyRepresentation();
        if (this.ContextOfItems != null) ifcTopologyRepresentation.setContextOfItems((IfcRepresentationContext) this.ContextOfItems.clone());
        if (this.RepresentationIdentifier != null) ifcTopologyRepresentation.setRepresentationIdentifier((IfcLabel) this.RepresentationIdentifier.clone());
        if (this.RepresentationType != null) ifcTopologyRepresentation.setRepresentationType((IfcLabel) this.RepresentationType.clone());
        if (this.Items != null) ifcTopologyRepresentation.setItems((SET<IfcRepresentationItem>) this.Items.clone());
        return ifcTopologyRepresentation;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcTopologyRepresentation ifcTopologyRepresentation = new IfcTopologyRepresentation();
        if (this.ContextOfItems != null) ifcTopologyRepresentation.setContextOfItems(this.ContextOfItems);
        if (this.RepresentationIdentifier != null) ifcTopologyRepresentation.setRepresentationIdentifier(this.RepresentationIdentifier);
        if (this.RepresentationType != null) ifcTopologyRepresentation.setRepresentationType(this.RepresentationType);
        if (this.Items != null) ifcTopologyRepresentation.setItems(this.Items);
        return ifcTopologyRepresentation;
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
