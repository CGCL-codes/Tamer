package gameserver.model.gameobjects;

import gameserver.ai.npcai.HomingAi;
import gameserver.controllers.HomingController;
import gameserver.controllers.NpcController;
import gameserver.controllers.movement.ActionObserver;
import gameserver.model.templates.VisibleObjectTemplate;
import gameserver.model.templates.spawn.SpawnTemplate;
import java.util.concurrent.Future;

public class Homing extends NpcWithCreator {

    /**
	 * Number of performed attacks
	 */
    private int attackCount;

    /**
	 * counts number of usages of attack/skill
	 */
    private int counter = 0;

    private ActionObserver observer = null;

    private Future<?> task = null;

    /**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
    public Homing(int objId, NpcController controller, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate) {
        super(objId, controller, spawnTemplate, objectTemplate);
    }

    @Override
    public Homing getOwner() {
        return (Homing) this;
    }

    @Override
    public HomingController getController() {
        return (HomingController) super.getController();
    }

    /**
	 * @param attackCount
	 *            the attackCount to set
	 */
    public void setAttackCount(int attackCount) {
        this.attackCount = attackCount;
    }

    /**
	 * @return the attackCount
	 */
    public int getAttackCount() {
        return attackCount;
    }

    @Override
    public void initializeAi() {
        this.ai = new HomingAi();
        ai.setOwner(this);
    }

    /**
	 * @return NpcObjectType.HOMING
	 */
    @Override
    public NpcObjectType getNpcObjectType() {
        return NpcObjectType.HOMING;
    }

    public void setCounter(int number) {
        this.counter = number;
    }

    public int getCounter() {
        return this.counter;
    }

    public void setObserver(ActionObserver acO) {
        this.observer = acO;
    }

    public ActionObserver getObserver() {
        return this.observer;
    }

    public void setDespawnTask(Future<?> task) {
        this.task = task;
    }

    public Future<?> getDespawnTask() {
        return this.task;
    }

    public void cancelDespawnTask() {
        if (task != null) {
            task.cancel(true);
            task = null;
        }
    }
}
