package pcgen.cdom.base;

/**
 * This is a transitional class from PCGen 5.15+ to the final CDOM core. It is
 * provided as convenience to hold a set of choices and the number of choices
 * allowed, prior to final implementation of the new choice system.
 * 
 * This is a TransitionChoice that is designed to be stored in a PlayerCharacter
 * file when saved. Thus, encoding and decoding (to a 'persistent' string)
 * methods are provided.
 * 
 * @param <T>
 */
public interface PersistentChoice<T> extends BasicChoice<T> {

    public PersistentChoiceActor<T> getChoiceActor();

    public T decodeChoice(String persistentFormat);

    public String encodeChoice(T item);
}
