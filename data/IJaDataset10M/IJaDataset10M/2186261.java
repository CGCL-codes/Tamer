package org.openaion.gameserver.skill.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.apache.log4j.Logger;
import org.openaion.gameserver.configs.main.CustomConfig;
import org.openaion.gameserver.geo.GeoEngine;
import org.openaion.gameserver.model.gameobjects.player.Player;
import org.openaion.gameserver.skill.model.DashParam;
import org.openaion.gameserver.skill.model.Effect;
import org.openaion.gameserver.skill.model.DashParam.DashType;
import org.openaion.gameserver.utils.MathUtil;
import org.openaion.gameserver.world.World;

/**
 * @author Sylar
 * @edit kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RandomMoveLocEffect")
public class RandomMoveLocEffect extends EffectTemplate {

    @XmlAttribute(name = "direction")
    private String direction;

    @XmlAttribute(name = "value")
    private int value;

    @Override
    public void applyEffect(Effect effect) {
        DashParam dash = effect.getDashParam();
        World.getInstance().updatePosition((Player) effect.getEffected(), dash.getX(), dash.getY(), dash.getZ(), (byte) dash.getHeading(), false);
    }

    @Override
    public void calculate(Effect effect) {
        if (!(effect.getEffected() instanceof Player)) return;
        Player player = (Player) effect.getEffected();
        double radian = Math.toRadians(MathUtil.convertHeadingToDegree(player.getHeading()));
        float x = player.getX();
        float y = player.getY();
        float z = player.getZ();
        int worldId = player.getWorldId();
        float x2 = 0;
        float y2 = 0;
        float lastSee = 0;
        float lastNonSee = 0;
        if (direction.equals("FRONT")) {
            x2 = (float) (x + (value * Math.cos(radian)));
            y2 = (float) (y + (value * Math.sin(radian)));
            this.setDashParam(effect, x2, y2, z + 2);
            super.calculate(effect);
            return;
        } else if (direction.equals("BACK")) {
            x2 = (float) (x + (value * Math.cos(Math.PI + radian)));
            y2 = (float) (y + (value * Math.sin(Math.PI + radian)));
            this.setDashParam(effect, x2, y2, z + 2);
            super.calculate(effect);
            float temp = 0;
            while (lastNonSee - lastSee >= 0.5) {
                temp = (lastNonSee - lastSee) / 2 + lastSee;
                x2 = (float) (x + (temp * Math.cos(Math.PI + radian)));
                y2 = (float) (y + (temp * Math.sin(Math.PI + radian)));
                lastNonSee = temp;
            }
            x2 = (float) (x + (lastSee * Math.cos(Math.PI + radian)));
            y2 = (float) (y + (lastSee * Math.sin(Math.PI + radian)));
        } else {
            Logger.getLogger(RandomMoveLocEffect.class).error("Cannot move without direction");
            return;
        }
        this.setDashParam(effect, x2, y2, z + 2);
        super.calculate(effect);
    }

    private void setDashParam(Effect effect, float x, float y, float z) {
        Player player = (Player) effect.getEffected();
        Logger.getLogger(RandomMoveLocEffect.class).debug(player.getObjectId() + " moving " + value + "m direction " + direction.toString());
        Logger.getLogger(RandomMoveLocEffect.class).debug(player.getObjectId() + " x: " + x + " y: " + y + " z: " + z);
        effect.setDashParam(new DashParam(DashType.RANDOMMOVELOC, x, y, z, player.getHeading()));
    }
}
