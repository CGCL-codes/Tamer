package com.zubarev.htmltable;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import com.zubarev.htmltable.vo.HTMLTableConfig;

/**
 * @author Yuriy Zubarev
 * @deprecated Use HTMLTablePlugIn instead
 *
 */
public class HTMLTableServlet extends HttpServlet {

    protected Hashtable tables = new Hashtable();

    protected Hashtable blocks = new Hashtable();

    protected String config = "/WEB-INF/html-table.xml";

    protected String images = "/images/htmltable";

    protected int debug = 0;

    public void init() throws ServletException {
        super.init();
        System.out.println("HTMLTableServlet - init()");
        String value = null;
        value = getServletConfig().getInitParameter("debug");
        try {
            debug = Integer.parseInt(value);
        } catch (Throwable t) {
            debug = 0;
        }
        value = getServletConfig().getInitParameter("config");
        if (value != null) config = value;
        InputStream is = getServletContext().getResourceAsStream(config);
        if (is == null) throw new ServletException("Configuration file is missing");
        URL rulesURL = null;
        rulesURL = this.getClass().getResource("/com/zubarev/htmltable/html-table-rules.xml");
        if (rulesURL == null) {
            throw new ServletException("Rules configuration file for HTMLTable is missing");
        }
        try {
            Digester digester = DigesterLoader.createDigester(rulesURL);
            HTMLTableConfig htmlTableConfig = (HTMLTableConfig) digester.parse(is);
            tables = htmlTableConfig.getTables();
            blocks = htmlTableConfig.getBlocks();
            if (debug > 0) {
                System.out.println("HTMLTable configuration:");
                for (Enumeration e = tables.keys(); e.hasMoreElements(); ) {
                    System.out.println(tables.get(e.nextElement()));
                    System.out.println();
                }
                for (Enumeration e = blocks.keys(); e.hasMoreElements(); ) {
                    System.out.println(blocks.get(e.nextElement()));
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Exception during parsing configuration file");
        }
        getServletContext().setAttribute(Constants.TABLES_KEY, tables);
        getServletContext().setAttribute(Constants.BLOCKS_KEY, blocks);
        if (getServletConfig().getInitParameter("images") != null) {
            images = getServletConfig().getInitParameter("images");
        }
        getServletContext().setAttribute(Constants.HTMLTABLE_IMAGE_PATH, images);
    }
}
