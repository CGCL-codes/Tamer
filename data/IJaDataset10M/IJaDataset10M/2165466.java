package ofc4j.gallery;

import groovy.lang.GroovyShell;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ofc4j.OFC;
import ofc4j.OFCException;
import ofc4j.model.Chart;
import org.codehaus.groovy.control.CompilationFailedException;

public class GalleryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String IMPORTS = "import ofc4j.*;\nimport ofc4j.model.*;\nimport ofc4j.model.elements.*;\nimport ofc4j.model.axis.*;" + "\nimport ofc4j.model.axis.Label.Rotation;\nimport ofc4j.model.elements.HorizontalBarChart.Bar;\nimport ofc4j.model.elements.PieChart.Slice;" + "\nimport ofc4j.model.elements.ScatterChart.Point;\nimport ofc4j.model.elements.FilledBarChart;\nimport ofc4j.model.elements.SketchBarChart;" + "\nimport ofc4j.model.elements.StackedBarChart;\nimport ofc4j.model.elements.StackedBarChart.StackValue;\n\n";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        File f = new File(".", req.getRequestURI().replaceFirst("chart", "gallery") + ".groovy");
        boolean showSource = req.getParameter("source") != null;
        boolean renderPretty = req.getParameter("pretty") != null;
        if (f.exists()) {
            GroovyShell sh = new GroovyShell();
            DataInputStream dis = null;
            try {
                byte[] data = new byte[(int) f.length()];
                dis = new DataInputStream(new FileInputStream(f));
                dis.readFully(data);
                if (showSource) {
                    resp.getWriter().write(simpleEscape(new String(data)));
                } else {
                    String script = IMPORTS + new String(data);
                    Object o = sh.evaluate(script);
                    if (Chart.class.isAssignableFrom(o.getClass())) {
                        if (renderPretty) {
                            resp.getWriter().write(simpleEscape(OFC.getInstance().prettyPrint((Chart) o, 2)));
                        } else {
                            resp.getWriter().write(OFC.getInstance().render((Chart) o));
                        }
                    } else {
                        resp.getWriter().write(o.toString());
                    }
                }
            } catch (CompilationFailedException cfe) {
                cfe.printStackTrace(resp.getWriter());
            } catch (OFCException ofce) {
                System.err.println("Error rendering Chart:");
                ofce.getCause().printStackTrace(System.err);
                System.err.println("JSON output:");
                System.err.println(ofce.getMessage());
                resp.sendError(500, "Error rendering Chart");
            } finally {
                if (dis != null) dis.close();
            }
        } else {
            System.err.println("Requested file does not exist: " + f.getAbsolutePath());
            resp.sendError(404);
        }
    }

    private String simpleEscape(String s) {
        return s.replace("<", "&lt;").replace(">", "&gt;");
    }
}
