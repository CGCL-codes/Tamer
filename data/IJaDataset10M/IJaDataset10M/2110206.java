package org.openremote.android.console.bindings;

import org.w3c.dom.Node;
import android.util.Log;

/**
 * The super class of control component, which include button, switch and slider.
 */
@SuppressWarnings("serial")
public class Control extends Component {

    /**
    * Builds the control component by parse component node, 
    * which include button, switch and slider.
    * 
    * @param node the node
    * 
    * @return the control component
    */
    public static Component buildWithXML(Node node) {
        Component component = null;
        if (node == null) {
            Log.e("OpenRemote-COMPONENT", "The node is null in buildWithXML.");
            return null;
        }
        if (BUTTON.equals(node.getNodeName())) {
            component = new ORButton(node);
        } else if (SWITCH.equals(node.getNodeName())) {
            component = new Switch(node);
        } else if (SLIDER.equalsIgnoreCase(node.getNodeName())) {
            component = new Slider(node);
        } else if (COLORPICKER.equals(node.getNodeName())) {
            component = new ColorPicker(node);
        }
        return component;
    }
}
