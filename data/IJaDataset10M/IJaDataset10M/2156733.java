package com.l2jserver.gameserver.skills;

import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.L2Skill;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2CubicInstance;

/**
 *
 * An Env object is just a class to pass parameters to a calculator such as L2PcInstance, L2ItemInstance, Initial value.
 *
 */
public final class Env {

    public L2Character player;

    public L2CubicInstance cubic;

    public L2Character target;

    public L2ItemInstance item;

    public L2Skill skill;

    public L2Effect effect;

    public double value;

    public double baseValue;

    public boolean skillMastery = false;

    public byte shld = 0;

    public boolean ss = false;

    public boolean sps = false;

    public boolean bss = false;

    public Env() {
    }

    public Env(byte shd, boolean s, boolean ps, boolean bs) {
        shld = shd;
        ss = s;
        sps = ps;
        bss = bs;
    }
}
