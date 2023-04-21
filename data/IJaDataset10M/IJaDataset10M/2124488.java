package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcAsymmetricIShapeProfileDef<br><br>
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
public class IfcAsymmetricIShapeProfileDef extends IfcIShapeProfileDef implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcProfileTypeEnum", "IfcLabel", "IfcAxis2Placement2D", "IfcPositiveLengthMeasure", "IfcPositiveLengthMeasure", "IfcPositiveLengthMeasure", "IfcPositiveLengthMeasure", "IfcPositiveLengthMeasure", "IfcPositiveLengthMeasure", "IfcPositiveLengthMeasure", "IfcPositiveLengthMeasure", "IfcPositiveLengthMeasure" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public IfcPositiveLengthMeasure TopFlangeWidth;

    public IfcPositiveLengthMeasure TopFlangeThickness;

    public IfcPositiveLengthMeasure TopFlangeFilletRadius;

    public IfcPositiveLengthMeasure CentreOfGravityInY;

    /**
	 * The default constructor.
	 **/
    public IfcAsymmetricIShapeProfileDef() {
    }

    /**
	 * Constructs a new IfcAsymmetricIShapeProfileDef object using the given parameters.
	 **/
    public IfcAsymmetricIShapeProfileDef(IfcProfileTypeEnum ProfileType, IfcLabel ProfileName, IfcAxis2Placement2D Position, IfcPositiveLengthMeasure OverallWidth, IfcPositiveLengthMeasure OverallDepth, IfcPositiveLengthMeasure WebThickness, IfcPositiveLengthMeasure FlangeThickness, IfcPositiveLengthMeasure FilletRadius, IfcPositiveLengthMeasure TopFlangeWidth, IfcPositiveLengthMeasure TopFlangeThickness, IfcPositiveLengthMeasure TopFlangeFilletRadius, IfcPositiveLengthMeasure CentreOfGravityInY) {
        this.ProfileType = ProfileType;
        this.ProfileName = ProfileName;
        this.Position = Position;
        this.OverallWidth = OverallWidth;
        this.OverallDepth = OverallDepth;
        this.WebThickness = WebThickness;
        this.FlangeThickness = FlangeThickness;
        this.FilletRadius = FilletRadius;
        this.TopFlangeWidth = TopFlangeWidth;
        this.TopFlangeThickness = TopFlangeThickness;
        this.TopFlangeFilletRadius = TopFlangeFilletRadius;
        this.CentreOfGravityInY = CentreOfGravityInY;
    }

    /**
	 * This method initializes the IfcAsymmetricIShapeProfileDef object using the given parameters.
	 **/
    public void setParameters(IfcProfileTypeEnum ProfileType, IfcLabel ProfileName, IfcAxis2Placement2D Position, IfcPositiveLengthMeasure OverallWidth, IfcPositiveLengthMeasure OverallDepth, IfcPositiveLengthMeasure WebThickness, IfcPositiveLengthMeasure FlangeThickness, IfcPositiveLengthMeasure FilletRadius, IfcPositiveLengthMeasure TopFlangeWidth, IfcPositiveLengthMeasure TopFlangeThickness, IfcPositiveLengthMeasure TopFlangeFilletRadius, IfcPositiveLengthMeasure CentreOfGravityInY) {
        this.ProfileType = ProfileType;
        this.ProfileName = ProfileName;
        this.Position = Position;
        this.OverallWidth = OverallWidth;
        this.OverallDepth = OverallDepth;
        this.WebThickness = WebThickness;
        this.FlangeThickness = FlangeThickness;
        this.FilletRadius = FilletRadius;
        this.TopFlangeWidth = TopFlangeWidth;
        this.TopFlangeThickness = TopFlangeThickness;
        this.TopFlangeFilletRadius = TopFlangeFilletRadius;
        this.CentreOfGravityInY = CentreOfGravityInY;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.ProfileType = (IfcProfileTypeEnum) parameters.get(0);
        this.ProfileName = (IfcLabel) parameters.get(1);
        this.Position = (IfcAxis2Placement2D) parameters.get(2);
        this.OverallWidth = (IfcPositiveLengthMeasure) parameters.get(3);
        this.OverallDepth = (IfcPositiveLengthMeasure) parameters.get(4);
        this.WebThickness = (IfcPositiveLengthMeasure) parameters.get(5);
        this.FlangeThickness = (IfcPositiveLengthMeasure) parameters.get(6);
        this.FilletRadius = (IfcPositiveLengthMeasure) parameters.get(7);
        this.TopFlangeWidth = (IfcPositiveLengthMeasure) parameters.get(8);
        this.TopFlangeThickness = (IfcPositiveLengthMeasure) parameters.get(9);
        this.TopFlangeFilletRadius = (IfcPositiveLengthMeasure) parameters.get(10);
        this.CentreOfGravityInY = (IfcPositiveLengthMeasure) parameters.get(11);
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
        stepString = stepString.concat("IFCASYMMETRICISHAPEPROFILEDEF(");
        if (getRedefinedDerivedAttributeTypes().contains("ProfileType")) stepString = stepString.concat("*,"); else {
            if (this.ProfileType != null) stepString = stepString.concat(((IfcRootInterface) this.ProfileType).getStepParameter(IfcProfileTypeEnum.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("ProfileName")) stepString = stepString.concat("*,"); else {
            if (this.ProfileName != null) stepString = stepString.concat(((IfcRootInterface) this.ProfileName).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Position")) stepString = stepString.concat("*,"); else {
            if (this.Position != null) stepString = stepString.concat(((IfcRootInterface) this.Position).getStepParameter(IfcAxis2Placement2D.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("OverallWidth")) stepString = stepString.concat("*,"); else {
            if (this.OverallWidth != null) stepString = stepString.concat(((IfcRootInterface) this.OverallWidth).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("OverallDepth")) stepString = stepString.concat("*,"); else {
            if (this.OverallDepth != null) stepString = stepString.concat(((IfcRootInterface) this.OverallDepth).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("WebThickness")) stepString = stepString.concat("*,"); else {
            if (this.WebThickness != null) stepString = stepString.concat(((IfcRootInterface) this.WebThickness).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("FlangeThickness")) stepString = stepString.concat("*,"); else {
            if (this.FlangeThickness != null) stepString = stepString.concat(((IfcRootInterface) this.FlangeThickness).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("FilletRadius")) stepString = stepString.concat("*,"); else {
            if (this.FilletRadius != null) stepString = stepString.concat(((IfcRootInterface) this.FilletRadius).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("TopFlangeWidth")) stepString = stepString.concat("*,"); else {
            if (this.TopFlangeWidth != null) stepString = stepString.concat(((IfcRootInterface) this.TopFlangeWidth).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("TopFlangeThickness")) stepString = stepString.concat("*,"); else {
            if (this.TopFlangeThickness != null) stepString = stepString.concat(((IfcRootInterface) this.TopFlangeThickness).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("TopFlangeFilletRadius")) stepString = stepString.concat("*,"); else {
            if (this.TopFlangeFilletRadius != null) stepString = stepString.concat(((IfcRootInterface) this.TopFlangeFilletRadius).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("CentreOfGravityInY")) stepString = stepString.concat("*);"); else {
            if (this.CentreOfGravityInY != null) stepString = stepString.concat(((IfcRootInterface) this.CentreOfGravityInY).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the ProfileType attribute to the given value.
	 * 
	 * @param ProfileType value to set
	 *            /
	 **/
    public void setProfileType(IfcProfileTypeEnum ProfileType) {
        this.ProfileType = ProfileType;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ProfileType attribute.
	 * 
	 * @return the value of ProfileType
	 *         /
	 **/
    public IfcProfileTypeEnum getProfileType() {
        return this.ProfileType;
    }

    /**
	 * This method sets the ProfileName attribute to the given value.
	 * 
	 * @param ProfileName value to set
	 *            /
	 **/
    public void setProfileName(IfcLabel ProfileName) {
        this.ProfileName = ProfileName;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ProfileName attribute.
	 * 
	 * @return the value of ProfileName
	 *         /
	 **/
    public IfcLabel getProfileName() {
        return this.ProfileName;
    }

    /**
	 * This method sets the Position attribute to the given value.
	 * 
	 * @param Position value to set
	 *            /
	 **/
    public void setPosition(IfcAxis2Placement2D Position) {
        this.Position = Position;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Position attribute.
	 * 
	 * @return the value of Position
	 *         /
	 **/
    public IfcAxis2Placement2D getPosition() {
        return this.Position;
    }

    /**
	 * This method sets the OverallWidth attribute to the given value.
	 * 
	 * @param OverallWidth value to set
	 *            /
	 **/
    public void setOverallWidth(IfcPositiveLengthMeasure OverallWidth) {
        this.OverallWidth = OverallWidth;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the OverallWidth attribute.
	 * 
	 * @return the value of OverallWidth
	 *         /
	 **/
    public IfcPositiveLengthMeasure getOverallWidth() {
        return this.OverallWidth;
    }

    /**
	 * This method sets the OverallDepth attribute to the given value.
	 * 
	 * @param OverallDepth value to set
	 *            /
	 **/
    public void setOverallDepth(IfcPositiveLengthMeasure OverallDepth) {
        this.OverallDepth = OverallDepth;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the OverallDepth attribute.
	 * 
	 * @return the value of OverallDepth
	 *         /
	 **/
    public IfcPositiveLengthMeasure getOverallDepth() {
        return this.OverallDepth;
    }

    /**
	 * This method sets the WebThickness attribute to the given value.
	 * 
	 * @param WebThickness value to set
	 *            /
	 **/
    public void setWebThickness(IfcPositiveLengthMeasure WebThickness) {
        this.WebThickness = WebThickness;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the WebThickness attribute.
	 * 
	 * @return the value of WebThickness
	 *         /
	 **/
    public IfcPositiveLengthMeasure getWebThickness() {
        return this.WebThickness;
    }

    /**
	 * This method sets the FlangeThickness attribute to the given value.
	 * 
	 * @param FlangeThickness value to set
	 *            /
	 **/
    public void setFlangeThickness(IfcPositiveLengthMeasure FlangeThickness) {
        this.FlangeThickness = FlangeThickness;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the FlangeThickness attribute.
	 * 
	 * @return the value of FlangeThickness
	 *         /
	 **/
    public IfcPositiveLengthMeasure getFlangeThickness() {
        return this.FlangeThickness;
    }

    /**
	 * This method sets the FilletRadius attribute to the given value.
	 * 
	 * @param FilletRadius value to set
	 *            /
	 **/
    public void setFilletRadius(IfcPositiveLengthMeasure FilletRadius) {
        this.FilletRadius = FilletRadius;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the FilletRadius attribute.
	 * 
	 * @return the value of FilletRadius
	 *         /
	 **/
    public IfcPositiveLengthMeasure getFilletRadius() {
        return this.FilletRadius;
    }

    /**
	 * This method sets the TopFlangeWidth attribute to the given value.
	 * 
	 * @param TopFlangeWidth value to set
	 *            /
	 **/
    public void setTopFlangeWidth(IfcPositiveLengthMeasure TopFlangeWidth) {
        this.TopFlangeWidth = TopFlangeWidth;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the TopFlangeWidth attribute.
	 * 
	 * @return the value of TopFlangeWidth
	 *         /
	 **/
    public IfcPositiveLengthMeasure getTopFlangeWidth() {
        return this.TopFlangeWidth;
    }

    /**
	 * This method sets the TopFlangeThickness attribute to the given value.
	 * 
	 * @param TopFlangeThickness value to set
	 *            /
	 **/
    public void setTopFlangeThickness(IfcPositiveLengthMeasure TopFlangeThickness) {
        this.TopFlangeThickness = TopFlangeThickness;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the TopFlangeThickness attribute.
	 * 
	 * @return the value of TopFlangeThickness
	 *         /
	 **/
    public IfcPositiveLengthMeasure getTopFlangeThickness() {
        return this.TopFlangeThickness;
    }

    /**
	 * This method sets the TopFlangeFilletRadius attribute to the given value.
	 * 
	 * @param TopFlangeFilletRadius value to set
	 *            /
	 **/
    public void setTopFlangeFilletRadius(IfcPositiveLengthMeasure TopFlangeFilletRadius) {
        this.TopFlangeFilletRadius = TopFlangeFilletRadius;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the TopFlangeFilletRadius attribute.
	 * 
	 * @return the value of TopFlangeFilletRadius
	 *         /
	 **/
    public IfcPositiveLengthMeasure getTopFlangeFilletRadius() {
        return this.TopFlangeFilletRadius;
    }

    /**
	 * This method sets the CentreOfGravityInY attribute to the given value.
	 * 
	 * @param CentreOfGravityInY value to set
	 *            /
	 **/
    public void setCentreOfGravityInY(IfcPositiveLengthMeasure CentreOfGravityInY) {
        this.CentreOfGravityInY = CentreOfGravityInY;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the CentreOfGravityInY attribute.
	 * 
	 * @return the value of CentreOfGravityInY
	 *         /
	 **/
    public IfcPositiveLengthMeasure getCentreOfGravityInY() {
        return this.CentreOfGravityInY;
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
        IfcAsymmetricIShapeProfileDef ifcAsymmetricIShapeProfileDef = new IfcAsymmetricIShapeProfileDef();
        if (this.ProfileType != null) ifcAsymmetricIShapeProfileDef.setProfileType((IfcProfileTypeEnum) this.ProfileType.clone());
        if (this.ProfileName != null) ifcAsymmetricIShapeProfileDef.setProfileName((IfcLabel) this.ProfileName.clone());
        if (this.Position != null) ifcAsymmetricIShapeProfileDef.setPosition((IfcAxis2Placement2D) this.Position.clone());
        if (this.OverallWidth != null) ifcAsymmetricIShapeProfileDef.setOverallWidth((IfcPositiveLengthMeasure) this.OverallWidth.clone());
        if (this.OverallDepth != null) ifcAsymmetricIShapeProfileDef.setOverallDepth((IfcPositiveLengthMeasure) this.OverallDepth.clone());
        if (this.WebThickness != null) ifcAsymmetricIShapeProfileDef.setWebThickness((IfcPositiveLengthMeasure) this.WebThickness.clone());
        if (this.FlangeThickness != null) ifcAsymmetricIShapeProfileDef.setFlangeThickness((IfcPositiveLengthMeasure) this.FlangeThickness.clone());
        if (this.FilletRadius != null) ifcAsymmetricIShapeProfileDef.setFilletRadius((IfcPositiveLengthMeasure) this.FilletRadius.clone());
        if (this.TopFlangeWidth != null) ifcAsymmetricIShapeProfileDef.setTopFlangeWidth((IfcPositiveLengthMeasure) this.TopFlangeWidth.clone());
        if (this.TopFlangeThickness != null) ifcAsymmetricIShapeProfileDef.setTopFlangeThickness((IfcPositiveLengthMeasure) this.TopFlangeThickness.clone());
        if (this.TopFlangeFilletRadius != null) ifcAsymmetricIShapeProfileDef.setTopFlangeFilletRadius((IfcPositiveLengthMeasure) this.TopFlangeFilletRadius.clone());
        if (this.CentreOfGravityInY != null) ifcAsymmetricIShapeProfileDef.setCentreOfGravityInY((IfcPositiveLengthMeasure) this.CentreOfGravityInY.clone());
        return ifcAsymmetricIShapeProfileDef;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcAsymmetricIShapeProfileDef ifcAsymmetricIShapeProfileDef = new IfcAsymmetricIShapeProfileDef();
        if (this.ProfileType != null) ifcAsymmetricIShapeProfileDef.setProfileType(this.ProfileType);
        if (this.ProfileName != null) ifcAsymmetricIShapeProfileDef.setProfileName(this.ProfileName);
        if (this.Position != null) ifcAsymmetricIShapeProfileDef.setPosition(this.Position);
        if (this.OverallWidth != null) ifcAsymmetricIShapeProfileDef.setOverallWidth(this.OverallWidth);
        if (this.OverallDepth != null) ifcAsymmetricIShapeProfileDef.setOverallDepth(this.OverallDepth);
        if (this.WebThickness != null) ifcAsymmetricIShapeProfileDef.setWebThickness(this.WebThickness);
        if (this.FlangeThickness != null) ifcAsymmetricIShapeProfileDef.setFlangeThickness(this.FlangeThickness);
        if (this.FilletRadius != null) ifcAsymmetricIShapeProfileDef.setFilletRadius(this.FilletRadius);
        if (this.TopFlangeWidth != null) ifcAsymmetricIShapeProfileDef.setTopFlangeWidth(this.TopFlangeWidth);
        if (this.TopFlangeThickness != null) ifcAsymmetricIShapeProfileDef.setTopFlangeThickness(this.TopFlangeThickness);
        if (this.TopFlangeFilletRadius != null) ifcAsymmetricIShapeProfileDef.setTopFlangeFilletRadius(this.TopFlangeFilletRadius);
        if (this.CentreOfGravityInY != null) ifcAsymmetricIShapeProfileDef.setCentreOfGravityInY(this.CentreOfGravityInY);
        return ifcAsymmetricIShapeProfileDef;
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
