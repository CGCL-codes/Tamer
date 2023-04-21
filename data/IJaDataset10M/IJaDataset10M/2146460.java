package org.corrib.s3b.sscf.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ServletTest extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String result;
        String strDirectoy = "/notitio.us/notitio_quest";
        if ((result = request.getParameter("person")) != null) {
            try {
                (new File(strDirectoy)).mkdir();
                BufferedWriter skad = new BufferedWriter(new FileWriter("/notitio.us/notitio_quest/" + result + ".txt"));
                Date now = new Date();
                skad.write("Date of the document:");
                result = now.toString();
                skad.newLine();
                skad.write(result);
                skad.newLine();
                result = request.getParameter("person");
                skad.write("PERSON:");
                skad.newLine();
                skad.write(result);
                skad.newLine();
                if ((result = request.getParameter("email")) != null) {
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("credential")) != null) {
                    skad.write("Would you willing to leave your credentials with us?:");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                } else {
                    out.println("QPA wyszla");
                    out.println(result);
                }
                if ((result = request.getParameter("tag")) != null) {
                    skad.write("How do you find this organization of tags ( are the similar tags clustered together )");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                } else out.println("TAG QPA");
                if ((result = request.getParameter("clustering")) != null) {
                    skad.write("Should be the clustering stronger, but more inaccurate?");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("system")) != null) {
                    skad.write("Do you use some on-line bookmark management system?");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("thesauri")) != null) {
                    skad.write("What other established controlled vocabularies (thesauri, taxonomies) would you like to use in the system?");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("answer")) != null) {
                    skad.write("What kind of features you miss?");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("filtring")) != null) {
                    skad.write("Do we need bookmarks filtering?");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("update")) != null) {
                    skad.write("Handling del.icio.us updates");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("sex")) != null) {
                    skad.write("Sex");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("age")) != null) {
                    skad.write("Age");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("education")) != null) {
                    skad.write("Education:");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("knowledge")) != null) {
                    skad.write("IT Knowledge:");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                if ((result = request.getParameter("interests")) != null) {
                    skad.write("Interests:");
                    skad.newLine();
                    skad.write(result);
                    skad.newLine();
                }
                result = request.getParameter("person");
                response.setContentType("text/html");
                skad.close();
                getServletContext().getRequestDispatcher("/Thanks.jsp").forward(request, response);
                System.out.println((int) new Integer('!'));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
