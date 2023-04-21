package machine.themachine;

/**
 *
 * @author lantern
 */
public abstract class Mapping {

    public abstract long getValue(String view);

    public abstract String getView(long value);

    public abstract String getFirstView(String input) throws NothingToReadException;

    public abstract String getName();

    public abstract String getSample();
}
