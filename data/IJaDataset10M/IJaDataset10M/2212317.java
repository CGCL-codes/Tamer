package com.l2jserver.gameserver.model.actor.instance;

import com.l2jserver.gameserver.model.base.ClassType;
import com.l2jserver.gameserver.model.base.PlayerClass;
import com.l2jserver.gameserver.model.base.Race;
import com.l2jserver.gameserver.templates.chars.L2NpcTemplate;

public final class L2VillageMasterFighterInstance extends L2VillageMasterInstance {

    public L2VillageMasterFighterInstance(int objectId, L2NpcTemplate template) {
        super(objectId, template);
    }

    @Override
    protected final boolean checkVillageMasterRace(PlayerClass pclass) {
        if (pclass == null) return false;
        return pclass.isOfRace(Race.Human) || pclass.isOfRace(Race.Elf);
    }

    @Override
    protected final boolean checkVillageMasterTeachType(PlayerClass pclass) {
        if (pclass == null) return false;
        return pclass.isOfType(ClassType.Fighter);
    }
}
