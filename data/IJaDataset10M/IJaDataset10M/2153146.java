package gui.editor;

import gui.viewer.AutomatonDrawer;
import gui.viewer.AutomatonPane;
import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 * A tool that handles the creation of states.
 *
 * @author
 */
public class StateTool extends Tool {

    /**
     * Instantiates a new state tool.
     */
    public StateTool(AutomatonPane view, AutomatonDrawer drawer) {
        super(view, drawer);
    }

    /**
     * Gets the tool tip for this tool.
     *
     * @return the tool tip for this tool
     */
    public String getToolTip() {
        return "State Creator";
    }

    /**
     * Returns the tool icon.
     *
     * @return the state tool icon
     */
    protected Icon getIcon() {
        java.net.URL url = getClass().getResource("/ICON/state.gif");
        return new ImageIcon(url);
    }

    /**
     * When the user clicks, one creates a state.
     *
     * @param event the mouse event
     */
    public void mousePressed(MouseEvent event) {
        state = getAutomaton().createColorState(event.getPoint(), getAutomaton().getColor());
        getView().repaint();
    }

    /**
     * When the user drags, one moves the created state.
     *
     * @param event the mouse event
     */
    public void mouseDragged(MouseEvent event) {
        state.setPoint(event.getPoint());
        getView().repaint();
    }

    /**
     * Returns the keystroke to switch to this tool, S.
     *
     * @return the keystroke for this tool
     */
    public KeyStroke getKey() {
        return KeyStroke.getKeyStroke('s');
    }

    /**
     * The state that was created.
     */
    automata.State state = null;
}
