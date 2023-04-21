package com.commander4j.sys;

import com.commander4j.db.JDBTable;
import com.commander4j.util.JUtility;

/**
 */
public class JLaunchLookup {

    public String dlgKeyField;

    public String dlgCriteriaField;

    public String dlgOrderField;

    /**
	 * Field dlgCriteriaDefault. Value: {@value dlgCriteriaDefault}
	 */
    public static String dlgCriteriaDefault = "";

    /**
	 * Field dlgAutoExec. Value: {@value dlgAutoExec}
	 */
    public static boolean dlgAutoExec = false;

    /**
	 * Field dlgResult. Value: {@value dlgResult}
	 */
    public static String dlgResult = "";

    public JLaunchLookup() {
        dlgKeyField = "";
        dlgCriteriaField = "";
        dlgOrderField = "";
    }

    /**
	 * Constructor for JLaunchLookup.
	 * 
	 * @param key
	 *            String
	 * @param criteria
	 *            String
	 * @param order
	 *            String
	 */
    public JLaunchLookup(String key, String criteria, String order) {
        dlgKeyField = key;
        dlgCriteriaField = criteria;
        dlgOrderField = order;
    }

    /**
	 * Method processOrders.
	 * 
	 * @return boolean
	 */
    public static boolean processOrders() {
        String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
        JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_PROCESS_ORDER"));
        JDialogLookup.dlg_title = "Process Orders";
        JDialogLookup.dlg_key_field_name = "process_order";
        JDialogLookup.dlg_criteria_field_name_default = "status";
        JDialogLookup.dlg_orderBy_name_default = "due_date";
        JDialogLookup.dlg_sort_descending = true;
        dlgAutoExec = true;
        JDialogLookup inst = new JDialogLookup(Common.mainForm);
        inst.setVisible(true);
        dlgResult = JDialogLookup.dlg_selected_var.trim();
        return JDialogLookup.dlg_selected;
    }

    public static boolean users() {
        String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
        JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}SYS_USERS"));
        JDialogLookup.dlg_title = "Users";
        JDialogLookup.dlg_key_field_name = "user_id";
        JDialogLookup.dlg_criteria_field_name_default = "USER_COMMENT";
        JDialogLookup.dlg_orderBy_name_default = "USER_COMMENT";
        JDialogLookup.dlg_sort_descending = false;
        dlgAutoExec = true;
        JDialogLookup inst = new JDialogLookup(Common.mainForm);
        inst.setVisible(true);
        dlgResult = JDialogLookup.dlg_selected_var.trim();
        return JDialogLookup.dlg_selected;
    }

    /**
	 * Method locations.
	 * 
	 * @return boolean
	 */
    public static boolean locations() {
        String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
        JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_LOCATION"));
        JDialogLookup.dlg_title = "Locations";
        JDialogLookup.dlg_key_field_name = "location_id";
        JDialogLookup.dlg_criteria_field_name_default = "DESCRIPTION";
        JDialogLookup.dlg_orderBy_name_default = "LOCATION_ID";
        JDialogLookup.dlg_sort_descending = false;
        dlgAutoExec = true;
        JDialogLookup inst = new JDialogLookup(Common.mainForm);
        inst.setVisible(true);
        dlgResult = JDialogLookup.dlg_selected_var.trim();
        return JDialogLookup.dlg_selected;
    }

    public static boolean reasons() {
        String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
        JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_MHN_REASONS"));
        JDialogLookup.dlg_title = "Reasons";
        JDialogLookup.dlg_key_field_name = "reason";
        JDialogLookup.dlg_criteria_field_name_default = "DESCRIPTION";
        JDialogLookup.dlg_orderBy_name_default = "REASON";
        JDialogLookup.dlg_sort_descending = false;
        dlgAutoExec = true;
        JDialogLookup inst = new JDialogLookup(Common.mainForm);
        inst.setVisible(true);
        dlgResult = JDialogLookup.dlg_selected_var.trim();
        return JDialogLookup.dlg_selected;
    }

    public static boolean customers() {
        String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
        JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_CUSTOMER"));
        JDialogLookup.dlg_title = "Customers";
        JDialogLookup.dlg_key_field_name = "customer_id";
        JDialogLookup.dlg_criteria_field_name_default = "customer_id";
        JDialogLookup.dlg_orderBy_name_default = "customer_name";
        JDialogLookup.dlg_sort_descending = false;
        dlgAutoExec = true;
        JDialogLookup inst = new JDialogLookup(Common.mainForm);
        inst.setVisible(true);
        dlgResult = JDialogLookup.dlg_selected_var.trim();
        return JDialogLookup.dlg_selected;
    }

    /**
	 * Method materials.
	 * 
	 * @return boolean
	 */
    public static boolean materials() {
        String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
        JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_MATERIAL"));
        JDialogLookup.dlg_title = "Materials";
        JDialogLookup.dlg_key_field_name = "material";
        JDialogLookup.dlg_criteria_field_name_default = "description";
        JDialogLookup.dlg_orderBy_name_default = "material_type";
        dlgAutoExec = true;
        JDialogLookup inst = new JDialogLookup(Common.mainForm);
        inst.setVisible(true);
        dlgResult = JDialogLookup.dlg_selected_var.trim();
        return JDialogLookup.dlg_selected;
    }

    /**
	 * Method materialBatches.
	 * 
	 * @return boolean
	 */
    public static boolean materialBatches() {
        String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
        JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_MATERIAL_BATCH"));
        JDialogLookup.dlg_title = "Material Batches";
        JDialogLookup.dlg_key_field_name = "batch_number";
        JDialogLookup.dlg_criteria_field_name_default = "material";
        JDialogLookup.dlg_orderBy_name_default = "expiry_date";
        JDialogLookup.dlg_sort_descending = true;
        JDialogLookup inst = new JDialogLookup(Common.mainForm);
        inst.setVisible(true);
        dlgResult = JDialogLookup.dlg_selected_var.trim();
        return JDialogLookup.dlg_selected;
    }

    /**
	 * Method pallets.
	 * 
	 * @return boolean
	 */
    public static boolean pallets() {
        String schemaName = Common.hostList.getHost(Common.selectedHostID).getDatabaseParameters().getjdbcDatabaseSchema();
        JDialogLookup.dlg_table = new JDBTable(Common.selectedHostID, Common.sessionID, JUtility.substSchemaName(schemaName, "{schema}APP_PALLET"));
        JDialogLookup.dlg_title = "Pallets";
        JDialogLookup.dlg_key_field_name = "sscc";
        JDialogLookup.dlg_criteria_field_name_default = "MATERIAL";
        JDialogLookup.dlg_orderBy_name_default = "DATE_OF_MANUFACTURE";
        JDialogLookup.dlg_sort_descending = true;
        JDialogLookup inst = new JDialogLookup(Common.mainForm);
        inst.setVisible(true);
        dlgResult = JDialogLookup.dlg_selected_var.trim();
        return JDialogLookup.dlg_selected;
    }
}
