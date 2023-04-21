package net.sf.rcpforms.formDsl.impl;

import java.util.Collection;
import net.sf.rcpforms.formDsl.Composite;
import net.sf.rcpforms.formDsl.FormDslPackage;
import net.sf.rcpforms.formDsl.FormPart;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Form Part</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.sf.rcpforms.formDsl.impl.FormPartImpl#getInput <em>Input</em>}</li>
 *   <li>{@link net.sf.rcpforms.formDsl.impl.FormPartImpl#getColumns <em>Columns</em>}</li>
 *   <li>{@link net.sf.rcpforms.formDsl.impl.FormPartImpl#getDefaultBuilderMethod <em>Default Builder Method</em>}</li>
 *   <li>{@link net.sf.rcpforms.formDsl.impl.FormPartImpl#getComposite <em>Composite</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FormPartImpl extends TypeImpl implements FormPart {

    /**
   * The cached value of the '{@link #getInput() <em>Input</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInput()
   * @generated
   * @ordered
   */
    protected EList<String> input;

    /**
   * The default value of the '{@link #getColumns() <em>Columns</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColumns()
   * @generated
   * @ordered
   */
    protected static final int COLUMNS_EDEFAULT = 0;

    /**
   * The cached value of the '{@link #getColumns() <em>Columns</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColumns()
   * @generated
   * @ordered
   */
    protected int columns = COLUMNS_EDEFAULT;

    /**
   * The default value of the '{@link #getDefaultBuilderMethod() <em>Default Builder Method</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultBuilderMethod()
   * @generated
   * @ordered
   */
    protected static final String DEFAULT_BUILDER_METHOD_EDEFAULT = null;

    /**
   * The cached value of the '{@link #getDefaultBuilderMethod() <em>Default Builder Method</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultBuilderMethod()
   * @generated
   * @ordered
   */
    protected String defaultBuilderMethod = DEFAULT_BUILDER_METHOD_EDEFAULT;

    /**
   * The cached value of the '{@link #getComposite() <em>Composite</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComposite()
   * @generated
   * @ordered
   */
    protected Composite composite;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected FormPartImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return FormDslPackage.Literals.FORM_PART;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public EList<String> getInput() {
        if (input == null) {
            input = new EDataTypeEList<String>(String.class, this, FormDslPackage.FORM_PART__INPUT);
        }
        return input;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public int getColumns() {
        return columns;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setColumns(int newColumns) {
        int oldColumns = columns;
        columns = newColumns;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, FormDslPackage.FORM_PART__COLUMNS, oldColumns, columns));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public String getDefaultBuilderMethod() {
        return defaultBuilderMethod;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setDefaultBuilderMethod(String newDefaultBuilderMethod) {
        String oldDefaultBuilderMethod = defaultBuilderMethod;
        defaultBuilderMethod = newDefaultBuilderMethod;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, FormDslPackage.FORM_PART__DEFAULT_BUILDER_METHOD, oldDefaultBuilderMethod, defaultBuilderMethod));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Composite getComposite() {
        return composite;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetComposite(Composite newComposite, NotificationChain msgs) {
        Composite oldComposite = composite;
        composite = newComposite;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FormDslPackage.FORM_PART__COMPOSITE, oldComposite, newComposite);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setComposite(Composite newComposite) {
        if (newComposite != composite) {
            NotificationChain msgs = null;
            if (composite != null) msgs = ((InternalEObject) composite).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FormDslPackage.FORM_PART__COMPOSITE, null, msgs);
            if (newComposite != null) msgs = ((InternalEObject) newComposite).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FormDslPackage.FORM_PART__COMPOSITE, null, msgs);
            msgs = basicSetComposite(newComposite, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, FormDslPackage.FORM_PART__COMPOSITE, newComposite, newComposite));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case FormDslPackage.FORM_PART__COMPOSITE:
                return basicSetComposite(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch(featureID) {
            case FormDslPackage.FORM_PART__INPUT:
                return getInput();
            case FormDslPackage.FORM_PART__COLUMNS:
                return getColumns();
            case FormDslPackage.FORM_PART__DEFAULT_BUILDER_METHOD:
                return getDefaultBuilderMethod();
            case FormDslPackage.FORM_PART__COMPOSITE:
                return getComposite();
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
            case FormDslPackage.FORM_PART__INPUT:
                getInput().clear();
                getInput().addAll((Collection<? extends String>) newValue);
                return;
            case FormDslPackage.FORM_PART__COLUMNS:
                setColumns((Integer) newValue);
                return;
            case FormDslPackage.FORM_PART__DEFAULT_BUILDER_METHOD:
                setDefaultBuilderMethod((String) newValue);
                return;
            case FormDslPackage.FORM_PART__COMPOSITE:
                setComposite((Composite) newValue);
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
            case FormDslPackage.FORM_PART__INPUT:
                getInput().clear();
                return;
            case FormDslPackage.FORM_PART__COLUMNS:
                setColumns(COLUMNS_EDEFAULT);
                return;
            case FormDslPackage.FORM_PART__DEFAULT_BUILDER_METHOD:
                setDefaultBuilderMethod(DEFAULT_BUILDER_METHOD_EDEFAULT);
                return;
            case FormDslPackage.FORM_PART__COMPOSITE:
                setComposite((Composite) null);
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
            case FormDslPackage.FORM_PART__INPUT:
                return input != null && !input.isEmpty();
            case FormDslPackage.FORM_PART__COLUMNS:
                return columns != COLUMNS_EDEFAULT;
            case FormDslPackage.FORM_PART__DEFAULT_BUILDER_METHOD:
                return DEFAULT_BUILDER_METHOD_EDEFAULT == null ? defaultBuilderMethod != null : !DEFAULT_BUILDER_METHOD_EDEFAULT.equals(defaultBuilderMethod);
            case FormDslPackage.FORM_PART__COMPOSITE:
                return composite != null;
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
        result.append(" (input: ");
        result.append(input);
        result.append(", columns: ");
        result.append(columns);
        result.append(", defaultBuilderMethod: ");
        result.append(defaultBuilderMethod);
        result.append(')');
        return result.toString();
    }
}
