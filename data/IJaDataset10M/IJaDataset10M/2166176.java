package org.hardtokenmgmt.admin.ui.panels.editglprops;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.hardtokenmgmt.admin.control.AdminInterfacesFactory;
import org.hardtokenmgmt.admin.control.GlobalPropertyListUpdater;
import org.hardtokenmgmt.admin.ui.panels.editglprops.AddGlobalPropertyDialog;
import org.hardtokenmgmt.common.Constants;
import org.hardtokenmgmt.common.vo.OrganizationVO;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.ui.UIHelper;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Edit global properties main panel.
 * 
 * 
 * @author Philip Vendil 21 mar 2009
 *
 * @version $Id$
 */
public class EditGlobalPropertiesPanel extends JPanel implements Observer {

    private JTextField filterTextField;

    private JComboBox orgComboBox;

    private CurrentGlobalPropertiesTablePanel currentGlobalPropertiesTablePanel;

    private static final long serialVersionUID = 1L;

    private boolean edit = true;

    private JButton importButton;

    private JButton addButton;

    private JPanel thisPanel = this;

    private FormLayout fl;

    public EditGlobalPropertiesPanel() {
        super();
        initialize();
    }

    public EditGlobalPropertiesPanel(boolean edit) {
        super();
        this.edit = edit;
        initialize();
    }

    private String getOrgId() {
        return ((OrganizationVO) getOrgComboBox().getSelectedItem()).getOrgId();
    }

    private String getOrgName() {
        return ((OrganizationVO) getOrgComboBox().getSelectedItem()).getOrgName();
    }

    private CurrentGlobalPropertiesTablePanel getCurrentGlobalPropertiesTablePanel() {
        if (currentGlobalPropertiesTablePanel == null) {
            currentGlobalPropertiesTablePanel = new CurrentGlobalPropertiesTablePanel(getOrgId(), edit);
        }
        return currentGlobalPropertiesTablePanel;
    }

    private Component getFilterTextField() {
        if (filterTextField == null) {
            filterTextField = new JTextField();
            filterTextField.addKeyListener(new KeyAdapter() {

                public void keyReleased(final KeyEvent e) {
                    getCurrentGlobalPropertiesTablePanel().setFilter(filterTextField.getText());
                }
            });
        }
        return filterTextField;
    }

    private JComboBox getOrgComboBox() {
        if (orgComboBox == null) {
            List<OrganizationVO> availOrgs = new ArrayList<OrganizationVO>();
            try {
                availOrgs = AdminInterfacesFactory.getOrganizationManager().getOrganisations();
            } catch (Exception e1) {
                LocalLog.getLogger().log(Level.SEVERE, "Error fetching organizations : " + e1.getMessage(), e1);
            }
            orgComboBox = new JComboBox();
            orgComboBox.addItem(new OrganizationVO(Constants.SPECIAL_ORGID_ALL, UIHelper.getText("editglobprops.defaultpropeties"), false, false, null));
            for (OrganizationVO org : availOrgs) {
                if (!org.getOrgId().equals(Constants.SPECIAL_ADMINGROUP_SUPERADMINS)) {
                    orgComboBox.addItem(org);
                }
            }
            orgComboBox.addActionListener(new ActionListener() {

                public void actionPerformed(final ActionEvent e) {
                    loadOrgData();
                }
            });
        }
        return orgComboBox;
    }

    private void initialize() {
        GlobalPropertyListUpdater.getInstance().addObserver(this);
        setBackground(Color.white);
        fl = new FormLayout(new ColumnSpec[] { ColumnSpec.decode("80px"), ColumnSpec.decode("40px"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("120px"), ColumnSpec.decode("180px"), FormFactory.UNRELATED_GAP_COLSPEC, ColumnSpec.decode("190px"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("190px"), ColumnSpec.decode("190px:grow(1.0)") }, new RowSpec[] { FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, RowSpec.decode("27dlu"), FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, RowSpec.decode("pref") });
        setLayout(fl);
        final JLabel titleLabel = new JLabel();
        if (edit) {
            titleLabel.setText(UIHelper.getText("editglobprops.editglobalprops"));
        } else {
            titleLabel.setText(UIHelper.getText("editglobprops.viewglobalprops"));
        }
        titleLabel.setFont(UIHelper.getTitleFont());
        add(titleLabel, new CellConstraints(1, 1, 8, 1));
        final JLabel orgLabel = new JLabel();
        orgLabel.setText(UIHelper.getText("organization") + ":");
        add(orgLabel, new CellConstraints(1, 3, 2, 1, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        add(getOrgComboBox(), new CellConstraints(4, 3, 2, 1));
        addButton = new JButton();
        addButton.setVisible(edit);
        addButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                AddGlobalPropertyDialog addGlobalPropertyDialog = new AddGlobalPropertyDialog(getOrgId(), getCurrentGlobalPropertiesTablePanel().getTableModel(), (Window) SwingUtilities.getRoot(thisPanel));
                addGlobalPropertyDialog.setLocationRelativeTo(SwingUtilities.getRootPane(thisPanel));
                addGlobalPropertyDialog.setVisible(true);
            }
        });
        addButton.setIcon(UIHelper.getImage("addglobalprops_small.png"));
        addButton.setText(UIHelper.getText("editglobprops.add") + "...");
        add(addButton, new CellConstraints(7, 3));
        importButton = new JButton();
        importButton.setVisible(edit);
        importButton.setIcon(UIHelper.getImage("addmultipleglobalprops_small.png"));
        importButton.setText(UIHelper.getText("editglobprops.addtemplate") + "...");
        importButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                ImportGlobalPropertiesDialog importTemplateDialog = new ImportGlobalPropertiesDialog(getOrgId(), getOrgName(), (Window) SwingUtilities.getRoot(thisPanel));
                importTemplateDialog.setLocationRelativeTo(SwingUtilities.getRootPane(thisPanel));
                importTemplateDialog.setVisible(true);
            }
        });
        add(importButton, new CellConstraints(9, 3));
        final JLabel currentPropsLabel = new JLabel();
        currentPropsLabel.setText(UIHelper.getText("editglobprops.currentprops"));
        currentPropsLabel.setFont(UIHelper.getLabelFontBold());
        add(currentPropsLabel, new CellConstraints(1, 5, 4, 1, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JLabel filterLabel = new JLabel();
        filterLabel.setText(UIHelper.getText("editadmins.filterby") + ":");
        add(filterLabel, new CellConstraints(7, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        add(getFilterTextField(), new CellConstraints(9, 5, 1, 1, CellConstraints.DEFAULT, CellConstraints.DEFAULT));
        add(getCurrentGlobalPropertiesTablePanel(), "1, 7, 10, 1, fill, fill");
        setTableHeight(getCurrentGlobalPropertiesTablePanel().getTableHeight());
        final JButton refreshButton = new JButton();
        refreshButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                loadOrgData();
            }
        });
        refreshButton.setText(UIHelper.getText("editadmins.refresh"));
        refreshButton.setIcon(UIHelper.getImage("reload.png"));
        add(refreshButton, new CellConstraints(5, 5));
    }

    private void setTableHeight(int height) {
        fl.setRowSpec(7, RowSpec.decode(height + 30 + "px"));
    }

    private void loadOrgData() {
        OrganizationVO selected = (OrganizationVO) orgComboBox.getSelectedItem();
        currentGlobalPropertiesTablePanel.setOrgId(selected.getOrgId());
        importButton.setEnabled(true);
        setTableHeight(getCurrentGlobalPropertiesTablePanel().getTableHeight());
        thisPanel.repaint();
        revalidate();
    }

    @Override
    public void update(Observable o, Object arg) {
        getCurrentGlobalPropertiesTablePanel().updateHeight();
        setTableHeight(getCurrentGlobalPropertiesTablePanel().getTableHeight());
        thisPanel.repaint();
        revalidate();
    }

    public static void main(String args[]) throws Exception {
        UIHelper.setTheme();
        EditGlobalPropertiesPanel app = new EditGlobalPropertiesPanel(true);
        JFrame jFrame = new JFrame();
        jFrame.setSize(new java.awt.Dimension(1024, 600));
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel jContentPane = new JPanel();
        jContentPane.setLayout(new BorderLayout());
        jContentPane.add(app, BorderLayout.CENTER);
        jFrame.setContentPane(jContentPane);
        jFrame.setVisible(true);
    }
}
