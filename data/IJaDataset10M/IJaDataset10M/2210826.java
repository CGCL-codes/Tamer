package org.xmlhammer.gui.schemavalidator;

import java.awt.BorderLayout;
import java.awt.Font;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.XMLConstants;
import org.xmlhammer.gui.overview.ComboBoxNode;
import org.xmlhammer.gui.overview.OverviewNode;
import org.xmlhammer.gui.util.UndoableComboBoxItemListener;
import org.xmlhammer.model.project.SchemaValidator;

/**
 * Input Panel.
 * 
 * Allows to select either one URI, multiple URIs or 
 * a range of files.
 * 
 * @version $Revision$, $Date$
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class SchemaPanel extends JPanel {

    private static final long serialVersionUID = 5530326695690039953L;

    private JComboBox schemaLanguageField = null;

    private ArrayList<OverviewNode> nodes = null;

    /**
	 * Constructs a new Input Panel.
	 */
    public SchemaPanel(SchemaPage page) {
        super(new BorderLayout(10, 0));
        nodes = new ArrayList<OverviewNode>();
        schemaLanguageField = new JComboBox();
        schemaLanguageField.addItem(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schemaLanguageField.addItem(XMLConstants.RELAXNG_NS_URI);
        schemaLanguageField.setEditable(false);
        schemaLanguageField.setFont(schemaLanguageField.getFont().deriveFont(Font.PLAIN));
        schemaLanguageField.setToolTipText("Specify a Schema language.");
        schemaLanguageField.addItemListener(new UndoableComboBoxItemListener(page, schemaLanguageField));
        if (page != null) {
            page.getProjectView().getFieldManager().addField(schemaLanguageField);
        }
        add(new JLabel("Language:"), BorderLayout.WEST);
        add(schemaLanguageField, BorderLayout.CENTER);
        nodes.add(new ComboBoxNode(page, schemaLanguageField, "schema"));
    }

    public void setSchemaLanguage(String schemaLanguage) {
        if (schemaLanguage == null || schemaLanguage.trim().length() == 0) {
            schemaLanguageField.setSelectedIndex(0);
        } else {
            schemaLanguageField.setSelectedItem(schemaLanguage);
        }
    }

    public void setSchemaLanguages(List<String> languages) {
        int index = schemaLanguageField.getSelectedIndex();
        if (schemaLanguageField.getItemCount() > 0) {
            schemaLanguageField.removeAllItems();
        }
        for (String language : languages) {
            schemaLanguageField.addItem(language);
        }
        if (languages.size() > index) {
            schemaLanguageField.setSelectedIndex(index);
        } else {
            schemaLanguageField.setSelectedIndex(0);
        }
    }

    public String getSchemaLanguage() {
        return (String) schemaLanguageField.getSelectedItem();
    }

    /**
	 * Sets the input model.
	 * Null resets current values.
	 * 
	 * @param parser the parser settings.
	 * @throws URISyntaxException
	 */
    public void setSchemaValidator(SchemaValidator validator) {
        if (validator != null && validator.getLanguage() != null) {
            schemaLanguageField.setSelectedItem(validator.getLanguage());
        } else {
            schemaLanguageField.setSelectedIndex(0);
        }
    }

    public SchemaValidator getSchemaValidator() {
        SchemaValidator validator = new SchemaValidator();
        if (schemaLanguageField.getSelectedItem() != null) {
            validator.setLanguage(schemaLanguageField.getSelectedItem().toString());
        }
        return validator;
    }

    public ArrayList<OverviewNode> getChildNodes() {
        return nodes;
    }

    public void dispose() {
    }
}
