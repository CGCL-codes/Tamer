package lablog.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lablog.app.Application;
import lablog.gui.comp.DepartmentChooser;
import lablog.util.SecurityHelper;
import lablog.util.actions.AbstractDeleteAction;
import lablog.util.actions.AbstractSubmitAction;
import lablog.util.actions.LablogAction;
import lablog.util.actions.WindowCancelAction;
import lablog.util.orm.DatabaseHelper;
import lablog.util.orm.auto.Organisation;

/**
 * A dialog for managing affiliations/departments.
 * The dialog shows a combobox with all available departments on top of the window. If a
 * department is selected all data are displayed in various editable text fields in the center
 * of the dialog. On the bottom are three buttons: save, delete and cancel. 
 * 
 * TODO It would be a nice feature if the user is warned when he selects an other department and 
 * some changes are unsaved.
 */
public class EditAffiliationDialog extends JDialog {

    private static final long serialVersionUID = -6006243831800348905L;

    private JTextField organisation, department, address, pcode, city, country;

    private DepartmentChooser affiliations;

    public EditAffiliationDialog() {
        super(Application.instance().getMainWindow());
        setModal(true);
        setTitle("Edit affiliations");
        initGUI();
        setResizable(false);
        setSize(330, 370);
        setLocationRelativeTo(Application.instance().getMainWindow());
        setVisible(true);
    }

    /**
	 * Display the data of the given department inside the text fields. The combobox is not
	 * effected this method. If department is null, all text fields are empty. In most cases
	 * it is preferred to call {@link #refresh(Organisation)} instead of this method.
	 * 
	 * @param dep The department do display inside the text fields.
	 */
    public void setActualDepartment(Organisation dep) {
        if (dep == null) {
            organisation.setText("");
            department.setText("");
            address.setText("");
            pcode.setText("");
            city.setText("");
            country.setText("");
        } else {
            organisation.setText(dep.getName());
            department.setText(dep.getDepartment());
            address.setText(dep.getAddress());
            pcode.setText(dep.getPostalCode());
            city.setText(dep.getCity());
            country.setText(dep.getCountry());
        }
    }

    /**
	 * Returns the currently selected department.
	 * The data of the department are overwritten by the values of the text fields, but not
	 * persisted in the database.
	 * 
	 * @return The actual department.
	 */
    public Organisation getActualDepartment() {
        Organisation dep = affiliations.getSelectedDepartment();
        dep.setName(organisation.getText().equals("") ? null : organisation.getText());
        dep.setDepartment(department.getText().equals("") ? null : department.getText());
        dep.setAddress(address.getText().equals("") ? null : address.getText());
        dep.setPostalCode(pcode.getText().equals("") ? null : pcode.getText());
        dep.setCity(city.getText().equals("") ? null : city.getText());
        dep.setCountry(country.getText().equals("") ? null : country.getText());
        return dep;
    }

    /**
	 * Refreshes the content of the combobox an sets the selected an displayed department
	 * to the given one.
	 * 
	 * @param dep The selected and displayed department after refresh.
	 */
    public void refresh(Organisation dep) {
        affiliations.refresh(dep);
        setActualDepartment(dep);
    }

    /**
	 * Initialize the GUI.
	 */
    private void initGUI() {
        BorderLayout borderLayout = new BorderLayout();
        getContentPane().setLayout(borderLayout);
        JPanel chooserPanel = new JPanel();
        chooserPanel.setBorder(BorderFactory.createTitledBorder("Affiliations"));
        GridLayout chooserLayout = new GridLayout(1, 1);
        chooserPanel.setLayout(chooserLayout);
        affiliations = new DepartmentChooser(DepartmentChooser.NEW_DEP);
        chooserPanel.add(affiliations);
        affiliations.addActionListener(new DepartmentsActionListener(affiliations, this));
        JPanel dataPanel = new JPanel();
        dataPanel.setBorder(BorderFactory.createTitledBorder("Affiliation data"));
        GridBagLayout dataLayout = new GridBagLayout();
        dataPanel.setLayout(dataLayout);
        addConstraint(dataLayout, dataPanel, new JLabel("Organisation"), 1, 1, 1, false);
        organisation = new JTextField();
        addConstraint(dataLayout, dataPanel, organisation, 2, 1, 4, true);
        addConstraint(dataLayout, dataPanel, new JLabel("Department"), 1, 2, 1, false);
        department = new JTextField();
        addConstraint(dataLayout, dataPanel, department, 2, 2, 4, true);
        addConstraint(dataLayout, dataPanel, new JLabel("Address"), 1, 3, 1, false);
        address = new JTextField();
        addConstraint(dataLayout, dataPanel, address, 2, 3, 4, true);
        addConstraint(dataLayout, dataPanel, new JLabel("Postal code"), 1, 4, 1, false);
        pcode = new JTextField();
        addConstraint(dataLayout, dataPanel, pcode, 2, 4, 4, true);
        addConstraint(dataLayout, dataPanel, new JLabel("City"), 1, 5, 1, false);
        city = new JTextField();
        addConstraint(dataLayout, dataPanel, city, 2, 5, 4, true);
        addConstraint(dataLayout, dataPanel, new JLabel("Country"), 1, 6, 1, false);
        country = new JTextField();
        addConstraint(dataLayout, dataPanel, country, 2, 6, 4, true);
        JPanel buttonPanel = new JPanel();
        GridBagLayout buttonLayout = new GridBagLayout();
        buttonPanel.setLayout(buttonLayout);
        LablogAction submitAction = new AffiliationSubmitAction(this);
        buttonPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(submitAction.getAccel(), submitAction.getName());
        buttonPanel.getActionMap().put(submitAction.getName(), submitAction);
        JButton submitButton = new JButton(submitAction);
        LablogAction deleteAction = new AffiliationDeleteAction(this);
        buttonPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(deleteAction.getAccel(), deleteAction.getName());
        buttonPanel.getActionMap().put(deleteAction.getName(), deleteAction);
        JButton deleteButton = new JButton(deleteAction);
        LablogAction cancelAction = new WindowCancelAction(this);
        buttonPanel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(cancelAction.getAccel(), cancelAction.getName());
        buttonPanel.getActionMap().put(cancelAction.getName(), cancelAction);
        JButton cancelButton = new JButton(cancelAction);
        if (submitButton.getPreferredSize().width > deleteButton.getPreferredSize().width) deleteButton.setPreferredSize(submitButton.getPreferredSize()); else submitButton.setPreferredSize(deleteButton.getPreferredSize());
        addConstraint(buttonLayout, buttonPanel, deleteButton, 1, 1, 2, true);
        addConstraint(buttonLayout, buttonPanel, submitButton, 3, 1, 2, true);
        addConstraint(buttonLayout, buttonPanel, cancelButton, 1, 2, 4, true);
        getContentPane().add(chooserPanel, BorderLayout.NORTH);
        getContentPane().add(dataPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
	 * Used internally for easier GridBagLayouts. 
	 * 
	 * @param l The used Layout.
	 * @param p The parent container.
	 * @param c The component to add to the parent container.
	 * @param posx The horizontal position.
	 * @param posy The vertical position.
	 * @param width The horizontal cell consumption. 
	 * @param fill Fill left over horizontal space.
	 */
    private void addConstraint(GridBagLayout l, Container p, Component c, int posx, int posy, int width, boolean fill) {
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = posx;
        constr.gridy = posy;
        constr.gridwidth = width;
        constr.gridheight = 1;
        constr.weightx = fill ? 1.0 : 0.0;
        constr.anchor = GridBagConstraints.WEST;
        constr.insets = new Insets(2, 2, 2, 2);
        l.addLayoutComponent(c, constr);
        p.add(c);
    }

    /**
	 * The action to perform for submissions.
	 */
    @SuppressWarnings("serial")
    private final class AffiliationSubmitAction extends AbstractSubmitAction {

        private EditAffiliationDialog dialog;

        public AffiliationSubmitAction(EditAffiliationDialog dialog) {
            super();
            this.dialog = dialog;
        }

        @Override
        protected boolean performTest() {
            if (SecurityHelper.isPrivilegedUser()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(Application.instance().getMainWindow(), "You are not allowed to edit affiliations!", "Error!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        @Override
        protected void testedActionPerformed(ActionEvent event) {
            try {
                Organisation dep = dialog.getActualDepartment();
                EntityManager em = DatabaseHelper.instance().getEntityManager();
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                dep = em.merge(dep);
                em.persist(dep);
                tx.commit();
                em.close();
                dialog.refresh(dep);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Application.instance().getMainWindow(), e.getMessage(), "Unknown Error!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
	 * The action to perform on deletions. 
	 */
    @SuppressWarnings("serial")
    private final class AffiliationDeleteAction extends AbstractDeleteAction {

        private EditAffiliationDialog dialog;

        public AffiliationDeleteAction(EditAffiliationDialog dialog) {
            super();
            this.dialog = dialog;
        }

        @Override
        protected boolean performTest() {
            if (SecurityHelper.isPrivilegedUser()) {
                return true;
            } else {
                JOptionPane.showMessageDialog(Application.instance().getMainWindow(), "You are not allowed to delete affiliations!", "Error!", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        @Override
        protected void testedActionPerformed(ActionEvent event) {
            try {
                Organisation dep = dialog.getActualDepartment();
                EntityManager em = DatabaseHelper.instance().getEntityManager();
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                dep = em.merge(dep);
                em.remove(dep);
                tx.commit();
                em.close();
                dialog.refresh(dep);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(Application.instance().getMainWindow(), e.getMessage(), "Unknown Error!", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
	 * A action listener for the combobox.
	 */
    private class DepartmentsActionListener implements ActionListener {

        private DepartmentChooser departments;

        private EditAffiliationDialog dialog;

        public DepartmentsActionListener(DepartmentChooser departments, EditAffiliationDialog dialog) {
            this.departments = departments;
            this.dialog = dialog;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.setActualDepartment(departments.getSelectedDepartment());
        }
    }
}
