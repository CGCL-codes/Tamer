package jorgan.executor.gui.preferences.category;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import jorgan.executor.Executions;
import jorgan.gui.preferences.category.AppCategory;
import jorgan.gui.preferences.category.JOrganCategory;
import jorgan.swing.layout.DefinitionBuilder;
import jorgan.swing.layout.DefinitionBuilder.Column;
import jorgan.swing.text.MultiLineLabel;
import bias.Configuration;
import bias.swing.Category;
import bias.util.Property;

/**
 * {@link jorgan.App} category.
 */
public class ExecutorCategory extends JOrganCategory {

    private static Configuration config = Configuration.getRoot().get(ExecutorCategory.class);

    private Model<Boolean> executionsAllowed = getModel(new Property(Executions.class, "allowed"));

    private JCheckBox executionsAllowedCheckBox = new JCheckBox();

    public ExecutorCategory() {
        config.read(this);
    }

    @Override
    public Class<? extends Category> getParentCategory() {
        return AppCategory.class;
    }

    @Override
    protected JComponent createComponent() {
        JPanel panel = new JPanel();
        DefinitionBuilder builder = new DefinitionBuilder(panel);
        Column column = builder.column();
        column.box(config.get("description").read(new MultiLineLabel()));
        column.definition(config.get("allowExecute").read(executionsAllowedCheckBox));
        return panel;
    }

    @Override
    protected void read() {
        executionsAllowedCheckBox.setSelected(executionsAllowed.getValue());
    }

    @Override
    protected void write() {
        executionsAllowed.setValue(executionsAllowedCheckBox.isSelected());
    }
}
