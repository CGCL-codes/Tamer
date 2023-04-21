package org.dbc.component.composite_library;

import java.util.Iterator;
import java.util.LinkedList;

public class Application {

    public static void main(String[] args) {
        Composite<Leaf> aComposite = new Composite<Leaf>();
        LinkedList<Component<Leaf>> someComponents = new LinkedList<Component<Leaf>>();
        someComponents.add(new Leaf());
        aComposite.add(new Leaf());
        aComposite.add(new Leaf());
        someComponents.add(aComposite);
        for (Iterator<Component<Leaf>> iter = someComponents.iterator(); iter.hasNext(); ) {
            Component<Leaf> aComponent = iter.next();
            aComponent.operation();
            if (aComponent.isComposite()) {
                aComponent.add(new Leaf());
            }
        }
    }
}
