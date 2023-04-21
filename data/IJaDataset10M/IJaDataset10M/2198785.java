package servlets;

import database.DBConnection;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.CommonUtil;

/**
 *
 * @author Isak Rabin
 */
public class ListPatient extends HttpServlet {

    private static final long serialVersionUID = -1037373238454018517L;

    /**
         * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            DataInputStream din = new DataInputStream(request.getInputStream());
            DataOutputStream dout = new DataOutputStream(response.getOutputStream());
            String doctor_ic = "";
            try {
                doctor_ic = CommonUtil.getStringNull(din.readUTF());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            con = DBConnection.getConnection();
            String sql = "" + " select " + "       patient_id, " + "       patient_name, " + "       patient_ic, " + "       patient_age, " + "       patient_address, " + "       ifnull(patient_phone,'-') as patient_phone, " + "       ifnull(patient_email,'-') as patient_email, " + "       patient_pwd, " + "       ifnull(patient_splcondtn,'NA') as patient_splcondtn, " + "       ifnull(patient_emgncy_name,'NA')  as patient_emgncy_name, " + "       ifnull(patient_emgncy_contact,'NA')  as patient_emgncy_contact," + "       ifnull(patient_nationality,'NA') as patient_nationality, " + "       ifnull(patient_gender,'NA') as patient_gender, " + "       ifnull(patient_citz_sts,'NA') as patient_citz_sts, " + "       ifnull(patient_sts,'NA') as patient_sts, " + "       ifnull(patient_dob,'NA') as patient_dob " + " from " + "       patient " + " where " + "       patient_id = any (" + "               select distinct app_patient_id from appointment where app_doctor_id = any ( " + "                       select doctor_id from doctor where ucase(trim(doctor_ic)) = ucase(trim(?))" + "               )" + "       )";
            System.out.println("ListPatient SQL = " + sql);
            stmt = con.prepareStatement(sql);
            stmt.setString(1, doctor_ic);
            rs = stmt.executeQuery();
            if (rs.next()) {
                rs.last();
                int rows = rs.getRow();
                dout.writeInt(rows);
                rs.first();
                do {
                    dout.writeUTF(rs.getString("patient_id"));
                    dout.writeUTF(rs.getString("patient_name"));
                    dout.writeUTF(rs.getString("patient_ic"));
                    dout.writeInt(rs.getInt("patient_age"));
                    dout.writeUTF(rs.getString("patient_address"));
                    dout.writeUTF(rs.getString("patient_phone"));
                    dout.writeUTF(rs.getString("patient_email"));
                    dout.writeUTF(rs.getString("patient_pwd"));
                    dout.writeUTF(rs.getString("patient_splcondtn"));
                    dout.writeUTF(rs.getString("patient_emgncy_name"));
                    dout.writeUTF(rs.getString("patient_emgncy_contact"));
                    dout.writeUTF(rs.getString("patient_nationality"));
                    String gender = rs.getString("patient_gender");
                    boolean isMale = false;
                    if ("Y".equalsIgnoreCase(gender)) {
                        isMale = true;
                    }
                    dout.writeBoolean(isMale);
                    dout.writeUTF(rs.getString("patient_citz_sts"));
                    String status = rs.getString("patient_sts");
                    boolean isPatient = false;
                    if ("Y".equalsIgnoreCase(status)) {
                        isPatient = true;
                    }
                    dout.writeBoolean(isPatient);
                    dout.writeUTF(rs.getString("patient_dob"));
                    System.out.println(rs.getString("patient_id") + " - " + rs.getString("patient_name") + " - " + rs.getString("patient_ic") + " - " + rs.getInt("patient_age") + " - " + rs.getString("patient_address") + " - " + rs.getString("patient_phone") + " - " + rs.getString("patient_email") + " - " + rs.getString("patient_pwd") + " - " + rs.getString("patient_splcondtn") + " - " + rs.getString("patient_emgncy_name") + " - " + rs.getString("patient_emgncy_contact") + " - " + rs.getString("patient_nationality") + " - " + String.valueOf(isMale) + " - " + rs.getString("patient_citz_sts") + " - " + String.valueOf(isPatient) + " - " + rs.getString("patient_dob"));
                } while (rs.next());
                dout.writeBoolean(true);
            } else {
                dout.writeUTF("-");
                dout.writeUTF("-");
                dout.writeUTF("-");
                dout.writeInt(-1);
                dout.writeUTF("-");
                dout.writeUTF("-");
                dout.writeUTF("-");
                dout.writeUTF("-");
                dout.writeUTF("-");
                dout.writeUTF("-");
                dout.writeUTF("-");
                dout.writeUTF("-");
                dout.writeBoolean(false);
                dout.writeUTF("-");
                dout.writeBoolean(false);
                dout.writeUTF("-");
                dout.writeBoolean(false);
            }
            dout.flush();
            dout.close();
        } catch (SQLException e) {
            throw new ServletException("Servlet Could not display records.", e);
        } catch (ClassNotFoundException e) {
            throw new ServletException("JDBC Driver not found.", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException e) {
            }
        }
    }

    /**
         * Handles the HTTP <code>GET</code> method.
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
         * Handles the HTTP <code>POST</code> method.
         * @param request servlet request
         * @param response servlet response
         * @throws ServletException if a servlet-specific error occurs
         * @throws IOException if an I/O error occurs
         */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
         * Returns a short description of the servlet.
         * @return a String containing servlet description
         */
    @Override
    public String getServletInfo() {
        return "ListPatient Servlet";
    }
}
