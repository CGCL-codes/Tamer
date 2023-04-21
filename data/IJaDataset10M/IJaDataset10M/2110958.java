package cn.myapps.version.transfer;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.dts.excelimport.utility.Sequence;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.dynaform.view.ejb.ViewProcess;
import cn.myapps.core.dynaform.view.ejb.type.CalendarType;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.StringUtil;
import cn.myapps.util.json.JsonUtil;
import cn.myapps.util.xml.XmlUtil;

public class ViewTransfer extends BaseTransfer {

    /**
	 * 升级到2.4版本
	 */
    public void to2_4() {
        transferAllMapping();
    }

    /**
	 * 迁移视图映射，包括（甘特视图、地图视图、树形视图、日期视图）
	 */
    public void transferAllMapping() {
        Connection conn = getConnection();
        try {
            ViewProcess viewProcess = (ViewProcess) ProcessFactory.createProcess(ViewProcess.class);
            String sql = "select ID,RELATEDMAP,TREERELATIONFIELD,NODENAMEFIELD,NODEVALUEFIELD,RELATIONDATECOLUM from T_VIEW";
            QueryRunner qRunner = new QueryRunner();
            List<?> dataList = (List<?>) qRunner.query(conn, sql, new MapListHandler());
            for (Iterator<?> iterator = dataList.iterator(); iterator.hasNext(); ) {
                Map<?, ?> data = (Map<?, ?>) iterator.next();
                String viewid = (String) data.get("id");
                View view = (View) viewProcess.doView(viewid);
                switch(view.getViewType()) {
                    case View.VIEW_TYPE_GANTT:
                        transferGanttMapping(view, data);
                        viewProcess.doUpdate(view);
                        break;
                    case View.VIEW_TYPE_MAP:
                        transferMapMapping(view, data);
                        viewProcess.doUpdate(view);
                        break;
                    case View.VIEW_TYPE_TREE:
                        transferTreeMapping(view, data);
                        viewProcess.doUpdate(view);
                        break;
                    case View.VIEW_TYPE_CALENDAR:
                        transferCalendarMapping(view, data);
                        viewProcess.doUpdate(view);
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                PersistenceUtils.closeSessionAndConnection();
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * 迁移甘特视图映射
	 * 
	 * @param view
	 * @param data
	 */
    public void transferGanttMapping(View view, Map<?, ?> data) {
        String relatemap = (String) data.get("relatedmap");
        Map<?, ?> relateMap = JsonUtil.toMap(relatemap);
        if (relateMap != null) {
            System.out.println("Start to transfer gantt mapping: " + relateMap.get("ganttview"));
            transferMappingToColumn(view, (Map<?, ?>) relateMap.get("ganttview"));
        }
    }

    /**
	 * 迁移地图视图映射
	 * 
	 * @param view
	 * @param data
	 */
    public void transferMapMapping(View view, Map<?, ?> data) {
        String relatemap = (String) data.get("relatedmap");
        Map<?, ?> relateMap = JsonUtil.toMap(relatemap);
        if (relateMap != null) {
            System.out.println("Start to transfer map mapping: " + relateMap.get("mapview"));
            transferMappingToColumn(view, (Map<?, ?>) relateMap.get("mapview"));
        }
    }

    /**
	 * 迁移树形视图映射
	 * 
	 * @param view
	 * @param data
	 */
    public void transferTreeMapping(View view, Map<?, ?> data) {
        String superiorNodeField = (String) data.get("treerelationfield");
        String currentNodeField = (String) data.get("nodevaluefield");
        String nameNodeField = (String) data.get("nodenamefield");
        Map<Object, Object> mapping = new HashMap<Object, Object>();
        if (!StringUtil.isBlank(superiorNodeField)) mapping.put("superior_Node", superiorNodeField);
        if (!StringUtil.isBlank(currentNodeField)) mapping.put("current_Node", currentNodeField);
        if (!StringUtil.isBlank(nameNodeField)) mapping.put("name_Node", nameNodeField);
        System.out.println("Start to transfer tree mapping: " + mapping);
        transferMappingToColumn(view, mapping);
    }

    /**
	 * 迁移日历视图映射
	 * 
	 * @param view
	 * @param data
	 */
    public void transferCalendarMapping(View view, Map<?, ?> data) {
        String relationdatecolum = (String) data.get("relationdatecolum");
        Map<Object, Object> mapping = new HashMap<Object, Object>();
        if (!StringUtil.isBlank(relationdatecolum)) mapping.put(CalendarType.DEFAULT_KEY_FIELDS[0], relationdatecolum);
        System.out.println("Start to transfer calendar mapping: " + mapping);
        transferMappingToColumn(view, mapping);
    }

    /**
	 * 迁移映射到视图列
	 * 
	 * @param view
	 * @param key2NameMap
	 *            (name是视图列名称或表单字段名称)
	 */
    public void transferMappingToColumn(View view, Map<?, ?> key2NameMap) {
        boolean needTransfer = false;
        if (key2NameMap != null && key2NameMap.size() > 0) {
            for (Iterator<?> iterator = key2NameMap.entrySet().iterator(); iterator.hasNext(); ) {
                Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
                System.out.println("---->get column by column name<----");
                Column column = view.findColumnByName((String) entry.getValue());
                if (column != null) {
                    if (StringUtil.isBlank(column.getMappingField())) {
                        System.out.println("------>update current column: " + column.getName() + column.getMappingField() + "------>" + (String) entry.getKey());
                        needTransfer = true;
                        column.setMappingField((String) entry.getKey());
                    }
                } else {
                    System.out.println("---->get column by column field name<----");
                    column = view.findColumnByFieldName((String) entry.getValue());
                    if (column != null) {
                        if (StringUtil.isBlank(column.getMappingField())) {
                            System.out.println("------>update current column: " + column.getName() + "----" + column.getMappingField() + "------>" + (String) entry.getKey());
                            needTransfer = true;
                            column.setMappingField((String) entry.getKey());
                        }
                    } else {
                        System.out.println("---->creating new column... and update the column set of view");
                        needTransfer = true;
                        createColumn(view, entry);
                    }
                }
            }
            if (needTransfer) {
                System.out.println("---->column set transfer to xml<----");
                view.setColumnXML(XmlUtil.toXml(view.getColumns()));
            } else {
                System.out.println("---->no data to transfer<----");
            }
        }
    }

    private void createColumn(View view, Entry<?, ?> entry) {
        Set<Column> columns = view.getColumns();
        Column column = new Column();
        column.setId(String.valueOf(Sequence.getSequence()));
        column.setApplicationid(view.getApplicationid());
        column.setName(entry.getValue().toString());
        column.setFieldName(entry.getValue().toString());
        column.setMappingField(entry.getKey().toString());
        column.setFlowReturnCss(false);
        column.setSum(false);
        if (columns != null && columns.size() > 0) {
            Column c = (Column) columns.toArray()[0];
            column.setFormid(c.getFormid());
        }
        column.setParentView(view.getId());
        column.setOrderno(createOerderNo(columns));
        column.setShowType("00");
        column.setType(Column.COLUMN_TYPE_FIELD);
        column.setSortId("");
        column.setImageName("");
        column.setFontColor("");
        column.setHiddenScript("");
        column.setValueScript("");
        column.setWidth("");
        column.setVersion(0);
        if (columns != null) {
            columns.add(column);
            view.setColumns(columns);
        }
    }

    private int createOerderNo(Set<Column> columns) {
        Iterator<Column> it = columns.iterator();
        int orderno = 0;
        while (it.hasNext()) {
            Column column = (Column) it.next();
            orderno = orderno < column.getOrderno() ? column.getOrderno() : orderno;
        }
        return ++orderno;
    }

    public static void main(String[] args) {
        new ViewTransfer().to2_4();
    }
}
