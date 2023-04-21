package midlet;

/**
 *
 * @author mh
 */
public interface Storable {

    public int getStateSize();

    /**
   * Creates a new instance of Storable 
   */
    public byte[] storeState();

    public void loadState(byte[] state);
}
