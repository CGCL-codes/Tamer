package gameserver.skillengine.effect;

import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.player.Player;
import gameserver.network.aion.serverpackets.SM_FORCED_MOVE;
import gameserver.skillengine.model.Effect;
import gameserver.skillengine.model.SpellStatus;
import gameserver.utils.PacketSendUtility;
import gameserver.utils.ThreadPoolManager;
import gameserver.world.World;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Sarynth
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PulledEffect")
public class PulledEffect extends EffectTemplate {

    @Override
    public void applyEffect(Effect effect) {
        effect.addToEffectedController();
    }

    @Override
    public void calculate(Effect effect) {
        if (effect.getEffector() instanceof Player && effect.getEffected() != null) {
            effect.addSucessEffect(this);
        }
        effect.setSpellStatus(SpellStatus.NONE);
    }

    @Override
    public void startEffect(final Effect effect) {
        final Creature effector = effect.getEffector();
        final Creature effected = effect.getEffected();
        effect.setAbnormal(EffectId.CANNOT_MOVE.getEffectId());
        effected.getEffectController().setAbnormal(EffectId.CANNOT_MOVE.getEffectId());
        ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                World.getInstance().updatePosition(effected, effector.getX(), effector.getY(), effector.getZ() + 0.25F, effected.getHeading());
                PacketSendUtility.broadcastPacketAndReceive(effected, new SM_FORCED_MOVE(effector, effected));
            }
        }, 1000);
    }

    @Override
    public void endEffect(final Effect effect) {
        effect.getEffected().getEffectController().unsetAbnormal(EffectId.CANNOT_MOVE.getEffectId());
    }
}
