package gui.editors;

import java.awt.LayoutManager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import model.PropertyChangeNotifier;
import model.PropertyChangeObserver;

public abstract class PropertyEditor extends JPanel implements PropertyChangeNotifier {

    private static final long serialVersionUID = 1L;

    List<PropertyChangeObserver> obs = new LinkedList<PropertyChangeObserver>();

    /** the id of the property being editted */
    String propId;

    public PropertyEditor(String propId) {
        super();
        this.propId = propId;
    }

    public String getPropertyId() {
        return propId;
    }

    public void register(String propId, PropertyChangeObserver o) {
        obs.add(o);
    }

    void notifyAll(Object x) {
        Iterator<PropertyChangeObserver> i = obs.iterator();
        while (i.hasNext()) i.next().notify(getPropertyId(), x);
    }
}
