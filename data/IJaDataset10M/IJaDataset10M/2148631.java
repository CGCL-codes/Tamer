package pcgen.gui.filter;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import pcgen.gui.tools.FlippingSplitPane;
import pcgen.gui.tools.ResourceManager;
import pcgen.gui.tools.SelectionDialog;
import pcgen.gui.tools.SelectionModel;
import pcgen.gui.util.GenericListModel;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public class FilterSelectionModel implements SelectionModel<DisplayableFilter<?>> {

    private static final Properties props = new Properties();

    static {
        props.setProperty(SelectionModel.AVAILABLE_TEXT_PROP, "availFilt");
        props.setProperty(SelectionModel.SELECTION_TEXT_PROP, "selFilt");
        props.setProperty(SelectionModel.NEW_TOOLTIP_PROP, "newFilt");
        props.setProperty(SelectionModel.COPY_TOOLTIP_PROP, "copyFilt");
        props.setProperty(SelectionModel.DELETE_TOOLTIP_PROP, "deleteFilt");
        props.setProperty(SelectionModel.ADD_TOOLTIP_PROP, "addFilt");
        props.setProperty(SelectionModel.REMOVE_TOOLTIP_PROP, "removeFilt");
    }

    private final JTextArea descriptionArea;

    private final JTextArea editorArea;

    private MutableFilter mutableFilter = null;

    public FilterSelectionModel() {
        this.descriptionArea = new JTextArea();
        this.editorArea = new JTextArea();
    }

    public GenericListModel<DisplayableFilter<?>> getAvailableList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public GenericListModel<DisplayableFilter<?>> getSelectedList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setAvailableList(GenericListModel<DisplayableFilter<?>> list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setSelectedList(GenericListModel<DisplayableFilter<?>> list) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Component getCustomizer(Component currentItemPanel, DisplayableFilter<?> selectedItem) {
        if (currentItemPanel == null) {
            FlippingSplitPane splitPane = new FlippingSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPane.setTopComponent(createPanel("desc", descriptionArea));
            splitPane.setBottomComponent(createPanel("editor", editorArea));
            currentItemPanel = splitPane;
        }
        if (selectedItem != null && selectedItem != mutableFilter) {
            if (mutableFilter != null) {
                mutableFilter.setDescription(descriptionArea.getText());
                mutableFilter.setCode(editorArea.getText());
            }
            descriptionArea.setText(selectedItem.getDescription());
            editorArea.setText(selectedItem.getCode());
            boolean mutable = isMutable(selectedItem);
            descriptionArea.setEditable(mutable);
            editorArea.setEditable(mutable);
            mutableFilter = mutable ? (MutableFilter) selectedItem : null;
        }
        return currentItemPanel;
    }

    private JPanel createPanel(String prop, JTextArea textArea) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 2, 2, 2);
        panel.add(new JLabel(ResourceManager.getText(prop)), gridBagConstraints);
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.insets = new Insets(0, 0, 0, 0);
        panel.add(textArea, gridBagConstraints);
        return panel;
    }

    public DisplayableFilter<?> createMutableItem(SelectionDialog<DisplayableFilter<?>> selectionDialog, DisplayableFilter<?> templateItem) {
        MutableFilter<?> filter = new DefaultMutableFilter(JOptionPane.showInputDialog(selectionDialog, ResourceManager.getText("createFilt")));
        if (templateItem != null) {
            filter.setCode(templateItem.getCode());
            filter.setDescription(templateItem.getDescription());
        }
        return filter;
    }

    public Properties getDisplayProperties() {
        return props;
    }

    public boolean isMutable(DisplayableFilter<?> item) {
        return item instanceof MutableFilter;
    }

    public boolean isAddable(DisplayableFilter<?> item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Color getItemColor(DisplayableFilter<?> item) {
        if (isMutable(item)) {
            return Color.BLUE;
        }
        return Color.BLACK;
    }

    public boolean isCopyable(DisplayableFilter<?> item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
