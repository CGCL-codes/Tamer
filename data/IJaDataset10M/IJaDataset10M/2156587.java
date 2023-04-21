package org.openXpertya.plugin.install;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.openXpertya.model.MChangeLog;
import org.openXpertya.model.MComponent;
import org.openXpertya.model.MComponentVersion;
import org.openXpertya.model.M_Column;
import org.openXpertya.model.PO;
import org.openXpertya.plugin.install.PluginXMLUpdater.ChangeGroup;
import org.openXpertya.util.DB;
import org.openXpertya.util.Env;
import org.openXpertya.util.Util;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.tools.codec.Base64Encoder;

public abstract class ChangeLogXMLBuilder extends PluginXMLBuilder {

    /** Versión del componente */
    private MComponentVersion componentVersion;

    /** Componente */
    private MComponent component;

    /** Columnas de referencia ignoradas */
    private List<String> ignoresReferenceColumns;

    /** AD_ChangeLog_ID inicial */
    private Integer changeLogIDFrom = null;

    /** AD_ChangeLog_ID fin */
    private Integer changeLogIDTo = null;

    /** Usuario registrado en registros del changelog */
    private Integer userID = null;

    /** Last Changelog (ultimo changelog) */
    protected int lastChangelogID = -1;

    public ChangeLogXMLBuilder(int replicationArrayPos, String trxName) {
        super(replicationArrayPos, trxName);
        List<String> ignoreColumns = new ArrayList<String>();
        setIgnoresReferenceColumns(ignoreColumns);
    }

    public ChangeLogXMLBuilder(String path, String fileName, Integer componentVersionID, String trxName) {
        super(path, fileName, componentVersionID, trxName);
        initComponentVersion(componentVersionID);
        initIgnoreColumns();
    }

    public ChangeLogXMLBuilder(String path, String fileName, Integer componentVersionID, Integer changeLogIDFrom, Integer changeLogIDTo, Integer userID, String trxName) {
        this(path, fileName, componentVersionID, trxName);
        setChangeLogIDFrom(changeLogIDFrom);
        setChangeLogIDTo(changeLogIDTo);
        setUserID(userID);
    }

    private void initComponentVersion(Integer componentVersionID) {
        setComponentVersion(new MComponentVersion(Env.getCtx(), componentVersionID, trxName));
        setComponent(new MComponent(Env.getCtx(), getComponentVersion().getAD_Component_ID(), trxName));
    }

    private void initIgnoreColumns() {
        List<String> ignoreColumns = new ArrayList<String>();
        ignoreColumns.add("AD_Client_ID");
        ignoreColumns.add("AD_Org_ID");
        ignoreColumns.add("AD_User_ID");
        ignoreColumns.add("CreatedBy");
        ignoreColumns.add("UpdatedBy");
        setIgnoresReferenceColumns(ignoreColumns);
    }

    @Override
    protected String getRootNodeName() {
        return "changelog";
    }

    @Override
    protected void fillDocument() throws Exception {
        ChangeLogGroupList groupList = new ChangeLogGroupList(getComponentVersion().getID(), getTableSchemaID(), getChangeLogIDFrom(), getChangeLogIDTo(), getUserID());
        groupList.fillList(trxName);
        Element groupNode, columnNode, oldValue, newValue;
        Text oldValueTextNode, newValueTextNode;
        boolean isTableReference;
        List<String> elementsUID;
        for (ChangeLogGroup group : groupList.getGroups()) {
            groupNode = createElement("changegroup");
            setAttribute("tableName", group.getTableName(), groupNode);
            setAttribute("uid", group.getAd_componentObjectUID(), groupNode);
            setAttribute("operation", group.getOperation(), groupNode);
            if (!group.getOperation().equals(MChangeLog.OPERATIONTYPE_Deletion)) {
                for (ChangeLogElement element : group.getElements()) {
                    columnNode = createElement("column");
                    oldValue = createElement("oldValue");
                    newValue = createElement("newValue");
                    setAttribute("name", element.getColumnName(), columnNode);
                    setAttribute("type", String.valueOf(element.getAD_Reference_ID()), columnNode);
                    isTableReference = isTableReference(element);
                    if (isTableReference && !getIgnoresReferenceColumns().contains(element.getColumnName())) {
                        String uidReference = null;
                        String tableName = element.getColumnName();
                        String columnName = element.getColumnName();
                        if (element.getColumnName().toUpperCase().endsWith("_ID")) {
                            tableName = element.getColumnName().substring(0, element.getColumnName().lastIndexOf("_"));
                        }
                        if (element.getAD_Reference_Value_ID() != 0) {
                            String sql = "SELECT ad_table_id, ad_key FROM ad_ref_table WHERE ad_reference_id = ?";
                            PreparedStatement ps = null;
                            ResultSet rs = null;
                            int tableID = 0, key = 0;
                            try {
                                ps = DB.prepareStatement(sql, trxName);
                                ps.setInt(1, element.getAD_Reference_Value_ID());
                                rs = ps.executeQuery();
                                if (rs.next()) {
                                    tableID = rs.getInt("ad_table_id");
                                    key = rs.getInt("ad_key");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    if (ps != null) ps.close();
                                    if (rs != null) rs.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            sql = "SELECT tablename FROM ad_table WHERE ad_table_id = ? LIMIT 1";
                            tableName = DB.getSQLValueString(trxName, sql, tableID);
                            columnName = M_Column.getColumnName(Env.getCtx(), key);
                        }
                        if (existsColumn(tableName, "ad_componentObjectUID")) {
                            setAttribute("refTable", tableName, columnNode);
                            if (element.getNewValue() != null) {
                                String sql = "SELECT ad_componentobjectuid FROM " + tableName + " WHERE " + columnName + " = ?";
                                PreparedStatement ps = null;
                                ResultSet rs = null;
                                Object value;
                                Integer intValue;
                                String strValue = String.valueOf(element.getNewValue());
                                try {
                                    intValue = Integer.parseInt(strValue);
                                    value = intValue;
                                } catch (NumberFormatException cce) {
                                    value = strValue;
                                }
                                try {
                                    ps = DB.prepareStatement(sql, trxName);
                                    ps.setObject(1, value);
                                    rs = ps.executeQuery();
                                    if (rs.next()) {
                                        uidReference = rs.getString("ad_componentobjectuid");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (ps != null) ps.close();
                                        if (rs != null) rs.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (Util.isEmpty(uidReference)) {
                                    elementsUID = new ArrayList<String>();
                                    elementsUID.add(getComponent().getPrefix());
                                    elementsUID.add(tableName);
                                    elementsUID.add(String.valueOf(element.getNewValue()));
                                    uidReference = PO.makeUID(elementsUID);
                                }
                                setAttribute("refUID", uidReference, newValue);
                            }
                        }
                    }
                    oldValueTextNode = createTextNode(String.valueOf(element.getOldValue()));
                    newValueTextNode = createTextNode(String.valueOf(element.getNewValue()));
                    if (element.getBinaryValue() != null) {
                        setAttribute("algorithm", "base64", columnNode);
                        System.out.println(element.getBinaryValue());
                        Base64Encoder encoder = new Base64Encoder(element.toString());
                        oldValueTextNode = createTextNode("null");
                        newValueTextNode = createTextNode(encoder.processString());
                    }
                    addNode(oldValueTextNode, oldValue);
                    addNode(newValueTextNode, newValue);
                    addNode(oldValue, columnNode);
                    addNode(newValue, columnNode);
                    addNode(columnNode, groupNode);
                }
            }
            addNode(groupNode, getRootNode());
        }
        int groupListSize = groupList.getGroups().size();
        if (groupListSize > 0) {
            ChangeLogGroup lastGroup = groupList.getGroups().get(groupListSize - 1);
            int elementSize = lastGroup.getElements().size();
            if (elementSize > 0) {
                ChangeLogElement lastElement = lastGroup.getElements().get(elementSize - 1);
                lastChangelogID = lastElement.getAD_Changelog_ID();
            }
        }
    }

    /**
	 * @param tableName nombre de tabla
	 * @param columnName nombre de columna
	 * @return true si existe la columna con el nombre parámetro en la tabla parámetro
	 */
    protected boolean existsColumn(String tableName, String columnName) {
        String sql = "SELECT COALESCE(count(*),0) AS exist " + "FROM ad_table as t " + "INNER JOIN ad_column as c on (t.ad_table_id = c.ad_table_id) " + "WHERE upper(tablename) = upper(?) AND upper(columnname) = upper(?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        int exist = 0;
        try {
            ps = DB.prepareStatement(sql, trxName);
            ps.setString(1, tableName);
            ps.setString(2, columnName);
            rs = ps.executeQuery();
            if (rs.next()) {
                exist = rs.getInt("exist");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (rs != null) rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return exist > 0;
    }

    /**
	 * Obtener el esquema de tablas de las subclases
	 */
    protected abstract Integer getTableSchemaID();

    private void setIgnoresReferenceColumns(List<String> ignoresReferenceColumns) {
        this.ignoresReferenceColumns = ignoresReferenceColumns;
    }

    protected List<String> getIgnoresReferenceColumns() {
        return ignoresReferenceColumns;
    }

    protected void setComponentVersion(MComponentVersion componentVersion) {
        this.componentVersion = componentVersion;
    }

    protected MComponentVersion getComponentVersion() {
        return componentVersion;
    }

    protected void setComponent(MComponent component) {
        this.component = component;
    }

    protected MComponent getComponent() {
        return component;
    }

    protected void setChangeLogIDFrom(Integer changeLogIDFrom) {
        this.changeLogIDFrom = changeLogIDFrom;
    }

    protected Integer getChangeLogIDFrom() {
        return changeLogIDFrom;
    }

    protected void setChangeLogIDTo(Integer changeLogIDTo) {
        this.changeLogIDTo = changeLogIDTo;
    }

    protected Integer getChangeLogIDTo() {
        return changeLogIDTo;
    }

    protected void setUserID(Integer userID) {
        this.userID = userID;
    }

    protected Integer getUserID() {
        return userID;
    }

    public int getLastChangelogID() {
        return lastChangelogID;
    }

    public void setLastChangelogID(int lastChangelogID) {
        this.lastChangelogID = lastChangelogID;
    }
}
