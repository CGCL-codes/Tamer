package net.sourceforge.tableview;

import java.util.Collection;

/**
 * TableViewModelEvent is used to notify listeners that a table view model.
 * 
 * @author St�phane Brunner, Last modified by: $Author: stbrunner $
 * @version $Revision: 1.10 $ $Date: 2004/09/05 19:22:16 $. Revision history:
 *          $Log: TableViewModelEvent.java,v $ Revision 1.10 2004/09/05 19:22:16
 *          stbrunner add select by first letter
 * 
 * Revision 1.9 2004/05/31 14:39:14 stbrunner *** empty log message ***
 * 
 * Revision 1.8 2004/02/15 19:29:43 stbrunner *** empty log message ***
 * 
 * Revision 1.7 2003/06/06 06:31:32 stbrunner Change description
 * 
 * @see javas.swing.event.TableModelEvent
 */
public class TableViewModelEvent extends java.util.EventObject {

    private static final long serialVersionUID = 8269008896469380840L;

    /**
   * Identifies the addtion of new rows or columns.
   */
    public static final int INSERT = 1;

    /**
   * Identifies a change to existing data.
   */
    public static final int UPDATE = 0;

    /**
   * Identifies the removal of rows or columns.
   */
    public static final int DELETE = -1;

    /**
   * Identifies the header row.
   */
    public static final int HEADER_ROW = -1;

    /** the type */
    protected int mType;

    /** the row object */
    protected Collection<?> mRowObjects;

    /** the colimn index */
    protected int mColumn;

    /**
   * Construct with source model.
   * 
   * @param pSource
   *          the model
   */
    public TableViewModelEvent(TableViewModel pSource) {
        this(pSource, null, UPDATE);
    }

    /**
   * Construct with source model and row Object.
   * 
   * @param pSource
   *          the model
   * @param pRowObject
   *          the row Object
   */
    public TableViewModelEvent(TableViewModel pSource, Object pRowObject) {
        this(pSource, java.util.Arrays.asList(new Object[] { pRowObject }), UPDATE);
    }

    /**
   * Construct with source model, row Object and type.
   * 
   * @param pSource
   *          the model
   * @param pRowObject
   *          the row Object
   * @param pType
   *          the type (INSERT, DELETE or UPDATE)
   */
    public TableViewModelEvent(TableViewModel pSource, Object pRowObject, int pType) {
        this(pSource, java.util.Arrays.asList(new Object[] { pRowObject }), pType);
    }

    /**
   * Construct with source model and row Objects.
   * 
   * @param pSource
   *          the model
   * @param pRowObjects
   *          the rows Objects
   */
    public TableViewModelEvent(TableViewModel pSource, Collection<?> pRowObjects) {
        this(pSource, pRowObjects, UPDATE);
    }

    /**
   * Construct with source model, rows Objects and type.
   * 
   * @param pSource
   *          the model
   * @param pRowObjects
   *          the rows Objects
   * @param pType
   *          the type (INSERT, DELETE or UPDATE)
   */
    public TableViewModelEvent(TableViewModel pSource, Collection<?> pRowObjects, int pType) {
        super(pSource);
        this.mRowObjects = pRowObjects;
        this.mType = pType;
    }

    /**
   * Returns a collection of row Object.
   * 
   * @return the rows
   */
    public Collection<?> getRowObjects() {
        return mRowObjects;
    }

    /**
   * Returns the type of event - one of: INSERT, UPDATE and DELETE.
   * 
   * @return the type
   */
    public int getType() {
        return mType;
    }
}
