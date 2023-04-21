package jonkoshare.util;

/**
 * Provider interface to hand over an anonymous factory instead of cloning
 * instances or creating heavy weight objects which could be accessed in
 * a more deeper level.
 * 
 * @author onkobu
 *
 * @param <T>
 */
public interface Provider<T> {

    T provide();
}
