package jorgan.customizer.gui.console;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import jorgan.disposition.Console;
import jorgan.disposition.Elements;
import jorgan.gui.FullScreen;
import jorgan.swing.combobox.BaseComboBoxModel;
import jorgan.swing.layout.DefinitionBuilder;
import jorgan.swing.layout.Group;
import jorgan.swing.layout.DefinitionBuilder.Column;
import bias.Configuration;

/**
 * A panel for a {@link console}.
 */
public class ConsolePanel extends JPanel {

    private static Configuration config = Configuration.getRoot().get(ConsolePanel.class);

    private Console console;

    private JComboBox screenComboBox;

    private JSpinner zoomSpinner;

    public ConsolePanel(Console console) {
        this.console = console;
        setLayout(new BorderLayout());
        add(new Group(new JLabel(Elements.getDisplayName(console))), BorderLayout.NORTH);
        JPanel definitions = new JPanel();
        add(definitions, BorderLayout.CENTER);
        DefinitionBuilder builder = new DefinitionBuilder(definitions);
        Column column = builder.column();
        column.term(config.get("screen").read(new JLabel()));
        screenComboBox = new JComboBox();
        screenComboBox.setEditable(false);
        column.definition(screenComboBox).fillHorizontal();
        column.term(config.get("zoom").read(new JLabel()));
        zoomSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.5, 5.0, 0.1));
        zoomSpinner.setEditor(new JSpinner.NumberEditor(zoomSpinner, "0.00"));
        column.definition(zoomSpinner);
        read();
    }

    private void read() {
        this.screenComboBox.setModel(new BaseComboBoxModel<String>(true, FullScreen.getIDs()));
        this.screenComboBox.setSelectedItem(console.getScreen());
        this.zoomSpinner.setValue(new Double(console.getZoom()));
    }

    public void apply() {
        console.setScreen((String) this.screenComboBox.getSelectedItem());
        console.setZoom(((Number) this.zoomSpinner.getValue()).floatValue());
    }
}
