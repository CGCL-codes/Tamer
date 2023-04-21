package com.integrationpath.mengine.console;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class ConsoleServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String command = req.getParameter("command");
        if (command.equals("list")) {
            list(req, resp);
        }
        if (command.equals("listAsync")) {
            listAsync(req, resp);
        }
        if (command.equals("start")) {
            start(req, resp, new Long(req.getParameter("bundle")));
        }
        if (command.equals("stop")) {
            stop(req, resp, new Long(req.getParameter("bundle")));
        }
        if (command.equals("startAsync")) {
            startAsync(req, resp, new Long(req.getParameter("bundle")));
        }
        if (command.equals("stopAsync")) {
            stopAsync(req, resp, new Long(req.getParameter("bundle")));
        }
        if (command.equals("services")) {
            services(req, resp);
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("plugins", Activator.getBundles());
        resp.sendRedirect(resp.encodeRedirectURL("/bridge/console/console.jsp"));
    }

    private void listAsync(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("plugins", Activator.getBundles());
        req.getSession().setAttribute("return", req.getParameter("return"));
        resp.sendRedirect(resp.encodeRedirectURL("/bridge/console/consoleAsync.jsp"));
    }

    private void services(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("services", Activator.getServices());
        resp.sendRedirect(resp.encodeRedirectURL("/bridge/console/plugins.jsp"));
    }

    private void start(HttpServletRequest req, HttpServletResponse resp, Long bundle) throws ServletException, IOException {
        Bundle currentbundle = Activator.getBundleContext().getBundle(bundle);
        try {
            currentbundle.start();
        } catch (BundleException e) {
            e.printStackTrace();
        }
        list(req, resp);
    }

    private void stop(HttpServletRequest req, HttpServletResponse resp, Long bundle) throws ServletException, IOException {
        Bundle currentbundle = Activator.getBundleContext().getBundle(bundle);
        try {
            currentbundle.stop();
        } catch (BundleException e) {
            e.printStackTrace();
        }
        list(req, resp);
    }

    private void startAsync(HttpServletRequest req, HttpServletResponse resp, Long bundle) throws ServletException, IOException {
        Bundle currentbundle = Activator.getBundleContext().getBundle(bundle);
        try {
            currentbundle.start();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }

    private void stopAsync(HttpServletRequest req, HttpServletResponse resp, Long bundle) throws ServletException, IOException {
        Bundle currentbundle = Activator.getBundleContext().getBundle(bundle);
        try {
            currentbundle.stop();
        } catch (BundleException e) {
            e.printStackTrace();
        }
    }
}
