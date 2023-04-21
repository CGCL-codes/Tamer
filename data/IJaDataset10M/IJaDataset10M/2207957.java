package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcTextStyleFontModel<br><br>
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
public class IfcTextStyleFontModel extends IfcPreDefinedTextFont implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcLabel", "LIST<IfcTextFontName>", "IfcFontStyle", "IfcFontVariant", "IfcFontWeight", "IfcSizeSelect" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public LIST<IfcTextFontName> FontFamily;

    public IfcFontStyle FontStyle;

    public IfcFontVariant FontVariant;

    public IfcFontWeight FontWeight;

    public IfcSizeSelect FontSize;

    /**
	 * The default constructor.
	 **/
    public IfcTextStyleFontModel() {
    }

    /**
	 * Constructs a new IfcTextStyleFontModel object using the given parameters.
	 **/
    public IfcTextStyleFontModel(IfcLabel Name, LIST<IfcTextFontName> FontFamily, IfcFontStyle FontStyle, IfcFontVariant FontVariant, IfcFontWeight FontWeight, IfcSizeSelect FontSize) {
        this.Name = Name;
        this.FontFamily = FontFamily;
        this.FontStyle = FontStyle;
        this.FontVariant = FontVariant;
        this.FontWeight = FontWeight;
        this.FontSize = FontSize;
    }

    /**
	 * This method initializes the IfcTextStyleFontModel object using the given parameters.
	 **/
    public void setParameters(IfcLabel Name, LIST<IfcTextFontName> FontFamily, IfcFontStyle FontStyle, IfcFontVariant FontVariant, IfcFontWeight FontWeight, IfcSizeSelect FontSize) {
        this.Name = Name;
        this.FontFamily = FontFamily;
        this.FontStyle = FontStyle;
        this.FontVariant = FontVariant;
        this.FontWeight = FontWeight;
        this.FontSize = FontSize;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    @SuppressWarnings("unchecked")
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.Name = (IfcLabel) parameters.get(0);
        this.FontFamily = (LIST<IfcTextFontName>) parameters.get(1);
        this.FontStyle = (IfcFontStyle) parameters.get(2);
        this.FontVariant = (IfcFontVariant) parameters.get(3);
        this.FontWeight = (IfcFontWeight) parameters.get(4);
        this.FontSize = (IfcSizeSelect) parameters.get(5);
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
        stepString = stepString.concat("IFCTEXTSTYLEFONTMODEL(");
        if (getRedefinedDerivedAttributeTypes().contains("Name")) stepString = stepString.concat("*,"); else {
            if (this.Name != null) stepString = stepString.concat(((IfcRootInterface) this.Name).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("FontFamily")) stepString = stepString.concat("*,"); else {
            if (this.FontFamily != null) stepString = stepString.concat(((IfcRootInterface) this.FontFamily).getStepParameter(IfcTextFontName.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("FontStyle")) stepString = stepString.concat("*,"); else {
            if (this.FontStyle != null) stepString = stepString.concat(((IfcRootInterface) this.FontStyle).getStepParameter(IfcFontStyle.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("FontVariant")) stepString = stepString.concat("*,"); else {
            if (this.FontVariant != null) stepString = stepString.concat(((IfcRootInterface) this.FontVariant).getStepParameter(IfcFontVariant.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("FontWeight")) stepString = stepString.concat("*,"); else {
            if (this.FontWeight != null) stepString = stepString.concat(((IfcRootInterface) this.FontWeight).getStepParameter(IfcFontWeight.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("FontSize")) stepString = stepString.concat("*);"); else {
            if (this.FontSize != null) stepString = stepString.concat(((IfcRootInterface) this.FontSize).getStepParameter(IfcSizeSelect.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the FontFamily attribute to the given value.
	 * 
	 * @param FontFamily value to set
	 *            /
	 **/
    public void setFontFamily(LIST<IfcTextFontName> FontFamily) {
        this.FontFamily = FontFamily;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the FontFamily attribute.
	 * 
	 * @return the value of FontFamily
	 *         /
	 **/
    public LIST<IfcTextFontName> getFontFamily() {
        return this.FontFamily;
    }

    /**
	 * This method sets the FontStyle attribute to the given value.
	 * 
	 * @param FontStyle value to set
	 *            /
	 **/
    public void setFontStyle(IfcFontStyle FontStyle) {
        this.FontStyle = FontStyle;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the FontStyle attribute.
	 * 
	 * @return the value of FontStyle
	 *         /
	 **/
    public IfcFontStyle getFontStyle() {
        return this.FontStyle;
    }

    /**
	 * This method sets the FontVariant attribute to the given value.
	 * 
	 * @param FontVariant value to set
	 *            /
	 **/
    public void setFontVariant(IfcFontVariant FontVariant) {
        this.FontVariant = FontVariant;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the FontVariant attribute.
	 * 
	 * @return the value of FontVariant
	 *         /
	 **/
    public IfcFontVariant getFontVariant() {
        return this.FontVariant;
    }

    /**
	 * This method sets the FontWeight attribute to the given value.
	 * 
	 * @param FontWeight value to set
	 *            /
	 **/
    public void setFontWeight(IfcFontWeight FontWeight) {
        this.FontWeight = FontWeight;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the FontWeight attribute.
	 * 
	 * @return the value of FontWeight
	 *         /
	 **/
    public IfcFontWeight getFontWeight() {
        return this.FontWeight;
    }

    /**
	 * This method sets the FontSize attribute to the given value.
	 * 
	 * @param FontSize value to set
	 *            /
	 **/
    public void setFontSize(IfcSizeSelect FontSize) {
        this.FontSize = FontSize;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the FontSize attribute.
	 * 
	 * @return the value of FontSize
	 *         /
	 **/
    public IfcSizeSelect getFontSize() {
        return this.FontSize;
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
        IfcTextStyleFontModel ifcTextStyleFontModel = new IfcTextStyleFontModel();
        if (this.Name != null) ifcTextStyleFontModel.setName((IfcLabel) this.Name.clone());
        if (this.FontFamily != null) ifcTextStyleFontModel.setFontFamily((LIST<IfcTextFontName>) this.FontFamily.clone());
        if (this.FontStyle != null) ifcTextStyleFontModel.setFontStyle((IfcFontStyle) this.FontStyle.clone());
        if (this.FontVariant != null) ifcTextStyleFontModel.setFontVariant((IfcFontVariant) this.FontVariant.clone());
        if (this.FontWeight != null) ifcTextStyleFontModel.setFontWeight((IfcFontWeight) this.FontWeight.clone());
        if (this.FontSize != null) ifcTextStyleFontModel.setFontSize((IfcSizeSelect) this.FontSize.clone());
        return ifcTextStyleFontModel;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcTextStyleFontModel ifcTextStyleFontModel = new IfcTextStyleFontModel();
        if (this.Name != null) ifcTextStyleFontModel.setName(this.Name);
        if (this.FontFamily != null) ifcTextStyleFontModel.setFontFamily(this.FontFamily);
        if (this.FontStyle != null) ifcTextStyleFontModel.setFontStyle(this.FontStyle);
        if (this.FontVariant != null) ifcTextStyleFontModel.setFontVariant(this.FontVariant);
        if (this.FontWeight != null) ifcTextStyleFontModel.setFontWeight(this.FontWeight);
        if (this.FontSize != null) ifcTextStyleFontModel.setFontSize(this.FontSize);
        return ifcTextStyleFontModel;
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
