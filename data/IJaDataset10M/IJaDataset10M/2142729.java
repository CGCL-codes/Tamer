package Leveleditor.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import Leveleditor.Creature;
import Leveleditor.ECreatureClass;
import Leveleditor.ECreatureRace;
import Leveleditor.Item;
import Leveleditor.LeveleditorPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Creature</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getName <em>Name</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getInitHp <em>Init Hp</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getInitMana <em>Init Mana</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getMana <em>Mana</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getStrength <em>Strength</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getAgility <em>Agility</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getCharisma <em>Charisma</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getIntelligence <em>Intelligence</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getLevel <em>Level</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getInventory <em>Inventory</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getExperience <em>Experience</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getGold <em>Gold</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getInitPlayerActions <em>Init Player Actions</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getResistLightning <em>Resist Lightning</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getResistFire <em>Resist Fire</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getResistPoison <em>Resist Poison</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getResistIce <em>Resist Ice</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#isIsMale <em>Is Male</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getRace <em>Race</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getCreatureClass <em>Creature Class</em>}</li>
 *   <li>{@link Leveleditor.impl.CreatureImpl#getArmor <em>Armor</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class CreatureImpl extends EObjectImpl implements Creature {

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
	 * The default value of the '{@link #getInitHp() <em>Init Hp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitHp()
	 * @generated
	 * @ordered
	 */
    protected static final int INIT_HP_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getInitHp() <em>Init Hp</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitHp()
	 * @generated
	 * @ordered
	 */
    protected int initHp = INIT_HP_EDEFAULT;

    /**
	 * The default value of the '{@link #getInitMana() <em>Init Mana</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitMana()
	 * @generated
	 * @ordered
	 */
    protected static final int INIT_MANA_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getInitMana() <em>Init Mana</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitMana()
	 * @generated
	 * @ordered
	 */
    protected int initMana = INIT_MANA_EDEFAULT;

    /**
	 * The default value of the '{@link #getMana() <em>Mana</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMana()
	 * @generated
	 * @ordered
	 */
    protected static final int MANA_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getMana() <em>Mana</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMana()
	 * @generated
	 * @ordered
	 */
    protected int mana = MANA_EDEFAULT;

    /**
	 * The default value of the '{@link #getStrength() <em>Strength</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStrength()
	 * @generated
	 * @ordered
	 */
    protected static final int STRENGTH_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getStrength() <em>Strength</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStrength()
	 * @generated
	 * @ordered
	 */
    protected int strength = STRENGTH_EDEFAULT;

    /**
	 * The default value of the '{@link #getAgility() <em>Agility</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAgility()
	 * @generated
	 * @ordered
	 */
    protected static final int AGILITY_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getAgility() <em>Agility</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAgility()
	 * @generated
	 * @ordered
	 */
    protected int agility = AGILITY_EDEFAULT;

    /**
	 * The default value of the '{@link #getCharisma() <em>Charisma</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharisma()
	 * @generated
	 * @ordered
	 */
    protected static final int CHARISMA_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getCharisma() <em>Charisma</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharisma()
	 * @generated
	 * @ordered
	 */
    protected int charisma = CHARISMA_EDEFAULT;

    /**
	 * The default value of the '{@link #getIntelligence() <em>Intelligence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntelligence()
	 * @generated
	 * @ordered
	 */
    protected static final int INTELLIGENCE_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getIntelligence() <em>Intelligence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntelligence()
	 * @generated
	 * @ordered
	 */
    protected int intelligence = INTELLIGENCE_EDEFAULT;

    /**
	 * The default value of the '{@link #getLevel() <em>Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLevel()
	 * @generated
	 * @ordered
	 */
    protected static final int LEVEL_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getLevel() <em>Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLevel()
	 * @generated
	 * @ordered
	 */
    protected int level = LEVEL_EDEFAULT;

    /**
	 * The cached value of the '{@link #getInventory() <em>Inventory</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInventory()
	 * @generated
	 * @ordered
	 */
    protected EList<Item> inventory;

    /**
	 * The default value of the '{@link #getExperience() <em>Experience</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExperience()
	 * @generated
	 * @ordered
	 */
    protected static final int EXPERIENCE_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getExperience() <em>Experience</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExperience()
	 * @generated
	 * @ordered
	 */
    protected int experience = EXPERIENCE_EDEFAULT;

    /**
	 * The default value of the '{@link #getGold() <em>Gold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGold()
	 * @generated
	 * @ordered
	 */
    protected static final int GOLD_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getGold() <em>Gold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGold()
	 * @generated
	 * @ordered
	 */
    protected int gold = GOLD_EDEFAULT;

    /**
	 * The default value of the '{@link #getInitPlayerActions() <em>Init Player Actions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitPlayerActions()
	 * @generated
	 * @ordered
	 */
    protected static final int INIT_PLAYER_ACTIONS_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getInitPlayerActions() <em>Init Player Actions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitPlayerActions()
	 * @generated
	 * @ordered
	 */
    protected int initPlayerActions = INIT_PLAYER_ACTIONS_EDEFAULT;

    /**
	 * The default value of the '{@link #getResistLightning() <em>Resist Lightning</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResistLightning()
	 * @generated
	 * @ordered
	 */
    protected static final int RESIST_LIGHTNING_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getResistLightning() <em>Resist Lightning</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResistLightning()
	 * @generated
	 * @ordered
	 */
    protected int resistLightning = RESIST_LIGHTNING_EDEFAULT;

    /**
	 * The default value of the '{@link #getResistFire() <em>Resist Fire</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResistFire()
	 * @generated
	 * @ordered
	 */
    protected static final int RESIST_FIRE_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getResistFire() <em>Resist Fire</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResistFire()
	 * @generated
	 * @ordered
	 */
    protected int resistFire = RESIST_FIRE_EDEFAULT;

    /**
	 * The default value of the '{@link #getResistPoison() <em>Resist Poison</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResistPoison()
	 * @generated
	 * @ordered
	 */
    protected static final int RESIST_POISON_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getResistPoison() <em>Resist Poison</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResistPoison()
	 * @generated
	 * @ordered
	 */
    protected int resistPoison = RESIST_POISON_EDEFAULT;

    /**
	 * The default value of the '{@link #getResistIce() <em>Resist Ice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResistIce()
	 * @generated
	 * @ordered
	 */
    protected static final int RESIST_ICE_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getResistIce() <em>Resist Ice</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResistIce()
	 * @generated
	 * @ordered
	 */
    protected int resistIce = RESIST_ICE_EDEFAULT;

    /**
	 * The default value of the '{@link #isIsMale() <em>Is Male</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsMale()
	 * @generated
	 * @ordered
	 */
    protected static final boolean IS_MALE_EDEFAULT = false;

    /**
	 * The cached value of the '{@link #isIsMale() <em>Is Male</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsMale()
	 * @generated
	 * @ordered
	 */
    protected boolean isMale = IS_MALE_EDEFAULT;

    /**
	 * The default value of the '{@link #getRace() <em>Race</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRace()
	 * @generated
	 * @ordered
	 */
    protected static final ECreatureRace RACE_EDEFAULT = ECreatureRace.HUMAN;

    /**
	 * The cached value of the '{@link #getRace() <em>Race</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRace()
	 * @generated
	 * @ordered
	 */
    protected ECreatureRace race = RACE_EDEFAULT;

    /**
	 * The default value of the '{@link #getCreatureClass() <em>Creature Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreatureClass()
	 * @generated
	 * @ordered
	 */
    protected static final ECreatureClass CREATURE_CLASS_EDEFAULT = ECreatureClass.WARRIOR;

    /**
	 * The cached value of the '{@link #getCreatureClass() <em>Creature Class</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreatureClass()
	 * @generated
	 * @ordered
	 */
    protected ECreatureClass creatureClass = CREATURE_CLASS_EDEFAULT;

    /**
	 * The default value of the '{@link #getArmor() <em>Armor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArmor()
	 * @generated
	 * @ordered
	 */
    protected static final int ARMOR_EDEFAULT = 0;

    /**
	 * The cached value of the '{@link #getArmor() <em>Armor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArmor()
	 * @generated
	 * @ordered
	 */
    protected int armor = ARMOR_EDEFAULT;

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected CreatureImpl() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
        return LeveleditorPackage.Literals.CREATURE;
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
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__NAME, oldName, name));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getInitHp() {
        return initHp;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInitHp(int newInitHp) {
        int oldInitHp = initHp;
        initHp = newInitHp;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__INIT_HP, oldInitHp, initHp));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getInitMana() {
        return initMana;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInitMana(int newInitMana) {
        int oldInitMana = initMana;
        initMana = newInitMana;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__INIT_MANA, oldInitMana, initMana));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getMana() {
        return mana;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setMana(int newMana) {
        int oldMana = mana;
        mana = newMana;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__MANA, oldMana, mana));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getStrength() {
        return strength;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setStrength(int newStrength) {
        int oldStrength = strength;
        strength = newStrength;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__STRENGTH, oldStrength, strength));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getAgility() {
        return agility;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setAgility(int newAgility) {
        int oldAgility = agility;
        agility = newAgility;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__AGILITY, oldAgility, agility));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getCharisma() {
        return charisma;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCharisma(int newCharisma) {
        int oldCharisma = charisma;
        charisma = newCharisma;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__CHARISMA, oldCharisma, charisma));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getIntelligence() {
        return intelligence;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIntelligence(int newIntelligence) {
        int oldIntelligence = intelligence;
        intelligence = newIntelligence;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__INTELLIGENCE, oldIntelligence, intelligence));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getLevel() {
        return level;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setLevel(int newLevel) {
        int oldLevel = level;
        level = newLevel;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__LEVEL, oldLevel, level));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public EList<Item> getInventory() {
        if (inventory == null) {
            inventory = new EObjectContainmentEList<Item>(Item.class, this, LeveleditorPackage.CREATURE__INVENTORY);
        }
        return inventory;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getExperience() {
        return experience;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setExperience(int newExperience) {
        int oldExperience = experience;
        experience = newExperience;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__EXPERIENCE, oldExperience, experience));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getGold() {
        return gold;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setGold(int newGold) {
        int oldGold = gold;
        gold = newGold;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__GOLD, oldGold, gold));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getInitPlayerActions() {
        return initPlayerActions;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setInitPlayerActions(int newInitPlayerActions) {
        int oldInitPlayerActions = initPlayerActions;
        initPlayerActions = newInitPlayerActions;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__INIT_PLAYER_ACTIONS, oldInitPlayerActions, initPlayerActions));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getResistLightning() {
        return resistLightning;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setResistLightning(int newResistLightning) {
        int oldResistLightning = resistLightning;
        resistLightning = newResistLightning;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__RESIST_LIGHTNING, oldResistLightning, resistLightning));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getResistFire() {
        return resistFire;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setResistFire(int newResistFire) {
        int oldResistFire = resistFire;
        resistFire = newResistFire;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__RESIST_FIRE, oldResistFire, resistFire));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getResistPoison() {
        return resistPoison;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setResistPoison(int newResistPoison) {
        int oldResistPoison = resistPoison;
        resistPoison = newResistPoison;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__RESIST_POISON, oldResistPoison, resistPoison));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getResistIce() {
        return resistIce;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setResistIce(int newResistIce) {
        int oldResistIce = resistIce;
        resistIce = newResistIce;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__RESIST_ICE, oldResistIce, resistIce));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public boolean isIsMale() {
        return isMale;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setIsMale(boolean newIsMale) {
        boolean oldIsMale = isMale;
        isMale = newIsMale;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__IS_MALE, oldIsMale, isMale));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ECreatureRace getRace() {
        return race;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setRace(ECreatureRace newRace) {
        ECreatureRace oldRace = race;
        race = newRace == null ? RACE_EDEFAULT : newRace;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__RACE, oldRace, race));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public ECreatureClass getCreatureClass() {
        return creatureClass;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setCreatureClass(ECreatureClass newCreatureClass) {
        ECreatureClass oldCreatureClass = creatureClass;
        creatureClass = newCreatureClass == null ? CREATURE_CLASS_EDEFAULT : newCreatureClass;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__CREATURE_CLASS, oldCreatureClass, creatureClass));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public int getArmor() {
        return armor;
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    public void setArmor(int newArmor) {
        int oldArmor = armor;
        armor = newArmor;
        if (eNotificationRequired()) eNotify(new ENotificationImpl(this, Notification.SET, LeveleditorPackage.CREATURE__ARMOR, oldArmor, armor));
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch(featureID) {
            case LeveleditorPackage.CREATURE__INVENTORY:
                return ((InternalEList<?>) getInventory()).basicRemove(otherEnd, msgs);
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
            case LeveleditorPackage.CREATURE__NAME:
                return getName();
            case LeveleditorPackage.CREATURE__INIT_HP:
                return getInitHp();
            case LeveleditorPackage.CREATURE__INIT_MANA:
                return getInitMana();
            case LeveleditorPackage.CREATURE__MANA:
                return getMana();
            case LeveleditorPackage.CREATURE__STRENGTH:
                return getStrength();
            case LeveleditorPackage.CREATURE__AGILITY:
                return getAgility();
            case LeveleditorPackage.CREATURE__CHARISMA:
                return getCharisma();
            case LeveleditorPackage.CREATURE__INTELLIGENCE:
                return getIntelligence();
            case LeveleditorPackage.CREATURE__LEVEL:
                return getLevel();
            case LeveleditorPackage.CREATURE__INVENTORY:
                return getInventory();
            case LeveleditorPackage.CREATURE__EXPERIENCE:
                return getExperience();
            case LeveleditorPackage.CREATURE__GOLD:
                return getGold();
            case LeveleditorPackage.CREATURE__INIT_PLAYER_ACTIONS:
                return getInitPlayerActions();
            case LeveleditorPackage.CREATURE__RESIST_LIGHTNING:
                return getResistLightning();
            case LeveleditorPackage.CREATURE__RESIST_FIRE:
                return getResistFire();
            case LeveleditorPackage.CREATURE__RESIST_POISON:
                return getResistPoison();
            case LeveleditorPackage.CREATURE__RESIST_ICE:
                return getResistIce();
            case LeveleditorPackage.CREATURE__IS_MALE:
                return isIsMale();
            case LeveleditorPackage.CREATURE__RACE:
                return getRace();
            case LeveleditorPackage.CREATURE__CREATURE_CLASS:
                return getCreatureClass();
            case LeveleditorPackage.CREATURE__ARMOR:
                return getArmor();
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
            case LeveleditorPackage.CREATURE__NAME:
                setName((String) newValue);
                return;
            case LeveleditorPackage.CREATURE__INIT_HP:
                setInitHp((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__INIT_MANA:
                setInitMana((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__MANA:
                setMana((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__STRENGTH:
                setStrength((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__AGILITY:
                setAgility((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__CHARISMA:
                setCharisma((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__INTELLIGENCE:
                setIntelligence((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__LEVEL:
                setLevel((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__INVENTORY:
                getInventory().clear();
                getInventory().addAll((Collection<? extends Item>) newValue);
                return;
            case LeveleditorPackage.CREATURE__EXPERIENCE:
                setExperience((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__GOLD:
                setGold((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__INIT_PLAYER_ACTIONS:
                setInitPlayerActions((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__RESIST_LIGHTNING:
                setResistLightning((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__RESIST_FIRE:
                setResistFire((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__RESIST_POISON:
                setResistPoison((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__RESIST_ICE:
                setResistIce((Integer) newValue);
                return;
            case LeveleditorPackage.CREATURE__IS_MALE:
                setIsMale((Boolean) newValue);
                return;
            case LeveleditorPackage.CREATURE__RACE:
                setRace((ECreatureRace) newValue);
                return;
            case LeveleditorPackage.CREATURE__CREATURE_CLASS:
                setCreatureClass((ECreatureClass) newValue);
                return;
            case LeveleditorPackage.CREATURE__ARMOR:
                setArmor((Integer) newValue);
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
            case LeveleditorPackage.CREATURE__NAME:
                setName(NAME_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__INIT_HP:
                setInitHp(INIT_HP_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__INIT_MANA:
                setInitMana(INIT_MANA_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__MANA:
                setMana(MANA_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__STRENGTH:
                setStrength(STRENGTH_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__AGILITY:
                setAgility(AGILITY_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__CHARISMA:
                setCharisma(CHARISMA_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__INTELLIGENCE:
                setIntelligence(INTELLIGENCE_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__LEVEL:
                setLevel(LEVEL_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__INVENTORY:
                getInventory().clear();
                return;
            case LeveleditorPackage.CREATURE__EXPERIENCE:
                setExperience(EXPERIENCE_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__GOLD:
                setGold(GOLD_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__INIT_PLAYER_ACTIONS:
                setInitPlayerActions(INIT_PLAYER_ACTIONS_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__RESIST_LIGHTNING:
                setResistLightning(RESIST_LIGHTNING_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__RESIST_FIRE:
                setResistFire(RESIST_FIRE_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__RESIST_POISON:
                setResistPoison(RESIST_POISON_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__RESIST_ICE:
                setResistIce(RESIST_ICE_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__IS_MALE:
                setIsMale(IS_MALE_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__RACE:
                setRace(RACE_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__CREATURE_CLASS:
                setCreatureClass(CREATURE_CLASS_EDEFAULT);
                return;
            case LeveleditorPackage.CREATURE__ARMOR:
                setArmor(ARMOR_EDEFAULT);
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
            case LeveleditorPackage.CREATURE__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case LeveleditorPackage.CREATURE__INIT_HP:
                return initHp != INIT_HP_EDEFAULT;
            case LeveleditorPackage.CREATURE__INIT_MANA:
                return initMana != INIT_MANA_EDEFAULT;
            case LeveleditorPackage.CREATURE__MANA:
                return mana != MANA_EDEFAULT;
            case LeveleditorPackage.CREATURE__STRENGTH:
                return strength != STRENGTH_EDEFAULT;
            case LeveleditorPackage.CREATURE__AGILITY:
                return agility != AGILITY_EDEFAULT;
            case LeveleditorPackage.CREATURE__CHARISMA:
                return charisma != CHARISMA_EDEFAULT;
            case LeveleditorPackage.CREATURE__INTELLIGENCE:
                return intelligence != INTELLIGENCE_EDEFAULT;
            case LeveleditorPackage.CREATURE__LEVEL:
                return level != LEVEL_EDEFAULT;
            case LeveleditorPackage.CREATURE__INVENTORY:
                return inventory != null && !inventory.isEmpty();
            case LeveleditorPackage.CREATURE__EXPERIENCE:
                return experience != EXPERIENCE_EDEFAULT;
            case LeveleditorPackage.CREATURE__GOLD:
                return gold != GOLD_EDEFAULT;
            case LeveleditorPackage.CREATURE__INIT_PLAYER_ACTIONS:
                return initPlayerActions != INIT_PLAYER_ACTIONS_EDEFAULT;
            case LeveleditorPackage.CREATURE__RESIST_LIGHTNING:
                return resistLightning != RESIST_LIGHTNING_EDEFAULT;
            case LeveleditorPackage.CREATURE__RESIST_FIRE:
                return resistFire != RESIST_FIRE_EDEFAULT;
            case LeveleditorPackage.CREATURE__RESIST_POISON:
                return resistPoison != RESIST_POISON_EDEFAULT;
            case LeveleditorPackage.CREATURE__RESIST_ICE:
                return resistIce != RESIST_ICE_EDEFAULT;
            case LeveleditorPackage.CREATURE__IS_MALE:
                return isMale != IS_MALE_EDEFAULT;
            case LeveleditorPackage.CREATURE__RACE:
                return race != RACE_EDEFAULT;
            case LeveleditorPackage.CREATURE__CREATURE_CLASS:
                return creatureClass != CREATURE_CLASS_EDEFAULT;
            case LeveleditorPackage.CREATURE__ARMOR:
                return armor != ARMOR_EDEFAULT;
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
        result.append(", initHp: ");
        result.append(initHp);
        result.append(", initMana: ");
        result.append(initMana);
        result.append(", mana: ");
        result.append(mana);
        result.append(", strength: ");
        result.append(strength);
        result.append(", agility: ");
        result.append(agility);
        result.append(", charisma: ");
        result.append(charisma);
        result.append(", intelligence: ");
        result.append(intelligence);
        result.append(", level: ");
        result.append(level);
        result.append(", experience: ");
        result.append(experience);
        result.append(", gold: ");
        result.append(gold);
        result.append(", initPlayerActions: ");
        result.append(initPlayerActions);
        result.append(", resistLightning: ");
        result.append(resistLightning);
        result.append(", resistFire: ");
        result.append(resistFire);
        result.append(", resistPoison: ");
        result.append(resistPoison);
        result.append(", resistIce: ");
        result.append(resistIce);
        result.append(", isMale: ");
        result.append(isMale);
        result.append(", race: ");
        result.append(race);
        result.append(", creatureClass: ");
        result.append(creatureClass);
        result.append(", armor: ");
        result.append(armor);
        result.append(')');
        return result.toString();
    }
}
