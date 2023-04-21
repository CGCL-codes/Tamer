package com.ufnasoft.dms.server;

import com.ufnasoft.dms.server.index.DOMUtil;
import com.ufnasoft.dms.server.database.*;
import com.ufnasoft.dms.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import javax.sql.*;
import java.sql.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ServerLogin extends InitDMS {

    Logger logger1 = Logger.getLogger(ServerLogin.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        setHeader(res);
        String sql = "";
        Statement stmt;
        ResultSet rs;
        String username = req.getParameter("user");
        String password = req.getParameter("password");
        int userid = 0;
        StringBuffer sb = new StringBuffer("<dms>");
        Database db = new Database();
        userid = db.getUserId(username, password);
        if (userid > 0) {
            Login login = new Login();
            StringBuffer dbuser = login.createUserKey(username, password);
            sb.append(dbuser);
        } else {
            sb.append("<rvalue>");
            sb.append("no");
            sb.append("</rvalue>");
        }
        sb.append("</dms>");
        out.print(sb.toString());
    }
}
