package fr.univartois.cril.xtext.als.impl;

import fr.univartois.cril.xtext.als.Alias;
import fr.univartois.cril.xtext.als.AlsPackage;
import fr.univartois.cril.xtext.als.AssertionName;
import fr.univartois.cril.xtext.als.Block;
import fr.univartois.cril.xtext.als.CheckCommand;
import fr.univartois.cril.xtext.als.Scope;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Check Command</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link fr.univartois.cril.xtext.als.impl.CheckCommandImpl#getCheckName <em>Check Name</em>}</li>
 *   <li>{@link fr.univartois.cril.xtext.als.impl.CheckCommandImpl#getName <em>Name</em>}</li>
 *   <li>{@link fr.univartois.cril.xtext.als.impl.CheckCommandImpl#getBlock <em>Block</em>}</li>
 *   <li>{@link fr.univartois.cril.xtext.als.impl.CheckCommandImpl#getScope <em>Scope</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CheckCommandImpl extends ParagraphImpl implements CheckCommand {

    /**
   * The cached value of the '{@link #getCheckName() <em>Check Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCheckName()
   * @generated
   * @ordered
   */
    protected Alias checkName;

    /**
   * The cached value of the '{@link #getName() <em>Name</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
    protected AssertionName name;

    /**
   * The cached value of the '{@link #getBlock() <em>Block</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBlock()
   * @generated
   * @ordered
   */
    protected Block block;

    /**
   * The cached value of the '{@link #getScope() <em>Scope</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getScope()
   * @generated
   * @ordered
   */
    protected Scope scope;

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    protected CheckCommandImpl() {
        super();
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    protected EClass eStaticClass() {
        return AlsPackage.Literals.CHECK_COMMAND;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Alias getCheckName() {
        return checkName;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetCheckName(Alias newCheckName, NotificationChain msgs) {
        Alias oldCheckName = checkName;
        checkName = newCheckName;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AlsPackage.CHECK_COMMAND__CHECK_NAME, oldCheckName, newCheckName);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setCheckName(Alias newCheckName) {
        if (newCheckName != checkName) {
            NotificationChain msgs = null;
            if (checkName != null) msgs = ((InternalEObject) checkName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AlsPackage.CHECK_COMMAND__CHECK_NAME, null, msgs);
            if (newCheckName != null) msgs = ((InternalEObject) newCheckName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AlsPackage.CHECK_COMMAND__CHECK_NAME, null, msgs);
            msgs = basicSetCheckName(newCheckName, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlsPackage.CHECK_COMMAND__CHECK_NAME, newCheckName, newCheckName));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AssertionName getName() {
        if (name != null && name.eIsProxy()) {
            InternalEObject oldName = (InternalEObject) name;
            name = (AssertionName) eResolveProxy(oldName);
            if (name != oldName) {
                if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.RESOLVE, AlsPackage.CHECK_COMMAND__NAME, oldName, name));
            }
        }
        return name;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public AssertionName basicGetName() {
        return name;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setName(AssertionName newName) {
        AssertionName oldName = name;
        name = newName;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlsPackage.CHECK_COMMAND__NAME, oldName, name));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Block getBlock() {
        return block;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetBlock(Block newBlock, NotificationChain msgs) {
        Block oldBlock = block;
        block = newBlock;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AlsPackage.CHECK_COMMAND__BLOCK, oldBlock, newBlock);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setBlock(Block newBlock) {
        if (newBlock != block) {
            NotificationChain msgs = null;
            if (block != null) msgs = ((InternalEObject) block).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AlsPackage.CHECK_COMMAND__BLOCK, null, msgs);
            if (newBlock != null) msgs = ((InternalEObject) newBlock).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AlsPackage.CHECK_COMMAND__BLOCK, null, msgs);
            msgs = basicSetBlock(newBlock, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlsPackage.CHECK_COMMAND__BLOCK, newBlock, newBlock));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public Scope getScope() {
        return scope;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public NotificationChain basicSetScope(Scope newScope, NotificationChain msgs) {
        Scope oldScope = scope;
        scope = newScope;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AlsPackage.CHECK_COMMAND__SCOPE, oldScope, newScope);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    public void setScope(Scope newScope) {
        if (newScope != scope) {
            NotificationChain msgs = null;
            if (scope != null) msgs = ((InternalEObject) scope).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AlsPackage.CHECK_COMMAND__SCOPE, null, msgs);
            if (newScope != null) msgs = ((InternalEObject) newScope).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AlsPackage.CHECK_COMMAND__SCOPE, null, msgs);
            msgs = basicSetScope(newScope, msgs);
            if (msgs != null) msgs.dispatch();
        } else if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, AlsPackage.CHECK_COMMAND__SCOPE, newScope, newScope));
    }

    /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case AlsPackage.CHECK_COMMAND__CHECK_NAME:
                return basicSetCheckName(null, msgs);
            case AlsPackage.CHECK_COMMAND__BLOCK:
                return basicSetBlock(null, msgs);
            case AlsPackage.CHECK_COMMAND__SCOPE:
                return basicSetScope(null, msgs);
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
            case AlsPackage.CHECK_COMMAND__CHECK_NAME:
                return getCheckName();
            case AlsPackage.CHECK_COMMAND__NAME:
                if (resolve) return getName();
                return basicGetName();
            case AlsPackage.CHECK_COMMAND__BLOCK:
                return getBlock();
            case AlsPackage.CHECK_COMMAND__SCOPE:
                return getScope();
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
            case AlsPackage.CHECK_COMMAND__CHECK_NAME:
                setCheckName((Alias) newValue);
                return;
            case AlsPackage.CHECK_COMMAND__NAME:
                setName((AssertionName) newValue);
                return;
            case AlsPackage.CHECK_COMMAND__BLOCK:
                setBlock((Block) newValue);
                return;
            case AlsPackage.CHECK_COMMAND__SCOPE:
                setScope((Scope) newValue);
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
            case AlsPackage.CHECK_COMMAND__CHECK_NAME:
                setCheckName((Alias) null);
                return;
            case AlsPackage.CHECK_COMMAND__NAME:
                setName((AssertionName) null);
                return;
            case AlsPackage.CHECK_COMMAND__BLOCK:
                setBlock((Block) null);
                return;
            case AlsPackage.CHECK_COMMAND__SCOPE:
                setScope((Scope) null);
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
            case AlsPackage.CHECK_COMMAND__CHECK_NAME:
                return checkName != null;
            case AlsPackage.CHECK_COMMAND__NAME:
                return name != null;
            case AlsPackage.CHECK_COMMAND__BLOCK:
                return block != null;
            case AlsPackage.CHECK_COMMAND__SCOPE:
                return scope != null;
        }
        return super.eIsSet(featureID);
    }
}
