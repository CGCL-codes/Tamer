package net.sf.l2j.gameserver.handler;

import java.util.logging.Logger;
import javolution.util.FastMap;
import net.sf.l2j.gameserver.handler.skillhandlers.BalanceLife;
import net.sf.l2j.gameserver.handler.skillhandlers.BeastFeed;
import net.sf.l2j.gameserver.handler.skillhandlers.Blow;
import net.sf.l2j.gameserver.handler.skillhandlers.Charge;
import net.sf.l2j.gameserver.handler.skillhandlers.CombatPointHeal;
import net.sf.l2j.gameserver.handler.skillhandlers.Continuous;
import net.sf.l2j.gameserver.handler.skillhandlers.CpDam;
import net.sf.l2j.gameserver.handler.skillhandlers.Craft;
import net.sf.l2j.gameserver.handler.skillhandlers.DeluxeKey;
import net.sf.l2j.gameserver.handler.skillhandlers.Disablers;
import net.sf.l2j.gameserver.handler.skillhandlers.DrainSoul;
import net.sf.l2j.gameserver.handler.skillhandlers.Fishing;
import net.sf.l2j.gameserver.handler.skillhandlers.FishingSkill;
import net.sf.l2j.gameserver.handler.skillhandlers.GetPlayer;
import net.sf.l2j.gameserver.handler.skillhandlers.GiveSp;
import net.sf.l2j.gameserver.handler.skillhandlers.Harvest;
import net.sf.l2j.gameserver.handler.skillhandlers.Heal;
import net.sf.l2j.gameserver.handler.skillhandlers.ManaHeal;
import net.sf.l2j.gameserver.handler.skillhandlers.Manadam;
import net.sf.l2j.gameserver.handler.skillhandlers.Mdam;
import net.sf.l2j.gameserver.handler.skillhandlers.Pdam;
import net.sf.l2j.gameserver.handler.skillhandlers.Recall;
import net.sf.l2j.gameserver.handler.skillhandlers.Resurrect;
import net.sf.l2j.gameserver.handler.skillhandlers.SiegeFlag;
import net.sf.l2j.gameserver.handler.skillhandlers.Sow;
import net.sf.l2j.gameserver.handler.skillhandlers.Spoil;
import net.sf.l2j.gameserver.handler.skillhandlers.StrSiegeAssault;
import net.sf.l2j.gameserver.handler.skillhandlers.SummonFriend;
import net.sf.l2j.gameserver.handler.skillhandlers.SummonTreasureKey;
import net.sf.l2j.gameserver.handler.skillhandlers.Sweep;
import net.sf.l2j.gameserver.handler.skillhandlers.TakeCastle;
import net.sf.l2j.gameserver.handler.skillhandlers.Unlock;
import net.sf.l2j.gameserver.handler.skillhandlers.ZakenPlayer;
import net.sf.l2j.gameserver.handler.skillhandlers.ZakenSelf;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.L2Skill.SkillType;

/**
 * @author Maxi56
 */
public class SkillHandler {

    private static Logger _log = Logger.getLogger(SkillHandler.class.getName());

    private FastMap<L2Skill.SkillType, ISkillHandler> _datatable;

    public static SkillHandler getInstance() {
        return SingletonHolder._instance;
    }

    private SkillHandler() {
        _datatable = new FastMap<L2Skill.SkillType, ISkillHandler>();
        registerSkillHandler(new Blow());
        registerSkillHandler(new Pdam());
        registerSkillHandler(new Mdam());
        registerSkillHandler(new CpDam());
        registerSkillHandler(new Manadam());
        registerSkillHandler(new Heal());
        registerSkillHandler(new CombatPointHeal());
        registerSkillHandler(new ManaHeal());
        registerSkillHandler(new BalanceLife());
        registerSkillHandler(new Charge());
        registerSkillHandler(new Continuous());
        registerSkillHandler(new Resurrect());
        registerSkillHandler(new Spoil());
        registerSkillHandler(new Sweep());
        registerSkillHandler(new SummonTreasureKey());
        registerSkillHandler(new Disablers());
        registerSkillHandler(new Recall());
        registerSkillHandler(new Unlock());
        registerSkillHandler(new DrainSoul());
        registerSkillHandler(new Craft());
        registerSkillHandler(new Fishing());
        registerSkillHandler(new FishingSkill());
        registerSkillHandler(new BeastFeed());
        registerSkillHandler(new DeluxeKey());
        registerSkillHandler(new Sow());
        registerSkillHandler(new Harvest());
        registerSkillHandler(new GetPlayer());
        registerSkillHandler(new GiveSp());
        registerSkillHandler(new ZakenPlayer());
        registerSkillHandler(new ZakenSelf());
        registerSkillHandler(new StrSiegeAssault());
        registerSkillHandler(new SummonFriend());
        registerSkillHandler(new SiegeFlag());
        registerSkillHandler(new TakeCastle());
        _log.config("SkillHandler: Loaded " + _datatable.size() + " handlers.");
    }

    public void registerSkillHandler(ISkillHandler handler) {
        SkillType[] types = handler.getSkillIds();
        for (SkillType t : types) _datatable.put(t, handler);
    }

    public ISkillHandler getSkillHandler(SkillType skillType) {
        return _datatable.get(skillType);
    }

    public int size() {
        return _datatable.size();
    }

    private static final class SingletonHolder {

        protected static final SkillHandler _instance = new SkillHandler();
    }
}
