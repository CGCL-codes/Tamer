package Game.Stores;

import Game.Entity;

/**
 *
 * @author medv4380
 */
public class EntityStore extends Store {

    private static EntityStore single = new EntityStore();

    public static EntityStore get() {
        return single;
    }

    public Entity get(int Index) {
        return (Entity) this.getObject(Index);
    }
}
