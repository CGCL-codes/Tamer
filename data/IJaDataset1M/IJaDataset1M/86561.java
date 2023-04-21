package thickclient.dataAccess;

import java.sql.*;
import thickclient.util.TimeFormat;

/**
 *
 * @author ravenscream
 */
public class BOMDAO extends BaseDAO {

    public static final String PKID = "pkid";

    public static final String NAMESPACE = "namespace";

    public static final String LINK_TYPE = "link_type";

    public static final String PARENT_ITEM_ID = "parent_item_id";

    public static final String PARENT_ITEM_CODE = "parent_item_code";

    public static final String REF_REVISION_NUM = "ref_revision_num";

    public static final String REF_REVISION_TAG = "ref_revision_tag";

    public static final String REF_VERSION_NUM = "ref_version_num";

    public static final String REF_VERSION_TAG = "ref_version_tag";

    public static final String REF_DRAWING = "ref_drawing";

    public static final String PROCESS_ID = "process_id";

    public static final String PROCESS_CODE = "process_code";

    public static final String PROCESS_TYPE = "process_type";

    public static final String PROCESS_OPTION = "process_option";

    public static final String PLANT_ID = "plant_id";

    public static final String PLANT_CODE = "plant_code";

    public static final String PLANT_TYPE = "plant_type";

    public static final String MACHINE_ID = "machine_id";

    public static final String MACHINE_CODE = "machine_code";

    public static final String MACHINE_TYPE = "machine_type";

    public static final String ASSEMBLYLINE_ID = "assemblyline_id";

    public static final String ASSEMBLYLINE_CODE = "assemblyline_code";

    public static final String ASSEMBLYLINE_TYPE = "assemblyline_type";

    public static final String LVL_THIS = "lvl_this";

    public static final String LVL_TOP = "lvl_top";

    public static final String LVL_BOT = "lvl_bot";

    public static final String CATEGORYID = "categoryid";

    public static final String CATEGORY1 = "category1";

    public static final String CATEGORY2 = "category2";

    public static final String CATEGORY3 = "category3";

    public static final String UOM = "uom";

    public static final String WEIGHT = "weight";

    public static final String LENGTH = "length";

    public static final String WIDTH = "width";

    public static final String DEPTH = "depth";

    public static final String REBATE1_PCT = "rebate1_pct";

    public static final String REBATE1_PRICE = "rebate1_price";

    public static final String REBATE1_START = "rebate1_start";

    public static final String REBATE1_END = "rebate1_end";

    public static final String DISC1_PCT = "disc1_pct";

    public static final String DISC1_AMOUNT = "disc1_amount";

    public static final String DISC1_START = "disc1_start";

    public static final String DISC1_END = "disc1_end";

    public static final String RESERVED1 = "reserved1";

    public static final String RESERVED2 = "reserved2";

    public static final String RESERVED3 = "reserved3";

    public static final String STATE = "state";

    public static final String STATUS = "status";

    public static final String LASTUPDATE = "lastupdate";

    public static final String USERID_EDIT = "userid_edit";

    public static final String DISPLAY_LOGIC = "display_logic";

    public static final String TABLENAME = "APP.\"inv_bom\"";

    public static final String DISC_FACTOR_ALLOW = "true";

    public static final String DISC_FACTOR_DISALLOW = "false";

    public static BOMObject getNextUpdatedBOM(long lastUpdate) {
        BOMObject result = new BOMObject();
        try {
            Timestamp time = TimeFormat.getTimestamp();
            time.setTime(lastUpdate);
            Connection conn = BaseDAO.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT *, lastupdate AS server_time from inv_bom WHERE lastupdate > '" + TimeFormat.strDisplayDate10(time) + "' ORDER BY lastupdate LIMIT 1");
            System.out.println(time.getTime());
            if (rs.next()) {
                System.out.println("In item resultset");
                result.pkid = new Integer(rs.getInt(PKID));
                result.namespace = rs.getString(NAMESPACE);
                result.linkType = rs.getString(LINK_TYPE);
                result.parentItemId = new Integer(rs.getInt(PARENT_ITEM_ID));
                result.parentItemCode = rs.getString(PARENT_ITEM_CODE);
                result.refRevisionNum = new Integer(rs.getInt(REF_REVISION_NUM));
                result.refRevisionTag = rs.getString(REF_REVISION_TAG);
                result.refVersionNum = new Integer(rs.getInt(REF_VERSION_NUM));
                result.refVersionTag = rs.getString(REF_VERSION_TAG);
                result.refDrawing = rs.getString(REF_DRAWING);
                result.processId = new Integer(rs.getInt(PROCESS_ID));
                result.processCode = rs.getString(PROCESS_CODE);
                result.processType = rs.getString(PROCESS_TYPE);
                result.processOption = rs.getString(PROCESS_OPTION);
                result.plantId = new Integer(rs.getInt(PLANT_ID));
                result.plantCode = rs.getString(PLANT_CODE);
                result.plantType = rs.getString(PLANT_TYPE);
                result.machineId = new Integer(rs.getInt(MACHINE_ID));
                result.machineCode = rs.getString(MACHINE_CODE);
                result.machineType = rs.getString(MACHINE_TYPE);
                result.assemblylineId = new Integer(rs.getInt(ASSEMBLYLINE_ID));
                result.assemblylineCode = rs.getString(ASSEMBLYLINE_CODE);
                result.assemblylineType = rs.getString(ASSEMBLYLINE_TYPE);
                result.lvlThis = new Integer(rs.getInt(LVL_THIS));
                result.lvlTop = new Integer(rs.getInt(LVL_TOP));
                result.lvlBot = new Integer(rs.getInt(LVL_BOT));
                result.categoryid = new Integer(rs.getInt(CATEGORYID));
                result.category1 = rs.getString(CATEGORY1);
                result.category2 = rs.getString(CATEGORY2);
                result.category3 = rs.getString(CATEGORY3);
                result.uom = rs.getString(UOM);
                result.weight = rs.getBigDecimal(WEIGHT);
                result.length = rs.getBigDecimal(LENGTH);
                result.width = rs.getBigDecimal(WIDTH);
                result.depth = rs.getBigDecimal(DEPTH);
                result.rebate1Pct = rs.getBigDecimal(REBATE1_PCT);
                result.rebate1Price = rs.getBigDecimal(REBATE1_PRICE);
                result.rebate1Start = rs.getTimestamp(REBATE1_START).getTime();
                result.rebate1End = rs.getTimestamp(REBATE1_END).getTime();
                result.disc1Pct = rs.getBigDecimal(DISC1_PCT);
                result.disc1Amount = rs.getBigDecimal(DISC1_AMOUNT);
                result.disc1Start = rs.getTimestamp(DISC1_START).getTime();
                result.disc1End = rs.getTimestamp(DISC1_END).getTime();
                result.reserved1 = rs.getString(RESERVED1);
                result.reserved2 = rs.getString(RESERVED2);
                result.reserved3 = rs.getString(RESERVED3);
                result.state = rs.getString(STATE);
                result.status = rs.getString(STATUS);
                result.lastupdate = rs.getTimestamp(LASTUPDATE).getTime();
                result.useridEdit = new Integer(rs.getInt(USERID_EDIT));
                result.display_logic = rs.getString(DISPLAY_LOGIC);
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
