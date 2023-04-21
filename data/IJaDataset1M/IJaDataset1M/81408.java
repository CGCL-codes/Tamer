package Leveleditor;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Room</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link Leveleditor.Room#getName <em>Name</em>}</li>
 *   <li>{@link Leveleditor.Room#getDescription <em>Description</em>}</li>
 *   <li>{@link Leveleditor.Room#getPostDescription <em>Post Description</em>}</li>
 *   <li>{@link Leveleditor.Room#getPostItemsDescription <em>Post Items Description</em>}</li>
 *   <li>{@link Leveleditor.Room#getRevealedDescription <em>Revealed Description</em>}</li>
 *   <li>{@link Leveleditor.Room#getHiddenDescription <em>Hidden Description</em>}</li>
 *   <li>{@link Leveleditor.Room#getEnemies <em>Enemies</em>}</li>
 *   <li>{@link Leveleditor.Room#getNpcs <em>Npcs</em>}</li>
 *   <li>{@link Leveleditor.Room#getItems <em>Items</em>}</li>
 *   <li>{@link Leveleditor.Room#isIsLocked <em>Is Locked</em>}</li>
 *   <li>{@link Leveleditor.Room#getUnlockKeys <em>Unlock Keys</em>}</li>
 *   <li>{@link Leveleditor.Room#isIsHidden <em>Is Hidden</em>}</li>
 *   <li>{@link Leveleditor.Room#isIsRevealed <em>Is Revealed</em>}</li>
 *   <li>{@link Leveleditor.Room#isIsHiddenEvent <em>Is Hidden Event</em>}</li>
 *   <li>{@link Leveleditor.Room#getNorth <em>North</em>}</li>
 *   <li>{@link Leveleditor.Room#getSouth <em>South</em>}</li>
 *   <li>{@link Leveleditor.Room#getWest <em>West</em>}</li>
 *   <li>{@link Leveleditor.Room#getEast <em>East</em>}</li>
 *   <li>{@link Leveleditor.Room#isAutosave <em>Autosave</em>}</li>
 * </ul>
 * </p>
 *
 * @see Leveleditor.LeveleditorPackage#getRoom()
 * @model abstract="true"
 * @generated
 */
public interface Room extends EObject {

    /**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see Leveleditor.LeveleditorPackage#getRoom_Name()
	 * @model
	 * @generated
	 */
    String getName();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
    void setName(String value);

    /**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see Leveleditor.LeveleditorPackage#getRoom_Description()
	 * @model
	 * @generated
	 */
    String getDescription();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
    void setDescription(String value);

    /**
	 * Returns the value of the '<em><b>Post Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Post Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Post Description</em>' attribute.
	 * @see #setPostDescription(String)
	 * @see Leveleditor.LeveleditorPackage#getRoom_PostDescription()
	 * @model
	 * @generated
	 */
    String getPostDescription();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getPostDescription <em>Post Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Post Description</em>' attribute.
	 * @see #getPostDescription()
	 * @generated
	 */
    void setPostDescription(String value);

    /**
	 * Returns the value of the '<em><b>Post Items Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Post Items Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Post Items Description</em>' attribute.
	 * @see #setPostItemsDescription(String)
	 * @see Leveleditor.LeveleditorPackage#getRoom_PostItemsDescription()
	 * @model
	 * @generated
	 */
    String getPostItemsDescription();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getPostItemsDescription <em>Post Items Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Post Items Description</em>' attribute.
	 * @see #getPostItemsDescription()
	 * @generated
	 */
    void setPostItemsDescription(String value);

    /**
	 * Returns the value of the '<em><b>Revealed Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Revealed Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Revealed Description</em>' attribute.
	 * @see #setRevealedDescription(String)
	 * @see Leveleditor.LeveleditorPackage#getRoom_RevealedDescription()
	 * @model
	 * @generated
	 */
    String getRevealedDescription();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getRevealedDescription <em>Revealed Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Revealed Description</em>' attribute.
	 * @see #getRevealedDescription()
	 * @generated
	 */
    void setRevealedDescription(String value);

    /**
	 * Returns the value of the '<em><b>Hidden Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hidden Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hidden Description</em>' attribute.
	 * @see #setHiddenDescription(String)
	 * @see Leveleditor.LeveleditorPackage#getRoom_HiddenDescription()
	 * @model
	 * @generated
	 */
    String getHiddenDescription();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getHiddenDescription <em>Hidden Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hidden Description</em>' attribute.
	 * @see #getHiddenDescription()
	 * @generated
	 */
    void setHiddenDescription(String value);

    /**
	 * Returns the value of the '<em><b>Enemies</b></em>' containment reference list.
	 * The list contents are of type {@link Leveleditor.Enemy}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enemies</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enemies</em>' containment reference list.
	 * @see Leveleditor.LeveleditorPackage#getRoom_Enemies()
	 * @model containment="true"
	 * @generated
	 */
    EList<Enemy> getEnemies();

    /**
	 * Returns the value of the '<em><b>Npcs</b></em>' containment reference list.
	 * The list contents are of type {@link Leveleditor.NPC}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Npcs</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Npcs</em>' containment reference list.
	 * @see Leveleditor.LeveleditorPackage#getRoom_Npcs()
	 * @model containment="true"
	 * @generated
	 */
    EList<NPC> getNpcs();

    /**
	 * Returns the value of the '<em><b>Items</b></em>' containment reference list.
	 * The list contents are of type {@link Leveleditor.Item}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Items</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Items</em>' containment reference list.
	 * @see Leveleditor.LeveleditorPackage#getRoom_Items()
	 * @model containment="true"
	 * @generated
	 */
    EList<Item> getItems();

    /**
	 * Returns the value of the '<em><b>North</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>North</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>North</em>' reference.
	 * @see #setNorth(Room)
	 * @see Leveleditor.LeveleditorPackage#getRoom_North()
	 * @model
	 * @generated
	 */
    Room getNorth();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getNorth <em>North</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>North</em>' reference.
	 * @see #getNorth()
	 * @generated
	 */
    void setNorth(Room value);

    /**
	 * Returns the value of the '<em><b>South</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>South</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>South</em>' reference.
	 * @see #setSouth(Room)
	 * @see Leveleditor.LeveleditorPackage#getRoom_South()
	 * @model
	 * @generated
	 */
    Room getSouth();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getSouth <em>South</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>South</em>' reference.
	 * @see #getSouth()
	 * @generated
	 */
    void setSouth(Room value);

    /**
	 * Returns the value of the '<em><b>East</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>East</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>East</em>' reference.
	 * @see #setEast(Room)
	 * @see Leveleditor.LeveleditorPackage#getRoom_East()
	 * @model
	 * @generated
	 */
    Room getEast();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getEast <em>East</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>East</em>' reference.
	 * @see #getEast()
	 * @generated
	 */
    void setEast(Room value);

    /**
	 * Returns the value of the '<em><b>Autosave</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Autosave</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Autosave</em>' attribute.
	 * @see #setAutosave(boolean)
	 * @see Leveleditor.LeveleditorPackage#getRoom_Autosave()
	 * @model default="false"
	 * @generated
	 */
    boolean isAutosave();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#isAutosave <em>Autosave</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Autosave</em>' attribute.
	 * @see #isAutosave()
	 * @generated
	 */
    void setAutosave(boolean value);

    /**
	 * Returns the value of the '<em><b>West</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>West</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>West</em>' reference.
	 * @see #setWest(Room)
	 * @see Leveleditor.LeveleditorPackage#getRoom_West()
	 * @model
	 * @generated
	 */
    Room getWest();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#getWest <em>West</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>West</em>' reference.
	 * @see #getWest()
	 * @generated
	 */
    void setWest(Room value);

    /**
	 * Returns the value of the '<em><b>Is Locked</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Locked</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Locked</em>' attribute.
	 * @see #setIsLocked(boolean)
	 * @see Leveleditor.LeveleditorPackage#getRoom_IsLocked()
	 * @model default="false"
	 * @generated
	 */
    boolean isIsLocked();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#isIsLocked <em>Is Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Locked</em>' attribute.
	 * @see #isIsLocked()
	 * @generated
	 */
    void setIsLocked(boolean value);

    /**
	 * Returns the value of the '<em><b>Unlock Keys</b></em>' reference list.
	 * The list contents are of type {@link Leveleditor.Key}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unlock Keys</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unlock Keys</em>' reference list.
	 * @see Leveleditor.LeveleditorPackage#getRoom_UnlockKeys()
	 * @model
	 * @generated
	 */
    EList<Key> getUnlockKeys();

    /**
	 * Returns the value of the '<em><b>Is Hidden</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Hidden</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Hidden</em>' attribute.
	 * @see #setIsHidden(boolean)
	 * @see Leveleditor.LeveleditorPackage#getRoom_IsHidden()
	 * @model default="false"
	 * @generated
	 */
    boolean isIsHidden();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#isIsHidden <em>Is Hidden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Hidden</em>' attribute.
	 * @see #isIsHidden()
	 * @generated
	 */
    void setIsHidden(boolean value);

    /**
	 * Returns the value of the '<em><b>Is Revealed</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Revealed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Revealed</em>' attribute.
	 * @see #setIsRevealed(boolean)
	 * @see Leveleditor.LeveleditorPackage#getRoom_IsRevealed()
	 * @model default="false"
	 * @generated
	 */
    boolean isIsRevealed();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#isIsRevealed <em>Is Revealed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Revealed</em>' attribute.
	 * @see #isIsRevealed()
	 * @generated
	 */
    void setIsRevealed(boolean value);

    /**
	 * Returns the value of the '<em><b>Is Hidden Event</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Is Hidden Event</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Hidden Event</em>' attribute.
	 * @see #setIsHiddenEvent(boolean)
	 * @see Leveleditor.LeveleditorPackage#getRoom_IsHiddenEvent()
	 * @model default="false"
	 * @generated
	 */
    boolean isIsHiddenEvent();

    /**
	 * Sets the value of the '{@link Leveleditor.Room#isIsHiddenEvent <em>Is Hidden Event</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Hidden Event</em>' attribute.
	 * @see #isIsHiddenEvent()
	 * @generated
	 */
    void setIsHiddenEvent(boolean value);
}
