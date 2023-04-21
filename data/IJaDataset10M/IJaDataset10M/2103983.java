package ua.od.lonewolf.Crow.View.EditCode;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import ua.od.lonewolf.Crow.Controller.MainFrameController;
import ua.od.lonewolf.Crow.Model.Element.Code;
import ua.od.lonewolf.Crow.Model.Element.RegisterFactory;
import ua.od.lonewolf.Crow.View.AbstractEditElementDialog;

public class EditCodeDialog extends AbstractEditElementDialog<Code> {

    private static final long serialVersionUID = 1L;

    private EditCodeDialogController controller;

    private JTextField txtCode;

    private JTextField txtName;

    private JTextArea txtDescription;

    private JButton btnOk;

    public EditCodeDialog(MainFrameController ctrl) {
        this(ctrl, -1);
    }

    public EditCodeDialog(MainFrameController ctrl, long codeId) {
        super("Edit Implementation Reference", "<b>Manage the implementation reference.</b><p></p>Provide the needed " + "properties for the implementation reference and press \"OK\" to continue.", new Dimension(700, 600), new Dimension(400, 520), true, ctrl, true, codeId, Code.class, true, -1);
        this.controller = new EditCodeDialogController(ctrl);
    }

    @Override
    protected void initControls() {
        super.initControls();
        if (getElementId() == -1) return;
        try {
            getController().getRegisterFactory();
            Code code = (Code) RegisterFactory.getElement(getController().getSession(), getElementId(), Code.class);
            txtCode.setText(code.getCode());
            txtName.setText(code.getName());
            txtDescription.setText(code.getDescription());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public JPanel createBottomPanel() {
        btnOk = new JButton(new AbstractAction("  OK  ") {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(final ActionEvent e) {
                try {
                    if (getElementId() == -1) {
                        setElementId(controller.createCode(txtCode.getText(), txtName.getText(), txtDescription.getText()));
                    } else {
                        controller.updateCode(getElementId(), txtCode.getText(), txtName.getText(), txtDescription.getText());
                    }
                    update();
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnOk.setDefaultCapable(true);
        JButton btnCancel = new JButton(new AbstractAction("Cancel") {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(btnOk);
        pane.add(Box.createHorizontalStrut(5));
        pane.add(btnCancel);
        return pane;
    }

    @Override
    public JPanel createMainPanel() {
        JPanel mainPane = new JPanel(new BorderLayout(0, 0));
        JPanel northPane = new JPanel();
        northPane.setLayout(new BoxLayout(northPane, BoxLayout.Y_AXIS));
        northPane.add(Box.createVerticalStrut(5));
        JLabel lblCode = new JLabel("Code (short name):");
        txtCode = new JTextField();
        txtCode.setMaximumSize(new Dimension(txtCode.getMaximumSize().width, txtCode.getPreferredSize().height));
        JPanel paneCode = new JPanel(new BorderLayout(0, 0));
        paneCode.add(lblCode, BorderLayout.NORTH);
        northPane.add(paneCode);
        northPane.add(txtCode);
        northPane.add(Box.createVerticalStrut(5));
        JLabel lblName = new JLabel("Implementation reference name:");
        txtName = new JTextField();
        txtName.setMaximumSize(new Dimension(txtName.getMaximumSize().width, txtName.getPreferredSize().height));
        JPanel paneName = new JPanel(new BorderLayout(0, 0));
        paneName.add(lblName, BorderLayout.NORTH);
        northPane.add(paneName);
        northPane.add(txtName);
        txtName.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateButtons();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                validateButtons();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateButtons();
            }
        });
        northPane.add(Box.createVerticalStrut(10));
        JPanel paneDescr = new JPanel(new BorderLayout(0, 0));
        paneDescr.add(new JLabel("Implementation reference description:"), BorderLayout.NORTH);
        northPane.add(paneDescr);
        mainPane.add(northPane, BorderLayout.NORTH);
        txtDescription = new JTextArea(10, 20);
        Font f = new JTextField().getFont();
        txtDescription.setFont(new Font("Monospaced", f.getStyle(), f.getSize()));
        JScrollPane scp = new JScrollPane(txtDescription);
        scp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPane.add(scp, BorderLayout.CENTER);
        transferFocus(tabPane, 0, txtCode);
        return mainPane;
    }

    @Override
    protected void validateButtons() {
        btnOk.setEnabled(txtName.getText().length() > 0);
    }
}
