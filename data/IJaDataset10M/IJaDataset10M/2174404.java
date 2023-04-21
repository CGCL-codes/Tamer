package org.torusfw.buckytools.repository.component.impl;

import org.eclipse.buckminster.core.cspec.builder.CSpecBuilder;
import org.eclipse.buckminster.core.cspec.model.AttributeAlreadyDefinedException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.torusfw.buckytools.repository.component.LocalRepoComponent;
import org.torusfw.buckytools.repository.component.RepoPackage;
import org.torusfw.buckytools.core.component.SpecBuildingException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Local Repo Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.torusfw.buckytools.repository.component.impl.LocalRepoComponentImpl#getRepoOffset <em>Repo Offset</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LocalRepoComponentImpl extends RepoComponentImpl implements LocalRepoComponent {

    /**
     * The default value of the '{@link #getRepoOffset() <em>Repo Offset</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRepoOffset()
     * @generated
     * @ordered
     */
    protected static final String REPO_OFFSET_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getRepoOffset() <em>Repo Offset</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getRepoOffset()
     * @generated
     * @ordered
     */
    protected String repoOffset = REPO_OFFSET_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected LocalRepoComponentImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RepoPackage.Literals.LOCAL_REPO_COMPONENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getRepoOffset() {
        return repoOffset;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setRepoOffset(String newRepoOffset) {
        String oldRepoOffset = repoOffset;
        repoOffset = newRepoOffset;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, RepoPackage.LOCAL_REPO_COMPONENT__REPO_OFFSET, oldRepoOffset, repoOffset));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case RepoPackage.LOCAL_REPO_COMPONENT__REPO_OFFSET:
                return getRepoOffset();
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
            case RepoPackage.LOCAL_REPO_COMPONENT__REPO_OFFSET:
                setRepoOffset((String) newValue);
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
            case RepoPackage.LOCAL_REPO_COMPONENT__REPO_OFFSET:
                setRepoOffset(REPO_OFFSET_EDEFAULT);
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
            case RepoPackage.LOCAL_REPO_COMPONENT__REPO_OFFSET:
                return REPO_OFFSET_EDEFAULT == null ? repoOffset != null : !REPO_OFFSET_EDEFAULT.equals(repoOffset);
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
        result.append(" (repoOffset: ");
        result.append(repoOffset);
        result.append(')');
        return result.toString();
    }

    @Override
    public void buildSpec(CSpecBuilder specBuilder) throws SpecBuildingException {
        super.buildSpec(specBuilder);
        try {
            specBuilder.addArtifact(REPOSITORY_ACTION_NAME, true, getRepoOutputPath());
        } catch (AttributeAlreadyDefinedException e) {
            throw new SpecBuildingException("Error while building spec for local repo component type", e);
        }
    }

    @Override
    public IPath getRepoOutputPath() {
        return super.getRepoOutputPath().append(getRepoOffset());
    }
}
