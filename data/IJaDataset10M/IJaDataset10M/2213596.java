package com.etc.bin;

import com.etc.bin.base.Constant;
import com.etc.bin.base.HttpBaseServlet;
import com.etc.bin.base.RequestManager;
import com.etc.bin.base.SessionManager;
import com.etc.bin.beans.UserLoginBeans;
import com.etc.controller.EnvironmentController;
import com.etc.controller.PartnerController;
import com.etc.controller.ProductCategoryController;
import com.etc.controller.ProductController;
import com.etc.controller.ProductGroupController;
import com.etc.controller.ProductTypeController;
import com.etc.controller.ProductUnitController;
import com.etc.controller.ShelfController;
import com.etc.controller.StoreController;
import com.etc.controller.beans.PartnerBeans;
import com.etc.controller.beans.ProductBeans;
import com.etc.controller.beans.ProductCategoryBeans;
import com.etc.controller.beans.ProductGroupBeans;
import com.etc.controller.beans.ProductTypeBeans;
import com.etc.controller.beans.ProductUnitBeans;
import com.etc.controller.beans.ShelfBeans;
import com.etc.controller.beans.StoreBeans;
import com.etc.db.oracle.Connector;
import com.etc.output.OutputManager;
import com.etc.report.ProductPriceReport;
import com.etc.ui.OptionManager;
import com.etc.util.Misc;
import com.etc.util.SQL;
import com.etc.ui.UIUtils;
import com.etc.util.CurrencyUtils;
import com.etc.util.NumberUtils;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.http.*;
import net.sf.jasperreports.engine.JRDataSource;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author MaGicBank
 * @version
 */
public class Pricing extends HttpBaseServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestManager req = new RequestManager(request);
        SessionManager session = new SessionManager(request);
        UserLoginBeans user = (UserLoginBeans) session.get("#USER#");
        if ((req.command("") || req.command("FIND")) && user.r(PRIC)) {
            int pageNumber = (req.getString("page").equalsIgnoreCase("")) ? 1 : Integer.parseInt(req.getString("page"));
            int pageTotal = 1;
            int rowCount = 0;
            String keyword = req.getString("keyword");
            String shelfkey = req.getString("shelf");
            String listTheme = "";
            int qtyfilter = req.getString("qtyfilter").equalsIgnoreCase("") ? Constant.TRUE : Integer.parseInt(req.getString("qtyfilter"));
            int expfilter = req.getString("expfilter").equalsIgnoreCase("") ? Constant.FALSE : Integer.parseInt(req.getString("expfilter"));
            int start = 0;
            String ext = "AND BALANCE_V.AMOUNT_BALANCE >= " + qtyfilter + " ";
            if (expfilter == Constant.TRUE) {
                ext += "AND EXP <= " + date.getTimeInMillis() + " ";
            }
            String sql = "";
            List<String> storelist = user.getStoreAccess();
            int sid = req.getInt("sId");
            String gId = req.getString("gId").equalsIgnoreCase("") ? "0" : req.getString("gId");
            String pcId = req.getString("pcId").equalsIgnoreCase("") ? "0" : req.getString("pcId");
            if (sid > 0) {
                sql = "AND BALANCE_V.STORE = " + sid;
            } else {
                if (user.getPermission() < Constant.ADMINISTRATOR) {
                    if (storelist.size() > 0) {
                        sql = "AND (";
                        for (int i = 0; i < storelist.size(); i++) {
                            if (i == storelist.size() - 1) {
                                sql += "BALANCE_V.STORE = " + storelist.get(i) + ")";
                            } else {
                                sql += "BALANCE_V.STORE = " + storelist.get(i) + " OR ";
                            }
                        }
                    }
                }
            }
            String shelfext = "";
            if (!shelfkey.isEmpty()) {
                shelfext = "SHELF.NAME LIKE '%" + shelfkey + "%' AND";
            }
            String sql2 = "";
            if (Integer.parseInt(gId) > 0) {
                sql2 = "AND BALANCE_V.GID = " + gId;
            } else {
                Set<String> group = user.getProductGroupAccess();
                if (group.size() > 0 && user.getPermission() < Constant.ADMINISTRATOR) {
                    sql2 = "AND (";
                    String[] list = group.toArray(new String[0]);
                    for (int i = 0; i < list.length; i++) {
                        if (i == list.length - 1) {
                            sql2 += "BALANCE_V.GID = " + list[i] + ")";
                        } else {
                            sql2 += "BALANCE_V.GID = " + list[i] + " OR ";
                        }
                    }
                }
            }
            if (Integer.parseInt(pcId) > 0) {
                ext += "AND BALANCE_V.CATID = " + pcId;
            }
            Connection conn = Connector.getConnection();
            try {
                rowCount = SQL.rowCount("SELECT BALANCE_V.* FROM BALANCE_V LEFT JOIN SHELF ON BALANCE_V.SHELF = SHELF.ID LEFT JOIN PARTNER ON BALANCE_V.OWNER = PARTNER.ID WHERE " + shelfext + " (BALANCE_V.PROCODE LIKE '%" + keyword + "%' OR BALANCE_V.PRONAME LIKE '%" + keyword + "%' OR BALANCE_V.BARCODE LIKE '%" + keyword + "%' OR BALANCE_V.DISP_LOT LIKE '%" + keyword + "%' OR PARTNER.NAME LIKE '%" + keyword + "%') " + sql + " " + sql2 + " " + ext, conn);
                pageTotal = new Double(Math.ceil(new Integer(rowCount).doubleValue() / new Integer(rowLimit).doubleValue())).intValue();
                if (pageNumber > pageTotal) {
                    pageNumber = pageTotal;
                }
                start = ((pageNumber - 1) * rowLimit) + 1;
                Statement stm = conn.createStatement();
                ResultSet raw = SQL.limit("SELECT BALANCE_V.* FROM BALANCE_V LEFT JOIN SHELF ON BALANCE_V.SHELF = SHELF.ID LEFT JOIN PARTNER ON BALANCE_V.OWNER = PARTNER.ID WHERE " + shelfext + " (BALANCE_V.PROCODE LIKE '%" + keyword + "%' OR BALANCE_V.PRONAME LIKE '%" + keyword + "%' OR BALANCE_V.BARCODE LIKE '%" + keyword + "%' OR BALANCE_V.DISP_LOT LIKE '%" + keyword + "%' OR PARTNER.NAME LIKE '%" + keyword + "%') " + sql + " " + sql2 + " " + ext + " ORDER BY BALANCE_V.REG DESC, BALANCE_V.PROCODE ASC", stm, start, rowLimit);
                while (raw.next()) {
                    StoreBeans store = StoreController.getStoreId(raw.getInt("STORE"));
                    ShelfBeans shelf = ShelfController.getShelfById(raw.getInt("SHELF"));
                    PartnerBeans partner = PartnerController.getPartner(raw.getInt("OWNER"));
                    ProductBeans product = ProductController.getProductById(raw.getInt("PROID"));
                    ProductTypeBeans type = ProductTypeController.getProductType(raw.getInt("TYPEID"));
                    ProductGroupBeans group = ProductGroupController.getProductGroup(raw.getInt("GID"));
                    ProductCategoryBeans category = ProductCategoryController.getProductCategory(raw.getInt("CATID"));
                    ProductUnitBeans unit = ProductUnitController.getUnit(raw.getInt("UNIT"));
                    HashMap<String, String> listMap = new HashMap<String, String>();
                    listMap.put(":V_ID:", raw.getString("ID"));
                    listMap.put(":V_PRODUCT_ID:", raw.getString("PROID"));
                    listMap.put(":V_PRODUCT_CODE:", raw.getString("PROCODE"));
                    listMap.put(":V_PRODUCT_NAME:", raw.getString("PRONAME"));
                    listMap.put(":V_PRODUCT_NAME2:", UIUtils.escape(raw.getString("PRONAME")));
                    listMap.put(":V_BARCODE:", deNull(raw.getString("BARCODE")));
                    listMap.put(":V_LOT:", deNull(raw.getString("LOT")));
                    listMap.put(":V_DISP_LOT:", deNull(raw.getString("DISP_LOT")));
                    listMap.put(":V_REG:", Misc.dateFormat(raw.getLong("REG"), "dd/MM/yyyy"));
                    listMap.put(":V_MFG:", Misc.dateFormat(raw.getLong("MFG"), "dd/MM/yyyy"));
                    listMap.put(":V_EXP:", Misc.dateFormat(raw.getLong("EXP"), "dd/MM/yyyy"));
                    listMap.put(":V_UNIT:", unit.getName());
                    listMap.put(":V_COST:", CurrencyUtils.format(raw.getString("COST"), CurrencyUtils.FORM));
                    listMap.put(":V_PRICE:", CurrencyUtils.format(raw.getString("PRICE"), CurrencyUtils.FORM));
                    listMap.put(":V_CCOST:", product.getCcost());
                    listMap.put(":V_CPRICE:", product.getCprice());
                    listMap.put(":V_QTY:", raw.getString("AMOUNT_BALANCE"));
                    listMap.put(":V_QTY_DISP:", NumberUtils.format(raw.getBigDecimal("AMOUNT_BALANCE")));
                    listMap.put(":V_STORE_ID:", Integer.toString(store.getId()));
                    listMap.put(":V_STORE_NAME:", StringUtils.defaultIfEmpty(store.getName(), "-"));
                    listMap.put(":V_SHELF_ID:", Integer.toString(shelf.getId()));
                    listMap.put(":V_SHELF_NAME:", StringUtils.defaultIfEmpty(shelf.getName(), "-"));
                    listMap.put(":V_TYPE:", type.getName());
                    listMap.put(":V_GROUP:", group.getName());
                    listMap.put(":V_CATEGORY:", category.getName());
                    listMap.put(":V_RECEIVE:", NumberUtils.format(raw.getBigDecimal("AMOUNT_IN")));
                    listMap.put(":V_RESERVE:", NumberUtils.format(raw.getBigDecimal("AMOUNT_PRE_OUT")));
                    listMap.put(":V_DISTRIB:", NumberUtils.format(raw.getBigDecimal("AMOUNT_OUT")));
                    listMap.put(":V_POCODE:", getPOCode(raw.getString("LOT")));
                    listMap.put(":V_INVCODE:", getINVCode(raw.getInt("ID")));
                    listMap.put(":V_OID:", Integer.toString(partner.getId()));
                    listMap.put(":V_OCODE:", partner.getCode());
                    listMap.put(":V_ONAME:", partner.getName());
                    listMap.put(":V_ONAME2:", partner.getName().length() > 28 ? StringUtils.left(partner.getName(), 25) + "..." : partner.getName().isEmpty() ? "&nbsp;" : partner.getName());
                    listMap.put(":V_REMARK:", StringUtils.defaultIfEmpty(raw.getString("REMARK"), "-").replaceAll("[\\r\\n]", "<br>"));
                    listMap.put(":V_STYLE:", raw.getLong("EXP") <= date.getTimeInMillis() ? "red" : "content");
                    listMap.putAll(map);
                    listTheme += html.render("/pricing_list.html", listMap);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
            map.put(":V_BEGIN:", "?cmd=FIND&keyword=" + keyword + "&shelf=" + shelfkey + "&pcId=" + pcId + "&gId=" + gId + "&sId=" + sid + "&qtyfilter=" + qtyfilter + "&expfilter=" + expfilter + "&page=1");
            map.put(":V_PREVIEW:", ((pageNumber - 1) < 1) ? "?cmd=FIND&keyword=" + keyword + "&shelf=" + shelfkey + "&pcId=" + pcId + "&gId=" + gId + "&sId=" + sid + "&qtyfilter=" + qtyfilter + "&expfilter=" + expfilter + "&page=1" : "?cmd=FIND&keyword=" + keyword + "&shelf=" + shelfkey + "&pcId=" + pcId + "&gId=" + gId + "&sId=" + sid + "&qtyfilter=" + qtyfilter + "&expfilter=" + expfilter + "&page=" + (pageNumber - 1));
            map.put(":V_NEXT:", ((pageNumber + 1) > pageTotal) ? "?cmd=FIND&keyword=" + keyword + "&shelf=" + shelfkey + "&pcId=" + pcId + "&gId=" + gId + "&sId=" + sid + "&qtyfilter=" + qtyfilter + "&expfilter=" + expfilter + "&page=" + pageTotal : "?cmd=FIND&keyword=" + keyword + "&shelf=" + shelfkey + "&pcId=" + pcId + "&gId=" + gId + "&sId=" + sid + "&qtyfilter=" + qtyfilter + "&expfilter=" + expfilter + "&page=" + (pageNumber + 1));
            map.put(":V_END:", "?cmd=FIND&keyword=" + keyword + "&shelf=" + shelfkey + "&pcId=" + pcId + "&gId=" + gId + "&sId=" + sid + "&qtyfilter=" + qtyfilter + "&expfilter=" + expfilter + "&page=" + pageTotal);
            map.put(":V_JUMP_URL:", "?cmd=FIND&keyword=" + keyword + "&shelf=" + shelfkey + "&pcId=" + pcId + "&gId=" + gId + "&sId=" + sid + "&qtyfilter=" + qtyfilter + "&expfilter=" + expfilter + "&page=");
            map.put(":V_POP_URL_PDF:", "?cmd=PRINT&type=PDF&keyword=" + keyword + "&shelf=" + shelfkey + "&sid=" + sid + "&gid=" + gId + "&qtyfilter=" + qtyfilter + "&expfilter=" + expfilter + "&catid=" + pcId);
            map.put(":V_POP_URL_XLS:", "?cmd=PRINT&type=XLS&keyword=" + keyword + "&shelf=" + shelfkey + "&sid=" + sid + "&gid=" + gId + "&qtyfilter=" + qtyfilter + "&expfilter=" + expfilter + "&catid=" + pcId);
            String opt = html.getNullOption(pcId) + getProductCategoryFindOption(pcId);
            map.put(":PRODUCT_CATEGORY_OPTION:", opt);
            opt = html.getNullOption(Integer.toString(sid)) + getStoreOption(Integer.toString(sid), user);
            map.put(":STORE_OPTION:", opt);
            opt = html.getNullOption(gId) + getProductGroupOption(gId, user);
            map.put(":PRODUCT_GROUP_OPTION:", opt);
            map.put(":V_QTYFILTER:", Integer.toString(qtyfilter));
            map.put(":V_EXPFILTER:", Integer.toString(expfilter));
            map.put(":STORE_SELECTOR:", OptionManager.storeOption(user, false));
            map.put(":DATA_LIST:", listTheme);
            map.put(":V_SHELFKEY:", req.getString("shelf"));
            map.put(":V_KEYWORD:", req.getString("keyword"));
            map.put(":V_ROW_COUNT:", Integer.toString(rowCount));
            map.put(":V_PAGE_NUMBER:", Integer.toString(pageNumber));
            map.put(":V_PAGE_TOTAL:", Integer.toString(pageTotal));
            content += html.render("/pricing.html", map);
            html.output(content);
        } else if (req.command("EDIT") && user.w(PRIC)) {
            String id = req.getString("id");
            String field = req.getString("field");
            String buffer = req.getString("buffer");
            String value = req.getString("value");
            String msg = "0x0E";
            if (!buffer.equalsIgnoreCase(value) && !id.isEmpty()) {
                Connection conn = Connector.getConnection();
                if (field.equalsIgnoreCase("REG")) {
                    long reg = Misc.getMillisFromMiniDate(value);
                    try {
                        String comm = "change register date from " + buffer + " -->> " + value;
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET REG_DATE = '" + reg + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("MFG")) {
                    long mfg = Misc.getMillisFromMiniDate(value);
                    try {
                        String comm = "change manufacturing date from " + buffer + " -->> " + value;
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET MFG_DATE = '" + mfg + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("EXP")) {
                    long exp = Misc.getMillisFromMiniDate(value);
                    try {
                        String comm = "change expire date from " + buffer + " -->> " + value;
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET EXP_DATE = '" + exp + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("LOT")) {
                    String lot = value;
                    try {
                        String comm = "change lot no. from " + buffer + " -->> " + value;
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET DISP_LOT = '" + lot + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("COST")) {
                    String cost = value.replaceAll(",", "");
                    try {
                        String comm = "change cost from " + buffer + " -->> " + value;
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET COST = '" + cost + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("PRICE")) {
                    String price = value.replaceAll(",", "");
                    try {
                        String comm = "change price from " + buffer + " -->> " + value;
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET PRICE = '" + price + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("QTY")) {
                    int ACTIVITY = EnvironmentController.getInt("ADJ_ACT");
                    BigDecimal diff = new BigDecimal(value.replaceAll(",", "")).subtract(new BigDecimal(buffer.replaceAll(",", "")));
                    try {
                        String comm = "change quantity from " + buffer + " -->> " + value;
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET " + "AMOUNT_IN = AMOUNT_IN+(" + diff.toString() + "), " + "AMOUNT_BALANCE = AMOUNT_BALANCE+(" + diff.toString() + ") " + "WHERE ID = " + id);
                        String lot = "";
                        ResultSet raw = stm.executeQuery("SELECT LOT_NO FROM PRODUCT_BALANCE WHERE ID = " + id);
                        if (raw.next()) {
                            lot = raw.getString("LOT_NO");
                        }
                        raw.close();
                        if (diff.compareTo(BigDecimal.ZERO) < 0) {
                            stm.executeUpdate("INSERT INTO LOG_PRODUCT (ID, TIME, LOT, QTY, AID, TYPE, CODE) VALUES (" + "'', " + "'" + date.getTimeInMillis() + "', " + "'" + lot + "', " + "'" + diff.abs() + "', " + "'" + ACTIVITY + "', " + "'" + OUT + "', " + "'ADJ-" + new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()) + "'" + ")");
                        } else if (diff.compareTo(BigDecimal.ZERO) > 0) {
                            stm.executeUpdate("INSERT INTO LOG_PRODUCT (ID, TIME, LOT, QTY, AID, TYPE, CODE) VALUES (" + "'', " + "'" + date.getTimeInMillis() + "', " + "'" + lot + "', " + "'" + diff.abs() + "', " + "'" + ACTIVITY + "', " + "'" + IN + "', " + "'ADJ-" + new SimpleDateFormat("yyyy-MM-dd").format(date.getTime()) + "'" + ")");
                        }
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("OWNER")) {
                    String owner = value;
                    try {
                        PartnerBeans prev = PartnerController.getPartner(Integer.parseInt(buffer));
                        PartnerBeans next = PartnerController.getPartner(Integer.parseInt(value));
                        String comm = "change product owner from " + prev.getName() + " -->> " + next.getName();
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET OWNER = '" + owner + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("STORE")) {
                    String store = value;
                    try {
                        StoreBeans prev = StoreController.getStoreId(Integer.parseInt(buffer));
                        StoreBeans next = StoreController.getStoreId(Integer.parseInt(value));
                        String comm = "change product store from " + prev.getName() + " -->> " + next.getName();
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET STORE_ID = '" + store + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = next.getName();
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("SHELF")) {
                    String shelf = value;
                    try {
                        ShelfBeans prev = ShelfController.getShelfById(Integer.parseInt(buffer));
                        ShelfBeans next = ShelfController.getShelfById(Integer.parseInt(value));
                        String comm = "change product shelf from " + prev.getName() + " -->> " + next.getName();
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET SHELF_ID = '" + shelf + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                } else if (field.equalsIgnoreCase("REMARK")) {
                    String remark = value;
                    try {
                        String comm = "change product remark from " + buffer + " -->> " + value;
                        Statement stm = conn.createStatement();
                        stm.executeUpdate("UPDATE PRODUCT_BALANCE SET REMARK = '" + remark + "' WHERE ID = " + id);
                        stm.executeUpdate("INSERT INTO ADJUST_LOG (ID, BID, ADJDATE, USR, COMM) VALUES (" + "'', " + "'" + id + "', " + "'" + date.getTimeInMillis() + "', " + "'" + user.getId() + "', " + "'" + comm + "'" + ")");
                        stm.close();
                        msg = "0x00";
                    } catch (SQLException ex) {
                        log4j.error("SQL Exception.", ex);
                    }
                }
                Connector.close(conn);
            }
            OutputManager.printText(msg, response);
        } else if (req.command("LOG") && user.r(PRIC)) {
            int pageNumber = (req.getString("page").equalsIgnoreCase("")) ? 1 : Integer.parseInt(req.getString("page"));
            int pageTotal = 1;
            int rowCount = 0;
            String keyword = req.getString("keyword").equalsIgnoreCase("") ? "" : req.getString("keyword");
            String pcId = req.getString("pcId").equalsIgnoreCase("") ? "0" : req.getString("pcId");
            String listTheme = "";
            int start = 0;
            String ext = "";
            String gid = "*";
            String catid = "*";
            if (!pcId.equalsIgnoreCase("0")) {
                ext += " AND CATID = " + pcId;
            }
            Connection conn = Connector.getConnection();
            try {
                rowCount = SQL.rowCount("SELECT * FROM ADJUST_LOG_V WHERE (PROCODE LIKE '%" + keyword + "%' OR PRONAME LIKE '%" + keyword + "%' OR DISPLOT LIKE '%" + keyword + "%') " + ext, conn);
                pageTotal = new Double(Math.ceil(new Integer(rowCount).doubleValue() / new Integer(rowLimit).doubleValue())).intValue();
                if (pageNumber > pageTotal) {
                    pageNumber = pageTotal;
                }
                start = ((pageNumber - 1) * rowLimit) + 1;
                Statement stm = conn.createStatement();
                ResultSet raw = SQL.limit("SELECT * FROM ADJUST_LOG_V WHERE (PROCODE LIKE '%" + keyword + "%' OR PRONAME LIKE '%" + keyword + "%' OR DISPLOT LIKE '%" + keyword + "%') " + ext + " ORDER BY ADJDATE DESC", stm, start, rowLimit);
                while (raw.next()) {
                    HashMap<String, String> listMap = new HashMap<String, String>();
                    listMap.put(":V_LOG_DATE:", Misc.dateFormat(raw.getLong("ADJDATE"), "dd/MM/yyyy HH:mm"));
                    listMap.put(":V_LOG_USER:", raw.getString("UNAME"));
                    listMap.put(":V_PRODUCT_CODE:", raw.getString("PROCODE"));
                    listMap.put(":V_PRODUCT_NAME:", raw.getString("PRONAME"));
                    listMap.put(":V_LOT:", deNull(raw.getString("DISPLOT")));
                    listMap.put(":V_REMARK:", StringUtils.defaultIfEmpty(raw.getString("COMM"), ""));
                    listMap.putAll(map);
                    listTheme += html.render("/pricing_log_list.html", listMap);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
            map.put(":V_BEGIN:", "?cmd=LOG&keyword=" + keyword + "&pcId=" + pcId + "&page=1");
            map.put(":V_PREVIEW:", ((pageNumber - 1) < 1) ? "?cmd=LOG&keyword=" + keyword + "&pcId=" + pcId + "&page=1" : "?cmd=LOG&keyword=" + keyword + "&pcId=" + pcId + "&page=" + (pageNumber - 1));
            map.put(":V_NEXT:", ((pageNumber + 1) > pageTotal) ? "?cmd=LOG&keyword=" + keyword + "&pcId=" + pcId + "&page=" + pageTotal : "?cmd=LOG&keyword=" + keyword + "&pcId=" + pcId + "&page=" + (pageNumber + 1));
            map.put(":V_END:", "?cmd=LOG&keyword=" + keyword + "&pcId=" + pcId + "&page=" + pageTotal);
            map.put(":V_JUMP_URL:", "?cmd=LOG&keyword=" + keyword + "&pcId=" + pcId + "&page=");
            map.put(":V_POP_URL_PDF:", "?cmd=LPRINT&type=PDF&keyword=" + keyword + "&gid=" + gid + "&catid=" + catid);
            map.put(":V_POP_URL_XLS:", "?cmd=LPRINT&type=XLS&keyword=" + keyword + "&gid=" + gid + "&catid=" + catid);
            String opt = html.getNullOption(pcId) + getProductCategoryFindOption(pcId);
            map.put(":PRODUCT_CATEGORY_OPTION:", opt);
            map.put(":DATA_LIST:", listTheme);
            map.put(":V_KEYWORD:", keyword);
            map.put(":V_ROW_COUNT:", Integer.toString(rowCount));
            map.put(":V_PAGE_NUMBER:", Integer.toString(pageNumber));
            map.put(":V_PAGE_TOTAL:", Integer.toString(pageTotal));
            content += html.render("/pricing_log.html", map);
            html.output(content);
        } else if (req.command("PRINT") && user.r(PRIC)) {
            String type = req.getString("type").equalsIgnoreCase("") ? "PDF" : req.getString("type");
            String keyword = req.getString("keyword");
            String sid = req.getString("sid");
            String gid = req.getString("gid");
            String qtyfilter = req.getString("qtyfilter");
            String expfilter = req.getString("expfilter");
            String catid = req.getString("catid");
            String SQL1 = "AND BALANCE_V.AMOUNT_BALANCE >= " + qtyfilter + " ";
            if (Integer.parseInt(expfilter) == Constant.TRUE) {
                SQL1 += "AND EXP <= " + date.getTimeInMillis() + " ";
            }
            if (Integer.parseInt(catid) > 0) {
                SQL1 += "AND BALANCE_V.CATID = " + catid + " ";
            }
            String SQL2 = "";
            if (Integer.parseInt(sid) > 0) {
                SQL2 = "AND BALANCE_V.STORE = " + sid;
            } else {
                List<String> store = user.getStoreAccess();
                if (store.size() > 0 && user.getPermission() < Constant.ADMINISTRATOR) {
                    SQL2 = "AND (";
                    for (int i = 0; i < store.size(); i++) {
                        if (i == store.size() - 1) {
                            SQL2 += "BALANCE_V.STORE = " + store.get(i) + ")";
                        } else {
                            SQL2 += "BALANCE_V.STORE = " + store.get(i) + " OR ";
                        }
                    }
                }
            }
            String SQL3 = "";
            if (Integer.parseInt(gid) > 0) {
                SQL3 = "AND BALANCE_V.GID = " + gid;
            } else {
                Set<String> group = user.getProductGroupAccess();
                if (group.size() > 0 && user.getPermission() < Constant.ADMINISTRATOR) {
                    SQL3 = "AND (";
                    String[] list = group.toArray(new String[0]);
                    for (int i = 0; i < list.length; i++) {
                        if (i == list.length - 1) {
                            SQL3 += "BALANCE_V.GID = " + list[i] + ")";
                        } else {
                            SQL3 += "BALANCE_V.GID = " + list[i] + " OR ";
                        }
                    }
                }
            }
            HashMap<Object, Object> vars = new HashMap<Object, Object>();
            vars.put("KEYWORD", keyword);
            vars.put("SQL1", SQL1);
            vars.put("SQL2", SQL2);
            vars.put("SQL3", SQL3);
            vars.putAll(ProductPriceReport.getDataSource(vars));
            printReport(response, "ProductPrice", type, vars, (JRDataSource) vars.get("LIST"));
        } else if (req.command("LPRINT") && user.r(PRIC)) {
            String type = req.getString("type").equalsIgnoreCase("") ? "PDF" : req.getString("type");
            String keyword = req.getString("keyword").equalsIgnoreCase("") ? "%" : req.getString("keyword");
            String gid = req.getString("gid").equalsIgnoreCase("") ? "%" : req.getString("gid");
            String catid = req.getString("catid").equalsIgnoreCase("") ? "%" : req.getString("catid");
            keyword = keyword.replaceAll("\\*", "%");
            gid = gid.replaceAll("\\*", "%");
            catid = catid.replaceAll("\\*", "%");
            HashMap<Object, Object> vars = new HashMap<Object, Object>();
            vars.put("KEYWORD", keyword);
            vars.put("GID", gid);
            vars.put("CATID", catid);
            printReport(response, "AdjustLog", type, vars);
        } else {
            response.sendError(403);
        }
    }

    private String getPOCode(String lot) {
        String code = "";
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT SALE_ORDER_LIST_V.SOCODE FROM SALE_ORDER_LIST_V, PORDER_LIST_V, PRODUCT_ICML_V WHERE SALE_ORDER_LIST_V.ID = PORDER_LIST_V.SOLID AND PORDER_LIST_V.ID = PRODUCT_ICML_V.POLID AND PRODUCT_ICML_V.LOT LIKE '" + lot + "'");
            if (raw.next()) {
                code = raw.getString("SOCODE");
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception", ex);
        }
        Connector.close(conn);
        return code;
    }

    private String getINVCode(int bid) {
        String code = "";
        Connection conn = Connector.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet raw = stm.executeQuery("SELECT SLCODE FROM SALE_LIST_V WHERE BID = " + bid);
            while (raw.next()) {
                if (code.isEmpty()) {
                    code += raw.getString("SLCODE");
                } else {
                    code += ", " + raw.getString("SLCODE");
                }
            }
            raw.close();
            stm.close();
        } catch (SQLException ex) {
            log4j.error("SQL Exception", ex);
        }
        Connector.close(conn);
        return code;
    }

    /** Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "MINERAL - Pricing";
    }
}
