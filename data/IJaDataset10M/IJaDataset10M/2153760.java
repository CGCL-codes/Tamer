package net.jgf.example.tanks.logic;

import net.jgf.config.Configurable;
import net.jgf.core.naming.Register;
import net.jgf.core.state.StateHelper;
import net.jgf.entity.Entity;
import net.jgf.entity.EntityGroup;
import net.jgf.example.tanks.entity.Bullet;
import net.jgf.example.tanks.entity.Player;
import net.jgf.example.tanks.entity.Tank;
import net.jgf.example.tanks.logic.flow.MissionLogic;
import net.jgf.jme.scene.DefaultJmeScene;
import net.jgf.loader.entity.pool.EntityPoolLoader;
import net.jgf.logic.action.BaseLogicAction;
import net.jgf.scene.SimpleSceneManager;
import net.jgf.settings.StringSetting;
import net.jgf.system.Jgf;
import org.apache.log4j.Logger;
import com.jme.scene.Node;

/**
 *
 */
@Configurable
public class CleanupGameAction extends BaseLogicAction {

    /**
	 * Class logger
	 */
    private static final Logger logger = Logger.getLogger(CleanupGameAction.class);

    @Register(ref = "logic/root/ingame/mission")
    private MissionLogic missionLogic;

    @Register(ref = "logic/action/newgame/init")
    private NewGameAction newGameAction;

    @Register(ref = "settings/game/map")
    private StringSetting mapSetting;

    @Override
    public void perform(Object arg) {
        logger.info("Cleanup game");
        EntityPoolLoader entityLoader = Jgf.getDirectory().getObjectAs("loader/entity/pool", EntityPoolLoader.class);
        SimpleSceneManager sceneManager = Jgf.getDirectory().getObjectAs("scene/manager", SimpleSceneManager.class);
        DefaultJmeScene scene = (DefaultJmeScene) sceneManager.getScene();
        if (scene != null) {
            Node bulletNode = (Node) scene.getRootNode().getChild("bullets");
            EntityGroup bullets = Jgf.getDirectory().getObjectAs("entity/root/bullets", EntityGroup.class);
            for (Entity bullet : bullets.children()) {
                if (bullet.isActive()) {
                    ((Bullet) bullet).withdraw(bullets, bulletNode);
                    StateHelper.deactivateAndUnload(bullet);
                    bullet.clearStateObservers();
                    entityLoader.returnToPool(bullet);
                }
            }
            EntityGroup enemies = Jgf.getDirectory().getObjectAs("entity/root/enemies", EntityGroup.class);
            for (Entity enemy : enemies.children()) {
                ((Tank) enemy).withdraw(enemies, scene.getRootNode());
                StateHelper.deactivateAndUnload(enemy);
            }
            EntityGroup tanks = Jgf.getDirectory().getObjectAs("entity/root/tanks", EntityGroup.class);
            for (Entity tank : tanks.children()) {
                ((Tank) tank).withdraw(tanks, scene.getRootNode());
                StateHelper.deactivateAndUnload(tank);
            }
            EntityGroup players = Jgf.getDirectory().getObjectAs("entity/root/players", EntityGroup.class);
            for (Entity player : players.children()) {
                ((Player) player).setTank(null);
            }
        }
    }
}
