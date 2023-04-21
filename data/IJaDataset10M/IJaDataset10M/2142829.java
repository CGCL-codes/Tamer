package ingenias.editor.widget;

import org.jgraph.graph.*;
import java.util.*;

public class UMLActivityDiagramDataEntityWidgetPreferences extends EntityWidgetPreferences {

    Hashtable preferredWidget = new Hashtable();

    Hashtable defaultValues = new Hashtable();

    public UMLActivityDiagramDataEntityWidgetPreferences() {
        super();
        String[] preferredOrder = { "id", "Description", "" };
        this.setPreferredOrder(preferredOrder);
        Vector result = null;
        preferredWidget.put("Description", ingenias.editor.widget.ScrolledTArea.class);
        result = new Vector();
        defaultValues.put("Description", result);
    }

    ;

    public Object getWidget(String attName) throws IllegalAccessException, InstantiationException {
        Class result = null;
        ConfigurableWidget instance = null;
        if (preferredWidget.get(attName) != null) result = ((Class) preferredWidget.get(attName)); else return (super.getWidget(attName));
        if (result != null) {
            instance = (ConfigurableWidget) result.newInstance();
            Vector values = (Vector) defaultValues.get(attName);
            instance.setDefaultValues(values);
        }
        return instance;
    }

    public void configureWidget(ConfigurableWidget cw) {
    }
}
