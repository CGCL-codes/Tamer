package com.l2jserver.gameserver.datatables;

import com.l2jserver.gameserver.model.L2Skill;

/**
 *
 * @author BiTi
 */
public class HeroSkillTable {

    private static final L2Skill[] _heroSkills = new L2Skill[5];

    private static final int[] _heroSkillsId = { 395, 396, 1374, 1375, 1376 };

    private HeroSkillTable() {
        for (int i = 0; i < _heroSkillsId.length; i++) _heroSkills[i] = SkillTable.getInstance().getInfo(_heroSkillsId[i], 1);
    }

    public static HeroSkillTable getInstance() {
        return SingletonHolder._instance;
    }

    public static L2Skill[] getHeroSkills() {
        return _heroSkills;
    }

    public static boolean isHeroSkill(int skillid) {
        for (int id : _heroSkillsId) {
            if (id == skillid) return true;
        }
        return false;
    }

    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {

        protected static final HeroSkillTable _instance = new HeroSkillTable();
    }
}
