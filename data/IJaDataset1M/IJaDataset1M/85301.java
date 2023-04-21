package pogvue.gui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public abstract class JalTwoStringToggleAction extends JalToggleAction implements ItemListener, ToggleI {

    private final String altName;

    public JalTwoStringToggleAction(String name, String altName, boolean selected) {
        super(name, selected);
        this.altName = altName;
    }

    public String getAltName() {
        return altName;
    }

    public void itemStateChanged(ItemEvent evt) {
        System.out.println("Got itemStateChanged in JalTwoStringToggleAction");
        setState(evt.getStateChange() == ItemEvent.SELECTED);
    }

    public void setGroup(JalToggleGroup group) {
        System.err.println("Error: Cannot add JalTwoStringToggleAction to a group");
    }

    public void getArgsFromSource(JalTwoStringMenuItem mi, ActionEvent evt) {
        putArg("state", Boolean.valueOf(selected).toString());
    }
}
