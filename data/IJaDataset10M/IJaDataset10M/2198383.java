package au.vermilion.desktop.GUI;

import au.vermilion.desktop.AudioRouter;
import au.vermilion.desktop.ParameterBase;
import au.vermilion.desktop.parameters.ParameterSwitch;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

public class SwitchListener implements ActionListener {

    /**
     * The parameter this listener is bound to (the slider is internal).
     */
    private ParameterSwitch param = null;

    private AudioRouter router = null;

    public SwitchListener(ParameterBase switchParam, AudioRouter owner) {
        param = (ParameterSwitch) switchParam;
        router = owner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        param.value = (short) ((JComboBox) e.getSource()).getSelectedIndex();
        router.setSecondarySavingNeeded();
    }
}
