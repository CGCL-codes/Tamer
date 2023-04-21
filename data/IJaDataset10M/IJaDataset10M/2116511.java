package org.mc.content;

import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.json.simple.JSONObject;
import org.mc.ajax.JSON;
import org.mc.catalog.Product;
import org.mc.catalog.ProductCreator;
import org.mc.db.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductChanger extends ContentChanger {

    private static Logger log;

    static {
        log = LoggerFactory.getLogger(ProductChanger.class);
    }

    @Override
    protected File getDescrFileInner() {
        return Product.getDescrFile(id);
    }

    private int parentId;

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    protected String changeTitleQuery() {
        return "update " + DB.Tbl.prod + " set " + col.title + " = '" + value + "' where " + col.id + " = " + id;
    }

    @Override
    protected String sameTitleExistsQuery() {
        return "select " + col.treeId + " from " + DB.Tbl.treeProd + " where " + col.parentId + " in " + " (select " + col.parentId + " from " + DB.Tbl.treeProd + " where " + col.prodId + "=" + id + ") " + " and " + col.title + "='" + value + "' and " + col.prodId + "<>" + id;
    }

    protected static class Const extends ContentChanger.Const {

        public static String VENDOR = "vendor";

        public static String ADD_TO_CATEGORY = "addToCategory";

        public static String DELETE_FROM_CATEGORY = "deleteFromCategory";

        public static String SHORT_DESCR = "shortDescr";

        public static String ATTR_LIST = "attrList";

        public static String EMPTY = "empty";

        public static String ATTR_DELIM = "__MC_ATTR__";

        public static String ATTR_DELIM_INNER = "__MC_ATTR_INNER__";
    }

    protected String changeVendor() throws IOException, SQLException {
        DB.Action.simpleUpdate("update " + DB.Tbl.prod + " set " + col.vendorId + " = " + value + " where " + col.id + " = " + id);
        return JSON.resultOk();
    }

    protected String addToCategory() throws SQLException {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            JSONObject json = new JSONObject();
            Product p = new Product();
            p.setId(id);
            if (DB.Action.sameExists(ProductCreator.getSameTitleExistsQuery(parentId, p.getTitle()))) {
                json.put(JSON.KEY_RESULT, JSON.VAL_SAME_TITLE_EXISTS);
                return json.toString();
            }
            synchronized (app.treeChangeLock) {
                conn = dataSource.getConnection();
                cs = conn.prepareCall("{? = call tree_add_child(?,?)}");
                cs.setInt(2, parentId);
                cs.setInt(3, id);
                cs.executeUpdate();
                cs.close();
                conn.commit();
                json.put(JSON.KEY_RESULT, JSON.VAL_SUCCESS);
                return json.toString();
            }
        } finally {
            try {
                cs.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    protected String removeFromCategory() throws SQLException {
        Connection conn = null;
        CallableStatement cs = null;
        try {
            JSONObject json = new JSONObject();
            synchronized (app.treeChangeLock) {
                conn = dataSource.getConnection();
                cs = conn.prepareCall("{call tree_delete_prod_from_cat(?,?)}");
                cs.setInt(1, id);
                cs.setInt(2, parentId);
                cs.executeUpdate();
                cs.close();
                conn.commit();
                json.put(JSON.KEY_RESULT, JSON.VAL_SUCCESS);
                return json.toString();
            }
        } finally {
            try {
                cs.close();
            } catch (Exception e) {
            }
            try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected String processChange() throws Exception {
        if (property.equals(Const.VENDOR)) {
            return changeVendor();
        }
        if (property.equals(Const.ADD_TO_CATEGORY)) {
            return addToCategory();
        }
        if (property.equals(Const.DELETE_FROM_CATEGORY)) {
            return removeFromCategory();
        }
        if (property.equals(Const.SHORT_DESCR)) {
            return changeShortDescr();
        }
        if (property.equals(Const.ATTR_LIST)) {
            return changeAttrList();
        }
        throw new IllegalArgumentException("Свойство " + property + " не поддерживается.");
    }

    private Map<String, String> parseAttrLisr() throws ParseException {
        try {
            Map<String, String> map = new HashMap<String, String>();
            if (value.equals(Const.EMPTY)) {
                return map;
            }
            String[] rawAttrs = value.split(Const.ATTR_DELIM);
            for (String rawA : rawAttrs) {
                String[] kv = rawA.split(Const.ATTR_DELIM_INNER);
                map.put(kv[0], kv[1]);
            }
            return map;
        } catch (Exception e) {
            throw new ParseException("Ошибка при парсинге переданной строки: " + value, 0);
        }
    }

    private String changeAttrList() throws SQLException, ParseException {
        Map<String, String> attrMap = parseAttrLisr();
        DB.Action.simpleUpdate("delete from " + DB.Tbl.prodAttrs + " where " + col.prodId + " = " + id);
        for (Entry<String, String> e : attrMap.entrySet()) {
            DB.Action.simpleUpdate("insert into " + DB.Tbl.prodAttrs + "(" + col.title + "," + col.val + "," + col.prodId + ") " + "values('" + e.getKey() + "','" + e.getValue() + "', " + id + ")");
        }
        return JSON.resultOk();
    }

    private String changeShortDescr() throws IOException {
        ContentCreator.saveDescr(value, Product.getShortDescrFile(id));
        return JSON.resultOk();
    }
}
