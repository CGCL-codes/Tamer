package com.mat.http.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.be.bo.UserObject;
import com.core.util.Booking;
import com.mat.bo.OrderBO;
import com.mat.bo.BookBO;

public class MatProcess extends HttpServlet {

    private static final long serialVersionUID = -8959042594029318748L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    @SuppressWarnings("unchecked")
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("com.mat.http.servlets.doPost: ");
        HttpSession session = request.getSession();
        UserObject uo = (UserObject) session.getAttribute("userObject");
        if (request.getParameter("book") != null) {
            System.out.println("Booking");
            Booking booking = new Booking(uo);
            if (request.getParameter("itemList") != null) {
                String[] temp = request.getParameterValues("itemList");
                if (temp != null && temp.length > 0) {
                    for (int i = 0; i < temp.length; i++) {
                        BookBO bo = new BookBO(uo.getConnectionPropertiesVO(), uo);
                        bo.book(Integer.parseInt(temp[i]), booking.getSpecialLedger(Booking.matDefaultStockReceiptDebit), booking.getSpecialLedger(Booking.matDefaultStockReceiptCredit), booking.getBookText(Booking.matBookText));
                    }
                }
            }
        } else if (request.getParameter("delete") != null) {
            System.out.println("deleting");
            if (request.getParameter("itemList") != null) {
                String[] temp = request.getParameterValues("itemList");
                if (temp != null && temp.length > 0) {
                    for (int i = 0; i < temp.length; i++) {
                        com.mat.bo.OrderBO bo = new OrderBO(uo.getConnectionPropertiesVO(), uo);
                        bo.delete(Integer.parseInt(temp[i]));
                    }
                }
            }
        } else if (request.getParameter("update") != null) {
            System.out.println("updating");
            if (request.getParameter("itemList") != null) {
                String[] temp = request.getParameterValues("itemList");
                if (temp != null && temp.length > 0) {
                    for (int i = 0; i < temp.length; i++) {
                        System.out.println("Update " + Integer.parseInt(temp[i]));
                        BookBO bo = new BookBO(uo.getConnectionPropertiesVO(), uo);
                        bo.updateInventory(Integer.parseInt(temp[i]));
                    }
                }
            }
        } else if (request.getParameter("action") != null) {
            System.out.println("Just Actions:");
        }
        if (request.getParameter("print") != null) {
            if (request.getParameter("itemList") != null) {
                String[] temp = request.getParameterValues("itemList");
                if (temp != null && temp.length > 0) {
                    for (int i = 0; i < temp.length; i++) {
                        System.out.println("Printing order: " + temp[i]);
                    }
                }
            }
        }
        if (request.getParameter("email") != null) {
            if (request.getParameter("itemList") != null) {
                String[] temp = request.getParameterValues("itemList");
                if (temp != null && temp.length > 0) {
                    for (int i = 0; i < temp.length; i++) {
                        System.out.println("Sending Email for order: " + temp[i]);
                    }
                }
            }
        }
        response.sendRedirect("index.jsp?" + request.getParameter("forward") + "&action=reload");
    }
}
