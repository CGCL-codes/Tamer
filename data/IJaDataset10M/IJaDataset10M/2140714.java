package de.cgarbs.apps.jprojecttimer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import java.awt.Color;
import java.lang.Integer;

/** @author Christian Garbs <mitch@cgarbs.de>
 * @author Boris Unckel <b.unckel@gmx.net>
 * @version $Id: Project.java,v 1.6 2002/11/12 18:57:58 mastermitch Exp $
 */
public class Project {

    File file;

    String name;

    String author;

    GanttDesign ganttDesign = new GanttDesign();

    int timeScale;

    boolean anchored;

    Date start;

    TaskList tasks = new TaskList();

    boolean needsToBeSaved;

    public Project() {
        clear();
    }

    public void clear() {
        tasks.removeAllElements();
        needsToBeSaved = false;
        name = "";
        timeScale = 0;
        anchored = false;
        start = new Date();
    }

    /**
     * @param file input file to read from
     * @deprecated XML is preferred format: use readFromXML instead.
     */
    public void readFromStream(File file) {
        this.file = file;
        tasks.removeAllElements();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));
            String s = in.readLine();
            if (s.equals("<project>")) {
                in.readLine();
                name = in.readLine();
                timeScale = Integer.parseInt(in.readLine());
                anchored = Boolean.valueOf(in.readLine()).booleanValue();
                start.setTime(Long.parseLong(in.readLine()));
                tasks.readFromStream(in);
            }
            in.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        needsToBeSaved = true;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
        needsToBeSaved = true;
    }

    public Color getGanttColor(int i) {
        return ganttDesign.ganttColor[i];
    }

    public void setGanttColor(int i, Color c) {
        ganttDesign.ganttColor[i] = c;
        needsToBeSaved = true;
    }

    public boolean needsToBeSaved() {
        if (needsToBeSaved) {
            return true;
        }
        if (tasks.needsToBeSaved()) {
            return true;
        }
        return false;
    }

    public void hasBeenSaved() {
        needsToBeSaved = false;
        tasks.hasBeenSaved();
    }

    public TaskList getTaskList() {
        return tasks;
    }

    /**
     * @param out Stream where data is written
     * @deprecated XML is preferred format: use writeToXML instead
     */
    public void writeToStream(PrintStream out) {
        out.println("<project>");
        out.println("1");
        out.println(name);
        out.println(timeScale);
        out.println(anchored);
        out.println(start.getTime());
        tasks.writeToStream(out);
        out.println("</project>");
    }

    /**
     * Read data from XML-input file. Does not check for valid format
     * @param file Input File to read from.
     */
    public void readFromXML(File file) {
        this.file = file;
        if (file.canRead()) {
            tasks.removeAllElements();
            try {
                SAXBuilder aSAXBuilder = new SAXBuilder();
                Document aDocument = aSAXBuilder.build(file);
                Element root = aDocument.getRootElement();
                if (root.getName().equalsIgnoreCase("project")) {
                    name = root.getChild("name").getText();
                    author = root.getChild("author").getText();
                    for (int i = 0; i < GanttDesign.NBCOLORS; i++) {
                        ganttDesign.ganttColor[i] = new Color(Integer.parseInt(root.getChild(GanttDesign.colorTechName[i]).getText()));
                    }
                    timeScale = Integer.parseInt(root.getChild("timeScale").getText());
                    anchored = Boolean.valueOf(root.getChild("anchored").getText()).booleanValue();
                    start.setTime(Long.parseLong(root.getChild("startTime").getText()));
                    Element dependencies = root.getChild("tasklist");
                    if (dependencies != null) tasks.readFromXml(dependencies);
                } else throw new Exception("Input XML false. Needs to be project format");
            } catch (Exception e) {
                System.err.println(e);
            }
        } else System.err.println("File not readable. Check input format for XML and project type");
    }

    /** @param out Stream where XML is written to
     *  @throws IOException if a problem with the stream occurs */
    public void writeToXML(PrintStream out) throws IOException {
        XMLOutputter aXMLOutputter = new XMLOutputter(" ", true);
        Element root = new Element("project");
        root.setAttribute("version", "1.0");
        Document aDocument = new Document(root);
        root.addContent(new Element("name").addContent(name));
        root.addContent(new Element("author").addContent(author));
        for (int i = 0; i < GanttDesign.NBCOLORS; i++) {
            root.addContent(new Element(GanttDesign.colorTechName[i]).addContent(ganttDesign.ganttColor[i].getRGB() + ""));
        }
        root.addContent(new Element("timeScale").addContent(timeScale + ""));
        root.addContent(new Element("anchored").addContent(anchored + ""));
        root.addContent(new Element("startTime").addContent(start.getTime() + ""));
        tasks.addToXmlElement(root);
        aXMLOutputter.output(aDocument, out);
    }
}
