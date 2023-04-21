package org.jimcat.gui.smartlisteditor.editor;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import org.jimcat.gui.smartlisteditor.model.ExifMetadataFilterNode;
import org.jimcat.gui.smartlisteditor.model.FilterTreeNode;
import org.jimcat.model.filter.metadata.ExifMetadataFilter.ExifMetadataProperty;

/**
 * An editor for exif metadata filter nodes.
 * 
 * $Id$
 * 
 * @author Herbert
 */
public class ExifMetadataNodeEditor extends BaseNodeEditor {

    /**
	 * the editor component
	 */
    private JPanel editor;

    /**
	 * the combo box to select property
	 */
    private JComboBox property;

    /**
	 * the text pattern
	 */
    private JTextField pattern;

    /**
	 * the currently edited node
	 */
    private ExifMetadataFilterNode currentNode;

    /**
	 * default constructor
	 */
    public ExifMetadataNodeEditor() {
        initComponents();
    }

    /**
	 * build up editor
	 */
    private void initComponents() {
        editor = new JPanel();
        editor.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        editor.setOpaque(false);
        JLabel text = new JLabel("exif property ");
        text.setOpaque(false);
        editor.add(text);
        ExifMetadataProperty items[] = ExifMetadataProperty.values();
        String[] values = new String[items.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = ExifMetadataFilterNode.getNameForProperty(items[i]);
        }
        property = new JComboBox(values);
        property.addActionListener(new PropertyListener());
        editor.add(property);
        editor.add(getNegateComboBox());
        text = new JLabel(" contain ");
        text.setOpaque(false);
        editor.add(text);
        pattern = new JTextField(12);
        PatternFieldListener listener = new PatternFieldListener();
        pattern.addActionListener(listener);
        pattern.addFocusListener(listener);
        editor.add(pattern);
    }

    /**
	 * prepair the editor to edit given node
	 * 
	 * @see org.jimcat.gui.smartlisteditor.editor.BaseNodeEditor#getEditor(javax.swing.JTree,
	 *      org.jimcat.gui.smartlisteditor.model.FilterTreeNode)
	 */
    @Override
    @SuppressWarnings("unused")
    public JComponent getEditor(JTree tree, FilterTreeNode node) {
        currentNode = (ExifMetadataFilterNode) node;
        ExifMetadataProperty p = currentNode.getProperty();
        property.setSelectedItem(ExifMetadataFilterNode.getNameForProperty(p));
        pattern.setText(currentNode.getPattern());
        return editor;
    }

    /**
	 * private class to react on property switch events
	 */
    private class PropertyListener implements ActionListener {

        /**
		 * react on changes
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        @SuppressWarnings("unused")
        public void actionPerformed(ActionEvent event) {
            String sel = (String) property.getSelectedItem();
            currentNode.setProperty(ExifMetadataFilterNode.getPropertyForName(sel));
        }
    }

    /**
	 * react on pattern changes
	 */
    private class PatternFieldListener extends FocusAdapter implements ActionListener {

        /**
		 * handle new input
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        public void actionPerformed(ActionEvent e) {
            handleChange((JTextField) e.getSource());
        }

        /**
		 * react on a lost focus
		 * 
		 * @see java.awt.event.FocusAdapter#focusLost(java.awt.event.FocusEvent)
		 */
        @Override
        public void focusLost(FocusEvent e) {
            handleChange((JTextField) e.getSource());
        }

        /**
		 * handle a value change
		 * 
		 * @param field
		 */
        private void handleChange(JTextField field) {
            currentNode.setPattern(field.getText());
        }
    }
}
