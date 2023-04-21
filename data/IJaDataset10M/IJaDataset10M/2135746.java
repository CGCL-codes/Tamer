package lablog.gui.comp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.ListDataListener;
import lablog.util.orm.DatabaseHelper;
import lablog.util.orm.auto.Organisation;

/**
 * A simple panel showing a combobox with available departments.
 * The panel can be created with different modes: {@link #NEW_DEP} and {@link #OTHER_DEP}.
 * <br /><br />
 * With the {@link #NEW_DEP} mode an option "New Affiliation" is shown inside the combobox. 
 * The {@link #OTHER_DEP} mode provides an option "Other". This can be used to create new departments
 * or to allow the selection of no specific department.
 */
public class DepartmentChooser extends JPanel {

    private static final long serialVersionUID = 659546424909984699L;

    /** The default mode: no additional option is displayed */
    public static final int EXISTING_DEPS = 0;

    /** This mode displays an additional option "New Affiliation" inside the combobox */
    public static final int NEW_DEP = 1;

    /** This mode displays an additional option "Other" inside the combobox */
    public static final int OTHER_DEP = 2;

    private DepartmentModel departmentModel;

    private JComboBox departmentCombo;

    private int mode;

    /**
	 * The default constructor.
	 * The panel s initialized with the default mode {@link #EXISTING_DEPS}.
	 */
    public DepartmentChooser() {
        this(EXISTING_DEPS, null);
    }

    /**
	 * Extended constructor.
	 *  
	 * @param mode {@link #EXISTING_DEPS}, {@link #OTHER_DEP} or {@link #NEW_DEP}.
	 */
    public DepartmentChooser(int mode) {
        this(mode, null);
    }

    /**
	 * Extended constructor.
	 * 
	 * @param mode {@link #EXISTING_DEPS}, {@link #OTHER_DEP} or {@link #NEW_DEP}.
	 * @param selected The department that should be preselected inside the combobox.
	 */
    public DepartmentChooser(int mode, Organisation selected) {
        this.departmentModel = new DepartmentModel(mode, selected);
        this.mode = mode;
        initGUI();
    }

    /**
	 * Refresh the combobox.
	 * 
	 * @param dep The department, that should be selected after the refresh.
	 */
    public void refresh(Organisation dep) {
        invalidate();
        departmentModel = new DepartmentModel(mode, dep);
        departmentCombo.setModel(departmentModel);
        validate();
    }

    /**
	 * Prepare the GUI.
	 */
    private void initGUI() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        departmentCombo = new JComboBox(departmentModel);
        GridBagConstraints constr = new GridBagConstraints();
        constr.fill = GridBagConstraints.BOTH;
        constr.gridx = 1;
        constr.gridy = 1;
        constr.gridwidth = 1;
        constr.gridheight = 1;
        constr.weightx = 1.0;
        constr.anchor = GridBagConstraints.WEST;
        constr.insets = new Insets(0, 0, 0, 0);
        layout.addLayoutComponent(departmentCombo, constr);
        add(departmentCombo);
    }

    /**
	 * Returns the department selected by the combobox.
	 * 
	 * @return The selected department.
	 */
    public Organisation getSelectedDepartment() {
        return departmentModel.getSelectedDepartment();
    }

    /**
	 * Set a action listener for the combobox {@link JComboBox#addActionListener(ActionListener)}.
	 * 
	 * @param listener The action listener to add.
	 */
    public void addActionListener(ActionListener listener) {
        departmentCombo.addActionListener(listener);
    }

    /**
	 * This model represents a set of departments stored in the database.
	 * In the current implementation, the model caches all departments in a private collection,
	 * in not mutable and does not refresh its content when the content of the database changes.
	 * In consequence the best way to refresh the combobox is to crate an new instance of this 
	 * model and discard the old one.
	 * 
	 * TODO Implement a better DepartmentModel an a better way to refresh the combobox.
	 */
    private final class DepartmentModel implements ComboBoxModel {

        private int mode;

        private String selected;

        private Vector<Organisation> departments;

        /**
		 * The constructor.
		 * 
		 * @param mode {@link #EXISTING_DEPS}, {@link #OTHER_DEP} or {@link #NEW_DEP}.
		 * @param selected The preselected department.
		 */
        public DepartmentModel(int mode, Organisation selected) {
            this.mode = mode;
            EntityManager em = DatabaseHelper.instance().getEntityManager();
            TypedQuery<Organisation> q = em.createQuery("from Department", Organisation.class);
            departments = new Vector<Organisation>(q.getResultList());
            em.close();
            if (selected == null) this.selected = getElementAt(0).toString(); else this.selected = selected.getName() + ", " + selected.getDepartment();
        }

        /**
		 * Return the selected department.
		 * If the model has been created with the {@link #OTHER_DEP} option and "Other" is 
		 * currently selected, this method returns null. If the {@link #NEW_DEP} option was used 
		 * for creation and "New Affiliation" is selected, a new department is returned.
		 * 
		 * @return The selected department or null.
		 */
        public Organisation getSelectedDepartment() {
            Organisation dep = null;
            if (selected.equals("New Affiliation")) {
                dep = new Organisation();
            } else if (!selected.equals("Other")) {
                boolean found = false;
                for (int i = 0; !found && i < departments.size(); i++) {
                    Organisation d = departments.elementAt(i);
                    if ((d.getName() + ", " + d.getDepartment()).equals(selected)) {
                        dep = d;
                        found = true;
                    }
                }
            }
            return dep;
        }

        /**
		 * Returns a string representation of the selected item as shown inside the combobox.
		 * 
		 * @return The currently selected item.
		 */
        @Override
        public Object getSelectedItem() {
            return selected;
        }

        /**
		 * Sets the selected item.
		 * 
		 * @param item A string representation of the selected item as shown inside the combobox.
		 */
        @Override
        public void setSelectedItem(Object item) {
            selected = item.toString();
        }

        /**
		 * Returns the element/item at a given position.
		 * The item is a string representation of a department.
		 * 
		 * @param index The position of the item.
		 */
        @Override
        public Object getElementAt(int index) {
            String name;
            if (mode == DepartmentChooser.NEW_DEP) {
                if (index == 0) {
                    name = "New Affiliation";
                } else {
                    Organisation dep = departments.get(index - 1);
                    name = dep.getName() + ", " + dep.getDepartment();
                }
            } else if (mode == DepartmentChooser.OTHER_DEP) {
                if (index == 0) {
                    name = "Other";
                } else {
                    Organisation dep = departments.get(index - 1);
                    name = dep.getName() + ", " + dep.getDepartment();
                }
            } else {
                Organisation dep = departments.get(index);
                name = dep.getName() + ", " + dep.getDepartment();
            }
            return name;
        }

        /**
		 * Returns the number of items in the current model.
		 * 
		 * @return The number if items.
		 */
        @Override
        public int getSize() {
            int size;
            if (mode == DepartmentChooser.NEW_DEP || mode == DepartmentChooser.OTHER_DEP) size = departments.size() + 1; else size = departments.size();
            return size;
        }

        /**
		 * Empty implementation.
		 */
        @Override
        public void addListDataListener(ListDataListener arg0) {
        }

        /**
		 * Empty implementation.
		 */
        @Override
        public void removeListDataListener(ListDataListener arg0) {
        }
    }
}
