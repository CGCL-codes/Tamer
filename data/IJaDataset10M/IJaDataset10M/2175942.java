package hsu.basis.web;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasisGateWay extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        process(request, response);
    }

    public void process(HttpServletRequest request, HttpServletResponse respoinse) {
    }
}
