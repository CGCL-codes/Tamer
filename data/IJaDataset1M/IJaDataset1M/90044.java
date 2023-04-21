package com.l2jserver.gameserver.model.zone.type;

import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.zone.L2ZoneType;

/**
 * @author UnAfraid
 */
public class L2ConditionZone extends L2ZoneType {

    private boolean NO_ITEM_DROP = false;

    private boolean NO_BOOKMARK = false;

    public L2ConditionZone(int id) {
        super(id);
    }

    @Override
    public void setParameter(String name, String value) {
        if (name.equalsIgnoreCase("NoBookmark")) NO_BOOKMARK = Boolean.parseBoolean(value); else if (name.equalsIgnoreCase("NoItemDrop")) NO_ITEM_DROP = Boolean.parseBoolean(value); else super.setParameter(name, value);
    }

    @Override
    protected void onEnter(L2Character character) {
        if (character instanceof L2PcInstance) {
            if (NO_BOOKMARK) character.setInsideZone(L2Character.ZONE_NOBOOKMARK, true);
            if (NO_ITEM_DROP) character.setInsideZone(L2Character.ZONE_NOITEMDROP, true);
        }
    }

    @Override
    protected void onExit(L2Character character) {
        if (character instanceof L2PcInstance) {
            if (NO_BOOKMARK) character.setInsideZone(L2Character.ZONE_NOBOOKMARK, false);
            if (NO_ITEM_DROP) character.setInsideZone(L2Character.ZONE_NOITEMDROP, false);
        }
    }

    @Override
    public void onDieInside(L2Character character) {
    }

    @Override
    public void onReviveInside(L2Character character) {
    }
}
