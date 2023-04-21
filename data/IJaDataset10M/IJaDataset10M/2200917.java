package org.eclipse.epf.uma._1._0.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.epf.uma._1._0.PracticeDescription;
import org.eclipse.epf.uma._1._0._0Package;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Practice Description</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.PracticeDescriptionImpl#getAdditionalInfo <em>Additional Info</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.PracticeDescriptionImpl#getApplication <em>Application</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.PracticeDescriptionImpl#getBackground <em>Background</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.PracticeDescriptionImpl#getGoals <em>Goals</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.PracticeDescriptionImpl#getLevelsOfAdoption <em>Levels Of Adoption</em>}</li>
 *   <li>{@link org.eclipse.epf.uma._1._0.impl.PracticeDescriptionImpl#getProblem <em>Problem</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PracticeDescriptionImpl extends ContentDescriptionImpl implements PracticeDescription {

    /**
	 * The default value of the '{@link #getAdditionalInfo() <em>Additional Info</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalInfo()
	 * @generated
	 * @ordered
	 */
    protected static final String ADDITIONAL_INFO_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getAdditionalInfo() <em>Additional Info</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalInfo()
	 * @generated
	 * @ordered
	 */
    protected String additionalInfo = ADDITIONAL_INFO_EDEFAULT;

    /**
	 * The default value of the '{@link #getApplication() <em>Application</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getApplication()
	 * @generated
	 * @ordered
	 */
    protected static final String APPLICATION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getApplication() <em>Application</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getApplication()
	 * @generated
	 * @ordered
	 */
    protected String application = APPLICATION_EDEFAULT;

    /**
	 * The default value of the '{@link #getBackground() <em>Background</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBackground()
	 * @generated
	 * @ordered
	 */
    protected static final String BACKGROUND_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getBackground() <em>Background</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBackground()
	 * @generated
	 * @ordered
	 */
    protected String background = BACKGROUND_EDEFAULT;

    /**
	 * The default value of the '{@link #getGoals() <em>Goals</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGoals()
	 * @generated
	 * @ordered
	 */
    protected static final String GOALS_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getGoals() <em>Goals</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGoals()
	 * @generated
	 * @ordered
	 */
    protected String goals = GOALS_EDEFAULT;

    /**
	 * The default value of the '{@link #getLevelsOfAdoption() <em>Levels Of Adoption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLevelsOfAdoption()
	 * @generated
	 * @ordered
	 */
    protected static final String LEVELS_OF_ADOPTION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getLevelsOfAdoption() <em>Levels Of Adoption</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLevelsOfAdoption()
	 * @generated
	 * @ordered
	 */
    protected String levelsOfAdoption = LEVELS_OF_ADOPTION_EDEFAULT;

    /**
	 * The default value of the '{@link #getProblem() <em>Problem</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProblem()
	 * @generated
	 * @ordered
	 */
    protected static final String PROBLEM_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getProblem() <em>Problem</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProblem()
	 * @generated
	 * @ordered
	 */
    protected String problem = PROBLEM_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected PracticeDescriptionImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return _0Package.Literals.PRACTICE_DESCRIPTION;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAdditionalInfo(String newAdditionalInfo) {
        String oldAdditionalInfo = additionalInfo;
        additionalInfo = newAdditionalInfo;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.PRACTICE_DESCRIPTION__ADDITIONAL_INFO, oldAdditionalInfo, additionalInfo));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getApplication() {
        return application;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setApplication(String newApplication) {
        String oldApplication = application;
        application = newApplication;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.PRACTICE_DESCRIPTION__APPLICATION, oldApplication, application));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getBackground() {
        return background;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setBackground(String newBackground) {
        String oldBackground = background;
        background = newBackground;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.PRACTICE_DESCRIPTION__BACKGROUND, oldBackground, background));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getGoals() {
        return goals;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setGoals(String newGoals) {
        String oldGoals = goals;
        goals = newGoals;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.PRACTICE_DESCRIPTION__GOALS, oldGoals, goals));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getLevelsOfAdoption() {
        return levelsOfAdoption;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLevelsOfAdoption(String newLevelsOfAdoption) {
        String oldLevelsOfAdoption = levelsOfAdoption;
        levelsOfAdoption = newLevelsOfAdoption;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.PRACTICE_DESCRIPTION__LEVELS_OF_ADOPTION, oldLevelsOfAdoption, levelsOfAdoption));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getProblem() {
        return problem;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setProblem(String newProblem) {
        String oldProblem = problem;
        problem = newProblem;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, _0Package.PRACTICE_DESCRIPTION__PROBLEM, oldProblem, problem));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case _0Package.PRACTICE_DESCRIPTION__ADDITIONAL_INFO:
                return getAdditionalInfo();
            case _0Package.PRACTICE_DESCRIPTION__APPLICATION:
                return getApplication();
            case _0Package.PRACTICE_DESCRIPTION__BACKGROUND:
                return getBackground();
            case _0Package.PRACTICE_DESCRIPTION__GOALS:
                return getGoals();
            case _0Package.PRACTICE_DESCRIPTION__LEVELS_OF_ADOPTION:
                return getLevelsOfAdoption();
            case _0Package.PRACTICE_DESCRIPTION__PROBLEM:
                return getProblem();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case _0Package.PRACTICE_DESCRIPTION__ADDITIONAL_INFO:
                setAdditionalInfo((String) newValue);
                return;
            case _0Package.PRACTICE_DESCRIPTION__APPLICATION:
                setApplication((String) newValue);
                return;
            case _0Package.PRACTICE_DESCRIPTION__BACKGROUND:
                setBackground((String) newValue);
                return;
            case _0Package.PRACTICE_DESCRIPTION__GOALS:
                setGoals((String) newValue);
                return;
            case _0Package.PRACTICE_DESCRIPTION__LEVELS_OF_ADOPTION:
                setLevelsOfAdoption((String) newValue);
                return;
            case _0Package.PRACTICE_DESCRIPTION__PROBLEM:
                setProblem((String) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public void eUnset(int featureID) {
        switch(featureID) {
            case _0Package.PRACTICE_DESCRIPTION__ADDITIONAL_INFO:
                setAdditionalInfo(ADDITIONAL_INFO_EDEFAULT);
                return;
            case _0Package.PRACTICE_DESCRIPTION__APPLICATION:
                setApplication(APPLICATION_EDEFAULT);
                return;
            case _0Package.PRACTICE_DESCRIPTION__BACKGROUND:
                setBackground(BACKGROUND_EDEFAULT);
                return;
            case _0Package.PRACTICE_DESCRIPTION__GOALS:
                setGoals(GOALS_EDEFAULT);
                return;
            case _0Package.PRACTICE_DESCRIPTION__LEVELS_OF_ADOPTION:
                setLevelsOfAdoption(LEVELS_OF_ADOPTION_EDEFAULT);
                return;
            case _0Package.PRACTICE_DESCRIPTION__PROBLEM:
                setProblem(PROBLEM_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case _0Package.PRACTICE_DESCRIPTION__ADDITIONAL_INFO:
                return ADDITIONAL_INFO_EDEFAULT == null ? additionalInfo != null : !ADDITIONAL_INFO_EDEFAULT.equals(additionalInfo);
            case _0Package.PRACTICE_DESCRIPTION__APPLICATION:
                return APPLICATION_EDEFAULT == null ? application != null : !APPLICATION_EDEFAULT.equals(application);
            case _0Package.PRACTICE_DESCRIPTION__BACKGROUND:
                return BACKGROUND_EDEFAULT == null ? background != null : !BACKGROUND_EDEFAULT.equals(background);
            case _0Package.PRACTICE_DESCRIPTION__GOALS:
                return GOALS_EDEFAULT == null ? goals != null : !GOALS_EDEFAULT.equals(goals);
            case _0Package.PRACTICE_DESCRIPTION__LEVELS_OF_ADOPTION:
                return LEVELS_OF_ADOPTION_EDEFAULT == null ? levelsOfAdoption != null : !LEVELS_OF_ADOPTION_EDEFAULT.equals(levelsOfAdoption);
            case _0Package.PRACTICE_DESCRIPTION__PROBLEM:
                return PROBLEM_EDEFAULT == null ? problem != null : !PROBLEM_EDEFAULT.equals(problem);
        }
        return super.eIsSet(featureID);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (additionalInfo: ");
        result.append(additionalInfo);
        result.append(", application: ");
        result.append(application);
        result.append(", background: ");
        result.append(background);
        result.append(", goals: ");
        result.append(goals);
        result.append(", levelsOfAdoption: ");
        result.append(levelsOfAdoption);
        result.append(", problem: ");
        result.append(problem);
        result.append(')');
        return result.toString();
    }
}
