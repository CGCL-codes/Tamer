package feature;

import java.util.Vector;

/**
 *
 * @author nautilus
 */
public interface Feature {

    public void loadDatabase(String dataDir);

    public Vector searchImage(String image);
}
