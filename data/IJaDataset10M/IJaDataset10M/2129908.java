package com.Team4.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.Team4.model.Item;
import com.Team4.model.Items;

/**
 * Servlet implementation class for Servlet: LoginServlet
 *
 */
public class ItemsServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    public ItemsServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String message = null;
        String catagory = request.getParameter("catagory");
        ArrayList items;
        Item item;
        try {
            if (catagory == null) {
                items = new Items().getRandomItems();
                message = "<table><tr>";
                for (int i = 0; i < items.size(); i++) {
                    item = (Item) items.get(i);
                    if (i % 3 == 0) {
                        message += "</tr><tr>";
                    }
                    message += "<td>Item Code: " + item.getId() + "<br>" + "Name: " + item.getName() + "<br> " + "Discription: " + item.getDisc() + "<br>" + "Price: " + item.getPrice() + "  Remaining: " + item.getQuantity() + " </td>";
                }
                message += "</tr></table>";
                System.err.println(message);
            } else {
                message = "Invalid user id or password";
            }
        } catch (Exception e) {
            message = "A system error occurred";
            e.printStackTrace();
        }
        PrintWriter out = response.getWriter();
        out.print(message);
    }
}
