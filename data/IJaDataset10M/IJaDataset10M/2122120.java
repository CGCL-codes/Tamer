package net.frede.gui.gui.explorer.table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.frede.toolbox.Invoker;
import org.apache.log4j.Logger;

/**
 * Represents an abstract class of a linear table model, that enables view of
 * different informations on a same object Each row gives information about one
 * object. These information are displayed in the columns
 */
public abstract class TableModelLinear extends AbstractTableModel {

    /**
	 * the logger that will log any abnormal outputs out of this instance.
	 */
    private Logger logger = Logger.getLogger(getClass());

    private TableModelLinearObject model;

    /**
	 * Creates a new TableModelLinear object.
	 */
    public TableModelLinear() {
        super();
    }

    /**
	 * indicated whether a cell is editable. Always false
	 * 
	 * @param row
	 *            the row of the cell. not used
	 * @param col
	 *            the column of the cell. not used
	 * 
	 * @return false
	 */
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public int getColumnCount() {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(getMethods().size() + " columns");
        }
        return getMethods().size();
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param tmo
	 *            DOCUMENT ME!
	 */
    public void setModel(TableModelLinearObject tmo) {
        model = tmo;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param l
	 *            DOCUMENT ME!
	 */
    public void setObjects(List l) {
        model.setObjects(l);
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public List getObjects() {
        List back = model.getObjects();
        if (getLogger().isDebugEnabled()) {
            getLogger().debug("getting " + back.size() + " objects from " + model.getClass());
        }
        return back;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public int getRowCount() {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(getObjects().size() + " rows");
        }
        return getObjects().size();
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param row
	 *            DOCUMENT ME!
	 * @param col
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public Object getValueAt(int row, int col) {
        Object back = null;
        Object tmp = getObjectAt(row);
        if (null != tmp) {
            if (getLogger().isDebugEnabled()) {
                getLogger().warn("object for row " + row + " is " + tmp.toString());
            }
            back = getValueAt(tmp, col);
        } else {
            getLogger().error("no object for row " + row);
        }
        return back;
    }

    /**
	 * DOCUMENT ME!
	 */
    public void reset() {
        setObjects(null);
        resetModel();
    }

    /**
	 * DOCUMENT ME!
	 */
    public void resetModel() {
        if (model != null) {
            getLogger().info("resetting model");
            model.reset();
        }
    }

    /**
	 * sets the logger of the current instance
	 * 
	 * @param l
	 *            the logger of the current instance
	 */
    protected void setLogger(Logger l) {
        if (l != null) {
            logger = l;
        } else {
            getLogger().warn("tring to set null logger" + this.toString());
        }
    }

    /**
	 * returns the current logger of this instance
	 * 
	 * @return the logger of the current instance
	 */
    protected Logger getLogger() {
        return logger;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param col
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    protected String getMethodAt(int col) {
        return (String) getMethods().get(col);
    }

    /**
	 * returns the object of a specified row
	 * 
	 * @param row
	 *            the index of the row
	 * 
	 * @return the object that is referring to the row
	 */
    protected Object getObjectAt(int row) {
        Object back = null;
        back = getObjects().get(row);
        return back;
    }

    /**
	 * returns the name of the method of the object that is supposed to provide
	 * information for the given column
	 * 
	 * @return the name of the method of the object that is supposed to provide
	 *         information for the given column
	 */
    abstract List getMethods();

    /**
	 * return the value of a specific column
	 * 
	 * @param f
	 *            the row object
	 * @param col
	 *            the index of the column
	 * 
	 * @return the value of the column on the object-row
	 */
    private Object getValueAt(Object f, int col) {
        Object back = null;
        if (getLogger().isDebugEnabled()) {
            getLogger().warn("invoking " + getMethodAt(col) + "  in " + f);
        }
        back = Invoker.invokeMethod(f, getMethodAt(col));
        return back;
    }
}
