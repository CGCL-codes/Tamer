package tr.view.filters;

import java.io.Serializable;
import java.util.Vector;
import org.openide.util.NbBundle;
import tr.appl.Constants;
import tr.model.IDGenerator;
import tr.model.criteria.Value;

/**
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class ValueMultiple extends Value implements Serializable {

    private static final IDGenerator multipleIDGenerator = new IDGenerator() {

        public int getNextID() {
            return Constants.ID_FILTER_MULTIPLE;
        }
    };

    private Vector<Value> chosen;

    /** Constructs a new instance. */
    public ValueMultiple() {
        super(NbBundle.getMessage(ValueMultiple.class, "filter-multiple"), multipleIDGenerator);
    }

    public Vector<Value> getChosen() {
        return (chosen == null) ? new Vector<Value>() : chosen;
    }

    public void setChosen(Vector<Value> chosen) {
        this.chosen = chosen;
    }

    public boolean equals(Object object) {
        return object instanceof ValueMultiple;
    }
}
