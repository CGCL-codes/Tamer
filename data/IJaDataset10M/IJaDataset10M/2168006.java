package net.sourceforge.hourglass.framework;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.jdom.output.XMLOutputter;

/**
 * Writes a project to an output stream.
 *
 * @author Mike Grant
 */
public class ProjectWriter {

    /**
   * Creates a ProjectWriter on the given output stream.
   */
    public ProjectWriter(OutputStream os) {
        try {
            _writer = new PrintWriter(new OutputStreamWriter(os, "UTF-8"), NO_AUTOFLUSH);
        } catch (UnsupportedEncodingException e1) {
            getLogger().error("Couldn't create UTF-8 output writer.", e1);
            _writer = new PrintWriter(os, NO_AUTOFLUSH);
        }
        _dateFormat = Utilities.getInstance().createDateFormat();
        _xmlOutputter = new XMLOutputter();
        try {
            initializeProperties();
        } catch (ParseException e) {
            getLogger().error("Parse exception initializing properties.", e);
        } catch (IOException e) {
            getLogger().error("IOException initializing properties.", e);
        }
        writeHeader(_writer);
    }

    private void initializeProperties() throws IOException, ParseException {
        Properties p = new Properties();
        p.load(getClass().getClassLoader().getResourceAsStream("net/sourceforge/hourglass/build.properties"));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
        _buildDate = df.parse((String) p.get("hourglass.build.time"));
        _version = (String) p.get("hourglass.version");
    }

    /**
   * Writes the XML header to the given writer.
   */
    private void writeHeader(PrintWriter writer) {
        writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        writer.print("<hourglass xmlns=\"http://hourglass.sourceforge.net/xml/hourglass\"");
        writer.print(" version=\"");
        writer.print(_version);
        writer.print("\" buildDate=\"");
        writer.print(_dateFormat.format(_buildDate));
        writer.println("\">");
    }

    /**
   * Writes the XML footer to the given writer.
   */
    private void writeFooter(PrintWriter writer) {
        writer.println("</hourglass>");
    }

    /**
   * Write the XML footer, and closes the underlying output stream.
   */
    public void close() throws IOException {
        writeFooter(_writer);
        _writer.close();
    }

    /**
   * Writes the project group in parent-first order.
   */
    public void write(ProjectGroup group) {
        Iterator i = group.getRootProject().getChildren().iterator();
        while (i.hasNext()) {
            Project eachTopLevelProject = (Project) i.next();
            writeRecursive(eachTopLevelProject, group);
        }
    }

    /**
   * Recursively writes the given project, parent-first, using the
   * group to examine the hierarchy.
   */
    private void writeRecursive(Project p, ProjectGroup g) {
        write(p, g);
        Iterator i = p.getChildren().iterator();
        while (i.hasNext()) {
            Project eachChild = (Project) i.next();
            writeRecursive(eachChild, g);
        }
    }

    /**
   * Writes the given project to the underlying output stream.
   *
   * @param p the project to write
   * @param g the group to which the project belongs (used for
   *            hierarchy exploration--not written to stream).
   */
    private void write(Project p, ProjectGroup g) {
        _writer.print("<project name=\"" + _xmlOutputter.escapeAttributeEntities(p.getName()) + "\" id=\"" + p.getId() + "\" ");
        if (g.getParent(p) != null && !g.getParent(p).equals(g.getRootProject())) {
            _writer.print("parent=\"" + g.getParent(p).getId() + "\" ");
        }
        _writer.println(">");
        _writer.print("<description>");
        getLogger().debug("Description: " + p.getDescription());
        _writer.print(_xmlOutputter.escapeElementEntities(p.getDescription()));
        _writer.println("</description>");
        Iterator i = p.getTimeSpans().iterator();
        while (i.hasNext()) {
            writeTimeSpan(_writer, (TimeSpan) i.next());
        }
        writeAttributes(_writer, p);
        _writer.println("</project>");
    }

    /**
   * Writes the given timespan to the writer.
   */
    private void writeTimeSpan(PrintWriter writer, TimeSpan span) {
        writer.print("<timespan start=\"");
        writer.print(_dateFormat.format(span.getStartDate()));
        writer.print("\" end=\"");
        writer.print(_dateFormat.format(span.getEndDate()));
        writer.println("\" />");
    }

    private void writeAttributes(PrintWriter writer, Project p) {
        Iterator i = p.getAttributeDomains();
        while (i.hasNext()) {
            String eachDomain = (String) i.next();
            Iterator j = p.getAttributeKeys(eachDomain);
            while (j.hasNext()) {
                String eachName = (String) j.next();
                String eachValue = p.getAttribute(eachDomain, eachName);
                writeAttribute(writer, eachDomain, eachName, eachValue);
            }
        }
    }

    private void writeAttribute(PrintWriter writer, String domain, String name, String value) {
        writer.print("<attribute domain=\"");
        writer.print(_xmlOutputter.escapeAttributeEntities(domain));
        writer.print("\" name=\"");
        writer.print(_xmlOutputter.escapeAttributeEntities(name));
        writer.print("\" value=\"");
        writer.print(_xmlOutputter.escapeAttributeEntities(value));
        writer.println("\" />");
    }

    private Logger getLogger() {
        if (_logger == null) {
            _logger = Logger.getLogger(getClass());
        }
        return _logger;
    }

    private Logger _logger;

    private PrintWriter _writer;

    private DateFormat _dateFormat;

    private XMLOutputter _xmlOutputter;

    private static boolean NO_AUTOFLUSH = false;

    private Date _buildDate;

    private String _version;
}
