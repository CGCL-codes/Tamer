package com.l2jserver.gameserver.templates.item;

import java.util.ArrayList;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.skills.Env;
import com.l2jserver.gameserver.skills.SkillHolder;
import com.l2jserver.gameserver.skills.funcs.Func;
import com.l2jserver.gameserver.skills.funcs.FuncTemplate;
import com.l2jserver.gameserver.templates.StatsSet;
import com.l2jserver.util.StringUtil;

/**
 * This class is dedicated to the management of armors.
 *
 * @version $Revision: 1.2.2.1.2.6 $ $Date: 2005/03/27 15:30:10 $
 */
public final class L2Armor extends L2Item {

    private final int _avoidModifier;

    private final int _pDef;

    private final int _mDef;

    private final int _mpBonus;

    private final int _hpBonus;

    private L2Skill _enchant4Skill = null;

    private SkillHolder[] _skillHolder;

    /**
     * Constructor for Armor.<BR><BR>
     * <U><I>Variables filled :</I></U><BR>
     * <LI>_avoidModifier</LI>
     * <LI>_pDef & _mDef</LI>
     * <LI>_mpBonus & _hpBonus</LI>
     * <LI>enchant4Skill</LI>
     * @param type : L2ArmorType designating the type of armor
     * @param set : StatsSet designating the set of couples (key,value) caracterizing the armor
     * @see L2Item constructor
     */
    public L2Armor(L2ArmorType type, StatsSet set) {
        super(type, set);
        _avoidModifier = set.getInteger("avoid_modify");
        _pDef = set.getInteger("p_def");
        _mDef = set.getInteger("m_def");
        _mpBonus = set.getInteger("mp_bonus", 0);
        _hpBonus = set.getInteger("hp_bonus", 0);
        String[] skill = set.getString("enchant4_skill").split("-");
        if (skill != null && skill.length == 2) {
            int skill_Id = Integer.parseInt(skill[0]);
            int skillLvl = Integer.parseInt(skill[1]);
            if (skill_Id > 0 && skillLvl > 0) _enchant4Skill = SkillTable.getInstance().getInfo(skill_Id, skillLvl);
        }
        String[] skills = set.getString("skill").split(";");
        _skillHolder = new SkillHolder[skills.length];
        byte iterator = 0;
        for (String st : skills) {
            String[] info = st.split("-");
            if (info == null || info.length != 2) continue;
            int id = 0;
            int level = 0;
            try {
                id = Integer.parseInt(info[0]);
                level = Integer.parseInt(info[1]);
            } catch (Exception nfe) {
                _log.info(StringUtil.concat("> Couldnt parse ", st, " in armor skills!"));
                continue;
            }
            if (id > 0 && level > 0) {
                _skillHolder[iterator] = new SkillHolder(id, level);
                iterator++;
            }
        }
    }

    /**
	 * Returns the type of the armor.
	 * @return L2ArmorType
	 */
    @Override
    public L2ArmorType getItemType() {
        return (L2ArmorType) super._type;
    }

    /**
	 * Returns the ID of the item after applying the mask.
	 * @return int : ID of the item
	 */
    @Override
    public final int getItemMask() {
        return getItemType().mask();
    }

    /**
	 * Returns the magical defense of the armor
	 * @return int : value of the magic defense
	 */
    public final int getMDef() {
        return _mDef;
    }

    /**
	 * Returns the physical defense of the armor
	 * @return int : value of the physical defense
	 */
    public final int getPDef() {
        return _pDef;
    }

    /**
	 * Returns avoid modifier given by the armor
	 * @return int : avoid modifier
	 */
    public final int getAvoidModifier() {
        return _avoidModifier;
    }

    /**
	 * Returns magical bonus given by the armor
	 * @return int : value of the magical bonus
	 */
    public final int getMpBonus() {
        return _mpBonus;
    }

    /**
	 * Returns physical bonus given by the armor
	 * @return int : value of the physical bonus
	 */
    public final int getHpBonus() {
        return _hpBonus;
    }

    /**
	* Returns skill that player get when has equiped armor +4  or more
	* @return
	*/
    public L2Skill getEnchant4Skill() {
        return _enchant4Skill;
    }

    /**
	 * Returns passive skill linked to that armor
	 * @return
	 */
    public SkillHolder[] getSkills() {
        return _skillHolder;
    }

    /**
	 * Returns array of Func objects containing the list of functions used by the armor
	 * @param instance : L2ItemInstance pointing out the armor
	 * @param player : L2Character pointing out the player
	 * @return Func[] : array of functions
	 */
    @Override
    public Func[] getStatFuncs(L2ItemInstance instance, L2Character player) {
        if (_funcTemplates == null || _funcTemplates.length == 0) return _emptyFunctionSet;
        ArrayList<Func> funcs = new ArrayList<Func>(_funcTemplates.length);
        Env env = new Env();
        env.player = player;
        env.item = instance;
        Func f;
        for (FuncTemplate t : _funcTemplates) {
            f = t.getFunc(env, instance);
            if (f != null) funcs.add(f);
        }
        return funcs.toArray(new Func[funcs.size()]);
    }
}
