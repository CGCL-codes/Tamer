package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcProductDefinitionShape<br><br>
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
public class IfcProductDefinitionShape extends IfcProductRepresentation implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcLabel", "IfcText", "LIST<IfcRepresentation>" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public SET<IfcProduct> ShapeOfProduct_Inverse;

    public SET<IfcShapeAspect> HasShapeAspects_Inverse;

    /**
	 * The default constructor.
	 **/
    public IfcProductDefinitionShape() {
    }

    /**
	 * Constructs a new IfcProductDefinitionShape object using the given parameters.
	 **/
    public IfcProductDefinitionShape(IfcLabel Name, IfcText Description, LIST<IfcRepresentation> Representations) {
        this.Name = Name;
        this.Description = Description;
        this.Representations = Representations;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    @SuppressWarnings("unchecked")
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.Name = (IfcLabel) parameters.get(0);
        this.Description = (IfcText) parameters.get(1);
        this.Representations = (LIST<IfcRepresentation>) parameters.get(2);
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
        stepString = stepString.concat("IFCPRODUCTDEFINITIONSHAPE(");
        if (getRedefinedDerivedAttributeTypes().contains("Name")) stepString = stepString.concat("*,"); else {
            if (this.Name != null) stepString = stepString.concat(((IfcRootInterface) this.Name).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Description")) stepString = stepString.concat("*,"); else {
            if (this.Description != null) stepString = stepString.concat(((IfcRootInterface) this.Description).getStepParameter(IfcText.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Representations")) stepString = stepString.concat("*);"); else {
            if (this.Representations != null) stepString = stepString.concat(((IfcRootInterface) this.Representations).getStepParameter(IfcRepresentation.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the Representations attribute to the given value.
	 * 
	 * @param Representations value to set
	 *            /
	 **/
    public void setRepresentations(LIST<IfcRepresentation> Representations) {
        this.Representations = Representations;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Representations attribute.
	 * 
	 * @return the value of Representations
	 *         /
	 **/
    public LIST<IfcRepresentation> getRepresentations() {
        return this.Representations;
    }

    /**
	 * This method sets the ShapeOfProduct_Inverse attribute to the given value.
	 * 
	 * @param ShapeOfProduct_Inverse value to set
	 *            /
	 **/
    public void setShapeOfProduct_Inverse(SET<IfcProduct> ShapeOfProduct_Inverse) {
        this.ShapeOfProduct_Inverse = ShapeOfProduct_Inverse;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ShapeOfProduct_Inverse attribute.
	 * 
	 * @return the value of ShapeOfProduct_Inverse
	 *         /
	 **/
    public SET<IfcProduct> getShapeOfProduct_Inverse() {
        return this.ShapeOfProduct_Inverse;
    }

    /**
	 * This method sets the HasShapeAspects_Inverse attribute to the given value.
	 * 
	 * @param HasShapeAspects_Inverse value to set
	 *            /
	 **/
    public void setHasShapeAspects_Inverse(SET<IfcShapeAspect> HasShapeAspects_Inverse) {
        this.HasShapeAspects_Inverse = HasShapeAspects_Inverse;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the HasShapeAspects_Inverse attribute.
	 * 
	 * @return the value of HasShapeAspects_Inverse
	 *         /
	 **/
    public SET<IfcShapeAspect> getHasShapeAspects_Inverse() {
        return this.HasShapeAspects_Inverse;
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
        IfcProductDefinitionShape ifcProductDefinitionShape = new IfcProductDefinitionShape();
        if (this.Name != null) ifcProductDefinitionShape.setName((IfcLabel) this.Name.clone());
        if (this.Description != null) ifcProductDefinitionShape.setDescription((IfcText) this.Description.clone());
        if (this.Representations != null) ifcProductDefinitionShape.setRepresentations((LIST<IfcRepresentation>) this.Representations.clone());
        return ifcProductDefinitionShape;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcProductDefinitionShape ifcProductDefinitionShape = new IfcProductDefinitionShape();
        if (this.Name != null) ifcProductDefinitionShape.setName(this.Name);
        if (this.Description != null) ifcProductDefinitionShape.setDescription(this.Description);
        if (this.Representations != null) ifcProductDefinitionShape.setRepresentations(this.Representations);
        return ifcProductDefinitionShape;
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
