package edu.ua.j3dengine.processors;

import edu.ua.j3dengine.core.DynamicGameObject;
import edu.ua.j3dengine.core.World;
import edu.ua.j3dengine.core.mgmt.GameObjectManager;
import java.util.Collection;

public class GameLogicProcessor extends Processor {

    public GameLogicProcessor() {
        super("GameLogicProcessor");
    }

    public void performConcreteInitialize() {
        GameObjectManager.getInstance();
    }

    public void performConcreteRelease() {
    }

    public void performConcreteExecute() {
        World world = GameObjectManager.getInstance().getWorld();
        world.update();
        Collection<DynamicGameObject> objects = GameObjectManager.getInstance().getAllDynamicObjects();
        for (DynamicGameObject object : objects) {
            object.update();
        }
    }

    public String getId() {
        return getClass().getCanonicalName();
    }
}
