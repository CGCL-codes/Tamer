package com.l2jserver.gameserver.model.zone.type;

import gnu.trove.TIntIntHashMap;
import java.util.Collection;
import java.util.concurrent.Future;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.ai.CtrlIntention;
import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Playable;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;
import com.l2jserver.gameserver.network.serverpackets.EtcStatusUpdate;
import com.l2jserver.util.Rnd;
import com.l2jserver.util.StringUtil;

/**
 * another type of damage zone with skills
 *
 * @author  kerberos
 */
public class L2PoisonZone extends L2ZoneType {

    private int _chance;

    private int _initialDelay;

    private int _reuse;

    private boolean _enabled;

    private boolean _bypassConditions;

    private String _target;

    private Future<?> _task;

    private TIntIntHashMap _skills;

    public L2PoisonZone(int id) {
        super(id);
        _chance = 100;
        _initialDelay = 0;
        _reuse = 30000;
        _enabled = true;
        _target = "pc";
        _bypassConditions = false;
    }

    @Override
    public void setParameter(String name, String value) {
        if (name.equals("chance")) {
            _chance = Integer.parseInt(value);
        } else if (name.equals("initialDelay")) {
            _initialDelay = Integer.parseInt(value);
        } else if (name.equals("default_enabled")) {
            _enabled = Boolean.parseBoolean(value);
        } else if (name.equals("target")) {
            _target = String.valueOf(value);
        } else if (name.equals("reuse")) {
            _reuse = Integer.parseInt(value);
        } else if (name.equals("bypassSkillConditions")) {
            _bypassConditions = Boolean.parseBoolean(value);
        } else if (name.equals("skillIdLvl")) {
            String[] propertySplit = value.split(";");
            _skills = new TIntIntHashMap(propertySplit.length);
            for (String skill : propertySplit) {
                String[] skillSplit = skill.split("-");
                if (skillSplit.length != 2) _log.warning(StringUtil.concat("[L2PoisonZone]: invalid config property -> skillsIdLvl \"", skill, "\"")); else {
                    try {
                        _skills.put(Integer.parseInt(skillSplit[0]), Integer.parseInt(skillSplit[1]));
                    } catch (NumberFormatException nfe) {
                        if (!skill.isEmpty()) {
                            _log.warning(StringUtil.concat("[L2PoisonZone]: invalid config property -> skillsIdLvl \"", skillSplit[0], "\"", skillSplit[1]));
                        }
                    }
                }
            }
        } else super.setParameter(name, value);
    }

    @Override
    protected void onEnter(L2Character character) {
        if (((character instanceof L2Playable) && _target.equalsIgnoreCase("pc")) || ((character instanceof L2PcInstance) && _target.equalsIgnoreCase("pc_only")) || ((character instanceof L2MonsterInstance) && _target.equalsIgnoreCase("npc"))) {
            if (_task == null) {
                synchronized (this) {
                    if (_task == null) _task = ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new ApplySkill(this), _initialDelay, _reuse);
                }
            }
        }
        if (character instanceof L2PcInstance) {
            character.setInsideZone(L2Character.ZONE_DANGERAREA, true);
            character.sendPacket(new EtcStatusUpdate((L2PcInstance) character));
        }
    }

    @Override
    protected void onExit(L2Character character) {
        if (_characterList.isEmpty() && _task != null) {
            _task.cancel(true);
            _task = null;
        }
        if (character instanceof L2PcInstance) {
            character.setInsideZone(L2Character.ZONE_DANGERAREA, false);
            if (!character.isInsideZone(L2Character.ZONE_DANGERAREA)) character.sendPacket(new EtcStatusUpdate((L2PcInstance) character));
        }
    }

    public L2Skill getSkill(int skillId, int skillLvl) {
        return SkillTable.getInstance().getInfo(skillId, skillLvl);
    }

    public String getTargetType() {
        return _target;
    }

    public boolean isEnabled() {
        return _enabled;
    }

    public int getChance() {
        return _chance;
    }

    public void setZoneEnabled(boolean val) {
        _enabled = val;
    }

    protected Collection<L2Character> getCharacterList() {
        return _characterList.values();
    }

    class ApplySkill implements Runnable {

        private final L2PoisonZone _poisonZone;

        ApplySkill(L2PoisonZone zone) {
            _poisonZone = zone;
            if (_skills == null) {
                _skills = new TIntIntHashMap(1);
                _skills.put(4070, 1);
            }
        }

        public void run() {
            if (isEnabled()) {
                for (L2Character temp : _poisonZone.getCharacterList()) {
                    if (temp != null && !temp.isDead()) {
                        if (((temp instanceof L2PcInstance && getTargetType().equalsIgnoreCase("pc_only")) || (temp instanceof L2Playable && getTargetType().equalsIgnoreCase("pc")) || (temp instanceof L2MonsterInstance && temp.hasAI() && temp.getAI().getIntention() != CtrlIntention.AI_INTENTION_IDLE && getTargetType().equalsIgnoreCase("npc"))) && Rnd.get(100) < getChance()) {
                            for (int skillId : _skills.keys()) {
                                if (_bypassConditions || getSkill(skillId, _skills.get(skillId)).checkCondition(temp, temp, false)) if (temp.getFirstEffect(skillId) == null) getSkill(skillId, _skills.get(skillId)).getEffects(temp, temp);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDieInside(L2Character character) {
    }

    @Override
    public void onReviveInside(L2Character character) {
    }
}
