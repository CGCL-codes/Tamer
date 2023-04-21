package com.l2jserver.gameserver.skills.effects;

import java.util.logging.Logger;
import com.l2jserver.Config;
import com.l2jserver.gameserver.GeoData;
import com.l2jserver.gameserver.model.L2Effect;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.network.serverpackets.FlyToLocation;
import com.l2jserver.gameserver.network.serverpackets.ValidateLocation;
import com.l2jserver.gameserver.network.serverpackets.FlyToLocation.FlyType;
import com.l2jserver.gameserver.skills.Env;
import com.l2jserver.gameserver.templates.effects.EffectTemplate;
import com.l2jserver.gameserver.templates.skills.L2EffectType;

public class EffectEnemyCharge extends L2Effect {

    static final Logger _log = Logger.getLogger(EffectEnemyCharge.class.getName());

    private int _x, _y, _z;

    public EffectEnemyCharge(Env env, EffectTemplate template) {
        super(env, template);
    }

    @Override
    public L2EffectType getEffectType() {
        return L2EffectType.BUFF;
    }

    @Override
    public boolean onStart() {
        final int curX = getEffector().getX();
        final int curY = getEffector().getY();
        final int curZ = getEffector().getZ();
        double dx = getEffected().getX() - curX;
        double dy = getEffected().getY() - curY;
        double dz = getEffected().getZ() - curZ;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 2000) {
            _log.info("EffectEnemyCharge was going to use invalid coordinates for characters, getEffector: " + curX + "," + curY + " and getEffected: " + getEffected().getX() + "," + getEffected().getY());
            return false;
        }
        int offset = Math.max((int) distance - getSkill().getFlyRadius(), 30);
        double cos;
        double sin;
        offset -= Math.abs(dz);
        if (offset < 5) offset = 5;
        if (distance < 1 || distance - offset <= 0) return false;
        sin = dy / distance;
        cos = dx / distance;
        _x = curX + (int) ((distance - offset) * cos);
        _y = curY + (int) ((distance - offset) * sin);
        _z = getEffected().getZ();
        if (Config.GEODATA > 0) {
            Location destiny = GeoData.getInstance().moveCheck(getEffector().getX(), getEffector().getY(), getEffector().getZ(), _x, _y, _z, getEffector().getInstanceId());
            _x = destiny.getX();
            _y = destiny.getY();
        }
        getEffector().broadcastPacket(new FlyToLocation(getEffector(), _x, _y, _z, FlyType.CHARGE));
        getEffector().setXYZ(_x, _y, _z);
        getEffector().broadcastPacket(new ValidateLocation(getEffector()));
        return true;
    }

    @Override
    public boolean onActionTime() {
        return false;
    }
}
