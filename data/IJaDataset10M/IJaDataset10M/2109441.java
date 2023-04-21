package com.sri.emo.dbobj;

import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.db.DBConnection;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.DBField;
import com.jcorporate.expresso.core.dbobj.RowSecuredDBObject;
import com.jcorporate.expresso.core.security.ReadOnlyUser;

/**
 * a row in a scoring or design matrix or covar matrix. The first two matrices
 * are for Measurement Model. Covar matrix was added later in Student Model.  first
 * two matrices have rows corresponding to ObservableVar, while covar matrix
 * has both row & column for SMVs.  Covar usage was added last, so some
 * methods calling for Obs.Var. are repurposed to "RowId".
 *
 * @author larry hamel
 */
public class MatrixCell extends RowSecuredDBObject {

    /**
     * id of attribute linking matrix rows to measurement model attributes for matrices
     * i.e., this attribute is stored as part of the measurement model node,
     * and references information through this MatrixCell
     */
    public static final String ATTRIB_ID = Attribute.ATTRIBUTE_ID;

    /**
     * row id, which is the attribute ID for the observable variable category
     * or, for covar matrix, for SMV id
     */
    public static final String ROW_ID = "OV_CAT_ID";

    /**
     * the column id
     * is null for continuous SMVs,
     * is SMV Category ID for discrete SMVs for a scoring matrix
     * is the id of the observable variables for a design matrix
     */
    public static final String COL_ID = "COL_ID";

    public static final String VALUE = "VALUE";

    public static final String MATRIX_DESCRIPTION = "MatrixCell";

    /**
     * @throws DBException If the new object cannot be created
     */
    public MatrixCell() throws DBException {
    }

    /**
     * constructor for new cell
     *
     * @param attrib   existing attribute for matrix, or null if new
     * @param columnId is either at the node ID for the student model variable (scoring matrix) or the attribute ID for the category (design matrix)
     * @throws DBException If the new object cannot be created
     */
    public MatrixCell(final Attribute attrib, final String columnId, final int rowId, final String value) throws DBException {
        if (attrib != null) {
            setField(MatrixCell.ATTRIB_ID, attrib.getAttribId());
        }
        setField(ROW_ID, rowId);
        setField(COL_ID, columnId);
        setField(VALUE, value);
    }

    public MatrixCell(final String attribId) throws DBException {
        setField(MatrixCell.ATTRIB_ID, attribId);
    }

    /**
     * Constructor
     *
     * @param theConnection DBConnection to be used to
     *                      communicate with the database
     * @param theUser       User name attempting to access the
     *                      object
     * @throws DBException If the user cannot access this
     *                     object or the object cannot be initialized
     */
    public MatrixCell(final DBConnection theConnection, final int theUser) throws DBException {
        super(theConnection, theUser);
    }

    /**
     * Constructor for security setup.
     *
     * @param userSecurity ReadOnlyUser security context.
     * @throws DBException upon construction error.
     */
    public MatrixCell(final ReadOnlyUser userSecurity) throws DBException {
        super(userSecurity);
    }

    /**
     * Defines the database table name and fields for this DB object
     *
     * @throws DBException if the operation cannot be performed
     */
    protected synchronized void setupFields() throws DBException {
        setTargetTable("matrixCell");
        setDescription(MATRIX_DESCRIPTION);
        addField(ATTRIB_ID, DBField.INTEGER_TYPE, 0, false, "foreign key to attribute table");
        addField(ROW_ID, DBField.INTEGER_TYPE, 0, false, "row ID");
        addField(COL_ID, DBField.VARCHAR_TYPE, 254, false, "column ID: variable definition field: SMV node ID for scoring, OV for design");
        addField(VALUE, DBField.VARCHAR_TYPE, 254, false, "value");
        addKey(ATTRIB_ID);
        addKey(ROW_ID);
        addKey(COL_ID);
        addIndex("attrib_idx", ATTRIB_ID, false);
    }

    /**
     * @return this cells attrib number which specifies its membership in a matrix (same attrib id for all cells)
     */
    public String getAttribId() throws DBException {
        return getField(ATTRIB_ID);
    }

    public String getValue() throws DBException {
        return getField(VALUE);
    }

    public String getOVCatId() throws DBException {
        return getField(ROW_ID);
    }

    /**
     * @return ID for student model variable, which is stored in the "COL_ID" field for scoring matrices
     */
    public String getSMVId() throws DBException {
        return getField(COL_ID);
    }

    /**
     * will throw if SMV not found
     */
    public Node getSMV(ControllerRequest request) throws DBException {
        Node smv = new Node(request, getSMVId());
        smv.retrieve();
        return smv;
    }

    /**
     * will throw if SMV not found
     */
    public Node getSMV() throws DBException {
        Node smv = new Node(getSMVId());
        smv.retrieve();
        return smv;
    }

    /**
     * will throw if OV Category not found
     */
    public Attribute getOVCat() throws DBException {
        Attribute ovCat = new Attribute(getOVCatId());
        ovCat.retrieve();
        return ovCat;
    }

    /**
     * @return column id for this cell.  for a scoring matrix, this is equivalent to getSMVId(); for a design matrix, this is a OVCat ID, but not to be confused with the other dimension
     */
    public String getColumnId() throws DBException {
        return getField(COL_ID);
    }

    /**
     * @return row id.  must be an integer.
     */
    public String getRowId() throws DBException {
        return getField(ROW_ID);
    }

    public Attribute getAttrib(final ControllerRequest request) throws DBException {
        Attribute attrib = new Attribute(getAttribId());
        attrib.retrieve();
        return attrib;
    }

    public void setValue(final String value) throws DBException {
        setField(VALUE, value);
    }

    public void setColId(final String colId) throws DBException {
        setField(COL_ID, colId);
    }

    public void setRowId(final String rowId) throws DBException {
        setField(ROW_ID, rowId);
    }

    public void setRowId(final int rowId) throws DBException {
        setField(ROW_ID, rowId);
    }

    public void setAttribID(final String attribId) throws DBException {
        setField(ATTRIB_ID, attribId);
    }
}
