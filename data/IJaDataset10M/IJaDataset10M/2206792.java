package org.dbgen.view;

import javax.swing.*;
import javax.swing.tree.*;
import org.dbgen.*;
import org.dbgen.event.*;

public class ProjectTreeNode extends javax.swing.tree.DefaultMutableTreeNode implements TableChangedListener {

    Project fieldProject = null;

    protected transient java.beans.PropertyChangeSupport propertyChange = new java.beans.PropertyChangeSupport(this);

    /**
   * ProjectTreeNode constructor comment.
   */
    public ProjectTreeNode() {
        super();
    }

    /**
   * ProjectTreeNode constructor comment.
   * @param arg1 java.lang.Object
   */
    public ProjectTreeNode(Object arg1) {
        super(arg1);
        setProject((Project) arg1);
    }

    /**
   * The addPropertyChangeListener method was generated to support the propertyChange field.
   */
    public synchronized void addPropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChange.addPropertyChangeListener(listener);
    }

    /**
   * The firePropertyChange method was generated to support the propertyChange field.
   */
    public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChange.firePropertyChange(propertyName, oldValue, newValue);
    }

    /**
   * Return the insertion position of this table inside this project node.
   * @return int
   * @param table org.dbgen.Table
   */
    protected int getInsertionPosition(Table table) {
        java.util.Vector tables = getProject().getTables();
        for (int i = 0; i < tables.size(); i++) {
            Table curr = (Table) tables.elementAt(i);
            if (table.toString().compareTo(curr.toString()) < 0) {
                return i;
            }
        }
        return tables.size();
    }

    /**
   * Gets the project property (org.dbgen.Project) value.
   * @return The project property value.
   * @see #setProject
   */
    public Project getProject() {
        if (fieldProject == null) {
            try {
                fieldProject = new Project();
            } catch (Throwable exception) {
                System.err.println("Exception creating project property.");
            }
        }
        ;
        return fieldProject;
    }

    /**
   * The removePropertyChangeListener method was generated to support the propertyChange field.
   */
    public synchronized void removePropertyChangeListener(java.beans.PropertyChangeListener listener) {
        propertyChange.removePropertyChangeListener(listener);
    }

    /**
   * Sets the project property (org.dbgen.Project) value.
   * @param project The new value for the property.
   * @see #getProject
   */
    public void setProject(Project project) {
        Project oldValue = fieldProject;
        fieldProject = project;
        firePropertyChange("project", oldValue, project);
        setUserObject(project);
        project.addTableChangedListener(this);
        return;
    }

    /**
   * Performs the tableAdded method.
   * @param event org.dbgen.TableAddedEvent
   */
    public void tableAdded(TableAddedEvent event) {
        return;
    }

    /**
   * Performs the tableDeleted method.
   * @param event org.dbgen.TableDeletedEvent
   */
    public void tableDeleted(TableDeletedEvent event) {
        return;
    }
}
