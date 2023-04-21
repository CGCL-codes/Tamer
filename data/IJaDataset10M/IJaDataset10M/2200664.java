package org.fao.waicent.kids.giews.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;
import org.fao.waicent.db.dbConnectionManager;
import org.fao.waicent.db.dbConnectionManagerPool;
import org.fao.waicent.kids.editor.MailSender;
import org.fao.waicent.kids.server.kidsRequest;
import org.fao.waicent.kids.server.kidsResponse;
import org.fao.waicent.kids.server.kidsService;
import org.fao.waicent.kids.server.kidsServiceException;
import org.fao.waicent.kids.server.kidsSession;
import org.fao.waicent.util.Debug;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *  This class generates the index.htm and map.htm to be displayed on the
 *      GIEWS website.
 *
 *      index.htm     This display the tables containing the latest list of the
 *                    countries in crisis.
 *      map.htm       This page displays the updated map that contains the
 *                    countries in crisis painted with the appropriate color
 *                    relative the legend.
 */
public class GiewsWebsiteXMLData extends kidsService {

    private String database_ini;

    private static final String TYPE_1 = "Exceptional shortfall in aggregate food production/supplies";

    private static final String TYPE_2 = "Widespread lack of access";

    private static final String TYPE_3 = "Severe localized food insecurity";

    private String display_date = "";

    private String total = "";

    public GiewsWebsiteXMLData() {
    }

    public GiewsWebsiteXMLData(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public boolean execute(kidsRequest request, kidsResponse response) throws kidsServiceException {
        Debug.println("GiewsWebsiteXMLData START");
        boolean consumed = false;
        kidsSession session = request.getSession();
        database_ini = session.getConfiguration().getDBIni();
        String global_path = session.getConfiguration().getGlobalPath();
        String file_name = "\\\\faoint0b\\http\\WAICENT\\FAOINFO\\ECONOMIC\\GIEWS\\english\\hotspots" + File.separator + "index.htm";
        String file_map = "\\\\faoint0b\\http\\WAICENT\\FAOINFO\\ECONOMIC\\GIEWS\\english\\hotspots" + File.separator + "map.htm";
        Connection con = null;
        try {
            con = popConnection();
            saveFile(file_name, getDataXML(con));
            saveFile(file_map, getMapTextHML());
            sendEmail();
        } catch (Exception e) {
            Debug.println("GiewsWebsiteXMLData EXCEPTION: " + e.getMessage());
        } finally {
            pushConnection(con);
        }
        consumed = true;
        Debug.println("GiewsWebsiteXMLData END");
        return consumed;
    }

    public boolean undo(kidsRequest request, kidsResponse response) throws kidsServiceException {
        boolean consumed = false;
        return consumed;
    }

    /**
     *  This method reads from the DB the countries along with their
     *       Food Security related data related to the current date.
     *
     * @param     Connection       the DB connection
     *
     * @return    String           The text that contains all the information
     *                             to be displayed in the html page.
     *
     * @throws    Exception
     */
    private String getDataXML(Connection con) throws Exception {
        Debug.println(" GiewsWebsiteXMLData START ");
        String text = "";
        String query = "select a.Proj_Code, b.Gaul_Name, a.FoodInsec_Type, " + "a.FoodInsec_Reasons, a.FoodInsec_Date, " + "a.FoodInsec_UnfavorableCrops, " + "a.FoodInsec_ContributingFactors, b.Region " + "from foodinsecurity a, project_codes b " + "where month(a.FoodInsec_Date)=? " + "and year(a.FoodInsec_Date)=? " + "and a.Proj_Code = b.Gaul_Code " + "order by b.Gaul_Name asc";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int month = -1;
        int year = -1;
        String current_month = "";
        Calendar cal = new GregorianCalendar();
        java.util.Date date = new java.util.Date();
        cal.setTime(date);
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);
        String[] array_months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
        current_month = array_months[cal.get(Calendar.MONTH)];
        try {
            pstmt = con.prepareStatement(query);
            pstmt.setInt(1, month);
            pstmt.setInt(2, year);
            rs = pstmt.executeQuery();
            String current_date = current_month + " " + year;
            Vector africa = new Vector();
            Vector asia = new Vector();
            Vector america = new Vector();
            Vector europe = new Vector();
            Vector v_crops = new Vector();
            int ctr = 0;
            while (rs.next()) {
                String code = rs.getString(1);
                String country = rs.getString(2);
                String type = rs.getString(3);
                String reasons = rs.getString(4);
                String sdate = rs.getString(5);
                String crops = rs.getString(6);
                String factors = rs.getString(7);
                String region = rs.getString(8);
                if (!country.equalsIgnoreCase("World")) {
                    if ((region.contains("Africa") || region.contains("Great Lakes")) && (type != null && !type.equals(""))) {
                        ctr++;
                        africa.add(new FoodSecurity(code, country, region, type, reasons, crops, factors));
                    } else if ((region.contains("America") || region.contains("Caribbean")) && (type != null && !type.equals(""))) {
                        ctr++;
                        america.add(new FoodSecurity(code, country, region, type, reasons, crops, factors));
                    } else if ((region.contains("Asia") || region.contains("Near East") || region.contains("Common wealth of Independent States")) && (type != null && !type.equals(""))) {
                        ctr++;
                        asia.add(new FoodSecurity(code, country, region, type, reasons, crops, factors));
                    } else if (region.contains("Europe") && (type != null && !type.equals(""))) {
                        ctr++;
                        europe.add(new FoodSecurity(code, country, region, type, reasons, crops, factors));
                    }
                }
                if (crops != null && crops.equals("true")) {
                    v_crops.add(new FoodSecurity(code, country, region, type, reasons, crops, factors));
                }
            }
            rs.close();
            pstmt.close();
            String header = getHeader(current_date, Integer.toString(ctr));
            String text_africa = getAfricaText(africa);
            String text_asia = getAsiaText(asia);
            String text_america = getAmericaText(america);
            String text_europe = getEuropeText(europe);
            String text_crops = getCropsText(v_crops);
            text = header + text_africa + text_asia + text_america + text_europe + text_crops + getFooter();
        } catch (Exception e) {
            Debug.println("getDataXML EXCEPTION: " + e.getMessage());
            throw new Exception("GiewsWebsiteXMLData EXCEPTION: " + e.getMessage());
        }
        Debug.println(" GiewsWebsiteXMLData END ");
        return text;
    }

    /**
     *  This method composes the text entries for the footer of the html page.
     *
     * @return      String   The composed footer text
     *
     */
    private String getFooter() {
        String text = "</table><p>&nbsp;</p>" + "<table border=\"0\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\" width=\"80%\">" + "<tr><td><p align=\"justify\"><u><b>TERMINOLOGY</b></u></p><p>" + "<p align=\"justify\"><b>Countries in Crisis Requiring External Assistance</b> " + "are expected to lack the resources to deal with reported critical problems of food " + "insecurity. Food crises are nearly always due to a combination of factors, " + "but for the purpose of response planning, it is important to establish whether " + "the nature of food crises is <b>predominantly</b> related to lack of food " + "availability, limited access to food, or severe but localized problems. " + "Accordingly, the list of countries requiring external assistance is " + "organized into three broad, not mutually exclusive, categories:</p><ul>" + "<li><p align=\"justify\">Countries facing an <b>exceptional shortfall " + "in aggregate food production/supplies</b> as a result of crop failure, " + "natural disasters, interruption of imports, disruption of distribution, " + "excessive post-harvest losses, or other supply bottlenecks.</li>" + "<li><p align=\"justify\">Countries with <b>widespread lack of access</b>, " + "where a majority of the population is considered to be unable to " + "procure food from local markets, due to very low incomes, " + "exceptionally high food prices, or the inability to circulate " + "within the country. </li>" + "<li><p align=\"justify\">Countries with <b>severe localized food " + "insecurity</b> due to the influx of refugees, a concentration of " + "internally displaced persons, or areas with combinations of crop " + "failure and deep poverty.  </li></ul></p><p>" + "<b>Unfavourable Prospects for Current Crops:</b> Refer to " + "prospects of a shortfall in production of current crops as a " + "result of a reduction of the area planted and/or adverse weather " + "conditions, plant pests, diseases and other calamities which " + "indicate a need for close monitoring of the crops for the " + "remainder of the growing season.<P>" + "<u><b>Note</b></u>: The maps on the <a href=\"http://www.fao.org/giews/\">GIEWS homepage</a> " + "indicate countries with unfavourable crop prospects and/or those in Crisis Requiring External Assistance.</p>" + "</td></tr></table><table align=\"center\"><tr height=\"10\"><td ><img src=\"../imgs/space.gif\" " + "height=\"10\" border=\"0\"></td></tr>" + "<script type=\"text/javascript\" src=\"../common/footer_2.htm\"></script></table></form></BODY>";
        return text;
    }

    /**
     *  This method saves the string as html to the file name specified.
     *
     * @param     String           The complete path and name of the file.
     * @param     String           The text that is to be saved as html file.
     *
     */
    private void saveFile(String fileName, String text) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            Debug.println("[GiewsWebsiteXMLData] Saving file: " + fileName);
            out.write("<html>" + text + "</html>");
            out.close();
        } catch (IOException e) {
            Debug.println("[GiewsWebsiteXMLData] Caught an exception trying " + "to save the file: " + e.getMessage());
        }
    }

    private Connection popConnection() {
        Connection con = null;
        dbConnectionManager manager = dbConnectionManagerPool.getConnectionManager(database_ini);
        con = manager.popConnection();
        return con;
    }

    private void pushConnection(Connection con) {
        dbConnectionManager manager = dbConnectionManagerPool.getConnectionManager(database_ini);
        manager.pushConnection(con);
    }

    /**
     *  This method sends an email the appropriate recepients to notify that
     *       updates were made and files are needed to upload to the website's
     *       server.
     */
    private void sendEmail() {
        String from = "GIEWS_WORKSTATION";
        String to = "Web-Intranet-Updates@fao.org";
        String cc = "henri.josserand@fao.org;yanyun.li@fao.org;tanzila.mohammad@fao.org;maria.dalessandrobarchiesi@fao.org";
        String subject = "Food Security Status UPDATES";
        String path = "\\\\faoint0b\\http\\WAICENT\\FAOINFO\\ECONOMIC\\GIEWS\\english\\hotspots";
        String test_url = "http://www-data.fao.org/giews/english/hotspots/index.htm";
        String text = "Dear colleague. <br><br>Please kindly upload the folder below ASAP: <a href='" + path + "'> " + path + "</a>. <br><br> To preview the pages on the test server (before uploading), go to " + "<a href='" + test_url + "'> " + test_url + "</a>." + "  <br><br> Thanks a lot, <br> GIEWS Workstation Team";
        try {
            Debug.println("create mail sender");
            MailSender ms = new MailSender(from, to, cc, subject, text, true);
            Debug.println("call mail sender");
            ms.Send();
        } catch (Exception ex) {
            Debug.println("Could not send the automatic e-mail to the approving/requesting officer");
        }
    }

    /**
     *  This method composes the header of the html page.
     *
     * @param       String   The current date.
     * @param       String   The total number of countries in crisis.
     *
     * @return      String   The composed header text
     *
     */
    private String getHeader(String current_date, String total) {
        String header = "";
        display_date = current_date;
        this.total = total;
        header = "<HEAD>" + "<META http-equiv=\"Content-Type\" content=\"text/html\" charset=utf-8>" + "<title>FAO/GIEWS - COUNTRIES IN CRISIS REQUIRING EXTERNAL ASSISTANCE -  " + current_date + "</title>" + "<link rel=stylesheet href=\"../style.css\" type=\"text/css\"> <link rel=\"Stylesheet\" type=\"text/css\" href=\"../fs.css\">" + "<LINK href=\"../print.css\" type=\"text/css\" rel=\"stylesheet\" media=\"print\">" + "<SCRIPT type=\"text/javascript\" src=\"../search.js\" language=\"javascript\">" + "</SCRIPT>" + "<meta name=\"keywords\" content=\"Foodcrops, shortages, production, food aid, food supply, current agricultural situation, agriculture, agricultural, hunger, food security, trade, country-specific, early warning,  Geographic, Regional Info\">" + "</HEAD>" + "<BODY bgcolor=\"#ffffff\" text=\"#000000\" link=\"#400080\" vlink=\"#800080\" alink=\"#ff0000\"><A name=\"TopOfPage\"></A>" + "<script type=\"text/javascript\" src=\"../common/header_2.htm\"></script>" + "<td ><a class=\"topmenu\" href=\"index.htm\"><img src=\"../imgs/arabic_lang.gif\"  height=\"17\" border=\"0\"></a></td>" + "<td ><a class=\"topmenu\" href=\"index.htm\"><img src=\"../imgs/chinese_lang.gif\"  height=\"17\" border=\"0\"></a></td>" + "<td  align=\"center\" valign=\"middle\"><a class=\"topmenu\" href=\"index.htm\"><b>fran�ais</b></a></td>" + "<td>&nbsp;&nbsp;</td>" + "<td  align=\"center\" valign=\"middle\"><a class=\"topmenu\" href=\"index.htm\"><b>espa�ol</b></a></td></tr>" + "<tr height=\"2\"><td colspan=\"17\" bgcolor=\"#003366\"><img src=\"../imgs/blue.gif\" height=\"2\" border=\"0\"></td>" + "</tr></table></td></tr></table>" + "<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"750\" class=\"main\" align=\"center\" >" + "<tr height=\"10\"><td ><img src=\"../imgs/space.gif\" height=\"10\" border=\"0\"></td></tr>" + "<tr><td align=\"right\"><b>" + current_date + "</b> </td></tr></table>" + "<a name=\"1\"></a><h2 class=\"fs2\" align=\"center\">COUNTRIES IN CRISIS REQUIRING EXTERNAL ASSISTANCE (total: " + total + " countries)</h2><a name=\"t1\"></a>" + "<center><font color=\"red\"><b>Click on any country to see details in the GIEWS Workstation</b></font></center>";
        return header;
    }

    /**
     *  This method composes the text entries for african countries of the
     *       html page.
     *
     * @param       Vector   Contains list of african countries.
     *
     * @return      String   The composed african text
     *
     */
    private String getAfricaText(Vector africa) {
        Debug.println(" getAfricaText START ");
        String text = "<H4 align=\"center\"><u>AFRICA</u> (" + africa.size() + " countries)</H4>" + "<table border=\"0\" align=\"center\" width=\"60%\">" + "<tr><td align=\"right\"><a href=\"map.htm\" class=\"black\">-->Map View</a></td><td></td></tr></table>" + "<table border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" + "<tr><td width=\"240\"><b>Nature of Food Insecurity</b></td><td width=\"280\"><b>Main Reasons</b></td></tr>";
        String type_1 = "";
        String type_2 = "";
        String type_3 = "";
        boolean type1 = false;
        boolean type2 = false;
        boolean type3 = false;
        if (africa.size() <= 0) {
            return "";
        }
        for (int i = 0; i < africa.size(); i++) {
            FoodSecurity fs = (FoodSecurity) africa.get(i);
            if (fs.getType() == null) {
                continue;
            }
            if (fs.getType().equals(TYPE_1)) {
                type1 = true;
                type_1 = type_1 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            } else if (fs.getType().equals(TYPE_2)) {
                type2 = true;
                type_2 = type_2 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            } else if (fs.getType().equals(TYPE_3)) {
                type3 = true;
                type_3 = type_3 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            }
        }
        if (type1) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Exceptional shortfall in aggregate food production/supplies</b></td></tr>" + type_1;
        }
        if (type2) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Widespread lack of access</b></td><td></td></tr>" + type_2;
        }
        if (type3) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Severe localized food insecurity</b></td><td></td></tr>" + type_3;
        }
        text = text + "</table><a name=\"t2\"></a>";
        return text;
    }

    /**
     *  This method composes the text entries for asian countries of the
     *       html page.
     *
     * @param       Vector   Contains list of asian countries.
     *
     * @return      String   The composed asian text
     *
     */
    private String getAsiaText(Vector asia) {
        String text = "<H4 align=\"center\"><u>ASIA/NEAR EAST</u> (" + asia.size() + " countries)</H4>" + "<table border=\"0\" align=\"center\" width=\"60%\">" + "<tr><td></td><td></td></tr></table>" + "<table border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" + "<tr><td width=\"240\"><b>Nature of Food Insecurity</b></td><td width=\"280\"><b>Main Reasons</b></td></tr>";
        String type_1 = "";
        String type_2 = "";
        String type_3 = "";
        boolean type1 = false;
        boolean type2 = false;
        boolean type3 = false;
        if (asia.size() <= 0) {
            return "";
        }
        for (int i = 0; i < asia.size(); i++) {
            FoodSecurity fs = (FoodSecurity) asia.get(i);
            if (fs.getType() == null) {
                continue;
            }
            if (fs.getType().equals(TYPE_1)) {
                type1 = true;
                type_1 = type_1 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            } else if (fs.getType().equals(TYPE_2)) {
                type2 = true;
                type_2 = type_2 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            } else if (fs.getType().equals(TYPE_3)) {
                type3 = true;
                type_3 = type_3 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            }
        }
        if (type1) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Exceptional shortfall in aggregate food production/supplies</b></td></tr>" + type_1;
        }
        if (type2) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Widespread lack of access</b></td><td></td></tr>" + type_2;
        }
        if (type3) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Severe localized food insecurity</b></td><td></td></tr>" + type_3;
        }
        text = text + "</table><a name=\"t2\"></a>";
        return text;
    }

    /**
     *  This method composes the text entries for american countries in crisis
     *       of the html page.
     *
     * @param       Vector   Contains list of american countries.
     *
     * @return      String   The composed american text
     *
     */
    private String getAmericaText(Vector america) {
        String text = "<H4 align=\"center\"><u>LATIN AMERICA</u> (" + america.size() + " countries)</H4>" + "<table border=\"0\" align=\"center\" width=\"60%\">" + "<tr><td></td><td></td></tr></table>" + "<table border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" + "<tr><td width=\"240\"><b>Nature of Food Insecurity</b></td><td width=\"280\"><b>Main Reasons</b></td></tr>";
        String type_1 = "";
        String type_2 = "";
        String type_3 = "";
        boolean type1 = false;
        boolean type2 = false;
        boolean type3 = false;
        if (america.size() <= 0) {
            return "";
        }
        for (int i = 0; i < america.size(); i++) {
            FoodSecurity fs = (FoodSecurity) america.get(i);
            if (fs.getType() == null) {
                continue;
            }
            if (fs.getType().equals(TYPE_1)) {
                type1 = true;
                type_1 = type_1 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            } else if (fs.getType().equals(TYPE_2)) {
                type2 = true;
                type_2 = type_2 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            } else if (fs.getType().equals(TYPE_3)) {
                type3 = true;
                type_3 = type_3 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            }
        }
        if (type1) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Exceptional shortfall in aggregate food production/supplies</b></td></tr>" + type_1;
        }
        if (type2) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Widespread lack of access</b></td><td></td></tr>" + type_2;
        }
        if (type3) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Severe localized food insecurity</b></td><td></td></tr>" + type_3;
        }
        text = text + "</table><a name=\"t2\"></a>";
        return text;
    }

    /**
     *  This method composes the text entries for european countries in crisis
     *       of the html page.
     *
     * @param       Vector   Contains list of european countries.
     *
     * @return      String   The composed european text
     *
     */
    private String getEuropeText(Vector europe) {
        String text = "<H4 align=\"center\"><u>EUROPE</u> (" + europe.size() + " countries)</H4>" + "<table border=\"0\" align=\"center\" width=\"60%\">" + "<tr><td></td><td></td></tr></table>" + "<table border=\"1\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\">" + "<tr><td width=\"240\"><b>Nature of Food Insecurity</b></td><td width=\"280\"><b>Main Reasons</b></td></tr>";
        String type_1 = "";
        String type_2 = "";
        String type_3 = "";
        boolean type1 = false;
        boolean type2 = false;
        boolean type3 = false;
        if (europe.size() <= 0) {
            return "";
        }
        for (int i = 0; i < europe.size(); i++) {
            FoodSecurity fs = (FoodSecurity) europe.get(i);
            if (fs.getType() == null) {
                continue;
            }
            if (fs.getType().equals(TYPE_1)) {
                type1 = true;
                type_1 = type_1 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            } else if (fs.getType().equals(TYPE_2)) {
                type2 = true;
                type_2 = type_2 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            } else if (fs.getType().equals(TYPE_3)) {
                type3 = true;
                type_3 = type_3 + "<tr><td class=\"small\">" + "<a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\" href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getReasons() + "</td></tr>";
            }
        }
        if (type1) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Exceptional shortfall in aggregate food production/supplies</b></td></tr>" + type_1;
        }
        if (type2) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Widespread lack of access</b></td><td></td></tr>" + type_2;
        }
        if (type3) {
            text = text + "<tr><td width=\"240\" class=\"fs3\"><b>Severe localized food insecurity</b></td><td></td></tr>" + type_3;
        }
        text = text + "</table><a name=\"t2\"></a>";
        return text;
    }

    /**
     *  This method composes the text entries for countries having unfavorable
     *       crops prospects.
     *
     * @param       Vector   Contains list of countries.
     *
     * @return      String   The composed text
     *
     */
    private String getCropsText(Vector crops) {
        String text = "<p>&nbsp;</p><a name=\"2\"></a><h2 class=\"fs2\" align=\"center\">" + "COUNTRIES WITH UNFAVOURABLE PROSPECTS FOR CURRENT CROPS</h2>" + "<table border=\"0\" align=\"center\" width=\"60%\"><tr><td align=\"right\"><a href=\"map.htm#2\" class=\"black\">-->Map View</a></td><td></td></tr></table>" + "<table align=\"center\" border=\"1\" cellpadding=\"3\" cellspacing=\"0\">" + "<tr><td align=\"justified\" valign=\"top\" width=\"240\"><b>Country</b></td>" + "<td align=\"justified\" valign=\"top\" width=\"280\"><b>Main contributing factor</b></td></tr>";
        if (crops.size() <= 0) {
            return "";
        }
        for (int i = 0; i < crops.size(); i++) {
            FoodSecurity fs = (FoodSecurity) crops.get(i);
            text = text + "<tr><td class=\"small\"><a class=\"black\" target=\"_blank\" title=\"click to see details in the GIEWS Workstation\"href=\"http://www.fao.org/giews/workstation/country.jsp?code=" + fs.getCode() + "\">" + fs.getCountry() + "</a></td>" + "<td class=\"small\">" + fs.getFactors() + "</td></tr>";
        }
        return text;
    }

    /**
     *  This method composes the text to be able to display the updated maps
     *       according to the updates from the Food Security status. The maps
     *       displayed are the exact graphical representation of the table of
     *       list of countries in crisis. This text is then converted to html.
     *
     * @return    String           The text that contains all the information
     *                             to be displayed in the html page.
     *
     */
    private String getMapTextHML() {
        Debug.println(" getMapTextHML this.total: " + this.total);
        String text = "<html><HEAD><META http-equiv=\"Content-Type\" content=\"text/html\" " + "charset=utf-8><title>FAO/GIEWS - COUNTRIES IN CRISIS REQUIRING EXTERNAL ASSISTANCE -  March 2007 </title>" + "<link rel=stylesheet href=\"../style.css\" type=\"text/css\"> " + "<link rel=\"Stylesheet\" type=\"text/css\" href=\"../fs.css\">" + "<LINK href=\"../print.css\" type=\"text/css\" rel=\"stylesheet\" media=\"print\">" + "<SCRIPT type=\"text/javascript\" src=\"../search.js\" language=\"javascript\">" + "</SCRIPT><meta name=\"keywords\" content=\"Foodcrops, shortages, production, food aid, food supply, current agricultural situation, agriculture, agricultural, hunger, food security, trade, country-specific, early warning,  Geographic, Regional Info\">" + "</HEAD><BODY bgcolor=\"#ffffff\" text=\"#000000\" link=\"#400080\" vlink=\"#800080\" alink=\"#ff0000\">" + "<A name=\"TopOfPage\"></A><script type=\"text/javascript\" src=\"../common/header_2.htm\"></script>" + "<td ><a class=\"topmenu\" href=\"map.htm\"><img src=\"../imgs/arabic_lang.gif\"  height=\"17\" border=\"0\"></a></td>" + "<td ><a class=\"topmenu\" href=\"map.htm\"><img src=\"../imgs/chinese_lang.gif\"  height=\"17\" border=\"0\"></a></td>" + "<td  align=\"center\" valign=\"middle\"><a class=\"topmenu\" href=\"map.htm\"><b>français</b></a></td><td>&nbsp;&nbsp;</td>" + "<td  align=\"center\" valign=\"middle\"><a class=\"topmenu\" href=\"map.htm\"><b>español</b></a></td></tr>" + "<tr height=\"2\"><td colspan=\"17\" bgcolor=\"#003366\"><img src=\"../imgs/blue.gif\" height=\"2\" border=\"0\"></td></tr></table>" + "</td></tr></table><table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"750\" class=\"main\" align=\"center\">" + "<tr height=\"10\"><td ><img src=\"../imgs/space.gif\" height=\"10\" border=\"0\"></td></tr>" + "<tr><td align=\"right\"><b>" + display_date + "</b> </td></tr></table>" + "<table border=\"0\" align=\"center\" width=\"750\"><tr><td colspan=\"3\">" + "<a name=\"1\"></a><h2 class=\"fs2\" align=\"center\">" + "COUNTRIES IN CRISIS REQUIRING EXTERNAL ASSISTANCE <br>(total:" + this.total + " countries)</h2><a name=\"t1\"></a></td></tr>" + "<tr><td colspan=\"3\" align=\"right\" valign=\"bottom\"\">" + "<a href=\"index.htm\" class=\"black\">-->Table view</a></td></tr>" + "<tr><td colspan=\"3\" align=\"center\">" + "<a href=\"index.htm\"><img src=\"mapbig.jpg\"  alt=\"Click for table view\" border=\"0\"></a></td></tr>" + "<tr><td>&nbsp;&nbsp;<a class=\"black\"\"><img src=\"../imgs/red_b.gif\" border=\"0\">&nbsp;Shortfall in aggregate food <br>production/supplies</a></td>" + "<td><a class=\"black\"><img src=\"../imgs/brown_b.gif\" border=\"0\">&nbsp;Widespread lack of access</a></td>" + "<td><a class=\"black\"><img src=\"../imgs/yellow_b.gif\" border=\"0\">&nbsp;Severe localized food insecurity</a></td></tr>" + "<tr><td height=\"20\">&nbsp;&nbsp;</td><tr>" + "<tr><td colspan=\"3\"><a name=\"2\"></a><h2 class=\"fs2\" align=\"center\">" + "COUNTRIES WITH UNFAVOURABLE PROSPECTS FOR CURRENT CROPS</h2></td></tr>" + "<tr><td colspan=\"3\" align=\"right\" valign=\"bottom\"\">" + "<a href=\"index.htm#2\" class=\"black\">-->Table view</a></td></tr>" + "<tr><td colspan=\"3\" align=\"center\">" + "<a href=\"index.htm#2\"><img src=\"map2big.jpg\"  class=\"world_map\" alt=\"Click for table view\" border=\"0\"></td></tr>" + "<tr><td height=\"20\">&nbsp;&nbsp;</td><tr></table>" + "<table border=\"0\" align=\"center\" cellspacing=\"0\" cellpadding=\"3\" width=\"80%\">" + "<tr><td><p align=\"justify\"><u><b>TERMINOLOGY</b></u></p><p><p align=\"justify\">" + "<b>Countries in Crisis Requiring External Assistance</b> " + "are expected to lack the resources to deal with reported critical " + "problems of food insecurity. Food crises are nearly always due to " + "a combination of factors, but for the purpose of response planning, " + "it is important to establish whether the nature of food crises is " + "<b>predominantly</b> related to lack of food availability, " + "limited access to food, or severe but localized problems. " + "Accordingly, the list of countries requiring external assistance " + "is organized into three broad, not mutually exclusive, categories:</p>" + "<ul><li><p align=\"justify\">Countries facing an " + "<b>exceptional shortfall in aggregate food production/supplies</b> " + "as a result of crop failure, natural disasters, interruption of imports, " + "disruption of distribution, excessive post-harvest losses, or other supply bottlenecks." + "</li><li><p align=\"justify\">Countries with " + "<b>widespread lack of access</b>, where a majority of " + "the population is considered to be unable to procure " + "food from local markets, due to very low incomes, " + "exceptionally high food prices, or the inability to " + "circulate within the country. </li><li>" + "<p align=\"justify\">Countries with " + "<b>severe localized food insecurity</b> due to the " + "influx of refugees, a concentration of internally " + "displaced persons, or areas with combinations of crop " + "failure and deep poverty.  </li></ul></p>" + "<p><b>Unfavourable Prospects for Current Crops:</b> " + "Refer to prospects of a shortfall in production of " + "current crops as a result of a reduction of the area " + "planted and/or adverse weather conditions, plant pests, " + "diseases and other calamities which indicate a need " + "for close monitoring of the crops for the remainder of " + "the growing season.<P><u><b>Note</b></u>: The maps on " + "the <a href=\"http://www.fao.org/giews/\">" + "GIEWS homepage</a> indicate countries with unfavourable " + "crop prospects and/or those in Crisis Requiring External " + "Assistance.</p></td></tr></table><table align=\"center\">" + "<tr height=\"10\"><td ><img src=\"../imgs/space.gif\" " + "height=\"10\" border=\"0\"></td></tr>" + "<script type=\"text/javascript\" src=\"../common/footer_2.htm\">" + "</script></table></form></BODY></html>";
        return text;
    }
}

class FoodSecurity {

    String country;

    String code;

    String type;

    String reasons;

    String sdate;

    String crops;

    String factors;

    String region;

    public FoodSecurity(String code, String country, String region, String type, String reasons, String crops, String factors) {
        this.code = code;
        this.country = country;
        this.region = region;
        this.type = type;
        this.reasons = reasons;
        this.crops = crops;
        this.factors = factors;
    }

    public String getCountry() {
        return this.country;
    }

    public String getCode() {
        return this.code;
    }

    public String getRegion() {
        return this.region;
    }

    public String getType() {
        return this.type;
    }

    public String getReasons() {
        return this.reasons;
    }

    public String getCrops() {
        return this.crops;
    }

    public String getFactors() {
        return this.factors;
    }
}
