package org.riverock.portlet.price;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.schema.db.CustomSequenceType;
import org.riverock.common.tools.DateTools;
import org.riverock.common.tools.RsetTools;

/**
 * User: Admin
 * Date: Dec 29, 2002
 * Time: 9:12:53 PM
 *
 * $Id: ImportPriceProcess.java,v 1.7 2006/04/22 11:55:10 serg_main Exp $
 */
public class ImportPriceProcess {

    private static Logger log = Logger.getLogger(ImportPriceProcess.class);

    private static void moveItemToPrice(DatabaseAdapter dbDyn, Long idSite) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = dbDyn.prepareStatement("select IS_GROUP, ID, ID_MAIN, NAME, PRICE, CURRENCY, b.ID_SHOP " + "from WM_PRICE_IMPORT_TABLE a, WM_PRICE_SHOP_LIST b " + "where a.SHOP_CODE=b.CODE_SHOP and b.ID_SITE=?");
            RsetTools.setLong(ps, 1, idSite);
            rs = ps.executeQuery();
            while (rs.next()) {
                Long id_shop_ = RsetTools.getLong(rs, "ID_SHOP");
                PreparedStatement ps1 = null;
                ResultSet rs1 = null;
                try {
                    ps1 = dbDyn.prepareStatement("select ID, ID_SHOP, ID_ITEM " + "from   WM_PRICE_LIST a " + "where  ID=? and ID_SHOP=?");
                    RsetTools.setLong(ps1, 1, RsetTools.getLong(rs, "ID"));
                    RsetTools.setLong(ps1, 2, id_shop_);
                    rs1 = ps1.executeQuery();
                    if (rs1.next()) {
                        PreparedStatement ps2 = null;
                        ResultSet rs2 = null;
                        try {
                            ps2 = dbDyn.prepareStatement("update WM_PRICE_LIST " + "set    IS_GROUP=?, ID_MAIN=?, PRICE=?, ITEM=?, CURRENCY=?, ABSOLETE=0 " + "where  ID_ITEM=?");
                            RsetTools.setLong(ps2, 1, RsetTools.getLong(rs, "IS_GROUP"));
                            RsetTools.setLong(ps2, 2, RsetTools.getLong(rs, "ID_MAIN"));
                            RsetTools.setDouble(ps2, 3, RsetTools.getDouble(rs, "PRICE"));
                            ps2.setString(4, RsetTools.getString(rs, "NAME"));
                            ps2.setString(5, RsetTools.getString(rs, "CURRENCY"));
                            RsetTools.setLong(ps2, 6, RsetTools.getLong(rs1, "ID_ITEM"));
                            ps2.executeUpdate();
                        } catch (Exception e) {
                            log.error("error mark for delete", e);
                            throw e;
                        } finally {
                            DatabaseManager.close(rs2, ps2);
                            rs2 = null;
                            ps2 = null;
                        }
                    } else {
                        PreparedStatement ps2 = null;
                        try {
                            CustomSequenceType seq = new CustomSequenceType();
                            seq.setSequenceName("seq_WM_PRICE_LIST");
                            seq.setTableName("WM_PRICE_LIST");
                            seq.setColumnName("ID_ITEM");
                            long id = dbDyn.getSequenceNextValue(seq);
                            ps2 = dbDyn.prepareStatement("insert into WM_PRICE_LIST " + "(id_item,is_group,id,id_main,item,price,currency," + "absolete,id_shop,ADD_DATE)" + "values " + "(?, ?, ?, ?, ?, ?, ?, 0, ?,  " + dbDyn.getNameDateBind() + " )");
                            ps2.setLong(1, id);
                            RsetTools.setLong(ps2, 2, RsetTools.getLong(rs, "IS_GROUP"));
                            RsetTools.setLong(ps2, 3, RsetTools.getLong(rs, "ID"));
                            RsetTools.setLong(ps2, 4, RsetTools.getLong(rs, "ID_MAIN"));
                            ps2.setString(5, RsetTools.getString(rs, "NAME"));
                            RsetTools.setDouble(ps2, 6, RsetTools.getDouble(rs, "PRICE"));
                            ps2.setString(7, RsetTools.getString(rs, "CURRENCY"));
                            RsetTools.setLong(ps2, 8, RsetTools.getLong(rs, "ID_SHOP"));
                            dbDyn.bindDate(ps2, 9, DateTools.getCurrentTime());
                            ps2.executeUpdate();
                        } catch (Exception e) {
                            log.error("error mark for delete", e);
                            throw e;
                        } finally {
                            DatabaseManager.close(ps2);
                            ps2 = null;
                        }
                    }
                } catch (Exception e) {
                    log.error("error mark for delete", e);
                    throw e;
                } finally {
                    DatabaseManager.close(rs1, ps1);
                    rs1 = null;
                    ps1 = null;
                }
            }
        } catch (Exception e) {
            log.error("error mark for delete", e);
            throw e;
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    private static void markForDelete(DatabaseAdapter dbDyn, Long idSite) throws Exception {
        PreparedStatement ps = null;
        String sql_ = null;
        try {
            if (dbDyn.getFamaly() != DatabaseManager.MYSQL_FAMALY) {
                sql_ = "update WM_PRICE_IMPORT_TABLE set FOR_DELETE=1 " + "where IS_TO_LOAD='NO' and SHOP_CODE in (" + "select CODE_SHOP from WM_PRICE_SHOP_LIST where ID_SITE=? )";
                ps = dbDyn.prepareStatement(sql_);
                RsetTools.setLong(ps, 1, idSite);
                ps.executeUpdate();
            } else {
                String sqlCheck = "";
                boolean isFound = false;
                PreparedStatement ps2 = null;
                ResultSet rs2 = null;
                boolean isFirst = true;
                try {
                    ps2 = dbDyn.prepareStatement("select CODE_SHOP from WM_PRICE_SHOP_LIST where ID_SITE=?");
                    RsetTools.setLong(ps2, 1, idSite);
                    rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        isFound = true;
                        if (isFirst) isFirst = false; else sqlCheck += ",";
                        sqlCheck += ("'" + RsetTools.getString(rs2, "CODE_SHOP") + "'");
                    }
                } catch (Exception e) {
                    log.error("error mark for delete", e);
                    throw e;
                } finally {
                    DatabaseManager.close(rs2, ps2);
                    rs2 = null;
                    ps2 = null;
                }
                if (isFound) {
                    sql_ = "update WM_PRICE_IMPORT_TABLE set FOR_DELETE=1 " + "where IS_TO_LOAD='NO' and SHOP_CODE in ( " + sqlCheck + " )";
                    if (log.isDebugEnabled()) log.debug("sql " + sql_);
                    ps = dbDyn.prepareStatement(sql_);
                    ps.executeUpdate();
                    ps.close();
                    ps = null;
                }
            }
        } catch (Exception e) {
            log.error("sql " + sql_);
            log.error("error mark for delete", e);
            throw e;
        } finally {
            DatabaseManager.close(ps);
            ps = null;
        }
        ResultSet rs = null;
        try {
            ps = dbDyn.prepareStatement("select a.ID " + "from   WM_PRICE_IMPORT_TABLE a, WM_PRICE_IMPORT_TABLE b, WM_PRICE_SHOP_LIST c " + "where  a.ID_MAIN = b.ID and b.FOR_DELETE=1 and a.FOR_DELETE=0 and " + "       a.SHOP_CODE=c.CODE_SHOP and c.ID_SITE=?");
            RsetTools.setLong(ps, 1, idSite);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (dbDyn.getFamaly() != DatabaseManager.MYSQL_FAMALY) {
                    PreparedStatement ps1 = null;
                    try {
                        ps1 = dbDyn.prepareStatement("update WM_PRICE_IMPORT_TABLE set FOR_DELETE=1 where ID_MAIN in " + "(select ID from WM_PRICE_IMPORT_TABLE z1, WM_PRICE_SHOP_LIST x1 " + "where z1.FOR_DELETE = 1 and z1.SHOP_CODE = x1.CODE_SHOP and x1.ID_SITE = ?)" + "and FOR_DELETE=0 and SHOP_CODE in " + "(select CODE_SHOP from WM_PRICE_SHOP_LIST where ID_SITE=?)");
                        RsetTools.setLong(ps1, 1, idSite);
                        ps1.executeUpdate();
                    } finally {
                        DatabaseManager.close(ps1);
                        ps1 = null;
                    }
                } else {
                }
            }
        } catch (Exception e) {
            log.error("error mark for delete", e);
            throw e;
        } finally {
            DatabaseManager.close(rs, ps);
            rs = null;
            ps = null;
        }
    }

    public static void process(DatabaseAdapter dbDyn, Long idSite) throws Exception {
        try {
            dbDyn.createStatement().executeUpdate("update WM_PRICE_IMPORT_TABLE set SHOP_CODE = UPPER(SHOP_CODE)");
            markForDelete(dbDyn, idSite);
            PreparedStatement ps = null;
            try {
                ps = dbDyn.prepareStatement("delete FROM WM_PRICE_IMPORT_TABLE where FOR_DELETE=1 and " + "SHOP_CODE in (select CODE_SHOP from WM_PRICE_SHOP_LIST where ID_SITE=?)");
                RsetTools.setLong(ps, 1, idSite);
                ps.executeUpdate();
            } catch (Exception e) {
                log.error("error mark for delete", e);
                throw e;
            } finally {
                DatabaseManager.close(ps);
                ps = null;
            }
            try {
                ps = dbDyn.prepareStatement("update WM_PRICE_IMPORT_TABLE set IS_NEW=1 where ID not in " + "(select z1.ID from WM_PRICE_LIST z1, WM_PRICE_SHOP_LIST x1 " + "where SHOP_CODE = x1.CODE_SHOP and " + "z1.ID_SHOP = x1.ID_SHOP and x1.ID_SITE=?)");
                RsetTools.setLong(ps, 1, idSite);
                ps.executeUpdate();
            } catch (Exception e) {
                log.error("error mark for delete", e);
                throw e;
            } finally {
                DatabaseManager.close(ps);
                ps = null;
            }
            try {
                ps = dbDyn.prepareStatement("update WM_PRICE_LIST set ABSOLETE=1 where ID_SHOP in " + "(select z1.ID_SHOP from WM_PRICE_SHOP_LIST z1, WM_PRICE_IMPORT_TABLE x1 " + "where z1.ID_SITE=? and z1.CODE_SHOP=x1.SHOP_CODE)");
                RsetTools.setLong(ps, 1, idSite);
                ps.executeUpdate();
            } catch (Exception e) {
                log.error("error mark for delete", e);
                throw e;
            } finally {
                DatabaseManager.close(ps);
                ps = null;
            }
            moveItemToPrice(dbDyn, idSite);
            try {
                ps = dbDyn.prepareStatement("update WM_PRICE_SHOP_LIST " + "set    LAST_DATE_UPLOAD=" + dbDyn.getNameDateBind() + " " + "where  ID_SITE=? and CODE_SHOP in " + "(select distinct x1.SHOP_CODE from WM_PRICE_IMPORT_TABLE x1 )");
                dbDyn.bindDate(ps, 1, DateTools.getCurrentTime());
                RsetTools.setLong(ps, 2, idSite);
                ps.executeUpdate();
            } catch (Exception e) {
                log.error("", e);
                throw e;
            } finally {
                DatabaseManager.close(ps);
                ps = null;
            }
        } catch (Exception e) {
            log.error("Error inport price-list", e);
            throw e;
        }
    }
}
