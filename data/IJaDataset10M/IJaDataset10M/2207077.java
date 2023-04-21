package org.sdrinovsky.sdsvn.properties;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToggleButton;

/**
 *
 * @author sdrinovsky
 */
public class SelectedProperty implements org.jdesktop.application.SessionStorage.Property {

    /**
   * Class ButtonState
   *
   *
   * @author
   * @version $Revision: 75 $
   */
    public static class SelectedState {

        public boolean selected = false;

        /**
     * Constructor ButtonState
     *
     *
     */
        public SelectedState() {
        }

        /**
     * Constructor ButtonState
     *
     *
     * @param state
     *
     */
        public SelectedState(boolean state) {
            selected = state;
        }

        /**
     * Method setPushed
     *
     *
     * @param state
     *
     */
        public void setSelected(boolean state) {
            selected = state;
        }

        /**
     * Method getPushed
     *
     *
     * @return
     *
     */
        public boolean isSelected() {
            return selected;
        }
    }

    private void checkComponent(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("null component");
        }
        if ((component instanceof JToggleButton) || (component instanceof JCheckBox) || (component instanceof JCheckBoxMenuItem)) {
            return;
        }
        throw new IllegalArgumentException("invalid component");
    }

    /**
   * Method getSessionState
   *
   *
   * @param c
   *
   * @return
   *
   */
    @Override
    public Object getSessionState(Component c) {
        checkComponent(c);
        SelectedState state = new SelectedState();
        if (c instanceof JToggleButton) {
            state.selected = ((JToggleButton) c).isSelected();
        } else if (c instanceof JCheckBox) {
            state.selected = ((JCheckBox) c).isSelected();
        } else if (c instanceof JCheckBoxMenuItem) {
            state.selected = ((JCheckBoxMenuItem) c).isSelected();
        }
        return state;
    }

    /**
   * Method setSessionState
   *
   *
   * @param c
   * @param state
   *
   */
    @Override
    public void setSessionState(Component c, Object state) {
        checkComponent(c);
        if (c instanceof JToggleButton) {
            ((JToggleButton) c).setSelected(((SelectedState) state).selected);
        } else if (c instanceof JCheckBox) {
            ((JCheckBox) c).setSelected(((SelectedState) state).selected);
        } else if (c instanceof JCheckBoxMenuItem) {
            ((JCheckBoxMenuItem) c).setSelected(((SelectedState) state).selected);
        }
    }
}
