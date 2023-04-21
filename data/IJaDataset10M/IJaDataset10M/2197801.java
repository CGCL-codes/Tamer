package gameserver.skillengine.effect;

import gameserver.model.TaskId;
import gameserver.model.gameobjects.Creature;
import gameserver.model.gameobjects.Trap;
import gameserver.model.templates.spawn.SpawnTemplate;
import gameserver.skillengine.model.Effect;
import gameserver.spawnengine.SpawnEngine;
import gameserver.utils.ThreadPoolManager;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.Future;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonTrapEffect")
public class SummonTrapEffect extends SummonEffect {

    @XmlAttribute(name = "skill_id", required = true)
    protected int skillId;

    @XmlAttribute(name = "time", required = true)
    protected int time;

    @Override
    public void applyEffect(Effect effect) {
        Creature effector = effect.getEffector();
        SpawnEngine spawnEngine = SpawnEngine.getInstance();
        float x = effector.getX();
        float y = effector.getY();
        float z = effector.getZ();
        byte heading = effector.getHeading();
        int worldId = effector.getWorldId();
        int instanceId = effector.getInstanceId();
        SpawnTemplate spawn = spawnEngine.addNewSpawn(worldId, instanceId, npcId, x, y, z, heading, 0, 0, true, true);
        final Trap trap = spawnEngine.spawnTrap(spawn, instanceId, effector, skillId);
        Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                trap.getController().onDespawn(true);
            }
        }, time * 1000);
        trap.getController().addTask(TaskId.DESPAWN, task);
    }

    @Override
    public void calculate(Effect effect) {
        super.calculate(effect);
    }
}
