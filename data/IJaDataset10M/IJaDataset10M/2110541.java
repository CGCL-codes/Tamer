package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcTextStyleWithBoxCharacteristics<br><br>
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
public class IfcTextStyleWithBoxCharacteristics implements IfcTextStyleSelect, IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcPositiveLengthMeasure", "IfcPositiveLengthMeasure", "IfcPlaneAngleMeasure", "IfcPlaneAngleMeasure", "IfcSizeSelect" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public IfcPositiveLengthMeasure BoxHeight;

    public IfcPositiveLengthMeasure BoxWidth;

    public IfcPlaneAngleMeasure BoxSlantAngle;

    public IfcPlaneAngleMeasure BoxRotateAngle;

    public IfcSizeSelect CharacterSpacing;

    /**
	 * The default constructor.
	 **/
    public IfcTextStyleWithBoxCharacteristics() {
    }

    /**
	 * Constructs a new IfcTextStyleWithBoxCharacteristics object using the given parameters.
	 **/
    public IfcTextStyleWithBoxCharacteristics(IfcPositiveLengthMeasure BoxHeight, IfcPositiveLengthMeasure BoxWidth, IfcPlaneAngleMeasure BoxSlantAngle, IfcPlaneAngleMeasure BoxRotateAngle, IfcSizeSelect CharacterSpacing) {
        this.BoxHeight = BoxHeight;
        this.BoxWidth = BoxWidth;
        this.BoxSlantAngle = BoxSlantAngle;
        this.BoxRotateAngle = BoxRotateAngle;
        this.CharacterSpacing = CharacterSpacing;
    }

    /**
	 * This method initializes the IfcTextStyleWithBoxCharacteristics object using the given parameters.
	 **/
    public void setParameters(IfcPositiveLengthMeasure BoxHeight, IfcPositiveLengthMeasure BoxWidth, IfcPlaneAngleMeasure BoxSlantAngle, IfcPlaneAngleMeasure BoxRotateAngle, IfcSizeSelect CharacterSpacing) {
        this.BoxHeight = BoxHeight;
        this.BoxWidth = BoxWidth;
        this.BoxSlantAngle = BoxSlantAngle;
        this.BoxRotateAngle = BoxRotateAngle;
        this.CharacterSpacing = CharacterSpacing;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.BoxHeight = (IfcPositiveLengthMeasure) parameters.get(0);
        this.BoxWidth = (IfcPositiveLengthMeasure) parameters.get(1);
        this.BoxSlantAngle = (IfcPlaneAngleMeasure) parameters.get(2);
        this.BoxRotateAngle = (IfcPlaneAngleMeasure) parameters.get(3);
        this.CharacterSpacing = (IfcSizeSelect) parameters.get(4);
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
        stepString = stepString.concat("IFCTEXTSTYLEWITHBOXCHARACTERISTICS(");
        if (getRedefinedDerivedAttributeTypes().contains("BoxHeight")) stepString = stepString.concat("*,"); else {
            if (this.BoxHeight != null) stepString = stepString.concat(((IfcRootInterface) this.BoxHeight).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("BoxWidth")) stepString = stepString.concat("*,"); else {
            if (this.BoxWidth != null) stepString = stepString.concat(((IfcRootInterface) this.BoxWidth).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("BoxSlantAngle")) stepString = stepString.concat("*,"); else {
            if (this.BoxSlantAngle != null) stepString = stepString.concat(((IfcRootInterface) this.BoxSlantAngle).getStepParameter(IfcPlaneAngleMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("BoxRotateAngle")) stepString = stepString.concat("*,"); else {
            if (this.BoxRotateAngle != null) stepString = stepString.concat(((IfcRootInterface) this.BoxRotateAngle).getStepParameter(IfcPlaneAngleMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("CharacterSpacing")) stepString = stepString.concat("*);"); else {
            if (this.CharacterSpacing != null) stepString = stepString.concat(((IfcRootInterface) this.CharacterSpacing).getStepParameter(IfcSizeSelect.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the BoxHeight attribute to the given value.
	 * 
	 * @param BoxHeight value to set
	 *            /
	 **/
    public void setBoxHeight(IfcPositiveLengthMeasure BoxHeight) {
        this.BoxHeight = BoxHeight;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the BoxHeight attribute.
	 * 
	 * @return the value of BoxHeight
	 *         /
	 **/
    public IfcPositiveLengthMeasure getBoxHeight() {
        return this.BoxHeight;
    }

    /**
	 * This method sets the BoxWidth attribute to the given value.
	 * 
	 * @param BoxWidth value to set
	 *            /
	 **/
    public void setBoxWidth(IfcPositiveLengthMeasure BoxWidth) {
        this.BoxWidth = BoxWidth;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the BoxWidth attribute.
	 * 
	 * @return the value of BoxWidth
	 *         /
	 **/
    public IfcPositiveLengthMeasure getBoxWidth() {
        return this.BoxWidth;
    }

    /**
	 * This method sets the BoxSlantAngle attribute to the given value.
	 * 
	 * @param BoxSlantAngle value to set
	 *            /
	 **/
    public void setBoxSlantAngle(IfcPlaneAngleMeasure BoxSlantAngle) {
        this.BoxSlantAngle = BoxSlantAngle;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the BoxSlantAngle attribute.
	 * 
	 * @return the value of BoxSlantAngle
	 *         /
	 **/
    public IfcPlaneAngleMeasure getBoxSlantAngle() {
        return this.BoxSlantAngle;
    }

    /**
	 * This method sets the BoxRotateAngle attribute to the given value.
	 * 
	 * @param BoxRotateAngle value to set
	 *            /
	 **/
    public void setBoxRotateAngle(IfcPlaneAngleMeasure BoxRotateAngle) {
        this.BoxRotateAngle = BoxRotateAngle;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the BoxRotateAngle attribute.
	 * 
	 * @return the value of BoxRotateAngle
	 *         /
	 **/
    public IfcPlaneAngleMeasure getBoxRotateAngle() {
        return this.BoxRotateAngle;
    }

    /**
	 * This method sets the CharacterSpacing attribute to the given value.
	 * 
	 * @param CharacterSpacing value to set
	 *            /
	 **/
    public void setCharacterSpacing(IfcSizeSelect CharacterSpacing) {
        this.CharacterSpacing = CharacterSpacing;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the CharacterSpacing attribute.
	 * 
	 * @return the value of CharacterSpacing
	 *         /
	 **/
    public IfcSizeSelect getCharacterSpacing() {
        return this.CharacterSpacing;
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
        IfcTextStyleWithBoxCharacteristics ifcTextStyleWithBoxCharacteristics = new IfcTextStyleWithBoxCharacteristics();
        if (this.BoxHeight != null) ifcTextStyleWithBoxCharacteristics.setBoxHeight((IfcPositiveLengthMeasure) this.BoxHeight.clone());
        if (this.BoxWidth != null) ifcTextStyleWithBoxCharacteristics.setBoxWidth((IfcPositiveLengthMeasure) this.BoxWidth.clone());
        if (this.BoxSlantAngle != null) ifcTextStyleWithBoxCharacteristics.setBoxSlantAngle((IfcPlaneAngleMeasure) this.BoxSlantAngle.clone());
        if (this.BoxRotateAngle != null) ifcTextStyleWithBoxCharacteristics.setBoxRotateAngle((IfcPlaneAngleMeasure) this.BoxRotateAngle.clone());
        if (this.CharacterSpacing != null) ifcTextStyleWithBoxCharacteristics.setCharacterSpacing((IfcSizeSelect) this.CharacterSpacing.clone());
        return ifcTextStyleWithBoxCharacteristics;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcTextStyleWithBoxCharacteristics ifcTextStyleWithBoxCharacteristics = new IfcTextStyleWithBoxCharacteristics();
        if (this.BoxHeight != null) ifcTextStyleWithBoxCharacteristics.setBoxHeight(this.BoxHeight);
        if (this.BoxWidth != null) ifcTextStyleWithBoxCharacteristics.setBoxWidth(this.BoxWidth);
        if (this.BoxSlantAngle != null) ifcTextStyleWithBoxCharacteristics.setBoxSlantAngle(this.BoxSlantAngle);
        if (this.BoxRotateAngle != null) ifcTextStyleWithBoxCharacteristics.setBoxRotateAngle(this.BoxRotateAngle);
        if (this.CharacterSpacing != null) ifcTextStyleWithBoxCharacteristics.setCharacterSpacing(this.CharacterSpacing);
        return ifcTextStyleWithBoxCharacteristics;
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
