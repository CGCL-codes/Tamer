package ingenias.editor.widget;

import org.jgraph.graph.*;
import java.util.*;

public class INGENIASCodeComponentWidgetPreferences extends INGENIASObjectWidgetPreferences {

    Hashtable preferredWidget = new Hashtable();

    Hashtable defaultValues = new Hashtable();

    public INGENIASCodeComponentWidgetPreferences() {
        super();
        String[] preferredOrder = { "id", "Language", "Code", "" };
        this.setPreferredOrder(preferredOrder);
        Vector result = null;
        preferredWidget.put("Code", ingenias.editor.widget.ScrolledTArea.class);
        result = new Vector();
        defaultValues.put("Code", result);
        preferredWidget.put("Language", ingenias.editor.widget.CustomJComboBox.class);
        result = new Vector();
        result.add("Java");
        result.add("C++");
        result.add("Plain text");
        result.add("Prolog");
        result.add("Jess");
        result.add("Other");
        defaultValues.put("Language", result);
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
