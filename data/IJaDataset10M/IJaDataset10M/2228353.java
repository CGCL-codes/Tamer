package org.dengues.model.warehouse.impl;

import org.dengues.model.warehouse.BlockType;
import org.dengues.model.warehouse.WarehousePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Block Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.dengues.model.warehouse.impl.BlockTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.dengues.model.warehouse.impl.BlockTypeImpl#getUuid <em>Uuid</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BlockTypeImpl extends AbstractCompElementImpl implements BlockType {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final String copyright = "// ============================================================================\r\n//\r\n// Copyright (C) 2006-2007 Dengues\r\n//\r\n// Google Group: http://groups.google.com/group/dengues\r\n// QQ Group: 24885404\r\n//\r\n// This library is free software; you can redistribute it and/or\r\n// modify it under the terms of the GNU Lesser General Public\r\n// License as published by the Free Software Foundation; either\r\n// version 2.1 of the License.\r\n//\r\n// This library is distributed in the hope that it will be useful,\r\n// but WITHOUT ANY WARRANTY; without even the implied warranty of\r\n// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU\r\n// Lesser General Public License for more details.\r\n//\r\n// You should have received a copy of the GNU General Public License\r\n// along with this program; if not, write to the Free Software\r\n// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.\r\n//\r\n// ============================================================================\r\n";

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
     * The default value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUuid()
     * @generated
     * @ordered
     */
    protected static final String UUID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUuid() <em>Uuid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getUuid()
     * @generated
     * @ordered
     */
    protected String uuid = UUID_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected BlockTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return WarehousePackage.Literals.BLOCK_TYPE;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WarehousePackage.BLOCK_TYPE__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setUuid(String newUuid) {
        String oldUuid = uuid;
        uuid = newUuid;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, WarehousePackage.BLOCK_TYPE__UUID, oldUuid, uuid));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case WarehousePackage.BLOCK_TYPE__NAME:
                return getName();
            case WarehousePackage.BLOCK_TYPE__UUID:
                return getUuid();
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
            case WarehousePackage.BLOCK_TYPE__NAME:
                setName((String) newValue);
                return;
            case WarehousePackage.BLOCK_TYPE__UUID:
                setUuid((String) newValue);
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
            case WarehousePackage.BLOCK_TYPE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case WarehousePackage.BLOCK_TYPE__UUID:
                setUuid(UUID_EDEFAULT);
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
            case WarehousePackage.BLOCK_TYPE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case WarehousePackage.BLOCK_TYPE__UUID:
                return UUID_EDEFAULT == null ? uuid != null : !UUID_EDEFAULT.equals(uuid);
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
        result.append(", uuid: ");
        result.append(uuid);
        result.append(')');
        return result.toString();
    }
}
