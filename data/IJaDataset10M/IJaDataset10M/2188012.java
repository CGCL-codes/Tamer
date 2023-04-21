package ao.prophet;

import java.util.Collection;

/**
 * Predicts preferences based on preferance data.
 *
 * Note that both U and I must both correctly implement equals() and hashCode().
 * @param U 
 * @param I 
 */
public interface Prophet<U, I> {

    /**
     * 
     * @param item 
     */
    void addItem(I item);

    /**
     * 
     * @param item 
     */
    void removeItem(I item);

    /**
     * 
     * @param user 
     */
    void removeUser(U user);

    /**
     * 
     * @param user 
     * @param item 
     */
    void likes(U user, I item);

    /**
     * 
     * @param user 
     * @param item 
     */
    void dislikes(U user, I item);

    /**
     * 
     * @param user 
     * @param howMany 
     * @return 
     */
    Collection<I> predict(U user, int howMany);

    /**
     * 
     * @param user 
     * @param howMany 
     * @param filter 
     * @return 
     */
    Collection<I> predict(U user, int howMany, ItemFilter<I> filter);
}
