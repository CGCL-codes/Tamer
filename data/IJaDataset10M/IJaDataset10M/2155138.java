package com.sun.bookstore1.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import com.sun.bookstore.cart.ShoppingCart;
import com.sun.bookstore1.database.BookDBAO;

/**
 * An HTTP servlet that responds to the POST method of the HTTP protocol. The
 * Receipt servlet updates the book database inventory, invalidates the user
 * session, thanks the user for the order.
 */
public class ReceiptServlet extends HttpServlet {

    private static final long serialVersionUID = 1004456222268202041L;

    private transient BookDBAO bookDB;

    @Resource
    private transient UserTransaction utx;

    public void init() throws ServletException {
        bookDB = (BookDBAO) getServletContext().getAttribute("bookDB");
        if (bookDB == null) {
            throw new UnavailableException("Couldn't get database.");
        }
    }

    public void destroy() {
        bookDB = null;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean orderCompleted = true;
        HttpSession session = request.getSession(true);
        ResourceBundle messages = (ResourceBundle) session.getAttribute("messages");
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        try {
            utx.begin();
            bookDB.buyBooks(cart);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception e) {
                System.out.println("Rollback failed: " + e.getMessage());
            }
            System.err.println(ex.getMessage());
            orderCompleted = false;
        }
        session.invalidate();
        response.setContentType("text/html");
        response.setBufferSize(8192);
        PrintWriter out = response.getWriter();
        out.println("<html>" + "<head><title>" + messages.getString("TitleReceipt") + "</title></head>");
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/banner");
        if (dispatcher != null) {
            dispatcher.include(request, response);
        }
        if (orderCompleted) {
            out.println("<h3>" + messages.getString("ThankYou") + request.getParameter("cardname") + ".");
        } else {
            out.println("<h3>" + messages.getString("OrderError"));
        }
        out.println("<p> &nbsp; <p><strong><a href=\"" + response.encodeURL(request.getContextPath()) + "/bookstore\">" + messages.getString("ContinueShopping") + "</a> &nbsp; &nbsp; &nbsp;" + "</body></html>");
        out.close();
    }

    public String getServletInfo() {
        return "The Receipt servlet updates the book database inventory, invalidates the user session, " + "thanks the user for the order.";
    }
}
