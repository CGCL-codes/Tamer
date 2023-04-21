package org.pubcurator.model.overall.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.internal.cdo.CDOObjectImpl;
import org.pubcurator.model.overall.PubOverallPackage;
import org.pubcurator.model.overall.PubTreeNode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Pub Tree Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.pubcurator.model.overall.impl.PubTreeNodeImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.impl.PubTreeNodeImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link org.pubcurator.model.overall.impl.PubTreeNodeImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PubTreeNodeImpl extends CDOObjectImpl implements PubTreeNode {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected PubTreeNodeImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return PubOverallPackage.Literals.PUB_TREE_NODE;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected int eStaticFeatureCount() {
        return 0;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public PubTreeNode getParent() {
        return (PubTreeNode) eGet(PubOverallPackage.Literals.PUB_TREE_NODE__PARENT, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setParent(PubTreeNode newParent) {
        eSet(PubOverallPackage.Literals.PUB_TREE_NODE__PARENT, newParent);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @SuppressWarnings("unchecked")
    public EList<PubTreeNode> getChildren() {
        return (EList<PubTreeNode>) eGet(PubOverallPackage.Literals.PUB_TREE_NODE__CHILDREN, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EObject getValue() {
        return (EObject) eGet(PubOverallPackage.Literals.PUB_TREE_NODE__VALUE, true);
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setValue(EObject newValue) {
        eSet(PubOverallPackage.Literals.PUB_TREE_NODE__VALUE, newValue);
    }
}
