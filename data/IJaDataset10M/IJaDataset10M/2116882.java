package com.joebertj.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.joebertj.helper.DatabaseConnection;

/**
 * Servlet implementation class for Servlet: EditPawn
 *
 */
public class EditPawn extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 57476656666682783L;

    public EditPawn() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (session.isNew()) {
            String redirectURL = "common/login.jsp";
            response.sendRedirect(redirectURL);
        } else {
            String authenticated = (String) session.getAttribute("authenticated");
            if (authenticated == null) {
                String redirectURL = "common/login.jsp";
                response.sendRedirect(redirectURL);
            } else {
                continuePost(request, response);
            }
        }
    }

    protected void continuePost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int pid = new Integer(request.getParameter("pid")).intValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            long now = new java.util.Date().getTime();
            String cdate = request.getParameter("cdate");
            long lcdate = sdf.parse(cdate).getTime();
            if ((now - lcdate) / (1000 * 60) <= 15) {
                HttpSession session = request.getSession(true);
                String encoder = (String) session.getAttribute("authenticated");
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = null;
                float interest = new Float(request.getParameter("interest")).floatValue();
                float loan = new Float(request.getParameter("loanamt")).floatValue();
                float appraised = new Integer(request.getParameter("appamt")).intValue();
                String description = request.getParameter("desc");
                float serviceCharge = new Float(request.getParameter("service")).floatValue();
                int nameid = -1;
                pstmt = conn.prepareStatement("SELECT id FROM customer WHERE last_name=? AND first_name=? AND middle_name=?");
                String lname = request.getParameter("lname");
                String fname = request.getParameter("fname");
                String mname = request.getParameter("mname");
                pstmt.setString(1, lname);
                pstmt.setString(2, fname);
                pstmt.setString(3, mname);
                ResultSet rs = pstmt.executeQuery();
                if (rs.first()) {
                    nameid = rs.getInt(1);
                } else {
                    String address = request.getParameter("address");
                    pstmt = conn.prepareStatement("INSERT INTO customer VALUES (0,?,?,?,?,0)");
                    pstmt.setString(1, lname);
                    pstmt.setString(2, fname);
                    pstmt.setString(3, mname);
                    pstmt.setString(4, address);
                    pstmt.executeUpdate();
                    pstmt = conn.prepareStatement("SELECT id FROM customer WHERE last_name=? AND first_name=? AND middle_name=?");
                    pstmt.setString(1, lname);
                    pstmt.setString(2, fname);
                    pstmt.setString(3, mname);
                    rs = pstmt.executeQuery();
                    if (rs.first()) {
                        nameid = rs.getInt(1);
                    }
                }
                pstmt = conn.prepareStatement("UPDATE pawn set nameid=?, loan=?, appraised=?, description=?, service_charge=?, advance_interest=?, encoder=? WHERE pid=?");
                pstmt.setInt(1, nameid);
                pstmt.setFloat(2, loan);
                pstmt.setFloat(3, appraised);
                pstmt.setString(4, description);
                pstmt.setFloat(5, serviceCharge);
                pstmt.setFloat(6, interest);
                pstmt.setString(7, encoder);
                pstmt.setLong(8, pid);
                pstmt.executeUpdate();
                response.sendRedirect("pawndetailpdf.jsp?pid=" + pid + "&msg=Pawn with pid " + pid + " successfully updated");
            } else {
                response.sendRedirect("pawnedit.jsp?pid=" + pid + "&msg=Allowable time of pawn with pid " + pid + " expired");
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
