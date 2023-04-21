package dioscuri.config;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import dioscuri.GUI;

/**
 *
 * @author Bram Lohman
 * @author Bart Kiers
 */
@SuppressWarnings("serial")
public class SimpleConfigDialog extends ConfigurationDialog {

    private JFormattedTextField updateIntField;

    dioscuri.config.Emulator emuConfig;

    /**
     *
     * @param parent -
     * @param moduleType -
     */
    public SimpleConfigDialog(GUI parent, ModuleType moduleType) {
        super(parent, moduleType.toString().toUpperCase() + " Configuration", false, moduleType);
    }

    /**
     * Read in params from XML.
     */
    @Override
    protected void readInParams() {
        emuConfig = parent.getEmuConfig();
        Integer updateInt = 0;
        if (moduleType.equals(ModuleType.PIT)) {
            updateInt = emuConfig.getArchitecture().getModules().getPit().getClockrate().intValue();
        } else if (moduleType.equals(ModuleType.KEYBOARD)) {
            updateInt = emuConfig.getArchitecture().getModules().getKeyboard().getUpdateintervalmicrosecs().intValue();
        } else if (moduleType.equals(ModuleType.VGA)) {
            updateInt = emuConfig.getArchitecture().getModules().getVideo().getUpdateintervalmicrosecs().intValue();
        }
        this.updateIntField.setValue(updateInt);
    }

    @Override
    protected void initSaveButton() {
        this.saveButton = new JButton("Save");
        this.saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveParams();
            }
        });
    }

    /**
     * Initialise the panel for data entry.
     */
    @Override
    protected void initMainEntryPanel() {
        String labelText = "  Update Int (microSecs)";
        if (this.moduleType == ModuleType.PIT) {
            labelText = "  Clock Rate";
        }
        JLabel updateIntLabel = new JLabel(labelText);
        updateIntField = new JFormattedTextField();
        updateIntField.setValue(0);
        updateIntField.setColumns(10);
        mainEntryPanel = new JPanel(new GridLayout(10, 3));
        Border blackline = BorderFactory.createLineBorder(Color.black);
        mainEntryPanel.setBorder(blackline);
        for (int i = 0; i < 3; i++) {
            mainEntryPanel.add(new JLabel(""));
        }
        mainEntryPanel.add(updateIntLabel);
        mainEntryPanel.add(updateIntField);
        mainEntryPanel.add(new JLabel(""));
        for (int i = 0; i < 24; i++) {
            mainEntryPanel.add(new JLabel(""));
        }
    }

    /**
     * Get the params from the GUI.
     * 
     * @return object array of params.
     */
    @Override
    protected Emulator getParamsFromGui() {
        if (moduleType.equals(ModuleType.PIT)) {
            emuConfig.getArchitecture().getModules().getPit().setClockrate(BigInteger.valueOf(((Number) updateIntField.getValue()).intValue()));
        } else if (moduleType.equals(ModuleType.KEYBOARD)) {
            emuConfig.getArchitecture().getModules().getKeyboard().setUpdateintervalmicrosecs(BigInteger.valueOf(((Number) updateIntField.getValue()).intValue()));
        } else if (moduleType.equals(ModuleType.VGA)) {
            emuConfig.getArchitecture().getModules().getVideo().setUpdateintervalmicrosecs(BigInteger.valueOf(((Number) updateIntField.getValue()).intValue()));
        }
        return emuConfig;
    }
}
