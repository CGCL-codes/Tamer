package org.xmlhammer.gui.wizard;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.border.EmptyBorder;
import org.bounce.wizard.WizardPage;
import org.xmlhammer.gui.util.wizard.HelpEnabledWizardPage;
import org.xmlhammer.gui.xslt.XMLOutputPanel;
import org.xmlhammer.model.tools.xslt.OutputProperty;

public class TextOutputPropertiesPage extends HelpEnabledWizardPage {

    private static final long serialVersionUID = 6688922851481710915L;

    private XMLOutputPanel propertiesPanel = null;

    public TextOutputPropertiesPage(String helpID) {
        super(new BorderLayout(), helpID);
        setBorder(new EmptyBorder(10, 10, 0, 10));
        propertiesPanel = new XMLOutputPanel(null, XMLOutputPanel.OUTPUT_METHOD_TEXT);
        add(propertiesPanel, BorderLayout.NORTH);
    }

    public List<OutputProperty> getProperties() {
        return propertiesPanel.getProperties(null);
    }

    @Override
    public String getTitle() {
        return "Text Output Method";
    }

    @Override
    public String getDescription() {
        return "Specify the properties for the Text output-method.";
    }

    @Override
    public WizardPage getNext() {
        return null;
    }
}
