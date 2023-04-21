package jriaffe.client;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;

/**
 * ButtonBar is not a visual component.  It is used by the framework to
 * display the buttons on the top of the window.  Applications should
 * create one of these and call ApplicationWindow.NOTIFY_ADDBUTTON_BAR
 * to put buttons on the window.
 * 
 * @author preisler
 */
public class ButtonBar {

    private String name;

    private Object handler = null;

    private List<JButton> buttons = new ArrayList<JButton>();

    public ButtonBar() {
    }

    public ButtonBar(String name) {
        this.name = name;
    }

    public void addButton(JButton button) {
        buttons.add(button);
    }

    public void setEventHandler(Object handler) {
        this.handler = handler;
    }

    public Object getEventHandler() {
        return handler;
    }

    /**
     * @return the buttons
     */
    public List<JButton> getButtons() {
        return buttons;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
