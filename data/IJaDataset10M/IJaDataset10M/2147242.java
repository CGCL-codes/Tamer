package de.flingelli.scrum.gui.task;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import de.flingelli.scrum.datastructure.Product;
import de.flingelli.scrum.datastructure.Task;
import de.flingelli.scrum.gui.util.EnabledCellRenderer;
import de.flingelli.scrum.language.JastTranslation;
import de.flingelli.scrum.observer.ProductPropertyChangeSupport;

@SuppressWarnings("serial")
public class TaskWorkingDatePanel extends JPanel implements PropertyChangeListener {

    private JLabel jWorkingDateLabel = null;

    private JScrollPane jWorkingDateScrollPane = null;

    private JTable jWorkingDateTable = null;

    private Product mProduct = null;

    private Task mTask = null;

    private void changeLanguage() {
        jWorkingDateLabel.setText(JastTranslation.getInstance().getValue("de.flingelli.scrum.gui.task.TaskWorkingDatePanel-WorkingDate"));
    }

    public void setValues(Product product, Task task) {
        mProduct = product;
        mTask = task;
        displayData();
    }

    /**
	 * This method initializes 
	 * 
	 */
    public TaskWorkingDatePanel(Product product, Task task) {
        super();
        mProduct = product;
        mTask = task;
        ProductPropertyChangeSupport productPropertyChangeSupport = ProductPropertyChangeSupport.getInstance(mProduct);
        productPropertyChangeSupport.addPropertyChangeListener(this);
        initialize();
        changeLanguage();
        displayData();
    }

    private void displayData() {
        getJWorkingDateTable();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        jWorkingDateLabel = new JLabel();
        jWorkingDateLabel.setBounds(new Rectangle(15, 350, 200, 20));
        jWorkingDateLabel.setText("Working Date");
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(673, 658));
        this.add(jWorkingDateLabel, BorderLayout.NORTH);
        this.add(getJWorkingDateScrollPane(), BorderLayout.CENTER);
    }

    /**
	 * This method initializes jWorkingDateScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJWorkingDateScrollPane() {
        if (jWorkingDateScrollPane == null) {
            jWorkingDateScrollPane = new JScrollPane();
            jWorkingDateScrollPane.setBounds(new Rectangle(15, 380, 630, 250));
            jWorkingDateScrollPane.setViewportView(getJWorkingDateTable());
        }
        return jWorkingDateScrollPane;
    }

    /**
	 * This method initializes jWorkingDateTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJWorkingDateTable() {
        WorkingUnitTableModel workingUnitTableModel = new WorkingUnitTableModel(mProduct, mTask);
        if (jWorkingDateTable == null) {
            jWorkingDateTable = new JTable(workingUnitTableModel);
            jWorkingDateTable.setName("task_workingdate_table");
        } else {
            jWorkingDateTable.setModel(workingUnitTableModel);
        }
        jWorkingDateTable.getColumnModel().getColumn(0).setCellRenderer(new EnabledCellRenderer());
        jWorkingDateTable.getColumnModel().getColumn(1).setCellRenderer(new EnabledCellRenderer());
        jWorkingDateTable.getColumnModel().getColumn(2).setCellRenderer(new EnabledCellRenderer());
        jWorkingDateTable.setFillsViewportHeight(true);
        jWorkingDateTable.getColumnModel().getColumn(0).setCellEditor(new DateChooserCellEditor());
        return jWorkingDateTable;
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("undo")) {
            displayData();
        } else if (event.getPropertyName().equals("language_changed")) {
            changeLanguage();
        }
    }
}
