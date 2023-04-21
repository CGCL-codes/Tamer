package openifctools.com.openifcjavatoolbox.ifc2x3tc1;

/**
 * This is a default implementation of the IFC object IfcOwnerHistory<br><br>
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
public class IfcOwnerHistory implements IfcClass {

    private static final long serialVersionUID = 8;

    public static final String[] nonInverseAttributes = new String[] { "IfcPersonAndOrganization", "IfcApplication", "IfcStateEnum", "IfcChangeActionEnum", "IfcTimeStamp", "IfcPersonAndOrganization", "IfcApplication", "IfcTimeStamp" };

    private java.util.ArrayList<CloneableObject> stepParameter = null;

    private java.util.HashSet<IfcObjectChangeListener> listenerList = null;

    private java.util.HashMap<String, Object> userObjectMap = null;

    public int stepLineNumber;

    public IfcPersonAndOrganization OwningUser;

    public IfcApplication OwningApplication;

    public IfcStateEnum State;

    public IfcChangeActionEnum ChangeAction;

    public IfcTimeStamp LastModifiedDate;

    public IfcPersonAndOrganization LastModifyingUser;

    public IfcApplication LastModifyingApplication;

    public IfcTimeStamp CreationDate;

    /**
	 * The default constructor.
	 **/
    public IfcOwnerHistory() {
    }

    /**
	 * Constructs a new IfcOwnerHistory object using the given parameters.
	 **/
    public IfcOwnerHistory(IfcPersonAndOrganization OwningUser, IfcApplication OwningApplication, IfcStateEnum State, IfcChangeActionEnum ChangeAction, IfcTimeStamp LastModifiedDate, IfcPersonAndOrganization LastModifyingUser, IfcApplication LastModifyingApplication, IfcTimeStamp CreationDate) {
        this.OwningUser = OwningUser;
        this.OwningApplication = OwningApplication;
        this.State = State;
        this.ChangeAction = ChangeAction;
        this.LastModifiedDate = LastModifiedDate;
        this.LastModifyingUser = LastModifyingUser;
        this.LastModifyingApplication = LastModifyingApplication;
        this.CreationDate = CreationDate;
    }

    /**
	 * This method initializes the IfcOwnerHistory object using the given parameters.
	 **/
    public void setParameters(IfcPersonAndOrganization OwningUser, IfcApplication OwningApplication, IfcStateEnum State, IfcChangeActionEnum ChangeAction, IfcTimeStamp LastModifiedDate, IfcPersonAndOrganization LastModifyingUser, IfcApplication LastModifyingApplication, IfcTimeStamp CreationDate) {
        this.OwningUser = OwningUser;
        this.OwningApplication = OwningApplication;
        this.State = State;
        this.ChangeAction = ChangeAction;
        this.LastModifiedDate = LastModifiedDate;
        this.LastModifyingUser = LastModifyingUser;
        this.LastModifyingApplication = LastModifyingApplication;
        this.CreationDate = CreationDate;
    }

    /**
	 * This method is used internally and should NOT be used for own purposes.
	 **/
    public void initialize(java.util.ArrayList<CloneableObject> parameters) {
        this.OwningUser = (IfcPersonAndOrganization) parameters.get(0);
        this.OwningApplication = (IfcApplication) parameters.get(1);
        this.State = (IfcStateEnum) parameters.get(2);
        this.ChangeAction = (IfcChangeActionEnum) parameters.get(3);
        this.LastModifiedDate = (IfcTimeStamp) parameters.get(4);
        this.LastModifyingUser = (IfcPersonAndOrganization) parameters.get(5);
        this.LastModifyingApplication = (IfcApplication) parameters.get(6);
        this.CreationDate = (IfcTimeStamp) parameters.get(7);
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
        stepString = stepString.concat("IFCOWNERHISTORY(");
        if (getRedefinedDerivedAttributeTypes().contains("OwningUser")) stepString = stepString.concat("*,"); else {
            if (this.OwningUser != null) stepString = stepString.concat(((IfcRootInterface) this.OwningUser).getStepParameter(IfcPersonAndOrganization.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("OwningApplication")) stepString = stepString.concat("*,"); else {
            if (this.OwningApplication != null) stepString = stepString.concat(((IfcRootInterface) this.OwningApplication).getStepParameter(IfcApplication.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("State")) stepString = stepString.concat("*,"); else {
            if (this.State != null) stepString = stepString.concat(((IfcRootInterface) this.State).getStepParameter(IfcStateEnum.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("ChangeAction")) stepString = stepString.concat("*,"); else {
            if (this.ChangeAction != null) stepString = stepString.concat(((IfcRootInterface) this.ChangeAction).getStepParameter(IfcChangeActionEnum.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("LastModifiedDate")) stepString = stepString.concat("*,"); else {
            if (this.LastModifiedDate != null) stepString = stepString.concat(((IfcRootInterface) this.LastModifiedDate).getStepParameter(IfcTimeStamp.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("LastModifyingUser")) stepString = stepString.concat("*,"); else {
            if (this.LastModifyingUser != null) stepString = stepString.concat(((IfcRootInterface) this.LastModifyingUser).getStepParameter(IfcPersonAndOrganization.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("LastModifyingApplication")) stepString = stepString.concat("*,"); else {
            if (this.LastModifyingApplication != null) stepString = stepString.concat(((IfcRootInterface) this.LastModifyingApplication).getStepParameter(IfcApplication.class.isInterface()) + ","); else stepString = stepString.concat("$,");
        }
        if (getRedefinedDerivedAttributeTypes().contains("CreationDate")) stepString = stepString.concat("*);"); else {
            if (this.CreationDate != null) stepString = stepString.concat(((IfcRootInterface) this.CreationDate).getStepParameter(IfcTimeStamp.class.isInterface()) + ");"); else stepString = stepString.concat("$);");
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
	 * This method sets the OwningUser attribute to the given value.
	 * 
	 * @param OwningUser value to set
	 *            /
	 **/
    public void setOwningUser(IfcPersonAndOrganization OwningUser) {
        this.OwningUser = OwningUser;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the OwningUser attribute.
	 * 
	 * @return the value of OwningUser
	 *         /
	 **/
    public IfcPersonAndOrganization getOwningUser() {
        return this.OwningUser;
    }

    /**
	 * This method sets the OwningApplication attribute to the given value.
	 * 
	 * @param OwningApplication value to set
	 *            /
	 **/
    public void setOwningApplication(IfcApplication OwningApplication) {
        this.OwningApplication = OwningApplication;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the OwningApplication attribute.
	 * 
	 * @return the value of OwningApplication
	 *         /
	 **/
    public IfcApplication getOwningApplication() {
        return this.OwningApplication;
    }

    /**
	 * This method sets the State attribute to the given value.
	 * 
	 * @param State value to set
	 *            /
	 **/
    public void setState(IfcStateEnum State) {
        this.State = State;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the State attribute.
	 * 
	 * @return the value of State
	 *         /
	 **/
    public IfcStateEnum getState() {
        return this.State;
    }

    /**
	 * This method sets the ChangeAction attribute to the given value.
	 * 
	 * @param ChangeAction value to set
	 *            /
	 **/
    public void setChangeAction(IfcChangeActionEnum ChangeAction) {
        this.ChangeAction = ChangeAction;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the ChangeAction attribute.
	 * 
	 * @return the value of ChangeAction
	 *         /
	 **/
    public IfcChangeActionEnum getChangeAction() {
        return this.ChangeAction;
    }

    /**
	 * This method sets the LastModifiedDate attribute to the given value.
	 * 
	 * @param LastModifiedDate value to set
	 *            /
	 **/
    public void setLastModifiedDate(IfcTimeStamp LastModifiedDate) {
        this.LastModifiedDate = LastModifiedDate;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the LastModifiedDate attribute.
	 * 
	 * @return the value of LastModifiedDate
	 *         /
	 **/
    public IfcTimeStamp getLastModifiedDate() {
        return this.LastModifiedDate;
    }

    /**
	 * This method sets the LastModifyingUser attribute to the given value.
	 * 
	 * @param LastModifyingUser value to set
	 *            /
	 **/
    public void setLastModifyingUser(IfcPersonAndOrganization LastModifyingUser) {
        this.LastModifyingUser = LastModifyingUser;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the LastModifyingUser attribute.
	 * 
	 * @return the value of LastModifyingUser
	 *         /
	 **/
    public IfcPersonAndOrganization getLastModifyingUser() {
        return this.LastModifyingUser;
    }

    /**
	 * This method sets the LastModifyingApplication attribute to the given value.
	 * 
	 * @param LastModifyingApplication value to set
	 *            /
	 **/
    public void setLastModifyingApplication(IfcApplication LastModifyingApplication) {
        this.LastModifyingApplication = LastModifyingApplication;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the LastModifyingApplication attribute.
	 * 
	 * @return the value of LastModifyingApplication
	 *         /
	 **/
    public IfcApplication getLastModifyingApplication() {
        return this.LastModifyingApplication;
    }

    /**
	 * This method sets the CreationDate attribute to the given value.
	 * 
	 * @param CreationDate value to set
	 *            /
	 **/
    public void setCreationDate(IfcTimeStamp CreationDate) {
        this.CreationDate = CreationDate;
        fireChangeEvent();
    }

    /**
	 * This method returns the value of the CreationDate attribute.
	 * 
	 * @return the value of CreationDate
	 *         /
	 **/
    public IfcTimeStamp getCreationDate() {
        return this.CreationDate;
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
        IfcOwnerHistory ifcOwnerHistory = new IfcOwnerHistory();
        if (this.OwningUser != null) ifcOwnerHistory.setOwningUser((IfcPersonAndOrganization) this.OwningUser.clone());
        if (this.OwningApplication != null) ifcOwnerHistory.setOwningApplication((IfcApplication) this.OwningApplication.clone());
        if (this.State != null) ifcOwnerHistory.setState((IfcStateEnum) this.State.clone());
        if (this.ChangeAction != null) ifcOwnerHistory.setChangeAction((IfcChangeActionEnum) this.ChangeAction.clone());
        if (this.LastModifiedDate != null) ifcOwnerHistory.setLastModifiedDate((IfcTimeStamp) this.LastModifiedDate.clone());
        if (this.LastModifyingUser != null) ifcOwnerHistory.setLastModifyingUser((IfcPersonAndOrganization) this.LastModifyingUser.clone());
        if (this.LastModifyingApplication != null) ifcOwnerHistory.setLastModifyingApplication((IfcApplication) this.LastModifyingApplication.clone());
        if (this.CreationDate != null) ifcOwnerHistory.setCreationDate((IfcTimeStamp) this.CreationDate.clone());
        return ifcOwnerHistory;
    }

    /**
	 * This method copys the object as shallow copy (all referenced objects are remaining).
	 * 
	 * @return the cloned object
	 **/
    public Object shallowCopy() {
        IfcOwnerHistory ifcOwnerHistory = new IfcOwnerHistory();
        if (this.OwningUser != null) ifcOwnerHistory.setOwningUser(this.OwningUser);
        if (this.OwningApplication != null) ifcOwnerHistory.setOwningApplication(this.OwningApplication);
        if (this.State != null) ifcOwnerHistory.setState(this.State);
        if (this.ChangeAction != null) ifcOwnerHistory.setChangeAction(this.ChangeAction);
        if (this.LastModifiedDate != null) ifcOwnerHistory.setLastModifiedDate(this.LastModifiedDate);
        if (this.LastModifyingUser != null) ifcOwnerHistory.setLastModifyingUser(this.LastModifyingUser);
        if (this.LastModifyingApplication != null) ifcOwnerHistory.setLastModifyingApplication(this.LastModifyingApplication);
        if (this.CreationDate != null) ifcOwnerHistory.setCreationDate(this.CreationDate);
        return ifcOwnerHistory;
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
