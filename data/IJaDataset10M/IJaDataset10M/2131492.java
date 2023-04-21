package mhhc.htmlcomponents.component;

import mhhc.htmlcomponents.ComponentIterator;
import mhhc.htmlcomponents.layout.LayoutStrategy;

public interface Container extends Component {

    /**
     * Adds a component to the container
     */
    public void addComponent(Component c);

    /**
     * Sets the layout stategy that is used to layout the components on the container
     */
    public void setLayoutStrategy(LayoutStrategy layoutstrategy);

    public ComponentIterator getChildComponents();
}
