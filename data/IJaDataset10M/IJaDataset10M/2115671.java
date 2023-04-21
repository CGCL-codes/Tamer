package com.solucionjava.servlet.ajax;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.solucionjava.db.ConnectDB;
import java.util.*;

/**
 * Servlet para regresar los valores de una lista de seleccion automaticas con
 * Ajax usando la libreria javascript de Script.aculo.us.
 * 
 * 
 * @author Cedric Simon
 * 
 * @version Ver version de la clase ConnectDB
 * 
 * 
 */
public abstract class AjaxList extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ArrayList<String[]> resultList = new ArrayList<String[]>();

    protected ConnectDB readDB;

    /**
	 * Constructor por defecto.
	 */
    protected void initConnection() {
        try {
            readDB = new ConnectDB(this.getServletName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Validación eventual de seguridad.
	 * <p>
	 * Ejemplo: <br>
	 * if (session.isNew() || session.getAttribute("noUser") == null) return
	 * "Login"; // Error de seguridad, mandar a la pagina de entrada<br>
	 * else if ((Byte)session.getAttribute("ADMIN")==0 && localSQL!=null &&<br>
	 * localSQL.equals("verClave")) <br>
	 * return "SecurityError"; //Alguna pagina de error de seguridad, por
	 * ejemplo.<br>
	 * else return ""; // OK
	 * 
	 * @param session
	 *            HttpSession
	 * @param localSQL
	 *            Nombre de la consulta
	 * @return Cadna vacia si OK, URL de pagina de error en caso de falla de
	 *         seguridad.
	 */
    protected abstract String checkSecurity(HttpSession session, String localSQL);

    /**
	 * Liga un nombre de consulta con una sentenciia SQL, con el fin de impedir
	 * SQL Injections.
	 * 
	 * 
	 * Ejemplo: if (localSQL.equals("city")) localSQL =
	 * "select no_city, city_name, concat('-->',region) from city "; return
	 * localSQL;
	 * 
	 * @param localSQL
	 *            Nombre de la consulta
	 * @param session
	 *            HttpSession
	 * @return Consulta SQL de base, a la cual se agregara eventualmente la
	 *         clausula where.
	 */
    protected abstract String translateSql(String localSQL, HttpSession session);

    /**
	 * doGet reenvia a doPost por defecto.
	 */
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    /**
	 * Codigo principal.
	 */
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        req.setCharacterEncoding("ISO-8859-1");
        String localSQL = req.getParameter("localSQL");
        String url = checkSecurity(session, localSQL);
        if (!url.equals("")) {
            resp.sendRedirect(url);
            return;
        }
        String sql = checkSql(localSQL, session);
        int limitRows = 60;
        readDB = null;
        try {
            initConnection();
            if (req.getParameter("debug") != null) {
                System.out.println(req.getParameter("ajaxSQL"));
                System.out.println(req.getParameter("value"));
            }
            String ajaxSQL = sql + (req.getParameter("ajaxSQL").replaceAll("@", "%").replaceAll("#", "'"));
            if (req.getParameter("debug") != null) System.out.println(ajaxSQL);
            resultList.clear();
            if (req.getParameter("nolimit") == null) limitRows = 60; else if (req.getParameter("nolimit").equalsIgnoreCase("SI")) {
                limitRows = 9999;
            }
            readDB.selectFirstX(ajaxSQL, limitRows + 1);
            int cnt = 0;
            while (readDB.getNext()) {
                cnt++;
                if (cnt > limitRows) {
                    resultList.add(0, new String[] { "-1", "<font color=red size=1>Lista limitada a " + limitRows + " valores</font>", "" });
                    break;
                }
                String[] val = new String[] { readDB.getStringHTML(1), readDB.getStringHTML(2), "" };
                try {
                    val[2] = readDB.getStringHTML(3);
                    if (req.getParameter("debug") != null && req.getParameter("debug").equals("full")) System.out.println(val[2]);
                } catch (java.sql.SQLException ex) {
                }
                resultList.add(val);
            }
            if (resultList.size() == 0 || resultList == null) {
                resultList.add(new String[] { "-1", "??-- No existe !" });
            }
            String xmlString = getXMLData(resultList);
            this.writeResponse(resp, xmlString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (readDB != null) readDB.cleanup();
                readDB = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            readDB = null;
        }
    }

    private void writeResponse(HttpServletResponse resp, String output) throws IOException {
        resp.setContentType("text/xml");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Content", "text/html;charset=iso-8859-1");
        resp.getWriter().write(output);
    }

    private String getXMLData(ArrayList<String[]> data) {
        String xmlString = "";
        xmlString = xmlString + "<ul>";
        for (int i = 0; i < data.size(); i++) {
            xmlString = xmlString + "<li id='" + data.get(i)[0].toString() + "'>" + data.get(i)[1].toString() + ((data.get(i).length < 3 || data.get(i)[2] == null || (data.get(i)[2]).equals("")) ? "" : "<br/><span class='addInfo'>" + data.get(i)[2] + "</span>") + "</li>";
        }
        xmlString = xmlString + "</ul>";
        return xmlString;
    }

    private String checkSql(String localSQL, HttpSession session) {
        String sql = "";
        if (localSQL == null) {
            return sql;
        }
        sql = translateSql(localSQL, session);
        return sql;
    }
}
