package com.pioneer.app.xml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.frame.util.security.ApplicationValidate;
import com.pioneer.app.comm.ApplicationPathMg;
import com.pioneer.app.db.DBConnectionManager;
import com.pioneer.app.db.OADBUtil;
import com.pioneer.app.good.Good;
import com.pioneer.app.util.DateTimeUtil;
import com.pioneer.app.util.Dom4jUtil;

public class GoodCDSService {

    public static final String GET_SALE = "sale";

    public static final String GET_SALE_UP = "saleUpdate";

    public static final String GET_SALE_CHART = "chart";

    public static final String GET_SALE_STATUS = "shopstatus";

    public static final String GET_SALE_UP2 = "saleUpdate2";

    public static final String GET_SALE_PARS = "pars";

    public static Document FirstFilter(Document doc) {
        Document rtdoc = null;
        Element root = doc.getRootElement();
        boolean flg = ApplicationValidate.getInst().isValidate();
        if (!flg) {
            String message = ApplicationValidate.getInst().getMessage();
            rtdoc = DocumentHelper.createDocument();
            Element rtroot = rtdoc.addElement("DATAPACKET");
            rtroot.addAttribute("code", "error");
            rtroot.addAttribute("message", message);
        } else if (null != root) {
            String action = root.valueOf("@ACTION");
            if (GoodCDSService.GET_SALE.equalsIgnoreCase(action)) {
                rtdoc = saleFilter(doc);
            } else if (GoodCDSService.GET_SALE_UP.equalsIgnoreCase(action)) {
                rtdoc = updateSale(doc);
            } else if (GoodCDSService.GET_SALE_UP2.equalsIgnoreCase(action)) {
                rtdoc = updateSale2(doc);
            } else if (GoodCDSService.GET_SALE_CHART.equalsIgnoreCase(action)) {
                rtdoc = doChartData(doc);
            } else if (GoodCDSService.GET_SALE_STATUS.equalsIgnoreCase(action)) {
                rtdoc = doShopStatus(doc);
            } else if (GoodCDSService.GET_SALE_PARS.equalsIgnoreCase(action)) {
                rtdoc = doSetPars(doc);
            }
        }
        return rtdoc;
    }

    private static Document doSetPars(Document doc) {
        Document rtdoc = null;
        rtdoc = DocumentHelper.createDocument();
        Element rtroot = rtdoc.addElement("DATAPACKET");
        if (null != doc) {
            try {
                Element root = doc.getRootElement();
                String shopName = root.valueOf("@shopName");
                String path = ApplicationPathMg.getInstance().getWebRootPath() + "assets/info.xml";
                Document info = Dom4jUtil.getDocFromFile(path);
                Element infoRoot = info.getRootElement();
                infoRoot.addAttribute("shopName", shopName);
                Dom4jUtil.writeDocToFile(info, "GBK", path);
            } catch (Exception e) {
                rtroot.addAttribute("code", "error");
                rtroot.addAttribute("message", "系统错误！");
            }
        }
        return rtdoc;
    }

    private static Document doShopStatus(Document doc) {
        Document rtdoc = null;
        if (null != doc) {
            rtdoc = DocumentHelper.createDocument();
            Element root = doc.getRootElement();
            String startDate = root.valueOf("@STARTDATE");
            String endDate = root.valueOf("@ENDDATE");
            String salesql = "select sum(outprice*saleNumber) as salemoney from t_sale_list where  saledate>=" + startDate + " and saledate<=" + endDate;
            String usesql = "select sum(inprice*saleNumber) as usemoney from t_sale_list where  saledate>=" + startDate + " and saledate<=" + endDate;
            String storesql = "select count(*) little  from t_good where ALERT_NUMBER>=NUMBER";
            Date now = new Date();
            now.setDate(now.getDate() - 1);
            String yestoday = DateTimeUtil.getDate(now, DateTimeUtil.DEFAULT_DATE_SAMPLE_FORMAT, null);
            String daylazyesql = "select count(*) daylazye  from t_good where code in(select t_good.code from t_good right outer join t_sale_list on  (t_good.code=t_sale_list.code   and saledate=" + yestoday + " and t_good.DAY_LAZYE>=t_sale_list.saleNumber) union select code from t_good where code not in(select code from t_sale_list where saledate=" + yestoday + ")) and day_lazye is not null";
            String monthLazyeSql = "select count(*) monthlazye  from t_good where code in (select gg.code from t_good gg ,(select code, sum(saleNumber) sumsale from t_sale_list where saledate>=" + startDate + " and saledate<=" + endDate + " group by code) ss where gg.code=ss.code and gg.MONTH_LAZYE>ss.sumsale union select code from t_good where code not in(select code from t_sale_list where saledate>=" + startDate + " and saledate<=" + endDate + ") and MONTH_LAZYE is not null) ";
            String outDaySql = "select count(*) outday  from t_good where outdate is not null and DATEDIFF(OUTDATE,CURRENT_DATE())<=BEFOREALERT";
            String spendingsql = "select sum(money) as money from t_spending where datetime>=" + startDate + " and datetime<=" + endDate;
            Connection con = null;
            try {
                float saleMoney = 0, userMoney = 0, money = 0;
                con = DBConnectionManager.getInstance().getConn();
                ResultSet srs = OADBUtil.runQuerySql(salesql, con);
                if (srs.next()) {
                    saleMoney = srs.getFloat("salemoney");
                }
                ResultSet urs = OADBUtil.runQuerySql(usesql, con);
                if (urs.next()) {
                    userMoney = urs.getFloat("usemoney");
                }
                money = saleMoney - userMoney;
                Element rtRoot = rtdoc.addElement("DATAPACKET");
                rtRoot.addAttribute("SALEMONEY", String.valueOf(saleMoney));
                rtRoot.addAttribute("USEMONEY", String.valueOf(userMoney));
                rtRoot.addAttribute("MONEY", String.valueOf(money));
                ResultSet storers = OADBUtil.runQuerySql(storesql, con);
                String goodLittle = "0";
                if (storers.next()) {
                    goodLittle = storers.getString("little");
                }
                rtRoot.addAttribute("GOODLITTLE", goodLittle);
                ResultSet dayLazyeRs = OADBUtil.runQuerySql(daylazyesql, con);
                String daylazyeNum = "0";
                if (dayLazyeRs.next()) {
                    daylazyeNum = dayLazyeRs.getString("daylazye");
                }
                rtRoot.addAttribute("DAYLAZYE", daylazyeNum);
                ResultSet monthLazyeRs = OADBUtil.runQuerySql(monthLazyeSql, con);
                String monthlazyeNum = "0";
                if (monthLazyeRs.next()) {
                    monthlazyeNum = monthLazyeRs.getString("monthlazye");
                }
                rtRoot.addAttribute("MONTHLAZYE", monthlazyeNum);
                ResultSet outDayRs = OADBUtil.runQuerySql(outDaySql, con);
                String outdayNum = "0";
                if (outDayRs.next()) {
                    outdayNum = outDayRs.getString("outday");
                }
                rtRoot.addAttribute("OUTDAY", outdayNum);
                ResultSet spendingRs = OADBUtil.runQuerySql(spendingsql, con);
                String spendingNum = "0";
                if (spendingRs.next()) {
                    spendingNum = spendingRs.getString("money");
                }
                rtRoot.addAttribute("SPENDING", spendingNum);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    con.close();
                } catch (Exception ee) {
                }
            }
        }
        return rtdoc;
    }

    private static Document doChartData(Document doc) {
        Document rtdoc = null;
        if (null != doc) {
            rtdoc = bulidChartData(doc);
        }
        return rtdoc;
    }

    private static Document bulidChartData(Document doc) {
        Document rtdoc = DocumentHelper.createDocument();
        Element root = doc.getRootElement();
        String tblName = root.valueOf("@TABLENAME");
        String parameter = root.valueOf("@PARAMETER");
        String keyName = root.valueOf("@KEYNAME");
        String valfun = root.valueOf("@VALUEFUN");
        String where = root.valueOf("@WHERE");
        String code = root.valueOf("@CODE");
        String startDateStr = root.valueOf("@STARTDATE");
        String endDateStr = root.valueOf("@ENDDATE");
        Date startDate = DateTimeUtil.parse(startDateStr, DateTimeUtil.DEFAULT_DATE_SAMPLE_FORMAT);
        Date endDate = DateTimeUtil.parse(endDateStr, DateTimeUtil.DEFAULT_DATE_SAMPLE_FORMAT);
        Element rtroot = rtdoc.addElement("chart");
        StringBuffer bf = new StringBuffer(200);
        bf.append("select ").append(keyName).append(",").append(valfun).append(",code").append(" from ").append(tblName).append(" where ").append(where);
        Connection con = null;
        Element nameElt = (Element) root.selectSingleNode("name");
        try {
            con = DBConnectionManager.getInstance().getConn();
            Element meateElt = rtroot.addElement("meate");
            Element datasElt = rtroot.addElement("datas");
            Element dataElt = datasElt.addElement("data");
            String name = getName(nameElt, con);
            dataElt.addAttribute("name", name);
            ResultSet rs = OADBUtil.runQuerySql(bf.toString(), con);
            float total = 0;
            while (rs.next()) {
                String t_name = rs.getString(keyName);
                Date tEndDate = DateTimeUtil.parse(t_name, DateTimeUtil.DEFAULT_DATE_SAMPLE_FORMAT);
                if (startDate.before(tEndDate)) {
                    while (startDate.before(tEndDate)) {
                        Element melt = meateElt.addElement("mc");
                        String dateStr = DateTimeUtil.getDateTime(startDate, DateTimeUtil.DEFAULT_DATE_SAMPLE_FORMAT);
                        melt.addAttribute("code", dateStr);
                        melt.addAttribute("name", dateStr.substring(4, dateStr.length()));
                        startDate.setDate(startDate.getDate() + 1);
                    }
                }
                Element melt = meateElt.addElement("mc");
                String newName = "";
                if (t_name.length() > 4) newName = t_name.substring(4, t_name.length());
                melt.addAttribute("code", rs.getString(keyName));
                melt.addAttribute("name", newName);
                dataElt.addAttribute(rs.getString(keyName), rs.getString(parameter));
                total += rs.getFloat(parameter);
                startDate.setDate(startDate.getDate() + 1);
            }
            if (startDate.before(endDate)) {
                while (startDate.before(endDate)) {
                    Element melt = meateElt.addElement("mc");
                    String dateStr = DateTimeUtil.getDateTime(startDate, DateTimeUtil.DEFAULT_DATE_SAMPLE_FORMAT);
                    melt.addAttribute("code", dateStr);
                    melt.addAttribute("name", dateStr.substring(4, dateStr.length()));
                    startDate.setDate(startDate.getDate() + 1);
                }
                Element melt = meateElt.addElement("mc");
                String dateStr = DateTimeUtil.getDateTime(startDate, DateTimeUtil.DEFAULT_DATE_SAMPLE_FORMAT);
                melt.addAttribute("code", dateStr);
                melt.addAttribute("name", dateStr.substring(4, dateStr.length()));
            }
            dataElt.addAttribute("code", code);
            dataElt.addAttribute("total", String.valueOf(total));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception ee) {
            }
        }
        Dom4jUtil.writeDocToFile(rtdoc, "GBK", "j:/chart.xml");
        return rtdoc;
    }

    private static String getName(Element elt, Connection con) {
        String sql = elt.valueOf("@SQL");
        String name = null;
        try {
            ResultSet rs = OADBUtil.runQuerySql(sql, con);
            if (rs.next()) name = rs.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    private static Document saleFilter(Document doc) {
        Element root = doc.getRootElement();
        String code = root.valueOf("@CODE");
        String num = root.valueOf("@NUMBER");
        Connection con = null;
        try {
            con = DBConnectionManager.getInstance().getConn();
            Good good = getGoodByCode(code, con);
            root.addAttribute("NAME", good.getName());
            root.addAttribute("PRICE", String.valueOf(good.getOutPrice()));
        } catch (Exception e) {
            root.addAttribute("code", "error");
            root.addAttribute("message", "系统错误，请联系系统管理员！");
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception ee) {
            }
        }
        return doc;
    }

    private static Document updateSale(Document doc) {
        Document rtdoc = DocumentHelper.createDocument();
        Element root = rtdoc.addElement("DATAPACKET");
        Connection con = null;
        try {
            List goods = doc.selectNodes("/DATAPACKET/TABLEDATA/ROWDATA/ROW");
            Iterator it = goods.iterator();
            List sqlList = new ArrayList();
            con = DBConnectionManager.getInstance().getConn();
            String dateStr = DateTimeUtil.getDate(DateTimeUtil.DEFAULT_DATE_SAMPLE_FORMAT);
            while (it.hasNext()) {
                Element elt = (Element) it.next();
                String code = elt.valueOf("@CODE");
                String num = elt.valueOf("@NUMBER");
                String sql = "update t_good set NUMBER=NUMBER-" + num + " where CODE='" + code + "'";
                sqlList.add(sql);
                Good good = getSaleGoodByCode(code, dateStr, con);
                if (null == good) {
                    StringBuffer bf = new StringBuffer(300);
                    Good sgood = getGoodByCode(code, con);
                    bf.append("insert into t_sale_list(code,saledate,inprice,outprice,saleNumber) values('").append(code).append("','").append(dateStr).append("',").append(sgood.getInPrice()).append(",").append(sgood.getOutPrice()).append(",").append(num).append(")");
                    sqlList.add(bf.toString());
                } else {
                    String upsql = "update t_sale_list set saleNumber=saleNumber+" + num + " where code='" + code + "' and saledate=" + dateStr;
                    sqlList.add(upsql);
                }
            }
            OADBUtil.runBatchSQL(sqlList);
            root.addAttribute("STATUS", "1");
        } catch (Exception e) {
            root.addAttribute("STATUS", "-1");
            root.addAttribute("code", "error");
            root.addAttribute("message", "系统错误，请联系系统管理员！");
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception ee) {
            }
        }
        return rtdoc;
    }

    private static Document updateSale2(Document doc) {
        Document rtdoc = DocumentHelper.createDocument();
        Element root = rtdoc.addElement("DATAPACKET");
        Connection con = null;
        try {
            List goods = doc.selectNodes("/DATAPACKET/TABLEDATA/ROWDATA/ROW");
            Iterator it = goods.iterator();
            List sqlList = new ArrayList();
            con = DBConnectionManager.getInstance().getConn();
            String dateStr = DateTimeUtil.getDate(DateTimeUtil.DEFAULT_DATE_SAMPLE_FORMAT);
            while (it.hasNext()) {
                Element elt = (Element) it.next();
                String code = elt.valueOf("@CODE");
                String num = elt.valueOf("@NUMBER");
                String price = elt.valueOf("@PRICE");
                String sql = "update t_good set NUMBER=NUMBER-" + num + " where CODE='" + code + "'";
                sqlList.add(sql);
                Good good = getSaleGoodByCode(code, dateStr, con);
                if (null == good) {
                    StringBuffer bf = new StringBuffer(300);
                    Good sgood = getGoodByCode(code, con);
                    bf.append("insert into t_sale_list(code,saledate,inprice,outprice,saleNumber) values('").append(code).append("','").append(dateStr).append("',").append(sgood.getInPrice()).append(",").append(price).append(",").append(num).append(")");
                    sqlList.add(bf.toString());
                } else {
                    String upsql = "update t_sale_list set saleNumber=saleNumber+" + num + " ,outprice=(outprice*saleNumber+" + price + "*" + num + ")/(saleNumber+" + num + ") where code='" + code + "' and saledate=" + dateStr;
                    sqlList.add(upsql);
                }
            }
            OADBUtil.runBatchSQL(sqlList);
            root.addAttribute("STATUS", "1");
        } catch (Exception e) {
            root.addAttribute("STATUS", "-1");
            root.addAttribute("code", "error");
            root.addAttribute("message", "系统错误，请联系系统管理员！");
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (Exception ee) {
            }
        }
        return rtdoc;
    }

    private static Good getSaleGoodByCode(String code, String dateStr, Connection con) throws Exception {
        float price = 0;
        String sql = "select  saleNumber from t_sale_list where code=? and saledate=?";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, code);
        pstm.setString(2, dateStr);
        Good good = null;
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            good = new Good();
            good.setInPrice(rs.getInt("saleNumber"));
            good.setCode(code);
        }
        return good;
    }

    private static Good getGoodByCode(String code, Connection con) throws Exception {
        float price = 0;
        String sql = "select name, outprice,inprice from t_good where code=? ";
        PreparedStatement pstm = con.prepareStatement(sql);
        pstm.setString(1, code);
        Good good = new Good();
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            price = rs.getFloat("outprice");
            good.setOutPrice(price);
            good.setInPrice(rs.getFloat("inprice"));
            good.setName(rs.getString("name"));
        }
        return good;
    }
}
