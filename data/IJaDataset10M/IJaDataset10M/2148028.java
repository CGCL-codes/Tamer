package com.etc.bin;

import com.etc.bin.base.HttpBaseServlet;
import com.etc.bin.base.RequestManager;
import com.etc.bin.base.SessionManager;
import com.etc.bin.beans.UserLoginBeans;
import com.etc.controller.UserController;
import com.etc.controller.beans.UserBeans;
import com.etc.db.oracle.Connector;
import com.etc.util.SQL;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author MaGicBank
 * @version
 */
public class UserInfoFinder extends HttpBaseServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestManager req = new RequestManager(request);
        SessionManager session = new SessionManager(request);
        UserLoginBeans user = (UserLoginBeans) session.get("#USER#");
        String sort = SORTFIELD.equalsIgnoreCase(CODE) ? "CODE" : SORTFIELD.equalsIgnoreCase(NAME) ? "NAME" : "ID";
        if (req.command("") && user.r(UINF)) {
            int pageNumber = (req.getString("page").equalsIgnoreCase("")) ? 1 : Integer.parseInt(req.getString("page"));
            int pageTotal = 1;
            int rowCount = 0;
            String listTheme = "";
            Connection conn = Connector.getConnection();
            try {
                rowCount = SQL.rowCount("SELECT * FROM USER_INFO_V", conn);
                pageTotal = new Double(Math.ceil(new Integer(rowCount).doubleValue() / new Integer(rowLimit).doubleValue())).intValue();
                if (pageNumber > pageTotal) {
                    pageNumber = pageTotal;
                }
                int start = ((pageNumber - 1) * rowLimit) + 1;
                Statement stm = conn.createStatement();
                ResultSet raw = SQL.limit("SELECT * FROM USER_INFO_V ORDER BY " + sort + " " + SORTORDER, stm, start, rowLimit);
                while (raw.next()) {
                    UserBeans owner = UserController.getUser(raw.getInt("ID"));
                    HashMap<String, String> listMap = new HashMap<String, String>();
                    listMap.put(":V_USER_INFO_ID:", Integer.toString(owner.getId()));
                    listMap.put(":V_USER_INFO_CODE:", owner.getCode());
                    listMap.put(":V_USER_INFO_NAME:", owner.getName());
                    listMap.put(":V_USER_GROUP:", owner.getGname());
                    listMap.put(":V_USER_POSITION:", owner.getPname());
                    listMap.putAll(map);
                    listTheme += html.render("/user_info_chose_list.html", listMap);
                }
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
            map.put(":V_BEGIN:", "?page=1&act=" + req.getString("act"));
            map.put(":V_PREVIEW:", ((pageNumber - 1) < 1) ? "?page=1&act=" + req.getString("act") : "?page=" + (pageNumber - 1) + "&act=" + req.getString("act"));
            map.put(":V_NEXT:", ((pageNumber + 1) > pageTotal) ? "?page=" + pageTotal + "&act=" + req.getString("act") : "?page=" + (pageNumber + 1) + "&act=" + req.getString("act"));
            map.put(":V_END:", "?page=" + pageTotal + "&act=" + req.getString("act"));
            map.put(":V_JUMP_URL:", "?act=" + req.getString("act") + "&page=");
            String opt = html.getNullOption("") + getUserGroupOption("");
            map.put(":USER_GROUP_OPTION:", opt);
            opt = html.getNullOption("") + getUserPositionOption("");
            map.put(":USER_POSITION_OPTION:", opt);
            map.put(":V_ACT:", req.getString("act"));
            map.put(":DATA_LIST:", listTheme);
            map.put(":V_KEYWORD:", "");
            map.put(":V_ROW_COUNT:", Integer.toString(rowCount));
            map.put(":V_PAGE_NUMBER:", Integer.toString(pageNumber));
            map.put(":V_PAGE_TOTAL:", Integer.toString(pageTotal));
            content += html.render("/user_info_chose.html", map);
            html.output(content);
        } else if (req.command("FIND") && user.r(UINF)) {
            int pageNumber = (req.getString("page").equalsIgnoreCase("")) ? 1 : Integer.parseInt(req.getString("page"));
            int pageTotal = 1;
            int rowCount = 0;
            String listTheme = "";
            String ext = "";
            ext += req.getString("ugId").equalsIgnoreCase("0") ? "" : "AND GID = " + req.getString("ugId");
            ext += req.getString("upId").equalsIgnoreCase("0") ? "" : "AND PID = " + req.getString("upId");
            Connection conn = Connector.getConnection();
            try {
                rowCount = SQL.rowCount("SELECT * FROM USER_INFO_V WHERE (CODE LIKE '%" + req.getString("keyword") + "%' OR NAME LIKE '%" + req.getString("keyword") + "%') " + ext, conn);
                pageTotal = new Double(Math.ceil(new Integer(rowCount).doubleValue() / new Integer(rowLimit).doubleValue())).intValue();
                if (pageNumber > pageTotal) {
                    pageNumber = pageTotal;
                }
                int start = ((pageNumber - 1) * rowLimit) + 1;
                Statement stm = conn.createStatement();
                ResultSet raw = SQL.limit("SELECT * FROM USER_INFO_V WHERE (CODE LIKE '%" + req.getString("keyword") + "%' OR NAME LIKE '%" + req.getString("keyword") + "%') " + ext + " ORDER BY " + sort + " " + SORTORDER, stm, start, rowLimit);
                while (raw.next()) {
                    UserBeans owner = UserController.getUser(raw.getInt("ID"));
                    HashMap<String, String> listMap = new HashMap<String, String>();
                    listMap.put(":V_USER_INFO_ID:", Integer.toString(owner.getId()));
                    listMap.put(":V_USER_INFO_CODE:", owner.getCode());
                    listMap.put(":V_USER_INFO_NAME:", owner.getName());
                    listMap.put(":V_USER_GROUP:", owner.getGname());
                    listMap.put(":V_USER_POSITION:", owner.getPname());
                    listMap.putAll(map);
                    listTheme += html.render("/user_info_chose_list.html", listMap);
                }
                raw.close();
                stm.close();
            } catch (SQLException ex) {
                log4j.error("SQL Exception", ex);
            }
            Connector.close(conn);
            map.put(":V_BEGIN:", "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=1&act=" + req.getString("act"));
            map.put(":V_PREVIEW:", ((pageNumber - 1) < 1) ? "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=1&act=" + req.getString("act") : "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=" + (pageNumber - 1) + "&act=" + req.getString("act"));
            map.put(":V_NEXT:", ((pageNumber + 1) > pageTotal) ? "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=" + pageTotal + "&act=" + req.getString("act") : "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=" + (pageNumber + 1) + "&act=" + req.getString("act"));
            map.put(":V_END:", "?cmd=FIND&keyword=" + req.getString("keyword") + "&page=" + pageTotal + "&act=" + req.getString("act"));
            map.put(":V_JUMP_URL:", "?cmd=FIND&keyword=" + req.getString("keyword") + "&act=" + req.getString("act") + "&page=");
            String opt = html.getNullOption(req.getString("ugId")) + getUserGroupOption(req.getString("ugId"));
            map.put(":USER_GROUP_OPTION:", opt);
            opt = html.getNullOption(req.getString("upId")) + getUserPositionOption(req.getString("upId"));
            map.put(":USER_POSITION_OPTION:", opt);
            map.put(":V_ACT:", req.getString("act"));
            map.put(":DATA_LIST:", listTheme);
            map.put(":V_KEYWORD:", req.getString("keyword"));
            map.put(":V_ROW_COUNT:", Integer.toString(rowCount));
            map.put(":V_PAGE_NUMBER:", Integer.toString(pageNumber));
            map.put(":V_PAGE_TOTAL:", Integer.toString(pageTotal));
            content += html.render("/user_info_chose.html", map);
            html.output(content);
        } else {
            response.sendError(403);
        }
    }

    /** Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "MINERAL - User Finder";
    }
}
