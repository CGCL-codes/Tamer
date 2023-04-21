package gleam.gateservice.definition.tool;

import gleam.gateservice.definition.GateServiceDefinition.ParameterMapping;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * List cell renderer for a list of {@link ParameterMapping} objects.
 * Renders each row as "PRName: prParameter".
 * 
 * @author ian
 * 
 */
public class ParameterMappingCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        ParameterMapping pm = (ParameterMapping) value;
        return super.getListCellRendererComponent(list, pm.getPrName() + ": " + pm.getParamName(), index, isSelected, cellHasFocus);
    }
}
