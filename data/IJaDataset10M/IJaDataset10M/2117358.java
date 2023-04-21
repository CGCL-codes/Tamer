package pcgen.cdom.enumeration;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import pcgen.base.formula.Formula;
import pcgen.base.util.CaseInsensitiveMap;
import pcgen.base.util.DoubleKeyMapToList;
import pcgen.cdom.base.CDOMList;
import pcgen.cdom.base.CDOMListObject;
import pcgen.cdom.base.Category;
import pcgen.cdom.base.PersistentTransitionChoice;
import pcgen.cdom.base.TransitionChoice;
import pcgen.cdom.content.ChallengeRating;
import pcgen.cdom.content.HitDie;
import pcgen.cdom.content.LevelCommandFactory;
import pcgen.cdom.content.LevelExchange;
import pcgen.cdom.content.Modifier;
import pcgen.cdom.content.SpellResistance;
import pcgen.cdom.helper.AbilitySelection;
import pcgen.cdom.helper.Capacity;
import pcgen.cdom.list.ClassSkillList;
import pcgen.cdom.list.ClassSpellList;
import pcgen.cdom.list.DomainSpellList;
import pcgen.cdom.reference.CDOMSingleRef;
import pcgen.core.Ability;
import pcgen.core.ArmorProf;
import pcgen.core.Campaign;
import pcgen.core.Equipment;
import pcgen.core.GameMode;
import pcgen.core.Language;
import pcgen.core.PCAlignment;
import pcgen.core.PCClass;
import pcgen.core.PCStat;
import pcgen.core.QualifiedObject;
import pcgen.core.SettingsHandler;
import pcgen.core.ShieldProf;
import pcgen.core.SizeAdjustment;
import pcgen.core.SpellProhibitor;
import pcgen.core.SubClass;
import pcgen.core.WeaponProf;
import pcgen.core.Ability.Nature;
import pcgen.core.character.WieldCategory;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.spell.Spell;
import pcgen.util.enumeration.Load;
import pcgen.util.enumeration.Visibility;

/**
 * @author Tom Parker <thpr@users.sourceforge.net>
 * 
 * This is a Typesafe enumeration of legal Object Characteristics of an object.
 * It is designed to act as an index to a specific Objects within a
 * CDOMObject.
 * 
 * ObjectKeys are designed to store items in a CDOMObject in a type-safe
 * fashion. Note that it is possible to use the ObjectKey to cast the object to
 * the type of object stored by the ObjectKey. (This assists with Generics)
 * 
 * A "default value" (may be null) must be provided at object construction (the
 * default is provided when getSafe(ObjectKey) is called in CDOMObject). This
 * default value is especially useful for Boolean ObjectKeys.
 * 
 * @param <T>
 *            The class of object stored by this ObjectKey.
 */
public class ObjectKey<T> {

    private static CaseInsensitiveMap<ObjectKey<?>> map = null;

    public static final ObjectKey<Boolean> USE_UNTRAINED = new ObjectKey<Boolean>(Boolean.TRUE);

    public static final ObjectKey<Boolean> EXCLUSIVE = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<URI> SOURCE_URI = new ObjectKey<URI>(null);

    public static final ObjectKey<PCAlignment> ALIGNMENT = new ObjectKey<PCAlignment>(null);

    public static final ObjectKey<Boolean> READ_ONLY = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<PCStat> KEY_STAT = new ObjectKey<PCStat>(null);

    public static final ObjectKey<SkillArmorCheck> ARMOR_CHECK = new ObjectKey<SkillArmorCheck>(SkillArmorCheck.NONE);

    public static final ObjectKey<Visibility> VISIBILITY = new ObjectKey<Visibility>(Visibility.DEFAULT);

    public static final ObjectKey<Boolean> REMOVABLE = new ObjectKey<Boolean>(Boolean.TRUE);

    public static final ObjectKey<SubRegion> SUBREGION = new ObjectKey<SubRegion>(null);

    public static final ObjectKey<Region> REGION = new ObjectKey<Region>(null);

    public static final ObjectKey<Boolean> USETEMPLATENAMEFORSUBREGION = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> USETEMPLATENAMEFORREGION = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Gender> GENDER_LOCK = new ObjectKey<Gender>(null);

    public static final ObjectKey<BigDecimal> FACE_WIDTH = new ObjectKey<BigDecimal>(null);

    public static final ObjectKey<BigDecimal> FACE_HEIGHT = new ObjectKey<BigDecimal>(null);

    public static final ObjectKey<Boolean> USETEMPLATENAMEFORSUBRACE = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<SubRace> SUBRACE = new ObjectKey<SubRace>(null);

    public static final ObjectKey<BigDecimal> CR_MODIFIER = new ObjectKey<BigDecimal>(BigDecimal.ZERO);

    public static final ObjectKey<RaceType> RACETYPE = new ObjectKey<RaceType>(null);

    public static final ObjectKey<BigDecimal> COST = new ObjectKey<BigDecimal>(BigDecimal.ZERO);

    public static final ObjectKey<PCStat> SPELL_STAT = new ObjectKey<PCStat>(null);

    public static final ObjectKey<Boolean> COST_DOUBLE = new ObjectKey<Boolean>(null);

    public static final ObjectKey<Boolean> ASSIGN_TO_ALL = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<EqModNameOpt> NAME_OPT = new ObjectKey<EqModNameOpt>(EqModNameOpt.NORMAL);

    public static final ObjectKey<EqModFormatCat> FORMAT = new ObjectKey<EqModFormatCat>(EqModFormatCat.PARENS);

    public static final ObjectKey<Boolean> ATTACKS_PROGRESS = new ObjectKey<Boolean>(Boolean.TRUE);

    public static final ObjectKey<WieldCategory> WIELD = new ObjectKey<WieldCategory>(null);

    public static final ObjectKey<BigDecimal> WEIGHT = new ObjectKey<BigDecimal>(BigDecimal.ZERO);

    public static final ObjectKey<BigDecimal> WEIGHT_MOD = new ObjectKey<BigDecimal>(BigDecimal.ZERO);

    public static final ObjectKey<CDOMSingleRef<WeaponProf>> WEAPON_PROF = new ObjectKey<CDOMSingleRef<WeaponProf>>(null);

    public static final ObjectKey<CDOMSingleRef<ArmorProf>> ARMOR_PROF = new ObjectKey<CDOMSingleRef<ArmorProf>>(null);

    public static final ObjectKey<CDOMSingleRef<ShieldProf>> SHIELD_PROF = new ObjectKey<CDOMSingleRef<ShieldProf>>(null);

    public static final ObjectKey<EqModControl> MOD_CONTROL = new ObjectKey<EqModControl>(EqModControl.YES);

    public static final ObjectKey<BigDecimal> CURRENT_COST = new ObjectKey<BigDecimal>(null);

    public static final ObjectKey<Object> PARENT = new ObjectKey<Object>(null);

    public static final ObjectKey<Object> TOKEN_PARENT = new ObjectKey<Object>(null);

    public static final ObjectKey<Modifier<HitDie>> HITDIE = new ObjectKey<Modifier<HitDie>>(null);

    public static final ObjectKey<ChallengeRating> CHALLENGE_RATING = new ObjectKey<ChallengeRating>(ChallengeRating.ZERO);

    public static final ObjectKey<Boolean> USE_SPELL_SPELL_STAT = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> CASTER_WITHOUT_SPELL_STAT = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> SPELLBOOK = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> MOD_TO_SKILLS = new ObjectKey<Boolean>(Boolean.TRUE);

    public static final ObjectKey<Boolean> MEMORIZE_SPELLS = new ObjectKey<Boolean>(Boolean.TRUE);

    public static final ObjectKey<Boolean> IS_MONSTER = new ObjectKey<Boolean>(null);

    public static final ObjectKey<Boolean> ALLOWBASECLASS = new ObjectKey<Boolean>(Boolean.TRUE);

    public static final ObjectKey<Boolean> HAS_BONUS_SPELL_STAT = new ObjectKey<Boolean>(null);

    public static final ObjectKey<PCStat> BONUS_SPELL_STAT = new ObjectKey<PCStat>(null);

    public static final ObjectKey<HitDie> LEVEL_HITDIE = new ObjectKey<HitDie>(HitDie.ZERO);

    public static final ObjectKey<ClassSpellList> CLASS_SPELLLIST = new ObjectKey<ClassSpellList>(null);

    public static final ObjectKey<DomainSpellList> DOMAIN_SPELLLIST = new ObjectKey<DomainSpellList>(null);

    public static final ObjectKey<TransitionChoice<CDOMListObject<Spell>>> SPELLLIST_CHOICE = new ObjectKey<TransitionChoice<CDOMListObject<Spell>>>(null);

    public static final ObjectKey<TransitionChoice<ClassSkillList>> SKILLLIST_CHOICE = new ObjectKey<TransitionChoice<ClassSkillList>>(null);

    public static final ObjectKey<Boolean> STACKS = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> MULTIPLE_ALLOWED = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<BigDecimal> SELECTION_COST = new ObjectKey<BigDecimal>(BigDecimal.ONE);

    public static final ObjectKey<Boolean> NAME_PI = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> DESC_PI = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Category<Ability>> ABILITY_CAT = new ObjectKey<Category<Ability>>(null);

    public static final ObjectKey<Load> UNENCUMBERED_LOAD = new ObjectKey<Load>(Load.LIGHT);

    public static final ObjectKey<Load> UNENCUMBERED_ARMOR = new ObjectKey<Load>(Load.LIGHT);

    public static final ObjectKey<Boolean> ANY_FAVORED_CLASS = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<TransitionChoice<PCClass>> FAVCLASS_CHOICE = new ObjectKey<TransitionChoice<PCClass>>(null);

    public static final ObjectKey<LevelCommandFactory> MONSTER_CLASS = new ObjectKey<LevelCommandFactory>(null);

    public static final ObjectKey<CDOMSingleRef<Equipment>> BASE_ITEM = new ObjectKey<CDOMSingleRef<Equipment>>(null);

    public static final ObjectKey<LevelExchange> EXCHANGE_LEVEL = new ObjectKey<LevelExchange>(null);

    public static final ObjectKey<CDOMSingleRef<PCClass>> EX_CLASS = new ObjectKey<CDOMSingleRef<PCClass>>(null);

    public static final ObjectKey<SpellResistance> SR = new ObjectKey<SpellResistance>(SpellResistance.NONE);

    public static final ObjectKey<QualifiedObject<Boolean>> HAS_DEITY_WEAPONPROF = new ObjectKey<QualifiedObject<Boolean>>(new QualifiedObject<Boolean>(Boolean.FALSE));

    public static final ObjectKey<SpellProhibitor> CHOICE = new ObjectKey<SpellProhibitor>(null);

    public static final ObjectKey<TransitionChoice<Ability>> MODIFY_CHOICE = new ObjectKey<TransitionChoice<Ability>>(null);

    public static final ObjectKey<Boolean> CONTAINER_CONSTANT_WEIGHT = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<BigDecimal> CONTAINER_WEIGHT_CAPACITY = new ObjectKey<BigDecimal>(null);

    public static final ObjectKey<Capacity> TOTAL_CAPACITY = new ObjectKey<Capacity>(null);

    public static final ObjectKey<Category<SubClass>> SUBCLASS_CATEGORY = new ObjectKey<Category<SubClass>>(null);

    public static final ObjectKey<Nature> ABILITY_NATURE = new ObjectKey<Nature>(Ability.Nature.NORMAL);

    public static final ObjectKey<SizeAdjustment> BASESIZE;

    public static final ObjectKey<SizeAdjustment> SIZE;

    public static final ObjectKey<TransitionChoice<Region>> REGION_CHOICE = new ObjectKey<TransitionChoice<Region>>(null);

    public static final ObjectKey<Boolean> USE_MASTER_SKILL = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> DONTADD_HITDIE = new ObjectKey<Boolean>(null);

    public static final ObjectKey<Boolean> DONTADD_SKILLPOINTS = new ObjectKey<Boolean>(null);

    public static final ObjectKey<KitApply> APPLY_MODE = new ObjectKey<KitApply>(KitApply.PERMANENT);

    public static final ObjectKey<QualifiedObject<Formula>> EQUIP_BUY = new ObjectKey<QualifiedObject<Formula>>(null);

    public static final ObjectKey<Date> SOURCE_DATE = new ObjectKey<Date>(null);

    public static final ObjectKey<Campaign> SOURCE_CAMPAIGN = new ObjectKey<Campaign>(null);

    public static final ObjectKey<Boolean> IS_OGL = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> IS_MATURE = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> IS_LICENSED = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> IS_D20 = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<Boolean> SHOW_IN_MENU = new ObjectKey<Boolean>(Boolean.FALSE);

    public static final ObjectKey<PersistentTransitionChoice<Language>> CHOOSE_LANGAUTO = new ObjectKey<PersistentTransitionChoice<Language>>(null);

    public static final ObjectKey<Prerequisite> PRERACETYPE = new ObjectKey<Prerequisite>(null);

    public static final ObjectKey<QualifiedObject<Boolean>> HAS_ALL_WEAPONPROF = new ObjectKey<QualifiedObject<Boolean>>(new QualifiedObject<Boolean>(Boolean.FALSE));

    public static final ObjectKey<File> DIRECTORY = new ObjectKey<File>(null);

    public static final ObjectKey<File> WRITE_DIRECTORY = new ObjectKey<File>(null);

    public static final ObjectKey<GameMode> GAME_MODE = new ObjectKey<GameMode>(null);

    public static final ObjectKey<PersistentTransitionChoice<AbilitySelection>> TEMPLATE_FEAT = new ObjectKey<PersistentTransitionChoice<AbilitySelection>>(null);

    public static final ObjectKey<DoubleKeyMapToList<Spell, CDOMList<Spell>, Integer>> SPELL_PC_INFO = new ObjectKey<DoubleKeyMapToList<Spell, CDOMList<Spell>, Integer>>(null);

    static {
        buildMap();
        BASESIZE = new ObjectKey<SizeAdjustment>(null) {

            @Override
            public SizeAdjustment getDefault() {
                return SettingsHandler.getGame().getDefaultSizeAdjustment();
            }
        };
        map.put(BASESIZE.toString(), BASESIZE);
        SIZE = new ObjectKey<SizeAdjustment>(null) {

            @Override
            public SizeAdjustment getDefault() {
                return SettingsHandler.getGame().getDefaultSizeAdjustment();
            }
        };
        map.put(SIZE.toString(), SIZE);
    }

    private final T defaultValue;

    private ObjectKey(T def) {
        defaultValue = def;
    }

    public T getDefault() {
        return defaultValue;
    }

    public T cast(Object o) {
        return (T) o;
    }

    public static <OT> ObjectKey<OT> getKeyFor(Class<OT> c, String s) {
        if (map == null) {
            buildMap();
        }
        ObjectKey<OT> o = (ObjectKey<OT>) map.get(s);
        if (o == null) {
            o = new ObjectKey<OT>(null);
            map.put(s, o);
        }
        return o;
    }

    private static void buildMap() {
        map = new CaseInsensitiveMap<ObjectKey<?>>();
        Field[] fields = ObjectKey.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            int mod = fields[i].getModifiers();
            if (java.lang.reflect.Modifier.isStatic(mod) && java.lang.reflect.Modifier.isFinal(mod) && java.lang.reflect.Modifier.isPublic(mod)) {
                try {
                    Object o = fields[i].get(null);
                    if (o instanceof ObjectKey) {
                        map.put(fields[i].getName(), (ObjectKey<?>) o);
                    }
                } catch (IllegalArgumentException e) {
                    throw new InternalError();
                } catch (IllegalAccessException e) {
                    throw new InternalError();
                }
            }
        }
    }

    @Override
    public String toString() {
        if (map == null) {
            buildMap();
        }
        for (Map.Entry<?, ObjectKey<?>> me : map.entrySet()) {
            if (me.getValue() == this) {
                return me.getKey().toString();
            }
        }
        return "";
    }

    public static Collection<ObjectKey<?>> getAllConstants() {
        if (map == null) {
            buildMap();
        }
        return new HashSet<ObjectKey<?>>(map.values());
    }
}
