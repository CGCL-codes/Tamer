package wilos.presentation.assistant.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.util.Collection;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import wilos.model.spem2.role.RoleDescriptor;
import wilos.presentation.assistant.ressources.Bundle;

public class RolesDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    public static int CHOIX_OK = 0;

    public static int CHOIX_CANCEL = 1;

    private int choice = 0;

    private JPanel jContentPane = null;

    private JLabel selectRole = null;

    private JComboBox listeRole = null;

    private JPanel jPanel = null;

    private JButton cancel = null;

    private JButton OK = null;

    private JScrollPane jScrollPane = null;

    private JEditorPane roleDesc = null;

    private JPanel north = null;

    private JPanel center = null;

    private JPanel south = null;

    /**
	 * @param owner
	 */
    public RolesDialog(Frame owner, Collection<RoleDescriptor> _listRoles) {
        super(owner, true);
        initialize();
        for (RoleDescriptor roleDescriptor : _listRoles) {
            listeRole.addItem(roleDescriptor);
        }
        this.setVisible(true);
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(330, 343);
        this.setTitle(Bundle.getText("rolesDialog.title"));
        this.setResizable(false);
        this.setContentPane(getJContentPane());
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            selectRole = new JLabel();
            selectRole.setText(Bundle.getText("rolesDialog.selectRole"));
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getNorth(), BorderLayout.NORTH);
            jContentPane.add(getCenter(), BorderLayout.CENTER);
            jContentPane.add(getSouth(), BorderLayout.SOUTH);
        }
        return jContentPane;
    }

    /**
	 * This method initializes listeRole
	 * 
	 * @return javax.swing.JComboBox
	 */
    private JComboBox getListeRole() {
        if (listeRole == null) {
            listeRole = new JComboBox();
            listeRole.setPreferredSize(new Dimension(31, 20));
            listeRole.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    RolesDialog.this.setDescription(((RoleDescriptor) listeRole.getSelectedItem()).getName());
                }
            });
        }
        return listeRole;
    }

    /**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.gridy = 0;
            jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(getJPanel(), BoxLayout.X_AXIS));
            jPanel.setPreferredSize(new Dimension(68, 30));
            jPanel.add(getCancel(), null);
            jPanel.add(getOK(), null);
        }
        return jPanel;
    }

    /**
	 * This method initializes cancel
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getCancel() {
        if (cancel == null) {
            cancel = new JButton();
            cancel.setPreferredSize(new Dimension(80, 20));
            cancel.setMnemonic(KeyEvent.VK_UNDEFINED);
            cancel.setText(Bundle.getText("rolesDialog.cancel"));
            cancel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    choice = RolesDialog.CHOIX_CANCEL;
                    RolesDialog.this.setVisible(false);
                }
            });
        }
        return cancel;
    }

    /**
	 * This method initializes OK
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getOK() {
        if (OK == null) {
            OK = new JButton();
            OK.setPreferredSize(new Dimension(80, 20));
            OK.setHorizontalAlignment(SwingConstants.CENTER);
            OK.setText(Bundle.getText("rolesDialog.ok"));
            OK.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    choice = RolesDialog.CHOIX_OK;
                    RolesDialog.this.setVisible(false);
                }
            });
        }
        return OK;
    }

    public int getChoix() {
        return (choice);
    }

    public RoleDescriptor getModelElement() {
        return ((RoleDescriptor) listeRole.getSelectedItem());
    }

    public String getRealRole() {
        return ((RoleDescriptor) listeRole.getSelectedItem()).getName();
    }

    public String getRole() {
        return ((RoleDescriptor) listeRole.getSelectedItem()).getName();
    }

    /**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
            jScrollPane.setViewportView(getRoleDesc());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes roleDesc
	 * 
	 * @return javax.swing.JEditorPane
	 */
    private JEditorPane getRoleDesc() {
        if (roleDesc == null) {
            roleDesc = new JEditorPane();
            roleDesc.setEditable(false);
            roleDesc.setContentType("text/html");
            roleDesc.setBackground(SystemColor.control);
        }
        return roleDesc;
    }

    public void setDescription(String message) {
        roleDesc.setText(message);
        roleDesc.setCaretPosition(1);
    }

    /**
	 * This method initializes north
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getNorth() {
        if (north == null) {
            BorderLayout borderLayout1 = new BorderLayout();
            borderLayout1.setVgap(1);
            north = new JPanel();
            north.setLayout(borderLayout1);
            north.add(selectRole, BorderLayout.NORTH);
            north.add(getListeRole(), BorderLayout.SOUTH);
        }
        return north;
    }

    /**
	 * This method initializes center
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getCenter() {
        if (center == null) {
            BorderLayout borderLayout = new BorderLayout();
            center = new JPanel();
            center.setLayout(borderLayout);
            center.add(getJScrollPane(), BorderLayout.CENTER);
        }
        return center;
    }

    /**
	 * This method initializes south
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getSouth() {
        if (south == null) {
            south = new JPanel();
            south.setLayout(new BorderLayout());
            south.add(getJPanel(), BorderLayout.NORTH);
        }
        return south;
    }
}
