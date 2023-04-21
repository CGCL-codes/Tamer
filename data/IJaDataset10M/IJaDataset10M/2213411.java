package daam.ui.gwt.client;

import java.util.List;
import java.util.Vector;
import daam.ui.gwt.client.component.IControl;

public abstract class ComponentFactory {

    public static List componentFactories = new Vector();

    public abstract IControl getControl(String controlName);
}
