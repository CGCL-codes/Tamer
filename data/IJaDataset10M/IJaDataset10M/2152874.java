package ch.bbv.dynamicproperties.visualization.editor;

import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ch.bbv.dynamicproperties.core.ComplexValue;
import ch.bbv.dynamicproperties.core.PropertyValue;
import ch.bbv.dynamicproperties.visualization.tool.SwingTools;

/**
 * @author UeliKurmann
 */
public class ComplexEditor extends DefaultEditor {

    private JPanel panel;

    private ComplexValue attributeValue;

    public ComplexEditor(PropertyValue attributeValue) {
        this.attributeValue = (ComplexValue) attributeValue;
        panel = new JPanel();
        init();
    }

    private void init() {
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(attributeValue.getName()), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.add(SwingTools.createEditorPanel(attributeValue.getProperties()));
        panel.repaint();
    }

    public JComponent getComponent() {
        return panel;
    }

    public Object getValue() {
        return null;
    }

    public void setValue(Object value) {
    }
}
