package net.sourceforge.javabits.factory;

/**
 * @author Jochen Kuhnle
 */
public class SingletonFactory<C> implements Factory<C> {

    private final C singleton;

    public SingletonFactory(C singleton) {
        this.singleton = singleton;
    }

    /**
     * @see net.sourceforge.javabits.factory.Factory#create()
     */
    public C create() {
        return singleton;
    }
}
