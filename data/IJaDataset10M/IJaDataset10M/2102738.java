package mecca.lcms.tunnel;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mecca.lcms.ScoData;
import mecca.lcms.ScoDataFactory;
import mecca.lcms.VetStringAnalyzer;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class PrerequisitesServlet extends HttpServlet {

    private String userid = "";

    private String username = "";

    private String courseid = "";

    private String scoId = "";

    private String curScoId = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("application/x-java-serialized-object");
            HttpSession session = request.getSession(true);
            InputStream in = request.getInputStream();
            ObjectInputStream obinput = new ObjectInputStream(in);
            Object object = obinput.readObject();
            if (object != null) {
                String str = (String) object;
                int i = str.indexOf("=");
                curScoId = str.substring(0, i);
                scoId = str.substring(i + 1);
                session.setAttribute("sco_id", curScoId);
            }
            userid = (String) session.getAttribute("_portal_login");
            courseid = (String) session.getAttribute("courseid");
            OutputStream outstr = response.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(outstr);
            oos.writeObject(getPreqStatus(session, request, response));
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Hashtable getPreqStatus(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        try {
            Vector scos = new Vector();
            String ops = "";
            String s = "";
            for (int i = 0; i < scoId.length(); i++) {
                char c = scoId.charAt(i);
                if (c == '&' || c == '|') {
                    ops += c == '&' ? "1" : "0";
                    scos.addElement(s);
                    s = "";
                } else {
                    String str = String.valueOf(c);
                    if (str != null) s += str;
                }
            }
            scos.addElement(s);
            Vector scoItems = (Vector) session.getAttribute("scoItems");
            Hashtable idenHash = (Hashtable) scoItems.elementAt(2);
            Vector preqScos = new Vector();
            String _bit = "";
            for (int i = 0; i < scos.size(); i++) {
                String iden = (String) scos.elementAt(i);
                String pre_sco = (String) idenHash.get(iden);
                preqScos.addElement(pre_sco);
                ScoData db = ScoDataFactory.get();
                Hashtable data = db.get(userid, courseid, pre_sco);
                String value = (String) data.get("cmi.core.lesson_status");
                _bit += value != null ? "passed completed".indexOf(value) >= 0 ? "1" : "0" : "0";
            }
            session.setAttribute("preq_scos", preqScos);
            session.setAttribute("preq_scos_status", _bit);
            session.setAttribute("preq_scos_relation", ops);
            Hashtable result = new Hashtable();
            Vector list = new Vector();
            if (VetStringAnalyzer.doProcess(_bit + "-" + ops, list) == 1) {
                result.put("preq_met", "true");
            } else {
                result.put("preq_met", "false");
                String server_name = request.getServerName();
                int server_port = request.getServerPort();
                if (server_port != 80) server_name += ":" + server_port;
                String uri = request.getRequestURI();
                String s1 = uri.substring(1);
                String appname = s1.substring(0, s1.indexOf("/"));
                result.put("page", "http://" + server_name + "/" + appname + "/c2/mecca.lcms.modules.PreRequisitesModule");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
