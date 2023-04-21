package it.inrich.forum;

import it.inrich.forum.*;
import it.inrich.forum.util.CodeFormatter;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author riccardo
 * @version
 */
public class ForumManager extends HttpServlet {

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String alertMessage = null;
        String operation = request.getParameter("type");
        String testo = (String) request.getParameter("testo");
        testo = testo.replace("\'", "\\\'");
        testo = CodeFormatter.nl2br(testo);
        String oggetto = (String) request.getParameter("oggetto");
        oggetto = oggetto.replace("\'", "\\\'");
        User us = (User) request.getSession().getAttribute("user");
        ForumRoom rm = (ForumRoom) request.getSession().getAttribute("room");
        LinkedList rooms = (LinkedList) getServletContext().getAttribute("rooms");
        int owner = us.getUID();
        int pid = rm.getPid();
        Connection connection = (Connection) getServletContext().getAttribute("connection");
        Statement stm = null;
        try {
            if (operation.equals("nt")) {
                String qryThread = "SELECT nextval('s_thread')";
                stm = connection.createStatement();
                stm.execute("BEGIN TRANSACTION");
                ResultSet rs = stm.executeQuery(qryThread);
                rs.next();
                int nextThread = rs.getInt("nextval");
                String insert = "INSERT INTO thread VALUES (" + nextThread + ", '" + oggetto + "', now(), NULL, " + owner + ", " + pid + ", FALSE)";
                int inT = stm.executeUpdate(insert);
                String insMesg = "INSERT INTO message VALUES (nextval('s_message'), " + owner + ", " + nextThread + ", 0, NULL, 0, '" + testo + "', now(), NULL)";
                int inM = stm.executeUpdate(insMesg);
                stm.execute("COMMIT");
                String sql_th = "SELECT data_apertura, login_name, uid FROM thread, users WHERE utente_owner = users.uid AND tid = " + nextThread;
                rs = stm.executeQuery(sql_th);
                rs.next();
                Iterator itr = rooms.iterator();
                while (itr.hasNext()) {
                    ForumRoom fr = (ForumRoom) itr.next();
                    if (fr.getPid() == pid) {
                        fr.addThread(nextThread, 1, rs.getString("login_name"), rs.getDate("data_apertura"), null, false, oggetto);
                    }
                }
                alertMessage = "Nuovo thread inserito";
                rs.close();
            } else if (operation.equals("nm")) {
                String qryMsg = "SELECT nextval('s_message')";
                stm = connection.createStatement();
                ResultSet rs = stm.executeQuery(qryMsg);
                rs.next();
                int nextMessage = rs.getInt("nextval");
                ForumThread ft = (ForumThread) request.getSession().getAttribute("thread");
                String insert = "INSERT INTO message VALUES (" + nextMessage + ", " + owner + ", " + ft.getTid() + ", 0, " + ft.getFirstMessage().getMid() + ", 0, '" + testo + "', now(), NULL)";
                int inM = stm.executeUpdate(insert);
                alertMessage = "Nuovo messaggio inserito";
                String sel = "SELECT * FROM message, users WHERE uid = owner AND mid = " + nextMessage + " ";
                rs = stm.executeQuery(sel);
                while (rs.next()) {
                    User ownM = new User(rs.getInt("uid"), rs.getString("login_name"), rs.getString("passwd"), rs.getString("first_name"), rs.getString("last_name"));
                    ft.getMessages().add(new ForumMessage(nextMessage, ownM, rs.getTimestamp("data_inserimento"), rs.getTimestamp("data_modifica"), rs.getInt("thread"), rs.getInt("contatore_modifiche"), rs.getInt("parent"), rs.getString("testo")));
                    String last = "SELECT get_last_message_data(" + pid + ") AS data";
                    Statement ext = connection.createStatement();
                    ResultSet lastMD = ext.executeQuery(last);
                    lastMD.next();
                    String data = lastMD.getString("data");
                    Iterator itr = rooms.iterator();
                    while (itr.hasNext()) {
                        ForumRoom fr = (ForumRoom) itr.next();
                        if (fr.getPid() == pid) {
                            fr.setLastMessageData(data);
                        }
                    }
                    lastMD.close();
                }
                rs.close();
            } else {
                alertMessage = "Messaggio modificato";
            }
        } catch (SQLException se) {
            StringBuffer error = new StringBuffer("Si e' verificato un errore nell'esecuzione della stringa sql.\nStringa in esecuzione: ");
            error.append(se.getSQLState());
            error.append("\nMessaggio del database: ").append(se.getMessage());
            alertMessage = error.toString();
            try {
                stm.execute("ROLLBACK");
            } catch (SQLException s) {
            }
            request.getSession().setAttribute("alert", alertMessage);
            getServletContext().getRequestDispatcher(response.encodeURL("/operation_ko.jsp")).forward(request, response);
        }
        request.getSession().setAttribute("alert", alertMessage);
        getServletContext().getRequestDispatcher(response.encodeURL("/operation_ok.jsp")).forward(request, response);
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
