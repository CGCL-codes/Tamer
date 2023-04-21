package dioscuri.config;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import dioscuri.GUI;
import dioscuri.config.Emulator.Architecture.Modules.Cpu;

/**
 *
 * @author Bram Lohman
 * @author Bart Kiers
 */
@SuppressWarnings("serial")
public class CpuConfigDialog extends ConfigurationDialog {

    private JFormattedTextField speedField;

    private JRadioButton cpu32Button;

    private JRadioButton cpu16Button;

    dioscuri.config.Emulator emuConfig;

    /**
     *
     * @param parent -
     */
    public CpuConfigDialog(GUI parent) {
        super(parent, "CPU Configuration", false, ModuleType.CPU);
    }

    /**
     * Read in params from XML.
     */
    @Override
    protected void readInParams() {
        emuConfig = parent.getEmuConfig();
        Cpu cpu = emuConfig.getArchitecture().getModules().getCpu();
        Integer cpuSpeed = cpu.getSpeedmhz().intValue();
        this.speedField.setValue(cpuSpeed);
        Boolean cpu32Bit = cpu.isCpu32Bit();
        if (cpu32Bit) this.cpu32Button.setSelected(true); else this.cpu16Button.setSelected(true);
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
        JLabel bitLabel = new JLabel("  CPU bits:");
        JLabel speedLabel = new JLabel("  Speed (MHz)");
        String cpu32String = "32-bit";
        String cpu16String = "16-bit";
        cpu32Button = new JRadioButton(cpu32String);
        cpu16Button = new JRadioButton(cpu16String);
        cpu32Button.setSelected(true);
        ButtonGroup cpuBit = new ButtonGroup();
        cpuBit.add(cpu32Button);
        cpuBit.add(cpu16Button);
        speedField = new JFormattedTextField();
        speedField.setValue(0);
        speedField.setColumns(10);
        mainEntryPanel = new JPanel(new GridLayout(10, 3));
        Border blackline = BorderFactory.createLineBorder(Color.black);
        mainEntryPanel.setBorder(blackline);
        mainEntryPanel.add(bitLabel);
        mainEntryPanel.add(cpu32Button);
        mainEntryPanel.add(cpu16Button);
        for (int i = 0; i < 3; i++) {
            mainEntryPanel.add(new JLabel(""));
        }
        mainEntryPanel.add(speedLabel);
        mainEntryPanel.add(speedField);
        mainEntryPanel.add(new JLabel(""));
        for (int i = 0; i < 21; i++) {
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
        Cpu cpu = emuConfig.getArchitecture().getModules().getCpu();
        cpu.setSpeedmhz(BigDecimal.valueOf(((Number) speedField.getValue()).intValue()));
        cpu.setCpu32Bit(cpu32Button.isSelected());
        return emuConfig;
    }
}
