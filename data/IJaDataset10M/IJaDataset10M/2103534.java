package com.wwwc.admin.web.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.SQLException;
import com.wwwc.admin.web.database.*;
import com.wwwc.util.web.*;

public class adminDatabaseManager extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html");
            String user_ip = request.getRemoteAddr();
            HttpSession session = request.getSession(true);
            PrintWriter out = response.getWriter();
            ResourceBundle config = (ResourceBundle) session.getAttribute("Configuration");
            if (config == null) {
                return;
            }
            String databases = config.getString("3wcenter.databases");
            if (databases == null || databases.length() < 1) {
                System.out.println("adminDatabseManager:Error:From:" + user_ip);
                return;
            }
            String local_ips = config.getString("3wcenter.local_ips");
            if (user_ip == null || !user_ip.startsWith(local_ips)) {
                return;
            }
            String backup_home1 = config.getString("backup.home1");
            String action = request.getParameter("action");
            String query = null;
            String call_path = request.getContextPath() + request.getServletPath();
            String database = request.getParameter("database");
            String who = request.getParameter("who");
            if (who == null) {
                System.out.println("who==null");
                return;
            }
            adminDatabaseManagerBean abean = new adminDatabaseManagerBean();
            out.println(abean.listDatabases(config, call_path, database) + "<BR>");
            if (who.equals("m") && action == null) {
                out.println("<TABLE border=0 align=center height=500>");
                out.println("<TR><TD style='font-size:24pt' align='center'>");
                out.println("Please select a database.");
                out.println("</TD></TR></TABLE>");
            }
            if (database == null) {
                return;
            }
            if (who.equals("m")) {
                out.println(abean.databaseUtils(call_path, database) + "<BR>");
            }
            Database db = DatabaseAdapter.getDatabase(database, config);
            if (who.equals("m") && action != null && action.equals("list_table")) {
                out.println(abean.listTable(db, call_path, database));
            }
            if (who.equals("m") && action != null && action.equals("add_table")) {
                out.println(abean.addTable(request, db, call_path, database, databases));
            }
            if (who.equals("m") && action != null && action.equals("backup")) {
                String source_dir = config.getString("3wcenter.database." + database + ".data.dir");
                out.println(abean.backup(request, db, call_path, backup_home1, database, source_dir));
            }
            String max_rows = config.getString("3wcenter.content.max_rows");
            String table_name = request.getParameter("table_name");
            if (who.equals("m") && action != null && action.equals("drop_table")) {
                out.println(abean.deleteTable(request, call_path, db, database, table_name));
            }
            if (who.equals("m") && action != null && action.equals("table_info")) {
                out.println(abean.getTableInfo(db, table_name, true));
            }
            if (who.equals("m") && action.equals("view_edit_record")) {
                String page = (request.getParameter("pid") == null) ? "1" : request.getParameter("pid");
                out.println(abean.viewEditRecord(request, call_path, table_name, db, database, page, max_rows));
            }
            if (who.equals("m") && action != null && action.equals("query_form")) {
                String temp = request.getParameter("query");
                if (temp == null) {
                    temp = "";
                }
                out.println("<H1>SQL in [" + database + "]</H1>");
                out.println("<FORM method=post action=" + call_path + ">");
                out.println("<TABLE border=1 style='font-size: 10pt'>");
                out.println("<TR><TD><TEXTAREA name=query rows=5 cols=74>" + temp + "</TEXTAREA></TD></TR>");
                out.println("</TABLE>");
                out.println("<input type=hidden name=action value='excute_query'>");
                out.println("<input type=hidden name=database value=" + database + ">");
                out.println("<BR><BR><input type=submit name=B value='Submit' style='font-size: 8pt'>");
                out.println("</FORM>");
            }
            if (who.equals("m") && action != null && action.equals("excute_query")) {
                query = "" + request.getParameter("query");
                out.println(query);
                out.println("<H1>Query Excution Result</H1>");
                out.println("<TABLE border=1 style='font-size: 10pt' width=90%>");
                db = DatabaseAdapter.getDatabase(database, config);
                out.println("<TR><TD>Excute Query</TD><TD>" + query + "</TD>");
                out.println("<TD>Database</TD><TD>" + database + "</TD>");
                if ((query.toUpperCase()).startsWith("INSERT")) {
                    out.println("<TD>Result</TD><TD>" + db.insert(query) + "</TD></TR>");
                    out.println("<TR><TD>Message</TD><TD colspan=5>" + db.getMessage() + "&nbsp;</TD></TR>");
                } else if ((query.toUpperCase()).startsWith("DELETE")) {
                    out.println("<TD>Result</TD><TD>" + db.delete(query) + "</TD></TR>");
                    out.println("<TR><TD>Message</TD><TD colspan=5>" + db.getMessage() + "&nbsp;</TD></TR>");
                } else if ((query.toUpperCase()).startsWith("UPDATE")) {
                    out.println("<TD>Result</TD><TD>" + db.update(query) + "</TD></TR>");
                    out.println("<TR><TD>Message</TD><TD colspan=5>" + db.getMessage() + "&nbsp;</TD></TR>");
                } else {
                    out.println("<TD>Result</TD><TD>" + db.excute(query) + "</TD></TR>");
                    out.println("<TR><TD>Message</TD><TD colspan=5>" + db.getMessage() + "&nbsp;</TD></TR>");
                }
                out.println("</TABLE>");
            }
            if (who.equals("m") && action != null && action.equals("save_table_as")) {
                table_name = request.getParameter("table_name");
                out.println("<FORM method=post action=" + call_path + ">");
                out.println("<TABLE border=1  cellSpacing=0 cellPadding=2 style='font-size: 10pt' width=90%>");
                out.println("<TR><TD colspan=4 style='font-size: 14pt' align='center'>Save table data into file</TD></TR>");
                out.println("<TR><TD>Type</TD><TD>Default File Name</TD><TD>Location</TD></TR>");
                out.println("<TR><TD>File</TD>");
                out.println("<TD>" + table_name + "-" + MyDateAndTime.getCurrentDate(3) + ".txt</TD>");
                out.println("<TD>" + backup_home1 + "/sql/" + database + "</TD>");
                out.println("</TR>");
                out.println("<TR><TD colspan='3' align='center'><input type='submit' value='Save' style='font-size:8pt'></TR>");
                out.println("</TABLE>");
                out.println("<input type=hidden name=table_name value=" + table_name + "><BR>");
                out.println("<input type=hidden name=action value=save_data_to_file>");
                out.println("<input type=hidden name=database value=" + database + ">");
                out.println("</FORM>");
                out.println("<BR>");
                StringTokenizer tokens = new StringTokenizer(databases, ",");
                String temp = null;
                while (tokens.hasMoreTokens()) {
                    temp = tokens.nextToken();
                    out.println("<FORM method=post action=" + call_path + ">");
                    out.println("<TABLE border=1  cellSpacing=0 cellPadding=2 style='font-size: 10pt' width=90%>");
                    out.println("<TR><TD>Database</TD><TD>URL</TD><TD>Driver</TD><TD>Table Name</TD></TR>");
                    out.println("<TR><TD>" + temp + "</TD>");
                    out.println("<TD>" + config.getString("3wcenter.database." + temp + ".db_name") + "</TD>");
                    out.println("<TD>" + config.getString("3wcenter.database." + temp + ".driver_name") + "</TD>");
                    out.println("<TD><input type=text name='new_table_name' value='" + table_name + "' style='font-size:8pt'></TD>");
                    out.println("</TR>");
                    out.println("<TR><TD colspan='5' align='center'><input type='submit' value='Save' style='font-size:8pt'></TR>");
                    out.println("</TABLE>");
                    out.println("<input type=hidden name=action value=save_data_to_db>");
                    out.println("<input type=hidden name='target_db' value='" + temp + "'>");
                    out.println("<input type=hidden name=database value=" + database + ">");
                    out.println("<input type=hidden name='table_name' value='" + table_name + "'>");
                    out.println("</FORM>");
                    out.println("<BR>");
                }
            }
            if (who.equals("m") && action != null && action.equals("save_data_to_file")) {
                Vector vd = null;
                Vector vh = null;
                Vector vt = null;
                table_name = request.getParameter("table_name");
                db = DatabaseAdapter.getDatabase(database, config);
                query = "SELECT * FROM " + table_name;
                db.select(query);
                vd = db.getTableRecordVector();
                vh = db.getTableColumnNameVector();
                vt = db.getTableColumnTypeVector();
                DatabaseGenerateQuery dbgq = new DatabaseGenerateQuery();
                StringBuffer sbf = dbgq.getInsertQuery(table_name, vh, vt, vd);
                String error = null;
                if (sbf != null && (sbf.toString()).length() > 1) {
                    File f = new File(backup_home1 + "/sql/" + database);
                    if (!f.isDirectory()) {
                        f.mkdirs();
                    }
                    f = new File((backup_home1 + "/sql/" + database), table_name + "-" + MyDateAndTime.getCurrentDate(3) + ".txt");
                    if (f.exists()) {
                        error = "File:" + f.getPath() + " exists!";
                    }
                    if (error == null) {
                        MyFileWriter mf = new MyFileWriter();
                        mf.setFileName(f.getPath());
                        if (!mf.useBufferedOutputStream((sbf.toString()).replaceAll(";", ";\n"))) {
                            error = "Save file error:" + f.getPath();
                        }
                    }
                } else {
                    error = "DatabaseGenerateQuery:Error";
                }
                if (error == null) {
                    out.println(MyMessage.htmlMessage("Saved!"));
                } else {
                    out.println(MyMessage.htmlMessage(error));
                }
            }
            if (who.equals("m") && action != null && action.equals("save_data_to_db")) {
                Vector vd = null;
                Vector vh = null;
                Vector vt = null;
                table_name = request.getParameter("table_name");
                String new_table_name = request.getParameter("new_table_name");
                String target_db = request.getParameter("target_db");
                String error = null;
                if (table_name == null || target_db == null || new_table_name == null) {
                    error = "Error: table_name or target_db or new_table_name not selected.";
                }
                if (error == null) {
                    db = DatabaseAdapter.getDatabase(target_db, config);
                    query = "SELECT * FROM " + new_table_name;
                    if (db.select(query) != 0) {
                        error = "Table [" + new_table_name + "] not found in Database[" + target_db + "].";
                    }
                    if (error == null) {
                        DatabaseGenerateQuery dbgq = new DatabaseGenerateQuery();
                        db = DatabaseAdapter.getDatabase(database, config);
                        query = "SELECT * FROM " + table_name;
                        db.select(query);
                        vh = db.getTableColumnNameVector();
                        vt = db.getTableColumnTypeVector();
                        vd = db.getTableRecordVector();
                        StringBuffer sbf = dbgq.getInsertQuery(table_name, new_table_name, vh, vt, vd);
                        if (sbf != null) {
                            int result = -1;
                            query = sbf.toString();
                            if (query.length() > 0 && query.startsWith("INSERT")) {
                                db = DatabaseAdapter.getDatabase(target_db, config);
                                result = db.insert(query);
                                if (result != 1) {
                                    error = "<BR>[adminDatabaseManager][Error][" + query + "]";
                                } else {
                                    out.println("Table [" + table_name + "]'s data has been saved into [" + target_db + "]");
                                }
                            }
                        }
                        if (error != null) {
                            out.println(error);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("adminDatabaseManager:Exception:" + e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}
