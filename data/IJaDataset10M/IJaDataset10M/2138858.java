package toxtree.ui;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openscience.cdk.interfaces.IAtomContainer;
import toxTree.tree.rules.RuleVerifyProperty;

public class OptionsPanel extends JPanel implements FocusListener, ItemListener {

    protected JCheckBox dontAsk;

    protected JFormattedTextField propertyField;

    protected IAtomContainer atomContainer;

    protected final RuleVerifyProperty rule;

    /**
     * 
     */
    private static final long serialVersionUID = 8261591244903486566L;

    public OptionsPanel(String caption, IAtomContainer atomContainer, RuleVerifyProperty rule) {
        super();
        this.atomContainer = atomContainer;
        this.rule = rule;
        JLabel propertyNameField = new JLabel("<html><b>" + rule.getPropertyName() + "</b></html>");
        propertyNameField.setToolTipText("Property name");
        add(propertyNameField);
        if (!"".equals(rule.getPropertyUnits())) {
            JLabel propertyUnitsField = new JLabel("," + rule.getPropertyUnits());
            add(propertyUnitsField);
        }
        propertyField = new JFormattedTextField(NumberFormat.getNumberInstance());
        propertyField.addFocusListener(this);
        propertyField.setToolTipText(rule.getPropertyName() + " value");
        propertyField.setColumns(10);
        propertyField.setPreferredSize(new Dimension(300, 18));
        propertyField.setMinimumSize(new Dimension(100, 18));
        add(propertyField);
        dontAsk = new JCheckBox("Silent");
        dontAsk.addItemListener(this);
        dontAsk.setSelected(false);
        dontAsk.setToolTipText("Silently answer \"No\" if property value is not available");
        add(dontAsk);
        setBorder(BorderFactory.createTitledBorder(caption));
    }

    public boolean isSilent() {
        return dontAsk.isSelected();
    }

    public String getPropertyValue() {
        return propertyField.getText();
    }

    public void itemStateChanged(ItemEvent e) {
        this.rule.setInteractive(!dontAsk.isSelected());
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
        if (atomContainer != null) atomContainer.setProperty(rule.getPropertyName(), propertyField.getText());
    }
}
