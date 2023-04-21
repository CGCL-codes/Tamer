package spidr.webapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import wdc.dbaccess.ApiException;
import wdc.dbaccess.ConnectionPool;
import wdc.settings.Settings;

/**
 *
 */
public class GetMetadata extends HttpServlet {

    private static final long serialVersionUID = -6690056251986066639L;

    private Logger log = Logger.getLogger("spidr.webapp.GetMetadata");

    private static final HashMap<String, HashMap<String, String>> _MAPPINGS = new HashMap<String, HashMap<String, String>>();

    /**
	 * 
	 */
    static {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("stations_query", "SELECT id,name FROM cri_stations ORDER BY id");
        map.put("table", "CRImin");
        map.put("vo_prefix", "CriStations");
        map.put("station", "DENV");
        _MAPPINGS.put("cri", map);
        map = new HashMap<String, String>();
        map.put("stations_query", "SELECT stn,stnName FROM stations ORDER BY stn");
        map.put("table", "Iono");
        map.put("vo_prefix", "IonoStations");
        map.put("station", "BC840");
        _MAPPINGS.put("iono", map);
        map = new HashMap<String, String>();
        map.put("stations_query", "SELECT stn,name FROM stations ORDER BY stn");
        map.put("table", "Geom");
        map.put("vo_prefix", "GeomStations");
        map.put("station", "BOU");
        _MAPPINGS.put("geom", map);
    }

    /**
	 * @param out
	 * @param req
	 * @throws ApiException
	 * @throws SQLException
	 */
    private void getTierOneMapping(PrintWriter out, HttpServletRequest req) throws ApiException, SQLException {
        Connection con = ConnectionPool.getConnection("metadata");
        PreparedStatement prep = con.prepareStatement("SELECT theme,platform FROM platform_vo_mapping ORDER BY theme ASC");
        ResultSet rs = prep.executeQuery();
        StringBuffer mapping = new StringBuffer();
        String url = req.getRequestURL().toString();
        while (rs.next()) {
            String theme = rs.getString(1);
            String platform = rs.getString(2);
            mapping.append("<tr><td>");
            mapping.append("<a href='" + url + "?describe&param=" + theme + "'>" + theme + "</a>");
            mapping.append("</td><td>");
            mapping.append(platform);
            mapping.append("</td></tr>");
        }
        out.println("<html>");
        out.println("<body>");
        out.println("<p>");
        out.println("This is a brief summary of the available parameters, and lists the components of the 'param=' argument.<br/>");
        out.println("The platform value varies based on the theme value.<br/>");
        out.println("For example, iono. accepts ionosonde stations like 'BC840', whereas geom. accepts geomagnetic stations like 'BOU' <br/>");
        out.println("You can find more information by further querying this service, going to the <a href='http://spidr.ngdc.noaa.gov/spidrvo/'>SPIDR Virtual Observatory</a>, or reading the <a href='http://spidr.ngdc.noaa.gov/spidr/friend.do?hlink=services.jsp'>Web Services Users Guide</a><br/><br/>");
        out.println("Subsequent describe queries allow you to determine available stations, <br/>for example <a href='" + url + "?describe&param=iono'>" + url + "?describe&param=iono</a><br/><br/>");
        out.println("Additionally, as a convenience, the 'theme' column links to each subsequent query<br/><br/><br/>");
        out.println("<table border='1' bordercolor='#FF6600' style='background-color:#FFFFFF' width='30%' cellpadding='3' cellspacing='3'>");
        out.println("<tr><td><b><i>theme</b></i></td><td><b><i>platform</b></i></td></tr>");
        out.println(mapping.toString());
        out.println("</table></p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
	 * @param key
	 * @param out
	 * @param req
	 * @throws ApiException
	 * @throws SQLException
	 */
    private void getTierTwoMapping(String key, PrintWriter out, HttpServletRequest req) throws ApiException, SQLException {
        Connection con = ConnectionPool.getConnection(_MAPPINGS.get(key).get("table"));
        PreparedStatement prep = con.prepareStatement(_MAPPINGS.get(key).get("stations_query"));
        ResultSet rs = prep.executeQuery();
        StringBuffer stations = new StringBuffer();
        String voPrefix = Settings.get("locations.metadataServlet");
        String voSuffix = "viewdata.do?docname=" + _MAPPINGS.get(key).get("vo_prefix");
        String vo = voPrefix + voSuffix;
        String dataUrl = req.getRequestURL().toString();
        while (rs.next()) {
            String id = rs.getString(1);
            String name = rs.getString(2);
            stations.append("<tr><td>");
            stations.append("<a href='" + dataUrl + "?param=" + key + "." + id + "'>" + id + "</a>");
            stations.append("</td><td>");
            stations.append(name);
            stations.append("</td><td>");
            stations.append("<a target='_blank' href='" + vo + id + "'>SPIDR-VO</a>");
            stations.append("</td></tr>");
        }
        out.println("<html>");
        out.println("<body>");
        out.println("<p>");
        out.println("This helper exists to help describe available metadata.<br/> The table provides available stations, and links to the metadata itself.<br/>");
        out.println("The result of combining your theme '" + key + "' with a value from the ID column will get you all available metadata for that station in XML format, <br/>for example <a target='_blank' href='" + dataUrl + "?param=" + key + "." + _MAPPINGS.get(key).get("station") + "'>" + dataUrl + "?param=" + key + "." + _MAPPINGS.get(key).get("station") + "</a><br/><br/><br/>");
        out.println("<table border='1' bordercolor='#FF6600' style='background-color:#FFFFFF' width='30%' cellpadding='3' cellspacing='3'>");
        out.println("<tr><td><b><i>ID</b></i></td><td><b><i>Name</b></i></td><td><b><i>VO Link</b></i></td></tr>");
        out.println(stations.toString());
        out.println("</table></p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
	 * @param out
	 * @param req
	 * @throws ApiException
	 * @throws SQLException
	 */
    private void getTierTwoCriMapping(PrintWriter out, HttpServletRequest req) throws ApiException, SQLException {
        getTierTwoMapping("cri", out, req);
    }

    /**
	 * @param out
	 * @param req
	 * @throws ApiException
	 * @throws SQLException
	 */
    private void getTierTwoIonoMapping(PrintWriter out, HttpServletRequest req) throws ApiException, SQLException {
        getTierTwoMapping("iono", out, req);
    }

    /**
	 * @param out
	 * @param req
	 * @throws ApiException
	 * @throws SQLException
	 */
    private void getTierTwoGeomMapping(PrintWriter out, HttpServletRequest req) throws ApiException, SQLException {
        getTierTwoMapping("geom", out, req);
    }

    /**
	 * @param out
	 * @param message
	 */
    private void getTierThreeMapping(PrintWriter out, String message) {
        out.println("<html>");
        out.println("<body>");
        out.println("<p>");
        out.println(message);
        out.println("</p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
	 * @param out
	 * @param param
	 * @param urlPrefix
	 * @throws ApiException
	 * @throws SQLException
	 */
    private void getTierThreeCriMapping(PrintWriter out, String param, String urlPrefix) throws ApiException, SQLException {
        StringBuffer message = new StringBuffer();
        message.append("There isn't a third tier for describing cosmic ray station metadata, unlike for data.<br/>");
        message.append("Perhaps you meant to make this same query of the data service ?<br/><br/>");
        message.append("<a href='" + urlPrefix + "?describe&param=" + param + "'>" + urlPrefix + "?describe&param=" + param + "</a>");
        getTierThreeMapping(out, message.toString());
    }

    /**
	 * @param out
	 * @param param
	 * @param urlPrefix
	 * @throws ApiException
	 * @throws SQLException
	 */
    private void getTierThreeIonoMapping(PrintWriter out, String param, String urlPrefix) throws ApiException, SQLException {
        StringBuffer message = new StringBuffer();
        message.append("There isn't a third tier for describing ionosonde station metadata, unlike for data.<br/>");
        message.append("Perhaps you meant to make this same query of the data service ?<br/><br/>");
        message.append("<a href='" + urlPrefix + "?describe&param=" + param + "'>" + urlPrefix + "?describe&param=" + param + "</a>");
        getTierThreeMapping(out, message.toString());
    }

    /**
	 * @param out
	 * @param param
	 * @param urlPrefix
	 * @throws ApiException
	 * @throws SQLException
	 */
    private void getTierThreeGeomMapping(PrintWriter out, String param, String urlPrefix) throws ApiException, SQLException {
        StringBuffer message = new StringBuffer();
        message.append("There isn't a third tier for describing geomagnetic station metadata, unlike for data.<br/>");
        message.append("Perhaps you meant to make this same query of the data service ?<br/><br/>");
        message.append("<a href='" + urlPrefix + "?describe&param=" + param + "'>" + urlPrefix + "?describe&param=" + param + "</a>");
        getTierThreeMapping(out, message.toString());
    }

    /**
	 * @param theme
	 * @param param
	 * @return
	 */
    private boolean isThemeStation(String theme, String param) {
        if (param == null) {
            return false;
        }
        if (param.indexOf('.') < 0) {
            return false;
        }
        if (param.startsWith(theme)) {
            return true;
        }
        return false;
    }

    /**
	 * @param param
	 * @return
	 */
    private boolean isCriStation(String param) {
        return (isThemeStation("cri", param));
    }

    /**
	 * @param param
	 * @return
	 */
    private boolean isIonoStation(String param) {
        return (isThemeStation("iono", param));
    }

    /**
	 * @param param
	 * @return
	 */
    private boolean isGeomStation(String param) {
        return (isThemeStation("geom", param));
    }

    /**
	 * @param theme
	 * @param param
	 * @return
	 */
    private boolean isTheme(String theme, String param) {
        if (param == null) {
            return false;
        }
        if (param.indexOf('.') >= 0) {
            return false;
        }
        if (param.startsWith(theme)) {
            return true;
        }
        return false;
    }

    /**
	 * @param param
	 * @return
	 */
    private boolean isCri(String param) {
        return (isTheme("cri", param));
    }

    /**
	 * @param param
	 * @return
	 */
    private boolean isIono(String param) {
        return (isTheme("iono", param));
    }

    /**
	 * @param param
	 * @return
	 */
    private boolean isGeom(String param) {
        return (isTheme("geom", param));
    }

    /**
	 * This method is responsible for outputting a legend/capabilities document. It's intended for client developers
	 * and users, so they may see what their options are, and ask subsequent questions about the web service.
	 * 
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
    public void doGetMapping(HashMap<String, String> args, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            res.setContentType("text/html");
            PrintWriter out = res.getWriter();
            res.setHeader("Content-Language", "en");
            String param = args.get("param");
            String url = req.getRequestURL().toString();
            url = url.replace("Metadata", "Data");
            if (param == null) {
                getTierOneMapping(out, req);
            } else if (isIono(param)) {
                getTierTwoIonoMapping(out, req);
            } else if (isIonoStation(param)) {
                getTierThreeIonoMapping(out, param, url);
            } else if (isGeom(param)) {
                getTierTwoGeomMapping(out, req);
            } else if (isGeomStation(param)) {
                getTierThreeGeomMapping(out, param, url);
            } else if (isCri(param)) {
                getTierTwoCriMapping(out, req);
            } else if (isCriStation(param)) {
                getTierThreeCriMapping(out, param, url);
            } else {
                getTierOneMapping(out, req);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
	 *
	 */
    private void getCatalogCSV(HttpServletResponse res) {
        try {
            res.setContentType("text/plain");
            res.setHeader("Content-Language", "en");
            PrintWriter out = res.getWriter();
            out.println("not,yet,implemented");
            out.flush();
        } catch (IOException iox) {
            iox.printStackTrace();
        }
    }

    /**
         *
         */
    private String yyyymo(String target) {
        String response = target;
        if (target.length() == 5) {
            String yr = target.substring(0, 4);
            String mo = target.substring(4, 5);
            response = yr + "0" + mo;
        }
        return response + "01";
    }

    /**
	 *
	 */
    private void getCatalogXML(HttpServletResponse res) {
        try {
            StringBuffer sb = new StringBuffer();
            res.setContentType("application/xml");
            res.setHeader("Content-Language", "en");
            sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n");
            sb.append("<TS_Catalog xmlns=\"http://spidr.ngdc.noaa.gov\">\n");
            GetDataDescribe gdd = new GetDataDescribe();
            String[] params = gdd.getParameterOnlyCSV();
            if (params == null) {
                return;
            }
            for (int i = 0; i < params.length; i++) {
                String[] tables = gdd.findTable(params[i]);
                for (int z = 0; z < tables.length; z++) {
                    String table = tables[z];
                    Connection con2 = ConnectionPool.getConnection("metadata");
                    PreparedStatement prep3 = con2.prepareStatement("SELECT `elem` FROM translate WHERE param = ?");
                    prep3.setString(1, params[i]);
                    ResultSet rs3 = prep3.executeQuery();
                    rs3.next();
                    String element = rs3.getString(1);
                    PreparedStatement prep2 = con2.prepareStatement("SELECT `description`,`missingValue`,`units` FROM elements_descr WHERE element = ? AND elemTable = ?");
                    prep2.setString(1, element);
                    prep2.setString(2, table);
                    ResultSet rs2 = prep2.executeQuery();
                    rs2.next();
                    String descrip = "";
                    String missing = "";
                    String units = "";
                    try {
                        descrip = rs2.getString(1);
                        missing = rs2.getString(2);
                        units = rs2.getString(3);
                        rs2.close();
                    } catch (SQLException x) {
                        rs2.close();
                        con2.close();
                        continue;
                    }
                    if (gdd.get(table, "has_tier_two").equalsIgnoreCase("no")) {
                        String from = "";
                        String to = "";
                        Connection con4 = ConnectionPool.getConnection(gdd.get(table, "tier_three_table"));
                        PreparedStatement prep4 = con4.prepareStatement(gdd.get(table, "tier_three_query"));
                        if (gdd.get(table, "tier_three_needs_parameter").equalsIgnoreCase("yes")) {
                            prep4.setString(1, params[i]);
                        }
                        ResultSet rs4 = prep4.executeQuery();
                        rs4.next();
                        from = rs4.getString(1);
                        to = rs4.getString(2);
                        rs4.close();
                        con4.close();
                        if (from == null || to == null) {
                            continue;
                        }
                        from = yyyymo(from);
                        to = yyyymo(to);
                        sb.append("<Parameter>");
                        sb.append("<Id>" + params[i] + "</Id>");
                        sb.append("<LongName>" + descrip + "</LongName>");
                        sb.append("<Units>" + units + "</Units>");
                        sb.append("<MissingValue>" + missing + "</MissingValue>");
                        sb.append("<TimeInterval>");
                        sb.append("<Start>" + from + "</Start>");
                        sb.append("<End>" + to + "</End>");
                        sb.append("</TimeInterval>");
                        sb.append("</Parameter>\n");
                        continue;
                    }
                    if (gdd.get(table, "has_stations").equalsIgnoreCase("yes") && gdd.get(table, "has_sections").equalsIgnoreCase("no")) {
                        Connection con1 = ConnectionPool.getConnection(table);
                        PreparedStatement prep = con1.prepareStatement(gdd.get(table, "tier_two_query"));
                        ResultSet rs = prep.executeQuery();
                        while (rs.next()) {
                            String station = rs.getString(1);
                            String from = "";
                            String to = "";
                            Connection con4 = ConnectionPool.getConnection(gdd.get(table, "tier_three_table"));
                            PreparedStatement prep4 = con4.prepareStatement(gdd.get(table, "tier_three_query"));
                            prep4.setString(1, station);
                            ResultSet rs4 = prep4.executeQuery();
                            rs4.next();
                            from = rs4.getString(1);
                            to = rs4.getString(2);
                            rs4.close();
                            con4.close();
                            if (from == null || to == null) {
                                continue;
                            }
                            from = yyyymo(from);
                            to = yyyymo(to);
                            sb.append("<Parameter>");
                            sb.append("<Id>" + params[i] + "." + station + "</Id>");
                            sb.append("<LongName>" + descrip + "</LongName>");
                            sb.append("<Units>" + units + "</Units>");
                            sb.append("<MissingValue>" + missing + "</MissingValue>");
                            sb.append("<TimeInterval>");
                            sb.append("<Start>" + from + "</Start>");
                            sb.append("<End>" + to + "</End>");
                            sb.append("</TimeInterval>");
                            sb.append("</Parameter>\n");
                        }
                        rs.close();
                        con1.close();
                    } else if (gdd.get(table, "has_stations").equalsIgnoreCase("yes") && gdd.get(table, "has_sections").equalsIgnoreCase("yes")) {
                        Connection con1 = ConnectionPool.getConnection(table);
                        PreparedStatement prep = con1.prepareStatement(gdd.get(table, "tier_two_query"));
                        PreparedStatement prepT = con2.prepareStatement("SELECT DISTINCT(platform) FROM translate WHERE param = ?");
                        prepT.setString(1, params[i]);
                        ResultSet rsT = prepT.executeQuery();
                        while (rsT.next()) {
                            String platform = rsT.getString(1);
                            if (platform == null || platform.equalsIgnoreCase("NULL")) {
                                continue;
                            }
                            ResultSet rs = prep.executeQuery();
                            while (rs.next()) {
                                String station = rs.getString(1);
                                String from = "";
                                String to = "";
                                Connection con4 = ConnectionPool.getConnection(gdd.get(table, "tier_three_table"));
                                PreparedStatement prep4 = con4.prepareStatement(gdd.get(table, "tier_three_query"));
                                prep4.setString(1, station);
                                ResultSet rs4 = prep4.executeQuery();
                                rs4.next();
                                from = rs4.getString(1);
                                to = rs4.getString(2);
                                rs4.close();
                                con4.close();
                                if (from == null || to == null) {
                                    continue;
                                }
                                from = yyyymo(from);
                                to = yyyymo(to);
                                sb.append("<Parameter>");
                                if (platform.equalsIgnoreCase("*")) {
                                    sb.append("<Id>" + params[i] + "." + station + "</Id>");
                                } else {
                                    sb.append("<Id>" + params[i] + "." + platform + "." + station + "</Id>");
                                }
                                sb.append("<LongName>" + descrip + "</LongName>");
                                sb.append("<Units>" + units + "</Units>");
                                sb.append("<MissingValue>" + missing + "</MissingValue>");
                                sb.append("<TimeInterval>");
                                sb.append("<Start>" + from + "</Start>");
                                sb.append("<End>" + to + "</End>");
                                sb.append("</TimeInterval>");
                                sb.append("</Parameter>\n");
                            }
                            rs.close();
                        }
                        rsT.close();
                        con1.close();
                    } else {
                        String t2q = gdd.get(table, "tier_two_query");
                        Connection con1 = ConnectionPool.getConnection("metadata");
                        PreparedStatement prep = con1.prepareStatement(t2q);
                        prep.setString(1, params[i]);
                        ResultSet rs = prep.executeQuery();
                        while (rs.next()) {
                            String platform = rs.getString(1);
                            String from = "";
                            String to = "";
                            String t3t = gdd.get(table, "tier_three_table");
                            String t3q = gdd.get(table, "tier_three_query");
                            if (t3t == null || t3t.equalsIgnoreCase("") || t3q == null || t3q.equalsIgnoreCase("")) {
                                continue;
                            }
                            Connection con4 = ConnectionPool.getConnection(t3t);
                            PreparedStatement prep4 = con4.prepareStatement(t3q);
                            ResultSet rs4 = prep4.executeQuery();
                            rs4.next();
                            from = rs4.getString(1);
                            to = rs4.getString(2);
                            rs4.close();
                            con4.close();
                            if (from == null || to == null) {
                                continue;
                            }
                            from = yyyymo(from);
                            to = yyyymo(to);
                            sb.append("<Parameter>");
                            sb.append("<Id>" + params[i] + "." + platform + "</Id>");
                            sb.append("<LongName>" + descrip + "</LongName>");
                            sb.append("<Units>" + units + "</Units>");
                            sb.append("<MissingValue>" + missing + "</MissingValue>");
                            sb.append("<TimeInterval>");
                            sb.append("<Start>" + from + "</Start>");
                            sb.append("<End>" + to + "</End>");
                            sb.append("</TimeInterval>");
                            sb.append("</Parameter>\n");
                        }
                        rs.close();
                        con1.close();
                    }
                    con2.close();
                }
            }
            sb.append("</TS_Catalog>\n");
            res.setHeader("Content-Length", String.valueOf(sb.length()));
            PrintWriter out = res.getWriter();
            out.print(sb.toString());
            out.flush();
        } catch (IOException iox) {
            iox.printStackTrace();
        } catch (SQLException sqlx) {
            sqlx.printStackTrace();
        } catch (wdc.dbaccess.ApiException apix) {
            apix.printStackTrace();
        }
    }

    /**
	 * @param format - string which specifies the output format of the time series set catalog
	 */
    public void doGetCatalog(String format, HttpServletResponse res) {
        if (format == null || format.equalsIgnoreCase("")) {
            getCatalogCSV(res);
        } else if (format.equalsIgnoreCase("xml")) {
            getCatalogXML(res);
        } else if (format.equalsIgnoreCase("csv")) {
            getCatalogCSV(res);
        } else {
            getCatalogCSV(res);
        }
    }

    /**
	 * 
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            HashMap<String, String> args = GetData.parseQueryString(req);
            if (args.size() == 0 || args.containsKey("describe")) {
                doGetMapping(args, req, res);
                return;
            }
            String catalog = args.get("ts_catalog");
            if (catalog != null) {
                doGetCatalog(catalog, res);
                return;
            }
            String param = args.get("param");
            String[] paramParts = null;
            if (param != null) {
                paramParts = param.split("\\.");
                switch(paramParts.length) {
                    case 1:
                        doGetParam(args, req, res);
                        break;
                    case 2:
                        doGetPlatform(args, req, res);
                        break;
                    default:
                        throw new Exception("Wrong parameter format");
                }
            } else {
                throw new Exception("Parameter missing");
            }
        } catch (Exception e) {
            log.error(e.toString());
            throw new ServletException(e);
        }
    }

    /**
	 * @param args
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
    public void doGetParam(HashMap<String, String> args, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/xml");
        res.setHeader("Content-Language", "en");
        PrintWriter out = res.getWriter();
        String param = args.get("param");
        try {
            Connection con = null;
            try {
                con = ConnectionPool.getConnection("metadata");
                String spidrServerName = Settings.get("sites.localSite");
                String spidrServerUrl = Settings.get("sites." + spidrServerName + ".url");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT docname FROM param_vo_mapping WHERE param = '" + param + "'");
                if (rs.next()) {
                    String docname = rs.getString(1);
                    String fileUrl = spidrServerUrl + (spidrServerUrl.endsWith("/") ? "" : "/") + "osproxy.do?specialRequest=document&docId=" + docname;
                    URL url = new URL(fileUrl);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) out.println(inputLine);
                    in.close();
                }
            } finally {
                ConnectionPool.releaseConnection(con);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /**
	 * @param args
	 * @param req
	 * @param res
	 * @throws ServletException
	 * @throws IOException
	 */
    public void doGetPlatform(HashMap<String, String> args, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/xml");
        res.setHeader("Content-Language", "en");
        PrintWriter out = res.getWriter();
        String[] paramParts = args.get("param").split("\\.");
        try {
            Connection con = null;
            try {
                con = ConnectionPool.getConnection("metadata");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT viewGroup, station FROM platform_vo_mapping WHERE theme = '" + paramParts[0] + "' AND (platform = '" + paramParts[1] + "' OR platform = '*')");
                if (rs.next()) {
                    String group = rs.getString(1);
                    String station = rs.getString(2);
                    if ("*".equals(station)) station = paramParts[1];
                    String spidrServerName = Settings.get("sites.localSite");
                    String spidrServerUrl = Settings.get("sites." + spidrServerName + ".url");
                    String metadataCollection = Settings.get("viewGroups." + group + ".metadataCollection");
                    String fileUrl = spidrServerUrl + (spidrServerUrl.endsWith("/") ? "" : "/") + "osproxy.do?specialRequest=document&docId=" + metadataCollection + station;
                    URL url = new URL(fileUrl);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) out.println(inputLine);
                    in.close();
                }
            } finally {
                ConnectionPool.releaseConnection(con);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
