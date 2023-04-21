package br.com.mackenzie.fuzzy.bellmanzadeh.gui;

import java.awt.Container;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextField;
import br.com.mackenzie.fuzzy.bellmanzadeh.gui.actions.AddObjectiveOrConstraintAction;
import br.com.mackenzie.fuzzy.bellmanzadeh.gui.actions.HideDialogAction;
import br.com.mackenzie.fuzzy.bellmanzadeh.gui.actions.ShowMembershipWizardDialogAction;
import br.com.mackenzie.fuzzy.bellmanzadeh.gui.table.MackTable;
import br.com.mackenzie.fuzzy.bellmanzadeh.mf.MembershipFunction;

public class AddObjectiveOrConstraintPanel extends MembershipPanel {

    private JDialog dialog_;

    private JDialog membershipFunctionDialog_;

    private AddMembershipFunctionPanel membershipFunctionPanel_;

    private JTextField descriptionTextField_;

    private JTextField membershipTextField_;

    private JComboBox alternativesComboBox_;

    private MackTable table_;

    private JFrame owner_;

    public AddObjectiveOrConstraintPanel(String fileName, JDialog dialog, MackTable table, JFrame owner) {
        super(fileName);
        this.owner_ = owner;
        this.dialog_ = dialog;
        this.table_ = table;
        this.descriptionTextField_ = this.getTextField("description.text");
        this.alternativesComboBox_ = this.getComboBox("alternativa.combobox");
        this.membershipTextField_ = this.getTextField("pertinencia.text");
        membershipFunctionDialog_ = this.getMembershipFunctionDialog();
        buildButtons();
    }

    private JDialog getMembershipFunctionDialog() {
        if (membershipFunctionDialog_ == null) {
            membershipFunctionDialog_ = new JDialog(this.owner_, "Definir fun��o de pertin�ncia", true);
            membershipFunctionDialog_.setContentPane(getMembershipFunctionPanel());
            membershipFunctionDialog_.pack();
        }
        return membershipFunctionDialog_;
    }

    private Container getMembershipFunctionPanel() {
        if (membershipFunctionPanel_ == null) {
            membershipFunctionPanel_ = new AddMembershipFunctionPanel("AddMembershipFunctionPanel.jfrm", this, membershipFunctionDialog_);
        }
        return membershipFunctionPanel_;
    }

    private void buildButtons() {
        AbstractButton addButton = this.getButton("add.btn");
        addButton.setAction(new AddObjectiveOrConstraintAction(this, dialog_));
        AbstractButton cancelButton = this.getButton("cancel.btn");
        cancelButton.setAction(new HideDialogAction(this, this.dialog_));
        AbstractButton membershipButton = this.getButton("wiz.btn");
        membershipButton.setAction(new ShowMembershipWizardDialogAction(this, this.membershipFunctionDialog_));
    }

    public JTextField getDescriptionTextField() {
        return this.descriptionTextField_;
    }

    public JTextField getMembershipTextField() {
        return this.membershipTextField_;
    }

    public JComboBox getAlternativesComboBox() {
        return this.alternativesComboBox_;
    }

    public MackTable getTable() {
        return this.table_;
    }

    public void updateComboBox(LinkedHashMap fields) {
        Set set = fields.entrySet();
        Iterator i = set.iterator();
        i.next();
        while (i.hasNext()) {
            Entry entry = (Entry) i.next();
            this.alternativesComboBox_.addItem(entry.getKey());
        }
    }

    public void updateTextField(MembershipFunction mf) {
        super.updateTextField(mf);
        this.membershipTextField_.setText(mf.toString());
    }
}
