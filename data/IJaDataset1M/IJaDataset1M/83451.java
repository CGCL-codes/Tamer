package net.jgf.loader;

/**
 * The EntityManager holds and manages the level entities, and provides
 * methods to create new ones.
 */
public interface LoaderManager {

    public Loader<?> getLoader(String id);

    public void addLoader(Loader<?> modelLoader);
}
