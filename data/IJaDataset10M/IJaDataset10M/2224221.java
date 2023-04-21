package utility;

import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.sql.DataSource;

public class InsertRecord extends HttpServlet {

    private static final long serialVersionUID = 9222177154133363975L;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PreparedStatement pstm = null;
        Connection cn = null;
        String idView = request.getParameter("idview");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/fisr");
            cn = ds.getConnection();
            String[][] campi = ViewsXML.getEditableFields(idView, false);
            String table = ViewsXML.getAttribute(idView, "name");
            String fieldSQL = "";
            String valuesSQL = "";
            Enumeration parameters = request.getParameterNames();
            Vector<String> param_values = new Vector<String>();
            Vector<String> param_names = new Vector<String>();
            while (parameters.hasMoreElements()) {
                String param = (String) parameters.nextElement();
                if (!((param.equals("idview")) || (param.equals("pk_value")))) {
                    param_names.add(param);
                    param_values.add(request.getParameter(param));
                    fieldSQL += param.substring(param.lastIndexOf(".") + 1, param.length()) + ", ";
                    valuesSQL += "?, ";
                }
            }
            fieldSQL = fieldSQL.substring(0, fieldSQL.length() - 2);
            valuesSQL = valuesSQL.substring(0, valuesSQL.length() - 2);
            String sql = "INSERT INTO " + table + " (" + fieldSQL + ") VALUES (" + valuesSQL + ")";
            pstm = cn.prepareStatement(sql);
            for (int i = 0; i < param_names.size(); i++) {
                for (int j = 0; j < campi[2].length; j++) {
                    if (campi[2][j].equals(param_names.get(i))) {
                        if (campi[3][j].equals("double")) {
                            if (param_values.get(i).equals("NULL")) {
                                pstm.setNull(i + 1, java.sql.Types.DOUBLE);
                            } else {
                                double value = Double.parseDouble(param_values.get(i));
                                pstm.setDouble(i + 1, value);
                            }
                        } else if (campi[3][j].equals("boolean")) {
                            if (param_values.get(i).equals("NULL")) {
                                pstm.setNull(i + 1, java.sql.Types.BOOLEAN);
                            } else {
                                boolean value = (param_values.get(i).equals("on")) ? true : false;
                                pstm.setBoolean(i + 1, value);
                            }
                        } else if ((campi[3][i].equals("counter")) || (campi[3][i].equals("int"))) {
                            if (param_values.get(i).equals("NULL")) {
                                pstm.setNull(i + 1, java.sql.Types.INTEGER);
                            } else {
                                int value = Integer.parseInt(param_values.get(i));
                                pstm.setInt(i + 1, value);
                            }
                        } else if (campi[3][j].equals("date")) {
                            if (param_values.get(i).equals("NULL")) {
                                pstm.setNull(i + 1, java.sql.Types.DATE);
                            } else {
                                Date value = Date.valueOf(param_values.get(i));
                                pstm.setDate(i + 1, value);
                            }
                        } else if (campi[3][j].equals("varchar")) {
                            if (param_values.get(i).equals("NULL")) {
                                pstm.setNull(i + 1, java.sql.Types.VARCHAR);
                            } else {
                                pstm.setString(i + 1, param_values.get(i));
                            }
                        }
                    }
                }
            }
            int flag = pstm.executeUpdate();
            out.println(flag);
        } catch (Exception e) {
            e.printStackTrace(out);
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (cn != null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace(out);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Insert record";
    }
}
