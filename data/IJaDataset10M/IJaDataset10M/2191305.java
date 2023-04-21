package thing.spi;

/**
 * Receives notifications about changes in the XML file
 * @author Michael Nascimento Santos
 */
public interface EditorChangeListener {

    public void modified();

    public void unmodified();
}
