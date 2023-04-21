package it.ilz.hostingjava.servlet;

import it.ilz.hostingjava.backend.DAOFactory;
import it.ilz.hostingjava.model.Ftpuser;
import it.ilz.hostingjava.util.HibernateHelper;
import it.ilz.hostingjava.util.MailHelper;
import it.ilz.hostingjava.util.Util;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Random;
import javax.servlet.*;
import javax.servlet.http.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author EU91206
 * @version
 */
public class Update extends HttpServlet {

    private Random random = new Random();

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String command = request.getParameter("command");
        String username = request.getParameter("username");
        if (command == null) {
            response.getWriter().println("comandi: 1=updateHome,2=updateContext,3=updateDBPassword,4=recreateFtpPassword");
            response.getWriter().println("parameters: command,username");
            return;
        }
        Util util = Util.getUtil();
        PrintWriter writer = response.getWriter();
        File ctxBase = new File(MainServlet.CTXBASE);
        if (!ctxBase.exists()) {
            writer.println("La cartella dei contesti non esiste " + MainServlet.CTXBASE + " non esiste");
            return;
        }
        File webappBase = new File(MainServlet.BASE);
        if (!webappBase.exists()) {
            writer.println("La home " + MainServlet.BASE + " non esiste");
            return;
        }
        if (username != null && !username.equals("")) {
            Connection connection = null;
            try {
                connection = HibernateHelper.getSessionFactory().openSession().connection();
                switch(Integer.parseInt(command)) {
                    case 1:
                        updateHome(username, writer, connection, util);
                        break;
                    case 2:
                        updateContext(username, writer, connection, util);
                        break;
                    case 3:
                        updateDBPassword(username, writer, connection, util);
                        break;
                    case 4:
                        recreateFtpuser(username, writer, util);
                        break;
                    default:
                        help(writer);
                        break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ex.printStackTrace(writer);
            } finally {
                try {
                    connection.close();
                } catch (Exception e) {
                }
            }
        } else {
            File[] ctxApps = ctxBase.listFiles();
            for (int i = 0; i < ctxApps.length; i++) {
                if (!ctxApps[i].isFile() || !ctxApps[i].getName().startsWith("-")) continue;
                username = ctxApps[i].getName().substring("-".length());
                username = username.substring(0, username.lastIndexOf('.'));
                Connection connection = null;
                try {
                    connection = HibernateHelper.getSessionFactory().openSession().connection();
                    writer.print("Elaborazione della applicazione: " + username);
                    switch(Integer.parseInt(command)) {
                        case 1:
                            updateHome(username, writer, connection, util);
                            break;
                        case 2:
                            updateContext(username, writer, connection, util);
                            break;
                        case 3:
                            updateDBPassword(username, writer, connection, util);
                            break;
                        case 4:
                            recreateFtpuser(username, writer, util);
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ex.printStackTrace(writer);
                } finally {
                    try {
                        connection.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        out.close();
    }

    private void help(PrintWriter writer) {
    }

    private void updateHome(String username, PrintWriter writer, Connection connection, Util util) throws ServletException, IOException {
        File apps = new File(MainServlet.BASE + File.separatorChar + username);
        if (!apps.exists()) {
            apps.mkdirs();
        }
        if (!apps.isDirectory()) return;
        try {
            StringWriter msg = new StringWriter();
            DAOFactory.getFSDAO().createHome(apps.getAbsolutePath(), username);
            DAOFactory.getFSDAO().updateHome(apps.getAbsolutePath(), username, 0);
            writer.println(" ---> ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace(writer);
        }
    }

    private void updateContext(String username, PrintWriter writer, Connection connection, Util util) throws ServletException, IOException {
        writer.print("Elaborazione della applicazione: " + username);
        try {
            String query = "select passwd from ftpuser where userid='" + username + "'";
            ResultSet rs = connection.prepareStatement(query).executeQuery();
            rs.next();
            String pwd = rs.getString(1);
            rs.close();
            query = "select has_database from userdata where userid='" + username + "'";
            rs.next();
            boolean hasDatabase = rs.getBoolean(1);
            rs.close();
            DAOFactory.getFSDAO().savectx(username, pwd, MainServlet.CTXBASE + File.separatorChar + '-' + username + ".xml", MainServlet.BASE, hasDatabase);
            writer.println(" ---> true");
        } catch (Exception ex) {
            ex.printStackTrace();
            ex.printStackTrace(writer);
        }
    }

    private void recreateFtpuser(String username, PrintWriter writer, Util util) throws ServletException, IOException {
        String password = Integer.toString(random.nextInt(1000000));
        writer.print("Ricreo password per: " + username + ':' + password);
        Ftpuser user = new Ftpuser().setAccessed(new Date()).setCount(new Integer(0)).setGid((short) 5500).setHomedir("/home/hostingjava.it/" + username).setId(new Integer(0)).setModified(new Date()).setPasswd(password).setShell("/sbin/nologin").setUid((short) 5500).setUserid(username);
        Session session = HibernateHelper.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(user);
            tx.commit();
            MailHelper.sendTemplateMail(username, "Aggiornamento password", "templates/updatePassword.html");
        } catch (Exception ex) {
            writer.print(ex.toString());
        } finally {
            session.close();
            writer.println();
        }
    }

    /**
     *aggiorna le password del db
     */
    private void updateDBPassword(String username, PrintWriter writer, Connection connection, Util util) throws ServletException, IOException {
        PreparedStatement ps = null;
        String query;
        String password = null;
        ResultSet rs = null;
        try {
            query = "select passwd from ftpuser where userid='" + username + "'";
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) password = rs.getString(1); else {
                System.out.println("ERR " + query);
            }
            try {
                query = "CREATE DATABASE " + username;
                ps = connection.prepareStatement(query);
                log(query);
                ps.execute();
            } catch (Exception e) {
                writer.print("  Il db esiste: " + e.getMessage());
            } finally {
                try {
                    ps.close();
                } catch (Throwable t) {
                }
            }
            query = "grant all privileges on " + username + ".* to '" + username + "'@'localhost' identified by '" + password + "'";
            ps = connection.prepareStatement(query);
            ps.execute(query);
            query = "grant all privileges on " + username + ".* to '" + username + "'@'127.0.0.1' identified by '" + password + "'";
            ps = connection.prepareStatement(query);
            ps.execute(query);
        } catch (Exception ex) {
            ex.printStackTrace();
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception rsex) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception psex) {
                }
            }
        }
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
