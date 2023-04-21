package com.wwwc.index.web.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.SQLException;
import com.wwwc.util.web.*;

public class FileToTableServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html");
            HttpSession session = request.getSession(true);
            String user_ip = request.getRemoteAddr();
            PrintWriter out = response.getWriter();
            String mid = (String) request.getAttribute("mid");
            if (mid == null) {
                System.out.println("FileToTable:mid=null");
                return;
            }
            String call_path = request.getContextPath() + request.getServletPath();
            String aid = request.getParameter("aid");
            String file_type = request.getParameter("file_type");
            FileToTable hftt = new FileToTable(call_path, mid);
            if (file_type == null) {
                out.println(hftt.getFileType());
                return;
            }
            if (file_type.equals("text")) {
                aid = "1-1";
            }
            if (file_type.equals("html")) {
                aid = "1-2";
            }
            if (file_type.equals("xml")) {
                aid = "1-3";
            }
            if (aid.equals("1-1")) {
                out.println(hftt.formatTextFile(request, file_type));
            }
            if (aid.equals("1-2")) {
                out.println(hftt.getHtmlFile(request));
            }
            if (aid != null && aid.equals("2-0")) {
                out.println(hftt.getSelectedData(request, "2-0"));
            }
            if (aid != null && aid.equals("2-1")) {
                Vector vrs = hftt.getSelectedRecordsV(request, "2-1");
                out.println(hftt.displaySelectedRecors(request, "2-1"));
            }
        } catch (Exception e) {
            System.out.println("FileToTableServlet:Error:" + e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        doPost(request, response);
    }
}
