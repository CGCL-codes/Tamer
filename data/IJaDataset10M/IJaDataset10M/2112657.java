package jalview.gui.menus;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public abstract class JalToggleAction extends JalAction implements ItemListener, ToggleI {

    boolean selected;

    JalToggleGroup group;

    public JalToggleAction(String name, boolean selected) {
        super(name);
        this.selected = selected;
        listeners = new Vector();
        addRequiredArg("state");
    }

    public void itemStateChanged(ItemEvent evt) {
        setState(evt.getStateChange() == ItemEvent.SELECTED);
    }

    public void setStateQuietly(boolean state) {
        this.selected = state;
    }

    public void setState(boolean state) {
        setStateQuietly(state);
        if (group != null) {
            group.stateChanged(this);
        } else {
            fireJalActionEvent(new JalActionEvent(this, this, JalActionEvent.SELECT));
        }
    }

    public boolean getState() {
        return selected;
    }

    public void setGroup(JalToggleGroup group) {
        this.group = group;
    }

    public JalToggleGroup getGroup() {
        return group;
    }

    public void getArgsFromSource(JalCheckBoxMenuItem mi, ActionEvent evt) {
        super.getArgsFromSource(mi, evt);
        putArg("state", new Boolean(selected).toString());
    }

    public void getArgsFromSource(JalCommand jc, ActionEvent evt) {
        super.getArgsFromSource(jc, evt);
        if (!containsArg("state")) {
            System.err.println("Error: No STATE arg in JalToggleAction");
        }
    }

    public void getArgsFromSource(JalToggleAction jat, ActionEvent evt) {
        super.getArgsFromSource(jat, evt);
        System.out.println("JTA GAFS");
        putArg("state", new Boolean(selected).toString());
    }

    public void updateActionState(ActionEvent evt) {
        if (!(evt.getSource() instanceof JalCheckBoxMenuItem)) {
            if (containsArg("state")) {
                setState(new Boolean(getArg("state")).booleanValue());
            }
        }
    }
}
