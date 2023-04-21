package phex.gui.dialogs.filter.wizard.consequence;

import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import phex.common.log.NLogger;
import phex.gui.common.table.FWTable;
import phex.gui.dialogs.filter.wizard.FilterWizardDialog;
import phex.rules.Rule;
import phex.rules.consequence.Consequence;
import phex.utils.Localizer;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ConsequencePanel extends JPanel {

    private FilterWizardDialog parent;

    private ConsequenceTableModel consequenceTableModel;

    private FWTable consequenceTable;

    private JLabel selectConsequenceLbl;

    public ConsequencePanel(FilterWizardDialog parent) {
        this.parent = parent;
        prepareComponent();
    }

    private void prepareComponent() {
        CellConstraints cc = new CellConstraints();
        FormLayout layout = new FormLayout("fill:d:grow", "p, 2dlu, fill:75dlu:grow");
        PanelBuilder contentPB = new PanelBuilder(layout, this);
        selectConsequenceLbl = new JLabel(Localizer.getString("RuleWizard_SelectRuleConsequence"));
        contentPB.add(selectConsequenceLbl, cc.xywh(1, 1, 1, 1));
        consequenceTableModel = new ConsequenceTableModel(this);
        consequenceTable = new FWTable(consequenceTableModel);
        consequenceTable.setShowVerticalLines(false);
        JTableHeader tableHeader = consequenceTable.getTableHeader();
        tableHeader.setResizingAllowed(false);
        tableHeader.setReorderingAllowed(false);
        JCheckBox box = (JCheckBox) consequenceTable.getDefaultRenderer(Boolean.class);
        TableColumn column = consequenceTable.getColumnModel().getColumn(0);
        column.setMaxWidth(box.getPreferredSize().width + 2);
        column.setMinWidth(box.getPreferredSize().width + 2);
        consequenceTable.getColumnModel().getColumn(1).setCellRenderer(new ConsequenceCellRenderer());
        contentPB.add(FWTable.createFWTableScrollPane(consequenceTable), cc.xywh(1, 3, 1, 1));
    }

    public void updateRuleData() {
        Rule rule = parent.getEditRule();
        int rowCount = consequenceTableModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            consequenceTableModel.consequences[i][0] = Boolean.FALSE;
        }
        List consequencesList = rule.getConsequences();
        Iterator iterator = consequencesList.iterator();
        while (iterator.hasNext()) {
            Consequence consequence = (Consequence) iterator.next();
            int row = consequenceTableModel.getRowOf(consequence);
            consequenceTableModel.consequences[row][0] = Boolean.TRUE;
        }
        consequenceTable.setEnabled(!rule.isDefaultRule());
        selectConsequenceLbl.setEnabled(!rule.isDefaultRule());
        consequenceTableModel.fireTableDataChanged();
    }

    public void ruleStatusChanged(Class consequenceClass, boolean status) {
        Rule editRule = parent.getEditRule();
        if (status) {
            try {
                Consequence newConsequence = (Consequence) consequenceClass.newInstance();
                editRule.addConsequence(newConsequence);
                parent.updateRuleData();
            } catch (InstantiationException exp) {
                NLogger.error(ConsequencePanel.class, exp, exp);
            } catch (IllegalAccessException exp) {
                NLogger.error(ConsequencePanel.class, exp, exp);
            }
        } else {
            List consequences = editRule.getConsequences();
            for (int i = consequences.size() - 1; i >= 0; i--) {
                Consequence consequence = (Consequence) consequences.get(i);
                if (consequence.getClass() == consequenceClass) {
                    editRule.removeConsequence(consequence);
                }
            }
            parent.updateRuleData();
        }
    }
}
