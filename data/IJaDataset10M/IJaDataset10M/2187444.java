package freestyleLearningGroup.independent.plotter;

import java.util.Vector;

public class FLGComponentGroup2D {

    private Vector components = new Vector();

    public FLGComponentGroup2D() {
    }

    public int size() {
        return components.size();
    }

    public void add(FLGComponent2D component) {
        components.addElement(component);
    }

    public FLGComponent2D get(int ind) {
        return (FLGComponent2D) components.elementAt(ind);
    }
}
