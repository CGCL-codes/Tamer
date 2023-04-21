package org.openremote.android.controller.component;

import java.util.Hashtable;
import org.w3c.dom.Element;
import android.content.Context;
import org.openremote.android.controller.component.control.button.ButtonBuilder;
import org.openremote.android.controller.component.Component;
import org.openremote.android.controller.component.ComponentBuilder;

/**
 * A factory for creating Component objects.
 * 
 * We hardcode the support here.  Eventually we will want to refactor to have an external list, with a IoC type file. 
 * 
 * @author marcf@openremote.org
 */
public class ComponentFactory {

    /** The component builders. */
    private Hashtable<String, ComponentBuilder> componentBuilders;

    public ComponentFactory(Context context) {
        componentBuilders = new Hashtable();
        componentBuilders.put("button", new ButtonBuilder(context));
    }

    /**
     * Gets the component.
     * 
     * @param componentElement the component element
     * @param commandParam the command param
     * 
     * @return the component
     */
    public Component getComponent(Element componentElement, String commandParam) {
        String componentType = componentElement.getTagName();
        ComponentBuilder componentBuilder = componentBuilders.get(componentType);
        if (componentBuilder == null) {
            System.out.println("No Such component builder with the component " + componentElement.getTagName());
        }
        return (Component) componentBuilder.build(componentElement, commandParam);
    }
}
