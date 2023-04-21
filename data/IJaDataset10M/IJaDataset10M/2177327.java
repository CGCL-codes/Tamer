package ggc.cgms.data.db;

import ggc.cgms.data.CGMSValuesEntry;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.db.hibernate.cgms.CGMSDataH;
import ggc.core.util.DataAccess;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;

public class CGMSData extends CGMSDataH implements BackupRestoreObject, DatabaseObjectHibernate {

    private static final long serialVersionUID = 8478138731218069624L;

    private boolean selected = false;

    I18nControlAbstract ic = null;

    /**
     * Constructor
     */
    public CGMSData() {
    }

    /**
     * Constructor
     * 
     * @param ch
     */
    public CGMSData(CGMSDataH ch) {
        this.setId(ch.getId());
        this.setDt_info(ch.getDt_info());
        this.setBase_type(ch.getBase_type());
        this.setSub_type(ch.getSub_type());
        this.setValue(ch.getValue());
        this.setExtended(ch.getExtended());
        this.setPerson_id(ch.getPerson_id());
        this.setComment(ch.getComment());
        this.setChanged(ch.getChanged());
    }

    /**
     * Constructor
     * 
     * @param pve
     */
    public CGMSData(CGMSValuesEntry pve) {
        this.setId(0L);
        this.setDt_info(pve.getDateTime());
        this.setValue(pve.getValue());
        this.setExtended("");
        this.setPerson_id((int) DataAccessCGMS.getInstance().getCurrentUserId());
        this.setChanged(System.currentTimeMillis());
    }

    /**
     * Constructor
     * 
     * @param _ic
     */
    public CGMSData(I18nControlAbstract _ic) {
        this.ic = _ic;
    }

    /**
     * Get Target Name
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreBase#getTargetName()
     */
    public String getTargetName() {
        return DataAccessCGMS.getInstance().getI18nControlInstance().getMessage("CGMS_DATA");
    }

    /** 
     * Get Name
     * @return 
     */
    public String getName() {
        return this.getTargetName();
    }

    /**
     * getBackupClassName - name of class which will be updated/restored
     * 
     * @return
     */
    public String getClassName() {
        return "ggc.core.db.hibernate.cgms.CGMSDataH";
    }

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.getTargetName();
    }

    /** 
     * getChildren
     */
    public ArrayList<CheckBoxTreeNodeInterface> getNodeChildren() {
        return null;
    }

    /** 
     * isSelected
     */
    public boolean isSelected() {
        return selected;
    }

    /** 
     * setSelected
     */
    public void setSelected(boolean newValue) {
        this.selected = newValue;
    }

    /**
     * Is Object Collection
     * 
     * @return true if it has children
     */
    public boolean isCollection() {
        return false;
    }

    /**
     * Has Children
     */
    public boolean hasNodeChildren() {
        return false;
    }

    /**
     * DbAdd - Add this object to database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return id in type of String
     */
    public String DbAdd(Session sess) throws Exception {
        Transaction tx = sess.beginTransaction();
        CGMSDataH ch = new CGMSDataH();
        ch.setId(this.getId());
        ch.setDt_info(this.getDt_info());
        ch.setBase_type(this.getBase_type());
        ch.setSub_type(this.getSub_type());
        ch.setValue(this.getValue());
        ch.setExtended(this.getExtended());
        ch.setPerson_id(this.getPerson_id());
        ch.setComment(this.getComment());
        ch.setChanged(System.currentTimeMillis());
        Long id = (Long) sess.save(ch);
        tx.commit();
        ch.setId(id.longValue());
        return "" + id.longValue();
    }

    /**
     * DbDelete - Delete this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbDelete(Session sess) throws Exception {
        Transaction tx = sess.beginTransaction();
        CGMSDataH ch = (CGMSDataH) sess.get(CGMSDataH.class, new Long(this.getId()));
        sess.delete(ch);
        tx.commit();
        return true;
    }

    /**
     * DbEdit - Edit this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbEdit(Session sess) throws Exception {
        Transaction tx = sess.beginTransaction();
        CGMSDataH ch = (CGMSDataH) sess.get(CGMSDataH.class, new Long(this.getId()));
        ch.setId(this.getId());
        ch.setDt_info(this.getDt_info());
        ch.setBase_type(this.getBase_type());
        ch.setSub_type(this.getSub_type());
        ch.setValue(this.getValue());
        ch.setExtended(this.getExtended());
        ch.setPerson_id(this.getPerson_id());
        ch.setComment(this.getComment());
        ch.setChanged(System.currentTimeMillis());
        sess.update(ch);
        tx.commit();
        return true;
    }

    /**
     * DbGet - Loads this object. Id must be set.
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbGet(Session sess) throws Exception {
        CGMSDataH ch = (CGMSDataH) sess.get(CGMSDataH.class, new Long(this.getId()));
        this.setId(ch.getId());
        this.setDt_info(ch.getDt_info());
        this.setBase_type(ch.getBase_type());
        this.setSub_type(ch.getSub_type());
        this.setValue(ch.getValue());
        this.setExtended(ch.getExtended());
        this.setPerson_id(ch.getPerson_id());
        this.setComment(ch.getComment());
        this.setChanged(ch.getChanged());
        return true;
    }

    /**
     * DbHasChildren - Shows if this entry has any children object, this is needed for delete
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbHasChildren(Session sess) throws Exception {
        return false;
    }

    /** 
     * getAction
     */
    public int getAction() {
        return 0;
    }

    /**
     * Table Version
     */
    public int TABLE_VERSION = 1;

    /**
     * Get Table Version - returns version of table
     * 
     * @return version information
     */
    public int getTableVersion() {
        return this.TABLE_VERSION;
    }

    /**
     * dbExport - returns export String, for current version 
     *
     * @return line that will be exported
     * @throws Exception if export for table is not supported
     */
    public String dbExport(int table_version) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getId());
        sb.append("|");
        sb.append(this.getDt_info());
        sb.append("|");
        sb.append(this.getBase_type());
        sb.append("|");
        sb.append(this.getSub_type());
        sb.append("|");
        sb.append(this.getValue());
        sb.append("|");
        sb.append(this.getExtended());
        sb.append("|");
        sb.append(this.getPerson_id());
        sb.append("|");
        sb.append(this.getComment());
        sb.append("|");
        sb.append(this.getChanged());
        sb.append("\n");
        return sb.toString();
    }

    /**
     * dbExport - returns export String, for current version 
     *
     * @return line that will be exported
     * @throws Exception if export for table is not supported
     */
    public String dbExport() throws Exception {
        return dbExport(this.TABLE_VERSION);
    }

    /**
     * dbExportHeader - header for export file
     * 
     * @param table_version
     * @return
     */
    public String dbExportHeader(int table_version) {
        return "; Columns: id|dt_info|base_type|sub_type|value|extended|person_id|comment|changed\n" + "; Table version: " + getTableVersion() + "\n";
    }

    /**
     * dbExportHeader - header for export file
     * 
     * @return
     */
    public String dbExportHeader() {
        return this.dbExportHeader(this.TABLE_VERSION);
    }

    /**
     * dbImport - processes input entry to right fields
     * 
     * @param table_version version of table
     * @param value_entry whole import line
     * @throws Exception if import for selected table version is not supported or it fails
     */
    public void dbImport(int table_version, String value_entry) throws Exception {
        dbImport(table_version, value_entry, null);
    }

    /**
     * dbImport - processes input entry to right fields
     * 
     * @param table_version version of table
     * @param value_entry whole import line
     * @param parameters parameters
     * @throws Exception if import for selected table version is not supported or it fails
     */
    public void dbImport(int table_version, String value_entry, Object[] parameters) throws Exception {
        DataAccess da = DataAccess.getInstance();
        value_entry = DataAccess.getInstance().replaceExpression(value_entry, "||", "| |");
        String[] arr = da.splitString(value_entry, "|");
        this.setId(da.getLongValueFromString(arr[0]));
        this.setDt_info(da.getLongValueFromString(arr[1]));
        this.setBase_type(da.getIntValueFromString(arr[2]));
        this.setSub_type(da.getIntValueFromString(arr[3]));
        this.setValue(arr[4]);
        this.setExtended(arr[5]);
        this.setPerson_id(da.getIntValueFromString(arr[6]));
        this.setComment(arr[7]);
        this.setChanged(da.getLongValueFromString(arr[8]));
    }

    /**
     * getBackupFile - name of backup file (base part)
     * 
     * @return
     */
    public String getBackupFile() {
        return "CGMSDataH";
    }

    /**
     * getBackupClassName - name of class which will be updated/restored
     * 
     * @return
     */
    public String getBackupClassName() {
        return "ggc.core.db.hibernate.cgms.CGMSDataH";
    }

    /** 
     * getObjectName
     */
    public String getObjectName() {
        return "CGMSData";
    }

    /** 
     * isDebugMode
     */
    public boolean isDebugMode() {
        return false;
    }

    /**
     * getObjectUniqueId - get id of object
     * @return unique object id
     */
    public String getObjectUniqueId() {
        return "";
    }

    /**
     * Has To Be Clean - if table needs to be cleaned before import
     * 
     * @return true if we need to clean
     */
    public boolean hasToBeCleaned() {
        return false;
    }
}
