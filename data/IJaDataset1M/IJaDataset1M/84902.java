package org.jdmp.jetty.coreobject;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jdmp.core.JDMPCoreObject;
import org.jdmp.core.algorithm.HasAlgorithms;
import org.jdmp.core.dataset.HasDataSets;
import org.jdmp.core.module.HasModules;
import org.jdmp.core.module.Module;
import org.jdmp.core.sample.HasSamples;
import org.jdmp.core.variable.HasVariables;
import org.jdmp.jetty.html.Page;
import org.jdmp.jetty.html.tags.BRTag;
import org.jdmp.jetty.html.tags.H1Tag;
import org.jdmp.jetty.html.tags.LinkTag;

public class JettyCoreObjectServlet extends HttpServlet {

    private static final long serialVersionUID = -2077611823499422360L;

    private JDMPCoreObject object = null;

    public JettyCoreObjectServlet(JDMPCoreObject o) {
        this.object = o;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        String path = request.getPathInfo().substring(1);
        String action = null;
        Page page = new Page("Welcome to JDMP");
        page.add(new H1Tag("Welcome to JDMP"));
        page.add("This is a servlet for ");
        page.add(object.getClass().getSimpleName());
        page.add(" [" + object.getLabel() + "]. ");
        page.add(new BRTag());
        page.add("Here is what you can do:");
        page.add(new BRTag());
        page.add(new BRTag());
        page.add(new LinkTag("/", "Home"));
        page.add(new BRTag());
        if (object instanceof Module) {
            page.add(new LinkTag("/console", "Console"));
            page.add(new BRTag());
        }
        if (object instanceof HasModules) {
            page.add(new LinkTag("/modules", "Modules"));
            page.add(new BRTag());
        }
        if (object instanceof HasVariables) {
            page.add(new LinkTag("/variables", "Variables"));
            page.add(new BRTag());
        }
        if (object instanceof HasAlgorithms) {
            page.add(new LinkTag("/algorithms", "Algorithms"));
            page.add(new BRTag());
        }
        if (object instanceof HasDataSets) {
            page.add(new LinkTag("/datasets", "DataSets"));
            page.add(new BRTag());
        }
        if (object instanceof HasSamples) {
            page.add(new LinkTag("/samples", "Samples"));
            page.add(new BRTag());
        }
        page.add(new BRTag());
        out.append(page.toString());
        out.close();
    }
}
