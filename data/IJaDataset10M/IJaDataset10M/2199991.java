package projectviewer.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.util.Log;
import projectviewer.Project;

/**
 * A dialog for displaying project properties.
 */
public class PropertiesPanel extends JPanel implements MouseListener, ActionListener {

    public static final String ADD_PROPERTY_CMD = "addProperty";

    public static final String REMOVE_PROPERTY_CMD = "removeProperty";

    private Project project;

    private PropertiesModel props;

    private JTable table;

    /**
    * Create a new <code>PropertiesPanel</code>.
    */
    public PropertiesPanel() {
        initComponents();
    }

    /**
    * Show a dialog allowing the user to configure the project's properties.
    */
    public static void showDialog(View view, Project prj) {
        final PropertiesPanel propsPanel = new PropertiesPanel();
        propsPanel.setProject(prj);
        ConfigDialog dialog = new ConfigDialog(view, "Properties for " + prj.getName(), propsPanel) {

            public void ok() {
                if (!propsPanel.isEditingProperty()) {
                    super.ok();
                    return;
                }
                propsPanel.stopEditingProperty();
            }
        };
        UI.center(dialog);
        dialog.setVisible(true);
        if (dialog.isOk()) {
            propsPanel.synchProperties();
        }
    }

    /**
    * Returns <code>true</code> if a property is currently being edited.
    */
    public boolean isEditingProperty() {
        return table.isEditing();
    }

    /**
    * Stop current editing session.
    */
    public void stopEditingProperty() {
        table.getCellEditor().stopCellEditing();
    }

    /**
    * Synchronize the properties in this panel with the properties in current
    * project.
    */
    public void synchProperties() {
        List delProps = new ArrayList();
        for (Iterator i = project.propertyNames(); i.hasNext(); ) {
            String name = i.next().toString();
            if (!props.hasProperty(name)) delProps.add(name);
        }
        for (Iterator i = delProps.iterator(); i.hasNext(); ) {
            project.removeProperty(i.next().toString());
        }
        Properties newProps = props.getProperties();
        Enumeration propNames = newProps.propertyNames();
        while (propNames.hasMoreElements()) {
            String name = propNames.nextElement().toString();
            project.setProperty(name, newProps.getProperty(name));
        }
    }

    /**
    * Remove the currently selected property.
    */
    public void removeProperty() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        props.removeProperty(row);
    }

    /**
    * Add a property.
    */
    public void addProperty() {
        String propName = UI.input(this, "add-property", "");
        if (propName == null || propName.trim().length() < 1) return;
        if (props.getProperty(propName) != null) {
            boolean result = UI.confirmYesNo(this, "overwrite-property", new String[] { propName });
            if (!result) return;
        }
        props.setProperty(propName, "");
        final int idx = props.indexOfProperty(propName);
        table.editCellAt(idx, 1);
    }

    /**
    * Handle panel actions.
    */
    public void actionPerformed(ActionEvent evt) {
        if (ADD_PROPERTY_CMD.equals(evt.getActionCommand())) {
            addProperty();
        } else if (REMOVE_PROPERTY_CMD.equals(evt.getActionCommand())) {
            removeProperty();
        }
    }

    /**
    * Set the project that this panel will extract the properties from.
    */
    public void setProject(Project aProject) {
        project = aProject;
        Properties prjProps = new Properties();
        for (Iterator i = aProject.propertyNames(); i.hasNext(); ) {
            String name = i.next().toString();
            prjProps.setProperty(name, aProject.getProperty(name));
        }
        props.setProperties(prjProps);
    }

    /**
    * Handle a mouse enter.
    */
    public final void mouseEntered(MouseEvent evt) {
    }

    /**
    * Handle a mouse exit.
    */
    public final void mouseExited(MouseEvent evt) {
    }

    /**
    * Handle a mouse press.
    */
    public final void mousePressed(MouseEvent evt) {
        if (!GUIUtilities.isPopupTrigger(evt)) return;
        JPopupMenu popup = new JPopupMenu();
        popup.add(createMenuItem("Add", ADD_PROPERTY_CMD));
        if (table.getSelectedRow() != -1) popup.add(createMenuItem("Remove", REMOVE_PROPERTY_CMD));
        popup.show((Component) evt.getSource(), evt.getX(), evt.getY());
    }

    /**
    * Handle a mouse click.
    */
    public final void mouseClicked(MouseEvent evt) {
    }

    /**
    * Handle a mouse release.
    */
    public final void mouseReleased(MouseEvent evt) {
    }

    /**
    * Initialize panel components.
    */
    private void initComponents() {
        setLayout(new BorderLayout());
        table = new JTable(props = new PropertiesModel());
        table.addMouseListener(this);
        table.getColumn(props.getColumnName(0)).setCellRenderer(new UneditableRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table));
        addMouseListener(this);
    }

    /**
    * A table model that models a properties object.
    */
    private class PropertiesModel extends AbstractTableModel {

        private List names;

        private Properties props;

        /**
       * Create a new <code>PropertiesModel</code>.
       */
        public PropertiesModel() {
            names = Collections.EMPTY_LIST;
            props = new Properties();
        }

        /**
       * Returns <code>true</code> if the specified property exists.
       */
        public boolean hasProperty(String name) {
            return getProperty(name) != null;
        }

        /**
       * Remove the property at the given index.
       */
        public void removeProperty(int row) {
            String name = names.remove(row).toString();
            props.remove(name);
            fireTableRowsDeleted(row, row);
        }

        /**
       * Returns the value of a property.
       */
        public String getProperty(String name) {
            return props.getProperty(name);
        }

        /**
       * Set a property, adding it if it doesn't exist.
       */
        public void setProperty(String name, String value) {
            if (names.contains(name)) {
                props.setProperty(name, value);
                int idx = indexOfProperty(name);
                fireTableRowsUpdated(idx, idx);
            } else {
                props.setProperty(name, value);
                names.add(name);
                Collections.sort(names);
                int idx = indexOfProperty(name);
                fireTableRowsInserted(idx, idx);
            }
        }

        /**
       * Returns the index of the named property.
       */
        public int indexOfProperty(String name) {
            return names.indexOf(name);
        }

        /**
       * Set the properties that this model will model.
       */
        public void setProperties(Properties theProperties) {
            props = theProperties;
            names = new ArrayList(props.size());
            Enumeration propNames = props.propertyNames();
            while (propNames.hasMoreElements()) {
                names.add(propNames.nextElement());
            }
            Collections.sort(names);
            fireTableDataChanged();
        }

        /**
       * Returns the properties object.
       */
        public Properties getProperties() {
            return props;
        }

        /**
       * Returns the number of rows.
       */
        public int getRowCount() {
            return names.size();
        }

        /**
       * Returns the number of columns.
       */
        public int getColumnCount() {
            return 2;
        }

        /**
       * Returns the value at the given cell.
       */
        public Object getValueAt(int row, int col) {
            switch(col) {
                case 0:
                    return names.get(row);
                case 1:
                    return props.getProperty(names.get(row).toString());
            }
            return null;
        }

        /**
       * Returns the column name.
       */
        public String getColumnName(int col) {
            switch(col) {
                case 0:
                    return "Name";
                case 1:
                    return "Value";
            }
            return null;
        }

        /**
       * Returns whether a cell is editable.
       */
        public boolean isCellEditable(int row, int col) {
            return (col == 1);
        }

        /**
       * Change a cell value.
       */
        public void setValueAt(Object value, int row, int col) {
            if (col != 1) return;
            props.setProperty(names.get(row).toString(), value.toString());
        }
    }

    /**
    * A table renderer for the name cell.
    */
    private class UneditableRenderer extends DefaultTableCellRenderer {

        /**
       * Create a new <code>UneditableRenderer</code>.
       */
        public UneditableRenderer() {
            setBackground(UIManager.getColor("control"));
        }
    }

    /**
    * Create a menu item with the given label and action command.
    */
    private JMenuItem createMenuItem(String label, String actionCommand) {
        JMenuItem mi = new JMenuItem(label);
        mi.setActionCommand(actionCommand);
        mi.addActionListener(this);
        return mi;
    }
}
