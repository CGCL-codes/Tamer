package org.fao.waicent.kids.giews;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.fao.waicent.db.dbConnectionManager;
import org.fao.waicent.db.dbConnectionManagerPool;
import org.fao.waicent.kids.server.kidsSession;
import org.fao.waicent.util.Debug;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GIEWSCountryNewsList {

    private String path = "/WEB-INF/news.ini";

    private String database_ini = "";

    private Connection con = null;

    public GIEWSCountryNewsList() {
        System.out.println("[GIEWSCountryNewsList] In constructor");
    }

    public Element getXML(HttpServletRequest request) {
        DocumentBuilder builder = null;
        Document doc = null;
        Element output_xml = null;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = builder.newDocument();
            output_xml = doc.createElement("news");
        } catch (ParserConfigurationException pce) {
            System.out.println("[GIEWSCountryNewsList] ParserConfigurationException: " + pce.getMessage());
        }
        Element outputs_xml = doc.createElement("news_list");
        kidsSession kids = (kidsSession) request.getSession(true).getAttribute("kids");
        try {
            database_ini = request.getSession().getServletContext().getRealPath(path);
            Debug.println("database_ini: " + database_ini);
            con = popConnection();
            String ccode = kids.getMapContext().getSelectedMapCode();
            System.out.println("[GIEWSCountryNewsList] retrieving: " + ccode);
            String SQL = "";
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            SQL = "select distinct news_title, news_date, news.news_id from news, news_country where news_country.country_id = '" + ccode + "' and news_country.news_id = news.news_id order by news_date desc";
            pstmt = con.prepareStatement(SQL);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                output_xml = doc.createElement("news");
                output_xml.setAttribute("title", rs.getString(1));
                output_xml.setAttribute("date", rs.getString(2));
                output_xml.setAttribute("id", rs.getString(3));
                outputs_xml.appendChild(output_xml);
            }
            con.close();
        } catch (Exception e) {
            System.out.println("[GIEWSCountryNewsList]Exception: " + e.getMessage());
        }
        return outputs_xml;
    }

    private Connection popConnection() {
        dbConnectionManager manager = dbConnectionManagerPool.getConnectionManager(database_ini);
        con = manager.popConnection();
        return con;
    }

    private void pushConnection(Connection con) {
        dbConnectionManager manager = dbConnectionManagerPool.getConnectionManager(database_ini);
        manager.pushConnection(con);
    }
}
