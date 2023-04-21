package org.xmlhammer.gui.wizard;

import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;
import org.bounce.FormLayout;
import org.bounce.wizard.WizardPage;
import org.xmlhammer.gui.util.wizard.HelpEnabledWizardPage;

public class ParserPropertiesPage extends HelpEnabledWizardPage {

    private static final long serialVersionUID = -1909903213579390495L;

    private WizardPage nextPage = null;

    private JCheckBox namespaceAware = null;

    private JCheckBox xincludeAware = null;

    public ParserPropertiesPage(String helpID, WizardPage next) {
        super(new FormLayout(0, 5), helpID);
        this.nextPage = next;
        setBorder(new EmptyBorder(20, 50, 10, 10));
        namespaceAware = new JCheckBox("Namespace Aware");
        namespaceAware.setSelected(true);
        xincludeAware = new JCheckBox("XInclude Aware");
        add(namespaceAware, FormLayout.FULL);
        add(xincludeAware, FormLayout.FULL);
    }

    @Override
    public String getTitle() {
        return "Parser Properties";
    }

    @Override
    public String getDescription() {
        return "Specify the Parser Properties.";
    }

    @Override
    public WizardPage getNext() {
        return nextPage;
    }

    public boolean isNamespaceAware() {
        return namespaceAware.isSelected();
    }

    public boolean isXincludeAware() {
        return xincludeAware.isSelected();
    }
}
