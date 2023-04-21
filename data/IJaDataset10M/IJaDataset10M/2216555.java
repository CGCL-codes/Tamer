package ijgen.generator.gui.components;

import ijgen.generator.gui.components.propeditor.PropertiesEditor;
import ijgen.generator.gui.components.propeditor.PropertiesEditorFactory;
import ijgen.generator.gui.util.Mediator;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author Detelin Zlatev
 *
 */
public class IJTableCellRenderer implements TableCellRenderer {

    private Mediator mediator;

    public IJTableCellRenderer(Mediator mediator) {
        this.mediator = mediator;
    }

    /**
	 * 
	 */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        PropertiesEditor propertiesEditor = PropertiesEditorFactory.getPropertiesEditor(this.mediator.getModelsBrowser().getSelectedNode().getUserObject());
        JComponent component = propertiesEditor.getComponentFor(row, column, value);
        return component;
    }
}
