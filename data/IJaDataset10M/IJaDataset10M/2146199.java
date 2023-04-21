package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcOffsetCurve3D<br><br>
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
public class IfcOffsetCurve3D extends IfcCurve implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcCurve", "IfcLengthMeasure", "LOGICAL", "IfcDirection" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public IfcCurve BasisCurve;

    public IfcLengthMeasure Distance;

    public LOGICAL SelfIntersect;

    public IfcDirection RefDirection;

    /**
	 * The default constructor.
	 **/
    public IfcOffsetCurve3D() {
    }

    /**
	 * Constructs a new IfcOffsetCurve3D object using the given parameters.
	 **/
    public IfcOffsetCurve3D(IfcCurve BasisCurve, IfcLengthMeasure Distance, LOGICAL SelfIntersect, IfcDirection RefDirection) {
        this.BasisCurve = BasisCurve;
        this.Distance = Distance;
        this.SelfIntersect = SelfIntersect;
        this.RefDirection = RefDirection;
    }

    /**
	 * This method initializes the IfcOffsetCurve3D object using the given parameters.
	 **/
    public void setParameters(IfcCurve BasisCurve, IfcLengthMeasure Distance, LOGICAL SelfIntersect, IfcDirection RefDirection) {
        this.BasisCurve = BasisCurve;
        this.Distance = Distance;
        this.SelfIntersect = SelfIntersect;
        this.RefDirection = RefDirection;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.BasisCurve = (IfcCurve) parameters.get(0);
        this.Distance = (IfcLengthMeasure) parameters.get(1);
        this.SelfIntersect = (LOGICAL) parameters.get(2);
        this.RefDirection = (IfcDirection) parameters.get(3);
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
        stepString = stepString.concat("IFCOFFSETCURVE3D(");
        if (getRedefinedDerivedAttributeTypes().contains("BasisCurve")) stepString = stepString.concat("*,"); else {
            if (this.BasisCurve != null) stepString = stepString.concat(((IfcRootInterface) this.BasisCurve).getStepParameter(IfcCurve.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("Distance")) stepString = stepString.concat("*,"); else {
            if (this.Distance != null) stepString = stepString.concat(((IfcRootInterface) this.Distance).getStepParameter(IfcLengthMeasure.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("SelfIntersect")) stepString = stepString.concat("*,"); else {
            if (this.SelfIntersect != null) stepString = stepString.concat(((IfcRootInterface) this.SelfIntersect).getStepParameter(LOGICAL.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("RefDirection")) stepString = stepString.concat("*);"); else {
            if (this.RefDirection != null) stepString = stepString.concat(((IfcRootInterface) this.RefDirection).getStepParameter(IfcDirection.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the BasisCurve attribute to the given value.
	 * 
	 * @param BasisCurve value to set
	 *            /
	 **/
    public void setBasisCurve(IfcCurve BasisCurve) {
        this.BasisCurve = BasisCurve;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the BasisCurve attribute.
	 * 
	 * @return the value of BasisCurve
	 *         /
	 **/
    public IfcCurve getBasisCurve() {
        return this.BasisCurve;
    }

    /**
	 * This method sets the Distance attribute to the given value.
	 * 
	 * @param Distance value to set
	 *            /
	 **/
    public void setDistance(IfcLengthMeasure Distance) {
        this.Distance = Distance;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the Distance attribute.
	 * 
	 * @return the value of Distance
	 *         /
	 **/
    public IfcLengthMeasure getDistance() {
        return this.Distance;
    }

    /**
	 * This method sets the SelfIntersect attribute to the given value.
	 * 
	 * @param SelfIntersect value to set
	 *            /
	 **/
    public void setSelfIntersect(LOGICAL SelfIntersect) {
        this.SelfIntersect = SelfIntersect;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the SelfIntersect attribute.
	 * 
	 * @return the value of SelfIntersect
	 *         /
	 **/
    public LOGICAL getSelfIntersect() {
        return this.SelfIntersect;
    }

    /**
	 * This method sets the RefDirection attribute to the given value.
	 * 
	 * @param RefDirection value to set
	 *            /
	 **/
    public void setRefDirection(IfcDirection RefDirection) {
        this.RefDirection = RefDirection;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the RefDirection attribute.
	 * 
	 * @return the value of RefDirection
	 *         /
	 **/
    public IfcDirection getRefDirection() {
        return this.RefDirection;
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
        IfcOffsetCurve3D ifcOffsetCurve3D = new IfcOffsetCurve3D();
        if (this.BasisCurve != null) ifcOffsetCurve3D.setBasisCurve((IfcCurve) this.BasisCurve.clone());
        if (this.Distance != null) ifcOffsetCurve3D.setDistance((IfcLengthMeasure) this.Distance.clone());
        if (this.SelfIntersect != null) ifcOffsetCurve3D.setSelfIntersect((LOGICAL) this.SelfIntersect.clone());
        if (this.RefDirection != null) ifcOffsetCurve3D.setRefDirection((IfcDirection) this.RefDirection.clone());
        return ifcOffsetCurve3D;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcOffsetCurve3D ifcOffsetCurve3D = new IfcOffsetCurve3D();
        if (this.BasisCurve != null) ifcOffsetCurve3D.setBasisCurve(this.BasisCurve);
        if (this.Distance != null) ifcOffsetCurve3D.setDistance(this.Distance);
        if (this.SelfIntersect != null) ifcOffsetCurve3D.setSelfIntersect(this.SelfIntersect);
        if (this.RefDirection != null) ifcOffsetCurve3D.setRefDirection(this.RefDirection);
        return ifcOffsetCurve3D;
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
