package Leveleditor;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enemy</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Leveleditor.Enemy#isIsAttacking <em>Is Attacking</em>}</li>
 *   <li>{@link Leveleditor.Enemy#getScarefactor <em>Scarefactor</em>}</li>
 *   <li>{@link Leveleditor.Enemy#getDamageFire <em>Damage Fire</em>}</li>
 *   <li>{@link Leveleditor.Enemy#getDamageLightning <em>Damage Lightning</em>}</li>
 *   <li>{@link Leveleditor.Enemy#getDamageIce <em>Damage Ice</em>}</li>
 *   <li>{@link Leveleditor.Enemy#getDamagePoison <em>Damage Poison</em>}</li>
 * </ul>
 * </p>
 *
 * @see Leveleditor.LeveleditorPackage#getEnemy()
 * @model
 * @generated
 */
public interface Enemy extends Creature {

    /**
	 * Returns the value of the '<em><b>Is Attacking</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Attacking</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Attacking</em>' attribute.
	 * @see #setIsAttacking(boolean)
	 * @see Leveleditor.LeveleditorPackage#getEnemy_IsAttacking()
	 * @model default="false"
	 * @generated
	 */
    boolean isIsAttacking();

    /**
	 * Sets the value of the '{@link Leveleditor.Enemy#isIsAttacking <em>Is Attacking</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Attacking</em>' attribute.
	 * @see #isIsAttacking()
	 * @generated
	 */
    void setIsAttacking(boolean value);

    /**
	 * Returns the value of the '<em><b>Scarefactor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scarefactor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scarefactor</em>' attribute.
	 * @see #setScarefactor(int)
	 * @see Leveleditor.LeveleditorPackage#getEnemy_Scarefactor()
	 * @model
	 * @generated
	 */
    int getScarefactor();

    /**
	 * Sets the value of the '{@link Leveleditor.Enemy#getScarefactor <em>Scarefactor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scarefactor</em>' attribute.
	 * @see #getScarefactor()
	 * @generated
	 */
    void setScarefactor(int value);

    /**
	 * Returns the value of the '<em><b>Damage Fire</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Damage Fire</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Damage Fire</em>' attribute.
	 * @see #setDamageFire(int)
	 * @see Leveleditor.LeveleditorPackage#getEnemy_DamageFire()
	 * @model default="0"
	 * @generated
	 */
    int getDamageFire();

    /**
	 * Sets the value of the '{@link Leveleditor.Enemy#getDamageFire <em>Damage Fire</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Damage Fire</em>' attribute.
	 * @see #getDamageFire()
	 * @generated
	 */
    void setDamageFire(int value);

    /**
	 * Returns the value of the '<em><b>Damage Lightning</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Damage Lightning</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Damage Lightning</em>' attribute.
	 * @see #setDamageLightning(int)
	 * @see Leveleditor.LeveleditorPackage#getEnemy_DamageLightning()
	 * @model default="0"
	 * @generated
	 */
    int getDamageLightning();

    /**
	 * Sets the value of the '{@link Leveleditor.Enemy#getDamageLightning <em>Damage Lightning</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Damage Lightning</em>' attribute.
	 * @see #getDamageLightning()
	 * @generated
	 */
    void setDamageLightning(int value);

    /**
	 * Returns the value of the '<em><b>Damage Ice</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Damage Ice</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Damage Ice</em>' attribute.
	 * @see #setDamageIce(int)
	 * @see Leveleditor.LeveleditorPackage#getEnemy_DamageIce()
	 * @model default="0"
	 * @generated
	 */
    int getDamageIce();

    /**
	 * Sets the value of the '{@link Leveleditor.Enemy#getDamageIce <em>Damage Ice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Damage Ice</em>' attribute.
	 * @see #getDamageIce()
	 * @generated
	 */
    void setDamageIce(int value);

    /**
	 * Returns the value of the '<em><b>Damage Poison</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Damage Poison</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Damage Poison</em>' attribute.
	 * @see #setDamagePoison(int)
	 * @see Leveleditor.LeveleditorPackage#getEnemy_DamagePoison()
	 * @model default="0"
	 * @generated
	 */
    int getDamagePoison();

    /**
	 * Sets the value of the '{@link Leveleditor.Enemy#getDamagePoison <em>Damage Poison</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Damage Poison</em>' attribute.
	 * @see #getDamagePoison()
	 * @generated
	 */
    void setDamagePoison(int value);
}
