package gui.dialog.prop.panel;

import interfaces.Interface;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import bot.Settings;

public class WarningsPropPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JCheckBox stealthCheckBox = new JCheckBox("Enable attack warnings");

    private JTextField useragentField = new JTextField(12);

    public WarningsPropPanel() {
        this.setBorder(BorderFactory.createTitledBorder("Warnings"));
        JPanel upper = new JPanel();
        upper.setLayout(new BoxLayout(upper, BoxLayout.Y_AXIS));
        setFields();
        JPanel useragentPanel = new JPanel();
        useragentPanel.setLayout(new BoxLayout(useragentPanel, BoxLayout.X_AXIS));
        JPanel useragentLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel useragentLabel = new JLabel("User agent (advanced)       ");
        useragentLabel.setToolTipText("<html>The browser user agent the bot must use. If you don\'t know what you are doing, don\'t do it ;)</html>");
        useragentLabelPanel.add(useragentLabel);
        useragentPanel.add(useragentLabelPanel);
        JPanel useragentFieldPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        useragentFieldPanel.add(useragentField);
        useragentPanel.add(useragentFieldPanel);
        upper.add(useragentPanel);
        JPanel stealthPanel = new JPanel();
        stealthPanel.setLayout(new BoxLayout(stealthPanel, BoxLayout.X_AXIS));
        JPanel stealthCheckBoxPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        stealthCheckBox.setToolTipText("<html>If stealth mode is enabled the bot will calculate<br>when it can build and upgrade. This will make it even harder for hunters<br> to spot the bot. The only downside is when resources are gained from attacking troops,<br>the bot cannot know this and will probably wait unnecessary long.<br>I suggest to turn on stealth mode unless you are in a hurry.</html>");
        stealthCheckBoxPanel.add(stealthCheckBox);
        stealthPanel.add(stealthCheckBoxPanel);
        upper.add(stealthPanel);
        this.add(upper);
    }

    /**
	 * Fill the text fields with existing data
	 */
    public void setFields() {
        if (Settings.stealthModeEnabled) stealthCheckBox.setSelected(true); else stealthCheckBox.setSelected(false);
        useragentField.setText(Settings.browserUserAgent);
    }

    /**
	 * Save all data in this panel
	 */
    public void saveFields() {
        Interface.setStealthSettings(useragentField.getText().trim(), stealthCheckBox.isSelected(), false);
    }
}
