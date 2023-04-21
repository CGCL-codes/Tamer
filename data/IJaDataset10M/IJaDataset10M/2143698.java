package gui;

import project.*;
import javax.swing.table.AbstractTableModel;

/**
 * Implements a model for tables of attributes.
 * @author Paul A. Rubin <rubin@msu.edu>
 */
public class AttributeTableModel extends AbstractTableModel {

    private Main owner;

    private final String[] colNames = { "", "<html>Name</html>", "<html>Type</html>", "<html>ID?</html>", "<html>Case<br>Sensitive?</html>", "<html>Decimal<br>Places</html>", "<html>Priority</html>", "<html>Description</html>" };

    public static final int SELECT = 0;

    public static final int NAME = 1;

    public static final int TYPE = 2;

    public static final int ID = 3;

    public static final int CASESENSITIVE = 4;

    public static final int PRECISION = 5;

    public static final int PRIORITY = 6;

    public static final int DESCRIPTION = 7;

    /**
   * Constructor.
   * @param g the parent GUI
   */
    public AttributeTableModel(Main g) {
        super();
        this.owner = g;
    }

    /**
   * Get the number of rows.
   * @return the row count (number of existing attributes)
   */
    public int getRowCount() {
        Project project = owner.project;
        if (project == null) {
            return 0;
        } else {
            return project.getAttributeCount(true);
        }
    }

    /**
   * Get the number of columns.
   * @return the column count (including an extra column for a pointer)
   */
    public int getColumnCount() {
        return colNames.length;
    }

    /**
   * Gets the name of a column.
   * @param col the column index
   * @return the name of the column
   */
    @Override
    public String getColumnName(int col) {
        return colNames[col];
    }

    /**
   * Get the entry in a given cell
   * @param rowIndex the row index
   * @param columnIndex the column index
   * @return the entry in that cell
   */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Attribute a = owner.project.getAttributeByIndex(rowIndex);
        Attribute.AttributeType type = a.getType();
        switch(columnIndex) {
            case SELECT:
                return null;
            case NAME:
                return a.getName();
            case TYPE:
                return type;
            case ID:
                return (a == owner.project.getIDAttribute());
            case CASESENSITIVE:
                Boolean b = ((type == Attribute.AttributeType.LABEL || type == Attribute.AttributeType.CATEGORY || type == Attribute.AttributeType.AFFINITY) && a.isCaseSensitive());
                return b;
            case PRECISION:
                if (type == Attribute.AttributeType.NUMBER) {
                    return ((NumericAttribute) a).getDecimals();
                } else {
                    return null;
                }
            case PRIORITY:
                return (type == Attribute.AttributeType.LABEL) ? 0 : a.getPriority();
            case DESCRIPTION:
                return a.getDescription();
            default:
                return null;
        }
    }

    /**
   * Returns the data type for a column.
   * @param col the column index
   * @return the class of the entries in that column
   */
    @Override
    public Class getColumnClass(int col) {
        switch(col) {
            case SELECT:
            case NAME:
            case DESCRIPTION:
                return String.class;
            case TYPE:
                return Attribute.AttributeType.class;
            case CASESENSITIVE:
            case ID:
                return Boolean.class;
            case PRECISION:
            case PRIORITY:
                return Integer.class;
            default:
                return String.class;
        }
    }

    /**
   * Tests if a cell is editable.
   * @param r the row index
   * @param c the column index
   * @return true if the cell can be edited
   */
    @Override
    public boolean isCellEditable(int r, int c) {
        Attribute.AttributeType t = owner.project.getAttributeByIndex(r).getType();
        switch(c) {
            case NAME:
            case TYPE:
            case DESCRIPTION:
                return true;
            case ID:
                return t == Attribute.AttributeType.LABEL;
            case CASESENSITIVE:
                return t == Attribute.AttributeType.LABEL || t == Attribute.AttributeType.CATEGORY || t == Attribute.AttributeType.AFFINITY;
            case PRECISION:
                return t == Attribute.AttributeType.NUMBER;
            case PRIORITY:
                return t != Attribute.AttributeType.LABEL;
            case SELECT:
            default:
                return false;
        }
    }

    /**
   * Captures changes to the attribute table.
   * @param value the new entry in the cell
   * @param row the row index of the cell that changed
   * @param col the column index of the cell that changed
   */
    @Override
    public void setValueAt(Object value, int row, int col) {
        Project project = owner.project;
        Attribute a = owner.project.getAttributeByIndex(row);
        switch(col) {
            case NAME:
                String oldName = a.getName();
                a.setName((String) value);
                owner.project.renameAttribute(oldName, (String) value);
                break;
            case TYPE:
                Attribute.AttributeType t = Attribute.AttributeType.valueOf((String) value);
                if (project.isID(a) && t != Attribute.AttributeType.LABEL) {
                    project.unsetIDAttribute();
                }
                Attribute b;
                switch(t) {
                    case LABEL:
                        b = new Attribute();
                        break;
                    case CATEGORY:
                        b = new CategoricalAttribute();
                        break;
                    case NUMBER:
                        b = new NumericAttribute();
                        break;
                    case AFFINITY:
                        b = new AffinityAttribute();
                        break;
                    default:
                        b = new Attribute();
                }
                b.setName(a.getName());
                b.setDescription(a.getDescription());
                if (a.getType() != Attribute.AttributeType.LABEL && b.getType() != Attribute.AttributeType.LABEL) {
                    b.setPriority(a.getPriority());
                }
                if (a == owner.project.getIDAttribute()) {
                    project.unsetIDAttribute();
                    owner.resetIDLabel();
                }
                project.swapAttributes(a, b);
                break;
            case ID:
                if ((Boolean) value) {
                    project.setIDAttribute(a);
                } else {
                    project.unsetIDAttribute();
                }
                owner.resetIDLabel();
                break;
            case CASESENSITIVE:
                if (a.getType() == Attribute.AttributeType.LABEL || a.getType() == Attribute.AttributeType.CATEGORY || a.getType() == Attribute.AttributeType.AFFINITY) {
                    a.setCaseSensitive((Boolean) value);
                }
                break;
            case PRECISION:
                if (a.getType() == Attribute.AttributeType.NUMBER && ((Integer) value) >= -1) {
                    ((NumericAttribute) a).setDecimalPlaces((Integer) value);
                }
                break;
            case PRIORITY:
                a.setPriority((Integer) value);
                break;
            case DESCRIPTION:
                a.setDescription((String) value);
        }
        fireTableDataChanged();
    }
}
