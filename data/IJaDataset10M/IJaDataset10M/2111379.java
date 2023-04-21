package diet.parameters;

import java.util.Vector;

/**
 *
 * @author user
 */
public interface Fixed {

    /** Creates a new instance of Fixed */
    public Vector getPermittedValues();

    public int indexOfPermittedValue(Object o);

    public void addToPermittedValues(Object o);
}
