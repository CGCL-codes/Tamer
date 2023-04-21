package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcPhysicalComplexQuantity<br><br>
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
public class IfcPhysicalComplexQuantity extends IfcPhysicalQuantity implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcLabel", "IfcText", "SET<IfcPhysicalQuantity>", "IfcLabel", "IfcLabel", "IfcLabel" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public SET<IfcPhysicalQuantity> HasQuantities;

    public IfcLabel Discrimination;

    public IfcLabel Quality;

    public IfcLabel Usage;

    /**
	 * The default constructor.
	 **/
    public IfcPhysicalComplexQuantity() {
    }

    /**
	 * Constructs a new IfcPhysicalComplexQuantity object using the given parameters.
	 **/
    public IfcPhysicalComplexQuantity(IfcLabel Name, IfcText Description, SET<IfcPhysicalQuantity> HasQuantities, IfcLabel Discrimination, IfcLabel Quality, IfcLabel Usage) {
        this.Name = Name;
        this.Description = Description;
        this.HasQuantities = HasQuantities;
        this.Discrimination = Discrimination;
        this.Quality = Quality;
        this.Usage = Usage;
    }

    /**
	 * This method initializes the IfcPhysicalComplexQuantity object using the given parameters.
	 **/
    public void setParameters(IfcLabel Name, IfcText Description, SET<IfcPhysicalQuantity> HasQuantities, IfcLabel Discrimination, IfcLabel Quality, IfcLabel Usage) {
        this.Name = Name;
        this.Description = Description;
        this.HasQuantities = HasQuantities;
        this.Discrimination = Discrimination;
        this.Quality = Quality;
        this.Usage = Usage;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    @SuppressWarnings("unchecked")
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.Name = (IfcLabel) parameters.get(0);
        this.Description = (IfcText) parameters.get(1);
        this.HasQuantities = (SET<IfcPhysicalQuantity>) parameters.get(2);
        this.Discrimination = (IfcLabel) parameters.get(3);
        this.Quality = (IfcLabel) parameters.get(4);
        this.Usage = (IfcLabel) parameters.get(5);
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
        stepString = stepString.concat("IFCPHYSICALCOMPLEXQUANTITY(");
        if (getRedefinedDerivedAttributeTypes().contains("Name")) stepString = stepString.concat("*,"); else {
            if (this.Name != null) stepString = stepString.concat(((IfcRootInterface) this.Name).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Description")) stepString = stepString.concat("*,"); else {
            if (this.Description != null) stepString = stepString.concat(((IfcRootInterface) this.Description).getStepParameter(IfcText.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("HasQuantities")) stepString = stepString.concat("*,"); else {
            if (this.HasQuantities != null) stepString = stepString.concat(((IfcRootInterface) this.HasQuantities).getStepParameter(IfcPhysicalQuantity.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Discrimination")) stepString = stepString.concat("*,"); else {
            if (this.Discrimination != null) stepString = stepString.concat(((IfcRootInterface) this.Discrimination).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Quality")) stepString = stepString.concat("*,"); else {
            if (this.Quality != null) stepString = stepString.concat(((IfcRootInterface) this.Quality).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Usage")) stepString = stepString.concat("*);"); else {
            if (this.Usage != null) stepString = stepString.concat(((IfcRootInterface) this.Usage).getStepParameter(IfcLabel.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the HasQuantities attribute to the given value.
	 * 
	 * @param HasQuantities value to set
	 *            /
	 **/
    public void setHasQuantities(SET<IfcPhysicalQuantity> HasQuantities) {
        this.HasQuantities = HasQuantities;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the HasQuantities attribute.
	 * 
	 * @return the value of HasQuantities
	 *         /
	 **/
    public SET<IfcPhysicalQuantity> getHasQuantities() {
        return this.HasQuantities;
    }

    /**
	 * This method sets the Discrimination attribute to the given value.
	 * 
	 * @param Discrimination value to set
	 *            /
	 **/
    public void setDiscrimination(IfcLabel Discrimination) {
        this.Discrimination = Discrimination;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Discrimination attribute.
	 * 
	 * @return the value of Discrimination
	 *         /
	 **/
    public IfcLabel getDiscrimination() {
        return this.Discrimination;
    }

    /**
	 * This method sets the Quality attribute to the given value.
	 * 
	 * @param Quality value to set
	 *            /
	 **/
    public void setQuality(IfcLabel Quality) {
        this.Quality = Quality;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Quality attribute.
	 * 
	 * @return the value of Quality
	 *         /
	 **/
    public IfcLabel getQuality() {
        return this.Quality;
    }

    /**
	 * This method sets the Usage attribute to the given value.
	 * 
	 * @param Usage value to set
	 *            /
	 **/
    public void setUsage(IfcLabel Usage) {
        this.Usage = Usage;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Usage attribute.
	 * 
	 * @return the value of Usage
	 *         /
	 **/
    public IfcLabel getUsage() {
        return this.Usage;
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
        IfcPhysicalComplexQuantity ifcPhysicalComplexQuantity = new IfcPhysicalComplexQuantity();
        if (this.Name != null) ifcPhysicalComplexQuantity.setName((IfcLabel) this.Name.clone());
        if (this.Description != null) ifcPhysicalComplexQuantity.setDescription((IfcText) this.Description.clone());
        if (this.HasQuantities != null) ifcPhysicalComplexQuantity.setHasQuantities((SET<IfcPhysicalQuantity>) this.HasQuantities.clone());
        if (this.Discrimination != null) ifcPhysicalComplexQuantity.setDiscrimination((IfcLabel) this.Discrimination.clone());
        if (this.Quality != null) ifcPhysicalComplexQuantity.setQuality((IfcLabel) this.Quality.clone());
        if (this.Usage != null) ifcPhysicalComplexQuantity.setUsage((IfcLabel) this.Usage.clone());
        return ifcPhysicalComplexQuantity;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcPhysicalComplexQuantity ifcPhysicalComplexQuantity = new IfcPhysicalComplexQuantity();
        if (this.Name != null) ifcPhysicalComplexQuantity.setName(this.Name);
        if (this.Description != null) ifcPhysicalComplexQuantity.setDescription(this.Description);
        if (this.HasQuantities != null) ifcPhysicalComplexQuantity.setHasQuantities(this.HasQuantities);
        if (this.Discrimination != null) ifcPhysicalComplexQuantity.setDiscrimination(this.Discrimination);
        if (this.Quality != null) ifcPhysicalComplexQuantity.setQuality(this.Quality);
        if (this.Usage != null) ifcPhysicalComplexQuantity.setUsage(this.Usage);
        return ifcPhysicalComplexQuantity;
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
