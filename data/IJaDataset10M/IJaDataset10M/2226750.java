package com.sun.bookstore3.dispatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import com.sun.bookstore.cart.ShoppingCart;
import com.sun.bookstore.database.Book;
import com.sun.bookstore.exception.BookNotFoundException;
import com.sun.bookstore3.database.BookDBAO;

public class Dispatcher extends HttpServlet {

    private static final long serialVersionUID = -2340820669684283725L;

    @Resource
    private transient UserTransaction utx;

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String bookId = null;
        Book book = null;
        String clear = null;
        BookDBAO bookDBAO = (BookDBAO) getServletContext().getAttribute("bookDBAO");
        HttpSession session = request.getSession();
        String selectedScreen = request.getServletPath();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute("cart", cart);
        }
        if (selectedScreen.equals("/bookcatalog")) {
            bookId = request.getParameter("Add");
            if (!bookId.equals("")) {
                try {
                    book = bookDBAO.getBook(bookId);
                    if (book.getOnSale()) {
                        double sale = book.getPrice() * .85;
                        Float salePrice = new Float(sale);
                        book.setPrice(salePrice.floatValue());
                    }
                    cart.add(bookId, book);
                } catch (BookNotFoundException ex) {
                }
            }
        } else if (selectedScreen.equals("/bookshowcart")) {
            bookId = request.getParameter("Remove");
            if (bookId != null) {
                cart.remove(bookId);
            }
            clear = request.getParameter("Clear");
            if ((clear != null) && clear.equals("clear")) {
                cart.clear();
            }
        } else if (selectedScreen.equals("/bookreceipt")) {
            try {
                utx.begin();
                bookDBAO.buyBooks(cart);
                utx.commit();
            } catch (Exception ex) {
                try {
                    utx.rollback();
                    request.getRequestDispatcher("/bookordererror.jsp").forward(request, response);
                } catch (Exception e) {
                    System.out.println("Rollback failed: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        try {
            request.getRequestDispatcher("/template/template.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("selectedScreen", request.getServletPath());
        try {
            request.getRequestDispatcher("/template/template.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
