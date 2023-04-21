package org.project.trunks.webform;

import org.project.trunks.xml.*;
import javax.servlet.http.*;
import org.project.trunks.data.*;
import java.util.*;
import org.project.trunks.utilities.*;
import org.project.trunks.connection.*;
import java.math.*;
import org.project.trunks.user.*;
import java.sql.*;
import org.project.trunks.fileupload.*;
import org.project.trunks.exceptions.*;
import org.project.trunks.filter.*;

public class EditList extends StandardList {

    /**
   * Logger
   */
    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(EditList.class);

    protected TRow rowForAdd = null;

    public EditList() {
        super();
    }

    public EditList(XmlObject xo, HttpServletRequest request) {
        super(xo, request);
    }

    public void reInit() {
        this.errorMessage = "";
        this.sortExpression = null;
        rowForAdd = null;
        selectedIndex = -1;
    }

    /**
   * setAllFieldsValue
   * @param request
   * @return
   */
    public boolean setAllFieldsValue(HttpServletRequest request) throws Exception {
        IGenericUserSession userSession = (IGenericUserSession) SessionManager.getInstance().getSession(request);
        RequestManager rm = new RequestManager(request);
        errorMessage = "";
        String pfx = this.ID + ".";
        TRow tr;
        Field field;
        Field baseField;
        String fieldLabel = "";
        int nbRow = tTable.getVRow().size();
        int nbCol = tTable.getBaseRow().getFields().length;
        int firstRowIndex = 0;
        int limit = nbRow;
        if (hasPaging()) {
            firstRowIndex = getPageSize() * getCurrentPageIndex();
            if (firstRowIndex + getPageSize() < nbRow) limit = firstRowIndex + getPageSize();
        }
        for (int i = firstRowIndex; i < limit; i++) {
            tr = (TRow) tTable.getVRow().elementAt(i);
            for (int j = 0; j < tr.getFields().length; j++) {
                field = (FieldDB) tr.getField(j);
                baseField = tTable.getBaseRow().getField(j);
                fieldLabel = baseField.getLabel();
                String fieldID = field.getID() + "@@" + i;
                if (field.isInputField() || rm.getParameter(pfx + fieldID) != null) {
                    try {
                        String value = rm.getParameter(pfx + fieldID);
                        if (field.getKind().equals(FieldKind.SELECT) && field.isAutoComplete() && !field.isForceMatch()) value = rm.getParameter("entry_" + pfx + fieldID); else if (field.getKind().equals(FieldKind.RADIOBUTTON) && !StringUtilities.getNString((String) field.getCustomObjectParams("GROUP_NAME")).equals("")) {
                            String gn = (String) field.getCustomObjectParams("GROUP_NAME");
                            value = rm.getParameter(pfx + gn + "_" + i);
                        } else if (field.getKind().equals(FieldKind.FILE)) {
                            FileItem item = rm.getFileItem(pfx + fieldID);
                            if (item != null) {
                                if (baseField.isKeepCompletePath_file()) value = item.getName(); else value = getFilenameForFileField(item, pfx + fieldID);
                                if (value.equals("")) {
                                    value = StringUtilities.getNString(rm.getParameter("hFile_" + pfx + fieldID));
                                } else {
                                    field.setFileItem(item);
                                    String FILENAME_FIELD = (String) baseField.getCustomObjectParams("FILENAME_FIELD");
                                    if (!StringUtilities.getNString(FILENAME_FIELD).equals("")) {
                                        String lFilename = getFilenameForFileField(item, fieldID);
                                        tr.getField(FILENAME_FIELD).setValue(lFilename);
                                    }
                                }
                            } else value = field.getValue();
                        }
                        boolean hasChanged = field.setFieldValue(value);
                        if (hasChanged && baseField.isGestPageChanged()) tr.setModifiedState();
                    } catch (FormatException e) {
                        log.error("EditList.setAllFieldsValue [" + fieldID + "] - FORMAT_EXCEPTION : '" + e.getMessage() + "'", e);
                        if (!StringUtilities.getNString(errorMessage).equals("")) errorMessage += "\n";
                        errorMessage += "'" + StringUtilities.replaceParametersInExpression(userSession.getTranslation("MSG_LIST_INVALID_FORMAT"), new String[] { fieldLabel, (i + 1) + "", e.getFormat(), e.getValue() }) + "'";
                    } catch (Exception e) {
                        log.error("EditList.setAllFieldsValue [" + fieldID + "] - EXCEPTION : '" + e.getMessage() + "'");
                        if (!StringUtilities.getNString(errorMessage).equals("")) errorMessage += "<br/>";
                        errorMessage += "'" + e.getMessage() + "'";
                    }
                }
            }
        }
        setDetailAllFieldsValue(request);
        return this.getState().equals(TRowState.MODIFIED);
    }

    /**
   * setDetailAllFieldsValue
   * @param request HttpServletRequest
   * @throws java.lang.Exception
   */
    public void setDetailAllFieldsValue(HttpServletRequest request) throws Exception {
        for (int k = 0; k < vDetailsID.size(); k++) {
            String detailID = (String) vDetailsID.elementAt(k);
            Detail detail = (Detail) hsDetails.get(detailID);
            if (detail == null) {
                log.error("<<<< EditList.setAllFieldsValue - detail[" + k + "][" + detailID + "] is null ");
                continue;
            }
            if (detail.isIncluded() && detail.isDetailRW()) detail.setAllFieldsValue(request);
        }
    }

    /**
   * reloadListOfValuesForSELECTField
   */
    protected void reloadListOfValuesForSELECTField() {
        Field basefields[] = this.getTTable().getBaseRow().getFields();
        for (int j = 0; j < basefields.length; j++) {
            FieldDB baseField = ((FieldDB) basefields[j]);
            if (baseField.getKind().equals(FieldKind.SELECT) || baseField.getKind().equals(FieldKind.PICK_UP) || baseField.getKind().equals(FieldKind.MS_PICK_UP)) {
                basefields[j].setVValue(baseField.loadValues());
            }
        }
    }

    /**
   *
   * @param request
   * @throws java.lang.Exception
   */
    public boolean saveInMemory(HttpServletRequest request) throws Exception {
        errorMessage = "";
        return setAllFieldsValue(request);
    }

    /**
   * save
   * @param request
   * @return
   */
    public boolean saveToDB(HttpServletRequest request) throws Exception {
        return saveToDB(request, null);
    }

    /**
   * saveToDB
   * @param request
   * @param conn Utiliser la connexion pass�e en param�tre ! (
   * Correction MFR - 10/09/2008
   * @return
   * @throws java.lang.Exception
   */
    public boolean saveToDB(HttpServletRequest request, Connection conn) throws Exception {
        log.info("EditList.saveToDB");
        boolean givenConnection = true;
        try {
            if (!StringUtilities.getNString(errorMessage).equals("")) {
                log.error("EditList.saveToDB - errorMessage = '" + errorMessage + "'");
                return false;
            }
            if (!getState().equals(TRowState.MODIFIED)) {
                log.info("EditList.saveToDB - getState() <> TRowState.MODIFIED)");
                return false;
            }
            IGenericUserSession userSession = (IGenericUserSession) SessionManager.getInstance().getSession(request);
            String user_code_lang = (userSession == null) ? "" : userSession.getLanguage();
            WebFormDBManager dbm = new WebFormDBManager(this.connect_id, user_code_lang, this.ID, userSession);
            if (conn == null) {
                conn = dbm.getConnection();
                givenConnection = false;
            }
            if (!preSaveToDB(request, conn)) return false;
            int nbRowModified = 0;
            int nbRowAdded = 0;
            for (int i = 0; i < tTable.getVRow().size(); i++) {
                TRow tRow = ((TRow) tTable.getVRow().elementAt(i));
                if (tRow.getRowState().equals(TRowState.NEW)) nbRowAdded++;
                if (tRow.getRowState().equals(TRowState.MODIFIED)) {
                    nbRowModified++;
                    if (!this.preSave(false, tRow, request, conn)) return false;
                }
            }
            boolean rc = true;
            if (nbRowAdded > 0 || nbRowModified > 0) {
                saveValuesOfTable(dbm, conn);
            }
            saveDetailsToDB(conn);
            postSaveToDB(request, conn);
            if (!givenConnection && conn != null) conn.commit();
            return rc;
        } catch (Throwable e) {
            log.error("EditList.saveToDB - EXCEPTION : '" + e.getMessage() + "'");
            errorMessage = e.getMessage();
            if (!givenConnection && conn != null) conn.rollback();
            return false;
        } finally {
            if (!givenConnection && conn != null) conn.close();
        }
    }

    /**
   * saveValuesOfTable
   * @throws java.lang.Throwable
   */
    public boolean saveValuesOfTable(WebFormDBManager dbm, Connection conn) throws Throwable {
        return dbm.saveValuesOfTable(this, null, null, conn, false);
    }

    /**
   * preSaveToDB
   * @param request
   * @param conn
   * @return
   * @throws java.lang.Exception
   */
    public boolean preSaveToDB(HttpServletRequest request, Connection conn) throws Exception {
        return true;
    }

    /**
   * postSaveToDB
   * @param request
   * @param conn
   * @throws java.lang.Exception
   */
    public void postSaveToDB(HttpServletRequest request, Connection conn) throws Exception {
        if (bReloadAfterSave) {
            this.fillTable((sortExpression != null), conn);
        } else {
            for (int i = 0; i < tTable.getVRow().size(); i++) {
                TRow tRow = ((TRow) tTable.getVRow().elementAt(i));
                tRow.setRowState(TRowState.UNCHANGED);
            }
        }
    }

    /**
   * saveDetailsToDB
   * @throws java.lang.Exception
   */
    public void saveDetailsToDB() throws Exception {
        saveDetailsToDB(null);
    }

    public void saveDetailsToDB(Connection conn) throws Exception {
        log.info("<<<<< EditList.saveDetailsToDB...");
        TRow referenceRow = null;
        if (this.selectedIndex == -1) referenceRow = this.getBaseRow(); else referenceRow = this.getSelectedTRow();
        saveDetailsToDB(referenceRow, conn);
    }

    /**
   * save
   * @param request
   * @return
   */
    public boolean save(HttpServletRequest request) throws Exception {
        log.info("EditList.save");
        saveInMemory(request);
        if (!StringUtilities.getNString(errorMessage).equals("")) return false;
        return saveToDB(request);
    }

    /**
   * fillRowForAddFromRequest
   * @return nbRowToAdd
   */
    protected int fillRowForAddFromRequest(HttpServletRequest request) throws Exception {
        RequestManager rm = new RequestManager(request);
        String pfx = this.ID + ".";
        String table = getBaseTable();
        int nbRowToAdd = 1;
        for (int j = 0; j < rowForAdd.getFields().length; j++) {
            FieldDB baseField = (FieldDB) tTable.getBaseRow().getField(j);
            Field field = (FieldDB) rowForAdd.getField(j);
            if (field.isInputField() || field.isInsertable()) {
                field.setValues(new Vector());
                String fieldID = field.getID() + "@@" + "ADD";
                if (StringUtilities.getNString(baseField.getMultiple()).equalsIgnoreCase("TRUE") || StringUtilities.getNString(baseField.getMultiple()).equalsIgnoreCase("CUSTOM")) {
                    field.setValues(rm.getParameterValues(pfx + fieldID));
                    if (field.getValues().size() > 0) {
                        nbRowToAdd = field.getValues().size();
                        field.setValue((String) field.getValues().elementAt(0));
                    }
                } else {
                    if (field.getKind().equals(FieldKind.FILE)) {
                        log.debug("<<<<< EditList.fillRowForAddFromRequest - traitement du champ '" + fieldID + "' : FILE...");
                        FileItem item = rm.getFileItem(pfx + fieldID);
                        field.setFileItem(item);
                        String FILENAME_FIELD = (String) baseField.getCustomObjectParams("FILENAME_FIELD");
                        if (!StringUtilities.getNString(FILENAME_FIELD).equals("")) {
                            String lFilename = getFilenameForFileField(item, fieldID);
                            rowForAdd.getField(FILENAME_FIELD).setValue(lFilename);
                        }
                    } else {
                        String value = null;
                        rm.getParameter(pfx + fieldID);
                        if (field.getKind().equals(FieldKind.RADIOBUTTON) && !StringUtilities.getNString((String) field.getCustomObjectParams("GROUP_NAME")).equals("")) {
                            String gn = (String) field.getCustomObjectParams("GROUP_NAME");
                            value = rm.getParameter(pfx + gn + "__new0");
                        } else value = rm.getParameter(pfx + fieldID);
                        boolean hasChanged = field.setFieldValue(value);
                    }
                }
            }
            if (baseField.isForeignKey()) field.setValue(baseField.getValue());
        }
        return nbRowToAdd;
    }

    /**
   * Add one or several rows
   * @param request HttpServletRequest
   * @return vPkOfRowsAdded
   * @todo !!! si 2 champs sont select-Multiple
   */
    public Vector add(HttpServletRequest request) throws Exception {
        return add(request, false);
    }

    /**
   * Add one or several rows
   * @param request HttpServletRequest
   * @param rowAddValuesAlreadyFilled Values of row to add have been already filled
   * @return vPkOfRowsAdded
   * @todo !!! si 2 champs sont select-Multiple
   */
    public Vector add(HttpServletRequest request, boolean rowAddValuesAlreadyFilled) throws Exception {
        log.info("EditList.add");
        Vector vPkOfRowsAdded = new Vector();
        errorMessage = "";
        Connection conn = null;
        IGenericUserSession userSession = (IGenericUserSession) SessionManager.getInstance().getSession(request);
        String user_code_lang = (userSession == null) ? "" : userSession.getLanguage();
        WebFormDBManager dbm = new WebFormDBManager(this.connect_id, user_code_lang, this.ID, userSession);
        try {
            RequestManager rm = new RequestManager(request);
            String pfx = this.ID + ".";
            int nbRowToAdd = 1;
            if (!rowAddValuesAlreadyFilled) nbRowToAdd = fillRowForAddFromRequest(request);
            conn = dbm.getConnection();
            if (nbRowToAdd > 1) {
                for (int i = 0; i < nbRowToAdd; i++) {
                    for (int j = 0; j < rowForAdd.getFields().length; j++) {
                        Field field = (FieldDB) rowForAdd.getField(j);
                        if (field.getValues() != null && field.getValues().size() > 0) field.setValue((String) field.getValues().elementAt(i));
                        if (!StringUtilities.getNString(field.getValue()).equals("") && StringUtilities.getNString(rm.getParameter("cbIsSeq_" + pfx + field.getID() + "@@ADD")).equals("1")) {
                            BigDecimal bd = new BigDecimal(field.getValue());
                            if (i > 0) bd = bd.add(new BigDecimal(1));
                            field.setValue(bd.toString());
                        }
                    }
                    if (!this.preSave(true, rowForAdd, request, conn)) return null;
                    dbm.insertRow(this, rowForAdd, conn);
                    vPkOfRowsAdded.addElement(Field.cloneVector(rowForAdd.getPkFields()));
                    postSave(true, rowForAdd, request, conn);
                }
            } else {
                if (!this.preSave(true, rowForAdd, request, conn)) return null;
                dbm.insertRow(this, rowForAdd, conn);
                vPkOfRowsAdded.addElement(Field.cloneVector(rowForAdd.getPkFields()));
                postSave(true, rowForAdd, request, conn);
            }
            dbm.commit(conn);
            rowForAdd.clear();
            rowForAdd = null;
            return vPkOfRowsAdded;
        } catch (Exception e) {
            log.error("EditList.add - EXCEPTION : '" + e.getMessage() + "'", e);
            errorMessage = " " + e.getMessage() + " ";
            dbm.rollback(conn);
            return null;
        } finally {
            dbm.closeConnection(conn);
        }
    }

    /**
   * addMemory
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws Exception
   */
    public void addMemory(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            TRow r = getRowForAdd();
            r.setRowState(TRowState.NEW);
            this.getTTable().addRow(r);
            this.rowForAdd = null;
        } catch (Throwable e) {
            log.error("<<<< EditList.addMemory - EXCEPTION : '" + e.getMessage() + "'", e);
            this.errorMessage = e.getMessage() + " ";
        }
    }

    /**
   * preSave
   * @param bFromAdd boolean
   * @param request HttpServletRequest
   * @param conn Connection
   * @return boolean
   * @throws java.lang.Exception
   */
    public boolean preSave(boolean bFromAdd, TRow tRow, HttpServletRequest request, Connection conn) throws Exception {
        return true;
    }

    /**
   * postSave
   * @param request HttpServletRequest
   * @param conn Connection
   * @throws java.lang.Exception
   */
    public void postSave(boolean bFromAdd, TRow tRow, HttpServletRequest request, Connection conn) throws Exception {
    }

    /**
   * preDelete
   * @param request HttpServletRequest
   * @throws java.lang.Exception
   */
    public void preDelete(HttpServletRequest request, Connection conn) throws Exception {
    }

    /**
   * postDelete
   * @param request HttpServletRequest
   * @throws java.lang.Exception
   */
    public void postDelete(HttpServletRequest request, Connection conn) throws Exception {
    }

    /**
   * delete
   * @param request
   * @return
   */
    public int delete(HttpServletRequest request) throws Exception {
        log.info("EditList.delete");
        errorMessage = "";
        IGenericUserSession userSession = (IGenericUserSession) SessionManager.getInstance().getSession(request);
        String user_code_lang = (userSession == null) ? "" : userSession.getLanguage();
        WebFormDBManager dbm = new WebFormDBManager(this.connect_id, user_code_lang, this.ID, userSession);
        Connection conn = null;
        try {
            RequestManager rm = new RequestManager(request);
            int nrRow = Integer.parseInt(rm.getParameter("NR_ROW"));
            log.info("EditList.delete - nrRow = " + nrRow);
            TRow tr = (TRow) tTable.getVRow().elementAt(nrRow);
            log.info("EditList.delete - " + tr.toString());
            conn = dbm.getConnection();
            preDelete(request, conn);
            Field[] arrayPkFields = null;
            Vector v = tr.getPkFields();
            boolean existsNullPK = false;
            if (v != null) {
                arrayPkFields = new Field[v.size()];
                for (int i = 0; i < v.size(); i++) {
                    arrayPkFields[i] = (Field) v.elementAt(i);
                    if (arrayPkFields[i].getValue().equals("")) existsNullPK = true;
                }
            }
            deleteDetails(request, conn, arrayPkFields);
            int nb = 1;
            if (existsNullPK || tr.getRowState().equals(TRowState.NEW)) {
                log.info("EditList.delete - Pas de suppression en DB car il s'agit d'une ligne temporaire");
            } else {
                nb = executeDelete(dbm, tr, conn);
                log.info("EditList.delete - res = " + nb);
            }
            postDelete(request, conn);
            if (nb == 0) {
                loadRowFromDB(tr, conn);
            }
            dbm.commit(conn);
            if (nb != 0) {
                tTable.getVRow().remove(nrRow);
                reloadListOfValuesForSELECTField();
            }
            return 1;
        } catch (Throwable e) {
            dbm.rollback(conn);
            log.error("EditList.delete - EXCEPTION : '" + e.getMessage() + "'");
            errorMessage = e.getMessage();
            return 0;
        } finally {
            dbm.closeConnection(conn);
        }
    }

    /**
   * executeDelete
   * @param dbm WebFormDBManager
   * @param tr TRow
   * @param conn Connection
   * @throws java.lang.Throwable
   */
    public int executeDelete(WebFormDBManager dbm, TRow tr, Connection conn) throws Throwable {
        String table = getBaseTable();
        return dbm.executeDelete(table, tr.getPkFields(), null, null, conn);
    }

    /**
   * getRowForAdd
   * @return TRow
   */
    public TRow getRowForAdd() throws Exception {
        if (!this.hasAddFooter() && !this.hasAddPopup() && !this.hasAddMemory() && StringUtilities.getNString(this.getIdField_forForeign_add()).equals("")) return null;
        if (rowForAdd == null) {
            Field[] baseFields = tTable.getBaseRow().getFields();
            int nbCol = baseFields.length;
            Field[] fields = new Field[nbCol];
            for (int i = 0; i < nbCol; i++) {
                fields[i] = Field.clone(baseFields[i]);
                fields[i].setVValue(baseFields[i].getVValue());
                fields[i].setValue(baseFields[i].getDefaultValue());
            }
            rowForAdd = new TRow("-1", fields);
        }
        return rowForAdd;
    }

    /**
   * getListObject
   * @return EditList
   */
    public StandardList getListObject() {
        return new EditList();
    }

    /**
   * makeCopy
   * @return EditList
   */
    public StandardList makeCopy() {
        return makeCopy(getListObject());
    }
}
