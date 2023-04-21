package gnu.java.beans.encoder;

import java.util.Collection;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.Statement;
import java.util.Iterator;

/** <p>A <code>PersistenceDelegate</code> implementation that calls
 * the no-argument constructor to create the Collection instance and
 * uses an iterator to add all the objects it reaches through it.</p>
 * 
 * <p>It is used for <code>Set</code> and <code>List</code>
 * implementations.</p>
 * 
 * @author Robert Schuster (robertschuster@fsfe.org)
 */
public class CollectionPersistenceDelegate extends PersistenceDelegate {

    protected Expression instantiate(Object oldInstance, Encoder out) {
        return new Expression(oldInstance, oldInstance.getClass(), "new", null);
    }

    protected void initialize(Class type, Object oldInstance, Object newInstance, Encoder out) {
        Iterator ite = ((Collection) oldInstance).iterator();
        while (ite.hasNext()) {
            out.writeStatement(new Statement(oldInstance, "add", new Object[] { ite.next() }));
        }
    }
}
