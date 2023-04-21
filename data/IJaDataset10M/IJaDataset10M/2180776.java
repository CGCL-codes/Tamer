package model.diagram.impl;

import java.util.Collection;
import model.collaboration.Collaborator;
import model.collaboration.Communication;
import model.diagram.CollaborationDiagram;
import model.diagram.DiagramPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Collaboration Diagram</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link model.diagram.impl.CollaborationDiagramImpl#getName <em>Name</em>}</li>
 *   <li>{@link model.diagram.impl.CollaborationDiagramImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link model.diagram.impl.CollaborationDiagramImpl#getCollaborator <em>Collaborator</em>}</li>
 *   <li>{@link model.diagram.impl.CollaborationDiagramImpl#getCommunication <em>Communication</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CollaborationDiagramImpl extends EObjectImpl implements CollaborationDiagram {

    /**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected static final String NAME_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
    protected String name = NAME_EDEFAULT;

    /**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected static final String DESCRIPTION_EDEFAULT = null;

    /**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
    protected String description = DESCRIPTION_EDEFAULT;

    /**
	 * The cached value of the '{@link #getCollaborator() <em>Collaborator</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollaborator()
	 * @generated
	 * @ordered
	 */
    protected EList<Collaborator> collaborator;

    /**
	 * The cached value of the '{@link #getCommunication() <em>Communication</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommunication()
	 * @generated
	 * @ordered
	 */
    protected EList<Communication> communication;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CollaborationDiagramImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return DiagramPackage.Literals.COLLABORATION_DIAGRAM;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getName() {
        return name;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DiagramPackage.COLLABORATION_DIAGRAM__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setDescription(String newDescription) {
        String oldDescription = description;
        description = newDescription;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, DiagramPackage.COLLABORATION_DIAGRAM__DESCRIPTION, oldDescription, description));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Collaborator> getCollaborator() {
        if (collaborator == null) {
            collaborator = new EObjectResolvingEList<Collaborator>(Collaborator.class, this, DiagramPackage.COLLABORATION_DIAGRAM__COLLABORATOR);
        }
        return collaborator;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Communication> getCommunication() {
        if (communication == null) {
            communication = new EObjectResolvingEList<Communication>(Communication.class, this, DiagramPackage.COLLABORATION_DIAGRAM__COMMUNICATION);
        }
        return communication;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case DiagramPackage.COLLABORATION_DIAGRAM__NAME:
                return getName();
            case DiagramPackage.COLLABORATION_DIAGRAM__DESCRIPTION:
                return getDescription();
            case DiagramPackage.COLLABORATION_DIAGRAM__COLLABORATOR:
                return getCollaborator();
            case DiagramPackage.COLLABORATION_DIAGRAM__COMMUNICATION:
                return getCommunication();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch(featureID) {
            case DiagramPackage.COLLABORATION_DIAGRAM__NAME:
                setName((String) newValue);
                return;
            case DiagramPackage.COLLABORATION_DIAGRAM__DESCRIPTION:
                setDescription((String) newValue);
                return;
            case DiagramPackage.COLLABORATION_DIAGRAM__COLLABORATOR:
                getCollaborator().clear();
                getCollaborator().addAll((Collection<? extends Collaborator>) newValue);
                return;
            case DiagramPackage.COLLABORATION_DIAGRAM__COMMUNICATION:
                getCommunication().clear();
                getCommunication().addAll((Collection<? extends Communication>) newValue);
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
            case DiagramPackage.COLLABORATION_DIAGRAM__NAME:
                setName(NAME_EDEFAULT);
                return;
            case DiagramPackage.COLLABORATION_DIAGRAM__DESCRIPTION:
                setDescription(DESCRIPTION_EDEFAULT);
                return;
            case DiagramPackage.COLLABORATION_DIAGRAM__COLLABORATOR:
                getCollaborator().clear();
                return;
            case DiagramPackage.COLLABORATION_DIAGRAM__COMMUNICATION:
                getCommunication().clear();
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
            case DiagramPackage.COLLABORATION_DIAGRAM__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case DiagramPackage.COLLABORATION_DIAGRAM__DESCRIPTION:
                return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
            case DiagramPackage.COLLABORATION_DIAGRAM__COLLABORATOR:
                return collaborator != null && !collaborator.isEmpty();
            case DiagramPackage.COLLABORATION_DIAGRAM__COMMUNICATION:
                return communication != null && !communication.isEmpty();
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
        result.append(" (name: ");
        result.append(name);
        result.append(", description: ");
        result.append(description);
        result.append(')');
        return result.toString();
    }
}
