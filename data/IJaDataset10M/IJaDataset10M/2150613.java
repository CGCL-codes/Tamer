package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcLightSourcePositional<br><br>
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
public class IfcLightSourcePositional extends IfcLightSource implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcLabel", "IfcColourRgb", "IfcNormalisedRatioMeasure", "IfcNormalisedRatioMeasure", "IfcCartesianPoint", "IfcPositiveLengthMeasure", "IfcReal", "IfcReal", "IfcReal" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public IfcCartesianPoint Position;

    public IfcPositiveLengthMeasure Radius;

    public IfcReal ConstantAttenuation;

    public IfcReal DistanceAttenuation;

    public IfcReal QuadricAttenuation;

    /**
	 * The default constructor.
	 **/
    public IfcLightSourcePositional() {
    }

    /**
	 * Constructs a new IfcLightSourcePositional object using the given parameters.
	 **/
    public IfcLightSourcePositional(IfcLabel Name, IfcColourRgb LightColour, IfcNormalisedRatioMeasure AmbientIntensity, IfcNormalisedRatioMeasure Intensity, IfcCartesianPoint Position, IfcPositiveLengthMeasure Radius, IfcReal ConstantAttenuation, IfcReal DistanceAttenuation, IfcReal QuadricAttenuation) {
        this.Name = Name;
        this.LightColour = LightColour;
        this.AmbientIntensity = AmbientIntensity;
        this.Intensity = Intensity;
        this.Position = Position;
        this.Radius = Radius;
        this.ConstantAttenuation = ConstantAttenuation;
        this.DistanceAttenuation = DistanceAttenuation;
        this.QuadricAttenuation = QuadricAttenuation;
    }

    /**
	 * This method initializes the IfcLightSourcePositional object using the given parameters.
	 **/
    public void setParameters(IfcLabel Name, IfcColourRgb LightColour, IfcNormalisedRatioMeasure AmbientIntensity, IfcNormalisedRatioMeasure Intensity, IfcCartesianPoint Position, IfcPositiveLengthMeasure Radius, IfcReal ConstantAttenuation, IfcReal DistanceAttenuation, IfcReal QuadricAttenuation) {
        this.Name = Name;
        this.LightColour = LightColour;
        this.AmbientIntensity = AmbientIntensity;
        this.Intensity = Intensity;
        this.Position = Position;
        this.Radius = Radius;
        this.ConstantAttenuation = ConstantAttenuation;
        this.DistanceAttenuation = DistanceAttenuation;
        this.QuadricAttenuation = QuadricAttenuation;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.Name = (IfcLabel) parameters.get(0);
        this.LightColour = (IfcColourRgb) parameters.get(1);
        this.AmbientIntensity = (IfcNormalisedRatioMeasure) parameters.get(2);
        this.Intensity = (IfcNormalisedRatioMeasure) parameters.get(3);
        this.Position = (IfcCartesianPoint) parameters.get(4);
        this.Radius = (IfcPositiveLengthMeasure) parameters.get(5);
        this.ConstantAttenuation = (IfcReal) parameters.get(6);
        this.DistanceAttenuation = (IfcReal) parameters.get(7);
        this.QuadricAttenuation = (IfcReal) parameters.get(8);
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
        stepString = stepString.concat("IFCLIGHTSOURCEPOSITIONAL(");
        if (getRedefinedDerivedAttributeTypes().contains("Name")) stepString = stepString.concat("*,"); else {
            if (this.Name != null) stepString = stepString.concat(((IfcRootInterface) this.Name).getStepParameter(IfcLabel.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("LightColour")) stepString = stepString.concat("*,"); else {
            if (this.LightColour != null) stepString = stepString.concat(((IfcRootInterface) this.LightColour).getStepParameter(IfcColourRgb.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("AmbientIntensity")) stepString = stepString.concat("*,"); else {
            if (this.AmbientIntensity != null) stepString = stepString.concat(((IfcRootInterface) this.AmbientIntensity).getStepParameter(IfcNormalisedRatioMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Intensity")) stepString = stepString.concat("*,"); else {
            if (this.Intensity != null) stepString = stepString.concat(((IfcRootInterface) this.Intensity).getStepParameter(IfcNormalisedRatioMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Position")) stepString = stepString.concat("*,"); else {
            if (this.Position != null) stepString = stepString.concat(((IfcRootInterface) this.Position).getStepParameter(IfcCartesianPoint.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Radius")) stepString = stepString.concat("*,"); else {
            if (this.Radius != null) stepString = stepString.concat(((IfcRootInterface) this.Radius).getStepParameter(IfcPositiveLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("ConstantAttenuation")) stepString = stepString.concat("*,"); else {
            if (this.ConstantAttenuation != null) stepString = stepString.concat(((IfcRootInterface) this.ConstantAttenuation).getStepParameter(IfcReal.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("DistanceAttenuation")) stepString = stepString.concat("*,"); else {
            if (this.DistanceAttenuation != null) stepString = stepString.concat(((IfcRootInterface) this.DistanceAttenuation).getStepParameter(IfcReal.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("QuadricAttenuation")) stepString = stepString.concat("*);"); else {
            if (this.QuadricAttenuation != null) stepString = stepString.concat(((IfcRootInterface) this.QuadricAttenuation).getStepParameter(IfcReal.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the LightColour attribute to the given value.
	 * 
	 * @param LightColour value to set
	 *            /
	 **/
    public void setLightColour(IfcColourRgb LightColour) {
        this.LightColour = LightColour;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the LightColour attribute.
	 * 
	 * @return the value of LightColour
	 *         /
	 **/
    public IfcColourRgb getLightColour() {
        return this.LightColour;
    }

    /**
	 * This method sets the AmbientIntensity attribute to the given value.
	 * 
	 * @param AmbientIntensity value to set
	 *            /
	 **/
    public void setAmbientIntensity(IfcNormalisedRatioMeasure AmbientIntensity) {
        this.AmbientIntensity = AmbientIntensity;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the AmbientIntensity attribute.
	 * 
	 * @return the value of AmbientIntensity
	 *         /
	 **/
    public IfcNormalisedRatioMeasure getAmbientIntensity() {
        return this.AmbientIntensity;
    }

    /**
	 * This method sets the Intensity attribute to the given value.
	 * 
	 * @param Intensity value to set
	 *            /
	 **/
    public void setIntensity(IfcNormalisedRatioMeasure Intensity) {
        this.Intensity = Intensity;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Intensity attribute.
	 * 
	 * @return the value of Intensity
	 *         /
	 **/
    public IfcNormalisedRatioMeasure getIntensity() {
        return this.Intensity;
    }

    /**
	 * This method sets the Position attribute to the given value.
	 * 
	 * @param Position value to set
	 *            /
	 **/
    public void setPosition(IfcCartesianPoint Position) {
        this.Position = Position;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Position attribute.
	 * 
	 * @return the value of Position
	 *         /
	 **/
    public IfcCartesianPoint getPosition() {
        return this.Position;
    }

    /**
	 * This method sets the Radius attribute to the given value.
	 * 
	 * @param Radius value to set
	 *            /
	 **/
    public void setRadius(IfcPositiveLengthMeasure Radius) {
        this.Radius = Radius;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Radius attribute.
	 * 
	 * @return the value of Radius
	 *         /
	 **/
    public IfcPositiveLengthMeasure getRadius() {
        return this.Radius;
    }

    /**
	 * This method sets the ConstantAttenuation attribute to the given value.
	 * 
	 * @param ConstantAttenuation value to set
	 *            /
	 **/
    public void setConstantAttenuation(IfcReal ConstantAttenuation) {
        this.ConstantAttenuation = ConstantAttenuation;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ConstantAttenuation attribute.
	 * 
	 * @return the value of ConstantAttenuation
	 *         /
	 **/
    public IfcReal getConstantAttenuation() {
        return this.ConstantAttenuation;
    }

    /**
	 * This method sets the DistanceAttenuation attribute to the given value.
	 * 
	 * @param DistanceAttenuation value to set
	 *            /
	 **/
    public void setDistanceAttenuation(IfcReal DistanceAttenuation) {
        this.DistanceAttenuation = DistanceAttenuation;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the DistanceAttenuation attribute.
	 * 
	 * @return the value of DistanceAttenuation
	 *         /
	 **/
    public IfcReal getDistanceAttenuation() {
        return this.DistanceAttenuation;
    }

    /**
	 * This method sets the QuadricAttenuation attribute to the given value.
	 * 
	 * @param QuadricAttenuation value to set
	 *            /
	 **/
    public void setQuadricAttenuation(IfcReal QuadricAttenuation) {
        this.QuadricAttenuation = QuadricAttenuation;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the QuadricAttenuation attribute.
	 * 
	 * @return the value of QuadricAttenuation
	 *         /
	 **/
    public IfcReal getQuadricAttenuation() {
        return this.QuadricAttenuation;
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
        IfcLightSourcePositional ifcLightSourcePositional = new IfcLightSourcePositional();
        if (this.Name != null) ifcLightSourcePositional.setName((IfcLabel) this.Name.clone());
        if (this.LightColour != null) ifcLightSourcePositional.setLightColour((IfcColourRgb) this.LightColour.clone());
        if (this.AmbientIntensity != null) ifcLightSourcePositional.setAmbientIntensity((IfcNormalisedRatioMeasure) this.AmbientIntensity.clone());
        if (this.Intensity != null) ifcLightSourcePositional.setIntensity((IfcNormalisedRatioMeasure) this.Intensity.clone());
        if (this.Position != null) ifcLightSourcePositional.setPosition((IfcCartesianPoint) this.Position.clone());
        if (this.Radius != null) ifcLightSourcePositional.setRadius((IfcPositiveLengthMeasure) this.Radius.clone());
        if (this.ConstantAttenuation != null) ifcLightSourcePositional.setConstantAttenuation((IfcReal) this.ConstantAttenuation.clone());
        if (this.DistanceAttenuation != null) ifcLightSourcePositional.setDistanceAttenuation((IfcReal) this.DistanceAttenuation.clone());
        if (this.QuadricAttenuation != null) ifcLightSourcePositional.setQuadricAttenuation((IfcReal) this.QuadricAttenuation.clone());
        return ifcLightSourcePositional;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcLightSourcePositional ifcLightSourcePositional = new IfcLightSourcePositional();
        if (this.Name != null) ifcLightSourcePositional.setName(this.Name);
        if (this.LightColour != null) ifcLightSourcePositional.setLightColour(this.LightColour);
        if (this.AmbientIntensity != null) ifcLightSourcePositional.setAmbientIntensity(this.AmbientIntensity);
        if (this.Intensity != null) ifcLightSourcePositional.setIntensity(this.Intensity);
        if (this.Position != null) ifcLightSourcePositional.setPosition(this.Position);
        if (this.Radius != null) ifcLightSourcePositional.setRadius(this.Radius);
        if (this.ConstantAttenuation != null) ifcLightSourcePositional.setConstantAttenuation(this.ConstantAttenuation);
        if (this.DistanceAttenuation != null) ifcLightSourcePositional.setDistanceAttenuation(this.DistanceAttenuation);
        if (this.QuadricAttenuation != null) ifcLightSourcePositional.setQuadricAttenuation(this.QuadricAttenuation);
        return ifcLightSourcePositional;
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
