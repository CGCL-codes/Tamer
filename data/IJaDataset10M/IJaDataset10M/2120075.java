package net.sourceforge.olduvai.lrac.drawer.templates;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.olduvai.lrac.LiveRAC;
import net.sourceforge.olduvai.lrac.logging.LogEntry;
import net.sourceforge.olduvai.lrac.util.Util;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Note, there is no good reason to keep this class in memory for running LiveRAC.  
 * It can load the templates and then return a template list.  It is required to 
 * stay in memory only for the template manager application.    
 * 
 * @author Peter McLachlan <spark343@cs.ubc.ca>
 *
 */
public class TemplateHandler {

    public static final String DEFAULTTEMPLATEFILE = "templates.xml";

    /**
	 * List of bundles
	 */
    HashMap<String, Template> templateList;

    Document doc = null;

    public TemplateHandler(InputStream bundleFile) {
        readBundles(bundleFile);
    }

    public TemplateHandler(File bundleFile) {
        FileInputStream f;
        try {
            f = new FileInputStream(bundleFile);
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + bundleFile);
            return;
        }
        readBundles(f);
    }

    public HashMap<String, Template> getTemplateMap() {
        return templateList;
    }

    private void readBundles(InputStream bundleFile) {
        SAXBuilder builder = new SAXBuilder();
        try {
            doc = builder.build(bundleFile);
        } catch (JDOMException e1) {
            System.err.println("TemplateHandler: Error parsing template file.");
            e1.printStackTrace();
            return;
        } catch (IOException e1) {
            System.err.println("TemplateHandler: I/O error parsing template file.");
            e1.printStackTrace();
            return;
        }
        templateList = new HashMap<String, Template>();
        Element root = doc.getRootElement();
        List templates = root.getChildren("template");
        Iterator it = templates.iterator();
        Element e;
        while (it.hasNext()) {
            e = (Element) it.next();
            addTemplate(e);
        }
    }

    /**
	 * Creates a new basic template from defaults add adds it to the XML DOM.
	 *
	 */
    public Template newTemplate() {
        final String newTemplateName = "New template";
        Element newTemplate = new Element("template");
        Element templateName = new Element("templatename");
        templateName.setText(newTemplateName);
        newTemplate.addContent(templateName);
        return newTemplate(newTemplate);
    }

    /**
	 * Creates a new basic Template object from the JDOM Element n and adds it to the XML DOM. 
	 * 
	 * @param n Contains valid Template tags.  
	 * @see Template
	 * 
	 * @return
	 */
    public Template newTemplate(Element n) {
        if (!n.getName().equals("template")) {
            System.err.println("BundleLoader.newTemplate invalid template node");
            return null;
        }
        doc.getRootElement().addContent(n);
        return addTemplate(n);
    }

    /**
	 * Adds the template to the Template list.  Note: for data consistency 
	 * between the JDOM data model and the Java object model, the new Template should 
	 * already be in the JDOM when this is called! 
	 * 
	 * Externally, newTemplate is used to ensure this is the case.  
	 * 
	 * @param n
	 */
    protected Template addTemplate(Element n) {
        Template b = new Template(n);
        templateList.put(b.templateName, b);
        return b;
    }

    /**
	 * Removes a template from the Java object model and the JDOM model
	 * @param t
	 */
    public void delTemplate(Template t) {
        t.rootElement.getParent().removeContent(t.rootElement);
        templateList.remove(t.getTemplateName());
    }

    /**
	 * Saves all templates
	 * @param fileName File to save the templates to
	 */
    public void saveTemplates(File fileName) {
        LiveRAC.makeLogEntry(LogEntry.TEMPLATES_SAVED, "Templates saved", templateList);
        XMLOutputter outputter = new XMLOutputter();
        try {
            outputter.output(doc, new FileWriter(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("TemplateHandler: Error writing template XML file");
        }
    }

    public static void main(String[] args) {
        TemplateHandler loader = new TemplateHandler(new File(DEFAULTTEMPLATEFILE));
        loader.toString();
    }

    /**
	 * Sets the H S V values of the XML color in colorElement based 
	 * on the Java Color object
	 * 
	 * @param c Java color object
	 * @param colorElement XML color element
	 */
    static final Element makeColorXML(Color c) {
        float[] compArray = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), compArray);
        Element h = new Element("H");
        h.setText(Float.toString(compArray[0]));
        Element s = new Element("S");
        s.setText(Float.toString(compArray[1]));
        Element v = new Element("V");
        v.setText(Float.toString(compArray[2]));
        Element color = new Element("color");
        color.addContent(h).addContent(s).addContent(v);
        return color;
    }

    /**
	 * 
	 * @param n
	 * @return
	 */
    static final Color getColorFromXML(Element n) {
        float h = 0;
        float s = 0;
        float v = 0;
        List nl = n.getChildren();
        Element current;
        Iterator it = nl.iterator();
        while (it.hasNext()) {
            current = (Element) it.next();
            if (current.getName().equals("H")) {
                h = Float.parseFloat(current.getText());
            } else if (current.getName().equals("S")) {
                s = Float.parseFloat(current.getText());
            } else if (current.getName().equals("V")) {
                v = Float.parseFloat(current.getText());
            }
        }
        return Color.getHSBColor(h, s, v);
    }

    public HashMap<String, Template> getTemplateList() {
        return templateList;
    }

    public static final InputStream getTemplateFileStream(ClassLoader cl) {
        final String defaultTemplateFile = LiveRAC.getSelectedProfileSystemPath() + DEFAULTTEMPLATEFILE;
        final String defaultUserTemplateFile = LiveRAC.getSelectedProfileUserPath() + DEFAULTTEMPLATEFILE;
        return Util.getFileStream(cl, defaultTemplateFile, defaultUserTemplateFile, LiveRAC.loadProfileDefault);
    }
}
