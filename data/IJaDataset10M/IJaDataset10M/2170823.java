package ingenias.editor.widget;

import org.jgraph.graph.*;
import java.util.*;

public class DeploymentPackageWidgetPreferences extends INGENIASObjectWidgetPreferences {

    Hashtable preferredWidget = new Hashtable();

    Hashtable defaultValues = new Hashtable();

    public DeploymentPackageWidgetPreferences() {
        super();
        String[] preferredOrder = { "id", "Description", "AgentsDeployed", "Parameters", "" };
        this.setPreferredOrder(preferredOrder);
        Vector result = null;
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
