package es.caib.zkib.debug;

import java.util.Iterator;
import java.util.Vector;
import org.zkoss.zk.ui.Component;

public class ComponentChildren extends TreeObject {

    Component component;

    public ComponentChildren(Component component) {
        super();
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Vector createChildren() {
        Vector v = new Vector();
        for (Iterator it = component.getChildren().iterator(); it.hasNext(); ) {
            ComponentTree tree = new ComponentTree((Component) it.next());
            v.add(tree);
        }
        return v;
    }

    public String getKey() {
        return "Children";
    }

    public String getValue() {
        return "";
    }
}
