package se.netroid.physic;

public abstract class CollisionHandlerFactory {

    private static CollisionHandlerFactory instance;

    public static synchronized CollisionHandlerFactory getInstance() {
        if (instance == null) {
            instance = new DefaultCollisionHandlerFactory();
        }
        return instance;
    }

    public abstract CollisionHandler getShipCollisionHandler();

    public abstract CollisionHandler getAsteroidCollisionHandler();

    public abstract CollisionHandler getBulletCollisionHandler();

    public abstract CollisionHandler getExplosionCollisionHandler();
}
