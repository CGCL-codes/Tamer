package org.leviatan.definator.gui.swingcomponents;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.leviatan.definator.core.model.Record;
import org.leviatan.definator.gui.RuntimeContext;

public class SingleRecordEditView extends JPanel {

    private static final long serialVersionUID = 1L;

    private JTextField txtFldName = null;

    private JLabel lblTitle = null;

    private JLabel lblId = null;

    private JLabel lblIdTxt = null;

    private JLabel lblName = null;

    private JLabel lblAuthor = null;

    private JLabel lblVersion = null;

    private JLabel lblAuthorTxt = null;

    private JLabel lblVersionTxt = null;

    private JButton butSave = null;

    private JButton butDiscard = null;

    /**
	 * This is the default constructor
	 */
    public SingleRecordEditView() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        GridBagConstraints lblTitleConstr = new GridBagConstraints();
        GridBagConstraints lblIdConstr = new GridBagConstraints();
        GridBagConstraints lblNameConstr = new GridBagConstraints();
        GridBagConstraints lblVersionConstr = new GridBagConstraints();
        GridBagConstraints lblAuthorConstr = new GridBagConstraints();
        GridBagConstraints lblVersionTxtConstr = new GridBagConstraints();
        GridBagConstraints lblAuthorTxtConstr = new GridBagConstraints();
        GridBagConstraints lblIdTxtConstr = new GridBagConstraints();
        GridBagConstraints txtFldNameConstr = new GridBagConstraints();
        GridBagConstraints butSaveConstr = new GridBagConstraints();
        GridBagConstraints butDiscardConstr = new GridBagConstraints();
        lblTitleConstr.fill = GridBagConstraints.BOTH;
        lblTitleConstr.gridheight = 1;
        lblTitleConstr.gridwidth = 2;
        lblTitleConstr.ipadx = 10;
        lblTitleConstr.ipady = 20;
        lblTitleConstr.gridx = 0;
        lblTitleConstr.gridy = 0;
        lblIdConstr.fill = GridBagConstraints.BOTH;
        lblIdConstr.gridx = 0;
        lblIdConstr.gridy = 1;
        lblNameConstr.fill = GridBagConstraints.BOTH;
        lblNameConstr.ipady = 2;
        lblNameConstr.ipadx = 5;
        lblNameConstr.gridx = 0;
        lblNameConstr.gridy = 2;
        lblVersionConstr.fill = GridBagConstraints.BOTH;
        lblVersionConstr.ipady = 2;
        lblVersionConstr.ipadx = 5;
        lblVersionConstr.gridx = 0;
        lblVersionConstr.gridy = 3;
        lblAuthorConstr.fill = GridBagConstraints.BOTH;
        lblAuthorConstr.ipady = 2;
        lblAuthorConstr.ipadx = 5;
        lblAuthorConstr.gridx = 0;
        lblAuthorConstr.gridy = 4;
        lblIdTxtConstr.fill = GridBagConstraints.BOTH;
        lblIdTxtConstr.ipady = 2;
        lblIdTxtConstr.ipadx = 5;
        lblIdTxtConstr.gridx = 1;
        lblIdTxtConstr.gridy = 1;
        txtFldNameConstr.fill = GridBagConstraints.BOTH;
        txtFldNameConstr.gridx = 1;
        txtFldNameConstr.gridy = 2;
        lblVersionTxtConstr.fill = GridBagConstraints.BOTH;
        lblVersionTxtConstr.ipady = 2;
        lblVersionTxtConstr.ipadx = 5;
        lblVersionTxtConstr.gridx = 1;
        lblVersionTxtConstr.gridy = 3;
        lblAuthorTxtConstr.fill = GridBagConstraints.BOTH;
        lblAuthorTxtConstr.ipady = 2;
        lblAuthorTxtConstr.ipadx = 5;
        lblAuthorTxtConstr.gridx = 1;
        lblAuthorTxtConstr.gridy = 4;
        butSaveConstr.fill = GridBagConstraints.BOTH;
        butSaveConstr.insets = new Insets(10, 20, 10, 20);
        butSaveConstr.gridwidth = 1;
        butSaveConstr.ipady = 0;
        butSaveConstr.ipadx = 0;
        butSaveConstr.gridx = 0;
        butSaveConstr.gridy = 5;
        butDiscardConstr.fill = GridBagConstraints.BOTH;
        butDiscardConstr.insets = new Insets(10, 20, 10, 20);
        butDiscardConstr.gridwidth = 1;
        butDiscardConstr.ipady = 0;
        butDiscardConstr.ipadx = 0;
        butDiscardConstr.gridx = 1;
        butDiscardConstr.gridy = 5;
        lblTitle = new JLabel();
        lblTitle.setText("Record specifications");
        lblId = new JLabel("Id:");
        lblIdTxt = new JLabel();
        lblName = new JLabel("Name:");
        lblAuthor = new JLabel("Author:");
        lblVersion = new JLabel("Version:");
        lblVersionTxt = new JLabel();
        lblAuthorTxt = new JLabel();
        txtFldName = new JTextField();
        txtFldName.setColumns(10);
        butSave = new JButton("Save");
        butDiscard = new JButton("Discard");
        this.setSize(314, 400);
        this.setLayout(new GridBagLayout());
        this.add(lblTitle, lblTitleConstr);
        this.add(lblId, lblIdConstr);
        this.add(lblName, lblNameConstr);
        this.add(lblIdTxt, lblIdTxtConstr);
        this.add(txtFldName, txtFldNameConstr);
        this.add(lblAuthor, lblAuthorConstr);
        this.add(lblAuthorTxt, lblAuthorTxtConstr);
        this.add(lblVersion, lblVersionConstr);
        this.add(lblVersionTxt, lblVersionTxtConstr);
        this.add(butSave, butSaveConstr);
        this.add(butDiscard, butDiscardConstr);
        this.butSave.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                System.out.println("Save clicked");
                String newName = txtFldName.getText().trim();
                String recId = lblIdTxt.getText();
                if (recId != null && !recId.equals("")) {
                    RuntimeContext.definatorService.updateRecordName(recId, newName);
                }
                RuntimeContext.defTree.updateRecordNode(recId);
            }
        });
        this.butDiscard.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                System.out.println("Discard clicked");
                Record recModel = RuntimeContext.definatorService.getRecordModel(lblIdTxt.getText());
                setModel(recModel);
            }
        });
    }

    public void setModel(Record recMdl) {
        this.lblIdTxt.setText(String.valueOf(recMdl.getId()));
        this.txtFldName.setText(recMdl.getName());
        this.lblVersionTxt.setText(" - ");
        this.lblAuthorTxt.setText(" - ");
    }
}
