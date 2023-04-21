package periman;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class QuestionnaireRowScale implements QuestionnaireRow {

    JDialog parentDialog;

    String leftLabel;

    String rightLabel;

    int width;

    boolean mandatory;

    String header;

    ScaleProperties propertiesDialog = null;

    public QuestionnaireRowScale(JDialog mw) {
        parentDialog = mw;
    }

    @Override
    public boolean getPropertiesRowFromUser() {
        if (propertiesDialog == null) propertiesDialog = new ScaleProperties(parentDialog); else propertiesDialog.showForModify();
        if (propertiesDialog.isAccepted() == false) return false;
        leftLabel = propertiesDialog.getLeftLabel();
        rightLabel = propertiesDialog.getRightLabel();
        width = propertiesDialog.getScaleWidth();
        header = propertiesDialog.getHeaderLabel();
        mandatory = propertiesDialog.getIsMandatory();
        return true;
    }

    @Override
    public Element toDom(Document dom) {
        Element element = dom.createElement("scale");
        element.setAttribute("leftlabel", leftLabel);
        element.setAttribute("rightlabel", rightLabel);
        element.setAttribute("width", "" + width);
        if (mandatory == true) element.setAttribute("mandatory", "true"); else element.setAttribute("mandatory", "false");
        element.setAttribute("header", header);
        return element;
    }

    @Override
    public String toString() {
        return "Scale: (" + leftLabel + "," + rightLabel + ") , with " + width + " checkboxes";
    }

    @SuppressWarnings("serial")
    class ScaleProperties extends JDialog implements ActionListener {

        private boolean accepted = false;

        boolean isAccepted() {
            return accepted;
        }

        private SpinnerNumberModel widthModel;

        public int getScaleWidth() {
            return widthModel.getNumber().intValue();
        }

        private JTextField leftLabel;

        public String getLeftLabel() {
            return leftLabel.getText();
        }

        private JTextField rightLabel;

        public String getRightLabel() {
            return rightLabel.getText();
        }

        private JCheckBox checkbox;

        public boolean getIsMandatory() {
            return checkbox.isSelected();
        }

        private JTextField headerField;

        public String getHeaderLabel() {
            return headerField.getText();
        }

        ScaleProperties(JDialog mw) {
            super(mw, true);
            createGui();
            setTitle("Properties for Scale row");
            pack();
            setLocation(400, 400);
            setVisible(true);
        }

        void createGui() {
            GridBagLayout dialogLayout = new GridBagLayout();
            setLayout(dialogLayout);
            JPanel panel = (JPanel) this.getContentPane();
            GridBagConstraints headerConstraint = new GridBagConstraints();
            headerConstraint.gridx = 0;
            headerConstraint.gridy = 0;
            headerConstraint.anchor = GridBagConstraints.FIRST_LINE_START;
            JLabel headerLbl = new JLabel("Header");
            panel.add(headerLbl, headerConstraint);
            GridBagConstraints headerFieldConstraint = new GridBagConstraints();
            headerFieldConstraint.gridx = 1;
            headerFieldConstraint.gridy = 0;
            headerFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
            headerFieldConstraint.weightx = 1.0;
            headerField = new JTextField();
            panel.add(headerField, headerFieldConstraint);
            GridBagConstraints leftLblConstraint = new GridBagConstraints();
            leftLblConstraint.anchor = GridBagConstraints.FIRST_LINE_START;
            leftLblConstraint.gridx = 0;
            leftLblConstraint.gridy = 1;
            JLabel lblCapLbl = new JLabel("Left label");
            panel.add(lblCapLbl, leftLblConstraint);
            GridBagConstraints leftFieldConstraint = new GridBagConstraints();
            leftFieldConstraint.gridx = 1;
            leftFieldConstraint.gridy = 1;
            leftFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
            leftFieldConstraint.weightx = 1;
            leftLabel = new JTextField();
            panel.add(leftLabel, leftFieldConstraint);
            GridBagConstraints righLblConstraint = new GridBagConstraints();
            righLblConstraint.anchor = GridBagConstraints.FIRST_LINE_START;
            righLblConstraint.gridx = 0;
            righLblConstraint.gridy = 2;
            JLabel rlblCapLbl = new JLabel("Right label");
            panel.add(rlblCapLbl, righLblConstraint);
            GridBagConstraints rightFieldConstraint = new GridBagConstraints();
            rightFieldConstraint.gridx = 1;
            rightFieldConstraint.gridy = 2;
            rightFieldConstraint.fill = GridBagConstraints.HORIZONTAL;
            rightFieldConstraint.weightx = 1;
            rightLabel = new JTextField();
            panel.add(rightLabel, rightFieldConstraint);
            GridBagConstraints lbl3Constraints = new GridBagConstraints();
            lbl3Constraints.gridx = 0;
            lbl3Constraints.gridy = 3;
            JLabel lblWidth = new JLabel("Widht");
            panel.add(lblWidth, lbl3Constraints);
            GridBagConstraints spinnerConstraints = new GridBagConstraints();
            spinnerConstraints.gridx = 1;
            spinnerConstraints.gridy = 3;
            spinnerConstraints.fill = GridBagConstraints.HORIZONTAL;
            widthModel = new SpinnerNumberModel();
            JSpinner spinner = new JSpinner(widthModel);
            panel.add(spinner, spinnerConstraints);
            GridBagConstraints checkboxConstraint = new GridBagConstraints();
            checkboxConstraint.gridx = 0;
            checkboxConstraint.gridy = 4;
            checkboxConstraint.gridwidth = 2;
            checkboxConstraint.anchor = GridBagConstraints.LAST_LINE_START;
            checkbox = new JCheckBox("Mandatory");
            panel.add(checkbox, checkboxConstraint);
            GridBagConstraints okBtnConstraint = new GridBagConstraints();
            okBtnConstraint.gridx = 0;
            okBtnConstraint.gridy = 5;
            JButton okBtn = new JButton("OK");
            okBtn.addActionListener(this);
            okBtn.setActionCommand("OK");
            okBtn.setMnemonic(KeyEvent.VK_O);
            panel.add(okBtn, okBtnConstraint);
            GridBagConstraints cancelBtnConstraint = new GridBagConstraints();
            cancelBtnConstraint.gridx = 1;
            cancelBtnConstraint.gridy = 5;
            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.setActionCommand("Cancel");
            cancelBtn.addActionListener(this);
            cancelBtn.setMnemonic(KeyEvent.VK_C);
            panel.add(cancelBtn, cancelBtnConstraint);
        }

        @Override
        public void actionPerformed(ActionEvent fp) {
            if (fp.getActionCommand().equals("OK")) {
                accepted = true;
                setVisible(false);
            }
            if (fp.getActionCommand().equals("Cancel")) {
                accepted = false;
                setVisible(false);
            }
        }

        public void showForModify() {
            accepted = false;
            setLocation(400, 400);
            setVisible(true);
        }
    }
}
