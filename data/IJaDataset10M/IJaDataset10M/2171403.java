package de.schwartzonline.gmf.mavendependencies.impl;

import de.schwartzonline.gmf.mavendependencies.MavenDependenciesPackage;
import de.schwartzonline.gmf.mavendependencies.Project;
import de.schwartzonline.gmf.mavendependencies.ProjectReleation;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project Releation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link de.schwartzonline.gmf.mavendependencies.impl.ProjectReleationImpl#getProject <em>Project</em>}</li>
 *   <li>{@link de.schwartzonline.gmf.mavendependencies.impl.ProjectReleationImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class ProjectReleationImpl extends EObjectImpl implements ProjectReleation {

    /**
     * The cached value of the '{@link #getProject() <em>Project</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getProject()
     * @generated
     * @ordered
     */
    protected Project project = null;

    /**
     * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected static final String VERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getVersion()
     * @generated
     * @ordered
     */
    protected String version = VERSION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ProjectReleationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass() {
        return MavenDependenciesPackage.Literals.PROJECT_RELEATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Project getProject() {
        if (project != null && project.eIsProxy()) {
            InternalEObject oldProject = (InternalEObject) project;
            project = (Project) eResolveProxy(oldProject);
            if (project != oldProject) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, MavenDependenciesPackage.PROJECT_RELEATION__PROJECT, oldProject, project));
            }
        }
        return project;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Project basicGetProject() {
        return project;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setProject(Project newProject) {
        Project oldProject = project;
        project = newProject;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MavenDependenciesPackage.PROJECT_RELEATION__PROJECT, oldProject, project));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getVersion() {
        return version;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setVersion(String newVersion) {
        String oldVersion = version;
        version = newVersion;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, MavenDependenciesPackage.PROJECT_RELEATION__VERSION, oldVersion, version));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case MavenDependenciesPackage.PROJECT_RELEATION__PROJECT:
                if (resolve) return getProject();
                return basicGetProject();
            case MavenDependenciesPackage.PROJECT_RELEATION__VERSION:
                return getVersion();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case MavenDependenciesPackage.PROJECT_RELEATION__PROJECT:
                setProject((Project) newValue);
                return;
            case MavenDependenciesPackage.PROJECT_RELEATION__VERSION:
                setVersion((String) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(int featureID) {
        switch(featureID) {
            case MavenDependenciesPackage.PROJECT_RELEATION__PROJECT:
                setProject((Project) null);
                return;
            case MavenDependenciesPackage.PROJECT_RELEATION__VERSION:
                setVersion(VERSION_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(int featureID) {
        switch(featureID) {
            case MavenDependenciesPackage.PROJECT_RELEATION__PROJECT:
                return project != null;
            case MavenDependenciesPackage.PROJECT_RELEATION__VERSION:
                return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String toString() {
        if (eIsProxy()) return super.toString();
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (version: ");
        result.append(version);
        result.append(')');
        return result.toString();
    }
}
